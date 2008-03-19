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
 * $Header: /src/voyager/com/volantis/mcs/protocols/wml/waptv5/WapTV5UnabridgedTransformer.java,v 1.2 2003/01/15 12:42:10 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 13-Jan-03    Phil W-S        VBM:2002110402 - Created. Provides WAPTV5
 *                              protocol-specific DOM table optimization.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.wml.waptv5;

import com.volantis.mcs.protocols.trans.TransformationConfiguration;
import com.volantis.mcs.protocols.trans.UnabridgedTransformer;

/**
 * Provides WAPTV5 protocol-specific DOM table optimization.
 */
public class WapTV5UnabridgedTransformer extends UnabridgedTransformer {

    /**
     * Initialize a new instance using the given parameters.
     *
     * @param configuration protocol specific configuration used when
     *                      transforming
     */
    public WapTV5UnabridgedTransformer(
            TransformationConfiguration configuration) {
        super(new WapTV5TransFactory(configuration));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 19-Aug-05	9289/6	emma	VBM:2005081602 Refactoring UnabridgedTransformers so they're not singletons

 17-Aug-05	9289/1	emma	VBM:2005081602 Refactoring TransMapper and TransFactory so that they're not singletons

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
