package ca.deltagis.success.v1.adapters.web.http.project.request;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ca.deltagis.success.v1.adapters.web.api.ApiResponse;
import ca.deltagis.success.v1.adapters.web.message.ProjectMessage;
import ca.deltagis.success.v1.domain.core.entities.project.Project;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ProjectSaveResponse {

    private Project project;
    private ProjectMessage error;
    
     public ResponseEntity<ApiResponse<ProjectSaveResponse>> success(String string, ProjectSaveResponse response,
            HttpStatus ok) {
         // TODO Auto-generated method stub
         throw new UnsupportedOperationException("Unimplemented method 'success'");
    }





}
