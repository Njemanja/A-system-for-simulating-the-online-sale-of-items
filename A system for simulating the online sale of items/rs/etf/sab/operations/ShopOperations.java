/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package rs.etf.sab.operations;

/**
 *
 * @author Neca
 */
public interface ShopOperations {
    int createShop​(java.lang.String name, java.lang.String cityName);
    int setCity​(int shopId, java.lang.String cityName);
    int getCity​(int shopId);
    int setDiscount​(int shopId, int discountPercentage);
    int increaseArticleCount​(int articleId, int increment);
    int getArticleCount​(int articleId);
    java.util.List<java.lang.Integer> getArticles​(int shopId);
    int getDiscount​(int shopId);
    
}
