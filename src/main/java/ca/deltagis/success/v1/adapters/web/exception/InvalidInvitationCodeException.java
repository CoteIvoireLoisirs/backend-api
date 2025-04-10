/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-10-29 20:01:06
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-10-29 20:01:06
 */
package ca.deltagis.success.v1.adapters.web.exception;

public class InvalidInvitationCodeException extends RuntimeException {
    public InvalidInvitationCodeException(String message) {
        super(message);
    }
}
