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
package com.volantis.mcs.eclipse.ab.editors.dom;

import com.volantis.mcs.eclipse.ab.ABPlugin;
import com.volantis.mcs.eclipse.ab.editors.EditorMessages;
import com.volantis.mcs.eclipse.ab.editors.SaveCommand;
import com.volantis.mcs.eclipse.ab.editors.SaveCommandFactory;
import com.volantis.mcs.eclipse.ab.editors.dom.validation.AsynchronousXMLValidator;
import com.volantis.mcs.eclipse.ab.editors.dom.validation.MarkerGeneratingErrorReporter;
import com.volantis.mcs.eclipse.ab.editors.dom.validation.ValidationListener;
import com.volantis.mcs.eclipse.ab.editors.dom.validation.XMLValidator;
import com.volantis.mcs.eclipse.common.EclipseCommonMessages;
import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.eclipse.common.PolicyUtils;
import com.volantis.mcs.eclipse.common.RepositorySchemaResolverFactory;
import com.volantis.mcs.eclipse.common.odom.ODOMChangeEvent;
import com.volantis.mcs.eclipse.common.odom.ODOMChangeListener;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.common.odom.ODOMFactory;
import com.volantis.mcs.eclipse.common.odom.ODOMObservable;
import com.volantis.mcs.eclipse.common.odom.ODOMSelectionManager;
import com.volantis.mcs.eclipse.common.odom.undo.UndoRedoManager;
import com.volantis.mcs.eclipse.common.odom.undo.UndoRedoMementoOriginator;
import com.volantis.mcs.eclipse.controls.ActionableHandler;
import com.volantis.mcs.eclipse.builder.BuilderPlugin;
import com.volantis.mcs.eclipse.builder.preference.MCSPreferencePage;
import com.volantis.mcs.xml.validation.DOMSupplementaryValidatorDetails;
import com.volantis.mcs.xml.validation.DOMValidator;
import com.volantis.mcs.xml.validation.ErrorReporter;
import com.volantis.mcs.xml.validation.sax.ParserErrorException;
import com.volantis.mcs.xml.validation.sax.xerces.QueueingXercesBasedDOMValidator;
import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.xml.schema.JarFileEntityResolver;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.util.ListenerList;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.texteditor.ResourceAction;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.output.XMLOutputter;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.UndeclaredThrowableException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Contextual information for ODOM Editors. An explicit call to
 * {@link #dispose} must be made when the context is no longer required.
 */
public class ODOMEditorContext {
    /**
     * The prefix for resources associated with MultiPageODOMEditor.
     * Has package access on purpose.
     */
    static final String RESOURCE_PREFIX = "ODOMEditorContext."; //$NON-NLS-1$

    /**
     * The id of an MCS Admin project nature.
     */
    public static final String MCS_ADMIN_NATURE_ID = "MCSAdminNature";

    /**
     * The ODOMFactory for this context.
     */
    public static final ODOMFactory ODOM_FACTORY = new LPDMODOMFactory();

    /**
     * The ODOMSelectionManager in this context.
     */
    private ODOMSelectionManager oDOMSelectionManager;

    /**
     * The IResource in this context. This will be the IResource that is
     * being edited.
     */
    private IResource policyResource;

    /**
     * A map root elements and their associated ValidationManagers for the
     * validation of all the root elements that are handled by this
     * ODOMEditorContext. This map serves as both a collection of root elements
     * and ValidationManagers as well as a mapping between the two.
     */
    private Map rootElementsToValidationManagers = Collections.
            synchronizedMap(new HashMap());

    /**
     * A collection of ValidationListeners that are interested in
     * Validation events on against every root element and hence every
     * ValidationManager managed by this ODOMEditorContext. This list is
     * necessary so that newly added and removed root elements can be
     * listened to and stopped listening on as appropriate.
     */
    private Collection globalValidationListeners = new ArrayList();

    /**
     * A collection of ODOMChangeListeners that are interested in odom
     * change events on any of the root elements managed by this
     * ODOMEditorContext. This list is necessary so that newly added and
     * removed root elements can be listened to and stopped listening on as
     * appropriate.
     */
    private Collection globalChangeListeners = new ArrayList();

    /**
     * A collection of the supplementary validators that have been added
     * to this ODOMEditorContext. This list is necessary so that newly added
     * root elements can have relevant supplementary validators added to
     * their associated ValidationManager. The contents of this collection
     * will contain DOMSupplementaryValidatorDetails objects in order to
     * establish the relevancy of a supplementary validator to a given
     * root element.
     */
    private Collection supplementaryValidators = new ArrayList();


    /**
     * The IActionBars for the editor in context.
     */
    private IActionBars actionBars;

    /**
     * The UndoRedoManager listening for changes from this context's rootElement
     */
    private final UndoRedoManager undoRedoManager;

    /**
     * The actionable handler associated with this context.
     */
    private ActionableHandler handler;

    /**
     * Factory that will be used to create SaveCommand instances.
     */
    private SaveCommandFactory saveCommandFactory;

    /**
     * Flag indicating whether or not the project within which this context
     * is used has an MCS Admin nature i.e. is in admin mode or not.
     */
    private boolean isAdminProject;

    /**
     * Initializes an <code>ODOMEditorContext</code> with the given arguments
     *
     * @param policyResource            the {@link IResource} associated with the editor
     * @param undoRedoMementoOriginator the originator used when restoring the
     *                                  ui after undo/redo
     * @param rootElement               the rootElement that is being edited. Can be null.
     * @param rootElementIdentifier     an identifier for the root element -
     *                                  generally used by editors that edit multiple documents. Can be null.
     */
    protected ODOMEditorContext(
            IResource policyResource,
            UndoRedoMementoOriginator undoRedoMementoOriginator,
            ODOMElement rootElement, String rootElementIdentifier)
            throws SAXException, ParserErrorException {
        setPolicyResource(policyResource);

        if (undoRedoMementoOriginator == null) {
            throw new IllegalArgumentException(
                    "undoRedoMementoOriginator cannot be null"); //$NON-NLS-1$
        }

        this.oDOMSelectionManager = createODOMSelectionManager();

        this.undoRedoManager = new UndoRedoManager(undoRedoMementoOriginator);

        if (rootElement != null) {
            addRootElement(rootElement, rootElementIdentifier);
        }
    }

    /**
     * Get the ValidationManager associated with a particular root element.
     *
     * @param rootElement the root element.
     */
    private ValidationManager getValidationManager(ODOMElement rootElement) {
        return (ValidationManager)
                rootElementsToValidationManagers.get(rootElement);
    }

    /**
     * Add a rootElement to be managed by this ODOMEditorContext. Carries out
     * no validation. If the given element is already managed, this method does
     * nothing.
     * <p/>
     * <p>This method is used to circumvent DOM-based validation of an ODOM in
     * those cases where external validation will be applied.</p>
     *
     * @param rootElement           the root ODOMElement to be managed.
     * @param rootElementIdentifier a String that is uniquely associated with
     *                              the root element. This String is used for generating resource markers
     *                              and in the case of editors that edit multiple files within the same
     *                              resource is used to identify which file the problem is associated with
     *                              - e.g. the DeviceEditor. Can be null.
     * @throws SAXException             if there is a validation problem
     * @throws ParserErrorException     if there is a validation problem
     * @throws IllegalArgumentException if rootElement is not a root element
     */
    public synchronized void addRootElementWithoutValidation(
            ODOMElement rootElement, String rootElementIdentifier)
            throws SAXException, ParserErrorException {
        ValidationManager validationManager = getValidationManager(rootElement);

        if (validationManager == null) {
            // Have the UndoRedoManager track changes to this element. Note the
            // UndoRedoManager will continue to track changes to this element
            // until the UndoRedoManager is disposed or the stopTrackingElement
            // method is called using the same element. If the UndoRedoManager
            // is already tracking this ODOMElement instance then calling
            // trackElement will have no effect.
            undoRedoManager.startTrackingElement(rootElement);

            // Create a non-functional validation manager. This is still
            // required, as the keys from the map of validation managers is used
            // to provide the list of root elements (!).
            ValidationManager manager = new ValidationManager(rootElement,
                    getPolicyResource(), rootElementIdentifier) {
                // Javadoc inherited
                public void validate() {
                    // Do nothing - validation for this element will be
                    // handled externally
                }
            };

            // Add all the global validation listeners to the new
            // ValidationManager.
            Iterator globalListeners = globalValidationListeners.iterator();
            while (globalListeners.hasNext()) {
                manager.addValidationListener((ValidationListener)
                        globalListeners.next());
            }

            // Add all the global change listeners to the new root element
            for (Iterator i = globalChangeListeners.iterator(); i.hasNext();) {
                rootElement.addChangeListener((ODOMChangeListener) i.next());
            }
            // Add all the relevant supplementary validators to the new
            // ValidationManager
            Iterator validators = supplementaryValidators.iterator();
            while (validators.hasNext()) {
                DOMSupplementaryValidatorDetails validatorDetails =
                        (DOMSupplementaryValidatorDetails)
                                validators.next();
                if (validatorDetails.namespaceURI.equals(rootElement.
                        getNamespaceURI())) {
                    manager.addSupplementaryValidator(validatorDetails);
                }
            }

            rootElementsToValidationManagers.put(rootElement, manager);
        }
    }

    /**
     * Add a rootElement to be managed by this ODOMEditorContext. If the
     * given element is already managed then this method does nothing.
     * <p/>
     * The added element is validated as it is added.
     *
     * @param rootElement           the root ODOMElement to be managed.
     * @param rootElementIdentifier a String that is uniquely associated with
     *                              the root element. This String is used for generating resource markers
     *                              and in the case of editors that edit multiple files within the same
     *                              resource is used to identify which file the problem is associated with
     *                              - e.g. the DeviceEditor. Can be null.
     * @throws SAXException             if there is a validation problem
     * @throws ParserErrorException     if there is a validation problem
     * @throws IllegalArgumentException if rootElement is not a root element
     */
    public synchronized void addRootElement(ODOMElement rootElement,
                                            String rootElementIdentifier)
            throws SAXException, ParserErrorException {

        if (rootElement.getParent() != null) {
            throw new IllegalArgumentException("Not root element: " +
                    rootElement.getName());
        }

        ValidationManager validationManager = getValidationManager(rootElement);

        if (validationManager == null) {
            // Have the UndoRedoManager track changes to this element. Note the
            // UndoRedoManager will continue to track changes to this element
            // until the UndoRedoManager is disposed or the stopTrackingElement
            // method is called using the same element. If the UndoRedoManager
            // is already tracking this ODOMElement instance then calling
            // trackElement will have no effect.
            undoRedoManager.startTrackingElement(rootElement);

            ValidationManager manager = new ValidationManager(rootElement,
                    getPolicyResource(), rootElementIdentifier);

            // Add all the global validation listeners to the new
            // ValidationManager.
            Iterator globalListeners = globalValidationListeners.iterator();
            while (globalListeners.hasNext()) {
                manager.addValidationListener((ValidationListener)
                        globalListeners.next());
            }

            // Add all the global change listeners to the new root element
            for (Iterator i = globalChangeListeners.iterator(); i.hasNext();) {
                rootElement.addChangeListener((ODOMChangeListener) i.next());
            }
            // Add all the relevant supplementary validators to the new
            // ValidationManager
            Iterator validators = supplementaryValidators.iterator();
            while (validators.hasNext()) {
                DOMSupplementaryValidatorDetails validatorDetails =
                        (DOMSupplementaryValidatorDetails)
                                validators.next();
                if (validatorDetails.namespaceURI.equals(rootElement.
                        getNamespaceURI())) {
                    manager.addSupplementaryValidator(validatorDetails);
                }
            }

            manager.validate();
            rootElementsToValidationManagers.put(rootElement, manager);
        }
    }

    /**
     * Remove a root element from the root elements managed by this
     * ODOMEditorContext. This method does nothing if the given root element
     * is not currently being managed.
     *
     * @param rootElement the root ODOMElement
     */
    public synchronized void removeRootElement(ODOMElement rootElement) {
        ValidationManager validationManager = getValidationManager(rootElement);
        // If there is already a ValidationManager then the given rootElement
        // has already been added.
        if (validationManager != null) {
            // Stop tracking the given root element for undo changes.
            undoRedoManager.stopTrackingElement(rootElement);

            validationManager.dispose();

            // Removing the validation from rootElementsToValidationManagers
            // will automatically dispose of the validation listeners associated
            // with that manager by virtue of the fact there will be no more
            // references to the validation manager so it will will be GC'd
            // along with the listeners - assuming noone else is maintaining
            // references to the listeners that is.
            rootElementsToValidationManagers.remove(rootElement);
        }

        // Remove all the global change listeners to the new root element
        for (Iterator i = globalChangeListeners.iterator(); i.hasNext();) {
            rootElement.removeChangeListener((ODOMChangeListener) i.next());
        }
    }

    /**
     * Create an instance of ODOMEditorContext specifying the name of the
     * root element and the IFile that is the policy resource for the
     * ODOMEditorContext.
     *
     * @param rootElementName   The name of the root element for the context.
     * @param file              The IFile that is the policy resource for the
     *                          ODOMEditorContext.
     * @param mementoOriginator the originator used when restoring the ui after
     *                          undo/redo
     * @return An ODOMEditorContext.
     */
    public static ODOMEditorContext createODOMEditorContext(
            String rootElementName,
            IFile file,
            UndoRedoMementoOriginator mementoOriginator) {

        ODOMEditorContext context = null;
        try {
            context = new ODOMEditorContext(
                    file,
                    mementoOriginator,
                    ODOMEditorUtils.createRootElement(file, ODOM_FACTORY),
                    null);

        } catch (CoreException e) {
            EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
        } catch (IOException e) {
            EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
        } catch (JDOMException e) {
            // If there is a parse error while building the ODOM we
            // provide an error dialog.
            handleJDOMException(rootElementName, file, e);
        } catch (ParserErrorException e) {
            EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
        } catch (SAXException e) {
            EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
        }

        return context;
    }

    /**
     * FactoryMethod for creating an <code>ODOMSelectionManager</code> instance
     *
     * @return a ODOMSelectionManager instance
     */
    protected ODOMSelectionManager createODOMSelectionManager() {
        // don't need a filter
        return new ODOMSelectionManager(null);
    }

    /**
     * Provide a handler for JDOMExceptions that brings up a dialog
     * informing the user that the editor context could be created.
     *
     * @param rootElementName The name of the root element.
     * @param file            The IFile that is associated with this ODOMEditorContext.
     * @param jDOMException   The JDOM exception that is to be handled.
     */
    protected static void handleJDOMException(String rootElementName,
                                              IFile file,
                                              JDOMException jDOMException) {
        // PolicyName is used in the dialog message and title.
        String policyName = EclipseCommonMessages.
                getLocalizedPolicyName(rootElementName);

        // Create the title
        String title = EditorMessages.getString(RESOURCE_PREFIX +
                "creationErrorDialog.title"); //$NON-NLS-1$
        MessageFormat titleFormat = new MessageFormat(title);
        String args[] = {policyName};
        String formattedTitle = titleFormat.format(args);

        // Create the message
        String message = EditorMessages.getString(RESOURCE_PREFIX +
                "creationErrorDialog.message"); //$NON-NLS-1$
        MessageFormat messageFormat = new MessageFormat(message);
        args = new String[]{file.getName(), policyName};
        String formattedMessage = messageFormat.format(args);

        // Show the dialog
        showErrorDialog(formattedTitle, jDOMException, formattedMessage, true);
    }


    /**
     * Show an error dialog based on a Throwable. The message will be
     * a combination of the message in the throwable and the supplied
     * message. If there is no supplied message then only the throwable
     * message is displayed. The caller can optionally disable the
     * inclusion of the throwable message provided "message" is not
     * null.
     * <p/>
     * The title of the dialog will be the name of the editor.
     *
     * @param t                       The Throwable from which to obtain the error message.
     *                                Cannot be null.
     * @param message                 The message that is displayed in addition to any
     *                                message in the throwable. This message is displayed first in the
     *                                dialog. This can be null in which case the message from the
     *                                Throwable is used.
     * @param excludeThrowableMessage If true will exclude the message
     *                                from the Throwable.
     * @throws IllegalArgumentException If excludeThrowableMessage is true
     *                                  and message is null or if t is null.
     */
    public void showErrorDialog(Throwable t, String message,
                                boolean excludeThrowableMessage) {


        String title = EditorMessages.getString(RESOURCE_PREFIX +
                getRootElement().getName() + ".name"); //$NON-NLS-1$
        showErrorDialog(title, t, message, excludeThrowableMessage);
    }


    /**
     * Show an error dialog based on a Throwable. The message will be
     * a combination of the message in the throwable and the supplied
     * message. If there is no supplied message then only the throwable
     * message is displayed. The caller can optionally disable the
     * inclusion of the throwable message provided "message" is not
     * null.
     *
     * @param title                   The title of the dialog.
     * @param t                       The Throwable from which to obtain the error message.
     *                                Cannot be null.
     * @param message                 The message that is displayed in addition to any
     *                                message in the throwable. This message is displayed first in the
     *                                dialog. This can be null in which case the message from the
     *                                Throwable is used.
     * @param excludeThrowableMessage If true will exclude the message
     *                                from the Throwable.
     * @throws IllegalArgumentException If excludeThrowableMessage is true
     *                                  and message is null or if t is null.
     */
    public static void showErrorDialog(String title, Throwable t,
                                       String message,
                                       boolean excludeThrowableMessage) {
        if (t == null) {
            throw new IllegalArgumentException(
                    "Cannot be null: t"); //$NON-NLS-1$
        }
        if (excludeThrowableMessage && message == null) {
            throw new IllegalArgumentException(
                    "Cannot be null: message -" + //$NON-NLS-1$
                            " when excludeThrowableMessage is true"); //$NON-NLS-1$
        }

        StringBuffer messageBuffer = new StringBuffer();
        if (message != null) {
            messageBuffer.append(message);
        }

        if (message != null && !excludeThrowableMessage) {
            messageBuffer.append("\n\n"); //$NON-NLS-1$
        }

        if (!excludeThrowableMessage) {
            messageBuffer.append(t.getMessage());
        }
        String pluginId = ABPlugin.getDefault().
                getDescriptor().getUniqueIdentifier();
        Status status = new Status(Status.ERROR, pluginId,
                Status.ERROR, messageBuffer.toString(), t);

        Shell shell = Display.getCurrent().getActiveShell();
        ErrorDialog.openError(shell, title, null, status);
    }

    /**
     * Get all the root elements as a Collection.
     */
    protected Collection getRootElements() {
        return rootElementsToValidationManagers.keySet();
    }

    /**
     * Get all the ValidationManagers as a Collection.
     */
    private Collection getValidationManagers() {
        return rootElementsToValidationManagers.values();
    }

    /**
     * Provide the MarkerGeneratingErrorReporter associated with the
     * only ValidationManager in the collection of managers in this
     * ODOMEditorContext. This method is typically used when there is only
     * ever a single ValidationManager in the ODOMEditorContext.
     *
     * @return The MarkerGeneratingErrorReporter associated with this
     *         ODOMEditorContext.
     * @throws IllegalStateException if there are multiple ValidationManagers.
     */
    public MarkerGeneratingErrorReporter getErrorReporter() {
        Collection validationManagers = getValidationManagers();
        if (validationManagers.size() > 1) {
            throw new IllegalStateException(
                    "Expected only 1 ValidationManager" +
                            " but there were " + validationManagers.size());
        }

        return ((ValidationManager) validationManagers.iterator().next()).
                getErrorReporter();
    }

    /**
     * Get the MarkerGeneratingErrorReporter for the given root element
     *
     * @param rootElement the root Element associated with the
     *                    MarkerGeneratingErrorReporter to get.
     * @return the MarkerGeneratingErrorRepositor for the given root element.
     */
    public MarkerGeneratingErrorReporter
    getErrorReporter(ODOMElement rootElement) {
        ValidationManager validationManager =
                getValidationManager(rootElement);

        return validationManager.getErrorReporter();
    }

    /**
     * This method must be called when the editor context is no longer needed.
     */
    public void dispose() {
        /* For consistency, we should explicitly dispose the undo/redo actions
         * (remove themselves as listeners of the UndoredoManager) here.
         * But at this point getActionBars() is empty, so the task has been
         * included in UndoRedoManager.dispose()
         */

        undoRedoManager.dispose();

        Iterator managers = getValidationManagers().iterator();
        while (managers.hasNext()) {
            ((ValidationManager) managers.next()).dispose();
        }
    }

    /**
     * Add a validation listener to the first ValidationManager in the
     * collection of ValidationManagers. This method is typically for use
     * with ODOMEditorContexts that only ever have a single ValidationManager.
     *
     * @param listener the ValidationListener
     * @throws IllegalStateException if there are multiple ValidationManagers.
     */
    public void addValidationListener(ValidationListener listener) {
        Collection validationManagers = getValidationManagers();
        if (validationManagers.size() > 1) {
            throw new IllegalStateException(
                    "Expected only 1 ValidationManager" +
                            " but there were " + validationManagers.size());
        }
        ((ValidationManager) validationManagers.iterator().next()).
                addValidationListener(listener);
    }

    /**
     * Adds a validation listener to the ValidationManager associated with
     * a given root element.
     *
     * @param rootElement the root ODOMElement
     * @param listener    the ValidationListener
     * @throws IllegalArgumentException if there is no ValidationManager
     *                                  for the given rootElement.
     */
    public void addValidationListener(ODOMElement rootElement,
                                      ValidationListener listener) {
        ValidationManager manager = (ValidationManager)
                rootElementsToValidationManagers.get(rootElement);

        if (manager == null) {
            throw new IllegalArgumentException("There is no ValidationManager" +
                    " for element " + rootElement.getName());
        }

        manager.addValidationListener(listener);
    }

    /**
     * Add a change listener to this ODOMEditorContext that will listens for
     * changes on all the root elements managed by this context.
     *
     * @param listener the ODOMChangeListener
     */
    public synchronized void addChangeListener(ODOMChangeListener listener) {
        if (!globalChangeListeners.contains(listener)) {
            Iterator rootElements = getRootElements().iterator();
            while (rootElements.hasNext()) {
                ((ODOMElement) rootElements.next()).addChangeListener(listener);
            }
            globalChangeListeners.add(listener);
        }
    }

    /**
     * Remove a change listener from this ODOMEditorContext that will listens for
     * changes on all the root elements managed by this context.
     *
     * @param listener the ODOMChangeListener
     */
    public synchronized void removeChangeListener(ODOMChangeListener listener) {
        if (globalChangeListeners.remove(listener)) {
            Iterator rootElements = getRootElements().iterator();
            while (rootElements.hasNext()) {
                ((ODOMElement) rootElements.next())
                        .removeChangeListener(listener);
            }
        }
    }

    /**
     * Adds a validation listener to all ValidationManagers associated with
     * this context i.e. will be notified of changes in any of the root elements
     * belonging to this context. As new root elements are added and root
     * elements removed from the context the listeners added here will be
     * updated registered and un-registered as appropriate.
     *
     * @param listener the ValidationListener
     */
    public synchronized void addValidationsListener(
            ValidationListener listener) {
        if (!globalValidationListeners.contains(listener)) {
            Iterator managers = getValidationManagers().iterator();
            while (managers.hasNext()) {
                ((ValidationManager) managers.next()).
                        addValidationListener(listener);
            }
            globalValidationListeners.add(listener);
        }
    }

    /**
     * Removes a validation listener to from all ValidationManagers associated
     * with this context
     *
     * @param listener the ValidationListener
     */
    public synchronized void removeValidationsListener(
            ValidationListener listener) {
        if (globalValidationListeners.remove(listener)) {
            Iterator managers = getValidationManagers().iterator();
            while (managers.hasNext()) {
                ((ValidationManager) managers.next()).
                        removeValidationListener(listener);
            }
        }
    }

    /**
     * Remove a validation listener from the first ValidationManager in the
     * collection of ValidationManagers. This method is typically for use
     * with ODOMEditorContexts that only ever have a single ValidationManager.
     *
     * @param listener the ValidationListener
     * @throws IllegalStateException if there are multiple ValidationManagers.
     */
    public void removeValidationListener(ValidationListener listener) {
        Collection validationManagers = getValidationManagers();
        if (validationManagers.size() > 1) {
            throw new IllegalStateException(
                    "Expected only 1 ValidationManager" +
                            " but there were " + validationManagers.size());
        }
        ((ValidationManager) validationManagers.iterator().next()).
                removeValidationListener(listener);
    }


    /**
     * Removes a validation listener from the ValidationManager associated with
     * a given root element.
     *
     * @param rootElement the root ODOMElement
     * @param listener    the ValidationListener
     * @throws IllegalArgumentException if there is no ValidationManager
     *                                  for the given rootElement.
     */
    public void removeValidationListener(ODOMElement rootElement,
                                         ValidationListener listener) {
        ValidationManager manager = (ValidationManager)
                rootElementsToValidationManagers.get(rootElement);

        if (manager == null) {
            throw new IllegalArgumentException("There is no ValidationManager" +
                    " for element " + rootElement.getName());
        }

        manager.removeValidationListener(listener);
    }


    /**
     * @return The IActionBars associated with this context
     */
    public IActionBars getActionBars() {
        return actionBars;
    }

    /**
     * Set the IActionBars associated with this context.
     *
     * @param actionBars The IActionsBars. Cannot be null.
     */
    public void setActionBars(IActionBars actionBars) {
        if (actionBars == null) {
            throw new IllegalArgumentException(
                    "Cannot be null: actionBars"); //$NON-NLS-1$
        }
        this.actionBars = actionBars;
    }

    /**
     * The policy type is the name of the root element associated with this
     * ODOMEditorContent.
     *
     * @return The policy type associated with this context.
     */
    public String getPolicyType() {
        return getRootElement().getName();
    }

    /**
     * @return The ODOMFactory in this context.
     */
    public ODOMFactory getODOMFactory() {
        return ODOM_FACTORY;
    }

    /**
     * Provide the single rootElement associated with this ODOMEditorContext.
     * This method is typically used when there is only ever a single
     * rootElement in the ODOMEditorContext.
     *
     * @return the root ODOMElement associated with this ODOMEditorContext.
     * @throws IllegalStateException if there are multiple root elements.
     */
    public ODOMElement getRootElement() {
        Collection rootElements = getRootElements();
        if (rootElements.size() > 1) {
            throw new IllegalStateException("Expected only 1 rootElement" +
                    " but there were " + rootElements.size());
        } else if (rootElements.size() == 0) {
            return null;
        }
        return ((ODOMElement) rootElements.iterator().next());
    }

    /**
     * @return The IResource in this context.
     */
    public IResource getPolicyResource() {
        return policyResource;
    }

    /**
     * Set the policyResource.
     * <p/>
     * This is package local so that the ODOMEditorPart can change it should
     * a saveAs() operation take place.
     *
     * @param policyResource The policyResource.
     * @throws IllegalArgumentException If policyResource is null.
     */
    public void setPolicyResource(IResource policyResource) {
        if (policyResource == null) {
            throw new IllegalArgumentException(
                    "Cannot be null: " + //$NON-NLS-1$
                            "policyResource"); //$NON-NLS-1$
        }
        this.policyResource = policyResource;

        try {
            isAdminProject = policyResource.getProject().
                    hasNature(MCS_ADMIN_NATURE_ID);
        } catch (CoreException e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    /**
     * Determine whether or not the project associated with this context
     * is in administration mode or not.
     */
    public boolean isAdminProject() {
        return isAdminProject;
    }

    /**
     * @return The ODOMSelectionManager in this context.
     */
    public ODOMSelectionManager getODOMSelectionManager() {
        return oDOMSelectionManager;
    }

    /**
     * Find any problem type IMarkers on the IResource associated with this
     * context that are themselves associated with a particular XPath.
     *
     * @param xPath The XPath used as a key to find IMarkers on the
     *              IResource in this context.
     * @return All the problem type IMarkers on this contexts IResource that
     *         are assocatied with the given XPath. Will return an empty array if
     *         there are no matching markers.
     * @throws CoreException         If there is a problem calling
     *                               Resource.findMarkers.
     * @throws IllegalStateException If there is no IResource associated with
     *                               this context.
     */
    public IMarker[] findProblemMarkers(XPath xPath) throws CoreException {
        return PolicyUtils.findProblemMarkers(getPolicyResource(), xPath);
    }


    /**
     * Determine whether or not the resource associated with this
     * context exists.
     * <p/>
     * ODOMEditorContexts that operate on input that is not know to the file
     * system should override this method with their own implemenation.
     *
     * @return true If the resource associated with this context exists
     *         in the file system; false otherwise.
     */
    public boolean resourceExists() {
        return ODOMEditorUtils.resourceExists(getPolicyResource());
    }

    //javadoc unnecessary
    public UndoRedoManager getUndoRedoManager() {
        return undoRedoManager;
    }

    /**
     * This method updates actions to the provided pagesite's IActionBars.
     * <p/>
     * <p> <strong>PRECONDITION:</strong> {@link #setActionBars} has already
     * been called on this context (by the editor associated with this context).
     * </p>
     *
     * @param pageSite the page site to add the actions to.
     */
    public void updatePageSiteActions(IPageSite pageSite) {
        IActionBars pageSiteActionBars = pageSite.getActionBars();

        updateAction(pageSiteActionBars, IWorkbenchActionConstants.UNDO);
        updateAction(pageSiteActionBars, IWorkbenchActionConstants.REDO);
        updateAction(pageSiteActionBars, IWorkbenchActionConstants.CUT);
        updateAction(pageSiteActionBars, IWorkbenchActionConstants.COPY);
        updateAction(pageSiteActionBars, IWorkbenchActionConstants.PASTE);
        updateAction(pageSiteActionBars, IWorkbenchActionConstants.SELECT_ALL);

        pageSiteActionBars.updateActionBars();
    }


    /**
     * Helper method to update the global action handler settings.
     *
     * @param pageSiteActionBars the action bars.
     * @param key                the global action handler key defined in <code>IWorkbenchActionConstants</code>.
     */
    private void updateAction(IActionBars pageSiteActionBars, String key) {
        pageSiteActionBars.setGlobalActionHandler(key,
                getActionBars().getGlobalActionHandler(key));
    }

    /**
     * Getter method for the <code>ActionableHandler</code>.
     *
     * @return the handler.
     */
    public ActionableHandler getHandler() {
        return handler;
    }

    /**
     * Setter method for the <code>ActionableHandler</code>.
     *
     * @param handler
     */
    public void setHandler(ActionableHandler handler) {
        this.handler = handler;
    }

    /**
     * Creates an InputStream for this Document associated with this context,s
     * root element
     *
     * @return an InputStream instance
     * @throws IllegalStateException if the root element does not have an
     *                               associated document
     */
    protected InputStream createDocumentInputStream() {
        Document document = getRootElement().getDocument();
        if (document == null) {
            throw new IllegalStateException(
                    "Root element has no associated document"); //$NON-NLS-1$
        }
        XMLOutputter out = new XMLOutputter("    "); //$NON-NLS-1$
        out.setNewlines(false);
        out.setTrimAllWhite(false);
        String docString = out.outputString(document);
        byte[] bytes = new byte[0];
        try {
            bytes = docString.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
            throw new UndeclaredThrowableException(e);
        }
        return new ByteArrayInputStream(bytes);
    }

    /**
     * Returns a <code>SaveCommandFactory</code> instance that can be
     * used to save this context's associated root element to it's resource
     *
     * @return a SaveCommandFactory instance
     */
    public SaveCommandFactory getSaveCommandFactory() {
        if (saveCommandFactory == null) {
            saveCommandFactory = createSaveCommandFactory();
        }
        return saveCommandFactory;
    }

    /**
     * Creates a <code>SaveCommandFactory</code> instance that can be
     * used to save this context's associated root element to it's resource
     *
     * @return a SaveCommandFactory instance
     */
    protected SaveCommandFactory createSaveCommandFactory() {
        return new ODOMEditorSaveCommandFactory();
    }

    /**
     * Add a supplementary validator to all the ValidationManagers
     * associated with this ODOMEditorContext i.e. to affect all root
     * elements managed by this ODOMEditorContext.
     * <p/>
     * If a validator for the same element and namespace has already been added
     * then this method does nothing.
     * <p/>
     * NOTE: where the same supplementary validator is required for the same
     * element/namespace combination, use the same instance of the supplementary
     * validator in the validator details or implement equals() and hashCode()
     * on the given supplementary validator. Otherwise multiple validators will
     * run that do exactly the same thing.
     */
    // rest of javadoc inherited
    public void addSupplementaryValidator(
            DOMSupplementaryValidatorDetails validatorDetails) {
        if (!supplementaryValidators.contains(validatorDetails)) {
            Iterator rootElements = getRootElements().iterator();
            while (rootElements.hasNext()) {
                ODOMElement rootElement = (ODOMElement) rootElements.next();
                // Only register the supplementary validator on
                // ValidationManagers that manage validation on relevant
                // rootElements.
                if (rootElement.getNamespaceURI()
                        .equals(validatorDetails.namespaceURI)) {
                    ((ValidationManager) rootElementsToValidationManagers.
                            get(rootElement)).
                            addSupplementaryValidator(validatorDetails);
                }
            }
            supplementaryValidators.add(validatorDetails);
        }
    }

    /**
     * Remove a supplementary validator from all the ValidationManagers in the
     * collection of ValidationManagers i.e. to affect all root
     * elements managed by this ODOMEditorContext.
     * <p/>
     * If the specified supplementary validator has not been added already
     * then this method does nothing.
     */
    // rest of javadoc inherited
    public void removeSupplementaryValidator(
            DOMSupplementaryValidatorDetails validatorDetails) {

        if (supplementaryValidators.remove(validatorDetails)) {
            Iterator rootElements = getRootElements().iterator();
            while (rootElements.hasNext()) {
                ODOMElement rootElement = (ODOMElement) rootElements.next();
                // Only unregister the supplementary validator on
                // ValidationManagers that manage validation on relevant
                // rootElements.
                if (rootElement.getNamespaceURI()
                        .equals(validatorDetails.namespaceURI)) {
                    ((ValidationManager) rootElementsToValidationManagers.
                            get(rootElement)).
                            removeSupplementaryValidator(validatorDetails);
                }
            }
        }
    }

    /**
     * A <code>SaveCommandFactory<code> implmentation that allows this
     * contexts root element to be saved.
     */
    private class ODOMEditorSaveCommandFactory implements SaveCommandFactory {
        // javadoc inherited
        public SaveCommand createSaveCommand() {
            return new SaveCommand() {
                // javadoc inherited
                public void save(IProgressMonitor progressMonitor)
                        throws CoreException {
                    InputStream contents = createDocumentInputStream();
                    try {
                        // update the content of the resource with the modified
                        // document.
                        ((IFile) policyResource).setContents(contents,
                                false,
                                true,
                                progressMonitor);
                    } finally {
                        try {
                            contents.close();
                        } catch (IOException e) {
                            EclipseCommonPlugin.handleError(
                                    ABPlugin.getDefault(), e);
                        }
                    }
                }
            };
        }

        // javadoc inherited
        public SaveCommand createSaveAsCommand(final IFile destinationFile) {
            return new SaveCommand() {
                // javadoc inherited
                public void save(IProgressMonitor progressMonitor)
                        throws CoreException {
                    InputStream contents = createDocumentInputStream();
                    try {
                        if (destinationFile.exists()) {
                            // Overwrite the contents of the file that is exists
                            // already.
                            destinationFile.setContents(contents,
                                    false,
                                    true,
                                    progressMonitor);
                        } else {
                            // Create a new resource.
                            destinationFile.create(contents,
                                    false,
                                    progressMonitor);
                        }
                    } finally {
                        try {
                            // close the input stream
                            contents.close();
                        } catch (IOException e) {
                            EclipseCommonPlugin.handleError(
                                    ABPlugin.getDefault(), e);
                        }
                    }
                }
            };
        }
    }

    /**
     * A job that validates and can be terminated while running.
     */
    static class ValidationJob extends Job {
        DOMValidator validator;
        ODOMElement rootElement;
        ListenerList listenerList;

        public ValidationJob(DOMValidator validator, ODOMElement rootElement,
                             ListenerList listenerList) {
            super("Validating: " + rootElement
                    .getName()); // todo - should localize the 'Valiating: ' part at least
            this.validator = validator;
            this.rootElement = rootElement;
            this.listenerList = listenerList;
        }

        // javadoc inherited
        protected IStatus run(IProgressMonitor monitor) {
            IWorkspaceRunnable runnable = new IWorkspaceRunnable() {
                public void run(IProgressMonitor iProgressMonitor) {
                    validator.validate(rootElement);
                }
            };

            try {
                ResourcesPlugin.getWorkspace()
                        .run(runnable, null, IResource.NONE, null);
            } catch (CoreException e) {
                EclipseCommonPlugin.handleError(ABPlugin.getDefault(),
                        e);
            }
            fireValidationNotification();
            return Status.OK_STATUS;
        }

        /**
         * Fires the validation notification to all registered listeners.
         */
        private void fireValidationNotification() {
            Object listeners[] = listenerList.getListeners();
            if (listeners != null && listeners.length > 0) {
                for (int i = 0; i < listeners.length; i++) {
                    ((ValidationListener) listeners[i]).validated();
                }
            }
        }

        public void terminateValidation() {
            validator.terminateValidation();
        }
    }


    /**
     * An abstract action class that, when executed,
     * demarcates Undo/Redo UOWs with the UndoRedoManager
     * associated with this EditorContext.
     */
    public abstract class DemarcatingResourceAction extends ResourceAction {
        //javadoc unnecessary
        protected DemarcatingResourceAction(ResourceBundle resourceBundle,
                                            String s) {
            super(resourceBundle, s);
        }


        /**
         * Use of demarcate is liberal.
         * <p/>
         * Executing it before and after the run
         * helps minimizing lost demarcations (e.g. due to focus bugs)
         */
        public final void run() {
            getUndoRedoManager().demarcateUOW();
            runImpl();
            getUndoRedoManager().demarcateUOW();
        }

        //javadoc unnecessary
        protected abstract void runImpl();
    }


    /**
     * A simple implementation of the XMLValidator that wraps a DOMValidator.
     */
    private static class ContextXMLValidator implements XMLValidator {
        /**
         * The list of listeners added to this validator.
         */
        private final ListenerList listenerList = new ListenerList();

        /**
         * The DOMValidator to be delegated to.
         */
        private final DOMValidator validator;

        /**
         * The root ODOMElement that this validator validates.
         */
        private final ODOMElement rootElement;

        /**
         * The ValidationJob that does the validation.
         */
        ValidationJob validationJob;

        /**
         * Initializes the new instance using the given parameters.
         *
         * @param validator   the DOMValidator to be delegated to
         * @param rootElement the ODOMElement to be validated
         */
        public ContextXMLValidator(DOMValidator validator,
                                   ODOMElement rootElement) {
            this.validator = validator;
            this.rootElement = rootElement;
        }

        // javadoc inherited
        public void validate() {
            validationJob = new ValidationJob(validator, rootElement,
                    listenerList); // AMB
            validationJob.setPriority(
                    Job.LONG); // suggest a lower priority for the thread for platforms that support thread priority
            validationJob.schedule();
        }


        // javadoc inherited
        public void addValidationListener(ValidationListener listener) {
            listenerList.add(listener);
        }

        // javadoc inherited
        public void removeValidationListener(ValidationListener listener) {
            listenerList.remove(listener);
        }

        // javadoc inherited
        public void dispose() {
            if (validationJob != null) {
                validationJob.terminateValidation();
            }
        }
    }

    /**
     * Class for managing the construction and disposal of validators that
     * will be activated when a given ODOMElement changes.
     */
    private static class ValidationManager {
        private final AsynchronousXMLValidator xmlValidator;
        private final ContextXMLValidator contextXMLValidator;
        private final ODOMChangeListener validationChangeListener;
        private final DOMValidator domValidator;
        private final MarkerGeneratingErrorReporter errorReporter;

        final ODOMElement rootElement;

        /**
         * Create a DOMValidator for use in ODOMEditorContext construction.
         *
         * @param errorReporter The ErrorReporter to associate with the
         *                      DOMValidator that is created.
         * @return A DOMValidator that is associated with the given ErrorReporter
         *         and uses the Marlin LPDM schema for validation.
         * @throws SAXException         If there was a problem creating the validator.
         * @throws ParserErrorException If there was a problem creating the
         *                              validator.
         */
        protected static DOMValidator
        createDOMValidator(ErrorReporter errorReporter)
                throws SAXException, ParserErrorException {
            // return a XercesBasedDOMValidator that is configured with
            // the correct entity resolver.

            JarFileEntityResolver repositorySchemaResolver =
                    RepositorySchemaResolverFactory.create();

            QueueingXercesBasedDOMValidator validator =
                    new QueueingXercesBasedDOMValidator(
                            repositorySchemaResolver,
                            errorReporter);

            boolean fasterValidation = true; // Default to trueDefault();
            BuilderPlugin builderPlugin = BuilderPlugin.getDefault();
            if (builderPlugin != null) {
                // If there is a preference for fasterValidation then use it
                IPreferenceStore store =
                        BuilderPlugin.getDefault().getPreferenceStore();
                fasterValidation = store.getBoolean(
                        MCSPreferencePage.PREF_FASTER_VALIDATION);
            }
            validator.setFastValidation(fasterValidation);

            return validator;
        }

        /**
         * Construct a new ValidationManager for the given root element.
         *
         * @param rootElement           the root ODOMElement that will be validated when
         *                              changed.
         * @param policyResource        the IResource that the rootElement belongs to.
         * @param rootElementIdentifier a String that is uniquely associated with
         *                              the root element. This String is used for generating resource markers
         *                              and in the case of editors that edit multiple files within the same
         *                              resource is used to identify which file the problem is associated with
         *                              - e.g. the DeviceEditor. Can be null.
         */
        public ValidationManager(ODOMElement rootElement,
                                 IResource policyResource,
                                 String rootElementIdentifier)
                throws SAXException, ParserErrorException {

            this.rootElement = rootElement;

            errorReporter = new MarkerGeneratingErrorReporter(policyResource,
                    rootElement, rootElementIdentifier);

            domValidator =
                    createDOMValidator(errorReporter);

            contextXMLValidator = new ContextXMLValidator(domValidator,
                    rootElement);

            xmlValidator =
                    new AsynchronousXMLValidator(contextXMLValidator);

            validationChangeListener =
                    new ODOMChangeListener() {
                        public void changed(ODOMObservable node,
                                            ODOMChangeEvent event) {
                            validate();
                        }
                    };

            // register the validation listener with the new root element. Note
            // this will be unregistered whenever a new details is set
            rootElement.addChangeListener(validationChangeListener);

            // update the validator so that uses the correct error reporter
            domValidator.setErrorReporter(errorReporter);
        }

        /**
         * Get the MarkerGeneratingErrorReporter associated with this
         * ValidationManager
         */
        public MarkerGeneratingErrorReporter getErrorReporter() {
            return errorReporter;
        }

        /**
         *
         */
        public void dispose() {
            rootElement.removeChangeListener(validationChangeListener);
            contextXMLValidator.dispose();
            xmlValidator.dispose();
        }

        /**
         * Add a ValidationListener to the XMLValidation managed by this
         * ValidationManager.
         *
         * @param listener the ValidationListener
         */
        public void addValidationListener(ValidationListener listener) {
            xmlValidator.addValidationListener(listener);
        }

        /**
         * Remove a ValidationListener from the XMLValidation managed by this
         * ValidationManager.
         *
         * @param listener the ValidationListener
         */
        public void removeValidationListener(ValidationListener listener) {
            xmlValidator.removeValidationListener(listener);
        }

        /**
         * Run the XMLValidator managed by this ValidationManager.
         */
        public void validate() {
            xmlValidator.validate();
        }

        // javadoc inherited
        public synchronized void addSupplementaryValidator(
                DOMSupplementaryValidatorDetails validatorDetails) {

            domValidator.addSupplementaryValidator(
                    validatorDetails.namespaceURI,
                    validatorDetails.elementName,
                    validatorDetails.supplementaryValidator);
        }

        // javadoc inherited
        public synchronized void removeSupplementaryValidator(
                DOMSupplementaryValidatorDetails validatorDetails) {

            domValidator.removeSupplementaryValidator(
                    validatorDetails.namespaceURI,
                    validatorDetails.elementName,
                    validatorDetails.supplementaryValidator);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Nov-05	9896/2	geoff	VBM:2005101906 Avoid using JDOM in MCS at runtime: rewrite runtime XML device repository

 13-Oct-05	9732/1	adrianj	VBM:2005100509 Fixes to Eclipse GUI

 11-Oct-05	9729/1	geoff	VBM:2005100507 Mariner Export fails with NPE

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 17-Nov-04	6012/1	allan	VBM:2004051307 Remove standard elements in admin mode.

 10-Sep-04	5488/1	allan	VBM:2004081803 Fix TAC error messages and validate blank list enrties

 10-Sep-04	5432/6	allan	VBM:2004081803 Fix TAC error messages and validate blank list enrties

 10-Sep-04	5432/4	allan	VBM:2004081803 Fix TAC error messages and validate blank list enrties

 08-Sep-04	5432/2	allan	VBM:2004081803 Validation for range min and max values

 08-Sep-04	5449/1	claire	VBM:2004090809 New Build Mechanism: Remove the use of utilities.UndeclaredThrowableException

 09-Jul-04	4841/1	adrianj	VBM:2004010802 Removal of ODOMValidatable infrastructure

 13-May-04	4321/1	doug	VBM:2004051202 Added label decorating to the device hierarchy tree

 11-May-04	4272/1	allan	VBM:2004050503 Unique problem markers fix for device editor.

 06-May-04	4068/2	allan	VBM:2004032103 Structure page policies section.

 05-May-04	4115/5	allan	VBM:2004042907 Multiple root elements in ODOMEditorContext.

 05-May-04	4115/3	allan	VBM:2004042907 Multiple root elements in ODOMEditorContext.

 27-Apr-04	4016/4	allan	VBM:2004031010 DevicePoliciesPart and CategoriesSection.

 27-Apr-04	4016/2	allan	VBM:2004031010 DevicePoliciesPart and CategoriesSection.

 26-Apr-04	4037/1	doug	VBM:2004042301 Provided mechanism for obtaining an EntityResolver that resolves all MCS repository schemas

 22-Apr-04	3878/5	doug	VBM:2004032405 Created a basic DeviceEditor and overview page

 16-Apr-04	3743/6	doug	VBM:2004032101 Added a DeviceEditorContext class

 17-Mar-04	3346/1	byron	VBM:2004021308 Cannot undo deletion or cutting of single or multiple assets

 23-Feb-04	3057/1	byron	VBM:2004021105 Accelerator keys Ctrl+c and Ctrl+x , Ctrl+v do not work within editors

 17-Feb-04	2988/7	eduardo	VBM:2004020908 undo/redo reafctoring for multi-page editor

 16-Feb-04	2891/3	byron	VBM:2003121508 Eclipse PM Layout Editor: Format Attributes View: Real Estate Management - added comments and renamed methods

 16-Feb-04	2891/1	byron	VBM:2003121508 Eclipse PM Layout Editor: Format Attributes View: Real Estate Management - updated FormatAttributesView hierarchy as per rework item

 13-Feb-04	3023/1	philws	VBM:2004010901 Ensure that the Volantisized XERCES parser is explicitly used for compatibility with Eclipse under JRE 1.4

 12-Feb-04	2924/6	eduardo	VBM:2004021003 codestyle'n'typos fixes

 12-Feb-04	2924/3	eduardo	VBM:2004021003 editor actions demarcate UndoRedo UOWs

 09-Feb-04	2800/5	eduardo	VBM:2004012802 codestyle fixes

 09-Feb-04	2800/3	eduardo	VBM:2004012802 undo redo works from outline view

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 19-Jan-04	2665/1	allan	VBM:2003112702 Provide ThemeEditorContext.

 19-Jan-04	2562/3	allan	VBM:2003112010 ODOMOutlinePage displaying, decorating and tooltipping.

 08-Jan-04	2431/10	allan	VBM:2004010404 Fix validation and display update issues.

 08-Jan-04	2431/8	allan	VBM:2004010404 Fix validation and display update issues.

 08-Jan-04	2431/6	allan	VBM:2004010404 Fix validation and display update issues.

 08-Jan-04	2447/5	philws	VBM:2004010609 Fix UniqueAssetValidator and repackage it

 07-Jan-04	2447/3	philws	VBM:2004010609 Initial code for revised validation mechanism

 07-Jan-04	2426/3	richardc	VBM:2004010607 Refactored registration of UniqueAssetValidator

 06-Jan-04	2323/6	doug	VBM:2003120701 Added better validation error messages

 06-Jan-04	2391/2	byron	VBM:2003121726 Assets can be pasted into components where they are not valid

 05-Jan-04	2380/1	allan	VBM:2004010406 Improve handling of non-well-formed XML in policy files.

 17-Dec-03	2219/6	doug	VBM:2003121502 Added dom validation to the eclipse editors

 15-Dec-03	2213/3	allan	VBM:2003121401 Refactored to reduce the number of resource properties.

 15-Dec-03	2213/1	allan	VBM:2003121401 Refactored to reduce the number of resource properties.

 12-Dec-03	2123/2	allan	VBM:2003102005 Candidate ImageComponentEditor - no validation.

 02-Dec-03	2069/1	allan	VBM:2003111903 Basic ODOMEditorPart completed with skeleton ImageComponentEditor.

 01-Dec-03	2067/5	allan	VBM:2003111911 Rework issues.

 01-Dec-03	2067/3	allan	VBM:2003111911 Rework design making ODOMEditorContext immutable.

 29-Nov-03	2067/1	allan	VBM:2003111911 Create ODOMEditorContext.

 ===========================================================================
*/
