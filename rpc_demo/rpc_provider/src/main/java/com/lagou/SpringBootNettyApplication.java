package com.lagou;

import com.lagou.server.NettyServer;
import io.netty.channel.ChannelFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootNettyApplication implements CommandLineRunner {
    @Autowired
    private NettyServer nettyServer;

    @Value("${netty.host}")
    private String hostname;
    @Value("${netty.port}")
    private int port;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootNettyApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        ChannelFuture future=nettyServer.startServer(hostname,port);
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                nettyServer.destory();
            }
        });
        //服务端管道关闭的监听器并同步阻塞,直到channel关闭,线程才会往下执行,结束进程
        future.channel().closeFuture().syncUninterruptibly();
    }
}
