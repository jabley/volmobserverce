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
 * $Header: /src/voyager/com/volantis/mcs/themes/StyleValueVisitor.java,v 1.3 2002/07/30 09:28:24 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 26-Apr-02    Doug            VBM:2002040803 - Created as part of
 *                              implementation of new internal themes structure
 * 23-Jul-02    Ian             VBM:2002071802 - Added visit method for
 *                              StyleInvalid.
 * 29-Jul-2002  Sumit           VBM:2002072906 - Added visit method for
 *                              StyleTime
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.themes;



/**
 * This interface defines methods which will be called by the different
 * StyleValue classes.
 */
public interface StyleValueVisitor {

    /**
     * This method is called by StyleAngle's visit method.
     * @param value The value being visited
     * @param object A generic object parameter
     */
    public void visit(StyleAngle value, Object object);

    /**
     * This method is called by StyleColorName's visit method.
     * @param value The value being visited
     * @param object A generic object parameter
     */
    void visit(StyleColorName value, Object object);

    /**
     * This method is called by StyleColorPercentage's visit method.
     * @param value The value being visited
     * @param object A generic object parameter
     */
    void visit(StyleColorPercentages value, Object object);

    /**
     * This method is called by StyleColorRGB's visit method.
     * @param value The value being visited
     * @param object A generic object parameter
     */
    void visit(StyleColorRGB value, Object object);

    /**
     * This method is called by StyleComponentURI's visit method.
     * @param value The value being visited
     * @param object A generic object parameter
     */
    public void visit(StyleComponentURI value, Object object);

    /**
     * This method is called by StyleTranscodableURI's visit method.
     * 
     * @param value The value being visited
     * @param object A generic object parameter
     */
    public void visit(StyleTranscodableURI value, Object object);

    /**
     * This method is called by StyleFrequency's visit method.
     * @param value The value being visited
     * @param object A generic object parameter
     */
    public void visit(StyleFrequency value, Object object);

    /**
     * This method is called by StyleFunction's visit method.
     * @param value The value being visited
     * @param object A generic object parameter
     */
    public void visit(StyleFunctionCall value, Object object);

    /**
     * This method is called by StyleIdentifier's visit method.
     * @param value The value being visited
     * @param object A generic object parameter
     */
    public void visit(StyleIdentifier value, Object object);

    /**
     * This method is called by StyleInherit's visit method.
     * @param value The value being visited
     * @param object A generic object parameter
     */
    public void visit(StyleInherit value, Object object);

    /**
     * This method is called by StyleInteger's visit method.
     * @param value The value being visited
     * @param object A generic object parameter
     */
    public void visit(StyleInteger value, Object object);

    /**
     * This method is called by StyleInvalid's visit method.
     * @param value The value being visited
     * @param object A generic object parameter
     */
    public void visit(StyleInvalid value, Object object);

    /**
     * This method is called by StyleKeyword's visit method.
     * @param value The value being visited
     * @param object A generic object parameter
     */
    public void visit(StyleKeyword value, Object object);

    /**
     * This method is called by StyleLength's visit method.
     * @param value The value being visited
     * @param object A generic object parameter
     */
    public void visit(StyleLength value, Object object);

    /**
     * This method is called by StyleList's visit method.
     * @param value The value being visited
     * @param object A generic object parameter
     */
    public void visit(StyleList value, Object object);

    /**
     * This method is called by StyleNumber's visit method.
     * @param value The value being visited
     * @param object A generic object parameter
     */
    public void visit(StyleNumber value, Object object);

    /**
     * This method is called by StylePair's visit method.
     * @param value The value being visited
     * @param object A generic object parameter
     */
    public void visit(StylePair value, Object object);

    /**
     * This method is called by StylePercentage's visit method.
     * @param value The value being visited
     * @param object A generic object parameter
     */
    public void visit(StylePercentage value, Object object);

    /**
     * This method is called by StyleString's visit method.
     * @param value The value being visited
     * @param object A generic object parameter
     */
    public void visit(StyleString value, Object object);

    /**
     * This method is called by StyleURI's visit method.
     * @param value The value being visited
     * @param object A generic object parameter
     */
    public void visit(StyleURI value, Object object);

    /**
     * This method is called by {@link StyleUserAgentDependent#visit}.
     *
     * @param value The value being visited
     * @param object A generic object parameter
     */
    public void visit(StyleUserAgentDependent value, Object object);

    /**
     * This method is called by StyleTime's visit method.
     * @param value The value being visited
     * @param object A generic object parameter
     */
    public void visit(StyleTime value, Object object);

    /**
     * This method is called by StyleFraction's visit method.
     * @param value The value being visited
     * @param object A generic object parameter
     */
    public void visit(StyleFraction value, Object object);

    /**
     * This method is called by CustomStyleValue's visit method.
     * @param value The value being visited
     * @param object A generic object parameter
     */
    void visit(CustomStyleValue value, Object object);
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 27-Sep-05	9487/3	pduffin	VBM:2005091203 Committing new CSS Parser

 15-Sep-05	9512/1	pduffin	VBM:2005091408 Committing changes to JIBX to use new schema

 13-Sep-05	9496/1	pduffin	VBM:2005091211 Replaced text-decoration with individual properties

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/1	doug	VBM:2004111702 Refactored Logging framework

 01-Nov-04	5992/1	adrianj	VBM:2004101403 Moved generation of computed style values

 ===========================================================================
*/
