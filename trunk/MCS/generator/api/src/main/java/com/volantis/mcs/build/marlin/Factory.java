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
 * $Header: /src/voyager/com/volantis/mcs/build/marlin/Factory.java,v 1.1 2002/05/16 08:42:21 pduffin Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 19-Dec-01    Paul            VBM:2001120506 - Created.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 16-May-02    Paul            VBM:2002032501 - Moved from the
 *                              com.volantis.mcs.build package.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.build.marlin;

import com.volantis.mcs.build.parser.AttributeDefinition;
import com.volantis.mcs.build.parser.AttributeGroupDefinition;
import com.volantis.mcs.build.parser.ComplexType;
import com.volantis.mcs.build.parser.DefaultSchemaFactory;
import com.volantis.mcs.build.parser.ElementDefinition;
import com.volantis.mcs.build.parser.Scope;

/**
 * Extend the default factory to create extended versions of some of the
 * schema classes.
 */
public class Factory
        extends DefaultSchemaFactory {

    // Javadoc inherited from super class.
    public AttributeDefinition createAttributeDefinition(
            Scope scope,
            String name) {
        return new AttributeInfo(scope, name);
    }

    // Javadoc inherited from super class.
    public
    AttributeGroupDefinition createAttributeGroupDefinition(
            Scope scope,
            String name) {
        return new AttributeGroupInfo(scope, name);
    }

    // Javadoc inherited from super class.
    public ElementDefinition createElementDefinition(
            Scope scope,
            String name) {
        return new ElementInfo(scope, name);
    }

    public ComplexType createComplexType(Scope scope, String name) {
        ComplexType complexType = new ComplexTypeInfo(scope, name);
        return complexType;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Dec-05	10845/1	pduffin	VBM:2005121511 Moved architecture files from CVS to MCS Depot

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
