/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.libraries.java.io;

import java.io.FilterInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

/**
 * Triggers auto generation of classes within <code>java.io</code> for which
 * the source is not available.
 *
 * <p>If you add new fields in this file then remember to update the associated
 * test case to ensure that the generated mocks are usable.</p>
 *
 * @mock.generate library="true"
 */
public class IOLibrary {

    /**
     * @mock.generate
     */
    //public Writer writer;

    /**
     * @mock.generate
     */
    public Reader reader;

    /**
     * @mock.generate
     */
    public OutputStream outputStream;

    /**
     * @mock.generate
     */
    public InputStream inputStream;

    /**
     * @mock.generate base="InputStream"
     */
    public FilterInputStream filterInputStream;
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 31-May-05	7995/1	pduffin	VBM:2005050323 Added ability to generate mocks for external libraries

 ===========================================================================
*/
