package com.cz.nettyIO.netty5;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
        System.out.println("客户端收到信息：>" + msg);
    }
}
