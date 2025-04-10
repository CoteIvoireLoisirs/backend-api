/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-10-29 19:57:16
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-10-29 19:57:18
 */
package com.erastedev.ciexplore.v1.adapters.web.exception;

public class InvalidEmailException extends Exception {
    public InvalidEmailException(String message) {
        super(message);
    }
}