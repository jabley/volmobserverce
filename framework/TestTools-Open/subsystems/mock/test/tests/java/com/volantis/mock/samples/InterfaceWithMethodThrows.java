/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2007. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mock.samples;

import org.xml.sax.SAXException;

import java.io.IOException;

/**
 * An interface that has a couple of methods that throw exceptions.
 *
 * @mock.generate
 */
public interface InterfaceWithMethodThrows {

    void foo(int a) throws IOException, SAXException;

    int[] bar() throws IOException;
}
