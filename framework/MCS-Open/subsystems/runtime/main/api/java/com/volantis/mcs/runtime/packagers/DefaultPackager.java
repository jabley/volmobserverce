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
 * $Header: /src/voyager/com/volantis/mcs/runtime/packagers/DefaultPackager.java,v 1.3 2003/04/28 15:27:11 mat Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 12-Feb-03    Phil W-S        VBM:2003021206 - Created. Handles response
 *                              writer focused, non-packaging page generation.
 * 25-Apr-03    Mat             VBM:2003033108 - Changed to use the
 *                              CharsetEncodingWriter()
 * 02-May-03    Mat             VBM:2003033108 - Changed to pass the encoding
 *                              to the CharsetEncodingWriter()
 * 08-May-03    Steve           VBM:2003042914 - Update createPackage call to
 *                              write to create a package body output instead
 *                              of a Writer.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.packagers;

import java.io.OutputStream;
import java.io.Writer;

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;

import com.volantis.charset.CharsetEncodingWriter;
import com.volantis.charset.Encoding;
import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerContextException;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.EnvironmentContext;

/**
 * This "packager" performs the processing required when no packaging is
 * to be performed.
 *
 * @author <a href="mailto:phil.weighill-smith@volantis.com">Phil W-S</a>
 */
public class DefaultPackager implements Packager {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(DefaultPackager.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
                LocalizationFactory.createExceptionLocalizer(DefaultPackager.class);

    public void createPackage(MarinerRequestContext context,
                              PackageBodySource bodySource,
                              Object bodyContext)
        throws PackagingException {

        final MarinerPageContext pageContext =
             ContextInternals.getMarinerPageContext(context);

        try {
            // Perform basic page response initialization
            pageContext.initialiseResponse();

            final Encoding encoding = pageContext.getCharsetEncoding();
            if (encoding == null) {
                throw new PackagingException(
                            exceptionLocalizer.format("no-charset"));
            }
            final EnvironmentContext envContext =
                    pageContext.getEnvironmentContext();

            // Get the body source written to the response writer
            bodySource.write(new AbstractPackageBodyOutput() {
                // Retrieve the output stream from the page context and
                // filter up any errors
                public OutputStream getRealOutputStream() {
                    try {
                        return envContext.getResponseOutputStream();
                    } catch (MarinerContextException mce) {
                        throw new RuntimeException(mce);
                    }
                }

                // Retrieve the output writer from the page context, convert it
                // to a CharsetEncodingWriter and filter up any errors
                public Writer getRealWriter() {
                    try {
                        return new CharsetEncodingWriter(
                                envContext.getResponseWriter(), encoding);
                    } catch (MarinerContextException mce) {
                        throw new RuntimeException(mce);
                    }
                }
            }, context, bodyContext);
        } catch (MarinerContextException e) {            
            throw new PackagingException(
                        exceptionLocalizer.format("no-response-writer"), e);
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/5	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 25-Jul-03	860/3	geoff	VBM:2003071405 merge from metis again

 25-Jul-03	860/1	geoff	VBM:2003071405 merge from mimas

 25-Jul-03	858/1	geoff	VBM:2003071405 merge from metis; fix dissection test case sizes

 23-Jul-03	807/1	geoff	VBM:2003071405 works and tested but no design review yet

 ===========================================================================
*/
