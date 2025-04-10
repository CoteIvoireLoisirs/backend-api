/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-10-29 20:01:29
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-10-29 20:01:29
 */
package ca.deltagis.success.v1.adapters.web.exception;

public class ExpiredInvitationCodeException extends RuntimeException {
    public ExpiredInvitationCodeException(String message) {
        super(message);
    }
}
