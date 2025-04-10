package ca.deltagis.success.v1.domain.core.models.language;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Language {
    private LangCodeEnum code;
    private String name;
}
