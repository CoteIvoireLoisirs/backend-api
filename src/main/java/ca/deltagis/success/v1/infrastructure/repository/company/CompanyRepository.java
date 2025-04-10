package ca.deltagis.success.v1.infrastructure.repository.company;

import java.util.Optional;

import org.springframework.stereotype.Repository;
import ca.deltagis.success.v1.domain.core.entities.company.Company;
import ca.deltagis.success.v1.domain.ports.in.ICommonRepository;

@Repository
public interface CompanyRepository extends ICommonRepository<Company> {

    /**
     * Retrieves a company by its code.
     *
     * @param workspace_code the code of the workspace to find.
     * @return an Optional containing the exercice if found, or empty if not found.
     */
    Optional<Company> findByWorkspaceCode(String code);
}
