package org.noear.solonx.licence;

import org.noear.solon.Solon;
import org.noear.solon.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 许可证工具
 *
 * @author noear
 * @since 2.7
 */
public class LicenceInfo {
    private static final Logger log = LoggerFactory.getLogger(LicenceInfo.class);

    private static final String publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBALd9OL+RrIctvtzLdrLlHheSq4YwVRDNkDw0LPi9AFAD4V85Ruq92MfAdCyqiku8FHxhvxFa+pKRZoRl5ZJvJScCAwEAAQ==";
    private static final LicenceInfo instance = new LicenceInfo();

    public static LicenceInfo getInstance() {
        return instance;
    }

    private String sn;
    private int edition;
    private String version;

    public String getSn() {
        return sn;
    }

    public String getVersion() {
        return version;
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
                        // 0:sn,1:e,2:v,3:t,4:m,5:c,6:p
                        sn = licence[0];
                        edition = Integer.parseInt(licence[1]);
                        version = licence[2];
                        String start = licence[3];
                        String month = licence[4];
                        String consumer = licence[5];
                        String project = licence[6];

                        if (edition > 0) {
                            //e=0 Community Edition, 21.Enterprise Standard edition, 22 Enterprise Premium Edition, 23 Enterprise Ultimate Edition

                            StringBuilder buf = new StringBuilder();
                            buf.append("Noear Licence (for Solon-EE): \n");
                            buf.append("SN= ").append(getSn()).append("\n");
                            buf.append("E = ").append(getEditionName()).append("\n");
                            buf.append("T = ").append(start).append(" (+").append(month).append("M)\n");
                            buf.append("C = ").append(consumer).append("\n");
                            buf.append("P = ").append(project).append("");

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

    /**
     * 检测
     */
    public static void check() {
        //不用实现
    }

    /**
     * 打印
     */
    public static void print() {
        log.info(LicenceInfo.getInstance().getDescription());
    }
}