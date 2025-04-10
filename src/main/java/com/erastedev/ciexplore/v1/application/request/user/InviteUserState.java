/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-10-29 12:13:25
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-10-29 12:13:25
 */
package com.erastedev.ciexplore.v1.application.request.user;

public enum InviteUserState {
    PENDING,
    REJECTED,
    ALREADY_INVITED;

    private String state;

    InviteUserState() {
        this.state = name();
    }

    public String getState() {
        return state;
    }
}
