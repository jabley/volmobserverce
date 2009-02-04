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

package com.volantis.mcs.eclipse.ab.core;

import com.volantis.devrep.repository.accessors.DefaultNamespaceAdapterFilter;
import com.volantis.mcs.accessors.xml.jibx.JiBXReader;
import com.volantis.mcs.eclipse.ab.ABPlugin;
import com.volantis.mcs.eclipse.ab.editors.dom.LPDMJDOMFactory;
import com.volantis.mcs.eclipse.ab.editors.dom.validation.DuplicateNameValidator;
import com.volantis.mcs.eclipse.ab.editors.dom.validation.LayoutConstraintsValidator;
import com.volantis.mcs.eclipse.ab.editors.dom.validation.MarkerGeneratingErrorReporter;
import com.volantis.mcs.eclipse.ab.editors.dom.validation.UniqueAssetValidator;
import com.volantis.mcs.eclipse.builder.common.ResourceDiagnosticsAdapter;
import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.eclipse.common.RepositorySchemaResolverFactory;
import com.volantis.mcs.eclipse.common.odom.MCSNamespace;
import com.volantis.mcs.eclipse.common.odom.input.VolantisSAXBuilder;
import com.volantis.mcs.eclipse.controls.ControlsPlugin;
import com.volantis.mcs.eclipse.core.MCSProjectNature;
import com.volantis.mcs.eclipse.validation.PolicyNameValidator;
import com.volantis.mcs.eclipse.validation.ValidationMessageBuilder;
import com.volantis.mcs.eclipse.validation.ValidationStatus;
import com.volantis.mcs.interaction.InteractionFactory;
import com.volantis.mcs.interaction.InteractionModel;
import com.volantis.mcs.interaction.Proxy;
import com.volantis.mcs.layouts.LayoutSchemaType;
import com.volantis.mcs.objects.FileExtension;
import com.volantis.mcs.policies.InternalPolicyFactory;
import com.volantis.mcs.policies.PolicyModel;
import com.volantis.mcs.utilities.FaultTypes;
import com.volantis.mcs.xml.validation.ErrorReporter;
import com.volantis.mcs.xml.validation.sax.ParserErrorException;
import com.volantis.mcs.xml.validation.sax.xerces.XercesBasedDOMValidator;
import com.volantis.shared.content.BinaryContentInput;
import com.volantis.xml.schema.JarFileEntityResolver;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * The MCSProject Builder which is responsible for validating MCS resources
 * whenever a build/refresh request has been made (via rebuild/rebuild all and
 * refresh).
 * <p/>
 * An IncrementalProjectBuilder.FULL_BUILD shuold find all the policies in the
 * project and validate each one. There are two aspects to this validation:
 * <ol><li>
 * Reading in the policy as an ODOM document and validating this using the
 * ODOM validator.
 * </li>
 * <li>
 * Ensuring that the name of each validated policy is valid (this validation
 * is outside the scope of the ODOM validator).
 * </li></ol>
 * <p/>
 * The only difference between FULL_BUILD validation and other kinds of
 * validation is that non-FULL_BUILD validation should only validate the
 * resource deltas - i.e. the changed resources. ResourceDeltas are provided to
 * the builder.
 * <p/>
 * The builder will need to provide an MarkerGeneratingErrorReporter
 * that has the creates problem IMarkers on any resources that are invalid with
 * an IMarker for each problem that includes the validation message associated
 * with that problem. (Note that the builder should clear all the problem
 * markers on a resource prior to validation.)
 * <p/>
 * An appropriate problem marker should also be created on the resource if the
 * resource name is invalid (use a PolicyNameValidator).
 * <p/>
 * <strong>NOTE</strong>: The {@link #build} method returns null because MCS
 * projects do not currently depend on other MCS projects.
 */
public class MCSProjectBuilder extends IncrementalProjectBuilder {

    /**
     * Message key mappings.
     */
    private static Map MESSAGE_KEY_MAPPINGS = new HashMap();

    static {
        MESSAGE_KEY_MAPPINGS.put(FaultTypes.INVALID_CHARACTER,
                "MCSProjectBuilder.invalidNameCharacter");
        MESSAGE_KEY_MAPPINGS.put(FaultTypes.INVALID_FIRST_CHARACTER,
                "MCSProjectBuilder.invalidFirstCharacter");
        MESSAGE_KEY_MAPPINGS.put(FaultTypes.TOO_MANY_CHARACTERS,
                "MCSProjectBuilder.nameTooLong");
    }

    /**
     * Resource bundle.
     */
    private static final ResourceBundle BUNDLE =
            CoreMessages.getResourceBundle();

    /**
     * The set of file extensions representing resources using the new model.
     */
    private static final FileExtension[] NEW_MODEL_EXTENSIONS = {
        FileExtension.ASSET_GROUP,
        FileExtension.AUDIO_COMPONENT,
        FileExtension.CHART_COMPONENT,
        FileExtension.DYNVIS_COMPONENT,
        FileExtension.IMAGE_COMPONENT,
        FileExtension.LINK_COMPONENT,
        FileExtension.ROLLOVER_IMAGE_COMPONENT,
        FileExtension.SCRIPT_COMPONENT,
        FileExtension.TEXT_COMPONENT,
        FileExtension.THEME,
        FileExtension.LAYOUT,
    };

    // javadoc inherited.
    protected IProject[] build(int kind, Map args, IProgressMonitor monitor)
            throws CoreException {

        if (kind == IncrementalProjectBuilder.FULL_BUILD) {
            // Full build. Use a ResourceVisitor to process the project resource
            // tree.
            performBuild();
        } else {
            // Build with a delta (Auto/Incremental).
            IResourceDelta delta = getDelta(getProject());
            if (delta == null) {
                performBuild();
            } else {
                performBuild(delta);
            }
        }
        return null;
    }

    /**
     * Perform the delta build.
     *
     * @param delta the <code>IResourceDelta</code> object used to perform a
     *              delta build.
     * @throws CoreException if a CoreException occured.
     */
    private void performBuild(IResourceDelta delta) throws CoreException {
        delta.accept(new PolicyBuildDeltaVisitor());
    }

    /**
     * Perform the full build.
     *
     * @throws CoreException if a CoreException occured.
     */
    private void performBuild() throws CoreException {
        getProject().accept(new PolicyBuildVisitor());
    }

    /**
     * Do the actual validation on the resource (only if the resource is a
     * recognized policy resource and it is in the policy source directory
     * hierarchy).
     *
     * @param resource the resource to validate against (folders, projects and
     *                 unrecognized resources are simply ignored).
     */
    private void doValidation(IResource resource) {
        final int type = resource.getType();
        if (type == IResource.FILE) {
            // First check that the resource is within the policy source
            // hierarchy.
            if (inPolicySource(resource)) {
                FileExtension fileExtension =
                        FileExtension.getFileExtensionForExtension(
                                resource.getFileExtension());

                if (fileExtension != null &&
                        fileExtension.isPolicyFileExtension()) {
                    // Always clear the markers for this resource.
                    clearMarkers(resource);

                    // This file is a recognized file type which may now
                    // be validated.
                    validateResource(resource);
                }
            }
        }
    }

    /**
     * Determine whether or not a given Resource is located within the
     * policy source hierarchy. The policy source is retrieved via the
     * MCS project nature associated with the project that is being built by
     * this builder.
     * @param resource The IResource.
     */
    private boolean inPolicySource(IResource resource) {
        boolean result = false;
        try {
            MCSProjectNature mcsNature = (MCSProjectNature)
                    getProject().getNature(MCSProjectNature.NATURE_ID);
            IPath policySource = mcsNature.getPolicySourcePath();
            IPath resourcePath = resource.getFullPath();

            // Prepend the policySource with the path of the project so that
            // we get the right device and prefix that we expect the
            // resourcePath to have.
            IPath projectPath = mcsNature.getProject().getFullPath();
            IPath fullPolicySource = projectPath.append(policySource);
            result = fullPolicySource.isPrefixOf(resourcePath);
        } catch (CoreException e) {
            EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
        } catch (IllegalStateException e) {
            // When a project is being created, eclipse (v3.1 on) sometimes
            // kicks off a build before the project is fully configured. This
            // results in MCSProjectNature#getPolicySourcePath() throwing an
            // IllegalStateException. Subsequent builds should succeed (the
            // data will be populated by then) so we just cancel this build.
            // See vbm 2006042816 in mantis for more details.
            throw new OperationCanceledException(e.toString());
        }

        return result;
    }

    /**
     * Clear the markers for this resource only if the resource exists (it may
     * have been renamed and deleting the markers on a resource that no longer
     * exists throws an exception).
     *
     * @param resource the resource to clear the markers from.
     */
    private void clearMarkers(IResource resource) {
        try {
            if (resource.exists()) {
                resource.deleteMarkers(IMarker.PROBLEM,
                        true,
                        IResource.DEPTH_INFINITE);
            }
        } catch (CoreException e) {
            e.printStackTrace();
            EclipseCommonPlugin.handleError(
                    ControlsPlugin.getDefault(), e);
        }
    }

    /**
     * Helper method to obtain the root element from the resource.
     *
     * @param resource the resource used to obtain the root element from.
     * @return the root element or null if it could not be found.
     * @throws JDOMException if the builder encountered a JDOMException.
     * @throws IOException   if the builder encountered an IOException.
     */
    private Element obtainRootElement(IResource resource)
            throws JDOMException, IOException {

        final File file = resource.getLocation().toFile();
        Element result = null;
        if (file.exists()) {
            // Create a non-validating builder that uses the Volantisized
            // Xerces.
            // Note that this builder is JRE 1.4 and Eclipse friendly
            SAXBuilder builder = new VolantisSAXBuilder(false);

            // We require default namespace declarations to be replaced
            // with a prefix binding, the DefaultNamespaceAdapterFilter will
            // do this for us.
            builder.setXMLFilter(new DefaultNamespaceAdapterFilter(
                    MCSNamespace.LPDM.getPrefix()));
            builder.setFactory(new LPDMJDOMFactory());

            Document document = builder.build(file);
            result = document.getRootElement();
        }
        return result;
    }

    /**
     * Validate the resource.
     *
     * @param resource the resource to validate.
     */
    private void validateResource(IResource resource) {

        // Validate the policy name.
        if (validatePolicyName(resource)) {
            if (isNewModel(resource)) {
                repositoryModelValidate(resource);
            } else {
                Exception exception = null;
                try {

                    final Element node = obtainRootElement(resource);
                    if (node != null) {
                        // Create a builder and error reporter to validate the
                        // policy itself.
                        final MarkerGeneratingErrorReporter errorReporter =
                                new MarkerGeneratingErrorReporter(resource,
                                        node, null);
                        // create a validator
                        XercesBasedDOMValidator validator =
                                createDOMValidator(errorReporter);

                        // Add a UniqueAssetValidator to each of the dependent
                        // elements (if any) using the validator for this resource
                        UniqueAssetValidator.addValidatorToProvider(
                                node, validator);

                        // Add the layout constraints supplementary validator.
                        validator.addSupplementaryValidator(
                                MCSNamespace.LPDM.getURI(),
                                LayoutSchemaType.LAYOUT.getName(),
                                new LayoutConstraintsValidator());

                        // Add the layout duplicate name supplementary validator.
                        validator.addSupplementaryValidator(
                                MCSNamespace.LPDM.getURI(),
                                LayoutSchemaType.LAYOUT.getName(),
                                new DuplicateNameValidator());

                        // Validate the policy using the root element.
                        validator.validate(node);
                    }
                } catch (JDOMException e) {
                    // this exception is likely to occur if the DOM is badly
                    // formed. The user may be able rectify this by editing the
                    // file directly so we should add a marker that highlights
                    // what has gone wrong.
                    addMarkerForException(resource, e);
                } catch (IOException e) {
                    exception = e;
                } catch (SAXException e) {
                    // Again this exception is likely to occur if the XML is badly
                    // formed. Add a marker that highlights what has gone wrong.
                    addMarkerForException(resource, e);
                } catch (ParserErrorException e) {
                    exception = e;
                }
                // If an exception condition was encountered, handle it.
                if (exception != null) {
                    exception.printStackTrace();
                    EclipseCommonPlugin.handleError(ControlsPlugin.getDefault(),
                            exception);
                }
            }
        }
    }

    /**
     * Adds an {@link IMarker} for the given Exception against the specified
     * {@link IResource}. The markers message will be the message that the
     * {@link Exception#getMessage} method returns.
     * @param resource the associated IResource
     * @param exception the exception that the marker is being created for.
     */
    private void addMarkerForException(IResource resource,
                                       Exception exception) {
        try {
            IMarker marker = resource.createMarker(IMarker.PROBLEM);
            // use the exceptions message as the markers message.
            marker.setAttribute(IMarker.MESSAGE, exception.getMessage());
            marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
        } catch (CoreException e) {
            e.printStackTrace();
            EclipseCommonPlugin.handleError(
                    ControlsPlugin.getDefault(), e);
        }
    }

    /**
     * Create the DOMValidator with the appropriate entity resolver and error
     * reporter.
     *
     * This code is similar to 
     * {@link com.volantis.mcs.eclipse.ab.editors.dom.ODOMEditorContext#createODOMEditorContext}
     * and may benefit from some refactoring. todo later investigate this comment.
     *
     * @param errorReporter the error reporter to use to create the validator.
     * @return the newly created <code>XercesBasedDOMValidator</code>.
     * @throws SAXException         if a SAXException is encountered.
     * @throws ParserErrorException if a ParserErrorException is encountered.
     */
    private XercesBasedDOMValidator createDOMValidator(ErrorReporter errorReporter)
            throws SAXException, ParserErrorException {

        JarFileEntityResolver repositorySchemaResolver =
                RepositorySchemaResolverFactory.create();

        return new XercesBasedDOMValidator(repositorySchemaResolver,
                    errorReporter);
    }

    /**
     * Validate the policy name.
     *
     * @param resource  the resource that will be used to validate the policy
     *                  name.
     * @return true if the policy name is valid, false otherwise.
     */
    private boolean validatePolicyName(IResource resource) {

        boolean valid = true;

        PolicyNameValidator policyNameValidator = new PolicyNameValidator();

        ValidationMessageBuilder builder = new ValidationMessageBuilder(BUNDLE,
                MESSAGE_KEY_MAPPINGS, new Object[]{resource.getName()});

        // Perform the validation and store the result of the validation.
        ValidationStatus status =
                policyNameValidator.validate(resource.getName(), builder);

        if (status.getSeverity() == ValidationStatus.ERROR) {
            valid = false;
        } else if (status.getSeverity() == ValidationStatus.WARNING) {
            valid = false;
        }
        if (!valid) {
            try {
                IMarker marker = resource.createMarker(IMarker.PROBLEM);
                marker.setAttribute(IMarker.MESSAGE, status.getMessage());
                marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
            } catch (CoreException e) {
                e.printStackTrace();
                EclipseCommonPlugin.handleError(ControlsPlugin.getDefault(), e);
            }
        }
        return valid;
    }

    /**
     * Checks whether the resource is one that is handled using the new
     * repository model.
     *
     * <p>For the moment, only themes use the new model.</p>
     *
     * @param resource The resource to check
     * @return True if the resource uses the new model for validation, false
     *         otherwise
     */
    private boolean isNewModel(IResource resource) {
        String extension = resource.getFileExtension();
        boolean isNewModel = false;
        for (int i = 0; !isNewModel && i < NEW_MODEL_EXTENSIONS.length; i++) {
            if (NEW_MODEL_EXTENSIONS[i].getExtension().equals(extension)) {
                isNewModel = true;
            }
        }
        return isNewModel;
    }

    /**
     * Validate a resource using the repository model.
     * @param resource
     */
    private void repositoryModelValidate(IResource resource) {
        InputStream is = null;
        try {
            InternalPolicyFactory factory =
                    InternalPolicyFactory.getInternalInstance();
            // todo: re-enable validation by avoiding this overload once we have
            // sorted out validation.
            JiBXReader reader = factory.createDangerousNonValidatingPolicyReader();
            if (resource instanceof IFile && resource.exists()) {
                IFile file = (IFile) resource;
                is = file.getContents();
                BinaryContentInput content = new BinaryContentInput(is);
                Object theme = reader.read(content, file.getName());

                InteractionFactory interactionFactory =
                        InteractionFactory.getDefaultInstance();
                InteractionModel interactionModel = interactionFactory
                        .createInteractionModel(PolicyModel.MODEL_DESCRIPTOR);

                Proxy proxy = interactionModel.createProxyForModelObject(theme);
                proxy.validate();
                new ResourceDiagnosticsAdapter(resource).
                        setDiagnostics(proxy.getDiagnostics());

            }
        } catch (CoreException e) {
            e.printStackTrace();
            EclipseCommonPlugin.handleError(ControlsPlugin.getDefault(), e);
        } catch (IOException e) {
            e.printStackTrace();
            EclipseCommonPlugin.handleError(ControlsPlugin.getDefault(), e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    EclipseCommonPlugin.handleError(
                        ControlsPlugin.getDefault(), e);
                }
            }
        }
    }


    /**
     * Policy Builder visitor that is used to perform a partial/delta
     * validation.
     */
    private class PolicyBuildDeltaVisitor implements IResourceDeltaVisitor {

        // javadoc inherited
        public boolean visit(IResourceDelta delta) throws CoreException {
            doValidation(delta.getResource());
            return true;
        }
    }

    /**
     * Policy Builder visitor that is used to perform a full validation.
     */
    private class PolicyBuildVisitor implements IResourceVisitor {

        // javadoc inherited
        public boolean visit(IResource resource) throws CoreException {
            doValidation(resource);
            return true;
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 09-Dec-05	10756/1	geoff	VBM:2005120813 JiBX is reading XML using system default encoding

 09-Dec-05	10738/1	geoff	VBM:2005120813 JiBX is reading XML using system default encoding

 06-Dec-05	10539/1	adrianj	VBM:2005111712 Added 'New' button for device themes

 01-Dec-05	10520/1	adrianj	VBM:2005111712 Implement New... button for device themes

 17-Nov-05	10341/4	pduffin	VBM:2005111410 Fixed issue with mapping classes to type descriptors

 16-Nov-05	10341/1	pduffin	VBM:2005111410 Ported changes forward from MCS 3.5

 14-Nov-05	10287/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 16-Nov-05	10315/5	pduffin	VBM:2005111410 Added support for copying model objects
 16-Nov-05	9896/6	geoff	VBM:2005101906 Avoid using JDOM in MCS at runtime: rewrite runtime XML device repository

 13-Nov-05	9896/3	geoff	VBM:2005101906 Avoid using JDOM in MCS at runtime: rewrite runtime XML device repository

 07-Nov-05	10175/1	adrianj	VBM:2005110437 Validation in incremental builder
 14-Nov-05	10287/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 15-Nov-05	10315/1	pduffin	VBM:2005111410 Added support for copying model objects
 11-Nov-05	10199/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 07-Nov-05	10150/1	adrianj	VBM:2005110437 Validation in incremental builder

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Aug-04	5264/1	allan	VBM:2004081008 Remove invalid plugin dependencies

 09-Aug-04	5130/2	doug	VBM:2004080310 MCS

 17-May-04	4231/3	tom	VBM:2004042704 Fixedup the 2004032606 change

 17-May-04	4413/1	doug	VBM:2004051412 Fixed PolicyValueModifier labelling issue

 11-May-04	4272/1	allan	VBM:2004050503 Unique problem markers fix for device editor.

 06-May-04	4068/1	allan	VBM:2004032103 Structure page policies section.

 26-Apr-04	4037/1	doug	VBM:2004042301 Provided mechanism for obtaining an EntityResolver that resolves all MCS repository schemas

 16-Apr-04	3362/4	steve	VBM:2003082208 supermerged

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 01-Apr-04	3614/1	byron	VBM:2004022404 Layout: Panes are allowed same names - builder now does validation

 18-Feb-04	3068/4	allan	VBM:2004021115 Rework issues.

 18-Feb-04	3068/2	allan	VBM:2004021115 Validate fallback extensions in wizards.

 16-Feb-04	3044/2	allan	VBM:2004021604 Ensure that MCSProjectBuilder only builds policies.

 16-Feb-04	3023/4	philws	VBM:2004010901 Fix JDK 1.4/Eclipse XML API issue with JDOM SAXBuilder and the Volantisized Xerces parser

 13-Feb-04	2985/1	allan	VBM:2004012803 Allow policies to be created in non-MCS projects.

 12-Feb-04	2962/1	allan	VBM:2004021113 Replace old 3 char file extensions with new 4 char ones.

 03-Feb-04	2820/2	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 08-Jan-04	2447/1	philws	VBM:2004010609 Fix UniqueAssetValidator and repackage it

 07-Jan-04	2426/3	richardc	VBM:2004010607 Refactored registration of UniqueAssetValidator

 06-Jan-04	2323/9	doug	VBM:2003120701 Added better validation error messages

 06-Jan-04	2323/7	doug	VBM:2003120701 Added better validation error messages

 30-Dec-03	2311/2	doug	VBM:2003122901 Added IMarker for resource when xml is badly formed

 19-Dec-03	2237/3	byron	VBM:2003112804 Provide an MCS project builder - addressed various issues

 19-Dec-03	2237/1	byron	VBM:2003112804 Provide an MCS project builder

 ===========================================================================
*/
