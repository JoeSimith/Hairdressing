package com.Hairdressing.netty;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
/**
 * ClassName:BaseNettyController Function: TODO ADD FUNCTION.
 */
public class NettyControllerRouterHandler extends SimpleChannelInboundHandler<Object> {
    private static final Logger logger = Logger.getLogger(WebSocketServerHandshaker.class.getName());
    private WebSocketServerHandshaker handshaker;
    /**
     * channel 通道 action 活跃的 当客户端主动链接服务端的链接后，这个通道就是活跃的了。也就是客户端与服务端建立了通信通道并且可以传输数据
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 添加
        ChannelGroupPool channelGroupPool = ChannelGroupPool.getInstance();
        channelGroupPool.addChannel(ctx);
        System.out.println("客户端与服务端连接开启：" + ctx.channel().remoteAddress().toString());
    }
    /**
     * channel 通道 Inactive 不活跃的 当客户端主动断开服务端的链接后，这个通道就是不活跃的。也就是说客户端与服务端关闭了通信通道并且不可以传输数据
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 移除
        ChannelGroupPool channelGroupPool = ChannelGroupPool.getInstance();
        channelGroupPool.removeChannel(ctx);
        System.out.println("客户端与服务端连接关闭：" + ctx.channel().remoteAddress().toString());
    }
    /**
     * 接收客户端发送的消息 channel 通道 Read 读 简而言之就是从通道中读取数据，也就是服务端接收客户端发来的数据。但是这个数据在不进行解码时它是ByteBuf类型的
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 传统的HTTP接入
        if (msg instanceof FullHttpRequest) {
            handleHttpRequest(ctx, ((FullHttpRequest) msg));
            // WebSocket接入
        } else if (msg instanceof WebSocketFrame) {
            ChannelGroupPool channelGroupPool = ChannelGroupPool.getInstance();
            channelGroupPool.grouping(ctx);
            System.out.println(handshaker.uri());
            String javaClassName=ctx.attr(AttributeKey.valueOf("javaClassName")).toString();
            Class<?> clazz = Class.forName(javaClassName);
            Object obj = clazz.newInstance();
            Method setChannelHandlerContextMethod=clazz.getSuperclass().getMethod("setChannelHandlerContext",ChannelHandlerContext.class);
            setChannelHandlerContextMethod.invoke(obj,ctx);
            Method setWebSocketFrameMethod=clazz.getSuperclass().getMethod("setWebSocketFrame",WebSocketFrame.class);
            setWebSocketFrameMethod.invoke(obj,(WebSocketFrame) msg);
            Method setHandshakerMethod=clazz.getSuperclass().getMethod("setHandshaker",WebSocketServerHandshaker.class);
            setHandshakerMethod.invoke(obj,this.handshaker);
            Method method = clazz.getMethod(ctx.attr(AttributeKey.valueOf("javaMethodName")).toString());
            method.invoke(obj);
//            if("app".equals(ctx.attr(AttributeKey.valueOf("type")).get())){
//                handlerWebSocketFrame(ctx, (WebSocketFrame) msg);
//            }else{
//                handlerWebSocketFrame2(ctx, (WebSocketFrame) msg);
//            }
        }
    }
    /**
     * channel 通道 Read 读取 Complete 完成 在通道读取完成后会在这个方法里通知，对应可以做刷新操作 ctx.flush()
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
    private void handlerWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
        // 判断是否关闭链路的指令
        if (frame instanceof CloseWebSocketFrame) {
            System.out.println(1);
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            return;
        }
        // 判断是否ping消息
        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        // 本例程仅支持文本消息，不支持二进制消息
        if (!(frame instanceof TextWebSocketFrame)) {
            System.out.println("本例程仅支持文本消息，不支持二进制消息");
            throw new UnsupportedOperationException(
                    String.format("%s frame types not supported", frame.getClass().getName()));
        }
        // 返回应答消息
        String request = ((TextWebSocketFrame) frame).text();
        System.out.println("服务端收到：" + request);
        if (logger.isLoggable(Level.FINE)) {
            logger.fine(String.format("%s received %s", ctx.channel(), request));
        }
        TextWebSocketFrame tws = new TextWebSocketFrame(new Date().toString() + ctx.channel().id() + "：" + request);
        // 群发
        ChannelGroupPool channelGroupPool = ChannelGroupPool.getInstance();
        channelGroupPool.get("testGroup").writeAndFlush(tws);
        // 返回【谁发的发给谁】
        // ctx.channel().writeAndFlush(tws);
    }
    private void handlerWebSocketFrame2(ChannelHandlerContext ctx, WebSocketFrame frame) {
        // 判断是否关闭链路的指令
        if (frame instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            return;
        }
        // 判断是否ping消息
        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        // 本例程仅支持文本消息，不支持二进制消息
        if (!(frame instanceof TextWebSocketFrame)) {
            System.out.println("本例程仅支持文本消息，不支持二进制消息");
            throw new UnsupportedOperationException(
                    String.format("%s frame types not supported", frame.getClass().getName()));
        }
        // 返回应答消息
        String request = ((TextWebSocketFrame) frame).text();
        System.out.println("服务端2收到：" + request);
        if (logger.isLoggable(Level.FINE)) {
            logger.fine(String.format("%s received %s", ctx.channel(), request));
        }
        TextWebSocketFrame tws = new TextWebSocketFrame(new Date().toString() + ctx.channel().id() + "：" + request);
        // 群发
        ChannelGroupPool channelGroupPool = ChannelGroupPool.getInstance();
        channelGroupPool.get("testGroup").writeAndFlush(tws);
        // 返回【谁发的发给谁】
        // ctx.channel().writeAndFlush(tws);
    }
    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
        // 如果HTTP解码失败，返回HTTP异常
        if (!req.getDecoderResult().isSuccess() || (!"websocket".equals(req.headers().get("Upgrade")))) {
            sendHttpResponse(ctx, req,
                    new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }
        //获取url后置参数
        HttpMethod method=req.getMethod();
        String uri=req.getUri();
        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(uri);
        Map<String, List<String>> parameters = queryStringDecoder.parameters();
        //类名
        Pattern pattern = Pattern.compile(".*/");
        Matcher matcher = pattern.matcher(uri);
        matcher.find();
        String uriClass=matcher.group(0).replaceAll("/",".");
        uriClass="com.Hairdressing.controller.netty"+uriClass.substring(0,uriClass.length()-1);
        //方法名
        pattern = Pattern.compile("\\w+.ws");
        matcher = pattern.matcher(uri);
        matcher.find();
        String uriMethod = matcher.group(0).replaceAll(".ws","");
        //存入ctx
        ctx.attr(AttributeKey.valueOf("javaClassName")).set(uriClass);
        ctx.attr(AttributeKey.valueOf("javaMethodName")).set(uriMethod);
//        if(method==HttpMethod.GET&&"/webssss".equals(uri)){
//            //....处理
//            ctx.attr(AttributeKey.valueOf("type")).set("app");
//        }else if(method==HttpMethod.GET&&"/websocket".equals(uri)){
//            //...处理
//            ctx.attr(AttributeKey.valueOf("type")).set("live");
//        }
        // 构造握手响应返回，本机测试
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                "ws://"+req.headers().get(HttpHeaders.Names.HOST)+uri, null, false);
        handshaker = wsFactory.newHandshaker(req);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedWebSocketVersionResponse(ctx.channel());
        } else {
            handshaker.handshake(ctx.channel(), req);
        }
    }
    private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, DefaultFullHttpResponse res) {
        // 返回应答给客户端
        if (res.getStatus().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.getStatus().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
        }
        // 如果是非Keep-Alive，关闭连接
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!HttpHeaders.isKeepAlive(req) || res.getStatus().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }
    /**
     * exception 异常 Caught 抓住 抓住异常，当发生异常的时候，可以做一些相应的处理，比如打印日志、关闭链接
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
