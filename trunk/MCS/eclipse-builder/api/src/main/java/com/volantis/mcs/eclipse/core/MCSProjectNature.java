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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.eclipse.core;

import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.eclipse.common.EclipseConstants;
import com.volantis.mcs.eclipse.validation.DeviceRepositoryFileValidator;
import com.volantis.mcs.eclipse.validation.ValidationMessageBuilder;
import com.volantis.mcs.eclipse.validation.ValidationStatus;
import com.volantis.mcs.eclipse.validation.ValidationUtils;
import com.volantis.mcs.repository.RepositoryException;
import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;

import java.io.File;


/**
 * A nature that defines an MCS project.
 */
public class MCSProjectNature implements IProjectNature {

    /**
     * The QUALIFYING_ID is used for specifying the qualified name.
     * NB: This field was Formerly known as PACKAGE_ID as it stored the path of this class.
     * However, it can no longer be the actual path of this class as the class has been
     * moved to com.volantis.mcs.eclipse.ab.core and changing the value of this field
     * would break backward compatibility for the product
     */
    private static final String QUALIFYING_ID =
            "com.volantis.mcs.eclipse.common"; //$NON-NLS-1$

    /**
     * Store the value of the path object retrieved from the persistent session.
     */
    private IPath policySrcPath = null;

    /**
     * The repository name validator for this class.
     */
    private static final DeviceRepositoryFileValidator
            REPOSITORY_NAME_VALIDATOR =
            new DeviceRepositoryFileValidator();

    /**
     * Store the validation message builder.
     */
    private static final ValidationMessageBuilder messageBuilder =
            new ValidationMessageBuilder(null, null, null);

    /**
     * The qualified name used for storing the device repository name in the
     * session.
     */
    private static final QualifiedName QNAME_DEVICE_REPOSITORY =
            new QualifiedName(QUALIFYING_ID, "DeviceRepositoryName");

    /**
     * The qualified name used to access the web contents name.
     */
    private static final QualifiedName QNAME_POLICY_SRC =
            new QualifiedName(QUALIFYING_ID, "PolicySrc");

    /**
     * The qualified name used to access the collaborative working property.
     */
    private static final QualifiedName QNAME_COLLABORATIVE_WORKING =
            new QualifiedName(QUALIFYING_ID, "CollaborativeWorking");

    /**
     * The qualified name used to access the database name.
     */
    private static final QualifiedName QNAME_DATABASE_NAME =
            new QualifiedName(QUALIFYING_ID, "DatabaseName");

    /**
     * The qualified name used to access the database project name.
     */
    private static final QualifiedName QNAME_DATABASE_PROJECT_NAME =
            new QualifiedName(QUALIFYING_ID, "DatabaseProjectName");

    /**
     * The qualified name used to access the host name.
     */
    private static final QualifiedName QNAME_HOST_NAME =
            new QualifiedName(QUALIFYING_ID, "HostName");

    /**
     * The qualified name used to access the port number.
     */
    private static final QualifiedName QNAME_PORT =
            new QualifiedName(QUALIFYING_ID, "Port");

    /**
     * The qualified name used to access the database user name.
     */
    private static final QualifiedName QNAME_USER_NAME =
            new QualifiedName(QUALIFYING_ID, "UserName");

    /**
     * The qualified name used to access the database password.
     */
    private static final QualifiedName QNAME_PASSWORD =
            new QualifiedName(QUALIFYING_ID, "Password");

    /**
     * The qualified name used to access the display name.
     */
    private static final QualifiedName QNAME_DISPLAY_NAME =
            new QualifiedName(QUALIFYING_ID, "DisplayName");

    /**
     * The default policy source name to use (if none is found in the persistent
     * session).
     */
    public static final IPath DEFAULT_POLICY_SRC_PATH =
            new Path("WebContent/mcs-policies");

    /**
     * The project to which this nature applies
     */
    private IProject iProject;

    /**
     * The builder id is a concatenation of the plugin id and the id used in the
     * builder extension entry.
     */
    private static final String BUILDER_ID =
            "com.volantis.mcs.eclipse.ab.policyBuilder"; //$NON-NLS-1$


    /**
     * The ID for this project nature.
     * NOTE: This constant must be kept in-sync with the nature id in the
     * plugin.xml file.
     */
    public final static String NATURE_ID =
            "com.volantis.mcs.eclipse.ab.MCSProjectNature";

