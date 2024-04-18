/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.etf.sab.student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.*;
/**
 *
 * @author Neca
 */
public class KN190588_GeneralOperations implements GeneralOperations{
    private final Connection connection=DB.getInstance().getConnection();
    private static Calendar t = Calendar.getInstance();

    @Override
    public void eraseAll() {
        try{
           String query =  "delete from system\n" +
                            "delete from transakcija\n" +
                            "delete from konekcija\n" +
                            "delete from korpa\n" +
                            "delete from artikal\n" +
                            "delete from prodavnica\n" +
                            "delete from porudzbina\n" +
                            "delete from kupac\n" +
                            "delete from grad\n" +
                            "DBCC CHECKIDENT('SYSTEM', RESEED, 0)\n"+
                            "DBCC CHECKIDENT('Grad', RESEED, 0)\n" +
                            "DBCC CHECKIDENT('Kupac', RESEED, 0)\n" +
                            "DBCC CHECKIDENT('Artikal', RESEED, 0)\n" +
                            "DBCC CHECKIDENT('Konekcija', RESEED, 0)\n" +
                            "DBCC CHECKIDENT('Prodavnica', RESEED, 0)\n" +
                            "DBCC CHECKIDENT('Porudzbina', RESEED, 0)\n" +
                            "DBCC CHECKIDENT('Korpa', RESEED, 0)\n" +
                            "DBCC CHECKIDENT('Transakcija', RESEED, 0)\n"
                            +"DBCC CHECKIDENT('Transakcija', RESEED, 0)\n"
                            +"INSERT INTO SYSTEM VALUES(0)";
            Statement st = connection.createStatement();
            st.executeUpdate(query);
        } catch (SQLException ex) {
            Logger.getLogger(KN190588_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public KN190588_GeneralOperations() {
        t = (Calendar) t.clone(); // Stvaranje kopije početnog vremena
    }
    @Override
    public  Calendar getCurrentTime() {
        //Calendar calendar = Calendar.getInstance(); 
        return t;
    }

    @Override
    public Calendar time(int days) {
       
        t.add(Calendar.DAY_OF_MONTH, days);
        return t;
    }

    @Override
    public void setInitialTime(Calendar time) {
         t = time;
        
    }
/*delete from korpa
delete from porudzbina
delete from artikal
delete from kupac
delete from prodavnica
delete from konekcija
delete from grad*/
}
