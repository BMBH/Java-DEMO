package aio;

import java.io.IOException;

public class TimeClient {
    /**
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        int port = 8080;
        if(args != null &&args.length >0){
            try{
                port = Integer.valueOf(args[0]);
            }catch (NumberFormatException ex){
                //采用默认值
            }
        }
        new Thread(new AsyncTimeClientHandler("127.0.0.1",port),"AIO-AsyncTimeClientHandler-001").start();
    }
}
