package cn.qiuqiu.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class ChatClient {
	private int port;
	private String host;
	
	public ChatClient(int port, String host) {
		this.port = port;
		this.host = host;
	}
	
	public void start() {
		EventLoopGroup wokrGruop = new NioEventLoopGroup();
		Bootstrap bootstrap = new Bootstrap();
		try {
			bootstrap.group(wokrGruop)
			.channel(NioSocketChannel.class)
			.handler(new ChatClientInitializer());
			Channel channel = bootstrap.connect(host, port).sync().channel();
			
			BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
			while(true) {
				channel.writeAndFlush(input.readLine() + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			wokrGruop.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) {
		new ChatClient(8888,"192.168.43.46").start();
	}
}
