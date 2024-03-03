package org.noear.solonx.licence;

import org.noear.solon.Solon;
import org.noear.solon.Utils;

/**
 * 许可证工具
 *
 * @author noear
 * @since 2.7
 */
public class LicenceUtils {
    private static final String publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBALd9OL+RrIctvtzLdrLlHheSq4YwVRDNkDw0LPi9AFAD4V85Ruq92MfAdCyqiku8FHxhvxFa+pKRZoRl5ZJvJScCAwEAAQ==";
    private static final LicenceUtils instance = new LicenceUtils();

    public static LicenceUtils getInstance() {
        return instance;
    }

    private String sn;
    private int edition;
    private String version;
    private String subscribe;

    public String getSn() {
        return sn;
    }

    public int getEdition() {
        return edition;
    }

    public String getEditionName() {
        if (edition == 23) {
            return "Enterprise Ultimate Edition";
        } else if (edition == 22) {
            return "Enterprise Premium Edition";
        } else if (edition == 21) {
            return "Enterprise Standard edition";
        } else if (edition > 0) {
            return "Unknown Edition";
        } else {
            return "Community Edition";
        }
    }

    public String getSubscribe() {
        return subscribe;
    }

    public String getVersion() {
        return version;
    }

    private boolean isValid;
    private String description;

    public boolean isValid() {
        return isValid;
    }

    public String getDescription() {
        if (description == null) {
            try {
                String appLicence = Solon.cfg().appLicence();

                if (Utils.isNotEmpty(appLicence)) {
                    String licenceStr = LicenceHelper.licenceDecode(appLicence, publicKey);
                    String[] licence = licenceStr.split(",");

                    if (licence.length >= 3) {
                        // 0:sn,1:e,2:v,3:t,4:m,5:c
                        sn = licence[0];
                        edition = Integer.parseInt(licence[1]);
                        version = licence[2];
                        subscribe = licence[3];

                        if (edition > 0) {
                            //e=0 Community Edition, 21.Enterprise Standard edition, 22 Enterprise Premium Edition, 23 Enterprise Ultimate Edition

                            StringBuilder buf = new StringBuilder();
                            buf.append("Licence (for Solon-EE): ");
                            buf.append("SN=").append(getSn()).append(", ");
                            buf.append("E=").append(getEditionName()).append(", ");
                            buf.append("T=").append(getSubscribe());

                            isValid = true;
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
