package org.dovakin.push.client.core;

import android.content.Context;

import org.dovakin.push.client.core.bean.AuthAction;
import org.dovakin.push.client.core.event.EventType;
import org.dovakin.push.client.core.exception.AuthParamInvailbleException;
import org.dovakin.push.client.core.exception.ClientInitFailedException;
import org.dovakin.push.client.core.protocol.NGLSProtocol;
import org.dovakin.push.client.core.service.NotifyService;
import org.dovakin.push.client.core.utils.JsonUtil;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by Link on 2017/7/25.
 */
public class NGLSClient {

    /** 客户端实例*/
    private static NGLSClient mInstance = null;
    /** 事件通知接口实例*/
    public static NotifyService mNotifyInstance = null;
    public static Bootstrap b = null;

    private final EventLoopGroup group = new NioEventLoopGroup();
    private Channel globalChannel;

    /** SOCKET读空闲默认时间*/
    private static final int DEFAULT_READER_IDEL_TIME = 0;
    /** SOCKET写空闲默认时间*/
    private static final int DEFAULT_WRITER_IDEL_TIME = 160;
    /** SOCKET全部空闲时间*/
    private static final int DEFAULT_ALL_IDEL_TIME = 0;

    public static int readerIdleTime = DEFAULT_READER_IDEL_TIME;
    public static int writerIdleTime = DEFAULT_WRITER_IDEL_TIME;
    public static int allIdleTime = DEFAULT_ALL_IDEL_TIME;

    public static Context mContext;

    private String ip;
    private int port;

    private NGLSClient(){}

    public void setHeartbeatIdle(int idleTime){
        writerIdleTime = idleTime;
    }

    /**
     * 初始化客户端实例
     * @param context Android应用上下文
     * @return
     */
    public static NGLSClient init(Context context){
       if (mInstance == null) {
           mInstance = new NGLSClient();
           mInstance.mContext = context;
       }
       return mInstance;
    }

    /**
     * 设置NGLS服务器IP地址及端口号
     * @param ip
     * @param port
     * @return
     */
    public NGLSClient address(String ip, int port){
        this.ip = ip;
        this.port = port;
        return this;
    }

    /**
     * 添加自定义事件通知接口实例
     * @param service
     * @return
     */
    public NGLSClient addListener(NotifyService service){
        this.mNotifyInstance = service;
        return this;
    }

    /**
     * 设置心跳间隔
     * @param idle
     * @return
     */
    public NGLSClient setHeartBeatTimeIdle(int idle){
        this.readerIdleTime = idle;
        return this;
    }

    /**
     * 启动NGLS客户端
     */
    public void start(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    b = new Bootstrap();
                    b.group(group)
                            .channel(NioSocketChannel.class)
                            .handler(new NGLSInitializer());

                    final ChannelFuture future = b.connect(ip, port);
                    future.addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture future) throws Exception {
                            if(future.isSuccess()){
                                globalChannel = future.channel();
                            } else {
                                System.out.println("连接失败！...");
                            }
                        }
                    }).sync();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Exception e1){
                    e1.printStackTrace();
                }
            }
        });
        thread.start();
    }


    public void login(AuthAction authAction) throws AuthParamInvailbleException, ClientInitFailedException {
        if (authAction == null) throw new NullPointerException();
        if (authAction.getClientId() == null || authAction.getClientId().isEmpty()){
            throw new AuthParamInvailbleException("Login params vailidty failed");
        }
        if(globalChannel == null)
            throw new ClientInitFailedException("Client Init Failed");
        String content = JsonUtil.beanToString(authAction);
        NGLSProtocol nglsProtocol = new NGLSProtocol(content.length(), content.getBytes());
        nglsProtocol.setTYPE(EventType.AUTH);
        globalChannel.writeAndFlush(nglsProtocol);
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

}
