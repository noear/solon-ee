package org.noear.solonx.web.webservices.integration;

import org.noear.solon.core.AppContext;
import org.noear.solon.core.Plugin;
import org.noear.solonx.licence.LicenceInfo;
import org.noear.solonx.web.webservices.WebServiceReference;

import javax.jws.WebService;
import javax.servlet.ServletContainerInitializer;

/**
 * @author noear
 * @since 1.0
 */
public class WebServicePlugin implements Plugin {
    @Override
    public void start(AppContext context) throws Throwable {
        //避免补排除
        LicenceInfo.check();

        WebServiceBeanBuilder wsBeanBuilder = new WebServiceBeanBuilder();

        //注册 Bean
        context.wrapAndPut(ServletContainerInitializer.class, new WebServiceContainerInitializerImpl());
        context.wrapAndPut(WebServiceBeanBuilder.class, wsBeanBuilder);

        //添加 Anno 处理
        context.beanBuilderAdd(WebService.class, wsBeanBuilder);
        context.beanInjectorAdd(WebServiceReference.class, new WebServiceReferenceBeanInjector());
    }
}
