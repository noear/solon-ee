package demo.client;


import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService(name = "HelloService", targetNamespace = "http://demo.solon.io")
public interface HelloService {
    @WebMethod
    String hello(String name);
}
