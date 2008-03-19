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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.eclipse.common;

import com.volantis.mcs.eclipse.common.AttributesMessageFormatter;
import com.volantis.mcs.eclipse.common.PolicyAttributesDetails;
import com.volantis.mcs.assets.ImageAsset;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import org.jdom.Attribute;
import org.jdom.input.DefaultJDOMFactory;
import org.jdom.input.JDOMFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Test case for AttributesMessageFormat.
 */
public class AttributesMessageFormatterTestCase extends TestCaseAbstract {
    /**
     * Test format with just attributes.
     */
    public void testFormatAttributes() {
        JDOMFactory factory = new DefaultJDOMFactory();
        Attribute attr;
        List attributes = new ArrayList();
        attr = factory.attribute("device", "PC");
        attributes.add(attr);
        attr = factory.attribute("pixelsX", "100");
        attributes.add(attr);
        attr = factory.attribute("pixelsY", "200");
        attributes.add(attr);
        attr = factory.attribute("pixelDepth", "24");
        attributes.add(attr);
        attr = factory.attribute("rendering", "color");
        attributes.add(attr);

        String message = "{value}, {element}, {assetGroup}, {device}, " +
                "{rendering}, {pixelDepth}bits, {pixelsX}x{pixelsY}px, " +
                "{widthHint}";
        PolicyAttributesDetails details =
                new PolicyAttributesDetails("imageComponent", true);
        AttributesMessageFormatter attributesMessageFormatter =
                new AttributesMessageFormatter(details);
        String formatted =
                attributesMessageFormatter.format(attributes, message, null);
        assertEquals("PC, Color, 24bits, 100x200px", formatted);
    }

    /**
     * Test format with attributes and an element.
     */
    public void testFormatAttributesAndElement() {
        JDOMFactory factory = new DefaultJDOMFactory();
        Attribute attr;
        List attributes = new ArrayList();
        attr = factory.attribute("device", "PC");
        attributes.add(attr);
        attr = factory.attribute("pixelsX", "100");
        attributes.add(attr);
        attr = factory.attribute("pixelsY", "200");
        attributes.add(attr);
        attr = factory.attribute("pixelDepth", "24");
        attributes.add(attr);
        attr = factory.attribute("rendering", "color");
        attributes.add(attr);

        String message = "{value}, {element}, {assetGroup}, {device}, " +
                "{rendering}, {pixelDepth}bits, {pixelsX}x{pixelsY}px, " +
                "{widthHint}";


        PolicyAttributesDetails details =
                new PolicyAttributesDetails("imageComponent", true);
        AttributesMessageFormatter attributesMessageFormatter =
                new AttributesMessageFormatter(details);
        String formatted =
                attributesMessageFormatter.format(attributes, message,
                        "Device Specific Image");

        assertEquals("Device Specific Image, PC, Color, 24bits, 100x200px",
                formatted);
    }

    /**
     * Test the removal of 'px' if no value for pixels and there is a previous
     * non-empty value in the list.
     */
    public void testFormatAttributesNoOtherElements() {
        JDOMFactory factory = new DefaultJDOMFactory();
        Attribute attr;
        List attributes = new ArrayList();
        attr = factory.attribute("pixelsX", "100");
        attributes.add(attr);

        String message = "{pixelDepth}bits, {pixelsX}x{pixelsY}px";

        PolicyAttributesDetails details =
                new PolicyAttributesDetails("imageComponent", true);
        AttributesMessageFormatter attributesMessageFormatter =
                new AttributesMessageFormatter(details);
        String formatted =
                attributesMessageFormatter.format(attributes, message,
                        "");

        assertEquals("Result is: '" + formatted + "'",
                "100x",
                formatted);
    }


    /**
     * Test the removal of 'px' if no value for pixels and there is a previous
     * non-empty value in the list.
     */
    public void testFormatAttributesEndWithX() {
        JDOMFactory factory = new DefaultJDOMFactory();
        Attribute attr;
        List attributes = new ArrayList();
        attr = factory.attribute("pixelDepth", "24");
        attributes.add(attr);
        attr = factory.attribute("pixelsX", "100");
        attributes.add(attr);

        String message = "{pixelDepth}bits,  {pixelsX}x{pixelsY}px  and that is the end";

        PolicyAttributesDetails details =
                new PolicyAttributesDetails("imageComponent", true);
        AttributesMessageFormatter attributesMessageFormatter =
                new AttributesMessageFormatter(details);
        String formatted =
                attributesMessageFormatter.format(attributes, message,
                        "");

        assertEquals("Result is: '" + formatted + "'",
                "24bits,  100x and that is the end",
                formatted);
    }

