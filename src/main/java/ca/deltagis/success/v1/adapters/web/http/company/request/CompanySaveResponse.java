package ca.deltagis.success.v1.adapters.web.http.company.request;

import java.util.HashMap;

import ca.deltagis.success.v1.adapters.web.message.CompanyMessage;
import ca.deltagis.success.v1.domain.core.entities.company.Company;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanySaveResponse {

    private Company company;

    private CompanyMessage error;

    private HashMap<String, String> errorDetail = null;
}
