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


package com.volantis.mcs.accessors.common;

import com.volantis.mcs.repository.RepositoryException;

import java.io.Writer;


/**
 * Provides method to write a serialized form of an object to the specified
 * writer.
 *
 * @mock.generate 
 */
public interface ComponentWriter {

    /**
     * Write an object to the given Writer.
     *
     * @param writer destination of serialized form.
     * @param object object to write out.
     * @throws RepositoryException
     */
    public void write(Writer writer, Object object)
        throws RepositoryException;

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Oct-05	9789/1	emma	VBM:2005101113 Refactor JDBC Accessors to use chunked accessor

 28-Sep-05	9445/5	gkoch	VBM:2005090603 Introduced ComponentContainers to bring components and their assets together

 05-Jul-05	8552/1	pabbott	VBM:2005051902 JIBX Javadoc updates

 03-Jun-05	8346/1	ianw	VBM:2005051911 New JDBC Theme Accessor

 ===========================================================================
*/
