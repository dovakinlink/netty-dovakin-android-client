package org.dovakin.push.client.core;

import org.dovakin.push.client.core.codec.NGLSProtocolCodec;
import org.dovakin.push.client.core.handler.NGLSClientHandler;

import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * Created by liuhuanchao on 2017/7/25.
 */

public class NGLSInitializer extends ChannelInitializer<SocketChannel>{

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new IdleStateHandler(0, 4, 0, TimeUnit.SECONDS));
        pipeline.addLast(new NGLSProtocolCodec());
        pipeline.addLast(new NGLSClientHandler());

    }
}
