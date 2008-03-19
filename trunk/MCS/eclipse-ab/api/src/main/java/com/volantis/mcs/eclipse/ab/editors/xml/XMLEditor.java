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
package com.volantis.mcs.eclipse.ab.editors.xml;

import com.volantis.mcs.eclipse.ab.ABPlugin;
import com.volantis.mcs.eclipse.ab.editors.dom.validation.AsynchronousXMLValidator;
import com.volantis.mcs.eclipse.ab.editors.dom.validation.ValidationListener;
import com.volantis.mcs.eclipse.ab.editors.dom.validation.XMLValidator;
import com.volantis.mcs.eclipse.ab.editors.xml.schema.SchemaDefinition;
import com.volantis.mcs.eclipse.ab.editors.xml.schema.SimpleSchemaParser;
import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.eclipse.common.PolicyUtils;
import com.volantis.mcs.eclipse.common.RepositorySchemaResolverFactory;
import com.volantis.mcs.repository.xml.PolicySchemas;
import com.volantis.xml.schema.JarFileEntityResolver;
import com.volantis.synergetics.UndeclaredThrowableException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.source.IAnnotationHover;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.util.ListenerList;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.views.tasklist.TaskList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;

/**
 * An XML source editor with content assist and validation based on an
 * xsd.
 *
 * DISCLAIMER: This class and its associated classes are a quick fix built to
 * provide the ability to edit themes and layouts without the Design
 * parts. As such there are more likely to be bugs, bits missing and
 * bits that could be better designed.
 */
public class XMLEditor extends TextEditor {

    /**
     * The ColorManager that manages the colour of the text.
     */
    private ColorManager colorManager;

    /**
     * The SchemaDefinition that provides the schema for the xml that this
     * editor edits.
     */
    private SchemaDefinition schema;

    /**
     * The EntityResolver for this XMLEditor.
     */
    private JarFileEntityResolver resolver;

    /**
     * The validator for this editor.
     */
    private XMLEditorValidator validator;

    private AsynchronousXMLValidator asyncValidator;

    /**
     * Flag that allows dirty status handling to be enabled/disabled.
     */
    private boolean dirtyStatusEnabled = true;

    /**
     * Construct a new XMLEditor.
     */
    public XMLEditor() {
        this.colorManager = new ColorManager();
        this.schema = new SchemaDefinition();
        resolver = RepositorySchemaResolverFactory.create();

        populateSchemaDefinition();
        setDocumentProvider(new XMLDocumentProvider());
    }

    /**
     * Override dispose() to stop the validator and dispose of the
     * ColorManager.
     */
    // rest of javadoc inherited
    public void dispose() {
        validator.dispose();

        if (asyncValidator != null) {
            asyncValidator.dispose();
        }

        colorManager.dispose();
        super.dispose();
    }


    // javadoc inherited
    public void init(IEditorSite editorSite, IEditorInput editorInput)
            throws PartInitException {

        super.init(editorSite, editorInput);

        IDocument document = getDocumentProvider().
                getDocument(getEditorInput());
        final IFile file = ((IFileEditorInput) getEditorInput()).getFile();
        XMLDocumentValidator xmlDocumentValidator =
                new XMLDocumentValidator(file, resolver, 
                        RepositorySchemaResolverFactory.GUI_SCHEMATA);

        this.validator = new XMLEditorValidator(xmlDocumentValidator,
                document);

        xmlDocumentValidator.validate(document);

        this.asyncValidator = new AsynchronousXMLValidator(this.validator);

        // Create the IAnnotationHover for this XMLEditor.
        // This IAnnotationHover uses PolicyUtils.findProblemMarkers() with
        // the line number to build up the hover text from the messages
        // of all the problem markers for the specified line number.
        // Note that the lineNumber given to getHoverInfo() is 1 less than
        // the line known to the marker or shown in the line number rule in
        // the editor. Presumably this is because whomever calls
        // getHoverInfo() thinks that line numbers start from 0.
        IAnnotationHover annotationHover = new IAnnotationHover() {
            public String getHoverInfo(ISourceViewer iSourceViewer,
                                       int lineNumber) {
                String hoverInfo = null;

                try {
                    IMarker markers [] =
                            PolicyUtils.findProblemMarkers(file, lineNumber + 1);
                    if (markers.length > 0) {
                        StringBuffer buffer = new StringBuffer();
                        for (int i = 0; i < markers.length; i++) {
                            buffer.append(markers[i].
                                    getAttribute(IMarker.MESSAGE));
                            if (i < markers.length - 1) {
                                buffer.append('\n');
                            }
                        }
                        hoverInfo = buffer.toString();
                    }
                } catch (CoreException e) {
                    EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
                }
                return hoverInfo;
            }
        };

        setSourceViewerConfiguration(
                new XMLConfiguration(colorManager, schema, annotationHover));
    }

