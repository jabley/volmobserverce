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
package com.volantis.mcs.policies.impl.xml;

import com.volantis.mcs.accessors.CollectionRepositoryEnumeration;
import com.volantis.mcs.accessors.PolicyBuilderAccessor;
import com.volantis.mcs.accessors.common.ComponentWriter;
import com.volantis.mcs.accessors.xml.jdom.XMLAccessorHelper;
import com.volantis.mcs.accessors.xml.jibx.JiBXReader;
import com.volantis.mcs.accessors.xml.jibx.JiBXWriter;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.policies.InternalPolicyFactory;
import com.volantis.mcs.policies.PolicyBuilder;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.project.InternalProject;
import com.volantis.mcs.project.Project;
import com.volantis.mcs.project.xml.XMLPolicySource;
import com.volantis.mcs.repository.RepositoryConnection;
import com.volantis.mcs.repository.RepositoryEnumeration;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.repository.xml.XMLRepositoryException;
import com.volantis.shared.content.BinaryContentInput;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.net.URLDecoder;

/**
 * Parent class for all XML accessors.
 *
 * <p>Note: The connection must never be used as it will normally be null.</p>
 */
public class XMLPolicyBuilderAccessor
        implements PolicyBuilderAccessor {

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(
                    XMLPolicyBuilderAccessor.class);

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
            LocalizationFactory.createLogger(XMLPolicyBuilderAccessor.class);

    // Javadoc inherited from super class.
    public void addPolicyBuilder(
            RepositoryConnection connection, Project project,
            PolicyBuilder builder) throws RepositoryException {

        marshall(project, builder, false);
    }

    public boolean updatePolicyBuilder(
            RepositoryConnection connection, Project project,
            PolicyBuilder builder) throws RepositoryException {

        // update object is the same as an add object
        return marshall(project, builder, true);
    }

    // Javadoc inherited from super class.
    public boolean removePolicyBuilder(
            final RepositoryConnection connection,
            Project project, final String name)
        throws RepositoryException {

        checkName(name);

        final File directory = getProjectDirectory(project);

        File file = new File(directory, name);
        return XMLAccessorHelper.deletePolicyFile(file);
    }

    public PolicyBuilder retrievePolicyBuilder(
            RepositoryConnection connection, Project project, String name)
            throws RepositoryException {

        return unmarshall(project, name, false);
    }

    // Javadoc inherited from super class.
    public RepositoryEnumeration enumeratePolicyBuilderNames(
            RepositoryConnection connection, Project project,
            PolicyType policyType) throws RepositoryException {

        InternalProject internalProject = (InternalProject) project;
        File directory = getProjectDirectory(internalProject);

        Collection collection = XMLAccessorHelper.getRepositoryPolicyNames(
                directory, policyType);

        return new CollectionRepositoryEnumeration(collection);
    }

    /**
     * Marshalls the specified repository policy builder.
     *
     * @param project
     * @param policyBuilder the repository policyBuilder to marshall
     * @param overwrite
     * @throws RepositoryException if the marshalling of the repository
     *                             policy builder failed
     */
    private boolean marshall(
            Project project, final PolicyBuilder policyBuilder,
            boolean overwrite)
            throws RepositoryException {

        final File projectDirectory = getProjectDirectory(project);
        final String name = policyBuilder.getName();

        final ComponentWriter componentWriter = new JiBXWriter();

        final File file = new File(projectDirectory, name);

        final File directory = file.getParentFile();

        boolean exists = file.exists();
        if (exists && !overwrite) {
            throw new RepositoryException(EXCEPTION_LOCALIZER.format(
                    "policy-exists", projectDirectory.getAbsolutePath()));
        }

        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new RepositoryException(EXCEPTION_LOCALIZER.format(
                        "cannot-create-dir", directory.getAbsolutePath()));
            }
        }

        try {
            final FileWriter fileWriter = new FileWriter(file);
            componentWriter.write(fileWriter, policyBuilder);
        } catch (IOException e) {
            throw new XMLRepositoryException(EXCEPTION_LOCALIZER.format(
                    "unable-to-open-output-file", file), e);
        }
        return exists;
    }

    /**
     * Unmarshalls the repository object described by the specified identity
     * using the given XML connection.
     *
     * @param project
     * @param name     the name of the repository object
     * @param required true if the repository object is expected to
     *                 exist
     * @return the unmarshalled repository object
     * @throws RepositoryException if the policy is required, but cannot be
     *                             found
     */
    private PolicyBuilder unmarshall(
            Project project, final String name,
            final boolean required) throws RepositoryException {

        final File directory = getProjectDirectory(project);
        final File file = new File(directory, name);

        if (!required && !file.exists()) {
            return null;
        }

        final InputStream inputStream;
        try {
            inputStream = new FileInputStream(file);
        } catch (IOException e) {
            throw new XMLRepositoryException(EXCEPTION_LOCALIZER.format(
                    "unable-to-open-input-file", file.getAbsolutePath()), e);
        }

        BinaryContentInput content = new BinaryContentInput(inputStream);
        InternalPolicyFactory factory =
                InternalPolicyFactory.getInternalInstance();
        // todo: re-enable validation by avoiding this overload once we have
        // sorted out validation.
        final JiBXReader policyReader =
                factory.createDangerousNonValidatingPolicyReader();
        PolicyBuilder policyBuilder;
        try {
            policyBuilder = (PolicyBuilder) policyReader.read(
                            content, file.getPath());
        } catch (IOException e) {
            throw new XMLRepositoryException(EXCEPTION_LOCALIZER.format(
                    "io-exception", e), e);
        }
        policyBuilder.setName(name);
        return policyBuilder;
    }

    /**
     * Check that the name value is valid.
     *
     * @return true if this name is valid XDIME
     */
    private boolean checkName(final String value)
        throws RepositoryException {

        // Make sure that the property has been set
        if (value == null) {
            throw new RepositoryException(
                    EXCEPTION_LOCALIZER.format(
                            "missing-mandatory-attribute", "name"));
        }

        if (value.length() > 254) {
            throw new RepositoryException(
                    EXCEPTION_LOCALIZER.format("max-length-exceeded",
                            new Object[]{"name", Integer.toString(254)}));
        }

        if (!value.startsWith("/")) {
            LOGGER.warn("invalid-xdime-name", new Object[]{value});
            return false;
        }

        return true;
    }

    // Javadoc inherited from super class.
    public void renamePolicyBuilder(
            final RepositoryConnection connection,
            Project project, final String name,
            final String newName)
        throws RepositoryException {

        final File directory = getProjectDirectory(project);

        final File oldXmlFile = new File(directory, name);
        final File newXmlFile = new File(directory, newName);

        // todo: check return
        oldXmlFile.renameTo(newXmlFile);
    }

    /**
     * Return the project directory associated with the identity provided.
     *
     * @param project
     * @return the project directory.
     */
    private File getProjectDirectory(Project project) {
        XMLPolicySource policySource = (XMLPolicySource)
                ((InternalProject) project).getPolicySource();
        String url = policySource.getDirectory();
        try {

            String decoded = URLDecoder.decode(url, "UTF-8");
            return new File(decoded);
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("url-decoding-error", new String[] {url, "UTF-8"}, e);
            throw new IllegalArgumentException(EXCEPTION_LOCALIZER.format(
                    "url-decoding-error",
                    new String[] {url, "UTF-8"}));
        }

    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Nov-05	9789/9	emma	VBM:2005101113 Supermerge: Refactor JDBC Accessors to use chunked accessor

 03-Nov-05	9789/4	emma	VBM:2005101113 Supermerge: Migrate JDBC Accessors to use chunked accessors

 23-Oct-05	9789/1	emma	VBM:2005101113 Migrate JDBC Accessors to chunked accessors

 16-Nov-05	9896/1	geoff	VBM:2005101906 Avoid using JDOM in MCS at runtime: rewrite runtime XML device repository

 14-Nov-05	10287/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 11-Nov-05	10199/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 27-Oct-05	10007/1	geoff	VBM:2005102512 MCS35: Investigate and fix any JDBC repository import/export problems

 27-Oct-05	9986/1	geoff	VBM:2005102512 MCS35: Investigate and fix any JDBC repository import/export problems

 09-Jun-05	8552/1	pabbott	VBM:2005051902 Version 1 of JIBX implementation

 ===========================================================================
*/
