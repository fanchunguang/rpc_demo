package com.lagou.server;

import com.lagou.util.RpcDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class NettyServer {

    private static NioEventLoopGroup bossGroup=new NioEventLoopGroup();
    private static NioEventLoopGroup workGroup=new NioEventLoopGroup();
    private Channel channel;

    public static ChannelFuture startServer(String host,int port) throws InterruptedException {
        ServerBootstrap bootstrap=new ServerBootstrap();
        ChannelFuture channelFuture=null;
        final UserServerHandler serverHandler=new UserServerHandler();
        try {
            bootstrap.group(bossGroup,workGroup)
                    //指定服务器监听的通道
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ChannelPipeline pipeline=ch.pipeline();
                            //解决粘包/半包,根据消息长度自动拆包
                            pipeline.addLast(new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2));
                            //返回给客户端，编码方式
                            pipeline.addLast(new StringEncoder());
                            //服务端解析客户端的入参
                            pipeline.addLast(new RpcDecoder());
                            pipeline.addLast(serverHandler);
                        }
                    });
            channelFuture = bootstrap.bind(host,port).sync();
            channelFuture.channel();
            System.out.println("==================>NettyServer 启动成功");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (channelFuture != null && channelFuture.isSuccess()) {
                System.out.println("Netty server listening " + host + " on port " + port + " and ready for connections...");
            } else {
                System.out.println("Netty server start up Error!");
            }
        }
        return  channelFuture;
    }


    public void destory(){
        System.out.println("==========>shutdown netty server begin");
        workGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
        System.out.println("==========>shutdown netty server end");
    }
}
