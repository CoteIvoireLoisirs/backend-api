/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-11-12 12:52:04
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-11-12 12:52:04
 */
package com.erastedev.ciexplore.v1.domain.models.rigths;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum ModuleEnum {
    HOME("HOME"),
    WORKSPACE("WORKSPACE"),
    PROJECT("PROJECT"),
    ACCOUNTING("ACCOUNTING"),
    PROCUREMENT("PROCUREMENT"),
    LOANMANAGEMENT("LOANMANAGEMENT"),
    FIXEDASSETS("FIXEDASSETS"),
    FINANCIALSTATE("FINANCIALSTATE"),
    GESPAID("GESPAID"),
    GESSTOCK("GESSTOCK"),
    GESAUTO("GESAUTO"),
    ADMINISTRATION("ADMINISTRATION");

    private final String code;

    ModuleEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    /**
     * Retrieves all the default modules in the system.
     *
     * @return a list of all the default modules.
     */
    public static List<ModuleEnum> getAll() {
        return new ArrayList<>(Arrays.asList(ModuleEnum.values()));
    }

    /**
     * Retrieves a default module by its value.
     *
     * @param code the value of the module.
     * @return the default module corresponding to the value.
     */
    public static ModuleEnum getByCode(String code) {
        for (ModuleEnum module : ModuleEnum.values()) {
            if (module.getCode().equals(code)) {
                return module;
            }
        }
        return null;
    }
}
