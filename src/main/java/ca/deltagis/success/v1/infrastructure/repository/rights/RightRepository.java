/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-11-08 13:58:08
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-11-08 13:58:12
 */
package ca.deltagis.success.v1.infrastructure.repository.rights;

import ca.deltagis.success.v1.domain.core.entities.rights.Right;
import ca.deltagis.success.v1.domain.ports.in.ICommonRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RightRepository extends ICommonRepository<Right> {
}
