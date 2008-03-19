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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.integration;

/**
 * Encapsulates details about the URL that is about to be rewritten.
 *
 * <p><strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels.</strong></p>
 *
 * @see PageURLRewriter
 *
 * @mock.generate
 */
public interface PageURLDetails {

    /**
     * The type of URL.
     * 
     * @return An instance of {@link PageURLType}, may not be null.
     */
    public PageURLType getPageURLType();
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jul-05	8862/1	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 07-Jul-04	4824/1	pduffin	VBM:2004062102 Documented PageURLRewriter and related classes

 28-Jun-04	4733/3	allan	VBM:2004062105 Convert Volantis to use the new PageURLRewriter

 21-Jun-04	4728/1	allan	VBM:2004062101 Classes and interfaces for general url rewriting.

 ===========================================================================
*/
