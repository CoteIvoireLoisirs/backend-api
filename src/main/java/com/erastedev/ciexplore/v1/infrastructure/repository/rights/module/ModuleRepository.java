/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-11-12 10:36:05
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-11-12 10:36:05
 */
package com.erastedev.ciexplore.v1.infrastructure.repository.rights.module;

import com.erastedev.ciexplore.v1.domain.entities.rights.module.Module;
import com.erastedev.ciexplore.v1.domain.ports.in.ICommonRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleRepository extends ICommonRepository<Module> {
    /**
     * Retrieves a list of all enabled modules in the database.
     *
     * @return a list of all enabled Module entities.
     */
    List<Module> findByEnableIsTrue();

    boolean existsByEnableIsTrue();
}
