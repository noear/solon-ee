package org.noear.solon.extend.impl;

import org.noear.solon.core.util.LicenceUtil;
import org.noear.solonx.licence.LicenceUtils;

/**
 * @author noear
 * @since 2.7
 */
public class LicenceUtilExt extends LicenceUtil {
    @Override
    public boolean isEnable() {
        return true;
    }

    @Override
    public String getDescription() {
        return LicenceUtils.getInstance().getDescription();
    }
}
