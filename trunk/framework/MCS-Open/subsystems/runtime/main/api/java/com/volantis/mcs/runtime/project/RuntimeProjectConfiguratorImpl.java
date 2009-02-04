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

import com.volantis.cache.CacheFactory;
import com.volantis.cache.group.Group;
import com.volantis.cache.group.GroupBuilder;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.project.LocalPolicySource;
import com.volantis.mcs.project.PolicySource;
import com.volantis.mcs.project.remote.RemotePolicySource;
import com.volantis.mcs.runtime.ExternalPathToInternalURLMapper;
import com.volantis.mcs.runtime.RuntimeProject;
import com.volantis.mcs.runtime.RuntimeProjectBuilder;
import com.volantis.mcs.runtime.configuration.ConfigContext;
import com.volantis.mcs.runtime.configuration.ConfigurationException;
import com.volantis.mcs.runtime.configuration.RemotePolicyCacheConfiguration;
import com.volantis.mcs.runtime.configuration.project.AbstractPoliciesConfiguration;
import com.volantis.mcs.runtime.configuration.project.AssetsConfiguration;
import com.volantis.mcs.runtime.configuration.project.DefaultProjectLayoutLocator;
import com.volantis.mcs.runtime.configuration.project.GeneratedResourcesConfiguration;
import com.volantis.mcs.runtime.configuration.project.PolicyCachePartitionConfiguration;
import com.volantis.mcs.runtime.configuration.project.PolicyTypePartitionConfiguration;
import com.volantis.mcs.runtime.configuration.project.ProjectThemes;
import com.volantis.mcs.runtime.configuration.project.ProjectsConfiguration;
import com.volantis.mcs.runtime.configuration.project.RuntimeProjectConfiguration;
import com.volantis.mcs.runtime.policies.cache.CacheControlConstraints;
import com.volantis.mcs.runtime.policies.cache.CacheControlConstraintsMap;
import com.volantis.mcs.runtime.policies.cache.PolicyCache;
import com.volantis.mcs.runtime.policies.cache.PolicyCachePartitionConstraints;
import com.volantis.mcs.runtime.policies.cache.SeparateCacheControlConstraintsMap;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Implementation of {@link RuntimeProjectConfigurator}.
 */
