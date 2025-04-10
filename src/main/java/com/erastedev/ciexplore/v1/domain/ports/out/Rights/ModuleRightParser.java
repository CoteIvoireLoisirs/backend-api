/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-11-26 11:40:48
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-11-26 11:40:49
 */
package com.erastedev.ciexplore.v1.domain.ports.out.Rights;

import java.util.HashMap;

public class ModuleRightParser {
    /**
     * Parses the rights string and returns a structured HashMap.
     *
     * @param rights the input string containing module-right pairs
     * @return a HashMap where keys are module names and values are maps of rights
     */
    public static HashMap<String, HashMap<String, Boolean>> parseModuleRights(String rights) {
        HashMap<String, HashMap<String, Boolean>> moduleRightsMap = new HashMap<>();

        if (rights == null || rights.isEmpty()) {
            return moduleRightsMap; // Return empty map if input is invalid
        }

        // Split the string into module-right pairs
        String[] rightsArray = rights.split(";");
        for (String singleRight : rightsArray) {
            // Split each pair into module name and rights
            String[] parts = singleRight.split("_", 2);
            if (parts.length < 2) {
                continue; // Skip invalid entries
            }

            String moduleName = parts[0];
            String rightCode = parts[1];

            // Map rights (read, write, delete) based on the right code
            HashMap<String, Boolean> rightsMap = new HashMap<>();
            rightsMap.put("read", rightCode.contains("r"));
            rightsMap.put("write", rightCode.contains("w"));
            rightsMap.put("delete", rightCode.contains("d"));

            // Add to the outer map
            moduleRightsMap.put(moduleName, rightsMap);
        }

        return moduleRightsMap;
    }
}
