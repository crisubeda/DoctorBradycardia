/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import Pojos.Patient;
import interfaces.DoctorLoginWindow;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author carmen
 */
public class ConnectionWithServer {

    public static String[] getDataFromFile() throws IOException {
        File file2 = new File(".");
        String path = file2.getAbsolutePath();
        //we want to extract the information of the file DattaConnection.txt
        String goodpath = file2.getAbsolutePath().substring(0, path.length() - 2).concat("/files/DataConnection.txt");
        FileReader br = null;
        String[] datos = new String[5];
        try {
            br = new FileReader(goodpath);

            int caract;
            int i = 0;
            char a;
            String dt = "";
            while ((caract = br.read()) != -1) {
                a = (char) caract;
                if (a != '#') {
                    dt = dt + a;
                } else {
                    datos[i] = dt;
                    i++;
                    dt = "";
                    while (a != ';' || caract == -1) {
                        caract = br.read();
                        a = (char) caract;
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            throw new IOException("File not found");
        } catch (IOException e) {
            throw new IOException("Error");
        }
        //datos[0] ---> IP SERVER SOCKET
        //datos[1] ---> PORT SERVER SOCKET
        //datos[2] ---> IP DB SERVER
        //datos[3] ---> PORT DB SERVER
        System.out.println(datos[1]);
        System.out.println(Utilities.Exceptions.checkFloat(datos[1]));
        return datos;
    }

    public static Socket connectToServer() {
        boolean error = false;
        Socket socket1 = new Socket();
        try {
            String[] datos = getDataFromFile();
            //in datos[1] we have the number of the port whereas in datos[0] the ip address
            int ip = Utilities.Exceptions.convertInt(datos[1]);
            socket1 = new Socket(datos[0], ip);
            System.out.println("socket info1: " + socket1.getLocalPort());
        } catch (IOException ex) {
            error = true;
        }
        return socket1;
    }
//public static PrintWi

    public static boolean receiveData(Socket socket, BufferedReader bufferedReader) {
        boolean received = true;
        try {
            // bufferedReader = new BufferedReader(
            //  new InputStreamReader(socket.getInputStream()));
            String[] datos = new String[100];
            //vamos a leer toda la información que pasa el server del paciente
            System.out.println("Ha entrado en receiveData");
            String line = bufferedReader.readLine();
            System.out.println("la linea es: " +line);
            int i = 0;
            int contador = 0;
            String data = "";
            switch (line.charAt(0)) {
                case 'd':
                    while (line.charAt(i + 2) != '#') {
                        while (line.charAt(i + 2) != ';') {
                            data = data.concat(Character.toString(line.charAt(i + 2)));
                            //System.out.println("El contador de chars en con: " + i + " el string: " + cap);
                            i++;
                        }
                        //System.out.println("Con es:" + con);
                        datos[contador] = data;
                        i++;
                        System.out.println("El contador de i es: " +contador + "el dato es: " +datos[contador]);
                        contador++;
                        data = "";
                    }
                    if(datos[3].equals("null")){
                        System.out.println("El email es null");
                        received=false;
                    }
                    else{
                        System.out.println("el email es diferente de null");
                        if (Exceptions.checkInt(datos[0])) {
                            DoctorLoginWindow.doctor.setID(Exceptions.convertInt(datos[0]));
                        } else {
                            System.out.println("no se puede pasar");
                            DoctorLoginWindow.doctor.setID(30);
                        }
                        DoctorLoginWindow.doctor.setFullName(datos[1]);
                        DoctorLoginWindow.doctor.setUsername(datos[2]);
                        DoctorLoginWindow.doctor.setEmail(datos[5]);
                        //PatientPrincipalWindow.patient.setPassword(password);
                        received=true;
                    }
                    /*System.out.println("1: " + datos[0]);
                    System.out.println("2: " + datos[1]);
                    System.out.println("3: " + datos[2]);
                    System.out.println("4: " + datos[3]);
                    System.out.println("5: " + datos[4]);
                    System.out.println("6: " + datos[5]);
                    System.out.println("7: " + datos[7]);
                    System.out.println("Error despues de esto");*/
                    // return "p#" + ID + ";" + fullName + ";" + username + ";" + address + ";" + phonenumber + ";" +
                    //email + ";" + diagnosis + ";" + docId + ";" + password + ";" + macBitalino + ";" + bitalino + ";#";
                    break;
            }
        } catch (IOException ex) {
            received=false;
        }
        return received;
    }

    public static void sendDoctor(Socket socket, PrintWriter printWriter, String username, String password) {
        boolean error = false;
        //try {
        //OutputStream outputStream = new OutputStream();
        //outputStream = socket.getOutputStream();+
        System.out.println("socket info: " + socket.getLocalPort());
        String send = "d#" + username + ";" + password;
        //se manda al server un String que contiene el usuario y el password
        printWriter.println(send);
    }

    public static boolean sendDataToServer(Socket socket, String data) {
        //File To Read
        int character;
        int i = 0;
        boolean error = false;
        try {
            OutputStream outputStream = socket.getOutputStream();
            while (i != data.length()) {
                character = data.charAt(i);
                System.out.println(character);
                outputStream.write(character);
                outputStream.flush();
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ex) {
                    error = true;
                    //Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (IOException e) {

        }
        return error;
    }

    public static boolean exitFromServer(OutputStream outputStream, Socket socket) {
        boolean error = false;
        try {
            try {
                outputStream.close();
            } catch (IOException ex) {
                error = true;
                //Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
            socket.close();
        } catch (IOException ex) {
            error = true;
        }
        return error;
    }

    public static void sendNewPatient(Socket socket, PrintWriter printWriter, Patient patient) {
        boolean error = false;
        printWriter.println(patient.toString());
    }

    public static void sendSomething(Socket socket, PrintWriter printWriter, String mes) {
        printWriter.println(mes);
    }
    
    public static String receiveSomething(Socket socket, BufferedReader bf) {
          String line = "";
           try {
               line = bf.readLine();
           } catch (IOException ex) {
               //Logger.getLogger(ConnectionWithServer.class.getName()).log(Level.SEVERE, null, ex);
               line = "Error";
           }
          return line;            
     }
}


