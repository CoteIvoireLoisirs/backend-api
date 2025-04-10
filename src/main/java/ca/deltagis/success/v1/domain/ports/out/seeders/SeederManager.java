package ca.deltagis.success.v1.domain.ports.out.seeders;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class SeederManager {
    private final Set<String> executedSeeders = new HashSet<>();

    /**
     * Marks the specified seeder as executed by adding its name to the set of executed seeders.
     *
     * @param seederName the name of the seeder to be marked as executed
     */
    public void markAsRun(String seederName) {
        executedSeeders.add(seederName);
    }

    /**
     * Indicates whether the specified seeder has been executed.
     *
     * @param seederName the name of the seeder
     * @return true if the seeder has been executed, false otherwise
     */
    public boolean hasRun(String seederName) {
        return executedSeeders.contains(seederName);
    }
}
