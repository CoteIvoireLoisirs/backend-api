package ca.deltagis.success.v1.adapters.web.http.billetage.request;

import java.util.ArrayList;
import java.util.List;

import ca.deltagis.success.v1.adapters.web.message.BilletageMessage;
import ca.deltagis.success.v1.adapters.web.message.ProjectMessage;
import ca.deltagis.success.v1.domain.core.entities.billetage.Billetage;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class BilletageByCurrencyResponse {
    

     List<Billetage> billetage = new ArrayList<>();

    BilletageMessage error = null;
}


