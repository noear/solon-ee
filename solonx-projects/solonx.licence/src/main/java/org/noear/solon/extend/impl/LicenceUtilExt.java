package org.noear.solon.extend.impl;

import org.noear.solon.Solon;
import org.noear.solon.Utils;
import org.noear.solon.core.util.LicenceUtil;
import org.noear.solonx.licence.LicenceHelper;

/**
 * @author noear
 * @since 2.7
 */
public class LicenceUtilExt extends LicenceUtil {
    private static final String publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBALd9OL+RrIctvtzLdrLlHheSq4YwVRDNkDw0LPi9AFAD4V85Ruq92MfAdCyqiku8FHxhvxFa+pKRZoRl5ZJvJScCAwEAAQ==";


    private String description;

    @Override
    public boolean isEnable() {
        return true;
    }

    @Override
    public String getDescription() {
        if (description == null) {
            try {
                String appLicence = Solon.cfg().appLicence();

                if (Utils.isNotEmpty(appLicence)) {
                    String licenceStr = LicenceHelper.licenceDecode(appLicence, publicKey);
                    String[] licence = licenceStr.split(",");

                    if (licence.length >= 3) {
                        int edition = Integer.parseInt(licence[1]);
                        if (edition == 2 || edition == 3) {
                            //e=0 Community Edition, 2 Enterprise Premium Edition, 3 Enterprise Ultimate Edition

                            StringBuilder buf = new StringBuilder();
                            buf.append("Licence (for Solon-EE): ");
                            buf.append("SN=").append(licence[0]).append(", ");
                            if (edition == 3) {
                                buf.append("Edition=Enterprise Ultimate Edition, ");
                            } else {
                                buf.append("Edition=Enterprise Premium Edition, ");
                            }
                            buf.append("Authorized=").append(licence[2]);
                            description = buf.toString();
                        }
                    }
                }
            } catch (Throwable e) {

            }

            if (description == null) {
                description = "Licence (for Solon-EE): Unauthorized (with legal risks)";
            }
        }

        return description;
    }
}
