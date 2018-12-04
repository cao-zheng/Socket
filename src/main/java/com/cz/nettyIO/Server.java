package com.cz.nettyIO;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * netty 版本大致分为： 3.x 和 4.x  5.x
 * netty可以运用在哪些领域
 *  1.分布式进程通信
 *       例如：hadoop,dubbo,akka等具有分布式功能的框架，底层RPC通信都是基于netty实现的，这些框架使用的netty版本是netty3
 *
 *  2. 游戏服务器开发
 *       最新的游戏服务器公司可能采用了netty4.x或5.x
 *
 *  ServerBootstrap: 这是一个对服务端做配置和启动的类
 *
 *      ChannelPipeline 这是Netty处理请求的责任链。这是一个ChannelHandler的链表，而ChannelHandler就是用来处理网络请求的
 *                      内容的。
 *      ChannelHandler 用来处理网络请求的内容，有ChannelInboundHandler和ChannelOutboundHandler两种
 *      ChannelPipeline会从头到尾顺序调用ChannelInboundHandler处理网络请求内容，从尾到头调用
 *      ChannelOutboundHandler处理网络请求内容。这也是netty用来灵活处理网络请求的机制之一。因为使用的时候可以多个decoder
 *                      和encoder进行组合，从而适应不同的网络协议。而且这种类似分层的方式可以让每一个Handler专注于处理自
 *                      己的任务而不用管上下游。
 *                         这也是pipeline机制的特点。这根TCP/IP协议中的五层和七层的分层机制有异曲同工之处、
 */
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
         * https://www.cnblogs.com/wq920/p/3711237.html
         * 1. 设置一个NIOSocket工厂
         *
         * 1)Boss线程：
         *
         * 每个server服务器都会有一个boss线程，每绑定一个InetSocketAddress都会产生一个boss线程，
         * 比如：我们开启了两个服务器端口80和443，则我们会有两个boss线程。一个boss线程在端口绑定后，会接收传进来的连接，
         * 一旦连接接收成功，boss线程会指派一个worker线程处理连接。
         *
         * 2)Worker线程：
         *
         * 一个NioServerSocketChannelFactory会有一个或者多个worker线程。
         * 一个worker线程在非阻塞模式下为一个或多个Channels提供非阻塞 读或写
         *
         * 2. 线程的生命周期和优雅的关闭
         *
         * 在NioServerSocketChannelFactory被创建的时候，所有的线程都会从指定的Executors中获取。
         * Boss线程从bossExecutor中获取，worker线程从workerExecutor中获取。
         * 因此，我们应该准确的指定Executors可以提供足够数量的线程，最好的选择就是指定一个cached线程池
         * （It is the best bet to specify a cached thread pool）。
         * 此处发现所有源码中的例子(example)中均设置为Executors.newCachedThreadPool()
         *
         * 3. Boss线程和worker线程都是懒加载，没有程序使用的时候要释放掉。
         * 当boss线程和worker线程释放掉的时候，所有的相关资源如Selector也要释放掉。
         * 因此，如果想要优雅的关闭一个服务，需要做一下事情：
         *
         * 对factory创建的channels执行解绑（unbind）操作
         * 关闭所有的由解绑的channels处理的子channels（这两步目前通常通过ChannelGroup.close()来操作）
         * 调用releaseExternalResources()方法
         *
         * 请确保在所有的channels都关闭前不要关闭executor，否则，
         * 会报RejectedExecutionException异常而且相关资源可能不会被释放掉。
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
                 *  StringEncoder和StringDecoder通常我们也习惯将编码(Encode)称为序列化(serialization)
                 *  它将对象序列化为字节数组，用于网络传输、数据持久化或者其他用途
                 *  反之，解码(Decode)/反序列化(deserialization)把从网络、磁盘等读取的字节数组还原成原始对象(通常是原始
                 *  对象的拷贝)，以方便后续业务逻辑的操作。
                 */


                /**
                 * 装一个过滤器，数据经过StringDecoder
                 */
                pipeline.addLast("decoder",new StringDecoder(Charset.forName("UTF-8")));
                /**
                 * 可以写String
                 */
                pipeline.addLast("encoder",new StringEncoder(Charset.forName("UTF-8")));
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
