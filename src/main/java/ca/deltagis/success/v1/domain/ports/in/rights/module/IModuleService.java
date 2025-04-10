/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-11-12 10:38:02
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-11-12 10:38:02
 */
package ca.deltagis.success.v1.domain.ports.in.rights.module;

import ca.deltagis.success.v1.domain.core.entities.rights.module.Module;

import java.util.List;

public interface IModuleService {
    /**
     * Retrieves a list of modules that are currently enabled.
     *
     * @return a list of enabled Module entities.
     */
    List<Module> getEnabledModules();

    /**
     * Creates default modules if no default modules exist in the database.
     */
    void createDefaultModulesIfNotExist();
}
