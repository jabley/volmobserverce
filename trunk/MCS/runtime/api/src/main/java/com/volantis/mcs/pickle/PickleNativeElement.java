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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.pickle;

import com.volantis.mcs.papi.PAPIElement;
import com.volantis.mcs.papi.PAPIElementFactory;
import com.volantis.mcs.papi.PAPIFactory;
import com.volantis.mcs.papi.AbstractPAPIDelegatingElement;


/**
 * Instances of this class should be used when the pickle output is to be
 * written to the current pane using the nativeWriter writer.
 */
public final class PickleNativeElement
 extends AbstractPAPIDelegatingElement {

  /**
   * The delegated PAPI element implementation
   */
  private PAPIElement delegate;

  /**
   * The factory that provides the implementation objects for this element.
   */
  private static PAPIElementFactory  papiElementFactory =
      PAPIFactory.getDefaultInstance().getPickleNativeElementFactory();

  public PickleNativeElement () {
    super(papiElementFactory);
  }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 26-May-05	7995/1	pduffin	VBM:2005050323 Committing results of super merge

 18-May-05	8196/8	ianw	VBM:2005051203 Fixed Accurev hell and javadoc that got missed because of Accurev hell

 ===========================================================================
*/
