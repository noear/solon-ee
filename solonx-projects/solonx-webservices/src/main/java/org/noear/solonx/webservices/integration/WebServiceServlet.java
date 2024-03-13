package org.noear.solonx.webservices.integration;

import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.transport.servlet.CXFNonSpringServlet;
import org.noear.solon.Solon;
import org.noear.solon.Utils;
import org.noear.solon.core.BeanWrap;
import org.noear.solonx.webservices.utils.WebServiceHelper;

import javax.jws.WebService;
import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebServlet;

@WebServlet(name = "ws", urlPatterns = "/ws/*", loadOnStartup = 0)
public class WebServiceServlet extends CXFNonSpringServlet {
    @Override
    protected void loadBus(ServletConfig sc) {
        // 设置 Bus
        setBus(sc);
        // 发布 Web 服务
        publishWebService();
    }

    private void setBus(ServletConfig sc) {
        super.loadBus(sc);
        Bus bus = getBus();
        BusFactory.setDefaultBus(bus);
    }

    private void publishWebService() {
        // 遍历所有标注了 @WebService 注解的接口
        WebServiceBeanBuilder wsBeanBuilder = Solon.context().getBean(WebServiceBeanBuilder.class);

        for (BeanWrap bw : wsBeanBuilder.getWsBeanWarps()) {
            // 对于接口才能执行以下代码

            // 获取 name 属性
            String name = bw.clz().getAnnotation(WebService.class).name();
            // 获取 Web 服务地址
            String wsAddress = getAddress(name, bw.clz());
            // 获取 Web 服务实现类（找到唯一的实现类）
            Class<?> wsImplementClass = bw.clz();
            // 获取实现类的实例
            Object wsImplementInstance = bw.raw();
            // 发布 Web 服务
            WebServiceHelper.publishWebService(wsAddress, wsImplementInstance, wsImplementClass);

        }
    }

    private String getAddress(String value, Class<?> wsInterfaceClass) {
        String address;
        if (Utils.isNotEmpty(value)) {
            // 若不为空，则为 value
            address = value;
        } else {
            // 若为空，则为类名
            address = wsInterfaceClass.getSimpleName();
        }

        // 确保最前面只有一个 /
        if (!address.startsWith("/")) {
            address = "/" + address;
        }

        address = address.replaceAll("\\/+", "/");
        return address;
    }
}