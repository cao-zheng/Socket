package com.cz.nettyIO;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static void main(String[] args) {
        //创建一个服务类
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        /**
         * 创建2个线程池：一个线程监听端口，一个负责数据的读写
         */
        ExecutorService boss = Executors.newCachedThreadPool();
        ExecutorService worker = Executors.newCachedThreadPool();
        /**
         * 设置一个NIOSocket工厂
         */
        serverBootstrap.setFactory(new NioServerSocketChannelFactory(boss,worker));
        /**
         * 设置管道的工厂
         * ChannelPipelineFactory;
         */
        serverBootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() throws Exception {
                /**
                 * 获取一个管道，管道可以理解为很多的过滤器
                 */
                ChannelPipeline pipeline = Channels.pipeline();
                /**
                 * 装一个过滤器，数据经过StringDecoder
                 */
                pipeline.addLast("decoder",new StringDecoder());
                /**
                 * 可以写String
                 */
                pipeline.addLast("encoder",new StringEncoder());
                /**
                 * 接受消息的过滤器
                 */
                pipeline.addLast("helloHandler",new HelloHandler());

                return pipeline;
            }
        });

        //服务类绑定端口
        serverBootstrap.bind(new InetSocketAddress(10101));
        System.out.println("start");

    }
}
