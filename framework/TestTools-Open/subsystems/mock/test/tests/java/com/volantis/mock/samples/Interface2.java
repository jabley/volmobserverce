/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2006. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mock.samples;

import org.xml.sax.SAXException;

import java.io.IOException;

/**
 * A simple interface.
 *
 * @mock.generate
 */
public interface Interface2 {

    int bar(char[] c);

    void withThrows() throws IOException, SAXException;
}
