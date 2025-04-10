package ca.deltagis.success.v1.adapters.web.http.periode.request;

import java.util.ArrayList;
import java.util.List;

import ca.deltagis.success.v1.adapters.web.message.ProjectMessage;
import ca.deltagis.success.v1.domain.core.entities.project.Project;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
@Builder

public class PeriodeResponse {

     List<Project> projects = new ArrayList<>();

    ProjectMessage error = null;

}
