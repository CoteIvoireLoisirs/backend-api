/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-11-08 14:00:05
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-11-08 14:00:05
 */
package com.erastedev.ciexplore.v1.domain.ports.in.rights;

import com.erastedev.ciexplore.v1.domain.entities.rights.Right;

public interface IRightService {
    /**
     * Create default rights in the database if they do not exist.
     * <p>
     * This method is used to populate the database with default rights when the
     * application is first started, and can also be used to reset the default
     * rights if they are deleted.
     */
    void createDefaultRightsIfNotExist();

    /**
     * Checks if a default right with the specified name exists in the database.
     *
     * @return true if a default right with the specified name exists, false otherwise.
     */
    boolean checkIfRightDefaultExists();

    /**
     * Save a default right in the database if it does not exist.
     * <p>
     * This method is used to create a default right in the database if it does not
     * exist. If the right already exists, it does nothing.
     *
     * @param name the name of the right to be saved.
     */
    public void saveSystemRight(String name);

    /**
     * Determines if the given right is a system right.
     * <p>
     * This method checks the specified right and returns true if it is
     * marked as a system right, false otherwise.
     *
     * @param right the right to be checked.
     * @return true if the right is a system right, false otherwise.
     */
    public boolean isSystemRight(Right right);
}
