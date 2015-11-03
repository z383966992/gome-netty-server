package com.gome.netty.handlers;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import com.gome.netty.struct.Message;
import com.gome.netty.struct.MessageType;

/**
 * 类说明.
 * 服务器端心跳应答
 * <pre>
 * 修改日期        修改人    修改原因
 * 2015年10月29日    周亮亮    新建
 * </pre>
 */
public class HeartBeatResponseHandler extends ChannelHandlerAdapter {
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		Message message = (Message) msg;
		// 返回心跳应答消息
		if (message != null
				&& message.getMessageType() == MessageType.HEART_BEAT_REQUEST
						.value()) {
			System.out.println("Receive client heart beat message : ---> "
					+ message);
			Message heartBeat = buildHeatBeat();
			System.out
					.println("Send heart beat response message to client : ---> "
							+ heartBeat);
			ctx.writeAndFlush(heartBeat);
		} else
			ctx.fireChannelRead(msg);
	}

	private Message buildHeatBeat() {
		Message message = new Message();
		message.setMessageType(MessageType.HEART_BEAT_RESPONSE.value());
		return message;
	}
}
