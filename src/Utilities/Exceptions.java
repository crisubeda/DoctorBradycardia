/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

/**
 *
 * @author drijc
 */
public class Exceptions {
    
    public static int convertInt(String integer) {
        int isInt = 0;
        try {
            isInt = Integer.parseInt(integer);
        } catch (NumberFormatException ex) {
        }
        return isInt;
    }
        
    public static boolean checkInt(String integer) {
        boolean isInt = true;
        try {
            int a = Integer.parseInt(integer);
        } catch (NumberFormatException ex) {
            isInt = false;
        }
        return isInt;
    }
    
    public static String[] takeDiagnosis(String string) {
        int i = 0;
        int contador = 0;
        String[] datos = new String[7];
        String data = "";
        char a;
        System.out.println("la length del string es: " +string.length());
        for (i = 0; i < string.length(); i++) {
            a=string.charAt(i);
            while (a != '#') {
                data = data + Character.toString(a);
                System.out.println("Contador: " +i);
                i++;
                a=string.charAt(i);
            }
            datos[contador] = data;
            contador++;
            data = "";
        }
        return datos;
    }
}
