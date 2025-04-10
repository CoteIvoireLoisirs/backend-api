package ca.deltagis.success.v1.adapters.web.http.unit;

import java.util.HashMap;

import ca.deltagis.success.v1.adapters.web.message.UnitedMessage;
import ca.deltagis.success.v1.domain.core.entities.unit.Unit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UnitedSaveResponse {
    private Unit united;
    private UnitedMessage error;
    private HashMap<String, String> errorDetail = null;
}
