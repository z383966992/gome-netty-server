package com.gome.netty.handlers;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import com.gome.netty.channel.holder.ChannelHolder;
import com.gome.netty.struct.Message;
import com.gome.netty.struct.MessageType;

/**
 * 类说明.
 * 这个是业务handler,用来给客户端发送控制命令
 * <pre>
 * 修改日期        修改人    修改原因
 * 2015年10月30日    周亮亮    新建
 * </pre>
 */
public class ControlCommandHandler extends ChannelHandlerAdapter{

	/**
	 * 把控制命令发送给客户端
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) 
			throws Exception {
		Message message = (Message) msg;
		if (message != null && message.getContent() != null
				&& message.getMessageType() == MessageType.CONTROL_COMMAND.value()) {
System.out.println("in control command handler!");
			Message appMsg = new Message();
			appMsg.setContent(message.getContent());
			if(ChannelHolder.get(message.getChannelId()) != null) {
				//发送命令给手机端
				appMsg.setMessageType(MessageType.CONTROL_COMMAND.value());
				ChannelHolder.get(message.getChannelId()).writeAndFlush(appMsg);
				
				//发送命令给控制端
				Message conMsg = new Message();
				conMsg.setContent("control success!");
				ctx.writeAndFlush(conMsg);
System.out.println("send control command to app success");			
			} else {
				//发送命令给控制端
				Message conMsg = new Message();
				conMsg.setContent("control fail!");
				ctx.writeAndFlush(conMsg);
System.out.println("未能成功找到app channel");
			}
		}
	}
}
