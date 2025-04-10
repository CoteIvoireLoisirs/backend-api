package ca.deltagis.success.v1.application.services.company;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ca.deltagis.success.v1.adapters.web.http.company.request.CompanySaveResponse;
import ca.deltagis.success.v1.adapters.web.message.CompanyMessage;
import ca.deltagis.success.v1.adapters.web.message.files.FileUploadError;
import ca.deltagis.success.v1.application.services.files.FileNameBuilder;
import ca.deltagis.success.v1.application.services.files.FileStorageServiceImpl;
import ca.deltagis.success.v1.application.services.user.UserAuthServiceImpl;
import ca.deltagis.success.v1.application.services.user.UserServiceImpl;
import ca.deltagis.success.v1.application.services.workspace.WorkspaceServiceImpl;
import ca.deltagis.success.v1.domain.core.entities.company.Company;
import ca.deltagis.success.v1.domain.core.entities.workspace.Workspace;
import ca.deltagis.success.v1.domain.core.models.FileNameParam;
import ca.deltagis.success.v1.domain.core.models.FileOperationResponse;
import ca.deltagis.success.v1.domain.core.models.FileUploadResponse;
import ca.deltagis.success.v1.domain.ports.in.company.IcompanyService;
import ca.deltagis.success.v1.domain.ports.out.AbstractCommonService;
import ca.deltagis.success.v1.infrastructure.repository.company.CompanyRepository;
import ca.deltagis.success.v1.infrastructure.repository.workspace.WorkspaceRepository;
import ca.deltagis.success.v1.infrastructure.utils.DateUtil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Service
public class CompanyServiceImpl extends AbstractCommonService<Company> implements IcompanyService {

    @Autowired
    private CompanyRepository repository;

    @Autowired
    private WorkspaceRepository repositoryWorkspace;

    @Autowired
    private WorkspaceServiceImpl workspaceService;

    @Autowired
    private FileStorageServiceImpl fileStorage;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    FileStorageServiceImpl storageService;

    @Autowired
    private UserAuthServiceImpl userAuthService;

    private String uploadFileDirectory = "company";

    private static final Logger logger = LoggerFactory.getLogger(CompanyServiceImpl.class);

    @Override
    public List<Company> findAll() {
        return repository.findAll();
    }

    public Optional<Company> getByCode(String workspace_code) {
        return repository.findByWorkspaceCode(workspace_code);
    }


    

    /**
     * Retrieves a company by its ID.
     *
     * @param id the ID of the company to be retrieved.
     * @return the company with the specified ID, or null if no such company exists.
     */
    // @Override
    // public Company getById(Long id) {
    //     return repository.findById(id).orElse(null);
    // }


    @Override
    public Company getById(Long id) {
        Company findComapCompany = repository.findById(id).orElse(null);
        if (findComapCompany != null) {
            findComapCompany.setOwner(userService.getOptionalUserById(findComapCompany.getId()).orElse(null));
            findComapCompany.setImageUrl(storageService.getUrlFile(findComapCompany.getImagePath()));
        }
        return findComapCompany;
    }

    @Override
    public Long deleteById(Long id) {
        repository.deleteById(id);
        return id;
    }

    @Override
    public boolean existsById(Long id) {
        return repository.findById(id).isPresent();
    }

    @Override
    public Company save(Company entity) {
        return repository.save(entity);
    }

    


    @Override
    public List<Company> getAll() {
        return getRepository()
                .findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .peek(company -> company
                        .setOwner(userService.getOptionalUserById(company.getId()).orElse(null)))
                .peek(company -> company.setImageUrl(storageService.getUrlFile(company.getImagePath())))
                .toList();
    }

    /**
     * Creates a new company in the database if no company with the given workspace
     * code
     * exists. The newly created company is given the provided workspace code, and
     * the
     * other fields are set to default values.
     *
     * @param workspaceCode the workspace code of the company to be created
     */
    public Company createCompanyIfNotExists(String workspaceCode) {
        Company newCompany = new Company();
        newCompany.setWorkspaceCode(workspaceCode);
        newCompany.setAutoFields();
        newCompany.setUpdateBy(userAuthService.getCurrentLoggedUser().getId());
        return repository.save(newCompany);
    }

    /**
     * Retrieves a company by its workspace code. If no company with the given
     * workspace code
     * exists, a new company is created with the given workspace code and default
     * values for
     * other fields. If the company is found, it is simply returned. If a problem
     * occurs while
     * retrieving or creating the company, an error message is returned in the
     * response.
     *
     * @param workspaceCode the workspace code of the company to be retrieved.
     * @return a CompanySaveResponse containing the company if found or created, or
     *         an error
     *         message if a problem occurs.
     */

