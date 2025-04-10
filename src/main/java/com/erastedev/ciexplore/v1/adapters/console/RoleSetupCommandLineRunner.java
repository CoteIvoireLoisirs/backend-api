/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-11-10 11:08:59
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-11-10 11:09:00
 */
package com.erastedev.ciexplore.v1.adapters.console;

import com.erastedev.ciexplore.v1.application.services.rights.RightServiceImpl;
import org.springframework.boot.CommandLineRunner;

public class RoleSetupCommandLineRunner implements CommandLineRunner {

    private final RightServiceImpl rightService;

    public RoleSetupCommandLineRunner(RightServiceImpl rightService) {
        this.rightService = rightService;
    }

    @Override
    public void run(String... args) throws Exception {
        rightService.createDefaultRightsIfNotExist();
        System.out.println("Default rights creation task executed.");
    }
}