    /**
     * Configures this nature for its project. This method associates a
     * builder with this MCSProjectNature.
     *
     * @throws org.eclipse.core.runtime.CoreException  Never thrown at present
     */
    public void configure() throws CoreException {
        IProjectDescription desc = getProject().getDescription();
        ICommand[] commands = desc.getBuildSpec();
        boolean found = false;
        for (int i = 0; !found && (i < commands.length); i++) {
            if (commands[i].getBuilderName().equals(BUILDER_ID)) {
                found = true;
            }
        }
        if (!found) {
            ICommand command = desc.newCommand();
            command.setBuilderName(BUILDER_ID);
            ICommand[] newCommands = new ICommand[commands.length + 1];

            // Add it before other builders.
            System.arraycopy(commands, 0, newCommands, 1, commands.length);
            newCommands[0] = command;
            desc.setBuildSpec(newCommands);
            getProject().setDescription(desc, null);
        }
    }

    /**
     * Deconfigures this nature for its project. This method currently does
     * nothing but must be implemented to satisfy the interface.
     *
     * @throws org.eclipse.core.runtime.CoreException  Never thrown at present
     */
    public void deconfigure() throws CoreException {
    }

    /**
     * Returns the project to which this nature applies
     *
     * @return the IProject to which this nature applies
     */
    public IProject getProject() {
        return iProject;
    }

    /**
     * Sets the project to which this nature applies. Used when instantiating
     * this project nature runtime. This is called by IProject.create() or
     * IProject.setDescription() and should not be called directly by clients.
     *
     * @param project the IProject to which this nature is being applied
     */
    public void setProject(IProject project) {
        iProject = project;
    }

    /**
     * Get the policy source folder stored in the session. If none is found in
     * the session, store the default value and return that value.
     *
     * @return the policy source name stored in the session, or the default
     *         value if no value was found in the session.
     */
    public IPath getPolicySourcePath() {
        if (policySrcPath == null) {
            String path = getPolicySourcePath(getProject());
            if (path != null) {
                policySrcPath = new Path(path);
            } else {

                throw new IllegalStateException("Could not find policy " +
                        "source path.");
            }
        }
        return policySrcPath;
    }

    /**
     * Get the MCS project nature from teh project parameter.
     *
     * @param project the project parameter (assumed to be an MCS project).
     * Should not be null.
     * @return the MCS project nature from teh project parameter.
     * @throws java.lang.IllegalStateException if project is null or project does not
     * have an MCSNature.
     */
    public static MCSProjectNature getMCSProjectNature(IProject project) {
        MCSProjectNature nature = null;
        try {
            if (project == null) {
                throw new IllegalStateException("Tried to get the nature of " +
                        "a null project.");
            }
            nature = ((MCSProjectNature) project.getNature(EclipseConstants.NATURE_ID));
            if (nature == null) {
                throw new IllegalStateException(
                        "Could not find MCS project nature for nature ID: " +
                        EclipseConstants.NATURE_ID);
            }
        } catch (CoreException e) {
            EclipseCommonPlugin.handleError(e);
        }
        return nature;
    }

    /**
     * Determines if there is a valid device repository associated with this
     * project. Checks the validity of both the deviceRepositoryName and the
     * policy source path.
     * @return true if the repository is valid or false otherwise
     */
    public boolean hasValidDeviceRepository() {
        boolean result = false;
        // check the device repository name
        String deviceRepositoryName = getDeviceRepositoryName(iProject);
        if (deviceRepositoryName != null) {
            ValidationStatus status = REPOSITORY_NAME_VALIDATOR.validate(
                    deviceRepositoryName, messageBuilder);
            if (status.getSeverity() == ValidationStatus.OK) {
                // check the policy source path
                StringBuffer fullPolicyPathName = new StringBuffer(
                        getProject().getLocation().toOSString());
                fullPolicyPathName.append(File.separatorChar).
                        append(getPolicySourcePath());
                if (ValidationUtils.checkFile(fullPolicyPathName.toString(),
                        ValidationUtils.FILE_EXISTS |
                        ValidationUtils.FILE_CAN_READ).getSeverity() ==
                        ValidationStatus.OK) {
                    result = true;
                }
            }
        }
        return result;
    }

    /**
     * Returns the name of the device repository used by a project or null if
     * a repository is not assigned.
     * @param project the MCS project
     * @return the name of the device repository used by the project or null
     */
    public static String getDeviceRepositoryName(IProject project) {
        String deviceRepositoryName = null;
        try {
            deviceRepositoryName = project.getPersistentProperty(
                    QNAME_DEVICE_REPOSITORY);
        } catch (ResourceException e){        	
        	//although ResourceException overrides CoreException it does not have to be handled
        	//in this situation no device repository can be resolved, 
        	//do nothing, just return null value
        	return deviceRepositoryName;
        } catch (CoreException e) {
            EclipseCommonPlugin.handleError(e);
        }
        return deviceRepositoryName;
    }

