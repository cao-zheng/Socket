package com.cz.nettyIO;

import org.jboss.netty.channel.*;

public class HelloHandler extends SimpleChannelHandler {
    /**
     * 接受消息
     */
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        System.out.println("执行了messageReceived");
        String string = (String) e.getMessage();//获取数据
        System.out.println("s="+string);

        //回写数据，写给客户端
        ctx.getChannel().write("hi");
        super.messageReceived(ctx, e);
    }
    /**
     * 捕获异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        System.out.println("执行了exceptionCaught");
        super.exceptionCaught(ctx, e);
    }
    /**
     * 新的连接
     */
    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        System.out.println("执行了新的连接");
        super.channelConnected(ctx, e);
    }
    /**
     * 关闭连接
     */
    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        System.out.println("关闭连接！");
        super.channelClosed(ctx, e);
    }
    /**
     * 必须是连接已经建立了，关闭通道的时候才会触发事件
     */
    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        System.out.println("执行了channelDisconnected");
        super.channelDisconnected(ctx, e);
    }
}
