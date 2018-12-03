package com.cz.bio.io;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerIO {
    @Test
    public void server() throws IOException {
       ServerSocket serverSocket = new ServerSocket(8081);
        ExecutorService executorService = Executors.newCachedThreadPool();
        while(true) {
            final Socket socket = serverSocket.accept();
            System.out.println("来了一个客户端");
            executorService.execute(new Runnable() {
                public void run() {
                    try {
                        runContent(socket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
    private void runContent(Socket socket) throws IOException {
        InputStream inputStream = socket.getInputStream();
        //把接收的信息存入字节数组
        byte[] bytes = new byte[1024];
        while(true){
            //字节输出流读取信息到字节数组里面，read：接收信息的字节个数
            int read = inputStream.read(bytes);
            //如果是-1说明没有，所以不能等于-1
            if(read != -1){
                //字节数组变成字符串，0:起始下标，read:个数长度
                String messageString = new String(bytes,0,read);
                System.out.println(messageString);
                System.out.println(123);
            }else{
                break;//-1是没有，跳出死循环
            }
        }
        System.out.println("关闭socket");

    }
}
