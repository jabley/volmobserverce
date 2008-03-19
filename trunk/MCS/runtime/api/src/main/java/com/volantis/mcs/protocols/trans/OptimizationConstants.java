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
 * $Header: /src/voyager/com/volantis/mcs/protocols/trans/OptimizationConstants.java,v 1.3 2003/01/08 10:25:48 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 07-Jan-03    Phil W-S        VBM:2003010712 - Created. Provides useful
 *                              constants that define how optimization of
 *                              tables can be performed.
 * 08-Jan-03    Phil W-S        VBM:2003010712 - Added the always option.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.trans;

/**
 * Provides useful constants that define how optimization of tables can be
 * performed.
 *
 * @author <a href="mailto:phil.weighill-smith@volantis.com">Phil W-S</a>
 */
public interface OptimizationConstants {
    /**
     * The name of the artificial attribute that should be added to a
     * table element that is to be optimized in some non-default way. If
     * the given UnabridgedTransformer's default option is to be used the
     * attribute should not be set.
     */
    public final static String OPTIMIZATION_ATTRIBUTE = "OPTIMIZE";

    /**
     * A value that can be assigned to the OPTIMIZATION_ATTRIBUTE indicating
     * that the table should be optimized away irrespective of "fine" stylistic
     * value provided by the table.
     * <p>This can cause "lossy" optimization (stylistic value will be
     * lost).</p>
     */
    public final static String OPTIMIZE_ALWAYS = "always";

    /**
     * A value that can be assigned to the OPTIMIZATION_ATTRIBUTE indicating
     * that the table should be retained.
     */
    public final static String OPTIMIZE_NEVER = "never";

    /**
     * A value that can be assigned to the OPTIMIZATION_ATTRIBUTE indicating
     * that the table should be optimized away if there will be little or
     * no stylistic impact in so doing.
     */
    public final static String OPTIMIZE_LITTLE_IMPACT = "little impact";
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