    /**
     * Test the removal of 'px' if no value for pixels and there is NO previous
     * non-empty value in the list.
     */
    public void testFormatAttributesNoElements() {
        JDOMFactory factory = new DefaultJDOMFactory();
        Attribute attr;
        List attributes = new ArrayList();
        attr = factory.attribute("pixelDepth", "24");
        attributes.add(attr);

        String message = "{pixelsX}x{pixelsY}px";

        PolicyAttributesDetails details =
                new PolicyAttributesDetails("imageComponent", true);
        AttributesMessageFormatter attributesMessageFormatter =
                new AttributesMessageFormatter(details);
        String formatted =
                attributesMessageFormatter.format(attributes, message,
                        "");

        assertEquals("Result is: '" + formatted + "'",
                "",
                formatted);
    }

    /**
     * Test the removal of 'px' if no value for pixels and there is spaces and
     * commas.
     */
    public void testFormatAttributesCommasSpaces() {
        JDOMFactory factory = new DefaultJDOMFactory();
        Attribute attr;
        List attributes = new ArrayList();
        attr = factory.attribute("pixelDepth", "24");
        attributes.add(attr);
        attr = factory.attribute("pixelsY", "100");
        attributes.add(attr);

        String message = "{pixelDepth}bits , {pixelsX}x{pixelsY}px";

        PolicyAttributesDetails details =
                new PolicyAttributesDetails("imageComponent", true);
        AttributesMessageFormatter attributesMessageFormatter =
                new AttributesMessageFormatter(details);
        String formatted =
                attributesMessageFormatter.format(attributes, message,
                        "");

        assertEquals("Result is: '" + formatted + "'",
                "24bits , 100px",
                formatted);
    }

    /**
     * Test the missing first bracket (or char) if no value present.
     */
    public void testFormatAttributesMissingGridOpenBracket() {
        JDOMFactory factory = new DefaultJDOMFactory();
        Attribute attr;
        List attributes = new ArrayList();
        attr = factory.attribute("rows", "1");
        attributes.add(attr);
        attr = factory.attribute("columns", "2");
        attributes.add(attr);

        String message = "{element}: ''{name}'' ({rows}x{columns})";

        PolicyAttributesDetails details =
                new PolicyAttributesDetails("imageComponent", true);
        AttributesMessageFormatter attributesMessageFormatter =
                new AttributesMessageFormatter(details);
        String formatted =
                attributesMessageFormatter.format(attributes, message,
                        "elementName");

        assertEquals("Result is: '" + formatted + "'",
                "elementName: ''(1x2)",
                formatted);
    }

    /**
     * Test the missing first bracket (or char) if value present.
     */
    public void testFormatAttributesGridOpenBracket() {
        JDOMFactory factory = new DefaultJDOMFactory();
        Attribute attr;
        List attributes = new ArrayList();
        attr = factory.attribute("name", "name");
        attributes.add(attr);
        attr = factory.attribute("rows", "1");
        attributes.add(attr);
        attr = factory.attribute("columns", "2");
        attributes.add(attr);

        String message = "{element}: ''{name}'' ({rows}x{columns})";

        PolicyAttributesDetails details =
                new PolicyAttributesDetails("imageComponent", true);
        AttributesMessageFormatter attributesMessageFormatter =
                new AttributesMessageFormatter(details);
        String formatted =
                attributesMessageFormatter.format(attributes, message,
                        "elementName");

        assertEquals("Result is: '" + formatted + "'",
                "elementName: ''name'' (1x2)",
                formatted);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 24-Feb-04	3181/3	byron	VBM:2004020904 Components: Dynamic Visual asset value has default value of px

 16-Dec-03	2213/2	allan	VBM:2003121401 More editors and fixes for presentable values.

 15-Dec-03	2208/3	allan	VBM:2003121201 Include ImageAssetDetails in this fix.

 13-Dec-03	2208/1	allan	VBM:2003121201 Use PresentableItems for presenting attribute values.

 12-Dec-03	2123/1	allan	VBM:2003102005 Candidate ImageComponentEditor - no validation.

 ===========================================================================
*/
