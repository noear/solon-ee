package demo.server;


import org.noear.solon.Solon;

public class ServerTest {
    public static void main(String[] args){
        //String wsAddress = "http://localhost:8080/ws/HelloService";
        //WebServiceHelper.publishWebService(wsAddress, new HelloServiceImpl());

        Solon.start(ServerTest.class, args);
    }
}
