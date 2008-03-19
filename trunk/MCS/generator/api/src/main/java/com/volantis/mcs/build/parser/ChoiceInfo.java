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
 * $Header: /src/voyager/com/volantis/mcs/build/parser/ChoiceInfo.java,v 1.1 2002/04/27 16:10:54 doug Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 27-Apr-02    Doug            VBM:2002040803 - Created 
 * ----------------------------------------------------------------------------
 */


package com.volantis.mcs.build.parser;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class ChoiceInfo implements SchemaObject {

  private List elements;

  public ChoiceInfo() {
    elements = new ArrayList();
  }

  public void addElement(ElementDefinition element) {
    elements.add(element);
  }

  public Iterator elementIterator() {
    return elements.iterator();
  }

  public Scope getScope () {
    throw new UnsupportedOperationException ();
  }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
