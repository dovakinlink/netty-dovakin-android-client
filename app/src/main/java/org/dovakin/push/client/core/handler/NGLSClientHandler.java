package org.dovakin.push.client.core.handler;

import android.app.Activity;
import android.content.Context;

import org.dovakin.push.client.NGLSClient;
import org.dovakin.push.client.core.bean.PushAction;
import org.dovakin.push.client.core.event.EventType;
import org.dovakin.push.client.core.protocol.NGLSProtocol;
import org.dovakin.push.client.core.utils.JsonUtil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by liuhuanchao on 2017/7/25.
 */

public class NGLSClientHandler extends SimpleChannelInboundHandler<NGLSProtocol>{

    private Context context = NGLSClient.mContext;

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, NGLSProtocol msg) throws Exception {

        try {
            int action = msg.getTYPE();
            switch (action){
                case EventType.PUSH:
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
                case EventType.HEART:
                    break;
                default:
                    break;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }


}
