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

import com.volantis.mcs.runtime.RuntimeProject;

/**
 * Manages projects used within an instance of MCS.
 *
 * <p>Maintains a cache of all the projects that are currently in use by MCS.
 * This cache
 *
 * thread safe.
 *
 * @todo How is the project cache managed ?
 * @mock.generate
 */
public interface ProjectManager {

    /**
     * Locate the project that contains the specified URL.
     *
     * <p>Project is located as follows:</p>
     *
     * <ol>
     * <li>If it is supplied then the best guess projects is checked first, if
     * it contains the URL then it is returned. This is a simple optimisation
     * that reflects the fact that the caller knows the source of the URL and
     * therefore may be able to make a guess at the project to which it
     * belongs. e.g. if the URL is constructed from a relative URL then it is
     * likely that the resulting URL is owned by the same project as the
     * document / policy that contained the relative URL.</li>
     *
     * <li>The set of known projects is searched to see whether any
     * of them contain the URL. If one does then it is returned.</li>
     *
     * <li>An attempt is made to try and load the project from the
     * URL. If this succeeds then the project is cached and returned.
     * </li>
     *
     * </ol>
     *
     * @param policyURLAsString The URL whose containing project is requested.
     * @param bestGuess         An optional best guess as to the project that
     *                          may contain the URL.
     * @return The project containing the URL, or null if it could not be found.
     */
    RuntimeProject getProject(
            String policyURLAsString, RuntimeProject bestGuess);

    RuntimeProject getGlobalProject();

    RuntimeProject getDefaultProject();

    /**
     * As {@link #getProject(String, com.volantis.mcs.runtime.RuntimeProject)}
     * except that it does not load the project if it could not be found.
     *
     * @param policyURLAsString The URL whose containing project is requested.
     * @param bestGuess         An optional best guess as to the project that
     *                          may contain the URL.
     * @return The project containing the URL, or null if it could not be found.
     */
    RuntimeProject queryProject(
            String policyURLAsString, RuntimeProject bestGuess);

    /**
     * Get a predefined project.
     *
     * @param name The name of the project.
     * @return The project, or null if it could not be found.
     */
    RuntimeProject getPredefinedProject(String name);

}
