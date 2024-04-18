/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package rs.etf.sab.operations;

/**
 *
 * @author Neca
 */
public interface BuyerOperations {
    int createBuyer​(java.lang.String name, int cityId);
    int setCity​(int buyerId, int cityId);
    int getCity​(int buyerId);
    java.math.BigDecimal increaseCredit​(int buyerId, java.math.BigDecimal credit);
    int createOrder​(int buyerId);
    java.util.List<java.lang.Integer> getOrders​(int buyerId);
    java.math.BigDecimal getCredit​(int buyerId);
}
