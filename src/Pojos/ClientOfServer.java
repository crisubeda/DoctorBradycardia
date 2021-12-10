/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pojos;

import java.io.*;
import java.net.Socket;

/**
 *
 * @author carmen
 */
public class ClientOfServer implements Runnable {

    int byteRead;
    Socket socket;
    boolean[] error;

    public ClientOfServer(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        InputStream inputStream = null;
        try {
            inputStream = socket.getInputStream();
            while ((byteRead = inputStream.read()) != -1) {
                char caracter = (char) byteRead;
            }
        } catch (IOException ex) {
            error[3] = true;
            //Logger.getLogger(ClientOfServer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            error = releaseResourcesClient(inputStream, socket);
        }

    }

    private static boolean[] releaseResourcesClient(InputStream inputStream, Socket socket) {
        boolean error1 = false, error2 = false;
        boolean[] errors = {error1, error2};

        try {
            inputStream.close();
        } catch (IOException ex) {
            error1 = true;
        }
        try {
            socket.close();
        } catch (IOException ex) {
            error2 = true;
        }
        errors[0] = error1;
        errors[1] = error2;
        return errors;
    }
}
