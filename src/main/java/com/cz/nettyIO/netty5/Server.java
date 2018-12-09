package com.cz.nettyIO.netty5;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * 一个Thread + 队列 =  一个单线程池 ==============================>线程安全的，任务是线程串行执行的
 * 线程安全，不会产生阻塞效应，适用对象组
 *
 * 线程不安全，会产生阻塞效应，使用对象池
 *
 * 对象组实现流程图
 *  初始化N个对象，用数组缓存----->从数组中均匀获取一个可用的对象----->使用对象------>无需归还对象
 *
 *  对象池
 *  初始化N个对象，放入队列 -----> 从队列出栈一个对象 ------对列中有对象-------------->使用对象 ---使用完后将对象归还对象池--->返回队列
 *                                    |------无可用对象---->阻塞等待对象池中有可用对象--|
 *                                    |------创建一个新对象-----------------------------|
 */
public class Server {
    public static void main(String[] args) {

            //创建一个服务类
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            /**
             * 创建2个线程池：一个线程监听端口，一个负责数据的读写
             */
            EventLoopGroup boss = new NioEventLoopGroup();//多态；接口引用实现类对象

            EventLoopGroup worker = new NioEventLoopGroup();//多态；接口引用实现类对象
        try{
            //设置线程池
            serverBootstrap.group(boss,worker);

            //设置socket工厂
            serverBootstrap.channel(NioServerSocketChannel.class);

            //设置管道工厂
            serverBootstrap.childHandler(new ChannelInitializer<Channel>() {
                @Override
                protected void initChannel(Channel channel) throws Exception {
                    channel.pipeline().addLast(new StringDecoder());
                    channel.pipeline().addLast(new StringEncoder());
                    channel.pipeline().addLast(new ServerHandler());

                }
            });
            //serverSocketChannel的设置，链接缓冲池的大小
            serverBootstrap.option(ChannelOption.SO_BACKLOG,2048);
            //serverSocketChannel的设置，维持链接的活跃度，清除死链接
            serverBootstrap.option(ChannelOption.SO_KEEPALIVE,true);
            //serverSocketChannel的设置，关闭延发送
            serverBootstrap.option(ChannelOption.TCP_NODELAY,true);

            //服务类绑定端口
            ChannelFuture future = serverBootstrap.bind(10101);
            System.out.println("start");

            //等待服务端关闭
            future.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
