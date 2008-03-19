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

import com.volantis.xml.pipeline.sax.StreamingExceptionContentHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Base class for all state based content handlers. Extends the
 * StreamingExceptionContentHandler class as most types of content
 * should be dissallowed
 */
public abstract class TransformationState
        extends StreamingExceptionContentHandler {

    /**
     * Constant that identifies href attribute name
     */
    static final String HREF_ATTRIBUTE = "href";

    /**
     * Constant that identifies compilable attribute name
     */
    static final String COMPILABLE_ATTRIBUTE = "compilable";

    /**
     * Constant that identifies the transformations element
     */
    static final String TRANSFORMATIONS_ELEMENT = "transformations";

    /**
     * Constant that identifies the transformation element
     */
    static final String TRANSFORMATION_ELEMENT = "transformation";

    /**
     * Constant that identifies the parameter element
     */
    static final String PARAMETER_ELEMENT = "parameter";

    /**
     * Constant that identifies the parameters element
     */
    static final String PARAMETERS_ELEMENT = "parameters";

    /**
     * The TransformAdapterProcess for which we will act as the ContentHandler
     */
    protected TransformAdapterProcess adapterProcess;

    /**
     * The TransformOperationProcess which specialisations may configure and
     * delegate to.
     */
    protected TransformOperationProcess operationProcess;

    /**
     * Construct a new instance of TransformationState
     * @param transformAdapterProcess The TransformAdapterProcess for which we
     * will act as the ContentHandler
     */
    public TransformationState(TransformAdapterProcess transformAdapterProcess) {
        super(transformAdapterProcess.getPipelineContext(), transformAdapterProcess.getNextProcess());
        this.adapterProcess = transformAdapterProcess;
        this.operationProcess = adapterProcess.operation;
    }

    /**
     * Initialise this state
     * @param namespaceURI the namespace URI of the associated element
     * @param localName the localName of the associated element
     * @param qName the qualified name of the associated element
     * @param atts the Attributes of the associated element.
     * @throws SAXException if an error occurs.
     */
    public abstract void initialise(String namespaceURI, String localName,
                                    String qName, Attributes atts)
            throws SAXException;

    // javadoc inherited
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        // override this method and quietly consume
    }

    // javadoc inherited
    public void endPrefixMapping(String prefix) throws SAXException {
        // override this method and quietly consume
    }

    // javadoc inherited
    public void startDocument() throws SAXException {
        // override this method and quietly consume
    }

    // javadoc inherited
    public void endDocument() throws SAXException {
        // override this method and quietly consume
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
