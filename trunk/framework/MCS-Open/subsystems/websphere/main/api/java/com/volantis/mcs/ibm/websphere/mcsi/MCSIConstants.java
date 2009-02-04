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

package com.volantis.mcs.ibm.websphere.mcsi;

/**
 * @todo later refactor this to extract commonality with PAPI
*/
public interface MCSIConstants {

    /**
     * The Volantis copyright statement
     */
    static final String mark =
            "(c) Volantis Systems Ltd 2004. ";

  /**
   * Returned by {@link MCSIElement#elementStart} if the caller should
   * process the element's body (contents).
   */
  public static final int PROCESS_ELEMENT_BODY = 101;

  /**
   * Returned by {@link MCSIElement#elementStart} if the caller should
   * NOT process the element's body (contents).
   */
  public static final int SKIP_ELEMENT_BODY = 100;

  /**
   * Returned by {@link MCSIElement#elementEnd} if the caller should
   * continue processing the rest of the page (document).
   */
  public static final int CONTINUE_PROCESSING = 102;

  /**
   * Returned by {@link MCSIElement#elementEnd} if the caller should
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

 29-Oct-04	6027/1	geoff	VBM:2004102104 chartimages generated within portlets need some form of unique identifier.

 28-Oct-04	5897/1	geoff	VBM:2004102104 chartimages generated within portlets need some form of unique identifier.

 04-Feb-04	2828/1	ianw	VBM:2004011922 Added MCSI content handler

 ===========================================================================
*/
