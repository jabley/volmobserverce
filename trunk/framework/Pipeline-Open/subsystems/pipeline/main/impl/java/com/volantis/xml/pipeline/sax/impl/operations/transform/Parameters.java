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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.pipeline.sax.impl.operations.transform;

import com.volantis.xml.sax.ExtendedSAXParseException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This state represents the state encountered when a parameters
 * element has been encountered. This element has no attributes and can
 * only be moved to the parameter state via the processing of child
 * parameter element
 */
public class Parameters extends TransformationState {

    public Parameters(TransformAdapterProcess transformAdapterProcess) {
        super(transformAdapterProcess);
    }

    // javadoc inherited
    public void initialise(String namespaceURI, String localName,
                           String qName, Attributes atts) {
        // nothing to do
    }

    // javadoc inherited
    public void startElement(String namespaceURI, String localName,
                             String qName, Attributes atts)
            throws SAXException {

        // Look for child parameter elements.  These are the only valid child
        // elements for the parameters element.  As such we send an error down
        // the pipeline if we find any other elements.
        if (PARAMETER_ELEMENT.equals(localName) &&
                namespaceURI.equals(adapterProcess.getProcessNamespaceURI())) {
            // transformation element encountered. Move to the
            // corresponding state
            adapterProcess.changeState(new Parameter(adapterProcess),
                                       namespaceURI, localName, qName, atts);
        } else {
            // can only contain transformation elements
            fatalError(new ExtendedSAXParseException(
                    "The parameters can only contain parameter elements",
                    adapterProcess.getPipelineContext().getCurrentLocator()));
        }
    }

    // javadoc inherited
    public void endElement(String namespaceURI, String localName, String qName)
            throws SAXException {
        // Check if we see our own closing parameters element and remove this
        // instance from the adapter process.  Since only child parameter
        // elements are allowed and they are responsible for handling their
        // own end element events we should only ever see our own end element
        // event.  If we see any other we throw an exception.
        if (PARAMETERS_ELEMENT.equals(localName) &&
                namespaceURI.equals(adapterProcess.getProcessNamespaceURI())) {
            adapterProcess.popState(this);
        } else {
            // can only contain transformation elements
            fatalError(new ExtendedSAXParseException(
                    "The parameters can only contain parameter elements",
                    adapterProcess.getPipelineContext().getCurrentLocator()));
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 04-Mar-05	7294/1	geoff	VBM:2005022311 Remote Repository Exceptions

 04-Mar-05	7247/1	geoff	VBM:2005022311 Remote Repository Exceptions

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 30-Apr-04	686/3	adrian	VBM:2004042802 Fixed some rework issues with transform parameters

 30-Apr-04	686/1	adrian	VBM:2004042802 Add parameter support for transforms

 ===========================================================================
*/
