package org.dovakin.push.client.core;

import org.dovakin.push.client.NGLSClient;
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
        /** 心跳检测处理器*/
        pipeline.addLast(
                new IdleStateHandler(NGLSClient.readerIdleTime,
                        NGLSClient.writerIdleTime,
                        NGLSClient.allIdleTime,
                        TimeUnit.SECONDS));
        /** NGLS协议编解码器*/
        pipeline.addLast(new NGLSProtocolCodec());
        /** NGLS数据处理*/
        pipeline.addLast(new NGLSClientHandler());

    }
}