    /**
     * Override createPartControl() to add a MouseListener to the
     * IVerticalRuler so that we can select the markers in the task view
     * associated with markers in the IVerticalRuler when the user clicks
     * on a marker in the IVerticalRuler.
     *
     * NOTE: There may be a more appropriate way of doing this but I can't
     * find any documentations telling me how. I thought this behaviour
     * would be provided by default from by the TextEditor and its default
     * supporting classes.
     */
    // rest of javadoc inherited
    public void createPartControl(Composite parent) {
        super.createPartControl(parent);
        final IVerticalRuler rule = getVerticalRuler();
        Control control = rule.getControl();
        control.addMouseListener(new MouseListener() {

            public void mouseDoubleClick(MouseEvent event) {
                mouseUp(event);
            }

            public void mouseDown(MouseEvent event) {
            }

            public void mouseUp(MouseEvent event) {
                int line = rule.getLineOfLastMouseButtonActivity();
                IFileEditorInput input = (IFileEditorInput) getEditorInput();
                IResource resource = input.getFile();
                try {
                    final IMarker markers [] =
                            PolicyUtils.findProblemMarkers(resource, line + 1);

                    // Find the TaskList view. Show it (which automatically
                    // makes it the active view), and set the selection
                    // of tasks to the markers associated with this
                    // AlertsActionsSection.
                    IWorkbenchPage activePage =
                            PlatformUI.getWorkbench().
                            getActiveWorkbenchWindow().
                            getActivePage();
                    IViewPart view =
                            activePage.
                            showView("org.eclipse.ui.views.TaskList");
                    final TaskList tasklist = (TaskList) view;
                    Display.getCurrent().asyncExec(new Runnable() {
                        public void run() {
                            tasklist.
                                    setSelection(new StructuredSelection(markers),
                                            true);
                        }
                    });
                } catch (CoreException e) {
                    EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
                }
            }
        });
    }

    /**
     * This method is used to populate the schema definition from the LPDM
     * schema.
     */
    protected void populateSchemaDefinition() {
        try {
            InputSource inputSource = resolver.resolveEntity(null,
                    PolicySchemas.MARLIN_LPDM_CURRENT.getLocationURL());
            new SimpleSchemaParser(schema).parse(inputSource.getSystemId());
        } catch (SAXException e) {
            throw new UndeclaredThrowableException(e,
                    "There was a problem parsing the schema (" +
                    e.getMessage() + ")");
        } catch (IOException e) {
            throw new UndeclaredThrowableException(e,
                    "There was a problem accessing the schema (" +
                    e.getMessage() + ")");
        }
    }

    /**
     * Override firePropertyChange so that we can disable dirty state changing
     * if necessary (e.g. for refresh).
     * @param property The property whose state is changing.
     */
    protected void firePropertyChange(int property) {
        if (property != PROP_DIRTY || dirtyStatusEnabled) {
            super.firePropertyChange(property);
        }
    }

    /**
     * A simple implementation of the XMLValidator that wraps an
     * XMLDocumentValidator.
     */
    private class XMLEditorValidator implements XMLValidator {
        /**
         * The list of listeners added to this validator.
         */
        private final ListenerList listenerList = new ListenerList();

        /**
         * The DocumentListener for this validator.
         */
        private DocumentListener listener = new DocumentListener();

