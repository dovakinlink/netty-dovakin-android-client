package org.dovakin.push.client.core.handler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;

/**
 * Created by Link on 2017/8/7.
 */

@ChannelHandler.Sharable
public class ConnectionWatcher extends ChannelInboundHandlerAdapter
            implements TimerTask{
    private final Bootstrap bootstrap;
    private final Timer timer;
    private final int port;

    private final String host;

    private volatile boolean reconnect = true;
    private int attempts;

    public ConnectionWatcher(Bootstrap bootstrap, Timer timer, String host, int port, boolean reconnect){
        this.bootstrap = bootstrap;
        this.timer = timer;
        this.host = host;
        this.port = port;
        this.reconnect = reconnect;
    }

    @Override
    public void run(Timeout timeout) throws Exception {

    }
}
