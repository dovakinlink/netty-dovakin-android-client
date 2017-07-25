package org.dovakin.push.client.core.codec;

import org.dovakin.push.client.core.protocol.NGLSProtocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.util.List;

/**
 * Created by liuhuanchao on 2017/7/24.
 */
public class NGLSProtocolCodec extends ByteToMessageCodec<NGLSProtocol> {

    // 报文最小长度
    public final int MIN_LENGTH = 8;
    // 报文最大长度
    public final int MAX_LENGTH = 2048;

    /**
     * 协议编码
     * @param ctx
     * @param nglsProtocol
     * @param byteBuf
     * @throws Exception
     */
    protected void encode(ChannelHandlerContext ctx, NGLSProtocol nglsProtocol, ByteBuf byteBuf)
            throws Exception {

        // 填充包头
        byteBuf.writeInt(nglsProtocol.getHEAD());
        // 填充ACTION类型
        byteBuf.writeInt(nglsProtocol.getTYPE());
        // 填充消息长度
        byteBuf.writeInt(nglsProtocol.getContent_length());
        // 填充消息体
        byteBuf.writeBytes(nglsProtocol.getContent());
    }

    /**
     * 协议解码
     * @param ctx
     * @param byteBuf
     * @param list
     * @throws Exception
     */
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list)
            throws Exception {

        // 报文长度不足,退出
        if(byteBuf.readableBytes() < MIN_LENGTH) return;
        // 报文长度过长,判别为非法socket数据,丢弃
        if(byteBuf.readableBytes() > MAX_LENGTH) byteBuf.skipBytes(byteBuf.readableBytes());
        // 原始游标位置
        int index;
        // 开始读取协议包头
        while (true){
            index = byteBuf.readerIndex();
            byteBuf.markReaderIndex();
            // 成功解析协议包头
            if(byteBuf.readInt() == NGLSProtocol.getHEAD()){
                break;
            }
            // 过滤非协议数据
            byteBuf.resetReaderIndex();
            byteBuf.readByte();
            // 粘包处理
            if(byteBuf.readableBytes() < MIN_LENGTH){
                return;
            }
        }
        //解析协议ACTION类型
        int action = byteBuf.readInt();
        int length = byteBuf.readInt();
        // 粘包处理
        if(byteBuf.readableBytes() < length){
            byteBuf.readerIndex(index);
            return;
        }
        // 消息体读取
        byte[] content = new byte[length];
        byteBuf.readBytes(content);

        // 反序列化协议数据
        NGLSProtocol nglsProtocol = new NGLSProtocol(content.length, content);
        nglsProtocol.setTYPE(action);
        list.add(nglsProtocol);
    }
}
