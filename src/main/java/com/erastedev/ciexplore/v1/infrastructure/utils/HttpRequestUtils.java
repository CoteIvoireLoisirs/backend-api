/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-10-24 12:00:36
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-12-05 09:29:45
 * @FilePath: src/main/java/ca/deltagis/success/v1/infrastructure/utils/HttpRequestUtils.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package com.erastedev.ciexplore.v1.infrastructure.utils;

import jakarta.servlet.http.HttpServletRequest;

public interface HttpRequestUtils {
    /**
     * Get the base URL from the given request. This is the URL that you would enter
     * into your browser to access the given request.
     *
     * @param request the request object
     * @return the base URL
     */
    String getBaseUrl(HttpServletRequest request);

    /**
     * Gets the IP address of the client from the given request. This is the IP
     * address of the user making the request.
     *
     * @param request the request object
     * @return the IP address of the client
     */
    String getClientIp(HttpServletRequest request);

    /**
     * Constructs the full URL from the given request, including the scheme, server name,
     * server port, and request URI. It is useful for logging or redirect purposes where
     * the complete URL is needed.
     *
     * @param request the HttpServletRequest object containing client request data
     * @return the complete URL as a String
     */
    String getFullUrl(HttpServletRequest request);

    /**
     * Returns the value of the specified header from the given request.
     *
     * @param request    the HttpServletRequest object containing client request data
     * @param headerName the name of the header to retrieve
     * @return the header value, or null if the header does not exist
     */
    String getHeader(HttpServletRequest request, String headerName);
}
