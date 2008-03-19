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

import com.volantis.cache.group.Group;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.policies.PolicyBuilderReader;
import com.volantis.mcs.project.InternalProjectFactory;
import com.volantis.mcs.runtime.RepositoryContainer;
import com.volantis.mcs.runtime.RuntimeProject;
import com.volantis.mcs.runtime.configuration.ConfigContext;
import com.volantis.mcs.runtime.configuration.ConfigurationException;
import com.volantis.mcs.runtime.configuration.project.ProjectsConfiguration;
import com.volantis.mcs.runtime.configuration.project.RuntimeProjectConfiguration;
import com.volantis.mcs.runtime.policies.cache.CacheControlConstraintsMap;
import com.volantis.mcs.runtime.repository.remote.xml.RemoteReadersFactory;
import com.volantis.synergetics.log.LogDispatcher;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jibx.runtime.JiBXException;

/**
 * Manages projects.
 *
 * <p>Provides support for managing projects, including:</p>
 * <ul>
 * <li>Loading projects automatically.</li>
 * <li>Caching loaded projects to enable them to be retrieved quickly
 * later.</li>
 * <li>Searching for project that contains a specific policy, identified by a
 * URL.</li>
 * </ul>
 */
public class ProjectManagerImpl
        implements PopulatableProjectManager {
    /**
     * Used for logging.
     */
     private static final LogDispatcher LOGGER =
             LocalizationFactory.createLogger(ProjectManagerImpl.class);

    private final RuntimeProjectConfigurator configurator;

    /**
     * The global project.
     */
    private final RuntimeProject globalProject;

    /**
     * The map of predefined projects.
     *
     * <p>Maps from the name (or location) of the project to the
     * {@link RuntimeProject} instance.</p>
     */
    private final Map predefinedProjects;

    /**
     * A map from a {@link String} to a {@link Lock}.
     */
    private final Map locks;

    private final ProjectLoader projectLoader;

    /**
     * The optimizer to use.
     */
    private final ProjectLoadingOptimizer optimizer;

    /**
     * The default project.
     */
    private RuntimeProject defaultProject;

    private List projects;
    private RuntimePolicySourceFactory policySourceFactory;

    public ProjectManagerImpl(
            ProjectLoader projectLoader,
            RuntimeProjectConfigurator configurator,
            RepositoryContainer repositoryContainer,
            RemoteReadersFactory remoteReadersFactory) {

        this(projectLoader, configurator, repositoryContainer,
                new ProjectLoadingOptimizerImpl(), remoteReadersFactory);
    }

    public ProjectManagerImpl(
            ProjectLoader projectLoader,
            RuntimeProjectConfigurator configurator,
            RepositoryContainer repositoryContainer,
            ProjectLoadingOptimizer optimizer,
            RemoteReadersFactory remoteReadersFactory) {

        this.configurator = configurator;
        this.optimizer = optimizer;

        PolicyBuilderReader remoteReader =
                remoteReadersFactory.createPolicyBuilderReader(this);

        policySourceFactory = new RuntimePolicySourceFactoryImpl(
                InternalProjectFactory.getInternalInstance(),
                repositoryContainer, remoteReader);

        globalProject = configurator.createGlobalProject(policySourceFactory);

        locks = new HashMap();
        this.projectLoader = projectLoader;

        projects = new ArrayList();

        predefinedProjects = new HashMap();
    }

    public RuntimeProject getDefaultProject() {
        return defaultProject;
    }

    public void setDefaultProject(RuntimeProject defaultProject) {
        this.defaultProject = defaultProject;
    }

    public void createPredefinedProjects(
            ProjectsConfiguration projects, ConfigContext configContext,
            CacheControlConstraintsMap defaultLocalCacheControlConstraintsMap,
            Group defaultLocalGroup)
            throws ConfigurationException {

        PredefinedProjects predefinedProjects =
                configurator.createPredefinedProjects(projects, configContext,
                        policySourceFactory);

        Map namedProjects = predefinedProjects.getNamedProjects();
        this.predefinedProjects.putAll(namedProjects);
        defaultProject = predefinedProjects.getDefaultProject();
    }

    public RuntimeProject queryProject(
            String policyURLAsString,
            RuntimeProject bestGuess) {

        return retrieveProject(policyURLAsString, bestGuess, false);
    }

    public RuntimeProject getPredefinedProject(String name) {
        return (RuntimeProject) predefinedProjects.get(name);
    }

    public RuntimeProject getProject(
            String policyURLAsString,
            RuntimeProject bestGuess) {

        return retrieveProject(policyURLAsString, bestGuess, true);
    }

    private RuntimeProject retrieveProject(
            String policyURLAsString, RuntimeProject bestGuess,
            boolean loadProject) {
        // Try the best guess first, if it is set. This check does not need
        // to be synchronized.
        if (bestGuess != null && !bestGuess.getContainsOrphans()) {
            if (bestGuess.containsPolicy(policyURLAsString)) {
                return bestGuess;
            }
        }

        // Get the key to the lock to use for the specified policy.
        String lockKey = getLockKey(policyURLAsString);

        // Get the lock.
        Lock lock = getLock(lockKey);
        try {
            synchronized (lock) {

                // Take a local reference to the list of projects just in case
                // the list is modified by another thread while querying the
                // project.
                List projects;
                synchronized (this) {
                    projects = this.projects;
                }

                // Iterate over the projects trying to find the project that
                // contains the policy. If one could be found then it is
                // returned immediately.
                RuntimeProject project;
                for (int i = 0; i < projects.size(); i++) {
                    project = (RuntimeProject) projects.get(i);
                    if (project != bestGuess &&
                            project.containsPolicy(policyURLAsString)) {
                        return project;
                    }
                }

                // The project could not be found so try and load it if
                // requested.
                if (loadProject) {
                    RuntimeProjectConfiguration configuration = null;
                    try {
                        configuration = projectLoader.loadProjectConfiguration(
                                policyURLAsString, optimizer);
                    } catch (IOException e) {
                        // load failed, keep configuration null.
                        LOGGER.warn(
                            "failed-to-load-project", policyURLAsString, e);
                    } catch (JiBXException e) {
                        // load failed, keep configuration null.
                        LOGGER.warn(
                            "failed-to-load-project", policyURLAsString, e);
                    }
                    if (configuration == null) {
                        project = null;
                    } else {
                        project = buildProject(configuration);
                        final RuntimeProject existingProject =
                            getProjectWithSameAlias(project, projects);
                        if (existingProject != null) {
                            // we have an existing project that shares an alias
                            // with the new one, so lets us the existing project
                            // and add an alias to it so next time we'll find
                            // it easier
                            project = existingProject;
                            final String alias =
                                createAlias(policyURLAsString, existingProject);
                            existingProject.addPolicyRootAlias(alias);
                        } else {
                            // Need to add the project to the list of existing
                            // projects.
                            synchronized (this) {
                                // Check to make sure that the projects do not
                                // overlap, i.e. one is contained within another.
                                ensureNoOverlaps(project, projects);

                                projects = new ArrayList(this.projects);
                                projects.add(project);
                                this.projects = projects;
                            }
                        }
                    }
                    return project;
                }
            }
        } finally {
            releaseLock(lockKey);
        }


        // The project is not already known as this point so it is necessary to
        // try and load it in. Multiple projects could be loaded concurrently
        // so we need to try and protect against the same project being loaded
        // multiple times as that would be a waste of resources on this machine
        // and remote servers if the projects are remote.
        //
        // That can be an expensive operation and could
        // impact external servers so this tries to synchronize protect against multiple
        // threads trying to load the same project at the same time.

        return null;
    }

    /**
     * Checks if there is a project in the given list of projects that shares at
     * least one alias with the specified project.
     *
     * @param project the project that holds the aliases to search for
     * @param projects the list of project to check
     * @return the first project that has a matching alias or null if no such
     * project exist.
     */
    private RuntimeProject getProjectWithSameAlias(final RuntimeProject project,
                                                   final List projects) {
        RuntimeProject result = null;
        final Set aliases = project.getPolicyRootAliases();
        for (Iterator projectsIter = projects.iterator();
                projectsIter.hasNext() && result == null; ) {
            final RuntimeProject other = (RuntimeProject) projectsIter.next();
            final Set otherAliases = new HashSet(other.getPolicyRootAliases());
            for (Iterator aliasesIter = aliases.iterator();
                    aliasesIter.hasNext() && result == null;){
                if (otherAliases.contains(aliasesIter.next())) {
                    result = other;
                }
            }
        }
        return result;
    }

    /**
     * Creates a new alias for the specified project using the protocol, host
     * name and port number from the policy URL and the file part of an
     * existing alias.
     *
     * @param policyUrlAsString the
     * @param project
     * @return
     */
    private String createAlias(final String policyUrlAsString,
                               final RuntimeProject project) {
        try {
            final URL policyURL = new URL(policyUrlAsString);
            // get the first alias from the project (it must have at least one
            // alias and all of the aliases must have the same file part)
            final String oldAlias =
                (String) project.getPolicyRootAliases().iterator().next();
            final URL oldAliasURL = new URL(oldAlias);
            // use the protocol, host name and port number from the policy URL
            // and file part from the existing alias URL
            final URL newAliasURL = new URL(policyURL.getProtocol(),
                policyURL.getHost(), policyURL.getPort(), oldAliasURL.getFile());
            String newAlias = newAliasURL.toExternalForm();
            if (!newAlias.endsWith("/")) {
                newAlias += "/";
            }
            return newAlias;
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(
                "Cannot parse URL: " + policyUrlAsString);
        }
    }

    /**
     * Make sure that there are no overlaps.
     *
     * <p>This is called while holding the lock on the projects list.</p>
     *
     * @param project The new project being added.
     * @param projects The list of existing objects.
     */
    private void ensureNoOverlaps(RuntimeProject project, List projects) {
        final Set aliases = project.getPolicyRootAliases();
        for (Iterator projectsIter = projects.iterator();
                projectsIter.hasNext(); ) {
            final RuntimeProject other = (RuntimeProject) projectsIter.next();
            final Set otherAliases = other.getPolicyRootAliases();
            for (Iterator aliasesIter = aliases.iterator();
                    aliasesIter.hasNext();){
                final String alias = (String) aliasesIter.next();
                for (Iterator otherAliasesIter = otherAliases.iterator();
                        otherAliasesIter.hasNext();) {
                    final String otherAlias = (String) otherAliasesIter.next();
                    if (alias.startsWith(otherAlias) ||
                            otherAlias.startsWith(alias)) {
                        throw new IllegalStateException("Project '" + alias +
                                "' overlaps project '" + otherAlias + "'");
                    }
                }
            }
        }
    }

    /**
     * Build the project from the configuration.
     *
     * @param configuration The configuration from which the project will be
     *                      built.
     * @return The built project.
     */
    private RuntimeProject buildProject(
            RuntimeProjectConfiguration configuration) {

        // todo detect cycles.
        // Resolve base project.
        String baseProjectName = configuration.getFallbackProjectName();
        RuntimeProject baseProject;
        if (baseProjectName != null) {
            String location = configuration.getLocation();
            String baseProjectURL;
            try {
                URL base = new URL(location);
                URL url = new URL(base, baseProjectName);
                baseProjectURL = url.toExternalForm();
            } catch (MalformedURLException e) {
                throw new IllegalStateException("Cannot resolve base project " +
                        "name " + baseProjectName + " against " + location +
                        " because '" + e.getMessage() + "'");
            }
            baseProject = getProject(baseProjectURL, null);
            if (baseProject == null) {
                throw new IllegalStateException(
                        "Could not find base project at " + baseProjectURL);
            }
        } else {
            baseProject = null;
        }

        return configurator.buildProject(configuration, baseProject,
                policySourceFactory);
    }

    public RuntimeProject getGlobalProject() {
        if (globalProject == null) {
            throw new IllegalStateException(
                    "Global project has not been initialised");
        }

        return globalProject;
    }

    private String getLockKey(String url) {
        return "";
    }

    private Lock getLock(String key) {
        synchronized (locks) {
            Lock lock = (Lock) locks.get(key);
            if (lock == null) {
                lock = new Lock();
                locks.put(key, lock);
            }
            return lock;
        }
    }

    private void releaseLock(String lockKey) {
        synchronized (locks) {
            locks.remove(lockKey);
        }

    }

    /**
     *
     */
    private class Lock {

    }


//    // todo refactor this code with the code in RuntimeProjectBuilder.
//    public static class ProjectsCreator {
//
//        /**
//         * A special instance that is inserted into the map of named projects
//         * when a project starts building and is replaced by the project when it
//         * has finished building.
//         *
//         * <p>This is used to detect when there is a cycle in the fallback
//         * path.</p>
//         */
//        private static Object PROJECT_PENDING = new Object();
//
//        private final ConfigContext configContext;
//        private final CacheControlConstraintsMap defaultLocalCacheControlConstraintsMap;
//        private final Group defaultLocalGroup;
//        private final Map projectConfigurations;
//        private final RuntimeProjectConfiguration defaultProjectConfiguration;
//        private Map namedProjects;
//        private RuntimeProject defaultProject;
//        private final RepositoryContainer repositoryContainer;
//
//        public ProjectsCreator(
//                ConfigContext configContext,
//                ProjectsConfiguration projects,
//                CacheControlConstraintsMap defaultLocalCacheControlConstraintsMap,
//                Group defaultLocalGroup,
//                RepositoryContainer repositoryContainer) {
//
//            this.configContext = configContext;
//            this.defaultLocalCacheControlConstraintsMap =
//                    defaultLocalCacheControlConstraintsMap;
//            this.defaultLocalGroup = defaultLocalGroup;
//            this.projectConfigurations = projects.getNamedProjects();
//            defaultProjectConfiguration = projects.getDefaultProject();
//            this.repositoryContainer = repositoryContainer;
//        }
//
//        public void createProjects() throws ConfigurationException {
//
//            namedProjects = new HashMap();
//
//            for (Iterator i = projectConfigurations.values().iterator();
//                 i.hasNext();) {
//                RuntimeProjectConfiguration configuration =
//                        (RuntimeProjectConfiguration) i.next();
//
//                List dependencyPath = Collections.EMPTY_LIST;
//                createProject(configuration, dependencyPath);
//            }
//
//            // Create the default project after the named ones as it may
//            // fallback to one of them.
//            defaultProject = createDefaultProject(defaultProjectConfiguration);
//        }
//
//        public Map getNamedProjects() {
//            return namedProjects;
//        }
//
//        public RuntimeProject getDefaultProject() {
//            return defaultProject;
//        }
//
//        private RuntimeProject createDefaultProject(
//                final RuntimeProjectConfiguration configuration)
//                throws ConfigurationException {
//
//            // If we were asked to preload the local repository...
//            RuntimeProjectBuilder builder =
//                    new RuntimeProjectBuilder();
//            builder.setPreload(Boolean.TRUE.equals(
//                    configuration.getPreload()));
//
//            List dependencyPath = Collections.singletonList("<default>");
//
//            initialiseProjectBuilder(configuration, dependencyPath, builder);
//
//            // This project contains those pages that are local and do not belong
//            // to another project.
//            builder.setContainsOrphans(true);
//
//            return builder.getProject();
//        }
//
//        private void setCacheControlConstraintsMap(
//                PolicySource policySource, RuntimeProjectBuilder builder) {
//            // Assume that this project is local.
//            if (policySource instanceof LocalPolicySource) {
//                builder.setCacheControlDefaultsMap(
//                        defaultLocalCacheControlConstraintsMap);
//                builder.setCacheGroup(defaultLocalGroup);
//            } else {
//                throw new IllegalStateException(
//                        "Cannot handle remote projects");
//            }
//        }
//
//        private RuntimeProject createProject(
//                RuntimeProjectConfiguration configuration, List dependencyPath)
//                throws ConfigurationException {
//
//            String name = configuration.getName();
//
//            // Add this project name to the dependency path.
//            dependencyPath = new ArrayList(dependencyPath);
//            dependencyPath.add(name);
//
//            // Check to see whether this project has already or is being built.
//            Object existing = namedProjects.get(name);
//            if (existing == PROJECT_PENDING) {
//                throw new IllegalStateException(
//                        "Cycle in project fallback detected: " + dependencyPath);
//            } else if (existing != null) {
//                return (RuntimeProject) existing;
//            }
//
//            // Remember that this project is being built.
//            namedProjects.put(name, PROJECT_PENDING);
//
//            RuntimeProjectBuilder builder = new RuntimeProjectBuilder();
//            builder.setName(name);
//
//            initialiseProjectBuilder(configuration, dependencyPath, builder);
//
//            // Add the configuration to the map
//            RuntimeProject predefined = builder.getProject();
//            namedProjects.put(name, predefined);
//
//            return predefined;
//        }
//
//        /**
//         * Perform initialisation of the project that is common between the
//         * named and default projects.
//         *
//         * @param configuration  The configuration that is driving the
//         *                       initialisation.
//         * @param dependencyPath The dependency path.
//         * @param builder        The builder being initialised.
//         * @throws ConfigurationException
//         */
//        private void initialiseProjectBuilder(
//                RuntimeProjectConfiguration configuration, List dependencyPath,
//                RuntimeProjectBuilder builder)
//                throws ConfigurationException {
//
//            RuntimeProject fallbackProject = getFallbackProject(configuration,
//                    dependencyPath);
//            builder.setBaseProject(fallbackProject);
//
//            // Initialise the policy source.
//            AbstractPoliciesConfiguration policies = configuration.getPolicies();
//            PolicySource policySource = createPolicySource(policies);
//            builder.setPolicySource(policySource);
//
//            // Initialise the generated resource base directory.
//            String generatedResourceBaseDir = null;
//            GeneratedResourcesConfiguration generatedResourcesConfiguration =
//                    configuration.getGeneratedResources();
//            if (generatedResourcesConfiguration != null) {
//                generatedResourceBaseDir =
//                        generatedResourcesConfiguration.getBaseDir();
//            }
//            builder.setGeneratedResourceBaseDir(generatedResourceBaseDir);
//
//            // Initialise the asset configuration.
//            builder.setAssetsConfiguration(configuration.getAssets());
//
//            // Set the cache control constraints map.
//            setCacheControlConstraintsMap(policySource, builder);
//        }
//
//        private RuntimeProject getFallbackProject(
//                RuntimeProjectConfiguration configuration, List dependencyPath)
//                throws ConfigurationException {
//
//
//            RuntimeProject fallbackProject = null;
//            String fallbackProjectName = configuration.getFallbackProjectName();
//            if (fallbackProjectName != null) {
//                RuntimeProjectConfiguration fallbackConfiguration =
//                        (RuntimeProjectConfiguration) projectConfigurations.get(fallbackProjectName);
//                if (fallbackConfiguration == null) {
//                    throw new IllegalStateException("Named configuration '" +
//                            configuration.getName() +
//                            "' extends unknown configuration '" +
//                            fallbackProjectName + "'");
//                }
//
//                fallbackProject =
//                        createProject(fallbackConfiguration, dependencyPath);
//            }
//
//            return fallbackProject;
//        }
//
//        private PolicySource createPolicySource(
//                AbstractPoliciesConfiguration policies)
//                throws ConfigurationException {
//
//            InternalProjectFactory factory =
//                    InternalProjectFactory.getInternalInstance();
//
//            // todo what about remote policies.
//            PolicySource policySource;
//            if (policies instanceof XmlPoliciesConfiguration) {
//                String directoryPath =
//                        ((XmlPoliciesConfiguration) policies).getDirectory();
//                File directory = configContext.getConfigRelativeFile(
//                    directoryPath, true);
//                if (directory != null) {
//                    policySource = factory.createXMLPolicySource(
//                            repositoryContainer.getXMLRepository(),
//                            directory.getAbsolutePath());
//                } else {
//                    throw new ConfigurationException(exceptionLocalizer.format(
//                            "config-file-base-directory-non-existant",
//                            directoryPath));
//                }
//            } else {
//                policySource = factory.createJDBCPolicySource(
//                        repositoryContainer.getJDBCRepository(),
//                        ((JdbcPoliciesConfiguration) policies).getName());
//            }
//            return policySource;
//        }
//    }
}
