package com.cz.nettyIO.netty3;

import org.jboss.netty.channel.*;

public class HiHandler extends SimpleChannelHandler {
    /**
     * 接收消息
     */
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        String string = (String)e.getMessage();
        System.out.println("客户端s="+ string);
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
        System.out.println("channelConnected");
        super.channelConnected(ctx, e);
    }
    /**
     * 关闭通道的时候产生的事件
     */
    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        System.out.println("channelDisconnected");
        super.channelDisconnected(ctx, e);
    }
    /**
     * 关闭连接
     */
    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        System.out.println("channelClosed");
        super.channelClosed(ctx, e);
    }
}
