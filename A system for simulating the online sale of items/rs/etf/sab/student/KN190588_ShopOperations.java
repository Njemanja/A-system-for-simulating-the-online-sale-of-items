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
public class KN190588_ShopOperations implements ShopOperations{
    private final Connection connection=DB.getInstance().getConnection();
    @Override
    public int createShop(String name, String cityName) {
        try (PreparedStatement st = connection.prepareStatement("SELECT IDG FROM Grad where Naziv=?")) {
            st.setString(1, cityName);
            ResultSet rs=st.executeQuery();
            if(rs.next()){
                PreparedStatement st1 = connection.prepareStatement("SELECT IDP FROM Prodavnica where IDG=? and ImeProdavnice=?");
                st1.setInt(1, rs.getInt(1));
                st1.setString(2, name);
                ResultSet rs1=st1.executeQuery();
                if(rs1.next()){
                    return rs1.getInt(1);
                }
                
                
            }
            
            
           
            
        } catch (SQLException ex) {
            Logger.getLogger(KN190588_ArticleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        try (PreparedStatement st = connection.prepareStatement("INSERT INTO Prodavnica VALUES(?,?,?,?,?)")) {
            PreparedStatement st1 = connection.prepareStatement("SELECT IDG FROM Grad where Naziv=?");
            st1.setString(1, cityName);
            ResultSet rs1=st1.executeQuery();
            if(rs1.next()){
                st.setDouble(1,0.0);
                st.setDate(2,null);
                st.setInt(3, rs1.getInt(1));
                st.setDate(4,null);
                st.setString(5,name);
                st.executeUpdate();
                 
               
                Statement st2 = connection.createStatement();
                String query = "select max(IDP) from Prodavnica";
                ResultSet rs2 = st2.executeQuery(query);
                if(rs2.next()){
                    return rs2.getInt(1);
                }
                
                
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(KN190588_ArticleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    @Override
    public int setCity(int shopId, String cityName) {

        try(PreparedStatement st = connection.prepareStatement("SELECT IDG from grad where naziv=?")) {
           st.setString(1, cityName);
           ResultSet rs=st.executeQuery();
           
           if(rs.next()){
              int idg=rs.getInt(1);
              PreparedStatement st1 = connection.prepareStatement("UPDATE Prodavnica set idg=? where idp=?");
              st1.setInt(1, idg);
              st1.setInt(2, shopId);
              int s=st1.executeUpdate();
              if(s>0){
                  return 1;
              }
              
              
           }
        } catch (Exception e) {
        }
        
        return -1;
    }

    @Override
    public int getCity(int shopId) {
        int idg=-1;
         try (PreparedStatement st = connection.prepareStatement("SELECT IDG FROM Prodavnica WHERE IDP=? ")) {
            
            st.setInt(1, shopId);
            ResultSet rs=st.executeQuery();
            if(rs.next()){
                idg=rs.getInt(1); 
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(KN190588_ArticleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return idg;
    
    }

    @Override
    public int setDiscount(int shopId, int discountPercentage) {
        if(discountPercentage<0 || discountPercentage>100){
            return -1;
        }
        try(PreparedStatement st = connection.prepareStatement("Update Prodavnica set popust=? where IDP=?")) {
            st.setInt(1, discountPercentage);
            st.setInt(2, shopId);
            int s=st.executeUpdate();
            if(s<=0){
                return -1;
            }
        } catch (Exception e) {
        }
        
        return 1;
    }

    @Override
    public int increaseArticleCount(int articleId, int increment) {
        int count=-1;
        try (PreparedStatement st = connection.prepareStatement("SELECT Kolicina FROM Artikal WHERE IDA=? ")) {
            st.setInt(1, articleId);
            ResultSet rs=st.executeQuery();
            if(rs.next()){
                count=rs.getInt(1);
                count+=increment;
                PreparedStatement st1 = connection.prepareStatement("UPDATE Artikal set kolicina=? WHERE IDA=? ");
                st1.setInt(1, count);
                st1.setInt(2, articleId);
                st1.executeUpdate();
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(KN190588_ArticleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count;
    }

    @Override
    public int getArticleCount(int articleId) {
        int count=-1;
        try (PreparedStatement st = connection.prepareStatement("SELECT Kolicina FROM Artikal WHERE IDA=? ")) {
            st.setInt(1, articleId);
            ResultSet rs=st.executeQuery();
            if(rs.next()){
                count=rs.getInt(1); 
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(KN190588_ArticleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count;
    }

    @Override
    public List<Integer> getArticles(int shopId) {
        List<Integer> lista=new ArrayList<>();
        try (PreparedStatement st = connection.prepareStatement("SELECT IDA FROM Artikal WHERE IDP=? ")) {
            st.setInt(1, shopId);
            ResultSet rs=st.executeQuery();
            while(rs.next()){
              int ida=rs.getInt(1);
              lista.add(ida);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(KN190588_ArticleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }

    @Override
    public int getDiscount(int shopId) {
        int popust=-1;
         try (PreparedStatement st = connection.prepareStatement("SELECT Popust FROM Prodavnica WHERE IDP=? ")) {
            
            st.setInt(1, shopId);
            ResultSet rs=st.executeQuery();
            if(rs.next()){
                popust=rs.getInt(1); 
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(KN190588_ArticleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return popust;
    }
    
}
