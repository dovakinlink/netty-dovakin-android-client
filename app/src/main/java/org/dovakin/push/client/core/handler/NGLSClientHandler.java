package org.dovakin.push.client.core.handler;

import android.app.Activity;
import android.content.Context;

import com.google.gson.Gson;

import org.dovakin.push.client.core.NGLSClient;
import org.dovakin.push.client.core.bean.PushAction;
import org.dovakin.push.client.core.event.EventType;
import org.dovakin.push.client.core.protocol.NGLSProtocol;
import org.dovakin.push.client.core.utils.JsonUtil;

import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;

/**
 * Created by liuhuanchao on 2017/7/25.
 */

public class NGLSClientHandler extends SimpleChannelInboundHandler<NGLSProtocol>
            /*implements TimerTask*/{

    private Context context = NGLSClient.mContext;
/*    private final Bootstrap bootstrap;
    private final Timer timer;
    private final String host;
    private final int port;
    private volatile boolean reconnect = true;
    private int attemps;*/

    public NGLSClientHandler(/*Bootstrap bootstrap, Timer timer, int port, String host, boolean reconnect*/){
/*        this.bootstrap = bootstrap;
        this.timer = new HashedWheelTimer();
        this.port = port;
        this.host = host;
        this.reconnect = reconnect;*/
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        try {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    NGLSClient.mNotifyInstance.onActive();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        try {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    NGLSClient.mNotifyInstance.onInactive();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
/*
        if(reconnect){
            if (attemps < 12){
                attemps++;
                int timeout = 2 << attemps;
                timer.newTimeout(this,timeout, TimeUnit.MILLISECONDS);
            }
        }*/

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, final Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        try {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    NGLSClient.mNotifyInstance.onException(cause.getMessage());
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent){
            IdleState state = ((IdleStateEvent) evt).state();
            if(state == IdleState.WRITER_IDLE){
                NGLSProtocol protocol = new NGLSProtocol(0, new byte[]{});
                protocol.setTYPE(EventType.HEART);
                ctx.writeAndFlush(protocol);
            } else {
                super.userEventTriggered(ctx, evt);
            }
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, NGLSProtocol msg) throws Exception {

        try {
            //TODO 设计事件分发机制
            int action = msg.getTYPE();
            switch (action){
                case EventType.PUSH:
                    String str = new String(msg.getContent(), CharsetUtil.UTF_8);
                    PushAction a = new Gson().fromJson(str,PushAction.class);
                    final PushAction pushAction
                            = JsonUtil.readBytes(msg.getContent(), PushAction.class);
                    if (pushAction != null){
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                NGLSClient.mNotifyInstance.notify(pushAction);
                            }
                        });
                    }
                    break;
                case EventType.AUTH:
                    break;
                case EventType.AUTH_SUCCESS:
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            NGLSClient.mNotifyInstance.onNGLSActive();
                        }
                    });
                    break;
                case EventType.AUTH_FAILED:
                    break;
                case EventType.HEART:
                    break;
                default:
                    break;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

/*    @Override
    public void run(Timeout timeout) throws Exception {
        ChannelFuture future;
        synchronized (NGLSClient.b){
            future = NGLSClient.b.connect(host, port);
        }
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                boolean isSuccess = future.isSuccess();
                if(!isSuccess){
                    future.channel().pipeline().fireChannelInactive();
                } else {
                    System.out.println("重连成功！");
                }
            }
        });
    }*/
}
