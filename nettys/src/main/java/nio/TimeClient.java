package nio;

import java.io.IOException;

public class TimeClient {
    /**
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException{
        int port = 8080;
        if(args != null &&args.length >0){
            try{
                port = Integer.valueOf(args[0]);
            }catch (NumberFormatException ex){
                //采用默认值
            }
        }
        new Thread(new TimeClientHandle("127.0.0.1",port),"TimeClient-001").start();
    }
}
