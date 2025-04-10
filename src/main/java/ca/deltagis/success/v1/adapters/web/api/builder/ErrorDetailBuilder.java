/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-12-04 13:56:45
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-12-04 13:56:45
 */
package ca.deltagis.success.v1.adapters.web.api.builder;

import lombok.Data;

import java.util.HashMap;

@Data
public class ErrorDetailBuilder {
    HashMap<String, String> errorDetails;

    public ErrorDetailBuilder() {
        errorDetails = new HashMap<>();
    }

    public ErrorDetailBuilder add(String key, String value) {
        if (errorDetails == null) {
            errorDetails = new HashMap<>();
        }
        errorDetails.put(key, value);
        return this;
    }

    public HashMap<String, String> build() {
        return errorDetails;
    }
}
