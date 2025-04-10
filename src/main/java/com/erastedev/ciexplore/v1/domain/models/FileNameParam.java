package com.erastedev.ciexplore.v1.domain.models;

import lombok.Data;

/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-12-03 22:21:40
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-12-05 09:29:46
 * @FilePath: src/main/java/ca/deltagis/success/v1/domain/core/models/FileNameParam.java
 * @Description: Parameters for file name
 */
@Data
public class FileNameParam {
    String folder;

    String newName;

    String prefix;

    String suffix;
}
