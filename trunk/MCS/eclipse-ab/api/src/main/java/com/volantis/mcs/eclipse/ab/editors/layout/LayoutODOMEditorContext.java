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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.eclipse.ab.editors.layout;

import com.volantis.mcs.eclipse.ab.editors.SaveCommand;
import com.volantis.mcs.eclipse.ab.editors.SaveCommandFactory;
import com.volantis.mcs.eclipse.ab.editors.dom.ODOMEditorContext;
import com.volantis.mcs.eclipse.ab.editors.dom.ODOMEditorUtils;
import com.volantis.mcs.eclipse.ab.editors.dom.validation.ValidationListener;
import com.volantis.mcs.eclipse.common.odom.undo.UndoRedoMementoOriginator;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.common.odom.ODOMChangeListener;
import com.volantis.mcs.xml.validation.sax.ParserErrorException;
import com.volantis.mcs.xml.validation.DOMSupplementaryValidatorDetails;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.jdom.JDOMException;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Iterator;

/**
 * ODOMEditorContext for layouts. Redefines the save command factory.
 */
public class LayoutODOMEditorContext extends ODOMEditorContext {
    private final LayoutEditorContext editorContext;

    public LayoutODOMEditorContext(final LayoutEditorContext editorContext,
                final IFile layoutFile,
                final UndoRedoMementoOriginator undoRedoMementoOriginator)
            throws SAXException, ParserErrorException, IOException,
                CoreException, JDOMException {
        super(layoutFile, undoRedoMementoOriginator,
            ODOMEditorUtils.createRootElement(layoutFile, ODOM_FACTORY),
            null);
        this.editorContext = editorContext;
    }

    // Javadoc inherited
    public synchronized void addRootElement(ODOMElement rootElement,
                                            String rootElementIdentifier)
            throws SAXException, ParserErrorException {
        // This method is overridden so that we can avoid setting up any
        // validation for the new root element (for layouts we handle all
        // validation through the model). This is somewhat hacky, but is only
        // a temporary measure until the new layout editor comes in.
        addRootElementWithoutValidation(rootElement, rootElementIdentifier);
    }

    public LayoutEditorContext getLayoutEditorContext() {
        return editorContext;
    }

    // javadoc inherited
    protected SaveCommandFactory createSaveCommandFactory() {
        return new ODOMEditorSaveCommandFactory();
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
                    editorContext.saveFile(null, progressMonitor);
                }
            };
        }

        // javadoc inherited
        public SaveCommand createSaveAsCommand(final IFile destinationFile) {
            return new SaveCommand() {
                // javadoc inherited
                public void save(IProgressMonitor progressMonitor)
                        throws CoreException {
                    editorContext.saveFile(destinationFile, progressMonitor);
                }
            };
        }
    }

}
