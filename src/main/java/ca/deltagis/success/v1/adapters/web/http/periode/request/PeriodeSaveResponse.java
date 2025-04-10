package ca.deltagis.success.v1.adapters.web.http.periode.request;

import java.util.HashMap;

import ca.deltagis.success.v1.adapters.web.message.PeriodeMessage;
import ca.deltagis.success.v1.domain.core.entities.periode.Periode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor


public class PeriodeSaveResponse {

   
    private Periode periode;
    private PeriodeMessage error;
    private HashMap<String, String> errorDetail = null;



}
