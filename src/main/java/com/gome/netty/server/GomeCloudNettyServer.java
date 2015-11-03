package com.gome.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gome.netty.codec.MarshallingCodecFactory;
import com.gome.netty.config.Config;
import com.gome.netty.handlers.CheckAuthHandler;
import com.gome.netty.handlers.HeartBeatResponseHandler;
import com.gome.netty.handlers.ControlCommandHandler;
import com.gome.netty.handlers.TimeoutHandler;
/**
 * 国美云平台netty server
 */
public class GomeCloudNettyServer{
	
	private Logger logger = LoggerFactory.getLogger(GomeCloudNettyServer.class);
	
	private void start(String ip, int port) throws Exception {
		EventLoopGroup boss = new NioEventLoopGroup();
		EventLoopGroup worker = new NioEventLoopGroup();
		
		ServerBootstrap bootStrap = new ServerBootstrap();
		try {
			bootStrap.group(boss, worker)
			 .channel(NioServerSocketChannel.class)
			 //The maximum queue length for incoming connection indications (a request to connect) is set to the backlog parameter. 
			 //If a connection indication arrives when the queue is full, the connection is refused.
			 .option(ChannelOption.SO_BACKLOG, 2048)
			 //使消息立即发送出去，不用等待到一定的数量才发			 
			 .option(ChannelOption.TCP_NODELAY, true)
			 //设置消息缓冲
			 .option(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT)
			 //保持长连接状态
			 .option(ChannelOption.SO_KEEPALIVE, true)
			 .handler(new LoggingHandler(LogLevel.INFO))
			 .childHandler(new ChannelInitializer<SocketChannel>() {
				 @Override
				protected void initChannel(SocketChannel ch)
						throws Exception {
					 ChannelPipeline p = ch.pipeline();
					 //jboss marshalling codec
					 p.addLast("decoder", MarshallingCodecFactory.buildMarshallingDecoder());
				     p.addLast("encoder", MarshallingCodecFactory.buildMarshallingEncoder());
				     p.addLast("readTimeoutHandler", new ReadTimeoutHandler(20));
//				     p.addLast("timeoutHandler", new TimeoutHandler(20));
				     p.addLast("checkAuthHandler", new CheckAuthHandler());
				     p.addLast("heartBeatResponse", new HeartBeatResponseHandler());
				     p.addLast("serviceHandler", new ControlCommandHandler());
				}
			});
			//绑定端口，等待成功			
			ChannelFuture future = bootStrap.bind(ip, port).sync();
			future.addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					if (future.isSuccess()) {
System.out.println("gome cloud netty server starting...");						
						logger.info("gome cloud netty server starting...");
					} else {
System.out.println("Bound attempt failed");						
						logger.error("Bound attempt failed");						
						logger.error(future.cause().getMessage(), future.cause());
					}
				}
			});
			//等待服务端监听端口关闭			
			future.channel().closeFuture().sync();			
		} catch (Exception e) {
e.printStackTrace();
			logger.error(e.getMessage(), e);
		} finally {
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
	}
	
	public void startNettyServer() {
		try {
			System.out.println(Config.getIp());
			System.out.println(Config.getPort());
			System.out.println(Config.getMd5Code());
			start(Config.getIp(), Config.getPort());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	public static void main(String[] args) {
		new GomeCloudNettyServer().startNettyServer();
	}
}





































































