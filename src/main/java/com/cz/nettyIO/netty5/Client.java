package com.cz.nettyIO.netty5;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Client {
    public static void main(String[] args) {
        //创建一个客户端服务类
        Bootstrap serverBootstrap = new Bootstrap();
        //work EventLoopGroup :接口
        EventLoopGroup worker = new NioEventLoopGroup();

        try {

            //设置线程池
            serverBootstrap.group(worker);

            //设置socket工厂
            serverBootstrap.channel(NioSocketChannel.class);
            //设置管道
            serverBootstrap.handler(new ChannelInitializer<Channel>() {
                @Override
                protected void initChannel(Channel channel) throws Exception {
                    channel.pipeline().addLast(new StringDecoder());
                    channel.pipeline().addLast(new StringEncoder());
                    channel.pipeline().addLast(new ClientHandler());
                }
            });
            //连接服务器IP地址和端口号
            ChannelFuture connect = serverBootstrap.connect("127.0.0.1",10101);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            while(true){
                System.out.println("请输入：");
                String msg = bufferedReader.readLine();
                connect.channel().writeAndFlush(msg);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            worker.shutdownGracefully();
        }
    }
}
