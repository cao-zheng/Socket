package com.cz.nettyIO.netty3;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {
    public static void main(String[] args) {
        /**
         * 创建一个客户端服务类
         */
        ClientBootstrap clientBootstrap = new ClientBootstrap();
        /**
         * 创建2个线程池：一个线程监听端口，一个负责数据的读写
         */
        ExecutorService boss = Executors.newCachedThreadPool();
        ExecutorService worker = Executors.newCachedThreadPool();

        //socket工厂
        clientBootstrap.setFactory(new NioClientSocketChannelFactory(boss,worker));

        clientBootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline pipeline = Channels.pipeline();
                /**
                 * 装一个过滤器，数据经过StringDecoder
                 */
                pipeline.addLast("decoder",new StringDecoder(Charset.forName("UTF-8")));
                /**
                 * 可以写String
                 */
                pipeline.addLast("encoder",new StringEncoder(Charset.forName("UTF-8")));

                pipeline.addLast("hiHandler",new HiHandler());

                return pipeline;
            }
        });

        //连接服务端
        ChannelFuture connectChannelFuture = clientBootstrap.connect(new InetSocketAddress("127.0.0.1",10101));

        //获取管道通道
        Channel channel = connectChannelFuture.getChannel();
        System.out.println("client start");

        //在客户端给服务端发送消息
        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.print("请输入：");
            String msg = scanner.next();
            channel.write(msg);
        }
    }
}
