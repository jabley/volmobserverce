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
 * $Header: /src/voyager/com/volantis/mcs/papi/PAPIConstants.java,v 1.4 2003/03/12 16:10:43 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 14-Oct-01    Paul            VBM:2001111402 - Created
 * 30-Nov-01    Paul            VBM:2001112909 - Added copyright statement.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * 11-Mar-03    Steve           VBM:2003022403 - Added public api doclet tags
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.papi;

/**
 * This interface defines some constants which are used by both PAPI classes
 * and users of PAPI.
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
*/
public interface PAPIConstants {

  /**
   * The copyright statement.
   */
  public static String mark = "(c) Volantis Systems Ltd 2001.";

  /**
   * Returned by {@link PAPIElement#elementStart} if the caller should
   * process the element's body (contents).
   */
  public static final int PROCESS_ELEMENT_BODY = 101;

  /**
   * Returned by {@link PAPIElement#elementStart} if the caller should
   * NOT process the element's body (contents).
   */
  public static final int SKIP_ELEMENT_BODY = 100;

  /**
   * Returned by {@link PAPIElement#elementEnd} if the caller should
   * continue processing the rest of the page (document).
   */
  public static final int CONTINUE_PROCESSING = 102;

  /**
   * Returned by {@link PAPIElement#elementEnd} if the caller should
   * abort the processing the rest of the page (document).
   */
  public static final int ABORT_PROCESSING = 103;
}

/*
 * Local variables:
 * c-basic-offset: 2
 * End:
 */

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
