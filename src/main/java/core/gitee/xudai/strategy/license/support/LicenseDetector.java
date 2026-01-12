package core.gitee.xudai.strategy.license.support;

import org.apache.maven.model.License;
import org.apache.maven.model.Model;
import org.apache.maven.project.MavenProject;
import org.apache.maven.plugin.MojoExecutionException;
import java.util.List;

/**
 * 许可证检测工具类，用于判断项目中是否已配置许可证
 * @author daixu
 */
public class LicenseDetector {

    /**
     * 检测项目POM中是否已包含有效的许可证配置
     * @param project 当前Maven项目对象
     * @return 已包含有效许可证返回true，否则返回false
     * @throws MojoExecutionException 解析POM失败时抛出异常
     * @author daixu
     */
    public static boolean hasExistingLicense(MavenProject project) throws MojoExecutionException {
        try {
            // 从项目中获取POM模型
            Model model = project.getModel();

            // 获取<licenses>标签下的所有许可证配置
            List<License> licenses = model.getLicenses();

            // 检查许可证列表是否有效（非空且至少有一个配置了名称和URL）
            if (licenses == null || licenses.isEmpty()) {
                return false;
            }

            // 验证至少有一个许可证包含必要信息（名称和URL）
            for (License license : licenses) {
                if (isValidLicense(license)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            throw new MojoExecutionException("解析项目许可证配置失败", e);
        }
    }

    /**
     * 验证单个许可证是否有效（名称和URL非空）
     */
    private static boolean isValidLicense(License license) {
        return license != null
                && license.getName() != null
                && !license.getName().trim().isEmpty()
                && license.getUrl() != null
                && !license.getUrl().trim().isEmpty();
    }
}
