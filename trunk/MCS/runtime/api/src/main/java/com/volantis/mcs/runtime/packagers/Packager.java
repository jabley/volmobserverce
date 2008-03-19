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
 * $Header: /src/voyager/com/volantis/mcs/runtime/packagers/Packager.java,v 1.3 2003/02/18 11:50:40 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 31-Jan-03    Phil W-S        VBM:2003013013 - Created. Provides a mechanism
 *                              for creating a (response) package composed of
 *                              body (content) and associated resources.
 * 12-Feb-03    Phil W-S        VBM:2003021206 - Update signature of
 *                              createPackage.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.packagers;

import com.volantis.mcs.context.MarinerRequestContext;

/**
 * A packager's responsibility is to take a response's body and
 * appropriate associated resources (e.g. image files etc.) and to
 * package them together into some form of composite response.
 *
 * @author <a href="mailto:phil.weighill-smith@volantis.com">Phil W-S</a>
 */
public interface Packager {
    /**
     * A package is created using this method. The body of the package is
     * obtained from the given body source, while the resources or references
     * to the resources are obtained from the ApplicationContext's
     * PackageResources instance.
     *
     * @param context     the request context associated with the packaging
     *                    operation
     * @param bodySource  the source of the package's body content
     * @param bodyContext a contextual object of relevance to the package
     *                    bodySource
     */
    void createPackage(MarinerRequestContext context,
                       PackageBodySource bodySource,
                       Object bodyContext)
        throws PackagingException;
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
