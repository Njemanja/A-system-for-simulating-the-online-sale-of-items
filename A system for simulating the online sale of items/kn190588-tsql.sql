CREATE TRIGGER TR_TRANSFER_MONEY_TO_SHOP 
   ON  Porudzbina
   AFTER UPDATE
AS 
BEGIN
		DECLARE @imaPopust INT, @Profit FLOAT
		DECLARE @IDP INT, @IDPorudzbine1 INT

		IF (SELECT COUNT(*) FROM porudzbina
			WHERE IDPorudzbine = 1 AND cena > 100 AND DATEDIFF(day, VremeSlanja, GETDATE()) < 30) > 0
		BEGIN
			SET @imaPopust = 1
		END
		IF @imaPopust=1
		BEGIN
			DECLARE @IDPorudzbine INT
			IF UPDATE(Status) 
			BEGIN
			DECLARE statusCursor CURSOR FOR
			SELECT
            i.IDPorudzbine
			FROM
				inserted i
				JOIN deleted d ON i.IDPorudzbine = d.IDPorudzbine
			WHERE
				i.Status <> d.Status
				AND d.Status = 'sent'
				AND i.Status = 'arrived'
			
			OPEN statusCursor        
			FETCH NEXT FROM statusCursor INTO @IDPorudzbine
			WHILE @@FETCH_STATUS = 0
			BEGIN
				PRINT 'IDPORUDZBINE= '+CAST(@IDPorudzbine AS VARCHAR)
				SELECT @Profit=sum(Artikal.Cena*Korpa.Kolicina*(100-Popust)/100)
				FROM Prodavnica join Artikal on (Prodavnica.IDP=Artikal.IDP)
								join Korpa on (Artikal.IDA=Korpa.IDA)
								join Porudzbina on (Korpa.IDPorudzbine=Porudzbina.IDPorudzbine)
				--GROUP BY Porudzbina.IDPorudzbine
				UPDATE SYSTEM SET Proift=Proift+(@Profit*0.03)
				PRINT @Profit*0.03

				DECLARE kursor CURSOR FOR
				SELECT Prodavnica.IDP,sum(Artikal.Cena*Korpa.Kolicina*(100-Popust)/100), Porudzbina.IDPorudzbine
				FROM Prodavnica join Artikal on (Prodavnica.IDP=Artikal.IDP)
								join Korpa on (Artikal.IDA=Korpa.IDA)
								join Porudzbina on (Korpa.IDPorudzbine=Porudzbina.IDPorudzbine)
				WHERE Porudzbina.IDPorudzbine=@IDPorudzbine
				GROUP BY Prodavnica.IDP, Porudzbina.IDPorudzbine
			
			
				OPEN kursor
        
				FETCH NEXT FROM kursor INTO @IDP, @Profit, @IDPorudzbine1
				WHILE @@FETCH_STATUS = 0
				BEGIN
					print @Profit*0.95
					INSERT INTO Transakcija VALUES(@Profit*0.95, GETDATE(), 'placeno', @IDP, @IDPorudzbine1, NULL)
					FETCH NEXT FROM kursor INTO @IDP, @Profit,@IDPorudzbine1
				END
        
        
				CLOSE kursor
				DEALLOCATE kursor
				END
        
			FETCH NEXT FROM statusCursor INTO @IDPorudzbine
			CLOSE statusCursor
			DEALLOCATE statusCursor


			



		END
		END
		ELSE
		BEGIN
		IF UPDATE(Status) 
		BEGIN
			DECLARE statusCursor CURSOR FOR
			SELECT
            i.IDPorudzbine
			FROM
				inserted i
				JOIN deleted d ON i.IDPorudzbine = d.IDPorudzbine
			WHERE
				i.Status <> d.Status
				AND d.Status = 'sent'
				AND i.Status = 'arrived'
			
			OPEN statusCursor        
			-- Dohvati prvi red
			FETCH NEXT FROM statusCursor INTO @IDPorudzbine
			WHILE @@FETCH_STATUS = 0
			BEGIN
				PRINT 'IDPORUDZBINE= '+CAST(@IDPorudzbine AS VARCHAR)
				SELECT @Profit=sum(Artikal.Cena*Korpa.Kolicina*(100-Popust)/100)
				FROM Prodavnica join Artikal on (Prodavnica.IDP=Artikal.IDP)
								join Korpa on (Artikal.IDA=Korpa.IDA)
								join Porudzbina on (Korpa.IDPorudzbine=Porudzbina.IDPorudzbine)
				--GROUP BY Porudzbina.IDPorudzbine
				UPDATE SYSTEM SET Proift=Proift+(@Profit*0.05)
				PRINT @Profit*0.05

				DECLARE kursor CURSOR FOR
				SELECT Prodavnica.IDP,sum(Artikal.Cena*Korpa.Kolicina*(100-Popust)/100), Porudzbina.IDPorudzbine
				FROM Prodavnica join Artikal on (Prodavnica.IDP=Artikal.IDP)
								join Korpa on (Artikal.IDA=Korpa.IDA)
								join Porudzbina on (Korpa.IDPorudzbine=Porudzbina.IDPorudzbine)
				WHERE Porudzbina.IDPorudzbine=@IDPorudzbine
				GROUP BY Prodavnica.IDP, Porudzbina.IDPorudzbine
			
			
				-- Otvori kursor
				OPEN kursor
        
				-- Dohvati prvi red
				FETCH NEXT FROM kursor INTO @IDP, @Profit, @IDPorudzbine1
				-- Iteriraj kroz redove
				WHILE @@FETCH_STATUS = 0
				BEGIN
					print @Profit*0.95
					INSERT INTO Transakcija VALUES(@Profit*0.95, GETDATE(), 'placeno', @IDP, @IDPorudzbine1, NULL)
					FETCH NEXT FROM kursor INTO @IDP, @Profit,@IDPorudzbine1
				END
        
        
				CLOSE kursor
				DEALLOCATE kursor
				END
        
			FETCH NEXT FROM statusCursor INTO @IDPorudzbine
			CLOSE statusCursor
			DEALLOCATE statusCursor


			



		END
		END
		
		
	   
END
GO



create procedure SP_FINAL_PRICE
@IDPorudzbine int
as
begin
	DECLARE @suma DECIMAL(16,2)
	DECLARE @datumSlanja datetime
	DECLARE @IDK int
 	select @suma=sum(korpa.kolicina*artikal.cena*(100-popust) /100), @IDK=IDK from porudzbina join
	korpa on porudzbina.IDPorudzbine=korpa.IDPorudzbine join artikal on artikal.ida=korpa.ida
	join prodavnica on artikal.IDP=prodavnica.IDP
	where porudzbina.IDPorudzbine=@IDPorudzbine
	group by porudzbina.IDK,porudzbina.IDPorudzbine
	select @datumSlanja=vremeSlanja from porudzbina where IDK=@IDK and vremeSlanja>= ( getdate() - 30) and cena>=10000;
	IF @datumSlanja IS NOT NULL 
    BEGIN
        SET @suma = @suma * 0.98
    END
	SELECT @suma AS NovaSuma
end

	