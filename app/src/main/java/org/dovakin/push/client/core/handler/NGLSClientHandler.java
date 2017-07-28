package org.dovakin.push.client.core.handler;

import android.app.Activity;
import android.content.Context;

import com.google.gson.Gson;

import org.dovakin.push.client.core.NGLSClient;
import org.dovakin.push.client.core.bean.AuthAction;
import org.dovakin.push.client.core.bean.PushAction;
import org.dovakin.push.client.core.event.EventType;
import org.dovakin.push.client.core.protocol.NGLSProtocol;
import org.dovakin.push.client.core.utils.JsonUtil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * Created by liuhuanchao on 2017/7/25.
 */

public class NGLSClientHandler extends SimpleChannelInboundHandler<NGLSProtocol>{

    private Context context = NGLSClient.mContext;

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

    //TODO 添加心跳机制
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
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
}
