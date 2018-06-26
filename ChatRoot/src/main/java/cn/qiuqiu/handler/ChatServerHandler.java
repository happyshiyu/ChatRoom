package cn.qiuqiu.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import io.netty.channel.ChannelHandler.Sharable;
@Sharable
public class ChatServerHandler extends SimpleChannelInboundHandler<String>{
	//服务器中的客户端队列
	public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	
	//当有客户端连接时调用
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		Channel in = ctx.channel();
		
		for (Channel ch : channels) {
			if(ch != in) {
				ch.writeAndFlush("[用户" + in.remoteAddress() + "]进入\n");
			}
		}
		channels.add(in);
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		Channel out = ctx.channel();
		for (Channel ch : channels) {
			if(ch != out) {
				ch.writeAndFlush("[用户" + out.remoteAddress() + "]下线\n");
			}
		}
		channels.remove(out);
	}
	
	//
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		Channel c = ctx.channel();
		for (Channel ch : channels) {
			if(ch != c) {
				ch.writeAndFlush("[" + c.remoteAddress() + "]:" + msg + "\n");
			} else {
				ch.writeAndFlush("[我]:" + msg + "\n");
			}
		}
		
	}

	//当服务器监听到客户端活动时调用
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Channel c = ctx.channel();
		System.out.println("[" + c.remoteAddress() + "]在线上");
	}

	//当服务器监听到客户端离线时调用
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		Channel c = ctx.channel();
		System.out.println("[" + c.remoteAddress() + "]离线了");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		Channel c = ctx.channel();
		System.out.println("[" + c.remoteAddress() + "]出现异常了");
	}
	
	
	

}
