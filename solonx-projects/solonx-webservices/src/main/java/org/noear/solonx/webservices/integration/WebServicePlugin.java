package org.noear.solonx.webservices.integration;

import org.noear.solon.core.AppContext;
import org.noear.solon.core.Plugin;
import org.noear.solonx.webservices.WebServiceReference;

import javax.jws.WebService;

/**
 * @author noear
 * @since 1.0
 */
public class WebServicePlugin implements Plugin {
    @Override
    public void start(AppContext context) throws Throwable {
        WebServiceBeanBuilder wsBeanBuilder = new WebServiceBeanBuilder();

        //注册 Bean
        context.wrapAndPut(WebServiceServlet.class, new WebServiceServlet());
        context.wrapAndPut(WebServiceBeanBuilder.class, wsBeanBuilder);

        //添加 Anno 处理
        context.beanBuilderAdd(WebService.class, wsBeanBuilder);
        context.beanInjectorAdd(WebServiceReference.class, new WebServiceReferenceBeanInjector());
    }
}
