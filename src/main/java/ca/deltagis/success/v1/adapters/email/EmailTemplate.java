/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-11-05 11:47:52
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-11-05 11:47:52
 */
package ca.deltagis.success.v1.adapters.email;

public enum EmailTemplate {
    DEFAULT("default");

    private final String value;

    EmailTemplate(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
