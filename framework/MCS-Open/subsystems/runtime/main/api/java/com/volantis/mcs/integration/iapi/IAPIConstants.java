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

/**
 * This interface defines some constants which are used by both IAPI classes
 * and users of IAPI.
 * 
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * 
 * @todo later refactor this to extract commonality with PAPI
*/
public interface IAPIConstants {

    /**
     * The Volantis copyright statement
     */
    static final String mark =
            "(c) Volantis Systems Ltd 2003. ";

  /**
   * Returned by {@link com.volantis.mcs.integration.iapi.IAPIElement#elementStart} if the caller should
   * process the element's body (contents).
   */
  public static final int PROCESS_ELEMENT_BODY = 101;

  /**
   * Returned by {@link com.volantis.mcs.integration.iapi.IAPIElement#elementStart} if the caller should
   * NOT process the element's body (contents).
   */
  public static final int SKIP_ELEMENT_BODY = 100;

  /**
   * Returned by {@link com.volantis.mcs.integration.iapi.IAPIElement#elementEnd} if the caller should
   * continue processing the rest of the page (document).
   */
  public static final int CONTINUE_PROCESSING = 102;

  /**
   * Returned by {@link com.volantis.mcs.integration.iapi.IAPIElement#elementEnd} if the caller should
   * abort the processing the rest of the page (document).
   */
  public static final int ABORT_PROCESSING = 103;
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
