package org.dovakin.push.client;

import org.dovakin.push.client.core.NGLSInitializer;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by liuhuanchao on 2017/7/25.
 */

public class NGLSClient {

    private static NGLSClient mInstance = null;

    private final EventLoopGroup group = new NioEventLoopGroup();

    private String ip;
    private int port;

    private NGLSClient(){}

    public static NGLSClient init(){
       if (mInstance == null) {
           mInstance = new NGLSClient();
       }
       return mInstance;
    }


    public NGLSClient address(String ip, int port){
        this.ip = ip;
        this.port = port;
        return this;
    }

    public void start(){
        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new NGLSInitializer());
    }
}
