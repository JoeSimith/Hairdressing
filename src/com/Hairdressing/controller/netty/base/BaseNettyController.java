package com.Hairdressing.controller.netty.base;


import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.websocketx.*;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public abstract class BaseNettyController {
    protected ChannelHandlerContext channelHandlerContext;
    protected WebSocketFrame webSocketFrame;
    protected WebSocketServerHandshaker handshaker;
    protected static final Logger logger = Logger.getLogger(WebSocketServerHandshaker.class.getName());

    public ChannelHandlerContext getChannelHandlerContext() {
        return channelHandlerContext;
    }

    public void setChannelHandlerContext(ChannelHandlerContext channelHandlerContext) {
        this.channelHandlerContext = channelHandlerContext;
    }

    public WebSocketFrame getWebSocketFrame() {
        return webSocketFrame;
    }

    public void setWebSocketFrame(WebSocketFrame webSocketFrame) {
        this.webSocketFrame = webSocketFrame;
    }

    public WebSocketServerHandshaker getHandshaker() {
        return handshaker;
    }

    public void setHandshaker(WebSocketServerHandshaker handshaker) {
        this.handshaker = handshaker;
    }

    public boolean checkConnect(){
        // 判断是否关闭链路的指令
        if (this.webSocketFrame instanceof CloseWebSocketFrame) {
            this.handshaker.close(this.channelHandlerContext.channel(), (CloseWebSocketFrame) this.webSocketFrame.retain());
            return false;
        }
        // 判断是否ping消息
        if (this.webSocketFrame instanceof PingWebSocketFrame) {
            this.channelHandlerContext.channel().write(new PongWebSocketFrame(this.webSocketFrame.content().retain()));
            return false;
        }
        // 本例程仅支持文本消息，不支持二进制消息
        if (!(this.webSocketFrame instanceof TextWebSocketFrame)) {
            System.out.println("本例程仅支持文本消息，不支持二进制消息");
            throw new UnsupportedOperationException(
                    String.format("%s frame types not supported", this.webSocketFrame.getClass().getName()));
        }
        return  true;
    }

}
