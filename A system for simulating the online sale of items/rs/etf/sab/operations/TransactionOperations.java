/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package rs.etf.sab.operations;

/**
 *
 * @author Neca
 */
public interface TransactionOperations {
    java.math.BigDecimal getBuyerTransactionsAmmount​(int buyerId);
    java.math.BigDecimal getShopTransactionsAmmount​(int shopId);
    java.util.List<java.lang.Integer> getTransationsForBuyer​(int buyerId);
    int getTransactionForBuyersOrder​(int orderId);
    int getTransactionForShopAndOrder​(int orderId, int shopId);
    java.util.List<java.lang.Integer> getTransationsForShop​(int shopId);
    java.util.Calendar getTimeOfExecution​(int transactionId);
    java.math.BigDecimal getAmmountThatBuyerPayedForOrder​(int orderId);
    java.math.BigDecimal getAmmountThatShopRecievedForOrder​(int shopId, int orderId);
    java.math.BigDecimal getTransactionAmount​(int transactionId);
    java.math.BigDecimal getSystemProfit();
    
}
