
package com.erastedev.ciexplore.v1.domain.entities.user.model;

/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-10-24 11:40:54
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-10-24 11:42:02
 */
public enum UserInvitationState {
    PENDING,
    ACCEPTED,
    REJECTED,
    EXPIRED;

    private final String name;

    UserInvitationState() {
        this.name = name();
    }

    /**
     * Return the name of the invitation status.
     *
     * @return The name of the invitation status.
     */
    @Override
    public String toString() {
        return name;
    }
}
