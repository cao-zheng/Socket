package com.cz.bio.socket;

import lombok.Data;

import java.io.*;
import java.net.Socket;

/**
 * 客户端发消息给服务端，服务端发消息给客户端，而且有时候是同时进行的
 */
@Data
public class ClientAndServerThread implements Runnable{
    private Socket socket;
    private String methodFlag;
    public ClientAndServerThread(Socket socket,String methodFlag){
        this.socket = socket;
        this.methodFlag = methodFlag;
    }
    public void run() {
        try {
            if("get".equals(methodFlag))getMessage(socket);
            if("send".equals(methodFlag)) sendMessage(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  void getMessage(Socket socket) throws IOException {
        InputStream inputStream = socket.getInputStream();//读取字节流
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);//将字节流转换为字符流
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);//放入缓冲区
        String messeage = null;
        while((messeage=bufferedReader.readLine())!=null){
            if("bye".equals(messeage)){
                System.out.println("退出");
                break;
            }
            System.out.println(messeage);
        }
    }
    public void sendMessage(Socket socket) throws IOException{
        OutputStream outputStream = socket.getOutputStream();//写入字节流
        PrintStream printStream = new PrintStream(outputStream);//打印流
        InputStream inputStream = System.in;
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String input = null;
        while((input=bufferedReader.readLine())!=null){
            if("bye".equals(input)){
                printStream.println(input);
                break;
            }
            printStream.println(input);
        }
    }
}
