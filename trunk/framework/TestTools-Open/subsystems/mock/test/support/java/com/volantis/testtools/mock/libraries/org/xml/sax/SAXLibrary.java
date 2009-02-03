/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.libraries.org.xml.sax;

import org.xml.sax.EntityResolver;
import org.xml.sax.Locator;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.XMLReader;

/**
 * Triggers auto generation of classes within <code>org.xml.sax</code>.
 *
 * <p>If you add new fields in this file then remember to update the associated
 * test case to ensure that the generated mocks are usable.</p>
 *
 * @mock.generate library="true"
 */
public class SAXLibrary {

    /**
     * @mock.generate interface="true"
     */
    public EntityResolver entityResolver;

    /**
     * @mock.generate interface="true"
     */
    public Locator locator;

    /**
     * @mock.generate interface="true"
     */
    public ContentHandler contentHandler;

    /**
     * @mock.generate interface="true"
     */
    public ErrorHandler errorHandler;
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Nov-05	10098/1	phussain	VBM:2005110209 Migration Framework for Repository Parser - ready for review

 ===========================================================================
*/
