package core.gitee.xudai.strategy.license.enums;

/**
 * 许可证类型枚举
 * @author daixu
 */
public enum LicenseTypeEnum {

    APACHE_2_0("Apache License 2.0", "https://www.apache.org/licenses/LICENSE-2.0.txt", "repo"),
    MIT("MIT License", "https://opensource.org/licenses/MIT", "repo"),
    GPL_3_0("GNU General Public License v3.0", "https://www.gnu.org/licenses/gpl-3.0.html", "repo"),
    BSD_2_CLAUSE("BSD 2-Clause License", "https://opensource.org/licenses/BSD-2-Clause", "repo"),
    BSD_3_CLAUSE("BSD 3-Clause License", "https://opensource.org/licenses/BSD-3-Clause", "repo"),
    EPL_2_0("Eclipse Public License 2.0", "https://www.eclipse.org/legal/epl-2.0/", "repo"),
    LGPL_3_0("GNU Lesser General Public License v3.0", "https://www.gnu.org/licenses/lgpl-3.0.html", "repo"),
    MOZILLA_2_0("Mozilla Public License 2.0", "https://www.mozilla.org/en-US/MPL/2.0/", "repo"),
    UNKNOWN("Unknown License", "", "repo");

    private final String name;
    private final String url;
    private final String distribution;

    LicenseTypeEnum(String name, String url, String distribution) {
        this.name = name;
        this.url = url;
        this.distribution = distribution;
    }

    public String getName() { return name; }
    public String getUrl() { return url; }
    public String getDistribution() { return distribution; }

    public static LicenseTypeEnum fromString(String licenseStr) {
        if (licenseStr == null) return UNKNOWN;

        for (LicenseTypeEnum type : values()) {
            if (type.name().equalsIgnoreCase(licenseStr) ||
                    type.getName().equalsIgnoreCase(licenseStr)) {
                return type;
            }
        }
        return UNKNOWN;
    }

}
