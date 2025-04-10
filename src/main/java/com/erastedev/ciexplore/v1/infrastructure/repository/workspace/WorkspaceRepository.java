/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-10-27 12:44:01
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-10-27 12:44:01
 */
package com.erastedev.ciexplore.v1.infrastructure.repository.workspace;

import com.erastedev.ciexplore.v1.domain.entities.workspace.Workspace;
import com.erastedev.ciexplore.v1.domain.ports.in.ICommonRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// extends JpaRepository<Workspace, Long>
@Repository
public interface WorkspaceRepository extends ICommonRepository<Workspace> {

    /**
     * Retrieves a workspace by its code.
     *
     * @param code the code of the workspace to find.
     * @return an Optional containing the workspace if found, or empty if not found.
     */
    Optional<Workspace> findByCode(String code);

    boolean existsByCode(String code);



  
}
