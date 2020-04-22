package com.lagou.server;

import com.lagou.service.UserServiceImpl;

public class ServerBootStrap {

    public static void main(String[] args) throws InterruptedException {
        UserServiceImpl.startServer("localhost",8990);
    }
}
