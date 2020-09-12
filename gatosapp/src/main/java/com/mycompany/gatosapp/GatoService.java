/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.gatosapp;

import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;



public class GatoService {
    public static void verGatos() throws IOException{
        //Trayendo los datos de la api
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("https://api.thecatapi.com/v1/images/search").get().build();
        Response response = client.newCall(request).execute();
        String json = response.body().string();
        
        //Cortando los corchetes
        /*el contenido con los corchetes internos, se convierten en solo el contenido*/
        json = json.substring(1,json.length());
        /*los corchetes externos y el contenido se convierten en solo contenido*/
        json = json.substring(0,json.length()-1);
        
        //Convertir al json en objeto tipo Gato
        Gson gson = new Gson();
        Gato gato = gson.fromJson(json, Gato.class);
        
        //Redimensionar la imagen en caso de que sea muy grande (>800)
        Image image;
        try{
            URL url = new URL(gato.getUrl());
            //La url https (s = Segura) se convierte en http
            HttpURLConnection httpcon = (HttpURLConnection)url.openConnection();
            httpcon.addRequestProperty("User-Agent","");
            //Se convierte la url a tipo imagen
            BufferedImage bufferedImage = ImageIO.read(httpcon.getInputStream());
            ImageIcon fondoGato = new ImageIcon(bufferedImage);
            
            //Se convierte a tipo icono ya que el metodo getIconWidth lo requiere   
            
            if (fondoGato.getIconWidth() > 800){
                //Se reemplaza la resolución
                Image fondo = fondoGato.getImage();
                Image modificada = fondo.getScaledInstance(800,600,java.awt.Image.SCALE_SMOOTH);
                fondoGato = new ImageIcon(modificada);
            } 
            String menu = "Opciones: \n"
                    + "1. Ver otro gato \n"
                    + "2. Favorito \n"
                    + "3. Volver \n";
            String[] botones = {"Ver otro gato","Favorito","Volver"};
            String idGato = gato.getId();
            String opcion = (String) JOptionPane.showInputDialog(null,menu,idGato,JOptionPane.INFORMATION_MESSAGE,fondoGato,botones,botones[0]);
            
            int seleccion = -1;
            for (int i = 0; i < botones.length; i++) {
                if (opcion.equals(botones[i])) {
                    seleccion = i;
                }
            }
            
            switch (seleccion) {
                case 0:
                    verGatos();
                    break;
                case 1: 
                    favoritoGato(gato);
                    break;
                default:
                    break;
            }
        }catch (IOException e){
            System.out.println(e);
        }
        
        
    }

    public static void favoritoGato(Gato gato) {
        try {
            OkHttpClient client = new OkHttpClient(); //El .newBuilder().build() sacarlo
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\r\n  \"image_id\": \""+ gato.getId() +"\"\r\n}");
            Request request = new Request.Builder()
            .url("https://api.thecatapi.com/v1/favourites")
            .method("POST", body)
            .addHeader("Content-Type", "application/json")
            .addHeader("x-api-key", gato.getApiKey())
            .build();
            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    
    public static void verFavoritos(String apiKey) {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
            .url("https://api.thecatapi.com/v1/favourites")
            .method("GET", null)
            .addHeader("x-api-key", apiKey)
            .addHeader("Cookie", "__cfduid=d6d906e4c03f2efbe9a081f22ff2d40db1594285181")
            .build();
            Response response = client.newCall(request).execute();
            //Guardamos la respuesta en String
            String json = response.body().string();
            //Creamos el objeto tipo gson
            Gson gson = new Gson();
            GatoFavorito[] gatoFavArray = gson.fromJson(json, GatoFavorito[].class);
            
            if (gatoFavArray.length > 0) {
                int min = 1;
                int max = gatoFavArray.length;

                for (int i = min; i < max; i++) {
                    GatoFavorito gatoFav = gatoFavArray[i];
                    //Redimensionar la imagen en caso de que sea muy grande (>800)
                    Image image;
                    try{
                        URL url = new URL(gatoFav.getImage().getUrl());
                        //La url https (s = Segura) se convierte en http
                        HttpURLConnection httpcon = (HttpURLConnection)url.openConnection();
                        httpcon.addRequestProperty("User-Agent","");
                        //Se convierte la url a tipo imagen
                        BufferedImage bufferedImage = ImageIO.read(httpcon.getInputStream());
                        ImageIcon fondoGato = new ImageIcon(bufferedImage);

                        //Se convierte a tipo icono ya que el metodo getIconWidth lo requiere   

                        if (fondoGato.getIconWidth() > 800){
                            //Se reemplaza la resolución
                            Image fondo = fondoGato.getImage();
                            Image modificada = fondo.getScaledInstance(800,600,java.awt.Image.SCALE_SMOOTH);
                            fondoGato = new ImageIcon(modificada);
                        } 
                        String menu = "Opciones: \n"
                                + "1. Ver otro gato \n"
                                + "2. Eliminar Favorito \n"
                                + "3. Volver \n";
                        String[] botones = {"Ver otro gato","Eliminar Favorito","Volver"};
                        String idGato = gatoFav.getId();
                        String opcion = (String) JOptionPane.showInputDialog(null,menu,idGato,JOptionPane.INFORMATION_MESSAGE,fondoGato,botones,botones[0]);

                        int seleccion = -1;
                        for (int j = 0; j < botones.length; j++) {
                            if (opcion.equals(botones[j])) {
                                seleccion = j;
                            }
                        }

                        switch (seleccion) {
                            case 0:
                                if (i == max - 1){
                                    verFavoritos(apiKey);
                                    break;
                                } else {
                                    continue;
                                }
                            case 1: 
                                borrarFavorito(gatoFav);
                                if (i == max -1){
                                    verFavoritos(apiKey);
                                    break;
                                } else {
                                    continue;
                                }

                            default:
                                break;
                        }
                    }catch (IOException e){
                        System.out.println(e);
                    }
                }
                
            }
            
        } catch (IOException e) {
            System.out.println(e);
        }
        
    }
    
    public static void borrarFavorito(GatoFavorito gatoFav) {
        try {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
              .url("https://api.thecatapi.com/v1/favourites/" + gatoFav.getId() + "")
              .method("DELETE", body)
              .addHeader("Content-Type", "application/json")
              .addHeader("x-api-key", gatoFav.getApiKey())
              .build();
            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
