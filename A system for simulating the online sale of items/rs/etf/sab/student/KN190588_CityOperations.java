/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.etf.sab.student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.*;
/**
 *
 * @author Neca
 */
public class KN190588_CityOperations implements CityOperations{
    
    private final Connection connection=DB.getInstance().getConnection();
    @Override
    public int createCity(String name) {
        if(name==""){
            return -1;
        }
         try(PreparedStatement st1 = connection.prepareStatement("SELECT IDG FROM Grad  WHERE Naziv=?")) {
           st1.setString(1, name);
           ResultSet rs2 = st1.executeQuery();
           if(rs2.next()){
               return rs2.getInt(1);
               
           }
        } catch (SQLException ex) {
            Logger.getLogger(KN190588_ArticleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        try(PreparedStatement st = connection.prepareStatement("INSERT INTO Grad  VALUES(?)")) {
           st.setString(1, name);
           st.executeUpdate();
           Statement st2 = connection.createStatement();
           String query = "select max(IDG) from Grad";
           ResultSet rs2 = st2.executeQuery(query);
           if(rs2.next()){
               return rs2.getInt(1);
               
           }
        } catch (SQLException ex) {
            Logger.getLogger(KN190588_ArticleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return -1;
    }

    @Override
    public List<Integer> getCities() {
        List<Integer> lista=new ArrayList<>();
        try(PreparedStatement st = connection.prepareStatement("SELECT IDG FROM Grad")) {
           ResultSet rs=st.executeQuery();
           while(rs.next()){
               int idg=rs.getInt(1);
               lista.add(idg);
           }
        } catch (SQLException ex) {
            Logger.getLogger(KN190588_ArticleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }

    @Override
    public int connectCities(int cityId1, int cityId2, int distance) {
        if(distance<=0){
            return -1;
        }
        
        try(PreparedStatement st = connection.prepareStatement("SELECT * FROM Konekcija where IDG1=? and IDG2=?")) {
           st.setInt(1,cityId1);
           st.setInt(2,cityId2);
           ResultSet rs=st.executeQuery();
           if(rs.next()){
               return -1;
           }
           PreparedStatement st1 = connection.prepareStatement("SELECT * FROM Konekcija where IDG1=? and IDG2=?");
           st1.setInt(1,cityId2);
           st1.setInt(2,cityId1);
           ResultSet rs1=st1.executeQuery();
           if(rs1.next()){
               return -1;
           }
           PreparedStatement st4 = connection.prepareStatement("SELECT * FROM GRAD WHERE IDG=?");
           st4.setInt(1,cityId1);
           ResultSet rs4=st4.executeQuery();
           if(!rs4.next()){
               return -1;
           }
           PreparedStatement st5 = connection.prepareStatement("SELECT * FROM GRAD WHERE IDG=?");
           st5.setInt(1,cityId2);
           ResultSet rs5=st5.executeQuery();
           if(!rs5.next()){
               return -1;
           }
           
           
           PreparedStatement st2 = connection.prepareStatement("INSERT INTO Konekcija VALUES(?,?,?)");
           st2.setInt(1,distance);
           st2.setInt(2,cityId1);
           st2.setInt(3,cityId2);
           st2.executeUpdate();
           Statement st3 = connection.createStatement();
           String query = "select max(IDKonekcija) from Konekcija";
           ResultSet rs3 = st3.executeQuery(query);
           if(rs3.next()){
               return rs3.getInt(1);
           }
           
           
        } catch (SQLException ex) {
            Logger.getLogger(KN190588_ArticleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    @Override
    public List<Integer> getConnectedCities(int cityId) {
        List<Integer> lista=new ArrayList<>();
        try(PreparedStatement st = connection.prepareStatement("SELECT IDG1 FROM Konekcija where IDG2=?")) {
           st.setInt(1,cityId);
           ResultSet rs=st.executeQuery();
           while(rs.next()){
               int idg=rs.getInt(1);
               lista.add(idg);
           }
           PreparedStatement st1 = connection.prepareStatement("SELECT IDG2 FROM Konekcija where IDG1=?");
           st1.setInt(1,cityId);
           ResultSet rs1=st1.executeQuery();
           while(rs1.next()){
               int idg=rs1.getInt(1);
               lista.add(idg);
           }
           
        } catch (SQLException ex) {
            Logger.getLogger(KN190588_ArticleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }

    @Override
    public List<Integer> getShops(int cityId) {
        List<Integer> lista=new ArrayList<>();
        try(PreparedStatement st = connection.prepareStatement("SELECT IDP FROM Prodavnica where IDG=?")) {
           st.setInt(1,cityId);
           ResultSet rs=st.executeQuery();
           while(rs.next()){
               int idp=rs.getInt(1);
               lista.add(idp);
           }
        } catch (SQLException ex) {
            Logger.getLogger(KN190588_ArticleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }
    
    
    
}
