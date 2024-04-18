/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package rs.etf.sab.operations;
/**
 *
 * @author Neca
 */
public interface GeneralOperations {
    void eraseAll();
    java.util.Calendar getCurrentTime();
    java.util.Calendar time​(int days);
    void setInitialTime​(java.util.Calendar time);
}