    /**
     * Sets the name of the device repository used by a project .
     * @param project the MCS project
     * @param repositoryFileName the full path of the repository file name
     */
    public static void setDeviceRepositoryName(IProject project,
                                               String repositoryFileName) {
        try {

            String currentValue = getDeviceRepositoryName(project);
            if (currentValue == null || 
                !currentValue.equals(repositoryFileName)) {
                project.setPersistentProperty(
                        MCSProjectNature.QNAME_DEVICE_REPOSITORY,
                        repositoryFileName);

                if (repositoryFileName != null) {
                    // Inform the ProjectDeviceRepositoryProvider that the
                    // project device repository has changed.
                    ProjectDeviceRepositoryProvider.getSingleton().
                            updateProjectDeviceRepository(project,
                                    repositoryFileName);
                }
            }
        } catch (CoreException e) {
            EclipseCommonPlugin.handleError(e);
        } catch (RepositoryException e) {
            EclipseCommonPlugin.handleError(e);
        }
    }

    /**
     * Returns the path for the policy source of an MCS project.
     * @param project the MCS project
     * @return the policy source path or null if there was no policy source
     * path
     */
    public static String getPolicySourcePath(IProject project) {
        String pathName = null;
        try {
            pathName = project.getPersistentProperty(
                    QNAME_POLICY_SRC);
        } catch (CoreException e) {
            EclipseCommonPlugin.handleError(e);
        }
        return pathName;
    }

    /**
     * Sets the policy source path used by a an MCS project.
     * @param project the MCS project
     * @param policySourcePath the policy source path to associate with the
     * project
     */
    public static void setPolicySourcePath(IProject project,
                                           String policySourcePath) {
        try {

            String currentValue = getPolicySourcePath(project);
            if (currentValue == null ||
                    !currentValue.equals(policySourcePath)) {
                project.setPersistentProperty(
                        MCSProjectNature.QNAME_POLICY_SRC,
                        policySourcePath);
            }
        } catch (CoreException e) {
            EclipseCommonPlugin.handleError(e);
        }
    }

    /**
     * Returns the true iff collaborative working is enabled.
     * @param project the MCS project
     * @return true iff collaborative working is enabled
     */
    public static boolean getCollaborativeWorking(final IProject project) {

        final String value =
            getPropertyValue(project, QNAME_COLLABORATIVE_WORKING);
        return Boolean.valueOf(value).booleanValue();
    }

    /**
     * Enables/disables the collaborative working.
     * @param project the MCS project
     * @param newValue the new value of collaborative working
     */
    public static void setCollaborativeWorking(
            final IProject project, final boolean newValue) {
        setPropertyValue(project, QNAME_COLLABORATIVE_WORKING,
            Boolean.toString(newValue));
    }

    /**
     * Returns the database name for collaborative working.
     * @param project the MCS project
     * @return the database name
     */
    public static String getDatabaseName(final IProject project) {
        return getPropertyValue(project, QNAME_DATABASE_NAME);
    }

    /**
     * Sets the database name for collaborative working.
     * @param project the MCS project
     * @param newValue the new database name
     */
    public static void setDatabaseName(final IProject project,
                                       final String newValue) {
        setPropertyValue(project, MCSProjectNature.QNAME_DATABASE_NAME, newValue);
    }

    /**
     * Returns the database project name for collaborative working.
     * @param project the MCS project
     * @return the database project name
     */
    public static String getDatabaseProjectName(final IProject project) {
        return getPropertyValue(project, QNAME_DATABASE_PROJECT_NAME);
    }

    /**
     * Sets the database project name for collaborative working.
     * @param project the MCS project
     * @param newValue the new database project name
     */
    public static void setDatabaseProjectName(final IProject project,
                                              final String newValue) {
        setPropertyValue(project, QNAME_DATABASE_PROJECT_NAME, newValue);
    }

    /**
     * Returns the host name for collaborative working.
     * @param project the MCS project
     * @return the host name
     */
    public static String getHostName(final IProject project) {
        return getPropertyValue(project, QNAME_HOST_NAME);
    }

    /**
     * Sets the host name for collaborative working.
     * @param project the MCS project
     * @param newValue the new host name
     */
    public static void setHostName(final IProject project,
                                   final String newValue) {
        setPropertyValue(project, QNAME_HOST_NAME, newValue);
    }

    /**
     * Returns the port number for collaborative working.
     * @param project the MCS project
     * @return the port number
     */
    public static String getPort(final IProject project) {
        return getPropertyValue(project, QNAME_PORT);
    }

    /**
     * Sets the port number for collaborative working.
     * @param project the MCS project
     * @param newValue the new port number
     */
    public static void setPort(final IProject project, final String newValue) {
        setPropertyValue(project, QNAME_PORT, newValue);
    }

