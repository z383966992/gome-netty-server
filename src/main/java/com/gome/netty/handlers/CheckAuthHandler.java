package com.gome.netty.handlers;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;

import com.gome.netty.channel.holder.ChannelHolder;
import com.gome.netty.config.Config;
import com.gome.netty.struct.Message;
import com.gome.netty.struct.MessageType;
import com.gome.netty.utils.Md5Util;

/**
 * 检测有效性
 * 类说明.
 * 对传入的消息进行MD5编码，如果编码通过，则通过
 * <pre>
 * 修改日期        修改人    修改原因
 * 2015年10月29日    周亮亮    新建
 * </pre>
 */
public class CheckAuthHandler extends ChannelHandlerAdapter{

	/**
	 * 如果消息是一个连接请求消息，验证MD5
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		Message message = (Message)msg;
		
		if (message != null 
				&& message.getMessageType() == MessageType.CONNECT_AUTH_REQUEST.value()) {
			//校验是否通过
			String headerSign = message.getMd5Check();
			String checkSign = Md5Util.getMd5Code(message.getContent().toString() + Config.getMd5Code());
System.out.println(headerSign);
System.out.println(checkSign);
System.out.println(message.getContent().toString());
System.out.println(Config.getMd5Code());
			//校验通过
			if (headerSign.equals(checkSign)) {
				//发送校验通过消息
				Message response = new Message();
				
				response.setMessageType(MessageType.CONNECT_AUTH_RESPONSE.value());
				response.setStatus(1);
				response.setContent(null);
System.out.println("服务端校验成功!");	
                //把通道加入channel holder
ChannelHolder.add(message.getChannelId(), (SocketChannel)ctx.channel());
				ctx.writeAndFlush(response);
			} else {
				//校验没有通过:1返回应答，2关闭通道
				Message response = new Message();
				response.setMessageType(MessageType.CONNECT_AUTH_RESPONSE.value());
				response.setStatus(0);
				response.setContent(null);
System.out.println("服务端校验失败!");				
				ctx.writeAndFlush(response);
				//通道关闭
				ctx.close();
			}
		} else {	
			ctx.fireChannelRead(msg);
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
System.out.println("in checkauthhandler execption caught");
cause.getStackTrace();
System.out.println(cause.getMessage());
		
		ctx.close();
	}
}
