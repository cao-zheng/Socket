package com.cz.bio.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerCz {
    public void server() throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);//创建服务
        Socket socket = serverSocket.accept();//与客户端连接
        new Thread(new ClientAndServerThread(socket,"get")).start();//接受客户端信息
        new Thread(new ClientAndServerThread(socket,"send")).start();//发信息给客户端
    }

    public static void main(String[] args) {
        ServerCz serverCz = new ServerCz();
        try {
            serverCz.server();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
