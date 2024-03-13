package demo.client;


import org.noear.solonx.webservices.utils.WebServiceHelper;

public class ClientTest {
    public static void main(String[] args) {
        String wsAddress = "http://localhost:8080/ws/HelloService";

        HelloService helloService = WebServiceHelper.createWebClient(wsAddress, HelloService.class);

        String tmp = helloService.hello("noear");
        System.out.println("rst::" + tmp);
    }
}
