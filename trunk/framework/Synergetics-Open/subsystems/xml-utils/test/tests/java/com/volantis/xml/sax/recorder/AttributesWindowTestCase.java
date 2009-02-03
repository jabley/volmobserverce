package com.volantis.xml.sax.recorder;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.xml.sax.recorder.impl.attributes.AttributeContainerBuilder;
import com.volantis.xml.sax.recorder.impl.attributes.AttributeContainerBuilderImpl;
import com.volantis.xml.sax.recorder.impl.attributes.AttributesContainer;
import com.volantis.xml.sax.recorder.impl.attributes.AttributesWindow;
import com.volantis.xml.sax.recorder.impl.recording.StringTable;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Tests for the AttributesWindow class.
 */
public class AttributesWindowTestCase
        extends TestCaseAbstract {

    /**
     * Ensure that the getIndex() methods on the AttributesWindow work
     * correctly.
     */
    public void testGetIndex() {

        StringTable stringTable = new StringTable();
        AttributeContainerBuilder builder =
                new AttributeContainerBuilderImpl(stringTable);

        int offset1 = builder.getOffset();
        AttributesImpl attributes = new AttributesImpl();
        attributes.addAttribute("", "abc", "abc", "CDATA", "123");
        attributes.addAttribute("", "xyz", "xyz", "CDATA", "456");
        builder.addAttributes(attributes);

        int offset2 = builder.getOffset();
        builder.addAttributes(attributes);
        int offset3 = builder.getOffset();

        AttributesContainer container = builder.buildContainer();
        AttributesWindow window = container.createWindow();

        int index;

        // First search for attributes in the first set of attributes.
        window.move(offset1, offset2 - offset1);
        index = window.getIndex("", "abc");
        assertEquals("Index incorrect for attribute", 0, index);
        index = window.getIndex("abc");
        assertEquals("Index incorrect for attribute", 0, index);

        index = window.getIndex("", "xyz");
        assertEquals("Index incorrect for attribute", 1, index);
        index = window.getIndex("xyz");
        assertEquals("Index incorrect for attribute", 1, index);

        index = window.getIndex("", "qrs");
        assertEquals("Index incorrect for missing attribute", -1, index);
        index = window.getIndex("qrs");
        assertEquals("Index incorrect for missing attribute", -1, index);

        // Now move the window and try again.
        window.move(offset2, offset3 - offset2);

        index = window.getIndex("", "abc");
        assertEquals("Index incorrect for attribute", 0, index);
        index = window.getIndex("abc");
        assertEquals("Index incorrect for attribute", 0, index);

        index = window.getIndex("", "xyz");
        assertEquals("Index incorrect for attribute", 1, index);
        index = window.getIndex("xyz");
        assertEquals("Index incorrect for attribute", 1, index);

        index = window.getIndex("", "qrs");
        assertEquals("Index incorrect for missing attribute", -1, index);
        index = window.getIndex("qrs");
        assertEquals("Index incorrect for missing attribute", -1, index);
    }
}
