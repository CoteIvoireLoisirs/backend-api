package com.erastedev.ciexplore.v1.domain.ports.out.seeders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractCommonSeeder {
    @Autowired
    private SeederManager seederManager;

    /**
     * Execute the seeder.
     * <p>
     * This method must be implemented by all concrete subclasses of {@link AbstractCommonSeeder}.
     * It is responsible for executing the logic of the seeder.
     */
    public abstract void run();

    /**
     * Indicates whether this seeder should be executed.
     * <p>
     * This method should be implemented by all concrete subclasses of
     * {@link AbstractCommonSeeder}. It should return <code>true</code> if the seeder
     * should be executed, and <code>false</code> otherwise.
     * <p>
     * The default implementation returns <code>false</code>.
     * <p>
     * The value returned by this method is used by the seeder manager to
     * determine whether the seeder should be executed at application startup
     * time.
     *
     * @return <code>true</code> if the seeder should be executed, and
     * <code>false</code> otherwise.
     */
    public abstract boolean shouldRun();

    /**
     * Handles the {@link ContextRefreshedEvent} and executes the seeder if it
     * should run.
     * <p>
     * This method is an event listener that is called whenever the
     * application context is refreshed. It checks whether the seeder should
     * run (using the {@link #shouldRun()} method), and if so, it executes the
     * seeder (by calling the {@link #run()} method).
     *
     * @param event the context refreshed event.
     */
    @EventListener
    public void seed(ContextRefreshedEvent event) {
        if (shouldRun()) {
            run();
            seederManager.markAsRun(this.getClass().getName());
        }
    }
}
