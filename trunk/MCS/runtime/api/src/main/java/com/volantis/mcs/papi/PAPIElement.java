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
 * $Header: /src/voyager/com/volantis/mcs/papi/PAPIElement.java,v 1.8 2003/03/12 16:10:43 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 14-Oct-01    Paul            VBM:2001111402 - Created
 * 28-Nov-01    Paul            VBM:2001112202 - Updated to reflect changes
 *                              in papi classes.
 * 30-Nov-01    Paul            VBM:2001112909 - Added copyright statement.
 * 19-Dec-01    Paul            VBM:2001120506 - Reformatted some javadoc which
 *                              was too long.
 * 28-Feb-02    Paul            VBM:2002022804 - Added getContentWriter
 *                              and getDirectWriter.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 22-Feb-02    Paul            VBM:2002021802 - Added missing PAPIAttributes
 *                              parameter to writeDirect.
 * 11-Mar-03    Steve           VBM:2003022403 - Added public api doclet tags
 * 14-Apr-03    Steve           VBM:2003041501 - Add NativeWriter access
 * 23-Apr-03    Steve           VBM:2003041606 - Add isBlock() and 
 *                              hasMixedContent() methods which must be 
 *                              implemented by all PAPI elements. getContentWriter
 *                              now returns an OutputBufferWriter.
 * 19-May-03    Chris W         VBM:2003051902 - isBlock(), hasMixedContent(),
 *                              isPreFormatted(), getNativeWriter() removed.
 *                              getContentWriter(), getDirectWriter() return
 *                              java.io.Writer
 * 28-May-03    Steve           VBM:2003042206 - Patch 2003041501 from Metis
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.papi;

import java.io.Writer;

import com.volantis.mcs.context.MarinerRequestContext;

/**
 * This interface defines the methods which must be implemented by all
 * PAPI elements.
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 *
 * @mock.generate
 */
public interface PAPIElement
  extends PAPIConstants {

  /**
   * The copyright statement.
   */
  public static String mark = "(c) Volantis Systems Ltd 2001.";

  /**
   * Called at the start of a PAPI element.
   * @param context The MarinerRequestContext within which this element is
   * being processed.
   * @param papiAttributes The implementation of PAPIAttributes which
   * contains the attributes specific to the implementation of PAPIElement.
   * @return PROCESS_ELEMENT_BODY or SKIP_ELEMENT_BODY.
   * @throws PAPIException If there was a problem processing the element.
   */
  public int elementStart (MarinerRequestContext context,
                           PAPIAttributes papiAttributes)
    throws PAPIException;

  /**
   * Writes a String to the current output location.
   * <p>
   * This method encodes the String correctly for the destination
   * device / protocol.
   * </p><p>
   * This must only be called between calls to elementStart and the matching
   * call to elementEnd but it can be called as many times as needed during
   * that period.
   * </p>
   * @param context The MarinerRequestContext within which this element is
   * being processed.
   * @param papiAttributes The implementation of PAPIAttributes which
   * contains the attributes specific to the implementation of PAPIElement.
   * @param context The String to write.
   * @throws PAPIException If there was a problem processing the content.
   */
  public void elementContent (MarinerRequestContext context,
                              PAPIAttributes papiAttributes,
                              String content)
    throws PAPIException;

  /**
   * Get an <code>OutputBufferWriter</code> which can be used to write to 
   * the current output location.
   * <p>
   * The <code>Writer</code> write methods behaves as if the input was
   * wrapped in a String and passed to {@link #elementContent}, although
   * it may be more efficient.
   * </p>
   */
  public Writer getContentWriter (MarinerRequestContext context,
                                  PAPIAttributes papiAttributes)
    throws PAPIException;

    /**
   * Called at the end of a PAPI element.
   * <p>
   * If the elementStart method was called then this method will also be
   * called unless an Exception occurred during the processing of the
   * body.
   * </p>
   * @param context The MarinerRequestContext within which this element is
   * being processed.
   * @param papiAttributes The implementation of PAPIAttributes which
   * contains the attributes specific to the implementation of PAPIElement.
   * @return CONTINUE_PROCESSING or ABORT_PROCESSING.
   * @throws PAPIException If there was a problem processing the element.
   */
  public int elementEnd (MarinerRequestContext context,
                         PAPIAttributes papiAttributes)
    throws PAPIException;

  /**
   * Resets the internal state so it is equivalent (not necessarily identical)
   * to that of a newly created instance.
   * <p>
   * This method must be called after elementEnd in order to properly clean up
   * any resources held by this object.
   * </p>
   * <p>
   * This method allows the user to reuse PAPI element instances, although they
   * don't have to. After this method has been called it is safe to call
   * the elementStart method again.
   * </p>
   * @param context The MarinerRequestContext within which this element is
   * being processed.
   */
  public void elementReset (MarinerRequestContext context);
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

 02-Jun-05	7995/1	pduffin	VBM:2005050323 Additional enhancements for mock framework, allow setting of occurrences of a method call

 05-Apr-05	7513/1	geoff	VBM:2003100606 DOMOutputBuffer allows creation of text which renders incorrectly in WML

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
