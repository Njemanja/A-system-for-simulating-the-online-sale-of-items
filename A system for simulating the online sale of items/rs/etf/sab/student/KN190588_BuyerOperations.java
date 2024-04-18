/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.etf.sab.student;

import java.math.BigDecimal;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.*;
/**
 *
 * @author Neca
 */
public class KN190588_BuyerOperations  implements BuyerOperations{
    
    private final Connection connection=DB.getInstance().getConnection();
    @Override
    public int createBuyer(String name, int cityId) {
        int id=-1;
         try (PreparedStatement st = connection.prepareStatement("SELECT IDK FROM Kupac where Ime=? and IDG=?")) {
            st.setInt(2, cityId);
            st.setString(1, name);
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                //System.out.println("KN190588_BuyerOperations vec postoji: "+rs.getInt(1));

                return rs.getInt(1);
            }
            
         
        } catch (SQLException ex) {     
            Logger.getLogger(KN190588_BuyerOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        try (PreparedStatement st = connection.prepareStatement("INSERT INTO Kupac VALUES(?,?,?,?)")) {
            PreparedStatement st1 = connection.prepareStatement("SELECT * FROM Grad WHERE IDG=?");
            st1.setInt(1, cityId);
            ResultSet rs = st1.executeQuery();
            if(rs.next()){
                //String[] naziv=name.split(" ");
                st.setString(1,name);
                st.setString(2,"");
                st.setInt(3,0);
                st.setInt(4,cityId);
                st.executeUpdate();
                String query = "select max(IDK) from Kupac";
                Statement st2 = connection.createStatement();
                ResultSet rs1 = st2.executeQuery(query);
                if(rs1.next()){
                    id = rs1.getInt(1);
                    //System.out.println("Kreiran je kupac: "+rs1.getInt(1));
                }
                
            }
            
            
        }catch (SQLException ex) {
            Logger.getLogger(KN190588_ArticleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return id;
    }

    @Override
    public int setCity(int buyerId, int cityId) {
        try{
            PreparedStatement st1 = connection.prepareStatement("SELECT * FROM Grad WHERE IDG=?");
            st1.setInt(1, cityId);
            ResultSet rs = st1.executeQuery();
            if(rs.next()){
                PreparedStatement st= connection.prepareStatement("SELECT * FROM Kupac WHERE IDK=?");
                st.setInt(1, buyerId);
                ResultSet rs1 = st.executeQuery();
                if(rs1.next()){
                    PreparedStatement st2= connection.prepareStatement("UPDATE Kupac set IDG=? where IDK=?");
                    st2.setInt(1, cityId);
                    st2.setInt(2, buyerId);
                    st2.executeUpdate();
                    //System.out.println("Promenje je grad kupca na: "+cityId);
                    return 1;
                }else{
                    return -1;
                }
            }else{
                return -1;
            }
            
        }catch (SQLException ex) {
              Logger.getLogger(KN190588_ArticleOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
  
        return -1;
    }

    @Override
    public int getCity(int buyerId) {
        try {
            PreparedStatement st1= connection.prepareStatement("SELECT IDG FROM Kupac WHERE IDK=?");
            st1.setInt(1, buyerId);
            ResultSet rs1=st1.executeQuery();
            if(rs1.next()){
                //System.out.println("Grad kupca je: "+rs1.getInt(1));
                return rs1.getInt(1);

            }
        } catch (SQLException ex) {
            Logger.getLogger(KN190588_BuyerOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    @Override
    public BigDecimal increaseCredit(int buyerId, BigDecimal credit) {
        BigDecimal newCredit= new BigDecimal(-1);
        try {
            PreparedStatement st= connection.prepareStatement("SELECT * FROM Kupac WHERE IDK=?");
            st.setInt(1, buyerId);
            ResultSet rs=st.executeQuery();
            if(rs.next()){
                PreparedStatement st1= connection.prepareStatement("UPDATE Kupac set novac=? WHERE IDK=?");
                newCredit=credit.add(new BigDecimal(rs.getInt(4)));
                st1.setInt(1, newCredit.intValue() );
                st1.setInt(2, buyerId);
                st1.executeUpdate();
                //System.out.println("Kredit kupca je: "+rs.getInt(1));
                
            }else{
                return newCredit;
            }
        } catch (SQLException ex) {
            Logger.getLogger(KN190588_BuyerOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return newCredit.setScale(3);
    }

    @Override
    public int createOrder(int buyerId) {
        int id=-1;
        try(PreparedStatement st=connection.prepareStatement("INSERT INTO  Porudzbina VALUES (?,?,?,?,?,?)")){
            PreparedStatement st1=connection.prepareStatement("SELECT * FROM Kupac WHERE IDK=?");
            st1.setInt(1, buyerId);
            ResultSet rs=st1.executeQuery();
            if(rs.next()){
                st.setInt(1, buyerId);
                st.setString(2, "created");
                st.setInt(3, 0);
                st.setDate(4, null);
                st.setDate(5, null);
                st.setString(6, null);
                st.executeUpdate();
                PreparedStatement st2=connection.prepareStatement("SELECT max(IDPorudzbine) FROM Porudzbina");
                ResultSet rs2=st2.executeQuery();
                if(rs2.next()){
                    id=rs2.getInt(1);
                }
            }
           
            
        } catch (SQLException ex) {
            Logger.getLogger(KN190588_BuyerOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    @Override
    public List<Integer> getOrders(int buyerId) {
        List<Integer> lista=new ArrayList();
        try(PreparedStatement st=connection.prepareStatement("SELECT IDPorudzbine FROM Porudzbina WHERE IDK=?")){
            st.setInt(1, buyerId);
            ResultSet rs=st.executeQuery();
            while(rs.next()){
                lista.add(rs.getInt(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(KN190588_BuyerOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(lista.size()==0){
            return null;
        }
        return lista;
    }

    @Override
    public BigDecimal getCredit(int buyerId) {
        BigDecimal credit= new BigDecimal(-1);
        try {
            PreparedStatement st= connection.prepareStatement("SELECT * FROM Kupac WHERE IDK=?");
            st.setInt(1, buyerId);
            ResultSet rs=st.executeQuery();
            if(rs.next()){
               String a=rs.getFloat(4)+"00";
               credit=new BigDecimal(a); 
            }
        } catch (SQLException ex) {
            Logger.getLogger(KN190588_BuyerOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return credit.setScale(3);
    }
    
}
