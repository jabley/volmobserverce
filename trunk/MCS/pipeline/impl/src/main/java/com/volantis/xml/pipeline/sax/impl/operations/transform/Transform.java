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

import com.volantis.xml.pipeline.sax.XMLProcessingException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This state represents the transformation state when an tramsform
 * element is encountered. This state can move to either the
 * transforming state (if an href attribute is provided), the
 * transformations state (if the next element is a transformations) or
 * the transformation state (if the next element is a transformation)
 */
public class Transform extends TransformationState {

    public Transform(TransformAdapterProcess transformAdapterProcess) {
        super(transformAdapterProcess);
    }

    // javadoc inherited
    public void initialise(String namespaceURI, String localName,
                           String qName, Attributes atts)
            throws SAXException {

        String sCompilable = atts.getValue(COMPILABLE_ATTRIBUTE);
        Boolean compilable = null;
        if (null != sCompilable) {
            compilable = Boolean.valueOf(sCompilable);
        }

        // Store the current value of the compilable attribute so nested
        // transformation elements can refer to it.
        operationProcess.pushCompilableAttribute(compilable);

        String href = atts.getValue(HREF_ATTRIBUTE);
        if (null != href) {
            // the href identifies the XSL to use so instruct the operation
            // process to add the appropriate template
            operationProcess.addTemplate(href, compilable, false);
            // we are ready to transform so enter the transforming state
            adapterProcess.changeState(
                    new Transforming(adapterProcess),
                    namespaceURI, localName, qName, atts);
        }
    }

    // javadoc inherited
    public void startElement(String namespaceURI, String localName,
                             String qName, Attributes atts)
            throws SAXException {
        if (PARAMETERS_ELEMENT.equals(localName) &&
                namespaceURI.equals(adapterProcess.getProcessNamespaceURI())) {
            // Check that no templates have been added yet.  The parameters
            // element must be the first child of the transform element.
            if (operationProcess.getTemplateCount() > 0) {
                XMLProcessingException xpe = new XMLProcessingException(
                        "The parameters element must be the first child of " +
                        "the transform element.", adapterProcess.
                                                  getPipelineContext().getCurrentLocator());
                // report the error down the pipeline.
                fatalError(xpe);
            }
            adapterProcess.changeState(new Parameters(adapterProcess),
                                       namespaceURI, localName, qName, atts);
        } else if (TRANSFORMATION_ELEMENT.equals(localName) &&
                namespaceURI.equals(adapterProcess.getProcessNamespaceURI())) {

            // ok we have a transformation element directly inside a
            // transform element. We must ensure that no other
            // transformation has been requested (if so a transformations
            // element should have been the parent.
            if (operationProcess.getTemplateCount() > 0) {
                XMLProcessingException xpe = new XMLProcessingException(
                        "Transform elements can at most contain one " +
                        "transformation element", adapterProcess.
                                                  getPipelineContext().getCurrentLocator());
                // report the error down the pipeline.
                fatalError(xpe);
            }
            // ok so move into the transformation state
            adapterProcess.changeState(
                    new Transformation(adapterProcess),
                    namespaceURI, localName, qName, atts);

        } else if (TRANSFORMATIONS_ELEMENT.equals(localName) &&
                namespaceURI.equals(adapterProcess.getProcessNamespaceURI())) {
            // ok transfomations element move to the corresponding state
            adapterProcess.changeState(
                    new Transformations(adapterProcess),
                    namespaceURI, localName, qName, atts);
        } else {
            // ok this is an element inside a transform that is not known
            // it must be the content that is to be transformed.
            // Proceed to the transforming stat
            TransformationState transforming =
                    new Transforming(adapterProcess);
            adapterProcess.changeState(transforming,
                                       namespaceURI, localName, qName, atts);

            // this element that is being processed will is part of
            // the content to be transformed so pass it on.
            transforming.startElement(namespaceURI, localName, qName, atts);

        }
    }

    // javadoc inherited
    public void endElement(String namespaceURI, String localName, String qName)
            throws SAXException {
        // not really needed
        adapterProcess.popState(this);

        // Restore the value of the compilable attribute for the outer
        // transformation element.
        operationProcess.popCompilableAttribute();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 30-Apr-04	686/2	adrian	VBM:2004042802 Add parameter support for transforms

 28-Apr-04	683/3	adrian	VBM:2004042607 Added copyright statements

 27-Apr-04	683/1	adrian	VBM:2004042607 Refactored states out of transform adapter process

 ===========================================================================
*/
