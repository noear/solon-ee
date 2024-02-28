package org.noear.solonx.web.webservices.annotation;

import java.lang.annotation.*;

/**
 * @author noear
 * @since 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WebService {
    String name() default "";

    String targetNamespace() default "";

    String serviceName() default "";

    String portName() default "";

    String wsdlLocation() default "";

    String endpointInterface() default "";
}
