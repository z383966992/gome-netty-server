package com.gome.netty.channel.holder;

import io.netty.channel.socket.SocketChannel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 类说明.
 * 此类用来记录客户端连接
 * <pre>
 * 修改日期        修改人    修改原因
 * 2015年8月14日    周亮亮    新建
 * </pre>
 */
public class ChannelHolder {
	
	private static Map<String, SocketChannel> channelMap = new ConcurrentHashMap<String, SocketChannel>();

	public static void add(String clientId, SocketChannel socketChannel) {
		channelMap.put(clientId, socketChannel);
	}
	
	public static void remove(String channelId) {
		channelMap.remove(channelId);
	}
	
	public static SocketChannel get(String clientId) {
		return channelMap.get(clientId);
	}
	
	public static void remove(SocketChannel socketChannel) {
		for (Map.Entry<String, SocketChannel> entry :channelMap.entrySet()) {
			if (entry.getValue().equals(socketChannel)) {
				channelMap.remove(entry.getKey());
			}
		}
	}
	
	public static int size() {
		return channelMap.size();
	}
}
