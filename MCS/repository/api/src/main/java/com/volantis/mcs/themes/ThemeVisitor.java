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
package com.volantis.mcs.themes;


/**
 * Interface defining all visitor methods that a concrete Theme Visitor needs
 * to implement, one for each object type which may call the visitor.
 */
public interface ThemeVisitor {

    /**
     * Called to process the targetObject, normally targetObject will have
     * called this method.
     *
     * @param targetObject Object that is to be processed
     * @return true if the calling Rule should traverse its children,
     *         false if the traversal has been done by this visit method.
     */
   boolean visit(Rule targetObject);

    /**
     * Called to process the targetObject, normally targetObject will have
     * called this method.
     *
     * @param targetObject Object that is to be processed
     * @return true if the calling StyleProperties should traverse its
     *         children, false if the traversal has been done by this visit
     *         method.
     */
    boolean visit(StyleProperties targetObject);

    /**
     * Called to process the targetObject, normally targetObject will have
     * called this method.
     *
     * @param targetObject Object that is to be processed
     * @return true if the calling Selector should traverse its children,
     *         false if the traversal has been done by this visit method.
     */
   boolean visit(Selector targetObject);

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Sep-05	9407/1	pduffin	VBM:2005083007 Removed old themes model

 29-Jun-05	8552/5	pabbott	VBM:2005051902 JIBX Theme accessors

 29-Jun-05	8552/3	pabbott	VBM:2005051902 JIBX Theme accessors

 13-Jun-05	8552/1	pabbott	VBM:2005051902 An Eclipse editor fix

 24-May-05	8329/1	pabbott	VBM:2005051901 New vistitor pattern for Themes

 ===========================================================================
*/
