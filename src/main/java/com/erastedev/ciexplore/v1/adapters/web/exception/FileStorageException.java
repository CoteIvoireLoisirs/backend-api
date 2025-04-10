
package com.erastedev.ciexplore.v1.adapters.web.exception;

/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-12-03 20:24:16
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-12-05 09:29:46
 * @FilePath: src/main/java/ca/deltagis/success/v1/adapters/web/exception/FileStorageException.java
 * @Description: Exception thrown when a file storage operation fails
 */
public class FileStorageException extends RuntimeException {
    public FileStorageException(String message) {
        super(message);
    }

    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}