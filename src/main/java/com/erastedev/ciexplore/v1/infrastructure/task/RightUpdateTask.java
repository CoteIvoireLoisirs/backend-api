/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-11-10 10:59:48
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-11-10 11:01:41
 */
package com.erastedev.ciexplore.v1.infrastructure.task;

import com.erastedev.ciexplore.v1.application.services.rights.RightServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Spring Scheduled task to update system rights.
 * This task is scheduled to run every hour to update system rights in case new
 * rights are added to the system.
 */
// @Component
public class RightUpdateTask {
    private static final Logger logger = LoggerFactory.getLogger(RightUpdateTask.class);

    private final RightServiceImpl rightService;

    // @Autowired
    public RightUpdateTask(RightServiceImpl rightService) {
        this.rightService = rightService;
    }

    /**
     * Task that runs every hour to update application roles if they are missing.
     */
    // @Scheduled(cron = "0 */2 * * * ?")
    public void updateRolesIfMissing() {
        logger.info("Starting role update task...");

        try {
            rightService.createDefaultRightsIfNotExist();
            logger.info("Role update task completed successfully.");
        } catch (Exception e) {
            logger.error("An error occurred during the role update task: ", e);
        }
    }
}
