package com.Hairdressing.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.Hairdressing.util.Token;
import com.Hairdressing.util.TokenPool;

/**
 * ClassName:Global
 * Function: TODO ADD FUNCTION.
 */
public class ChannelGroupPool extends ConcurrentHashMap<String,ChannelGroup> {
    private static final long serialVersionUID = 1L;
    private static ChannelGroupPool channelGroupPool;
    private ChannelGroupPool() {}
    public static synchronized ChannelGroupPool getInstance() {
        if(channelGroupPool==null) {
            channelGroupPool=new ChannelGroupPool();
            cleanPool();
        }
        return channelGroupPool;
    }
    public boolean addChannel(ChannelHandlerContext channelHandlerContext) {
        String javaClassName=channelHandlerContext.attr(AttributeKey.valueOf("javaClassName")).toString();
        ChannelGroup group = channelGroupPool.get(javaClassName);
        if(group==null){
            group=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
            group.add(channelHandlerContext.channel());
            channelGroupPool.put(javaClassName,group);
        }else{
            group.add(channelHandlerContext.channel());
        }
        return true;
    }

    public boolean grouping(ChannelHandlerContext channelHandlerContext) {
        ChannelGroup group = channelGroupPool.get("null");
        if(group==null){
            return true;
        }else{
            group.remove(channelHandlerContext.channel());
        }
        this.addChannel(channelHandlerContext);
        return true;
    }

    public Channel getChannel(String groupName, ChannelId ChannelID) {
        ChannelGroup group = channelGroupPool.get(groupName);
        if(group==null){
            return null;
        }else{
            return group.find(ChannelID);
        }
    }

    public boolean removeChannel(ChannelHandlerContext channelHandlerContext) {
        String javaClassName=channelHandlerContext.attr(AttributeKey.valueOf("javaClassName")).toString();
        ChannelGroup group = channelGroupPool.get(javaClassName);
        if(group==null){
            return true;
        }else{
            group.remove(channelHandlerContext.channel());
        }
        return true;
    }

    public static synchronized boolean cleanPool() {
        channelGroupPool.clear();
        return true;
    }

}
