package com.lagou.com.lagou.client;

import com.lagou.RpcConsumer;
import com.lagou.service.UserService;

import java.util.Arrays;

public class ClientBootStrap {

    public static String provideName="UserService#sayHello#";

    public static void main(String[] args) throws InterruptedException {
        RpcConsumer consumer=new RpcConsumer();
        UserService service=(UserService) consumer.createProxy(UserService.class,provideName);
        while(true){
            System.out.println("***************************");
            Thread.sleep(1000);
            System.out.println(service.sayHello("are you ok ?"));
        }
    }
}
