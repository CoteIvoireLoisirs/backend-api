/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-11-25 08:57:16
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-11-25 08:57:16
 */
package com.erastedev.ciexplore.v1.application.request.right;

import com.erastedev.ciexplore.v1.adapters.web.message.rights.CreateModuleProfileRightError;
import com.erastedev.ciexplore.v1.domain.entities.rights.ModuleProfileRight;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateModuleProfileRightResponse {
    ModuleProfileRight moduleProfileRight;
    CreateModuleProfileRightError error;
    String message;
}
