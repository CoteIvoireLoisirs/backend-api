/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-11-06 12:14:50
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-11-06 12:14:50
 */
package com.erastedev.ciexplore.v1.infrastructure.repository.logs;

import com.erastedev.ciexplore.v1.domain.entities.logs.LogEntity;
import com.erastedev.ciexplore.v1.domain.ports.in.ICommonRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends ICommonRepository<LogEntity> {
}
