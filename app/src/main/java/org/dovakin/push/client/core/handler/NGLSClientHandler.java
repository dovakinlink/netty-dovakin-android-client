package org.dovakin.push.client.core.handler;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;


import org.dovakin.push.client.core.NGLSClient;
import org.dovakin.push.client.core.bean.AuthAction;
import org.dovakin.push.client.core.event.BaseEvent;
import org.dovakin.push.client.core.event.ChannelEvent;
import org.dovakin.push.client.core.event.EventType;
import org.dovakin.push.client.core.protocol.NGLSProtocol;
import org.dovakin.push.client.core.utils.JsonUtil;
import org.greenrobot.eventbus.EventBus;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * Created by liuhuanchao on 2017/7/25.
 */

public class NGLSClientHandler extends SimpleChannelInboundHandler<NGLSProtocol>
           {

    private Context context = NGLSClient.mContext;

    public NGLSClientHandler(){
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        try {
            EventBus.getDefault().post(new ChannelEvent(EventType.ON_ACTIVE, null));
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        try {
            EventBus.getDefault().post(new ChannelEvent(EventType.ON_INACTIVE, null));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, final Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        try {
            EventBus.getDefault().post(new ChannelEvent(EventType.ON_EXCEPTION, cause.getMessage()));
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
            final int action = msg.getTYPE();

/*            final Object obj
                    = JsonUtil.readBytes(msg.getContent(), Object.class);*/
            String obj = new String(msg.getContent());
            EventBus.getDefault().post(new BaseEvent(action, obj));
            //NGLSClient.mNotifyInstance.notify(action, obj);
/*            ((Activity)context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    NGLSClient.mNotifyInstance.notify(action, obj);
                }
            });*/
/*            switch (action){
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
            }*/
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
