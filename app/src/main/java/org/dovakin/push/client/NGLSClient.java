package org.dovakin.push.client;

import android.content.Context;

import org.dovakin.push.client.core.NGLSInitializer;
import org.dovakin.push.client.core.service.NotifyService;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by liuhuanchao on 2017/7/25.
 */

public class NGLSClient {

    private static NGLSClient mInstance = null;

    public static NotifyService mNotifyInstance = null;

    private final EventLoopGroup group = new NioEventLoopGroup();

    private static final int DEFAULT_READER_IDEL_TIME = 0;
    private static final int DEFAULT_WRITER_IDEL_TIME = 4;
    private static final int DEFAULT_ALL_IDEL_TIME = 0;

    public static int readerIdleTime = DEFAULT_READER_IDEL_TIME;
    public static int writerIdleTime = DEFAULT_WRITER_IDEL_TIME;
    public static int allIdleTime = DEFAULT_ALL_IDEL_TIME;

    public static Context mContext;

    private String ip;
    private int port;

    private NGLSClient(){}

    public static NGLSClient init(Context context){
       if (mInstance == null) {
           mInstance = new NGLSClient();
           mInstance.mContext = context;
       }
       return mInstance;
    }


    public NGLSClient address(String ip, int port){
        this.ip = ip;
        this.port = port;
        return this;
    }

    public NGLSClient addListener(NotifyService service){
        this.mNotifyInstance = service;
        return this;
    }

    public void start(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Bootstrap b = new Bootstrap();
                    b.group(group)
                            .channel(NioSocketChannel.class)
                            .option(ChannelOption.SO_KEEPALIVE, true)
                            .handler(new NGLSInitializer());

                    final ChannelFuture future = b.connect(ip, port);
                    future.sync();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Exception e1){
                    e1.printStackTrace();
                }
            }
        });
        thread.start();

    }


}
