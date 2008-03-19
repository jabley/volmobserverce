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
 * $Header: /src/voyager/com/volantis/mcs/runtime/packagers/PackageBodySource.java,v 1.3 2003/02/18 11:50:40 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 31-Jan-03    Phil W-S        VBM:2003013013 - Created. Provides a mechanism
 *                              for generating the body of a (response)
 *                              package.
 * 12-Feb-03    Phil W-S        VBM:2003021206 - Update signature of write.
 * 08-May-03    Steve           VBM:2003042914 - Update signature of write to
 *                              take a PackageBodyOutput class instead of a Writer
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.packagers;

import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.protocols.ProtocolException;

import java.io.Writer;
import java.io.IOException;

/**
 * Allows the packager to create a package containing content from a source
 * with which it can co-operate. The implementation of this interface should
 * ensure that the PackageResources instance in the ApplicationContext
 * is updated with the actual (encoded) resources used in the written content
 * (assuming that the context has a non-null PackageResources instance) if the
 * content is not written out in its entirity.
 *
 * @author <a href="mailto:phil.weighill-smith@volantis.com">Phil W-S</a>
 */
public interface PackageBodySource {
    /**
     * The package body source should write its content. During the output of
     * this content, if something other than the full content is to be written,
     * the ApplicationContext's PackageResources instance should be populated
     * with the actual (encoded) resources used, as appropriate.
     *
     * @param writer      A PackagBodyOutput holding the writer to which the 
     *                    package body content should be written
     * @param context     the context in which the package is being generated
     * @param bodyContext a contextual object relevant to the writing of the
     *                    package body content
     * @throws PackagingException if there is a problem generating or writing
     *                            the body content
     */
    public void write(PackageBodyOutput writer,
                      MarinerRequestContext context,
                      Object bodyContext)
        throws PackagingException;

    /**
     * The content type of the package body can be returned by this method.
     * Only important if the packaging mechanism requires content type
     * identification.
     *
     * @return the content type of the package body
     */
    public String getBodyType(MarinerRequestContext context);
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
