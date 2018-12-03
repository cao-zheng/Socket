package com.cz.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NioServer {

    private Selector selector;
    public void init(int port) throws IOException {
        //创建NIO通道，获取一个serverSocket通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //设置该通道为非阻塞
        serverSocketChannel.configureBlocking(false);
        //创建基于NIO通道的socket连接
        ServerSocket serverSocket = serverSocketChannel.socket();
        //新建socket通道的端口，将该通道对应的Server
        serverSocket.bind(new InetSocketAddress(port));
        //获取一个通道管理器
        selector = Selector.open();
        //将该通道管理器和通道绑定，并注册SelectionKey.OP_ACCEPT事情
        //当该事件到达的时候，selector.select()会返回，如果该事件没有到达selector.select()会一直阻塞
        //讲NIO通道绑定到选择器，绑定后分配的主键为selectionKey
        SelectionKey selectionKey = serverSocketChannel.register(selector,SelectionKey.OP_ACCEPT);
    }

    /**
     * 采用轮询的方式监听selector,是否需要处理事件，如果有，就进行处理
     * 轮询：由CPU定时发起询问。依次询问每一个周边设备是否需要服务，有即给服务。服务结束后再问下一个。
     */
    public void listen() throws IOException {
        System.out.println("服务启动成功");
        //轮询访问selector
        while(true){
            //当注册的事件到达的时候，方法返回，否则方法就会一直阻塞
            //获取通道内是否需要有选择器的服务事件
            int num = selector.select();
            if(num < 1){
                continue;
            }
            /**
             * 获取通道服务事件的集合   //一个是新顾客的事件，一个是老顾客（前面已经有对应的服务员）
             */
            Set selectKeys = selector.selectedKeys();
            Iterator it = selectKeys.iterator();
            while(it.hasNext()){
                //得到该顾客具体的key
                SelectionKey key = (SelectionKey)it.next();
                //删除已经处理的事件，以防止重复处理
                it.remove();
                handler(key);
            }

        }
    }
    /**
     * 处理请求，是老顾客还是新顾客
     */
    public void handler(SelectionKey key){
        //判断是不是新顾客，测试此秘钥的通道是否已准备好接受新的套接字连接。
        if(key.isAcceptable()){
            try {
                handlerAccept(key);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(key.isReadable()){//测试此秘钥的频道是否可以阅读。是否是老顾客
            try {
                handlerRead(key);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 新顾客 处理连接请求
     * @param key
     */
    public void handlerAccept(SelectionKey key) throws IOException {
        //新顾客，新注册
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel)key.channel();
        //获取与客户端连接的通道
        SocketChannel socketChannel = serverSocketChannel.accept();
        //设置成非阻塞false,如果为true，有异常
        socketChannel.configureBlocking(false);

        //可以给客户端发送信息了
        System.out.println("新的客户端连接");
        //再和客户端连接成功后，为了接收到客户端的信息，需要给通道设置权限
        SelectionKey newkey = socketChannel.register(selector,SelectionKey.OP_READ);
    }

    /**
     * 老顾客 处理读事件
     * @param key
     */
    public void handlerRead(SelectionKey key) throws IOException {
        //服务器可读取客户端的信息，得到事件发送的Socket通道 //读取数据，接收顾客信息
        SocketChannel socketChannel = (SocketChannel)key.channel();

        //创建读取缓冲区，一次能接收字节数  相当于前面的byte数组，存储读取的数据
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        //读取字节往ByteBuffer里面读，read：长度的个数
        int read = socketChannel.read(buffer);
        if(read>0){
            byte[] data = buffer.array();//把buffer变成字节数组
            String msg = new String(data).trim();//如果1024中有空白，去除
            System.out.println("服务器收到的信息：" + msg);

            //回写数据,给客户端发送信息
            ByteBuffer outBuffer = ByteBuffer.wrap("OK".getBytes());
            socketChannel.write(outBuffer); //将消息写给客户端
        }else{
            System.out.println("客户端关闭");
            key.cancel();
        }
    }

    public static void main(String[] args) throws IOException {
        NioServer nioServer = new NioServer();
        nioServer.init(8000);
        nioServer.listen();
    }
}
