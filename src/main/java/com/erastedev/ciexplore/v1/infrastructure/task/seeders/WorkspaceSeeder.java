package com.erastedev.ciexplore.v1.infrastructure.task.seeders;

import com.erastedev.ciexplore.v1.application.services.workspace.WorkspaceServiceImpl;
import com.erastedev.ciexplore.v1.domain.entities.workspace.Workspace;
import com.erastedev.ciexplore.v1.domain.ports.out.seeders.AbstractCommonSeeder;
import com.erastedev.ciexplore.v1.domain.ports.out.seeders.SeederManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

// @Component
// extends AbstractCommonSeeder
public class WorkspaceSeeder  {
//    @Autowired
//    private WorkspaceServiceImpl service;
//
//    @Autowired
//    private SeederManager seederManager;
//
//    Logger logger = LoggerFactory.getLogger(UserSeeder.class);
//
//    @Override
//    public List fakeData() {
//        return List.of();
//    }
//
//    @Override
//    public void run() {
//        Workspace workspace = new Workspace();
//        workspace.setName("OMCG");
//        workspace.setCode("OMCG");
//        workspace.setLimitUser(100);
//        workspace.setAutoFields();
//        // repository.save(workspace);
//        // service.saveWorkspace(workspace);
//    }
//
//    @Override
//    public boolean shouldRun() {
//        return !seederManager.hasRun(this.getClass().getName());
//    }
}
