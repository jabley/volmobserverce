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
 * 07-May-2003  Sumit       VBM:2003050606 - Created
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.utilities.sax;

import com.volantis.xml.utilities.sax.stream.AddRootElementInputStream;
import com.volantis.xml.utilities.sax.stream.AddRootElementReader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import org.xml.sax.InputSource;

/**
 * Takes and InputStream or systemID and uses the AddRootElementInputStream to
 * wrap a root element around it
 */
public class DocumentFragmentInputSource extends InputSource {

    /**
     * Store the namespaces that will be added to the outermost element
     */
    private Map namespaces = null;

    /**
     * Open a URL to the specified system id
     *
     * @param systemId
     */
    public DocumentFragmentInputSource(String systemId)
        throws IOException {
        // Open a URL to the system id specified and construct a
        // root element inputstream.
        // TODO: Fix the URL to a valid URL before opening
        InputStream urlStream = new URL(systemId).openStream();
        this.setByteStream(new AddRootElementInputStream(urlStream));
        this.setSystemId(systemId);
    }

    /**
     * Wrapped the specified input source in an AddRootElementInputStream
     *
     * @param inputSource
     */
    public DocumentFragmentInputSource(InputSource inputSource)
        throws IOException {
        //Construct this using the inputsource provided
        if (inputSource.getByteStream() != null) {
            this.setByteStream(
                new AddRootElementInputStream(inputSource.getByteStream()));
        } else if (inputSource.getSystemId() != null) {
            InputStream urlStream =
                new URL(inputSource.getSystemId()).openStream();
            this.setByteStream(new AddRootElementInputStream(urlStream));
            this.setSystemId(inputSource.getSystemId());
        } else {
            this.setCharacterStream(
                new AddRootElementReader(inputSource.getCharacterStream()));
        }
    }

    /**
     * Open a URL to the specified system id
     *
     * @param systemId
     * @param namespacePrefix the prefix for the "fragment" element
     * @param namespaces a map of prefix->url namespaces to be applied to the
     *                   "fragment" element. A prefix of "" indicated teh
     *                   default namespace
     */
    public DocumentFragmentInputSource(
        String systemId, String namespacePrefix, Map namespaces)
        throws IOException {
        // Open a URL to the system id specified and construct a
        // root element inputstream.
        // TODO: Fix the URL to a valid URL before opening
        InputStream urlStream = new URL(systemId).openStream();
        this.setByteStream(
            new AddRootElementInputStream(
                urlStream, namespacePrefix, namespaces));
        this.setSystemId(systemId);
    }

    /**
     * Wrapped the specified input source in an AddRootElementInputStream
     *
     * @param inputSource
     * @param namespaces  a map of prefix->url namespaces to be applied to the
     *                    "fragment" element. A prefix of "" indicated teh
     *                    default namespace
     */
    public DocumentFragmentInputSource(InputSource inputSource,
                                       String namespacePrefix,
                                       Map namespaces)
        throws IOException {
        //Construct this using the inputsource provided
        if (inputSource.getByteStream() != null) {
            this.setByteStream(new AddRootElementInputStream(
                inputSource.getByteStream(), namespacePrefix, namespaces));
        } else if (inputSource.getSystemId() != null) {
            InputStream urlStream =
                new URL(inputSource.getSystemId()).openStream();
            this.setByteStream(
                new AddRootElementInputStream(
                    urlStream, namespacePrefix, namespaces));
            this.setSystemId(inputSource.getSystemId());
        } else {
            this.setCharacterStream(
                new AddRootElementReader(inputSource.getCharacterStream(),
                                         namespacePrefix,
                                         namespaces));
        }
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
