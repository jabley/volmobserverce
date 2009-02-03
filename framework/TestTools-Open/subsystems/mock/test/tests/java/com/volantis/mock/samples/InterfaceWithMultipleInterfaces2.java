/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2007. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mock.samples;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.FileNotFoundException;

/**
 * An interface that extends multiple interfaces.
 *
 * @mock.generate base="Interface2" interfaces="Interface3,Interface1"
 */
public interface InterfaceWithMultipleInterfaces2
        extends Interface2, Interface1, Interface3 {

}
