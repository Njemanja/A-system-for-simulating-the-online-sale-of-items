
package rs.etf.sab.student;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.*;


public class KN190588_ArticleOperations implements ArticleOperations{
    
    private final Connection connection=DB.getInstance().getConnection();
    @Override
    public int createArticle(int shopId, String articleName, int articlePrice) {
       int id=-1;
       if(shopId<=0 || articleName==""  || articlePrice<0){
           return id;
       }
       try (PreparedStatement st = connection.prepareStatement("INSERT INTO Artikal VALUES(?,?,?,?)")) {
            st.setInt(1,shopId);
            st.setInt(2,articlePrice);
            st.setInt(3,0);
            st.setString(4,articleName);
            st.executeUpdate();
            Statement st1 = connection.createStatement();
            String query = "select max(IDA) from Artikal";
            ResultSet rs = st1.executeQuery(query);
            //Retrieving the result
            rs.next();
            id = rs.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(KN190588_ArticleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
       return id;
    }
    
}
