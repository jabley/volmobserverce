/*
This file is part of Volantis Mobility Server. 

Volantis Mobility Server is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Volantis Mobility Server is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Volantis Mobility Server.  If not, see <http://www.gnu.org/licenses/>. 
*/
/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime.project;

import java.util.HashSet;
import java.util.Set;
import java.util.List;

/**
 * Attempts to optimize project loading in the situation where the project
 * cannot be found by caching paths that did not refer to a project.
 *
 * <p>There will be an entry for every location that is checked that does not
 * contain a project. e.g. if there are no projects i
 *
 * <p>There are a lot of issues with this that will need addressing in
 * future.</p>
 *
 * <ul>
 * <li>There will be an entry for every location that is checked for a project
 * file, even if they are never used. e.g. if there is no project file in
 * <code>file:/home/user/project/dir/</code> then there will be an entry for
 * that URL in the cache. If there is a project file in
 * <code>file:/home/user/project/</code> then the above entry will never be
 * used as the project contains <code>file:/home/user/project/dir/</code> and
 * so neither it nor any of its nested directories will ever be checked again.
 * </li>
 *
 * <li>Once an entry has been added it will never be cleared, this is both
 * a potential memory leak and also prevents project files being copied into a
 * previously checked directory.</li>
 * </ul>
 */
public class ProjectLoadingOptimizerImpl
        implements ProjectLoadingOptimizer {

    /**
     * A set of string representation of those paths that do not contain a
     * project.
     */
    private final Set notProjects;

    public ProjectLoadingOptimizerImpl() {
        notProjects = new HashSet();
    }

    // Javadoc inherited.
    public void notProject(List paths) {
        synchronized(this) {
            for (int i = 0; i < paths.size(); i++) {
                String path = (String) paths.get(i);
                notProjects.add(path);
            }
        }
    }

    // Javadoc inherited.
    public Result isProject(String path) {
        synchronized(this) {
            if (notProjects.contains(path)) {
                return NO;
            } else {
                return MAYBE;
            }
        }
    }
}
