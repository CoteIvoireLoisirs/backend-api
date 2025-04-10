package ca.deltagis.success.v1.domain.ports.in.company;

import ca.deltagis.success.v1.adapters.web.http.company.request.CompanySaveResponse;
import ca.deltagis.success.v1.domain.core.entities.company.Company;

public interface IcompanyService {
    /**
     * Retrieves a company by its workspace code.
     *
     * @param workspaceCode The workspace code of the company to be retrieved.
     * @return A CompanySaveResponse containing the company if found, or an error
     *         message if not found.
     */
    CompanySaveResponse findCompanyByWorkspaceCode(String workspaceCode);

    /**
     * Creates a new company in the database if no company with the given workspace code
     * exists. The newly created company is given the provided workspace code, and the
     * other fields are set to default values.
     *
     * @param workspaceCode the workspace code of the company to be created
     * @return the newly created company, or an existing company if one already exists with the given workspace code
     */
    Company createCompanyIfNotExists(String workspaceCode);
}
