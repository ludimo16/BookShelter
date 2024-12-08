/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package App;

/**
 *
 * @author ludimo16
 */
public class SessionManager {
    private static int currentUserId; // ID del usuario actual
    private static boolean isAdmin;  // Indica si es administrador o usuario

    public static void login(int userId, boolean admin) {
        currentUserId = userId;
        isAdmin = admin;
    }

    public static int getCurrentUserId() {
        return currentUserId;
    }

    public static boolean isAdmin() {
        return isAdmin;
    }

    public static void logout() {
        currentUserId = 0; // Reinicia el ID
        isAdmin = false;
    }
}