    /**
     * Returns the user name to be used for collaborative working.
     * @param project the MCS project
     * @return the user name
     */
    public static String getUserName(final IProject project) {
        return getPropertyValue(project, QNAME_USER_NAME);
    }

    /**
     * Sets the user name for collaborative working.
     * @param project the MCS project
     * @param newValue the new user name
     */
    public static void setUserName(final IProject project,
                                   final String newValue) {
        setPropertyValue(project, QNAME_USER_NAME, newValue);
    }

    /**
     * Returns the password to be used for collaborative working.
     * @param project the MCS project
     * @return the password
     */
    public static String getPassword(final IProject project) {
        return getPropertyValue(project, QNAME_PASSWORD);
    }

    /**
     * Sets the password for collaborative working.
     * @param project the MCS project
     * @param newValue the new passworf
     */
    public static void setPassword(final IProject project,
                                   final String newValue) {
        setPropertyValue(project, QNAME_PASSWORD, newValue);
    }

    /**
     * Returns the display name for collaborative working.
     * @param project the MCS project
     * @return the display name
     */
    public static String getDisplayName(final IProject project) {
        return getPropertyValue(project, QNAME_DISPLAY_NAME);
    }

    /**
     * Sets the display name for collaborative working.
     * @param project the MCS project
     * @param newValue the new display name
     */
    public static void setDisplayName(final IProject project,
                                      final String newValue) {
        setPropertyValue(project, QNAME_DISPLAY_NAME, newValue);
    }

    /**
     * Sets the specified property for the given project.
     *
     * @param project the receiver of the new property value
     * @param propertyName the name of the property to change
     * @param newValue the new value
     */
    private static void setPropertyValue(final IProject project,
                                         final QualifiedName propertyName,
                                         final String newValue) {
        try {
            final String currentValue = getPropertyValue(project, propertyName);
            if (currentValue == null && newValue != null ||
                currentValue != null && !currentValue.equals(newValue)) {

                project.setPersistentProperty(propertyName, newValue);
            }
        } catch (CoreException e) {
            EclipseCommonPlugin.handleError(e);
        }
    }

    /**
     * Returns the value of the specified property.
     *
     * Never returns null, returns empty string instead.
     *
     * @param project the project to query
     * @param propertyName the name of the property
     * @return the value of the property
     */
    private static String getPropertyValue(
            final IProject project, final QualifiedName propertyName) {

        String value = null;
        if (project.exists()) {
        try {
            value = project.getPersistentProperty(propertyName);
        } catch (CoreException e) {
            EclipseCommonPlugin.handleError(e);
            }
        }
        return value == null? "": value;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Dec-05	10539/3	adrianj	VBM:2005111712 fixed up merge conflicts

 01-Dec-05	10520/4	adrianj	VBM:2005111712 Implement New... button for device themes

 17-Jan-05	6681/6	allan	VBM:2004081607 Rework issue

 14-Jan-05	6681/4	allan	VBM:2004081607 Rework issues

 14-Jan-05	6681/1	allan	VBM:2004081607 Allow device selectors and browser to see project device repository changes

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-May-04	4231/6	tom	VBM:2004042704 rework for 2004042704

 17-May-04	4231/4	tom	VBM:2004042704 Fixedup the 2004032606 change

 13-Feb-04	2985/2	allan	VBM:2004012803 Allow policies to be created in non-MCS projects.

 03-Feb-04	2820/2	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 06-Jan-04	2414/1	byron	VBM:2003121803 Misleading package id in MCSProjectNature

 19-Dec-03	2267/1	byron	VBM:2003121804 QNAME for device repository different

 19-Dec-03	2237/4	byron	VBM:2003112804 Provide an MCS project builder

 16-Nov-03	1909/2	allan	VBM:2003102405 Done Image Component Wizard.

 31-Oct-03	1642/7	byron	VBM:2003102409 Add support for webcontent directory - removed setting of persistent value

 31-Oct-03	1642/5	byron	VBM:2003102409 Add support for webcontent directory - addressed some rework issues

 31-Oct-03	1642/3	byron	VBM:2003102409 Add support for webcontent directory

 30-Oct-03	1639/3	byron	VBM:2003101602 Create a MCS Project properties page - addressed various review issues

 30-Oct-03	1639/1	byron	VBM:2003101602 Create a MCS Project properties page

 09-Oct-03	1515/3	steve	VBM:2003100704 Got rid of constructor from Nature class, corrected javadoc and corrected plugin text declaration names

 09-Oct-03	1515/1	steve	VBM:2003100704 Create MCS project Nature

 ===========================================================================
*/
