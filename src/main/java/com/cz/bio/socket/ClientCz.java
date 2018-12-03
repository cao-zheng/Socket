package com.cz.bio.socket;

import java.io.*;
import java.net.Socket;

public class ClientCz {
    public void client1() throws IOException {
        Socket socket = new Socket("127.0.0.1",8080);
        new Thread(new ClientAndServerThread(socket,"send")).start();
        new Thread(new ClientAndServerThread(socket,"get")).start();
    }
    public static void main(String[] args) {
        ClientCz clientCz = new ClientCz();
        try {
            clientCz.client1();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
