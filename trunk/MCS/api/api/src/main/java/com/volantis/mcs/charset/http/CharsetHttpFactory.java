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
 * $Header: /cvs/architecture/architecture/api/src/java/com/volantis/mcs/charset/http/CharsetHttpFactory.java,v 1.1 2004/07/22 15:37:42 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 22-Jul-04    Ian             Assignment:888 - Created.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.charset.http;



/**
 * This class will generate charset objects that are HTTP specific.
 *
 * <p><strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
abstract public class CharsetHttpFactory {
    /**
     * Get the default instance of this factory.
     *
     * @return The default instance of this factory.
     * @throws IllegalStateException if any error occurs.
     */
    public static CharsetHttpFactory getDefaultInstance() {

        try {
            return (CharsetHttpFactory) Class.forName(
                    "com.volantis.mcs.charset.http.DefaultCharsetHttpFactory").
                    newInstance();
        } catch (InstantiationException e) {
            throw new IllegalStateException(e.getMessage());
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    /**
     * This method returns the default charset selector for HTTP. This selector
     * can be used to determine the output character set for a device.
     *
     * @return The default {@link CharsetHttpSelector}
     */
    abstract public CharsetHttpSelector getDefaultCharsetHttpSelector();

}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 03-Nov-04	6098/1	byron	VBM:2004100715 No JavaDoc for com.volantis.mcs.charset public API

 03-Aug-04	5017/5	matthew	VBM:2004073003 fixed dependency issues

 02-Aug-04	5017/3	matthew	VBM:2004073003 Add CharsetHttpSelector and corresponding factories/default implementations

 02-Aug-04	5017/1	matthew	VBM:2004073003 CharsetHttpFactory and Selector

 ===========================================================================
*/
