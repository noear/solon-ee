package org.noear.solon.extend.impl;

import org.noear.solon.Solon;
import org.noear.solon.Utils;
import org.noear.solon.core.util.LicenceUtil;
import org.noear.solonx.utils.RsaUtil;

/**
 * @author noear
 * @since 2.7
 */
public class LicenceUtilExt extends LicenceUtil {
    private static final String PRIVATE_KEY = "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAt304v5Gshy2+3Mt2suUeF5KrhjBVEM2QPDQs+L0AUAPhXzlG6r3Yx8B0LKqKS7wUfGG/EVr6kpFmhGXlkm8lJwIDAQABAkBuDzBQyAIiey/2pBwopgFsxk8YCDmeAI85bVpkQInFc+okp5ko0WFW4iwPjNunaeie69njbFV/GNBy+G8lrmpRAiEA4mquKQE+JFSZQLE8al1XvZe54ANGkhr4wBG4/9SuK00CIQDPdqy1bOoQOE+SSmLjvg9rFsw0cxJUxw7My0Ny6jkQQwIge6oO08ClA0zdo5LqT6IJ8Ti0wiCn49ctdCJ56+lq86UCIADz7ZUju+t5JrxoRQ4AuHdocmWnRaVICMReBqBHfR2bAiEAsQzTFp3LqTIWGrARAphmO0KjUSpar039BqQwP4h1DMc=";

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
                    String licenceStr = RsaUtil.global().decryptFromBase64(appLicence, PRIVATE_KEY);
                    String[] licence = licenceStr.split(",");
                    if (licence.length == 3) {
                        int edition = Integer.parseInt(licence[1]);
                        if (edition == 2 || edition == 3) {
                            //e=0 Community Edition, 1 Premium Edition, 2 Ultimate Edition

                            StringBuilder buf = new StringBuilder();
                            buf.append("Licence: ");
                            buf.append("SN=").append(licence[0]).append(", ");
                            if (edition == 3) {
                                buf.append("Edition=Ultimate, ");
                            } else {
                                buf.append("Edition=Premium, ");
                            }
                            buf.append("Authorized=").append(licence[2]);
                            description = buf.toString();
                        }
                    }
                }
            } catch (Throwable e) {

            }

            if (description == null) {
                description = "Licence: Unauthorized (with legal risks)";
            }
        }

        return description;
    }
}
