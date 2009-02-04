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
package com.volantis.mcs.eclipse.ab.editors.dom;


/**
 * A type safe enumerator that designates the type of a form.
 */
public class FormType {
    /**
     * A FormType designating a standard OverviewForm.
     */
    public static final FormType STANDARD_FORM = new FormType("standard"); //$NON-NLS-1$

    /**
     * A FormType designating a Layout OverviewForm.
     */
    public static final FormType LAYOUT_FORM = new FormType("layout"); //$NON-NLS-1$

    /**
     * A FormType designating a Theme OverviewForm.
     */
    public static final FormType THEME_FORM = new FormType("theme"); //$NON-NLS-1$

    /**
     * The property name associated with this FormType.
     */
    public final String typeName;

    /**
     * Construct a new FormType with the specified property name.
     * @param typeName Type name of this FormType.
     */
    private FormType(String typeName) {
        this.typeName = typeName;
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 17-Dec-03	2213/3	allan	VBM:2003121401 Basic editor support for all policies. Some bugs remain.

 15-Dec-03	2213/1	allan	VBM:2003121401 Refactored to reduce the number of resource properties.

 ===========================================================================
*/
