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
	//�������еĿͻ��˶���
	public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	
	//���пͻ�������ʱ����
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		Channel in = ctx.channel();
		
		for (Channel ch : channels) {
			if(ch != in) {
				ch.writeAndFlush("[�û�" + in.remoteAddress() + "]����\n");
			}
		}
		channels.add(in);
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		Channel out = ctx.channel();
		for (Channel ch : channels) {
			if(ch != out) {
				ch.writeAndFlush("[�û�" + out.remoteAddress() + "]����\n");
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
				ch.writeAndFlush("[��]:" + msg + "\n");
			}
		}
		
	}

	//���������������ͻ��˻ʱ����
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Channel c = ctx.channel();
		System.out.println("[" + c.remoteAddress() + "]������");
	}

	//���������������ͻ�������ʱ����
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		Channel c = ctx.channel();
		System.out.println("[" + c.remoteAddress() + "]������");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		Channel c = ctx.channel();
		System.out.println("[" + c.remoteAddress() + "]�����쳣��");
	}
	
	
	

}
