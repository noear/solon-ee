package org.noear.solonx.webservices.integration;

import org.noear.solon.core.BeanInjector;
import org.noear.solon.core.VarHolder;
import org.noear.solonx.webservices.WebServiceHelper;
import org.noear.solonx.webservices.WebServiceReference;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author noear
 * @since 1.0
 */
public class WebServiceReferenceBeanInjector implements BeanInjector<WebServiceReference> {
    private Map<String, Object> cached = new ConcurrentHashMap<>();

    @Override
    public void doInject(VarHolder varH, WebServiceReference anno) {
        if (varH.getType().isInterface()) {

            String wsKey = anno.value() + "#" + varH.getType().getTypeName();
            Object wsProxy = cached.computeIfAbsent(wsKey, k -> WebServiceHelper.createWebClient(anno.value(), varH.getType()));

            varH.setValue(wsProxy);
        }
    }
}
