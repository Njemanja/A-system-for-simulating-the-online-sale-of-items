/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package rs.etf.sab.operations;

/**
 *
 * @author Neca
 */
public interface OrderOperations {
    int addArticle​(int orderId, int articleId, int count);
    int removeArticle​(int orderId, int articleId);
    java.util.List<java.lang.Integer> getItems​(int orderId);
    int completeOrder​(int orderId);
    java.math.BigDecimal getFinalPrice​(int orderId);
    java.math.BigDecimal getDiscountSum​(int orderId);
    java.lang.String getState​(int orderId);
    java.util.Calendar getSentTime​(int orderId);
    java.util.Calendar getRecievedTime​(int orderId);
    int getBuyer​(int orderId);
    int getLocation​(int orderId);
    
    
    
    
    
}
