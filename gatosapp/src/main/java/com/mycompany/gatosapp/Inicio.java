/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.gatosapp;

import java.io.IOException;
import javax.swing.JOptionPane;

/**
 *
 * @author Facundo
 */
public class Inicio {
    public static void main(String[] args) throws IOException {
        int opcionMenu = -1;
        String[] botones = {"1. Ver gatos","2. Ver favoritos","3. Salir"};
        do {
            //Menú Principal
            String opcion = (String) JOptionPane.showInputDialog(null,"Gatitos Java","Menú Principal",
                    JOptionPane.INFORMATION_MESSAGE, null, botones, botones[0]);
            
           //Validamos qué opcion selecciona el usuario.
            for (int i = 0; i < botones.length; i++) {
                if (opcion.equals(botones[i])) {
                    opcionMenu = i;
                }
            }
            
            switch (opcionMenu) {
                case 0:
                    GatoService.verGatos();
                    break;
                case 1:
                    Gato g = new Gato();
                    GatoService.verFavoritos(g.getApiKey());
                default:
                    break;
            }
        } while (opcionMenu != 1);
        
    }
}
