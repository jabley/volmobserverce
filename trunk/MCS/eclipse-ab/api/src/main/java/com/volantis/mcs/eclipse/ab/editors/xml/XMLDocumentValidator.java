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

import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.eclipse.controls.ControlsPlugin;
import com.volantis.mcs.xml.validation.DOMValidator;
import com.volantis.synergetics.UndeclaredThrowableException;
import com.volantis.xml.schema.Schemata;
import com.volantis.xml.xerces.parsers.SAXParser;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.IDocument;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.StringReader;

/**
 * A class that validates entire XML documents that are associated with an
 * IFile.
 *
 * DISCLAIMER: This class and its associated classes are a quick fix built to
 * provide the ability to edit themes and layouts without the Design
 * parts. As such there are more likely to be bugs, bits missing and
 * bits that could be better designed.
 */
public class XMLDocumentValidator {
    /**
     * The IIfile associated with this XMLDocumentValidator.
     */
    private final IFile file;

    /**
     * The parser for this validator.
     */
    private XMLReader xmlReader;

    /**
     * The ValidationHandler associated with this XMLDocumentValidator.
     */
    private final ValidationHandler validationHandler =
            new ValidationHandler();

    /**
     * Construct an new XMLDocumentValidator associated.
     * @param file The IStorage whose content will be validated and
     * that will possibly have markers created/removed against it as a result
     * @param schemata
     */
    public XMLDocumentValidator(
            IFile file, EntityResolver entityResolver, Schemata schemata) {

        try {
            this.file = file;

            // create the XMLReader instance
            xmlReader = new SAXParser();
            xmlReader.setContentHandler(validationHandler);
            xmlReader.setErrorHandler(validationHandler);
            xmlReader.setEntityResolver(entityResolver);

            // set up the features that we need
            xmlReader.setFeature("http://xml.org/sax/features/namespaces", //$NON-NLS-1$
                    true);
            xmlReader.setFeature("http://xml.org/sax/features/validation", //$NON-NLS-1$
                    true);
            xmlReader.setFeature("http://apache.org/xml/features/validation/schema", //$NON-NLS-1$
                    true);

            xmlReader.setProperty(
                    "http://apache.org/xml/properties/schema/external-schemaLocation",
                    schemata.getXSISchemaLocation());

        } catch (SAXException e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    /**
     * Validate a document given document and update the markers on the
     * IStorage of this XMLDocumentValidator accordingly.
     * */
    public void validate(final IDocument document) {
        IWorkspaceRunnable runnable = new IWorkspaceRunnable() {
            public void run(IProgressMonitor iProgressMonitor) {
                try {
                    removeOldMarkers();
                    String contents = document.get();
                    StringReader reader = new StringReader(contents);
                    InputSource is = new InputSource(reader);
                    xmlReader.parse(is);
                } catch (SAXException e) {
                    // Again this exception is likely to occur if the XML is badly
                    // formed. Add a marker that highlights what has gone wrong.
                    addMarkerForException(e);
                } catch (IOException e) {
                    EclipseCommonPlugin.handleError(ControlsPlugin.getDefault(),
                            e);
                }
            }
        };

        try {
            ResourcesPlugin.getWorkspace().run(runnable, null);
        } catch (CoreException e) {
            EclipseCommonPlugin.handleError(ControlsPlugin.getDefault(), e);
        }


    }

    /**
     * Remove all the markers on the IFile that have been added by this
     * XMLDocumentValidator.
     */
    private void removeOldMarkers() {
        try {
            IMarker[] markers = file.findMarkers(IMarker.PROBLEM, true,
                    IResource.DEPTH_INFINITE);
            for (int i = 0; i < markers.length; i++) {
                if (markers[i].getAttribute(DOMValidator.class.getName(),
                        false)) {
                    markers[i].delete();
                }
            }
        } catch (CoreException e) {
            e.printStackTrace();
            EclipseCommonPlugin.handleError(ControlsPlugin.getDefault(),
                    e);

        }
    }

    /**
     * Adds an {@link org.eclipse.core.resources.IMarker} for the given Exception against the specified
     * {@link org.eclipse.core.resources.IResource}. The markers message will be the message that the
     * {@link java.lang.Exception#getMessage} method returns.
     * @param exception the exception that the marker is being created for.
     */
    private void addMarkerForException(Exception exception) {
        try {
            String message = exception.getMessage();
            IMarker marker = file.createMarker(IMarker.PROBLEM);
            marker.setAttribute(IMarker.MESSAGE, message);
            marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
            marker.setAttribute(DOMValidator.class.getName(), true);
            Locator locator = validationHandler.getDocumentLocator();
            if (locator != null) {
                int lineNumber =
                        validationHandler.getDocumentLocator().getLineNumber();
                marker.setAttribute(IMarker.LINE_NUMBER, lineNumber);
            }
            marker.setAttribute(IMarker.LOCATION, file.getName());

        } catch (CoreException e) {
            e.printStackTrace();
            EclipseCommonPlugin.handleError(
                    ControlsPlugin.getDefault(), e);
        }
    }

    /**
     * A content and error handler for validation.
     */
    private class ValidationHandler extends DefaultHandler
            implements ErrorHandler {
        private Locator locator;
        /**
         * Override setDocumentLocator() to allow us to make the Locator
         * available externally.
         */
        // rest of javadoc inherited
        public void setDocumentLocator(Locator locator) {
            this.locator = locator;
        }

        /**
         * @return The Locator associated with this ValidationHandler.
         */
        public Locator getDocumentLocator() {
            return locator;
        }

        // ErrorHandler interface method - javadoc inherited
        public void warning(SAXParseException e) throws SAXException {
        }

        // ErrorHandler interface method - javadoc inherited
        public void error(SAXParseException e) throws SAXException {
            addMarkerForException(e);
        }

        // ErrorHandler interface method - javadoc inherited
        public void fatalError(SAXParseException e) throws SAXException {
        }

    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 08-Jan-04	2431/2	allan	VBM:2004010404 Fix validation and display update issues.

 04-Jan-04	2309/3	allan	VBM:2003122202 Provide an MCS source editor for multi-page and stand-alone policy editing.

 ===========================================================================
*/
