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
    private static LicenceInfo instance;

    public static LicenceInfo getInstance() {
        if (instance == null) {
            instance = new LicenceInfo();
        }

        return instance;
    }

    private final String sn;
    private final String version;
    private final int edition;
    private final String editionName;
    private final boolean isValid;
    private final String description;

    private LicenceInfo() {
        String snTmp = null;
        String versionTmp = null;
        int editionTmp = 0;
        String editionNameTmp = null;
        boolean isValidTmp = false;
        String descriptionTmp = null;

        try {
            String appLicence = Solon.cfg().appLicence();

            if (Utils.isNotEmpty(appLicence)) {
                String licenceStr = LicenceHelper.licenceDecode(appLicence, publicKey);
                String[] licence = licenceStr.split(",");

                if (licence.length >= 7) {
                    // 0:sn,1:e,2:v,3:t,4:m,5:c,6:p
                    snTmp = licence[0];
                    editionTmp = Integer.parseInt(licence[1]);
                    versionTmp = licence[2];
                    String start = licence[3];
                    String month = licence[4];
                    String consumer = licence[5];
                    String project = licence[6];

                    if (editionTmp == 23) {
                        editionNameTmp = "Enterprise Ultimate Edition";
                    } else if (editionTmp == 22) {
                        editionNameTmp = "Enterprise Premium Edition";
                    } else if (editionTmp == 21) {
                        editionNameTmp = "Enterprise Standard edition";
                    } else if (editionTmp > 0) {
                        editionNameTmp = "Unknown Edition";
                    } else {
                        editionNameTmp = "Community Edition";
                    }

                    if (editionTmp > 0) {
                        //e=0 Community Edition, 21.Enterprise Standard edition, 22 Enterprise Premium Edition, 23 Enterprise Ultimate Edition

                        StringBuilder buf = new StringBuilder();
                        buf.append("Noear Licence (for Solon-EE): \n");
                        buf.append("SN= ").append(snTmp).append("\n");
                        buf.append("E = ").append(editionNameTmp).append("\n");
                        buf.append("T = ").append(start).append(" (+").append(month).append("M)\n");
                        buf.append("C = ").append(consumer).append("\n");
                        buf.append("P = ").append(project).append("");

                        isValidTmp = true;
                        descriptionTmp = buf.toString();
                    }
                }
            }
        } catch (Throwable e) {

        }

        if (editionNameTmp == null) {
            editionNameTmp = "Community Edition";
        }

        if (descriptionTmp == null) {
            descriptionTmp = "\nLicence (for Solon-EE): Unauthorized (with legal risks)\n";
        }

        this.sn = snTmp;
        this.version = versionTmp;
        this.isValid = isValidTmp;
        this.description = descriptionTmp;
        this.edition = editionTmp;
        this.editionName = editionNameTmp;

    }

    public String getSn() {
        return sn;
    }

    public String getVersion() {
        return version;
    }

    public int getEdition() {
        return edition;
    }

    public String getEditionName() {
        return editionName;
    }

    public boolean isValid() {
        return isValid;
    }

    public String getDescription() {
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
        if (LicenceInfo.getInstance().isValid()) {
            log.info(LicenceInfo.getInstance().getDescription());
        } else {
            log.error(LicenceInfo.getInstance().getDescription());
        }
    }
}