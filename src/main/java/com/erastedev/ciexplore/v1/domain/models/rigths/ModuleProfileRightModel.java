/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-11-12 13:19:35
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-11-12 13:19:35
 */
package com.erastedev.ciexplore.v1.domain.models.rigths;

public class ModuleProfileRightModel {
    /**
     * Returns the rights string for the given profile type.
     *
     * @param defaultSystemRight the profile type for which the rights string is requested.
     * @return the rights string associated with the profile type.
     * @throws IllegalArgumentException if the provided profile type is null or unsupported.
     */
    public static String getProfileRights(DefaultSystemRight defaultSystemRight) {
        if (defaultSystemRight == null) {
            throw new IllegalArgumentException("DefaultSystemRight cannot be null");
        }

        return switch (defaultSystemRight) {
            case SUPER_ADMIN ->
                    "WORKSPACE_rwd;HOME_rwd;PROJECT_rwd;ACCOUNTING_rwd;PROCUREMENT_rwd;LOANMANAGEMENT_rwd;FIXED_ASSETS_rwd;FINANCIALSTATE_rwd;GESPAID_rwd;GESSTOCK_rwd;GESAUTO_rwd;ADMINISTRATION_rwd";
            case ADMIN ->
                    "WORKSPACE_---;HOME_rwd;PROJECT_rwd;ACCOUNTING_rwd;PROCUREMENT_rwd;LOANMANAGEMENT_rwd;FIXED_ASSETS_rwd;FINANCIALSTATE_rwd;GESPAID_rwd;GESSTOCK_rwd;GESAUTO_rwd;ADMINISTRATION_rwd";
            case USER ->
                    "WORKSPACE_---;HOME_rwd;PROJECT_rwd;ACCOUNTING_rwd;PROCUREMENT_rwd;LOANMANAGEMENT_rwd;FIXED_ASSETS_rwd;FINANCIALSTATE_rwd;GESPAID_rwd;GESSTOCK_rwd;GESAUTO_rwd;ADMINISTRATION_---";
            // GUEST
            case NO_ACCESS ->
                    "WORKSPACE_---;HOME_---;PROJECT_---;ACCOUNTING_---;PROCUREMENT_---;LOANMANAGEMENT_---;FIXED_ASSETS_---;FINANCIALSTATE_---;GESPAID_---;GESSTOCK_---;GESAUTO_---;ADMINISTRATION_---";
            default ->
                    "WORKSPACE_---;HOME_---;PROJECT_---;ACCOUNTING_---;PROCUREMENT_---;LOANMANAGEMENT_---;FIXED_ASSETS_---;FINANCIALSTATE_---;GESPAID_---;GESSTOCK_---;GESAUTO_---;ADMINISTRATION_---";
        };
    }
}
