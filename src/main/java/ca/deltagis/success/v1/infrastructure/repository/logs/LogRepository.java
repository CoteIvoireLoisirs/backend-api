/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-11-06 12:14:50
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-11-06 12:14:50
 */
package ca.deltagis.success.v1.infrastructure.repository.logs;

import ca.deltagis.success.v1.domain.core.entities.logs.LogEntity;
import ca.deltagis.success.v1.domain.ports.in.ICommonRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends ICommonRepository<LogEntity> {
}
