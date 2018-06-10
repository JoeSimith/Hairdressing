package com.Hairdressing.controller.netty.disasterAlarm;

import com.Hairdressing.controller.netty.base.BaseNettyController;
import com.Hairdressing.netty.ChannelGroupPool;

import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class DisasterAlarmNettyController extends BaseNettyController {
    public boolean listening() {
        try {
            if (!this.checkConnect()) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean alarm(String data) {
        try {
            TextWebSocketFrame tws = new TextWebSocketFrame(data);
            // 群发
            ChannelGroupPool channelGroupPool = ChannelGroupPool.getInstance();
            ChannelGroup channels = channelGroupPool.get("com.Hairdressing.controller.netty.disasterAlarm.DisasterAlarmNettyController");
            channels.writeAndFlush(tws);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
