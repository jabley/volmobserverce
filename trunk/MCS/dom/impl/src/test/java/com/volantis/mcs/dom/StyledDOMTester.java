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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.dom;

import com.volantis.mcs.dom.output.DOMDocumentOutputter;
import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.mcs.dom.debug.DocumentStyler;
import com.volantis.mcs.dom.debug.StyledDocumentWriter;
import com.volantis.mcs.dom.debug.DebugCharacterEncoder;
import com.volantis.styling.Styles;
import com.volantis.styling.StylesBuilder;
import com.volantis.styling.properties.StylePropertySet;
import org.xml.sax.EntityResolver;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

/**
 * A class to allow easy parsing and rendering of styled DOMs.
 * <p>
 * Useful for integration testing code which uses styled DOMs.
 */
public class StyledDOMTester {

    private final EntityResolver resolver;

    /**
     * Creates an MCS DOM Document for an input XML document, transforming
     * textual style properties defined in any style= attributes which are
     * found into a StyleValue object linked to the Element the attribute was
     * defined on.
     *
     * @param fragment the input XML, with style properties defined in style=
     *      attributes. E.g. "style='font-family:sans-serif'".
     * @return the created styled DOM, with StyleValues associated with each
     *      element which had a style= attribute as defined above.
     * @deprecated use non-static {@link #parse(String)} method.
     */
    public static Document createStyledDom(String fragment) {

        return new StyledDOMTester().parse(fragment);
    }

    /**
     * Renders an MCS DOM Document into an XML document, transforming any
     * {@link Styles} into textual style properties defined in style attributes.
     *
     * @param document      the styled DOM which should be rendered into XML
     * @return the parsed XML with style properties defined in the style
     *      attribute e.g. "style='{font-family:sans-serif} :link {color: red}'".
     * @deprecated use non-static {@link #render(Document)} instead.
     */
    public static String renderStyledDOM(Document document) {

        return new StyledDOMTester().render(document);
    }

    private StylePropertySet interestingProperties;
    private final boolean onlyExplicitlySpecified;
    private final boolean specified;

    public StyledDOMTester() {
        this(null, null, false, false);
    }

    public StyledDOMTester(boolean onlyExplicitlySpecified) {
        this(null, null, onlyExplicitlySpecified, false);
    }

    public StyledDOMTester(EntityResolver resolver) {
        this(resolver, null, false, false);
    }

    public StyledDOMTester(StylePropertySet interestingProperties) {
        this(null, interestingProperties, false, false);
    }

    public StyledDOMTester(
            EntityResolver resolver,
            StylePropertySet interestingProperties,
            boolean onlyExplicitlySpecified, boolean specified) {

        this.resolver = resolver;
        this.interestingProperties = interestingProperties;
        this.onlyExplicitlySpecified = onlyExplicitlySpecified;
        this.specified = specified;
    }

    /**
     * @deprecated does not fully populate styles.
     */
    public Document parse(InputStream inputStream) {
        Document document;
        try {
            XMLReader reader = DOMUtilities.getReader();
            if (resolver != null) {
                reader.setEntityResolver(resolver);
            }
            document = DOMUtilities.read(reader, inputStream);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return parse(document);        
    }

    /**
     * @deprecated does not fully populate styles.
     */
    public Document parse(String fragment) {
        Document document;
        try {
            document = DOMUtilities.read(fragment);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return parse(document);
    }

    /**
     * @deprecated does not fully populate styles.
     */
    public Document parse(Document document) {
        WalkingDOMVisitor visitor = new WalkingDOMVisitorStub() {
            public void visit(Element element) {

                String cssValues = element.getAttributeValue("style");
                Styles styles = StylesBuilder.getStyles(
                        cssValues, specified);

                if (styles != null) {
                    element.setStyles(styles);
                    // Remove the style attribute as it's information is now
                    // encapsulated in the styled element.
                    element.removeAttribute("style");
                }
            }
        };
        DOMWalker walker = new DOMWalker(visitor);
        walker.walk(document);

        return document;
    }

    /**
     * Parses the document and then fully populates any styles that may have
     * been set on style attributes.
     */
    public Document parseFull(String fragment) {
        Document document = null;
        try {
            document = DOMUtilities.read(fragment);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        DocumentStyler styler = new DocumentStyler("");
        styler.style(document);
        return document;
    }

    public String render(Document document) {
        StringWriter writer = new StringWriter();
        DOMDocumentOutputter outputter = createOutputter(writer);
        try {
            outputter.output(document);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return writer.toString();
    }

    private DOMDocumentOutputter createOutputter(StringWriter writer) {
        DOMDocumentOutputter outputter = new DOMDocumentOutputter(
            new StyledDocumentWriter(writer, interestingProperties,
                    onlyExplicitlySpecified),
                new DebugCharacterEncoder());
        return outputter;
    }

    public String render(Element element) {
        StringWriter writer = new StringWriter();
        DOMDocumentOutputter outputter = createOutputter(writer);
        try {
            outputter.output(element);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return writer.toString();
    }

    /**
     * Normalise a string XML fragment by parsing it into a styled DOM and then
     * rendering it back to a string.
     *
     * @param fragment
     * @return
     * @throws IOException
     * @throws SAXException
     * @deprecated does not fully populate styles.
     */
    public String normalize(String fragment)
            throws IOException, SAXException {

        if (fragment.length() == 0) {
            return fragment;
        }

        final Document expectedDOM = parse(fragment);
        return render(expectedDOM);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10527/3	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 02-Dec-05	10512/2	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 25-Nov-05	10453/1	rgreenall	VBM:2005092107 Restrict the length of lines written by MCS for devices that have a maximum line limit.

 25-Nov-05	9708/1	rgreenall	VBM:2005092107 Restrict the length of lines written by MCS for devices that have a maximum line limit.

 25-Nov-05	9708/1	rgreenall	VBM:2005092107 Restrict the length of lines written by MCS for devices that have a maximum line limit.

 12-Oct-05	9673/2	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 10-Oct-05	9637/1	emma	VBM:2005092807 Adding tests for XForms emulation

 29-Sep-05	9600/1	geoff	VBM:2005092306 Implement fine grained control over vertical whitespace in WML

 22-Aug-05	9298/4	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 22-Aug-05	9331/9	gkoch	VBM:2005081603 InputStream -> Reader

 22-Aug-05	9223/4	emma	VBM:2005080403 Remove style class from within protocols and transformers

 18-Aug-05	9007/5	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 16-Aug-05	9287/6	gkoch	VBM:2005080509 resolved conflict

 16-Aug-05	9287/3	gkoch	VBM:2005080509 vbm2005080509: supermerge + comment

 16-Aug-05	9287/1	gkoch	VBM:2005080509 vbm2005080509 applied review comments

 16-Aug-05	9286/1	geoff	VBM:2005072208 Normalizing of inferrable properties does not work properly.

 09-Aug-05	9195/3	emma	VBM:2005080510 Refactoring to create StyledDOMTester

 19-Jul-05	8668/13	geoff	VBM:2005060302 XDIMECP: Generate optimised CSS for a DOM.

 ===========================================================================
*/
