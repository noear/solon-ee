package demo.server;


import javax.jws.WebService;

/**
 * @author noear
 * @since 1.0
 */
@WebService(name = "HelloService", serviceName = "HelloService", targetNamespace = "http://demo.solon.io")
public class HelloServiceImpl {
    public String hello(String name) {
        return "hello " + name;
    }
}