        /**
         * The underlying XMLDocumentValidator for this validator.
         */
        private final XMLDocumentValidator validator;

        /**
         * The original document provided to this validator. This document
         * is listened to and is in reality the document that is validated.
         */
        private final IDocument origDocument;

        /**
         * The "new" document that needs validation.
         */
        private IDocument document;

        /**
         * Construct a new XMLEditorValidator.
         * @param validator The XMLDocumentValidator to delegate validation
         * to.
         * @param document The docuement that validation is based on and
         * listeners listen for changes on.
         */
        public XMLEditorValidator(XMLDocumentValidator validator,
                                  IDocument document) {
            this.validator = validator;
            this.origDocument = document;
            this.document = null;

            document.addDocumentListener(listener);
        }

        /**
         * Fires the validation notification to all registered listeners.
         */
        private void fireValidationNotification() {
            Object listeners [] = listenerList.getListeners();
            if (listeners != null && listeners.length > 0) {
                for (int i = 0; i < listeners.length; i++) {
                    ((ValidationListener) listeners[0]).validated();
                }
            }
        }

        // javadoc inherited
        public void validate() {
            Runnable runnable = new Runnable() {
                // javadoc inherited
                public void run() {
                    validator.validate(document);
                    fireValidationNotification();
                }
            };

            new Thread(runnable).start();
        }

        /**
         * Remove references used by this validator and discontinue it
         * from running.
         */
        public void dispose() {
            origDocument.removeDocumentListener(listener);
        }

        // javadoc inherited
        public void addValidationListener(ValidationListener listener) {
            listenerList.add(listener);
        }

        // javadoc inherited
        public void removeValidationListener(ValidationListener listener) {
            listenerList.remove(listener);
        }

        /**
         * A document listener for this validator.
         */
        private class DocumentListener implements IDocumentListener {
            public void documentAboutToBeChanged(DocumentEvent event) {
            }

            public void documentChanged(DocumentEvent event) {
                document = event.getDocument();
                XMLEditor.this.asyncValidator.validate();
            }
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Nov-05	9896/1	geoff	VBM:2005101906 Avoid using JDOM in MCS at runtime: rewrite runtime XML device repository

 13-Oct-05	9732/1	adrianj	VBM:2005100509 Fixes to Eclipse GUI

 11-Oct-05	9729/2	geoff	VBM:2005100507 Mariner Export fails with NPE

 21-Jul-05	8713/1	adrianj	VBM:2005060902 Enhanced GUI for creating new selectors

 21-Mar-05	7457/1	philws	VBM:2005030811 Allow the MCS Source editor to work again on existing LPDM file types

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 17-Nov-04	6012/1	allan	VBM:2004051307 Remove standard elements in admin mode.

 18-May-04	4410/1	byron	VBM:2004051307 Device Editor Administrator mode proliferates standard elements

 05-May-04	4115/1	allan	VBM:2004042907 Multiple root elements in ODOMEditorContext.

 26-Apr-04	4037/2	doug	VBM:2004042301 Provided mechanism for obtaining an EntityResolver that resolves all MCS repository schemas

 12-Feb-04	2789/3	tony	VBM:2004012601 Localised logging (and exceptions)

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 09-Jan-04	2515/1	allan	VBM:2004010513 Add hover annotation and mouse click handling to rulers.

 08-Jan-04	2447/3	philws	VBM:2004010609 Added constructor javadoc

 08-Jan-04	2431/6	allan	VBM:2004010404 Fix validation and display update issues.

 08-Jan-04	2431/4	allan	VBM:2004010404 Fix validation and display update issues.

 07-Jan-04	2447/1	philws	VBM:2004010609 Initial code for revised validation mechanism

 06-Jan-04	2412/1	allan	VBM:2004010407 Fixed dirty status handling when switching editor page.

 05-Jan-04	2380/1	allan	VBM:2004010406 Improve handling of non-well-formed XML in policy files.

 04-Jan-04	2309/3	allan	VBM:2003122202 Provide an MCS source editor for multi-page and stand-alone policy editing.

 ===========================================================================
*/
