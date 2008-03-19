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
package com.volantis.mcs.eclipse.common;

/**
 * Implementations of this interface can be used to perform conversions when an
 * attribute value is to be exchanged from the model to the control used to
 * represent it on the GUI, or vice versa.
 *
 * <p>It is expected that the conversion in one method can be reversed using
 * the other method, though there is no requirement for the <code>result</code>
 * in the following to be true:</p>
 *
 * <pre>
 * AttributeValueExchange x = new ...;
 * String v = ...;
 * boolean result = v.equals(x.toModelForm(x.toControlForm(v)));
 * </pre>
 */
public interface AttributeValueExchange {
    /**
     * Converts the specified value from the form appropriate to the control to
     * a form appropriate to the model.
     *
     * @param controlForm
     *         the attribute value in the form appropriate to the GUI control
     * @return the attribute value in the form appropriate to the model
     */
    String toModelForm(String controlForm);

    /**
     * Converts the specified value from the form appropriate to the model to
     * a form appropriate to the control.
     *
     * @param modelForm
     *         the attribute value in the form appropriate to the model
     * @return the attribute value in the form appropriate to the GUI control
     */
    String toControlForm(String modelForm);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Mar-05	7374/1	philws	VBM:2004121405 Allow asset values to contain space characters via URI encoding

 ===========================================================================
*/
