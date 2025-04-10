/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-11-08 13:58:08
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-11-08 13:58:12
 */
package com.erastedev.ciexplore.v1.infrastructure.repository.rights;

import com.erastedev.ciexplore.v1.domain.entities.rights.Right;
import com.erastedev.ciexplore.v1.domain.ports.in.ICommonRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RightRepository extends ICommonRepository<Right> {
}
