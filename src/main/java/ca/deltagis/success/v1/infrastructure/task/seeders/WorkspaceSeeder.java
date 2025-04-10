package ca.deltagis.success.v1.infrastructure.task.seeders;

import ca.deltagis.success.v1.application.services.workspace.WorkspaceServiceImpl;
import ca.deltagis.success.v1.domain.core.entities.workspace.Workspace;
import ca.deltagis.success.v1.domain.ports.out.seeders.AbstractCommonSeeder;
import ca.deltagis.success.v1.domain.ports.out.seeders.SeederManager;
import ca.deltagis.success.v1.infrastructure.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;

// @Component
public class WorkspaceSeeder extends AbstractCommonSeeder {
    @Autowired
    private WorkspaceServiceImpl service;

    @Autowired
    private SeederManager seederManager;

    Logger logger = LoggerFactory.getLogger(UserSeeder.class);

    @Override
    public void run() {
        Workspace workspace = new Workspace();
        workspace.setName("OMCG");
        workspace.setCode("OMCG");
        workspace.setLimitUser(100);
        workspace.setAutoFields();
        // repository.save(workspace);
        // service.saveWorkspace(workspace);
    }

    @Override
    public boolean shouldRun() {
        return !seederManager.hasRun(this.getClass().getName());
    }
}
