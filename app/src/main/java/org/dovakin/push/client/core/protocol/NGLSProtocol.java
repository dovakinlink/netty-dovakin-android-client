package org.dovakin.push.client.core.protocol;

import io.netty.util.CharsetUtil;

/**
 * Created by liuhuanchao on 2017/7/24.
 * NGLS协议
 * v0.1
 */
public class NGLSProtocol {

    /**
     * 协议头部固定数据
     */
    private final static int HEAD = 0x29;


    private int ACTION_TYPE;

    /**
     * 协议正文长度
     */
    private int content_length;

    /**
     * 协议正文
     */
    private byte[] content;

    public NGLSProtocol(int content_length, byte[] content){
        this.content_length = content_length;
        this.content = content;
    }

    public static int getHEAD() {
        return HEAD;
    }

    public int getTYPE() {
        return this.ACTION_TYPE;
    }

    public void setTYPE(int TYPE) {
        this.ACTION_TYPE = TYPE;
    }

    public int getContent_length() {
        return content_length;
    }

    public void setContent_length(int content_length) {
        this.content_length = content_length;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }


    public String contentToString(){
        return new String(this.content, CharsetUtil.UTF_8);
    }

    @Override
    public String toString(){
        return "NGLS \n"
                + "[HEAD]: " + HEAD + "\n"
                + "[CONTENT-LENGTH]: " + content_length + "\n"
                + "[CONTENT]: " + contentToString() + "\n";
    }
}
