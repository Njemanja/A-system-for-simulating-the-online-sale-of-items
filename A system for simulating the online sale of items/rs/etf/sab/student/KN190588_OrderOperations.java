/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.etf.sab.student;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.*;
import java.util.*;


/**
 *
 * @author Neca
 */
public class KN190588_OrderOperations implements  OrderOperations{
    private final Connection connection=DB.getInstance().getConnection();
    @Override
    public int addArticle(int orderId, int articleId, int count) {
        int kolicina=0;
        int id=-1;
        try{
          PreparedStatement st = connection.prepareStatement("SELECT Kolicina FROM Artikal WHERE IDA=?"); 
          st.setInt(1, articleId);
          ResultSet rs = st.executeQuery();
          if(rs.next()){
              kolicina=rs.getInt(1);
              if(kolicina<count){
                  return -1;
              }
          }
          PreparedStatement st1 = connection.prepareStatement("SELECT IDKorpa FROM Korpa WHERE IDA=? AND IDPorudzbine=?");
          st1.setInt(1, articleId);
          st1.setInt(2, orderId);
          ResultSet rs1 = st1.executeQuery();
          PreparedStatement st2 = connection.prepareStatement("UPDATE Artikal set kolicina=? where IDA=?");
          st2.setInt(1, kolicina-count);
          st2.setInt(2, articleId);
          st2.executeUpdate();
          if(rs1.next()){
            id=rs1.getInt(1);
            PreparedStatement st4 = connection.prepareStatement("UPDATE Korpa SET kolicina = kolicina + ? WHERE IDA=? AND IDPorudzbine=?");
            st4.setInt(1, count);
            st4.setInt(2, articleId);
            st4.setInt(3, orderId);
            st4.executeUpdate();
            
           
          }else{
            PreparedStatement st3 = connection.prepareStatement("INSERT INTO Korpa VALUES(?,?,?)");
            st3.setInt(1, count);
            st3.setInt(2, articleId);
            st3.setInt(3, orderId);
            st3.executeUpdate();
            String query = "select max(IDKorpa) from Korpa";
            Statement st5 = connection.createStatement();
            ResultSet rs5 = st5.executeQuery(query);
            if(rs5.next()){
                id = rs5.getInt(1);
            }
          }
          
          
        } catch (SQLException ex) {
            Logger.getLogger(KN190588_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return id;
    }

    @Override
    public int removeArticle(int orderId, int articleId) {
        int count=0;
        try{
            PreparedStatement ps1=connection.prepareStatement("SELECT Kolicina FROM Korpa WHERE IDA=? AND IDPorudzbine=?");
            ps1.setInt(1, articleId);
            ps1.setInt(2, orderId);
            ResultSet rs1=ps1.executeQuery();
            if(rs1.next()){
                count=rs1.getInt(1);
            }else{
                return -1;
            }
            
            PreparedStatement ps=connection.prepareStatement("DELETE FROM Korpa WHERE IDA=? AND IDPorudzbine=?");
            ps.setInt(1, articleId);
            ps.setInt(2, orderId);
            int s=ps.executeUpdate();
            if(s<=0){
                return -1;
            }
            PreparedStatement st4 = connection.prepareStatement("UPDATE Artikal SET kolicina = kolicina + ? WHERE IDA=?");
            st4.setInt(1, count);
            st4.setInt(2, articleId);
            st4.executeUpdate();
            
            
        }catch (SQLException ex) {
            Logger.getLogger(KN190588_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 1;
    }

    @Override
    public List<Integer> getItems(int orderId) {
        List<Integer> lista= new ArrayList();
        try{
            PreparedStatement ps=connection.prepareStatement("SELECT IDA FROM Korpa WHERE IDPorudzbine=?");
            ps.setInt(1, orderId);
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                lista.add(rs.getInt(1));
            }            
        }catch (SQLException ex) {
            Logger.getLogger(KN190588_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return lista;
    }

    @Override
    public int completeOrder(int orderId) {
        BigDecimal cena= getFinalPrice(orderId);
        int idk=0;
        KN190588_GeneralOperations gen=new KN190588_GeneralOperations();
        try{
           
            
            PreparedStatement ps=connection.prepareStatement("SELECT Novac, p.IDK FROM Porudzbina p JOIN Kupac k ON k.IDK=p.IDK  "
                    + "WHERE IDPorudzbine=?");
            ps.setInt(1, orderId);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                BigDecimal novac = new BigDecimal(rs.getFloat(1));
                idk=rs.getInt(2);
                if(cena.compareTo(novac)>0){
                    return -1;
                }
            }
            PreparedStatement ps2=connection.prepareStatement("UPDATE Kupac set novac= novac - ? WHERE IDK=?");
            ps2.setFloat(1, cena.floatValue());
            ps2.setInt(2, idk);
            int s1=ps2.executeUpdate();
            if(s1<=0){
                return -1;
            }
            ps2=connection.prepareStatement("INSERT INTO Transakcija VALUES(?,?,?,?,?,?)");
            ps2.setFloat(1, cena.floatValue());
            Date time = new Date(gen.getCurrentTime().getTimeInMillis());
            ps2.setDate(2, new java.sql.Date(time.getTime()));
            ps2.setString(3,null);
            ps2.setString(4, null);
            ps2.setInt(5, orderId);
            ps2.setInt(6, idk);
            ps2.executeUpdate();
            
            
            PreparedStatement ps1=connection.prepareStatement("UPDATE Porudzbina set status= ?, VremeSlanja= ?, cena=? WHERE IDPorudzbine=?");
            ps1.setString(1, "sent");
            time = new Date(gen.getCurrentTime().getTimeInMillis());
            ps1.setDate(2, new java.sql.Date(time.getTime()));
            ps1.setFloat(3, cena.floatValue());
            ps1.setInt(4,orderId);
            
            int s=ps1.executeUpdate();
            if(s<=0){
                return -1;
            }

            
        }catch (SQLException ex) {
            Logger.getLogger(KN190588_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return 1;
    }

    @Override
    public BigDecimal getFinalPrice(int orderId) {
        double sum=0;
        String state= getState(orderId);
        /*if(state.equals("created")){
            return new BigDecimal(-1);
        }*/
        try{
            /*PreparedStatement ps=connection.prepareStatement("SELECT IDA, Kolicina FROM Korpa WHERE IDPorudzbine=?");
            ps.setInt(1, orderId);
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                PreparedStatement ps1=connection.prepareStatement("SELECT Cena, IDP FROM Artikal WHERE IDA=?");
                ps1.setInt(1, rs.getInt(1));
                ResultSet rs1=ps1.executeQuery();
                if(rs1.next()){
                    sum= sum + rs1.getFloat(1)*rs.getInt(2);
                }
            }   */   
            PreparedStatement ps=connection.prepareStatement("exec SP_FINAL_PRICE ?");
            ps.setInt(1, orderId);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                sum=rs.getDouble(1);
            }
        }catch (SQLException ex) {
            Logger.getLogger(KN190588_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        //BigDecimal sum1=new BigDecimal(sum);
        //BigDecimal discountSum=getDiscountSum(orderId);
        //sum1 = sum1.subtract(discountSum);
        return new BigDecimal(sum).setScale(3);
    }
    

    @Override
    public BigDecimal getDiscountSum(int orderId) {
        float discountSum=0;
        String state= getState(orderId);
        /*if(state.equals("created")){
            return new BigDecimal(-1);
        }*/
        try{
            PreparedStatement ps=connection.prepareStatement("SELECT IDA, Kolicina FROM Korpa WHERE IDPorudzbine=?");
            ps.setInt(1, orderId);
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                PreparedStatement ps1=connection.prepareStatement("SELECT Cena, IDP FROM Artikal WHERE IDA=?");
                ps1.setInt(1, rs.getInt(1));
                ResultSet rs1=ps1.executeQuery();
                if(rs1.next()){
                    PreparedStatement ps2=connection.prepareStatement("SELECT Popust FROM Prodavnica WHERE IDP=?");
                    ps2.setInt(1, rs1.getInt(2));
                    ResultSet rs2=ps2.executeQuery();
                    if(rs2.next()){
                         discountSum= discountSum + rs1.getFloat(1)*rs2.getFloat(1)/100*rs.getInt(2);
                    }
                    
                    
                    
                }
            }        
        }catch (SQLException ex) {
            Logger.getLogger(KN190588_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return new BigDecimal(discountSum).setScale(3);
    }

    @Override
    public String getState(int orderId) {
        try{
            PreparedStatement ps=connection.prepareStatement("SELECT Status FROM Porudzbina WHERE IDPorudzbine=?");
            ps.setInt(1, orderId);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                return rs.getString(1);
            }            
        }catch (SQLException ex) {
            Logger.getLogger(KN190588_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
         return null;
    }

    @Override
    public Calendar getSentTime(int orderId) {
        Date date;
        try{
            PreparedStatement ps=connection.prepareStatement("SELECT VremeSlanja FROM Porudzbina WHERE IDPorudzbine=?");
            ps.setInt(1, orderId);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                if(rs.getTimestamp(1)==null){
                    return null;
                }
                date=rs.getTimestamp(1);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                return calendar;
            }            
        }catch (SQLException ex) {
            Logger.getLogger(KN190588_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
         return null;
    }

    @Override
    public Calendar getRecievedTime(int orderId) {
        Date date;
         try{
            PreparedStatement ps=connection.prepareStatement("SELECT VremeDolaska FROM Porudzbina WHERE IDPorudzbine=?");
            ps.setInt(1, orderId);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                if(rs.getTime(1)==null){
                    return null;
                }
                date=rs.getTimestamp(1);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                return calendar;
            }            
        }catch (SQLException ex) {
            Logger.getLogger(KN190588_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
         return null;
    }

    @Override
    public int getBuyer(int orderId) {
        int id=-1;
         try{
            PreparedStatement ps=connection.prepareStatement("SELECT IDK FROM Porudzbina WHERE IDPorudzbine=?");
            ps.setInt(1, orderId);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                id=rs.getInt(1);
            }            
        }catch (SQLException ex) {
            Logger.getLogger(KN190588_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
         return id;
    }   
    class Linija
    {
        public int odKoga; 
        public int doKoga;  
        public int    tezina;
        public List<Integer> lista=new ArrayList();
     };
    @Override
    public int getLocation(int orderId) {
        KN190588_GeneralOperations gen=new KN190588_GeneralOperations();
        Calendar vremeKadaJePoslato=null;
        int najblizaProdavnica=-1;
        List<Linija> lista=new ArrayList();
        int idGKupca=0;
        try{
            
            PreparedStatement ps=connection.prepareStatement("SELECT IDK FROM Porudzbina WHERE IDPorudzbine=?");
            ps.setInt(1, orderId);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                ps=connection.prepareStatement("SELECT IDG FROM Kupac WHERE IDK=?");
                ps.setInt(1, rs.getInt(1));
                rs=ps.executeQuery();
                if(rs.next()){
                    lista=konekcije(rs.getInt(1));
                    idGKupca=rs.getInt(1);
                }

            }
            for(int i=0; i<lista.size();i++){
                for(int j=0;j<lista.get(i).lista.size();j++){
                    //System.out.print(lista.get(i).lista.get(j)+"->");
                    int a=1;
                }
            //System.out.println("x\t\t\t\tTezina: "+lista.get(i).tezina);
            }   
            List<Integer> rastojanja=new ArrayList();
            najblizaProdavnica=najblizaProdavnica(lista);
            ps=connection.prepareStatement("SELECT IDG FROM Prodavnica WHERE IDP=?");
            ps.setInt(1, najblizaProdavnica);
            rs=ps.executeQuery();
            if(rs.next()){
                int idg=rs.getInt(1);
                ps=connection.prepareStatement("SELECT IDG FROM Artikal \n" +
                "		JOIN Korpa on (artikal.ida=korpa.ida) \n" +
                "		join porudzbina on (korpa.idporudzbine=porudzbina.idPorudzbine)\n" +
                "	    join prodavnica on (Artikal.IDP=Prodavnica.idp)\n" +
                "	   where porudzbina.IDPorudzbine=?");
                ps.setInt(1, orderId);
                ps.setInt(1, najblizaProdavnica);
                ResultSet rs2=ps.executeQuery();
                List<Linija> lista2=new ArrayList();
                lista2=konekcije(idg);
                for(int i=0; i<lista2.size();i++){
                    for(int j=0;j<lista2.get(i).lista.size();j++){
                        //System.out.print(lista2.get(i).lista.get(j)+"->");
                    }
                    //System.out.println("x\t\t\t\tTezina: "+lista2.get(i).tezina);
                    }  
                while(rs2.next()){
                    int rastojanje=Integer.MAX_VALUE;
                    for(int i=0; i<lista2.size();i++){
                        if((lista2.get(i).lista.get(lista2.get(i).lista.size()-1)).equals(rs2.getInt(1))){
                            if(lista2.get(i).tezina<rastojanje){
                                rastojanje=lista2.get(i).tezina;
                            }
                        }
                    }
                    //System.out.println(rastojanje);
                    rastojanja.add(rastojanje);
                }
                int posleKolikoDanaUPordavniciA=Collections.max(rastojanja);
                int odAdoB=Integer.MAX_VALUE;
                List<Integer> putanja=new ArrayList();
                for(int i=0; i<lista2.size();i++){
                    if((lista2.get(i).lista.get(lista2.get(i).lista.size()-1)).equals(idGKupca)){
                        if(lista2.get(i).tezina<odAdoB){
                            odAdoB=lista2.get(i).tezina;
                            putanja.clear();
                            for(int j=0;j<lista2.get(i).lista.size();j++){
                                putanja.add(lista2.get(i).lista.get(j));
                            }
                        }
                    }
                }
                //System.out.println("Ukupno vreme: "+(odAdoB+posleKolikoDanaUPordavniciA));
                ps=connection.prepareStatement("SELECT VremeSlanja FROM Porudzbina WHERE IDPorudzbine=?");
                ps.setInt(1, orderId);
                rs=ps.executeQuery();
                if(rs.next()){
                    vremeKadaJePoslato=Calendar.getInstance();
                    vremeKadaJePoslato.setTime(rs.getDate(1));
                    Calendar vremeSlanja = Calendar.getInstance();
                    vremeSlanja.setTime(rs.getDate(1));
                    Calendar trenutnoVreme = gen.getCurrentTime();
                    vremeSlanja.add(Calendar.DAY_OF_MONTH, posleKolikoDanaUPordavniciA);
                    int proslo=vremeSlanja.compareTo(trenutnoVreme);
                    if(proslo>0){
                        return idg;
                    }
                    vremeSlanja.setTime(rs.getDate(1));
                    vremeSlanja.add(Calendar.DAY_OF_MONTH, odAdoB+posleKolikoDanaUPordavniciA);
                    proslo=vremeSlanja.compareTo(trenutnoVreme);
                    if(proslo<=0){
                        vremeKadaJePoslato.add(Calendar.DAY_OF_MONTH, odAdoB+posleKolikoDanaUPordavniciA);
                        ps=connection.prepareStatement("UPDATE Porudzbina SET VremeDolaska=?, status=? WHERE IDPorudzbine=?");
                        Date time = new Date(vremeKadaJePoslato.getTimeInMillis());
                        ps.setDate(1, new java.sql.Date(time.getTime()));
                        ps.setString(2, "arrived");
                        ps.setInt(3, orderId);
                        ps.executeUpdate();
                        ps=connection.prepareStatement("UPDATE Transakcija SET Vreme=? WHERE Vreme>?");
                        ps.setDate(1, new java.sql.Date(time.getTime()));
                        ps.setDate(2, new java.sql.Date(time.getTime()));
                        int s=ps.executeUpdate();
                        if(s>0){

                            //System.out.println("jeste");
                        }
                        return idGKupca;
                    }
                    vremeSlanja.setTime(rs.getDate(1));
                    vremeSlanja.add(Calendar.DAY_OF_MONTH,posleKolikoDanaUPordavniciA);
                    for(int i=0;i<putanja.size()-1;i++){
                        ps=connection.prepareStatement("SELECT Tezina FROM Konekcija WHERE idg1=? and idg2=?");
                        ps.setInt(1, putanja.get(i));
                        ps.setInt(2, putanja.get(i+1));
                        ResultSet rs1=ps.executeQuery();
                        if(rs1.next()){
                            vremeSlanja.add(Calendar.DAY_OF_MONTH, rs1.getInt(1));
                            proslo=vremeSlanja.compareTo(trenutnoVreme);
                            if(proslo>0){
                                if(putanja.get(i)==idGKupca){
                                    vremeKadaJePoslato.add(Calendar.DAY_OF_MONTH, odAdoB+posleKolikoDanaUPordavniciA);
                                    Date time = new Date(vremeKadaJePoslato.getTimeInMillis());
                                    ps=connection.prepareStatement("UPDATE Porudzbina SET VremeDolaska=?, status=? WHERE IDPorudzbine=?");
                                    ps.setDate(1, new java.sql.Date(time.getTime()));
                                    ps.setString(2, "arrived");
                                    ps.setInt(3, orderId);
                                    ps.executeUpdate();
                                    ps=connection.prepareStatement("UPDATE Transakcija SET Vreme=? WHERE Vreme>?");
                                    ps.setDate(1, new java.sql.Date(time.getTime()));
                                    ps.setDate(2, new java.sql.Date(time.getTime()));
                                    int s=ps.executeUpdate();
                                    if(s>0){
                                        //System.out.println("jeste");
                                    }

                                }
                                return putanja.get(i);
                            }

                        }else{
                            ps=connection.prepareStatement("SELECT Tezina FROM Konekcija WHERE idg2=? and idg1=?");
                            ps.setInt(1, putanja.get(i));
                            ps.setInt(2, putanja.get(i+1));
                            rs2=ps.executeQuery();
                            if(rs2.next()){
                                vremeSlanja.add(Calendar.DAY_OF_MONTH, rs2.getInt(1));
                                proslo=vremeSlanja.compareTo(trenutnoVreme);
                                if(proslo<0){
                                    if(putanja.get(i)==idGKupca){
                                        vremeKadaJePoslato.add(Calendar.DAY_OF_MONTH, odAdoB+posleKolikoDanaUPordavniciA);
                                        ps=connection.prepareStatement("UPDATE Porudzbina SET VremeDolaska=?, status=? WHERE IDPorudzbine=?");
                                        Date time = new Date(vremeKadaJePoslato.getTimeInMillis());
                                        ps.setDate(1, new java.sql.Date(time.getTime()));
                                        ps.setString(2, "arrived");
                                        ps.setInt(3, orderId);
                                        ps.executeUpdate();
                                        time = new Date(gen.getCurrentTime().getTimeInMillis());
                                        ps=connection.prepareStatement("UPDATE Transakcija SET Vreme=? WHERE Vreme>?");
                                        ps.setDate(1, new java.sql.Date(time.getTime()));
                                        ps.setDate(2, new java.sql.Date(time.getTime()));
                                        int s=ps.executeUpdate();
                                        if(s>0){
                                            //System.out.println("jeste");
                                        }

                                    }
                                    
                                    return putanja.get(i);
                                }
                            }

                        }
                    }
                }

            }
           
        }catch (SQLException ex) {
            Logger.getLogger(KN190588_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public int najblizaProdavnica(List<Linija> lista2){
        int najblizaProdavnica=-1;
        try{
            PreparedStatement ps=connection.prepareStatement("SELECT IDG FROM GRAD ");
            ResultSet rs=ps.executeQuery();
            int rastojanje= Integer.MAX_VALUE;
            while(rs.next()){
                ps=connection.prepareStatement("SELECT IDP FROM Prodavnica WHERE IDG=?");
                ps.setInt(1, rs.getInt(1));
                ResultSet rs1=ps.executeQuery();
                if(rs1.next()){
                    for(int i=0; i<lista2.size();i++){
                        if((lista2.get(i).lista.get(lista2.get(i).lista.size()-1)).equals(rs.getInt(1))){
                            if(lista2.get(i).tezina<rastojanje){
                                rastojanje=lista2.get(i).tezina;
                                najblizaProdavnica=rs1.getInt(1);
                            }
                        }
                    }
                }
            }
        }catch (SQLException ex) {
            Logger.getLogger(KN190588_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return najblizaProdavnica;
            
    }
    
public  List<Linija> konekcije(int idg){
        List<Linija> lista2=new ArrayList();

        try{
            List<Linija> lista=new ArrayList();

            Linija linija=new Linija();
            linija.tezina=0;
            linija.lista.add(idg);
            lista.add(linija);
            while(lista.size()!=0){
                int nadjeno=0;
                linija=lista.get(0);
                int grad=linija.lista.get(linija.lista.size()-1);
                PreparedStatement ps=connection.prepareStatement("SELECT IDG1, Tezina FROM Konekcija WHERE IDG2=?");
                ps.setInt(1, grad);
                ResultSet rs=ps.executeQuery();
                while(rs.next()){
                    Linija l=new Linija();

                    if(!linija.lista.contains(rs.getInt(1))){
                        for(int i=0;i<linija.lista.size();i++){
                            l.lista.add(linija.lista.get(i));
                        }
                        l.tezina+=linija.tezina;
                        l.lista.add(rs.getInt(1));
                        l.tezina+=rs.getInt(2);
                        nadjeno+=1;
                        lista.add(l);

                    }
            }
            ps=connection.prepareStatement("SELECT IDG2, Tezina FROM Konekcija WHERE IDG1=?");
            ps.setInt(1, grad);
            rs=ps.executeQuery();
            while(rs.next()){
                Linija l=new Linija();

                if(!linija.lista.contains(rs.getInt(1))){
                    for(int i=0;i<linija.lista.size();i++){
                        l.lista.add(linija.lista.get(i));
                    }
                    l.tezina+=linija.tezina;
                    l.lista.add(rs.getInt(1));
                    l.tezina+=rs.getInt(2);
                    nadjeno+=1;
                    lista.add(l);

                }
            }

            lista2.add(lista.get(0));
            lista.remove(0);

        }
        
        }
        catch (SQLException ex) {
            Logger.getLogger(KN190588_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista2;
    }
    
}
