package com.wing;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class ChatServerHandle extends SimpleChannelInboundHandler<String> {

    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {

        channelGroup.stream()
                .forEach(channel -> {
                    if (channel.equals(channelHandlerContext.channel())) {
                        channel.writeAndFlush("[ 自己 ] : " + msg);
                    } else {
                        channel.writeAndFlush("[ 客户端 ] " + channelHandlerContext.channel().remoteAddress() + " : " + msg);
                    }

                    System.out.println(channelHandlerContext.channel().remoteAddress() + " : " + msg);
                });

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        channelGroup.writeAndFlush(ctx.channel().remoteAddress() + " 上线了");
        channelGroup.add(ctx.channel());
        System.out.println(ctx.channel().remoteAddress() + " 上线了");
    }

}