    public CompanySaveResponse findCompanyByWorkspaceCode(String workspaceCode) {

        Workspace workspace = repositoryWorkspace.findByCode(workspaceCode).orElse(null);
        if (workspace == null) {

            return new CompanySaveResponse(null, CompanyMessage.WORKSPACE_NOT_FOUND, null);
        }
        try {


            Company company = repository.findByWorkspaceCode(workspaceCode).orElse(null);
            if (company == null) {
                company = createCompanyIfNotExists(workspaceCode);
            }

            return new CompanySaveResponse(company, null, null);
        } catch (Exception e) {
            e.printStackTrace();

            HashMap<String, String> errorDetail = new HashMap<>();
            errorDetail.put("message", e.getMessage());

            return new CompanySaveResponse(null, CompanyMessage.SOMETHING_WENT_WRONG, errorDetail);
        }
    }

    public CompanySaveResponse updateCompany(Company company) {
        try {

            Company lastcompany = repository.findById(company.getId())
                    .orElseThrow(() -> new RuntimeException("company not found"));
            fillCompanyFields(lastcompany, company); // ! function not respect SOLID principles
            Company updateCompany = repository.save(lastcompany);
            return new CompanySaveResponse(updateCompany, CompanyMessage.UPDATED, null);
        } catch (Exception e) {
            e.printStackTrace();
            HashMap<String, String> errorDetail = new HashMap<>();
            errorDetail.put("message", e.getMessage());
            return new CompanySaveResponse(null, CompanyMessage.SOMETHING_WENT_WRONG, errorDetail);
        }
    }

    void fillCompanyFields(Company oldCompany, Company newCompany) {

        List<String> messages = new ArrayList<>();
       

        // ! why use messages ?
        if (newCompany != null && !oldCompany.getWorkspaceCode().equals(newCompany)) {
            messages.add("Field 'company' was not updated to preserve its original value.");
        } else {
            oldCompany.setWorkspaceCode(oldCompany.getWorkspaceCode());
        }
        oldCompany.setActivity(newCompany.getActivity());
        oldCompany.setAddress(newCompany.getAddress());
        oldCompany.setCity(newCompany.getCity());
        oldCompany.setContact(newCompany.getContact());
        oldCompany.setCountry(newCompany.getCountry());
        oldCompany.setEmail(newCompany.getEmail());
        oldCompany.setName(newCompany.getName());
        oldCompany.setPhoneNumber(newCompany.getPhoneNumber());
        oldCompany.setSalaryTax(newCompany.getSalaryTax());
        oldCompany.setFax(newCompany.getFax());
        oldCompany.setInsae(newCompany.getInsae());
        oldCompany.setIpts(newCompany.getIpts());
        oldCompany.setAcronym(newCompany.getAcronym());
        oldCompany.setSocialSecurity(newCompany.getSocialSecurity());
        oldCompany.setZipCode(newCompany.getZipCode());
        if (newCompany.getImagePath() != null && newCompany.getImagePath() != "") {
            oldCompany.setImagePath(newCompany.getImagePath());
        } else if (newCompany.getImagePath() == "") {

            oldCompany.setImagePath(null);
        }
        oldCompany.setUpdateBy(userAuthService.getCurrentLoggedUser().getId());
        oldCompany.setWorkspaceCode(newCompany.getWorkspaceCode());

        // ! why update is not saved ?
    }

    public CompanySaveResponse updateCompanyImage(MultipartFile file, String workspaceCode) {
        if (file == null) {
            return new CompanySaveResponse(null, CompanyMessage.CANT_UPLOAD_EMPTY_FILE, null);
        }

        Company company = repository.findByWorkspaceCode(workspaceCode).orElse(null);
        if (company == null) {
            return new CompanySaveResponse(null, CompanyMessage.NOT_FOUND, null);
        }

        FileUploadResponse uploadResponse = changeCompanyImage(file, company.getWorkspaceCode());

        if (uploadResponse.getError() != null) {
            return new CompanySaveResponse(
                    null,
                    CompanyMessage.fromFileUploadError(uploadResponse.getError()),
                    null);
        }

        company.setImagePath(uploadResponse.getFileName());
        repository.save(company);

        return new CompanySaveResponse(company, CompanyMessage.NONE, null);
    }

    public FileUploadResponse changeCompanyImage(MultipartFile file, String workspaceCode) {
        if (file == null) {
            return new FileUploadResponse(null, FileUploadError.EMPTY_FILE);
        }

        if (workspaceCode == null) {
            return new FileUploadResponse(null, FileUploadError.INVALID_DESTINATION_PATH);
        }

        LocalDateTime dateTime = DateUtil.getCurrentDateTime();
        FileNameParam param = new FileNameBuilder()
                .file(file)
                .folder(getDirectory(workspaceCode))
                .newName(workspaceCode)
                .buiFileNameParam();

        return storageService.storeImage(file, param);
    }

    public Boolean deleteProjectImage(Company company) {
        String path = company.getImagePath();
        if (path != null && !path.isEmpty()) {
            FileOperationResponse response = fileStorage.deleteFile(path);
            if (!response.isSuccess()) {
                logger.warn("Failed to delete file: {}", response.getMessage());
                return false;
            }
            return true;
        }
        return false;
    }

    public String getDirectory(String workspaceCode) {
        String targetCompany = workspaceCode == null ? "" : workspaceCode;
        return uploadFileDirectory + "/" + targetCompany;
    }

}
