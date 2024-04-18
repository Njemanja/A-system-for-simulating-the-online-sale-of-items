/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package rs.etf.sab.operations;

/**
 *
 * @author Neca
 */
public interface CityOperations {
    int createCity​(java.lang.String name);
    java.util.List<java.lang.Integer> getCities();
    int connectCities​(int cityId1, int cityId2, int distance);
    java.util.List<java.lang.Integer> getConnectedCities​(int cityId);
    java.util.List<java.lang.Integer> getShops​(int cityId);
}
