/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2006. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mock.samples;

import org.xml.sax.SAXException;

import java.io.IOException;

/**
 * A class that implements a number of different interfaces.
 *
 * @mock.generate base="InterfaceWithMultipleInterfaces1"
 *                interfaces="Interface1, Interface2"
 */
public abstract class ClassWithMultipleInterfaces1
        implements Interface1, Interface2, InterfaceWithMultipleInterfaces1 {

    public java.lang.String snozzle() {
        return null;
    }

    public byte[] swizzle(int f) {
        return null;
    }
}
