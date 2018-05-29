package aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

public class AsyncTimeServerHandler implements Runnable {
    private int port;
    CountDownLatch latch;
    AsynchronousServerSocketChannel asynchronousServerSocketChannel;

    public AsyncTimeServerHandler(int port){
        this.port = port;
        try{
            //创建一个异步的服务端通道
            asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open();
            //绑定端口
            asynchronousServerSocketChannel.bind(new InetSocketAddress(port));
            System.out.println("The time server is start in port:"+ port);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void run(){
        latch = new CountDownLatch(1);
        doAccept();
        try{
            latch.await();//允许当前线程阻塞，防止服务端执行完退出
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public void doAccept(){
        //传递一个CompletionHandler实例来接收通知
        asynchronousServerSocketChannel.accept(this,new AcceptCompletionHandler());
    }
}
