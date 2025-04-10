package ca.deltagis.success.v1.adapters.web.http.billetage.request;
import java.util.HashMap;
import ca.deltagis.success.v1.adapters.web.message.BilletageMessage;
import ca.deltagis.success.v1.domain.core.entities.billetage.Billetage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BilletageSaveResponse{

    
 
    private Billetage billetage;

    private BilletageMessage error;

    private HashMap<String, String> errorDetail = null;

   


}
