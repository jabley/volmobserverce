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

import java.util.List;

/**
 * Encapsulates information necessary to optimize project loading.
 *
 * @mock.generate
 */
public interface ProjectLoadingOptimizer {
    /**
     * The path has been seen before and was not a project.
     */
    Result NO = new Result("NO");
    /**
     * The path has not been seen before and so may be a project.
     */
    Result MAYBE = new Result("MAYBE");

    /**
     * Indicates to the optimizer that no project was found in any of the
     * paths.
     *
     * @param paths The paths that do not have a project.
     */
    void notProject(List paths);

    /**
     * Check to see whether the path is a project.
     *
     * @param path The path to check.
     * @return {@link ProjectLoadingOptimizer#MAYBE} if the path has not been
     *         seen before, otherwise {@link ProjectLoadingOptimizer#NO}.
     */
    Result isProject(String path);

    /**
     * The result of checking a path for a project.
     */
    public class Result {

        /**
         * The name of the entry in the enumeration.
         */
        private final String name;

        /**
         * Initialise.
         *
         * @param name The name of the entry in the enumeration.
         */
        private Result(String name) {
            this.name = name;
        }

        // Javadoc inherited.
        public String toString() {
            return name;
        }
    }
}
