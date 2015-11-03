package com.gome.netty.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

import com.gome.netty.channel.holder.ChannelHolder;
/**
 * 类说明.
 * 此类用来设置当通道断开的时候从channel holder里边清除断开的channel
 * <pre>
 * 修改日期        修改人    修改原因
 * 2015年10月30日    周亮亮    新建
 * </pre>
 */
public class TimeoutHandler extends ReadTimeoutHandler{

	public TimeoutHandler(int timeoutSeconds) {
		super(timeoutSeconds);
	}

	@Override
	protected void readTimedOut(ChannelHandlerContext ctx) throws Exception {
		//通道断开的时候从channel holder中清除channel
		ChannelHolder.remove((SocketChannel)ctx.channel());
		super.readTimedOut(ctx);
	}
}
