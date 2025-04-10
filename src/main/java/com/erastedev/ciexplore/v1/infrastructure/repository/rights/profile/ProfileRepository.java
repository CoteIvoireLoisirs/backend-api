/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-11-12 10:34:18
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-11-12 10:34:18
 */
package com.erastedev.ciexplore.v1.infrastructure.repository.rights.profile;

import com.erastedev.ciexplore.v1.domain.entities.rights.profil.Profile;
import com.erastedev.ciexplore.v1.domain.ports.in.ICommonRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends ICommonRepository<Profile> {
    Optional<Profile> findByNameAndIsDefaultIsTrue(String name);
}
