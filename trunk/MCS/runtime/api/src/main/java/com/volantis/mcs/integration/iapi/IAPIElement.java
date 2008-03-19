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
 
package com.volantis.mcs.integration.iapi;

import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.integration.iapi.IAPIAttributes;
import com.volantis.mcs.integration.iapi.IAPIConstants;

/**
 * This interface defines the methods which must be implemented by all
 * IAPI elements.
 * 
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * 
 * @todo later refactor this to extract commonality with PAPI
 */
public interface IAPIElement extends IAPIConstants {
    
    /**
     * The Volantis copyright statement
     */
    static final String mark =
            "(c) Volantis Systems Ltd 2003. ";
    
    /**
     * Called at the start of an IAPI element.
     * @param context The MarinerRequestContext within which this element is
     * being processed.
     * @param iapiAttributes The implementation of IAPIAttributes which
     * contains the attributes specific to the implementation of IAPIElement.
     * @return PROCESS_ELEMENT_BODY or SKIP_ELEMENT_BODY.
     * @throws com.volantis.mcs.integration.iapi.IAPIException If there was a problem processing the element.
     */
    public int elementStart(MarinerRequestContext context,
                            IAPIAttributes iapiAttributes)
            throws IAPIException;

    /**
     * Called at the end of an IAPI element.
     * <p>
     * If the {@link #elementStart} method was called then this method will 
     * also be called unless an Exception occurred during the processing of the
     * body.
     * </p>
     * @param context The MarinerRequestContext within which this element is
     * being processed.
     * @param iapiAttributes The implementation of IAPIAttributes which
     * contains the attributes specific to the implementation of IAPIElement.
     * @return CONTINUE_PROCESSING or ABORT_PROCESSING.
     * @throws com.volantis.mcs.integration.iapi.IAPIException If there was a problem processing the element.
     */
    public int elementEnd(MarinerRequestContext context,
                          IAPIAttributes iapiAttributes)
            throws IAPIException;

    /**
     * Resets the internal state so it is equivalent (not necessarily 
     * identical) to that of a newly created instance.
     * <p>
     * This method must be called after {@link #elementEnd} in order to 
     * properly clean up any resources held by this object.
     * </p>
     * <p>
     * This method allows the user to reuse IAPI element instances, although 
     * they don't have to. After this method has been called it is safe to call
     * the elementStart method again.
     * </p>
     * @param context The MarinerRequestContext within which this element is
     * being processed.
     */
    public void elementReset(MarinerRequestContext context);            
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 16-Jul-03	757/1	adrian	VBM:2003070706 Added IAPI, MarkupPlugin and configuration.

 ===========================================================================
*/
