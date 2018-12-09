package com.cz.nettyIO.netty5;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServerHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, String msg) {
        System.out.println("msg:" +msg);

        //回信息给客户端
        channelHandlerContext.channel().writeAndFlush("hi");
        channelHandlerContext.writeAndFlush("hi");
    }

    /**
     * 处理新客户连接
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("执行了channelActive");
        super.channelActive(ctx);
    }

    /**
     * 客户端断开
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("执行了channelInactive");
        super.channelInactive(ctx);
    }
    /**
     * 异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("执行了exceptionCaught");
        super.exceptionCaught(ctx, cause);
    }
}
