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
 * $Header: $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who         Description
 * -----------  ----------- -------------------------------------------------
 * 20-May-2003  Sumit       VBM:2003050606 - Created
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.utilities.sax.stream;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;

/**
 * A Stream specific command that reads from a Reader
 */
public class AddRootElementReaderCommand extends AddRootElementStreamCommand {

    public AddRootElementReaderCommand(Reader underlying) {
        super(underlying);
    }

    /**
     * This constructor takes a underlying stream that contains the fragment to
     * wrap and a Map of prefix->url namespaces. All entries in the map are
     * (String)->(String). If the map is null or empty then no namespaces will
     * be added to the underlying fragment."" prefix is the default namespace
     * to use.
     *
     * @param underlying the underlying reader to use
     * @param fragmentNamespace the namespace of the "fragment" element that
     * is inserted
     * @param namespaces the map of namespaces
     */
    public AddRootElementReaderCommand(Reader underlying,
                                       String fragmentNamespace,
                                       Map namespaces) {
        super(underlying, fragmentNamespace, namespaces);
    }

    int streamRead() throws IOException {
        return ((Reader)underlyingStream).read();
    }

    int streamAvailable() throws IOException {
        return 0;
    }

    void reset() throws IOException {
        ((Reader)underlyingStream).reset();
    }

    void close() throws IOException {
        ((Reader)underlyingStream).close();
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 ===========================================================================
*/
