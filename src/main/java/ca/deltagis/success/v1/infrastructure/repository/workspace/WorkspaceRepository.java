/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-10-27 12:44:01
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-10-27 12:44:01
 */
package ca.deltagis.success.v1.infrastructure.repository.workspace;

import ca.deltagis.success.v1.domain.core.entities.workspace.WorkSpaceStatus;
import ca.deltagis.success.v1.domain.core.entities.workspace.Workspace;
import ca.deltagis.success.v1.domain.ports.in.ICommonRepository;
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
