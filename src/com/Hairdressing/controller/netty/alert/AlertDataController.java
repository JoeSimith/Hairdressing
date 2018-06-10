package com.Hairdressing.controller.netty.alert;

import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.Hairdressing.controller.netty.base.BaseNettyController;
import com.Hairdressing.netty.ChannelGroupPool;

/**
 * 本Controller为Demo 请勿删改
 */
public class AlertDataController extends BaseNettyController {
    public void alert() {
        try {
            if (!this.checkConnect()) {
                return;
            }
            // 返回应答消息
            String request = ((TextWebSocketFrame) webSocketFrame).text();
            System.out.println("服务端收到：" + request);
            if (logger.isLoggable(Level.FINE)) {
                logger.fine(String.format("%s received %s", channelHandlerContext.channel(), request));
            }
            TextWebSocketFrame tws = new TextWebSocketFrame(new Date().toString() + channelHandlerContext.channel().id() + "：" + request);
            // 群发
            ChannelGroupPool channelGroupPool = ChannelGroupPool.getInstance();
            channelGroupPool.get("com.Hairdressing.controller.netty.alert.AlertDataController").writeAndFlush(tws);
            // 返回【谁发的发给谁】
            // ctx.channel().writeAndFlush(tws);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
