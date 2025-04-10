/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-11-25 08:47:29
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-11-25 08:47:29
 */
package com.erastedev.ciexplore.v1.infrastructure.repository.rights;

import com.erastedev.ciexplore.v1.domain.entities.rights.ModuleProfileRight;
import com.erastedev.ciexplore.v1.domain.ports.in.ICommonRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuleProfileRightRepository extends ICommonRepository<ModuleProfileRight> {

    /**
     * Count the number of ModuleProfileRight entities that are not deleted.
     *
     * @return The number of ModuleProfileRight entities that are not deleted.
     */
    int countByDeletedIsNull();

    /**
     * Retrieves a ModuleProfileRight by profileId where the 'deleted' field is null.
     *
     * @param profileId The profileId to search for.
     * @return The ModuleProfileRight entity that matches the profileId and is not deleted, or null if none is found.
     */
    ModuleProfileRight findByProfileIdAndDeletedIsNull(Long profileId);
}
