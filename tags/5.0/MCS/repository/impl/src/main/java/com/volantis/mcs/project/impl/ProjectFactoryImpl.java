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

package com.volantis.mcs.project.impl;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.project.InternalProjectFactory;
import com.volantis.mcs.project.InternalProjectImpl;
import com.volantis.mcs.project.PolicySource;
import com.volantis.mcs.project.Project;
import com.volantis.mcs.project.ProjectConfiguration;
import com.volantis.mcs.project.remote.RemotePolicySource;
import com.volantis.mcs.project.impl.jdbc.JDBCPolicySourceImpl;
import com.volantis.mcs.project.impl.xml.XMLPolicySourceImpl;
import com.volantis.mcs.project.impl.remote.RemotePolicySourceImpl;
import com.volantis.mcs.project.jdbc.JDBCPolicySource;
import com.volantis.mcs.project.xml.XMLPolicySource;
import com.volantis.mcs.repository.LocalRepository;
import com.volantis.mcs.repository.Repository;
import com.volantis.mcs.repository.LocalXMLRepository;
import com.volantis.mcs.repository.LocalJDBCRepository;
import com.volantis.mcs.policies.PolicyBuilderReader;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import java.io.File;

public class ProjectFactoryImpl
        extends InternalProjectFactory {

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(ProjectFactoryImpl.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(
                    ProjectFactoryImpl.class);

    // Javadoc inherited.
    public ProjectConfiguration createProjectConfiguration() {
        return new ProjectConfigurationImpl();
    }

    // Javadoc inherited.
    public Project createProject(ProjectConfiguration configuration) {

        PolicySource policySource = null;

        Repository repository = configuration.getRepository();
        String policyRoot = configuration.getPolicyLocation();
        if (repository instanceof LocalRepository) {
            LocalRepository localRepository =
                    (LocalRepository) repository;
            if (localRepository instanceof LocalXMLRepository) {

                createProjectDirectory(policyRoot,
                        configuration.isDeleteProjectContents());

                policySource =
                        new XMLPolicySourceImpl(localRepository, policyRoot);

            } else if (localRepository instanceof LocalJDBCRepository) {
                policySource =
                        new JDBCPolicySourceImpl(localRepository, policyRoot);
            }
        }

        if (policySource == null) {
            throw new IllegalArgumentException(
                    "Unknown repository type " + repository);
        }

        return new InternalProjectImpl(policySource);
    }

    // Javadoc inherited.
    public XMLPolicySource createXMLPolicySource(
            LocalRepository repository, String directory) {
        return new XMLPolicySourceImpl(repository, directory);
    }

    // Javadoc inherited.
    public JDBCPolicySource createJDBCPolicySource(
            LocalRepository repository, String projectName) {
        return new JDBCPolicySourceImpl(repository, projectName);
    }

    // Javadoc inherited.
    public RemotePolicySource createRemotePolicySource(
            PolicyBuilderReader reader, String baseURL) {
        return new RemotePolicySourceImpl(reader, baseURL);
    }

    /**
     * Utility method to avoid code duplication. Ensures that if a path is
     * specified it is a valid absolute path. Does not check if the file exists.
     *
     * @param path absolute path to file
     * @return File mapping to that path, or null if no file found
     */
    private static File createRepositoryFile(String path) {
        File f = null;
        if ((path != null) && (path.length() != 0)) {
            f = new File(path);
            if (!f.isAbsolute()) {
                // should not happen - paths should be validated by this point
                throw new IllegalArgumentException(EXCEPTION_LOCALIZER.format(
                        "file-path-is-not-absolute", path));
            }
        }
        return f;
    }

    /**
     * Resolve the directory name given and create the directory for use as the
     * default project directory. The DEFAULT_PROJECT_DIRECTORY_PROPERTY is
     * only used by command line tools and test cases..
     *
     * @param defaultProjectDirName the directory name to validate.
     * @param replaceDir            whether or not to delete and recreate the directory
     * @return the default Directory or null if the name did not map to a
     *         directory
     */
    private static File createProjectDirectory(
            String defaultProjectDirName, boolean replaceDir) {
        File defaultProjectDir = createRepositoryFile(defaultProjectDirName);
        if (defaultProjectDir != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Creating repository using the default project " +
                        "directory " +
                        defaultProjectDir.getAbsolutePath());
            }

            if (defaultProjectDir.exists() &&
                    !defaultProjectDir.isDirectory()) {
                throw new IllegalArgumentException(EXCEPTION_LOCALIZER.format(
                        "directory-is-file",
                        defaultProjectDir));

            }

            if (replaceDir) {
                if (defaultProjectDir.exists()) {
                    deleteDir(defaultProjectDir);
                }
            }

            if (!defaultProjectDir.exists()) {
                if (!defaultProjectDir.mkdirs()) {
                    throw new IllegalArgumentException(EXCEPTION_LOCALIZER.format(
                            "file-already-exists",
                            defaultProjectDir));
                }
            }
        } else if (logger.isDebugEnabled()) {
            logger.debug("No default project directory specified");
        }
        return defaultProjectDir;
    }

    /**
     * Deletes all files in the specified directory and sub-directories
     *
     * @param dir
     */
    private static void deleteDir(File dir) {
        String list[] = dir.list();

        if (list == null) {
            return;
        }

        for (int i = 0; i < list.length; i++) {
            File file = new File(dir, list[i]);

            if (file.isDirectory()) {
                deleteDir(file);
            } else {
                if (!file.delete()) {
                    throw new IllegalArgumentException(EXCEPTION_LOCALIZER.format(
                            "cannot-delete-file", file.getAbsolutePath()));
                }
            }
        }

        if (!dir.delete()) {
            throw new IllegalArgumentException(EXCEPTION_LOCALIZER.format(
                    "directory-cannot-be-deleted", dir.getAbsolutePath()));
        }
    }
}