public class RuntimeProjectConfiguratorImpl
        implements RuntimeProjectConfigurator {

    /**
     * Used for logging.
     */
     private static final LogDispatcher logger =
             LocalizationFactory.createLogger(RuntimeProjectConfiguratorImpl.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory.createExceptionLocalizer(RuntimeProjectConfiguratorImpl.class);

    /**
     * The local constraints map.
     */
    private final CacheControlConstraintsMap localConstraintsMap;

    /**
     * The remote constraints map.
     */
    private final CacheControlConstraintsMap remoteConstraintsMap;

    /**
     * The path to URL mapper.
     */
    private final ExternalPathToInternalURLMapper pathURLMapper;

    /**
     * The underlying policy cache.
     */
    private final PolicyCache policyCache;

    /**
     * Initialise.
     *
     * @param localConstraintsMap  The local constraints map.
     * @param remoteConstraintsMap The remote constraints map.
     * @param pathURLMapper        The path to URL mapper.
     * @param policyCache          The underlying policy cache.
     */
    public RuntimeProjectConfiguratorImpl(
            CacheControlConstraintsMap localConstraintsMap,
            CacheControlConstraintsMap remoteConstraintsMap,
            ExternalPathToInternalURLMapper pathURLMapper,
            PolicyCache policyCache) {
        this.localConstraintsMap = localConstraintsMap;
        this.remoteConstraintsMap = remoteConstraintsMap;
        this.pathURLMapper = pathURLMapper;
        this.policyCache = policyCache;
    }

    // Javadoc inherited.
    public RuntimeProject createGlobalProject(
            RuntimePolicySourceFactory policySourceFactory) {
        RuntimeProjectBuilder builder = new RuntimeProjectBuilder();
        builder.setPolicySource(policySourceFactory.createRemotePolicySource(
                ""));
        builder.setCacheControlDefaultsMap(remoteConstraintsMap);
        builder.setRemote(true);
        builder.setContainsOrphans(true);
        RuntimeProject project = builder.getProject();
        return project;
    }

    // Javadoc inherited.
    public RuntimeProject buildProject(
            RuntimeProjectConfiguration configuration,
            RuntimeProject baseProject,
            RuntimePolicySourceFactory policySourceFactory) {

        RuntimeProjectBuilder builder =
                new RuntimeProjectBuilder();
        configureBuilder(builder, configuration, policySourceFactory);
        builder.setBaseProject(baseProject);

        initialiseProjectCache(configuration, builder);

        return builder.getProject();
    }

    /**
     * Initialise the cache associated with the project.
     *
     * @param configuration The configuration.
     * @param builder       The builder.
     */
    private void initialiseProjectCache(
            RuntimeProjectConfiguration configuration,
            RuntimeProjectBuilder builder) {
        // If the project does not define its own partition then use either the
        // default local, or default remote one.
        PolicyCachePartitionConfiguration partition = configuration.getPolicyCachePartition();
        if (partition == null) {
            if (builder.isRemote()) {
                // The project is remote so use the group associated with the
                // remote partition if any, or the default remote group.
                String location = configuration.getLocation();
                Group group = policyCache.getRemotePartitionGroup(location);
                if (group == null) {
                    group = policyCache.getRemoteDefaultGroup();
                }
                builder.setCacheControlDefaultsMap(remoteConstraintsMap);
                builder.setCacheGroup(group);
            } else {
                // Use the default local partition.
                builder.setCacheControlDefaultsMap(localConstraintsMap);
                builder.setCacheGroup(policyCache.getLocalDefaultGroup());
            }
        } else {
            RemotePolicyCacheConfiguration partitionConfiguration =
                    partition.getConstraintsConfiguration();

            PolicyCachePartitionConstraints partitionConstraints;
            Group baseGroup;
            if (builder.isRemote()) {
                // The project is remote so use the group associated with the
                // remote partition if any, or the remote group.
                String location = configuration.getLocation();
                baseGroup = policyCache.getRemotePartitionGroup(location);
                if (baseGroup == null) {
                    baseGroup = policyCache.getRemoteGroup();
                }
                partitionConstraints =
                        policyCache.getRemotePartitionConstraints();
            } else {
                baseGroup = policyCache.getLocalGroup();
                partitionConstraints =
                        policyCache.getLocalPartitionConstraints();
            }

            int partitionSize = partitionConstraints.constrainPartitionSize(
                    partition.getSize());
            if (partitionSize < 1) {
                throw new IllegalStateException("Partition size must be >= 1");
            }

            CacheFactory cacheFactory = CacheFactory.getDefaultInstance();
            GroupBuilder groupBuilder = cacheFactory.createGroupBuilder();
            groupBuilder.setMaxCount(partitionSize);
            Group partitionGroup = baseGroup.addGroup(
                    configuration.getLocation(), groupBuilder);

            // Create the base constraints for the partition. These will
            // be used to derive the constraints for the type specific
            // partitions.
            CacheControlConstraints base =
                    new CacheControlConstraints(
                            partitionConstraints.getConstraints(),
                            partitionConfiguration);

            builder.setCacheControlDefaults(base.getDefaultCacheControl());

            SeparateCacheControlConstraintsMap constraintsMap =
                    new SeparateCacheControlConstraintsMap();
            for (Iterator i = PolicyType.getPolicyTypes().iterator(); i.hasNext();) {
                PolicyType policyType = (PolicyType) i.next();
                PolicyTypePartitionConfiguration typePartitionConfiguration =
                        partition.getTypePartition(policyType);
                CacheControlConstraints typeConstraints;
                groupBuilder = cacheFactory.createGroupBuilder();
                if (typePartitionConfiguration == null) {
                    typeConstraints = base;
                    groupBuilder.setMaxCount(partitionSize);
                } else {
                    RemotePolicyCacheConfiguration typeConfiguration =
                            typePartitionConfiguration.getConstraints();
                    if (typeConfiguration == null) {
                        typeConstraints = base;
                    } else {
                        typeConstraints = new CacheControlConstraints(base,
                                typeConfiguration);
                    }
                    groupBuilder.setMaxCount(typePartitionConfiguration.getSize());
                }
                partitionGroup.addGroup(policyType, groupBuilder);
                constraintsMap.addConstraints(policyType, typeConstraints);
            }

            builder.setCacheGroup(partitionGroup);
            builder.setCacheControlDefaultsMap(constraintsMap);
        }
    }

    private void configureBuilder(
            RuntimeProjectBuilder builder,
            RuntimeProjectConfiguration configuration,
            RuntimePolicySourceFactory policySourceFactory) {

        AbstractPoliciesConfiguration policies =
                configuration.getPolicies();

        // Only configurations loaded from an mcs-project.xml file have the
        // location set and this only works if the location has been set.
        String location = configuration.getLocation();
        if (location == null) {
            throw new IllegalArgumentException("Configuration not loaded " +
                    "from mcs-project.xml");
        }

        // Set the remote flag based on where the project was loaded from.
        boolean remote = false;
        if (location.startsWith("http:")) {
            remote = true;
        }
        builder.setRemote(remote);

        String policyRootAsString = getPolicyRootFromLocation(location);

        PolicySourceSelector selector = new SeperatePolicySourceSelector(
                policySourceFactory, policyRootAsString);

        PolicySource policySource = selector.selectPolicySource(
                policies, remote);

        builder.setPolicySource(policySource);

        builder.setPolicyRootAsString(policyRootAsString);

        DefaultProjectLayoutLocator defaultProjectLayoutLocator =
                configuration.getDefaultProjectLayoutLocator();
        builder.setDefaultProjectLayoutLocator(defaultProjectLayoutLocator);

        ProjectThemes projectThemes =
                configuration.getProjectThemes();
        builder.setProjectThemes(projectThemes);

        AssetsConfiguration assets = configuration.getAssets();
        if (assets == null) {
            assets = new AssetsConfiguration();
            assets.setBaseUrl(".");
        }

        // If this is local then get the external location as relative to the
        // servlet context. Otherwise the external location is the same as the
        // location.
        String externalLocation;
        if (remote) {
            externalLocation = policyRootAsString;
        } else {
            externalLocation = pathURLMapper.mapInternalURLToExternalPath(
                    policyRootAsString);

            if (logger.isDebugEnabled()) {
                logger.debug("Mapped project location URL " +
                        policyRootAsString + " to external path " +
                        externalLocation);
            }
        }

        // Resolve the asset's base URL against the external location of the
        // project file.
        String assetsBase = assets.getBaseUrl();
        MarinerURL assetsBaseURL = new MarinerURL(externalLocation, assetsBase);
        assetsBase = assetsBaseURL.getExternalForm();

        // This method is only used for portable projects.
        builder.setPortable(true);

        assets.setBaseUrl(assetsBase);

        builder.setAssetsConfiguration(assets);

        builder.setGeneratedResourceBaseDir(assets.getBaseUrl());
    }

    private String getPolicyRootFromLocation(String location) {
        String baseURL;
        baseURL = location;
        int index = location.lastIndexOf('/');
        if (index != -1) {
            baseURL = location.substring(0, index + 1);
        }
        return baseURL;
    }

    public PredefinedProjects createPredefinedProjects(
            ProjectsConfiguration projects, ConfigContext configContext,
            RuntimePolicySourceFactory policySourceFactory)
            throws ConfigurationException {

        ProjectsCreator creator = new ProjectsCreator(configContext, projects,
                policySourceFactory);
        return creator.createPredefinedProjects();
    }

    /**
     * A special instance that is inserted into the map of named projects
     * when a project starts building and is replaced by the project when it
     * has finished building.
     *
     * <p>This is used to detect when there is a cycle in the fallback
     * path.</p>
     */
    private static Object PROJECT_PENDING = new Object();

    public class ProjectsCreator {

        private final Map projectConfigurations;
        private final RuntimeProjectConfiguration defaultProjectConfiguration;
        private final PolicySourceSelector selector;

        public ProjectsCreator(
                ConfigContext configContext,
                ProjectsConfiguration projects,
                RuntimePolicySourceFactory policySourceFactory) {

            this.projectConfigurations = projects.getNamedProjects();
            defaultProjectConfiguration = projects.getDefaultProject();
            selector = new ConfigPolicySourceSelector(policySourceFactory,
                    configContext);
        }

        private RuntimeProject createDefaultProject(
                final RuntimeProjectConfiguration configuration,
                final Map namedProjects)
                throws ConfigurationException {

            // If we were asked to preload the local repository...
            RuntimeProjectBuilder builder =
                    new RuntimeProjectBuilder();
            builder.setPreload(Boolean.TRUE.equals(
                    configuration.getPreload()));

            List dependencyPath = Collections.singletonList("<default>");

            initialiseProjectBuilder(configuration, dependencyPath, builder, namedProjects);

            // This project contains those pages that are local and do not belong
            // to another project.
            builder.setContainsOrphans(true);

            return builder.getProject();
        }

        private void setCacheControlConstraintsMap(
                PolicySource policySource, RuntimeProjectBuilder builder) {
            // Assume that this project is local.
            if (policySource instanceof LocalPolicySource) {
                builder.setCacheControlDefaultsMap(localConstraintsMap);
                builder.setCacheGroup(policyCache.getLocalDefaultGroup());
            } else if (policySource instanceof RemotePolicySource) {
                builder.setCacheControlDefaultsMap(remoteConstraintsMap);
                builder.setCacheGroup(policyCache.getRemoteDefaultGroup());
            } else {
                throw new IllegalStateException(
                        "Unknown policy source type " + policySource);
            }
        }

        private RuntimeProject createProject(
                RuntimeProjectConfiguration configuration, List dependencyPath,
                Map namedProjects)
                throws ConfigurationException {

            String name = configuration.getName();

            // Add this project name to the dependency path.
            dependencyPath = new ArrayList(dependencyPath);
            dependencyPath.add(name);

            // Check to see whether this project has already or is being built.
            Object existing = namedProjects.get(name);
            if (existing == PROJECT_PENDING) {
                throw new IllegalStateException(
                        "Cycle in project fallback detected: " + dependencyPath);
            } else if (existing != null) {
                return (RuntimeProject) existing;
            }

            // Remember that this project is being built.
            namedProjects.put(name, PROJECT_PENDING);

            RuntimeProjectBuilder builder = new RuntimeProjectBuilder();
            builder.setName(name);

            initialiseProjectBuilder(configuration, dependencyPath, builder, namedProjects);

            // Add the configuration to the map
            RuntimeProject predefined = builder.getProject();
            namedProjects.put(name, predefined);

            return predefined;
        }

        /**
         * Perform initialisation of the project that is common between the
         * named and default projects.
         *
         * @param configuration  The configuration that is driving the
         *                       initialisation.
         * @param dependencyPath The dependency path.
         * @param builder        The builder being initialised.
         * @param namedProjects
         * @throws ConfigurationException
         */
        private void initialiseProjectBuilder(
                RuntimeProjectConfiguration configuration, List dependencyPath,
                RuntimeProjectBuilder builder,
                final Map namedProjects)
                throws ConfigurationException {

            RuntimeProject fallbackProject = getFallbackProject(configuration,
                    dependencyPath, namedProjects);
            builder.setBaseProject(fallbackProject);

            // Initialise the policy source.
            AbstractPoliciesConfiguration policies = configuration.getPolicies();

            PolicySource policySource = selector.selectPolicySource(
                    policies, false);
            builder.setPolicySource(policySource);

            // Initialise the generated resource base directory.
            String generatedResourceBaseDir = null;
            GeneratedResourcesConfiguration generatedResourcesConfiguration =
                    configuration.getGeneratedResources();
            if (generatedResourcesConfiguration != null) {
                generatedResourceBaseDir =
                        generatedResourcesConfiguration.getBaseDir();
            }
            builder.setGeneratedResourceBaseDir(generatedResourceBaseDir);

            // Initialise the asset configuration.
            builder.setAssetsConfiguration(configuration.getAssets());

            // Set the cache control constraints map.
            setCacheControlConstraintsMap(policySource, builder);
        }

        private RuntimeProject getFallbackProject(
                RuntimeProjectConfiguration configuration, List dependencyPath,
                final Map namedProjects)
                throws ConfigurationException {


            RuntimeProject fallbackProject = null;
            String fallbackProjectName = configuration.getFallbackProjectName();
            if (fallbackProjectName != null) {
                RuntimeProjectConfiguration fallbackConfiguration =
                        (RuntimeProjectConfiguration) projectConfigurations.get(fallbackProjectName);
                if (fallbackConfiguration == null) {
                    throw new IllegalStateException("Named configuration '" +
                            configuration.getName() +
                            "' extends unknown configuration '" +
                            fallbackProjectName + "'");
                }

                fallbackProject =
                        createProject(fallbackConfiguration, dependencyPath, namedProjects);
            }

            return fallbackProject;
        }

        public PredefinedProjects createPredefinedProjects()
                throws ConfigurationException {

            Map namedProjects = new HashMap();

            for (Iterator i = projectConfigurations.values().iterator();
                 i.hasNext();) {
                RuntimeProjectConfiguration configuration =
                        (RuntimeProjectConfiguration) i.next();

                List dependencyPath = Collections.EMPTY_LIST;
                createProject(configuration, dependencyPath, namedProjects);
            }

            // Create the default project after the named ones as it may
            // fallback to one of them.
            RuntimeProject defaultProject = createDefaultProject(
                    defaultProjectConfiguration, namedProjects);

            return new PredefinedProjects(defaultProject, namedProjects);
        }
    }
}
