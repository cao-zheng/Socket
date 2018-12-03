import jdk.internal.util.xml.impl.Input;
import org.junit.Test;

import java.awt.print.PrinterGraphics;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/*
 * ServerSocket 服务器套接字
 * Socket   客户端
 */
public class SocketTest {

    @Test
    public void server() throws IOException {
        System.out.println("创建服务器");
        ServerSocket serverSocket = new ServerSocket(8000);//创建服务器
        System.out.println("等待客户端链接");
        java.net.Socket socket = serverSocket.accept();//等待客户端连接     阻塞了
        System.out.println("客户端与服务器端连接成功");
        InputStream inputStream = socket.getInputStream();//接收客户端消息，获取字节输入流
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);//字节流转换为字符串
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);//缓冲流，加快速度，也叫管道流
        String messageString =bufferedReader.readLine();//读取客户端发送过来的消息
        System.out.println("来自客户端消息--->" + messageString);
    }
    @Test
    public void client() throws IOException {
        System.out.println("连接服务器");
        java.net.Socket socket = new java.net.Socket("127.0.0.1",8000);
        System.out.println("连接服务器成功");
        OutputStream outputStream = socket.getOutputStream();//套接字获取字节输出流对象
        PrintStream printStream = new PrintStream(outputStream);//打印流
        printStream.println("client--->hello world");//打印给服务器
    }



    public void print(){
        //System.out.println("hello world");

        PrintStream printStream = System.out;
        printStream.print("hello world");
    }

    @Test
    public void server1() throws IOException {
        ServerSocket serverSocket = new ServerSocket(9081);//创建服务器
        java.net.Socket socket = serverSocket.accept();//等待客户端连接
        InputStream inputStream = socket.getInputStream();//读取字节流
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);//获取字符流
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);//放入缓冲区
        String messageString = null;
        while((messageString = bufferedReader.readLine())!=null){//读取缓冲区一行内容
            if(messageString.equals("bye"))break;
            System.out.println("来自客户端" + messageString);
        }
    }
    public void client1() throws IOException {
        java.net.Socket socket = new java.net.Socket("127.0.0.1",9081);//连接服务器
        OutputStream outputStream = socket.getOutputStream();//获取字节输出流对象
        PrintStream printStream = new PrintStream(outputStream);//打印流
        InputStream inputStream = System.in;//读取从控制台输入信息
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);//字节流转换为字符流
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);//加入缓冲流
        String string = null;
        while((string = bufferedReader.readLine())!=null){
            if(string.equals("bye")){
                printStream.println(string);
                break;
            }
            printStream.println(string);//写入
        }
    }
    public static void main(String[] args) {
        SocketTest socketTest = new SocketTest();
        try {
            socketTest.client1();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
