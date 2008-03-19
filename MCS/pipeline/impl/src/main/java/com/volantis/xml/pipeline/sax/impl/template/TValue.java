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
package com.volantis.xml.pipeline.sax.impl.template;

import com.volantis.xml.pipeline.sax.XMLPipeline;
import org.xml.sax.SAXException;

import java.io.Serializable;

/**
 * Represents a named parameter for which a value can be substituted. The value
 * can be inserted into an {@link com.volantis.xml.pipeline.sax.XMLPipeline} implementation as a
 * sequence of SAX events via the {@link #insert} method.
 * <p/>
 * When a parameter has been verified as correct, the {@link #verify} method
 * should be invoked.
 */
public interface TValue
        extends Serializable {

    /**
     * Get the complexity of the value, this is the complexity that it was
     * declared to have, not the actual complexity of the value as that can
     * only be determined once it has been evaluated.
     *
     * @return The complexity.
     */
    Complexity getComplexity();

    /**
     * Indicates whether the value requires an external dynamic pipeline in
     * order to be processed correctly.
     *
     * @return True if it does need one, false otherwise.
     */
    boolean requiresDynamicPipeline();

    /**
     * Should be called to indicate that the value has been verified.
     */
    void verify();

    /**
     * Returns true if {@link #verify} has been invoked one or more times
     * for this value.
     *
     * @return true if the value has been verfied
     */
    boolean isVerified();

    /**
     * Requests that the parameter's value is inserted into the given target
     * process as a set of appropriate SAX events.
     *
     * @param target    the pipeline to which the event should be targetted
     * @param parameter
     */
    void insert(XMLPipeline target, String parameter) throws SAXException;
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 31-Jul-03	222/5	philws	VBM:2003071802 New pipeline API and implementation of the equals and not equals expression feature

 31-Jul-03	222/3	philws	VBM:2003071802 New pipeline API and implementation of the equals and not equals expression feature

 10-Jun-03	13/2	philws	VBM:2003030610 Integrate with Template Model Expression facilities

 ===========================================================================
*/
