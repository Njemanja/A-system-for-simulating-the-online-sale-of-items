CREATE TABLE [Artikal]
( 
	[IDA]                integer  IDENTITY  NOT NULL ,
	[IDP]                integer  NOT NULL ,
	[Cena]               integer  NULL ,
	[Kolicina]           integer  NULL ,
	[Naziv]              varchar(100)  NULL 
)
go

CREATE TABLE [Grad]
( 
	[IDG]                integer  IDENTITY  NOT NULL ,
	[Naziv]              varchar(100)  NULL 
)
go

CREATE TABLE [Konekcija]
( 
	[IDKonekcija]        integer  IDENTITY  NOT NULL ,
	[Tezina]             integer  NULL ,
	[IDG1]               integer  NULL ,
	[IDG2]               integer  NULL 
)
go

CREATE TABLE [Korpa]
( 
	[IDKorpa]            integer  IDENTITY  NOT NULL ,
	[Kolicina]           integer  NULL ,
	[IDA]                integer  NULL ,
	[IDPorudzbine]       integer  NULL 
)
go

CREATE TABLE [Kupac]
( 
	[IDK]                integer  IDENTITY  NOT NULL ,
	[Ime]                varchar(100)  NULL ,
	[Prezime]            varchar(100)  NULL ,
	[Novac]              integer  NULL ,
	[IDG]                integer  NOT NULL 
)
go

CREATE TABLE [Porudzbina]
( 
	[IDK]                integer  NOT NULL ,
	[IDPorudzbine]       integer  IDENTITY  NOT NULL ,
	[Status]             varchar(20)  NULL ,
	[Cena]               integer  NULL ,
	[VremeSlanja]        datetime  NULL ,
	[VremeDolaska]       datetime  NULL ,
	[Lokacija]           integer  NULL 
)
go

CREATE TABLE [Prodavnica]
( 
	[IDP]                integer  IDENTITY  NOT NULL ,
	[Popust]             decimal(10,3)  NULL ,
	[PocetakPopusta]     datetime  NULL ,
	[IDG]                integer  NOT NULL ,
	[KrajPopusta]        datetime  NULL ,
	[ImeProdavnice]      char(18)  NULL 
)
go

CREATE TABLE [SYSTEM]
( 
	[IDS]                integer  IDENTITY  NOT NULL ,
	[Proift]             integer  NULL 
)
go

CREATE TABLE [Transakcija]
( 
	[IDT]                integer  IDENTITY  NOT NULL ,
	[Cena]               integer  NULL ,
	[Vreme]              datetime  NULL ,
	[Status]             varchar(100)  NULL ,
	[IDP]                integer  NULL ,
	[IDPorudzbine]       integer  NULL ,
	[IDK]                integer  NULL 
)
go

ALTER TABLE [Artikal]
	ADD CONSTRAINT [XPKArtikal] PRIMARY KEY  CLUSTERED ([IDA] ASC)
go

ALTER TABLE [Grad]
	ADD CONSTRAINT [XPKGrad] PRIMARY KEY  CLUSTERED ([IDG] ASC)
go

ALTER TABLE [Konekcija]
	ADD CONSTRAINT [XPKKonekcija] PRIMARY KEY  CLUSTERED ([IDKonekcija] ASC)
go

ALTER TABLE [Korpa]
	ADD CONSTRAINT [XPKKorpa] PRIMARY KEY  CLUSTERED ([IDKorpa] ASC)
go

ALTER TABLE [Kupac]
	ADD CONSTRAINT [XPKKupac] PRIMARY KEY  CLUSTERED ([IDK] ASC)
go

ALTER TABLE [Porudzbina]
	ADD CONSTRAINT [XPKPorudzbina] PRIMARY KEY  CLUSTERED ([IDPorudzbine] ASC)
go

ALTER TABLE [Prodavnica]
	ADD CONSTRAINT [XPKProdavnica] PRIMARY KEY  CLUSTERED ([IDP] ASC)
go

ALTER TABLE [SYSTEM]
	ADD CONSTRAINT [XPKSYSTEM] PRIMARY KEY  CLUSTERED ([IDS] ASC)
go

ALTER TABLE [Transakcija]
	ADD CONSTRAINT [XPKTranskacijaProdavnica] PRIMARY KEY  CLUSTERED ([IDT] ASC)
go


ALTER TABLE [Artikal]
	ADD CONSTRAINT [R_1] FOREIGN KEY ([IDP]) REFERENCES [Prodavnica]([IDP])
		ON DELETE CASCADE
		ON UPDATE CASCADE
go


ALTER TABLE [Konekcija]
	ADD CONSTRAINT [R_7] FOREIGN KEY ([IDG1]) REFERENCES [Grad]([IDG])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Konekcija]
	ADD CONSTRAINT [R_8] FOREIGN KEY ([IDG2]) REFERENCES [Grad]([IDG])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Korpa]
	ADD CONSTRAINT [R_9] FOREIGN KEY ([IDA]) REFERENCES [Artikal]([IDA])
		ON DELETE CASCADE
		ON UPDATE CASCADE
go

ALTER TABLE [Korpa]
	ADD CONSTRAINT [R_10] FOREIGN KEY ([IDPorudzbine]) REFERENCES [Porudzbina]([IDPorudzbine])
		ON DELETE CASCADE
		ON UPDATE CASCADE
go


ALTER TABLE [Kupac]
	ADD CONSTRAINT [R_5] FOREIGN KEY ([IDG]) REFERENCES [Grad]([IDG])
		ON DELETE CASCADE
		ON UPDATE CASCADE
go


ALTER TABLE [Porudzbina]
	ADD CONSTRAINT [R_3] FOREIGN KEY ([IDK]) REFERENCES [Kupac]([IDK])
		ON DELETE CASCADE
		ON UPDATE CASCADE
go


ALTER TABLE [Prodavnica]
	ADD CONSTRAINT [R_61] FOREIGN KEY ([IDG]) REFERENCES [Grad]([IDG])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Transakcija]
	ADD CONSTRAINT [R_12] FOREIGN KEY ([IDP]) REFERENCES [Prodavnica]([IDP])
		ON DELETE CASCADE
		ON UPDATE CASCADE
go

ALTER TABLE [Transakcija]
	ADD CONSTRAINT [R_14] FOREIGN KEY ([IDPorudzbine]) REFERENCES [Porudzbina]([IDPorudzbine])
		ON DELETE CASCADE
		ON UPDATE CASCADE
go

ALTER TABLE [Transakcija]
	ADD CONSTRAINT [R_15] FOREIGN KEY ([IDK]) REFERENCES [Kupac]([IDK])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go
