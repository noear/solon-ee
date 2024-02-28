package org.noear.solonx.web.webservices.client;

import org.noear.solonx.web.webservices.Hello;
import org.noear.solonx.web.webservices.HelloService;

public class HelloClient {

    public static void main(String[] args) {
        HelloService service = new HelloService();
        Hello hello = service.getHelloPort();
        String text = hello.hello("soap");
        System.out.println(text);
    }

}
