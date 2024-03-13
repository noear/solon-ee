package demo.server;


import org.noear.solon.Solon;

import javax.jws.WebService;

public class ServerTest {
    public static void main(String[] args) {
        Solon.start(ServerTest.class, args);
    }

    @WebService(serviceName = "HelloService", targetNamespace = "http://demo.solon.io")
    public static class HelloServiceImpl {
        public String hello(String name) {
            return "hello " + name;
        }
    }
}
