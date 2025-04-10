/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-10-24 11:58:36
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-12-05 09:29:46
 * @FilePath: src/main/java/ca/deltagis/success/v1/infrastructure/utils/HttpRequestUtil.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package ca.deltagis.success.v1.infrastructure.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class HttpRequestUtil implements HttpRequestUtils {
    @Override
    public String getBaseUrl(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    }

    @Override
    public String getClientIp(HttpServletRequest request) {
        String clientIp = request.getHeader("X-Forwarded-For");
        if (clientIp == null || clientIp.isEmpty()) {
            clientIp = request.getRemoteAddr();
        }
        return clientIp;
    }

    @Override
    public String getFullUrl(HttpServletRequest request) {
        StringBuffer requestUrl = request.getRequestURL();
        String queryString = request.getQueryString();
        return queryString == null ? requestUrl.toString() : requestUrl.append('?').append(queryString).toString();
    }

    @Override
    public String getHeader(HttpServletRequest request, String headerName) {
        return request.getHeader(headerName);
    }
}
