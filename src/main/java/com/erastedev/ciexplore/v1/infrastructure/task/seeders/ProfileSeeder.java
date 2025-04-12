package com.erastedev.ciexplore.v1.infrastructure.task.seeders;

import com.erastedev.ciexplore.v1.domain.ports.out.seeders.AbstractCommonSeeder;
import com.erastedev.ciexplore.v1.domain.ports.out.seeders.SeederManager;
import com.erastedev.ciexplore.v1.infrastructure.repository.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProfileSeeder extends AbstractCommonSeeder {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SeederManager seederManager;

    Logger logger = LoggerFactory.getLogger(ProfileSeeder.class);

    @Override
    public void run() {
        logger.info("ProfileSeeder>initializeDefaultData");
        // service.initializeDefaultData();
    }

    @Override
    public boolean shouldRun() {
        return !seederManager.hasRun(this.getClass().getName());
    }
}
