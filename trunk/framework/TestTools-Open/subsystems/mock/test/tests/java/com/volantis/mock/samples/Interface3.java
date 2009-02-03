/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2006. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mock.samples;

import org.xml.sax.SAXException;

import java.io.FileNotFoundException;

/**
 * A simple interface.
 *
 * @mock.generate base="Interface4"
 */
public interface Interface3
        extends Interface4 {

    java.lang.String snozzle();

    void withThrows() throws FileNotFoundException, SAXException,
            InterruptedException;
}
