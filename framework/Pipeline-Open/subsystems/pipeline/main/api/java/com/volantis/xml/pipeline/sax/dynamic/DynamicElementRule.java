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

package com.volantis.xml.pipeline.sax.dynamic;

import com.volantis.xml.namespace.ExpandedName;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Specifies the behaviour of pipeline elements when processed by a dynamic
 * process.
 *
 * <h2>Error Recovery</h2>
 * <p>Rules will play no part in the recovery of errors within the pipeline.
 * This is because they rely heavily on the pipeline context and the pipeline
 * being in a consistent state which is almost certainly not the case when an
 * error occurs.</p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public interface DynamicElementRule {

    /**
     * The volantis copyright statement
     */
    String mark = "(c) Volantis Systems Ltd 2003. ";

    /**
     * Invoked by the dynamic process when it receives a
     * {@link com.volantis.xml.pipeline.sax.XMLProcess#startElement} event for
     * the pipeline element(s) that this rule has been registered.
     * <p>The objects that are passed in (attributes and element name) must not
     * be used outside this method.</p>
     * @param dynamicProcess The DynamicProcess that has invoked this rule.
     * @param element The name of the element that the rule was invoked for.
     * @param attributes The attributes of the element that the rule was invoked
     * for.
     * @return An object that is passed to the matching endElement, may be null.
     * @throws SAXException
     */
    public Object startElement(DynamicProcess dynamicProcess,
                               ExpandedName element,
                               Attributes attributes)
            throws SAXException;

    /**
     * Invoked by a dynamic process when it receives an
     * {@link com.volantis.xml.pipeline.sax.XMLProcess#endElement} event for
     * the pipeline element(s) that this rule has been registered.
     * <p>The objects that are passed in (attributes and element name) must not
     * be used outside this method.</p>
     * @param dynamicProcess The DynamicProcess that has invoked this rule.
     * @param element The name of the element that the rule was invoked for.
     * @param object The object that was returned by the matching
     * {@link #startElement} method.
     */
    public void endElement(DynamicProcess dynamicProcess,
                           ExpandedName element,
                           Object object)
            throws SAXException;

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 16-Jul-03	208/1	doug	VBM:2003071603 Added as part of Pipeline public API

 ===========================================================================
*/
