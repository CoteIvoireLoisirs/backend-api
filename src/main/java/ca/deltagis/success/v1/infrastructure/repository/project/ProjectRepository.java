package ca.deltagis.success.v1.infrastructure.repository.project;

import ca.deltagis.success.v1.domain.core.entities.project.Project;
import ca.deltagis.success.v1.domain.ports.in.ICommonRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends ICommonRepository<Project> {

    /**
     * Find a project by its code.
     *
     * @param code The code of the project to find.
     * @return An Optional containing the project if found, or empty if not found.
     */
    Optional<Project> findByCode(String code);

    /**
     * Retrieves all projects from a specified workspace.
     *
     * @param workspaceCode The code of the workspace.
     * @return A list of projects that belong to the specified workspace.
     */
    List<Project> findByWorkspaceCode(String workspaceCode);
}





