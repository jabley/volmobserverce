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
 * $Header: /src/voyager/com/volantis/mcs/runtime/RequestHeaders.java,v 1.6 2003/02/18 13:58:03 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 09-Jan-02    Paul            VBM:2002010403 - Created.
 * 11-Jan-02    Mat             VBM:2001121403 - Added the iterator() method.
 * 21-Feb-02    Allan           VBM:2002022007 - Changed headers into a TreeMap
 *                              so that we can avoid using toLowerCase when
 *                              adding and getting. Updated addHeader(),
 *                              getHeader() and the constructor accordingly.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from 
 *                              class to string.
 * 13-Feb-03    Byron           VBM:2003021204 - Removed map attribute and
 *                              supporting methods (constructor, addHeader,
 *                              (getHeader now abstract, iterator).
 *                              getProfileHeaderName, getHeader now abstract
 *                              and added getAcceptMimeTypes.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime;

/**
 * This class encapsulates the request headers in an environment independent
 * way.
 *
 * @deprecated This class has been deprecated in favour of
 * {@link com.volantis.mcs.http.HttpHeaders}
 */
public abstract class RequestHeaders {


    /**
     * Get the value of a header.
     *
     * @param           name The name of the header (case insensitive).
     * @return          The value of the header.
     * @see        #getAcceptMimeTypes
     * @deprecated this method should no longer be used due to the fact
     *                  that one can have more that one request header for the
     *                  specified name. If you want to guarentee that the
     *                  return value returns all the relevant values then you
     *                  will need to provide your own method implementation for
     *                  that.
     */
    public abstract String getHeader(String name);

    /**
     * Return an array of accept mime types without any qualified parts to the
     * values. ie. just mime/text
     *
     * @return      Return an array of accept mime types without any qualified
     *              parts to the values. ie. just mime/text
     */
    public String[] getAcceptMimeTypes() {
        return null;
    }
}
