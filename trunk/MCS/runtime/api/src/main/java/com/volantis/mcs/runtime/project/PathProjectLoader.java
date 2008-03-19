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

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.project.ProjectConfigurationReader;
import com.volantis.mcs.runtime.configuration.project.RuntimeProjectConfiguration;
import com.volantis.synergetics.log.LogDispatcher;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Attempts to load a project from an abstract {@link Path}.
 *
 * <p>This assumes that the tail end of the URL has a path like structure, i.e.
 * path components separated by /. e.g.</p>
 *
 * <pre>
 * file:/a/b/c/d.mimg
 * http://www.volantis.com/a/b/mimg
 * jndi:/localhost/volantis/welcome/welcome.mlyt
 * </pre>
 *
 * <p>The URL passed in is the URL to a resource (policy, some thing else)
 * within a project. The first thing that is done is to get a URL to the
 * directory containing that resource. That is done by stripping off the last
 * / and everything after it. This will also remove any query parameters
 * and such like.</p>
 */
public abstract class PathProjectLoader
        implements ProjectLoader {

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(PathProjectLoader.class);

    /**
     * Name of the project configuration file
     */
    protected static final String PROJECT_FILE_NAME = "mcs-project.xml";

    private final ProjectConfigurationReader reader;

    /**
     * Initialise.
     */
    public PathProjectLoader() {
        reader = new ProjectConfigurationReader();
    }

    /**
     * Create the {@link Path} from the url.
     *
     * @param url The input URL.
     * @return The {@link Path}.
     */
    public abstract Path createPath(String url);

    // Javadoc inherited.
    public RuntimeProjectConfiguration loadProjectConfiguration(
            String url, ProjectLoadingOptimizer optimizer)
            throws IOException {

        Path path = createPath(url);

        RuntimeProjectConfiguration configuration = null;

        // The list of visited paths (stored as Strings).
        List visited = null;

        Path parent = null;

        while (configuration == null) {
            // Get the directory (parent path).
            parent = path.getParentPath();
            if (parent == null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Could not load project from '" + url + "'");
                }
                break;
            }

            // Try and load the configuration from the directory.
            String projectDirAsString = parent.toExternalForm();

            // If an optimizer has been specified then check with it whether
            // the path may represent a project and if not prevent the attempt
            // to load.
            if (optimizer != null) {
                if (optimizer.isProject(projectDirAsString) ==
                        ProjectLoadingOptimizer.NO) {

                    if (logger.isDebugEnabled()) {
                        logger.debug("Not attempting to load configuration " +
                                "from " + projectDirAsString +
                                " as it has already been checked and did " +
                                "not exist");
                    }

                    // Neither this directory, or any of its ancestors,
                    // contains a project so stop looking immediately.
                    break;
                }
            }

            Path projectFile = parent.getChild(PROJECT_FILE_NAME);
            configuration = loadConfiguration(projectFile);

            if (configuration == null) {

                if (logger.isDebugEnabled()) {
                    logger.debug("Could not load project from " +
                            projectDirAsString);
                }

                // Add the path to the list of visited, if there is an
                // optimizer.
                if (optimizer != null) {
                    if (visited == null) {
                        visited = new ArrayList();
                    }
                    visited.add(projectDirAsString);
                }

                // Try the next directory above.
                path = parent;
            }
        }

        // If no configuration could be loaded and there is an optimizer then
        // pass the set of paths that were visited, if any so it can prevent
        // them being checked again.
        if (configuration == null && optimizer != null && visited != null) {
            optimizer.notProject(visited);
        }

        return configuration;
    }

    /**
     * Load the configuration from the path.
     *
     * @param projectFile The path.
     * @return The
     */
    protected RuntimeProjectConfiguration loadConfiguration(Path projectFile) {

        RuntimeProjectConfiguration configuration = null;
        try {
            InputStream stream = null;
            try {
                stream = projectFile.openStream();
                String url = projectFile.toExternalForm();
                if (stream == null) {
                    // Could not open stream to path.
                    if (logger.isDebugEnabled()) {
                        logger.debug("Could not open stream to path '" +
                                url + "'");
                    }
                } else {
                    configuration = reader.readProject(stream, url);
                }
            } finally {
                if (stream != null) {
                    stream.close();
                }
            }
        } catch (IOException e) {
            // todo improve logging.
            if (logger.isErrorEnabled()) {
                logger.error("cannot-load-project", e);
            }
        }

        return configuration;
    }

    /**
     * Parse an input stream into a {@link com.volantis.mcs.runtime.configuration.project.RuntimeProjectConfiguration}.
     *
     * @param projectStream input stream to be parsed into a project config
     * @return AbstractProjectConfiguration to use to build the
     *         {@link com.volantis.mcs.runtime.RuntimeProject} for this request
     */
    protected RuntimeProjectConfiguration createProjectConfiguration(
            InputStream projectStream, String url) throws IOException {

        RuntimeProjectConfiguration projectConfiguration = null;
        if (projectStream != null) {
            InputStreamReader projectReader =
                    new InputStreamReader(projectStream);
            ProjectConfigurationReader projectConfigurationReader
                    = new ProjectConfigurationReader();
            projectConfiguration = projectConfigurationReader.readProject(
                    projectReader, url);
        }

        return projectConfiguration;
    }
}
