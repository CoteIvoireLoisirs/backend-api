package ca.deltagis.success.v1.adapters.web.http.exercice.request;

import java.util.HashMap;

import ca.deltagis.success.v1.adapters.web.message.ExerciceMessage;
import ca.deltagis.success.v1.domain.core.entities.exercice.Exercice;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;




@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExerciceSaveResponse {
    private Exercice exercice;
    private ExerciceMessage error;
    private HashMap<String, String> errorDetail = null;


}
