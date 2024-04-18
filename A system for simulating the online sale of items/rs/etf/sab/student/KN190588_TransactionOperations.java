/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.etf.sab.student;
import rs.etf.sab.operations.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Neca
 */
public class KN190588_TransactionOperations implements TransactionOperations{
      private final Connection connection=DB.getInstance().getConnection();

    @Override
    public BigDecimal getBuyerTransactionsAmmount(int buyerId) {
        int suma=0;
        try{
            PreparedStatement ps=connection.prepareStatement("SELECT Cena FROM Transakcija WHERE IDK=?");
            ps.setInt(1, buyerId);
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                suma+=rs.getInt(1);
               
            }   
             return new BigDecimal(suma).setScale(3);
        }catch (SQLException ex) {
            Logger.getLogger(KN190588_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new BigDecimal(-1);
    }

    @Override
    public BigDecimal getShopTransactionsAmmount(int shopId) {
        int suma=0;
        try{
            PreparedStatement ps=connection.prepareStatement("SELECT Cena FROM Transakcija WHERE IDP=?");
            ps.setInt(1, shopId);
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                suma+=rs.getInt(1);
               
            }   
             return new BigDecimal(suma).setScale(3);
        }catch (SQLException ex) {
            Logger.getLogger(KN190588_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new BigDecimal(-1);
    }

    @Override
    public List<Integer> getTransationsForBuyer(int buyerId) {
        List<Integer> lista=new ArrayList();
        try{
            PreparedStatement ps=connection.prepareStatement("SELECT IDT FROM Transakcija WHERE IDK=?");
            ps.setInt(1, buyerId);
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
    public int getTransactionForBuyersOrder(int orderId) {
        int id=-1;
        try{
            PreparedStatement ps=connection.prepareStatement("SELECT IDT FROM Transakcija WHERE IDPorudzbine=? and IDK IS NOT NULL");
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

    @Override
    public int getTransactionForShopAndOrder(int orderId, int shopId) {
        int id=-1;
        try{
            PreparedStatement ps=connection.prepareStatement("SELECT IDT FROM Transakcija WHERE IDPorudzbine=? and IDP=?");
            ps.setInt(1, orderId);
            ps.setInt(2, shopId);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
               id=rs.getInt(1);
            }            
        }catch (SQLException ex) {
            Logger.getLogger(KN190588_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
         return id;
    }

    @Override
    public List<Integer> getTransationsForShop(int shopId) {
        List<Integer> lista=new ArrayList();
        try{
            PreparedStatement ps=connection.prepareStatement("SELECT IDT FROM Transakcija WHERE IDP=? AND IDK=NULL");
            ps.setInt(1, shopId);
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
               lista.add(rs.getInt(1));
            }            
        }catch (SQLException ex) {
            Logger.getLogger(KN190588_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(lista.size()==0){
            return null;
        }
        return lista;
    }

    @Override
    public Calendar getTimeOfExecution(int transactionId) {
        try{
            PreparedStatement ps=connection.prepareStatement("SELECT Vreme FROM Transakcija WHERE IDT=?");
            ps.setInt(1, transactionId);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                Date date=rs.getTimestamp(1);
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
    public BigDecimal getAmmountThatBuyerPayedForOrder(int orderId) {
        try{
            PreparedStatement ps=connection.prepareStatement("SELECT Cena FROM Transakcija WHERE IDPorudzbine=? and IDK IS NOT NULL");
            ps.setInt(1, orderId);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                return new BigDecimal(rs.getInt(1));
            }            
        }catch (SQLException ex) {
            Logger.getLogger(KN190588_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new BigDecimal(-1);
    }

    @Override
    public BigDecimal getAmmountThatShopRecievedForOrder(int shopId, int orderId) {
        try{
         PreparedStatement ps=connection.prepareStatement("SELECT Cena FROM Transakcija WHERE IDPorudzbine=? and IDP=?");
         ps.setInt(1, orderId);
         ps.setInt(2, shopId);
         ResultSet rs=ps.executeQuery();
         if(rs.next()){
             return new BigDecimal(rs.getInt(1));
         }            
        }catch (SQLException ex) {
            Logger.getLogger(KN190588_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new BigDecimal(-1);
    }

    @Override
    public BigDecimal getTransactionAmount(int transactionId) {
        try{
            PreparedStatement ps=connection.prepareStatement("SELECT Cena FROM Transakcija WHERE IDT=?");
            ps.setInt(1, 1);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                return new BigDecimal(rs.getInt(1));
            }            
        }catch (SQLException ex) {
            Logger.getLogger(KN190588_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new BigDecimal(-1);
    }

    @Override
    public BigDecimal getSystemProfit() {
        try{
            PreparedStatement ps=connection.prepareStatement("SELECT Proift FROM SYSTEM WHERE IDS=?");
            ps.setInt(1, 1);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                return new BigDecimal(rs.getInt(1)).setScale(3);
            }else{
                ps=connection.prepareStatement("INSERT INTO SYSTEM VALUES(0.0)");
                ps.executeUpdate();
                return new BigDecimal(0).setScale(3);
            }         
        }catch (SQLException ex) {
            Logger.getLogger(KN190588_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new BigDecimal(-1);
    }
      
      
}
