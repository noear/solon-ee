package org.noear.solonx.licence.integration;

import org.noear.solon.core.AppContext;
import org.noear.solon.core.Plugin;
import org.noear.solon.core.event.AppLoadEndEvent;
import org.noear.solon.core.event.EventBus;
import org.noear.solon.core.util.LogUtil;
import org.noear.solonx.licence.LicenceUtils;

/**
 * @author noear
 * @since 1.0
 */
public class LicencePlugin implements Plugin {
    @Override
    public void start(AppContext context) throws Throwable {
        EventBus.subscribe(AppLoadEndEvent.class, Integer.MAX_VALUE, e -> {
            LogUtil.global().info(LicenceUtils.getInstance().getDescription());
        });
    }
}
