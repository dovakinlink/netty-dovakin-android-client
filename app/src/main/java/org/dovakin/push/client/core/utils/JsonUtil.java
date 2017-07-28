package org.dovakin.push.client.core.utils;

import com.google.gson.Gson;

import java.nio.charset.Charset;

import io.netty.util.CharsetUtil;

/**
 * Created by liuhuanchao on 2017/7/25.
 */

public class JsonUtil {

    private static Gson gson = new Gson();

    public static <T> T readBytes(byte[] bytes, Class<T> valueType ){
        try {
            return gson.fromJson(new String(bytes, CharsetUtil.UTF_8), valueType);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static <T> String beanToString(T bean){
        try {
            return gson.toJson(bean, bean.getClass());
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
