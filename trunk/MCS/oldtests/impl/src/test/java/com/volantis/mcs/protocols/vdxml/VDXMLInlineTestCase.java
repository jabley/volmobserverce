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
package com.volantis.mcs.protocols.vdxml;

import com.volantis.mcs.dom.output.DOMDocumentOutputter;
import com.volantis.mcs.dom.output.DocumentOutputter;
import com.volantis.mcs.dom.debug.DebugCharacterEncoder;
import com.volantis.mcs.dom.output.XMLDocumentWriter;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.StyleAttributes;
import com.volantis.mcs.protocols.TestDOMOutputBuffer;
import com.volantis.mcs.protocols.vdxml.style.VDXMLStyleFactory;
import com.volantis.mcs.themes.StyleColor;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.styling.Styles;
import com.volantis.styling.StylesBuilder;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.io.CharArrayWriter;
import java.io.IOException;

/**
 * A test case for VDXML inline elements.
 * <p>
 * This test the effects of applying styles to inline elements.
 * <p>
 * This tests the rendering of all the normal style properties which relate to 
 * VDXML AC and AZ tags, and the effects of nesting and combining them. 
 */ 
public class VDXMLInlineTestCase extends TestCaseAbstract {

    private static final StyleValueFactory STYLE_VALUE_FACTORY =
        StyleValueFactory.getDefaultInstance();

    // Basic objects required for testing.
    VDXMLStyleFactory vdStyleFactory = new VDXMLStyleFactory();
    CharArrayWriter writer = new CharArrayWriter();
    VDXMLDocumentWriter docWriter =
            new VDXMLDocumentWriter(new XMLDocumentWriter(writer));
    DocumentOutputter outputter = new DOMDocumentOutputter(
            docWriter, new DebugCharacterEncoder());
    
    TestDOMOutputBuffer buffer = new TestDOMOutputBuffer();

    // Various example style property values.
    Styles begin;
    Styles fontLarge;
    Styles fontMedium;
    Styles fontRed;
    Styles backgroundBlue;
    Styles fontLargeRed;
    Styles textNone;
    Styles textUnderline;
    Styles textBlink;
    Styles reverseVideo;

    StyleColor colorCyan = STYLE_VALUE_FACTORY.getColorByRGB(null, 0x00FFFF);
    StyleColor colorGreen = STYLE_VALUE_FACTORY.getColorByRGB(null, 0x00FF00);
    StyleColor colorRed = STYLE_VALUE_FACTORY.getColorByRGB(null, 0xFF0000);
    StyleColor colorBlue = STYLE_VALUE_FACTORY.getColorByRGB(null, 0x0000FF);
    StyleColor colorWhite = STYLE_VALUE_FACTORY.getColorByRGB(null, 0xFFFFFF);
    StyleColor colorBlack = STYLE_VALUE_FACTORY.getColorByRGB(null, 0x000000);

    {
        begin = StylesBuilder.getStyles(
                "background-color: aqua; " +
                "color: lime; " +
                "font-size: medium");

        fontLarge = StylesBuilder.getStyles("font-size: large");

        fontMedium = StylesBuilder.getStyles("font-size: medium");

        fontRed = StylesBuilder.getStyles("color: red");

        backgroundBlue = StylesBuilder.getStyles("background-color: blue");

        fontLargeRed = StylesBuilder.getStyles("color: red; font-size: large");

        textNone = StylesBuilder.getStyles(
                "mcs-text-blink: none; " +
                "mcs-text-underline-style: none");

        textUnderline = StylesBuilder.getStyles(
                "mcs-text-underline-style: solid");

        textBlink = StylesBuilder.getStyles("mcs-text-blink: blink");

        reverseVideo = StylesBuilder.getStyles(
                "background-color: lime; " +
                "color: aqua");
    }
    
    public VDXMLInlineTestCase() {
        
        buffer.clear();
    }

    /**
     * Test basic rendering of the font-size style property.
     */ 
    public void testFontSize() throws Exception {

        openTexte(begin);
            text("[default]");
            open(fontLarge);
                text("[large]");
            close();
            text("[default]");
        closeTexte();

        String expected = 
        "<TEXTE>" +
            "[default]" +
            "<AC TC=\"DT\"/>" +             // large
                "[large]" +
            "<AC TC=\"TN\"/>" +             // medium (default)
            "[default]" +
        "</TEXTE>";

        String actual = render();
        //System.out.println(actual);
        assertEquals("", expected, actual);
    }

    /**
     * Test basic rendering of the font-color style property.
     */ 
    public void testFontColor() throws Exception {

        openTexte(begin);
            text("[default]");
            open(fontRed);
                text("[red]");
            close();
            text("[default]");
        closeTexte();

        String expected = 
        "<TEXTE>" +
            "[default]" +
            "<AC CC=\"RO\"/>" +             // red
                "[red]" +
            "<AC CC=\"VE\"/>" +             // green (default)
            "[default]" +
        "</TEXTE>";

        String actual = render();
        //System.out.println(actual);
        assertEquals("", expected, actual);
    }

    /**
     * Test basic rendering of the background-color style property.
     */ 
    public void testBackgroundColor() throws Exception {

        openTexte(begin);
            text("[default]");
            open(backgroundBlue);
                text("[background-blue]");
            close();
            text("[default]");
        closeTexte();

        String expected = 
        "<TEXTE>" +
            "[default]" +
            "<AZ CF=\"BU\"/>" +             // background blue
                "[background-blue]" +
            "<AZ CF=\"CY\"/>" +             // background cyan (default)
            "[default]" +
        "</TEXTE>";

        String actual = render();
        //System.out.println(actual);
        assertEquals("", expected, actual);
    }
    
    /**
     * Test basic rendering of nested style properties.
     */ 
    public void testBasicNesting() throws Exception {

        openTexte(begin);
            open(fontLarge);
              text("[large]");
              open(fontMedium);
                  text("[+medium]");
              close();
              text("[large]");
            close();
        closeTexte();

        String expected = 
        "<TEXTE>" +
            "<AC TC=\"DT\"/>" +         // large on
                "[large]" +
                "<AC TC=\"TN\"/>" +     // medium on
                    "[+medium]" +
                "<AC TC=\"DT\"/>" +     // large on
                "[large]" +
            "<AC TC=\"TN\"/>" +         // medium on (default)
        "</TEXTE>";

        String actual = render();
        //System.out.println(actual);
        assertEquals("", expected, actual);
    }

    /**
     * Test rendering of nested style properties including inline elements
     * with no style properties.
     */ 
    public void testSparseNesting() throws Exception {

        openTexte(begin);
            open(null);
                open(fontLarge);
                    open(null);
                        text("[large]");
                        open(null);
                            open(fontMedium);
                                text("[+medium]");
                            close();
                        close();
                        text("[large]");
                    close();
                close();
            close();
        closeTexte();

        String expected = 
        "<TEXTE>" +
                                                    // (no style)
                "<AC TC=\"DT\"/>" +                 // large on
                                                    // (no style)
                        "[large]" +
                                                    // (no style)
                            "<AC TC=\"TN\"/>" +     // medium on
                                "[+medium]" +
                            "<AC TC=\"DT\"/>" +     // large on
                                                    // (no style)
                        "[large]" +
                                                    // (no style)
                "<AC TC=\"TN\"/>" +                 // medium on (default)
                                                    // (no style)
        "</TEXTE>";

        String actual = render();
        //System.out.println(actual);
        assertEquals("", expected, actual);
    }
    
    /**
     * Test rendering of style properties that can be optimised.
     * <p>
     * For now all we optimise are rendundant values. In future it would be 
     * good to also optimise output tags by combining where possible.
     */ 
    public void testOptimisations() throws Exception {
        
        openTexte(begin);
            open(fontLargeRed);
                open(fontMedium);
                    text("[+medium red]");
                close();
                text("[large red]");
            close();
        closeTexte();
        
        // potential optimisations here:
        // 1) combine two ACs together
        
        String expected = 
        "<TEXTE>" +
            "<AC CC=\"RO\" TC=\"DT\"/>" +   // red large on 
                "<AC TC=\"TN\"/>" +         // medium on 
                    "[+medium red]" +
                "<AC TC=\"DT\"/>" +         // large on
                "[large red]" +               
            "<AC CC=\"VE\" TC=\"TN\"/>" +   // green, medium on (defaults)
        "</TEXTE>";
        
        String actual = render();
        //System.out.println(actual);
        assertEquals("", expected, actual);
    }

    /**
     * Test rendering of basic duplicate nested font-size properties. 
     * <p>
     * This is another example of redundant value optimisation.
     */ 
    public void testFontSizeDuplicate() throws Exception {
        
        openTexte(begin);
            open(fontLarge);
                open(fontLarge);
                    text("[+large]");
                close();
            close();
        closeTexte();

        String expected = 
        "<TEXTE>" +
            "<AC TC=\"DT\"/>" +     // large on
                                    // large on again (ignored)
                    "[+large]" +
                                    // medium on again (ignored)
            "<AC TC=\"TN\"/>" +     // medium on (default) 
        "</TEXTE>";
        
        String actual = render();
        //System.out.println(actual);
        assertEquals("", expected, actual);
    }

    /**
     * Test basic rendering of the text-decoration style property underline
     * value. 
     */ 
    public void testUnderline() throws Exception {
        
        openTexte(begin);
            open(textUnderline);
                text("[underline]");
            close();
        closeTexte();

        String expected = 
        "<TEXTE>" +
            "<AZ SO=\"DE\"/>" +     // underline on
                "[underline]" +
            "<AZ SO=\"FI\"/>" +     // underline off (default) 
        "</TEXTE>";
        
        String actual = render();
        //System.out.println(actual);
        assertEquals("", expected, actual);
    }

    /**
     * Test rendering of basic duplicate text-decoration style property 
     * underline values. 
     * <p>
     * This is another example of redundant value optimisation.
     */ 
    public void testUnderlineDuplicate() throws Exception {
        
        openTexte(begin);
            open(textUnderline);
                open(textUnderline);
                    text("+[underline]");
                close();
            close();
        closeTexte();

        String expected = 
        "<TEXTE>" +
            "<AZ SO=\"DE\"/>" +     // underline on
                                    // underline on again (ignored)
                "+[underline]" +
                                    // underline off again (ignored)
            "<AZ SO=\"FI\"/>" +     // underline off (default) 
        "</TEXTE>";
        
        String actual = render();
        //System.out.println(actual);
        assertEquals("", expected, actual);
    }

    /**
     * Test rendering of text-decoration style property using the underline
     * value to turn on and the none value to turn off.
     */ 
    public void testUnderlineInverse() throws Exception {
        
        openTexte(begin);
            open(textUnderline);
                text("[underline]");
                open(textNone);
                    text("+[none]");
                close();
                text("[underline]");
            close();
        closeTexte();

        String expected =
        "<TEXTE>" +
            "<AZ SO=\"DE\"/>" +
                "[underline]" +
                "<AZ SO=\"FI\"/>" +
                    "+[none]" +
                "<AZ SO=\"DE\"/>" +
                "[underline]" +
            "<AZ SO=\"FI\"/>" +
        "</TEXTE>";
        
        String actual = render();
        //System.out.println(actual);
        assertEquals("", expected, actual);
    }

    /**
     * An extended test for the text-decoration style property underline value. 
     */ 
    public void testUnderlineSequential() throws Exception {
        
        openTexte(begin);
            open(textUnderline);
                text("[underline]");
                open(textNone);
                    text("+[none]");
                close();
                text("[underline]");
                open(textUnderline);
                    text("+[underline]");
                close();
                text("[underline]");
                open(textUnderline);
                    text("+[underline]");
                close();
                text("[underline]");
                open(textNone);
                    text("+[none]");
                close();
                text("[underline]");
            close();
        closeTexte();

        String expected =
        "<TEXTE>" +
            "<AZ SO=\"DE\"/>" +         // underline on
                "[underline]" +
                "<AZ SO=\"FI\"/>" +     // underline off
                    "+[none]" +
                "<AZ SO=\"DE\"/>" +     // underline on
                "[underline]" +
                    "+[underline]" +
                "[underline]" +
                    "+[underline]" +
                "[underline]" +
                "<AZ SO=\"FI\"/>" +     // underline off
                    "+[none]" +
                "<AZ SO=\"DE\"/>" +     // underline on
                "[underline]" +
            "<AZ SO=\"FI\"/>" +         // underline off (default)
        "</TEXTE>";
        
        String actual = render();
        //System.out.println(actual);
        assertEquals("", expected, actual);
    }

    /**
     * Test basic rendering of the text-decoration style property blink value. 
     */ 
    public void testBlink() throws Exception {
        
        openTexte(begin);
            open(textBlink);
                text("[blink]");
            close();
        closeTexte();

        String expected = 
        "<TEXTE>" +
            "<AC CL=\"DE\"/>" +     // blink on
                "[blink]" +
            "<AC CL=\"FI\"/>" +     // blink off (default) 
        "</TEXTE>";
        
        String actual = render();
        //System.out.println(actual);
        assertEquals("", expected, actual);
    }
    
    /**
     * Test basic rendering using a combination of the the color and 
     * background-color style properties to generate VDXML reverse video. 
     */ 
    public void testReverseVideo() throws Exception {
        
        openTexte(begin);
            text("[normal]");
            open(reverseVideo);
                text("[reverse]");
            close();
            text("[normal]");
        closeTexte();

        String expected = 
        "<TEXTE>" +
            "[normal]" +
            "<AC IN=\"DE\"/>" +     // reverse video on
                "[reverse]" +
            "<AC IN=\"FI\"/>" +     // reverse video off (default)
            "[normal]" +
        "</TEXTE>"; 

        String actual = render();
        //System.out.println(actual);
        assertEquals("", expected, actual);
    }

    /**
     * Test rendering using a combination of the the color and 
     * background-color style properties to generate VDXML reverse video,
     * including inverting an existing value. 
     */ 
    public void testReverseVideoInverse() throws Exception {
        
        openTexte(begin);
            text("[normal]");
            open(reverseVideo);
                text("[reverse]");
                open(begin);
                    text("[normal]");
                close();
            close();
            text("[normal]");
        closeTexte();

        String expected = 
        "<TEXTE>" +
            "[normal]" +
            "<AC IN=\"DE\"/>" +         // reverse video on
                "[reverse]" +
                "<AC IN=\"FI\"/>" +     // reverse video off (default)
                    "[normal]" +
                "<AC IN=\"DE\"/>" +     // reverse video on
            "<AC IN=\"FI\"/>" +         // reverse video off (default)
            "[normal]" +
        "</TEXTE>"; 

        String actual = render();
        //System.out.println(actual);
        assertEquals("", expected, actual);
    }

    /**
     * Test rendering of multiple display contexts (TEXTE blocks).
     * <p>
     * Current values are reset between display contexts.
     */ 
    public void testDisplayContext() throws Exception {
        
        openTexte(begin);
        open(textUnderline);
          text("[underline]");
        close();
        closeTexte();
        openTexte(begin);
        open(fontLarge);
          text("[large]");
        close();
        closeTexte();

        String expected = 
                "<TEXTE>" +
                "<AZ SO=\"DE\"/>" +     // underline on
                    "[underline]" +
                "<AZ SO=\"FI\"/>" +     // underline off (default) 
                "</TEXTE>" +
                                        // (current values reset)
                "<TEXTE>" +
                "<AC TC=\"DT\"/>" +     // large on
                    "[large]" +
                "<AC TC=\"TN\"/>" +     // medium on (default)
                "</TEXTE>";

        String actual = render();
        //System.out.println(actual);
        assertEquals("", expected, actual);
    }
    
    /**
     * Test a bug that was reported during the acceptance tests.
     * <p> 
     * The background color of the rechercher was coming out as green rather
     * than white.
     */ 
    public void testRechererBug() throws Exception {

        Styles blackWhite = StylesBuilder.getStyles(
                "background-color: white; " +
                "color: black");

        Styles blueWhite = StylesBuilder.getStyles(
                "background-color: white; " +
                "color: blue");

        Styles noneGreen = StylesBuilder.getStyles("background-color: lime");

        openTexte(blackWhite);
            open(blueWhite);
                open(noneGreen);
                    text(" 1");
                close();
            close();
            text(" rechercher");
        closeTexte();

        String expected =
        "<TEXTE>" +
            "<AC CC=\"BU\"/>" +         // foreground blue
                "<AZ CF=\"VE\"/>" +     // background green
                    "1" +
                "<AZ CF=\"BC\"/>" +     // background white (default)
            "<AC CC=\"NO\"/>" +         // foreground black (default)
            " rechercher" +
        "</TEXTE>";

        String actual = render();
        //System.out.println(actual);
        assertEquals("", expected, actual);
    }
    
    /**
     * Open an inline element.
     *
     * todo Use MCSAttributesMock rather than StyleAttributes when mocks can be used from integration tests
     *
     * @param styles the styles to use for the element.
     */ 
    private void open(Styles styles) {
        // Create an instance of MCSAttributes - the type doesn't matter
        MCSAttributes attributes = new StyleAttributes();
        attributes.setStyles(styles);
        buffer.openStyledElement(VDXMLConstants.PSEUDO_INLINE_ELEMENT,
                attributes);
    }
    
    /**
     * Add some text to the currently open element.
     * 
     * @param text
     */ 
    private void text(String text) {
        
        buffer.writeText(text);
    }

    /**
     * Close an inline element.
     */ 
    private void close() {
        
        buffer.closeElement(VDXMLConstants.PSEUDO_INLINE_ELEMENT);
    }

    /**
     * Open a TEXTE element (display context).
     *
     * todo Use MCSAttributesMock rather than StyleAttributes when mocks can be used from integration tests
     * 
     * @param begin the styles to begin this display context with.
     */ 
    private void openTexte(Styles begin) {
        // Create an instance of MCSAttributes - the type doesn't matter
        MCSAttributes attributes = new StyleAttributes();
        attributes.setStyles(begin);
        buffer.openStyledElement(VDXMLConstants.TEXT_BLOCK_ELEMENT, attributes);
    }

    /**
     * Close a TEXTE element (display context).
     */ 
    private void closeTexte() {
        
        buffer.closeElement(VDXMLConstants.TEXT_BLOCK_ELEMENT);
    }

    /**
     * Render the DOM created and return it as a string. 
     * 
     * @return the rendered DOM.
     * @throws IOException
     */ 
    private String render() throws IOException {
        
        outputter.output(buffer.getCurrentElement());
        String actual = writer.toString();
        return actual;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10527/3	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	10505/7	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/5	pduffin	VBM:2005111405 Massive changes for performance

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (6)

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 25-Nov-05	10453/1	rgreenall	VBM:2005092107 Restrict the length of lines written by MCS for devices that have a maximum line limit.

 25-Nov-05	9708/2	rgreenall	VBM:2005092107 Restrict the length of lines written by MCS for devices that have a maximum line limit.

 25-Nov-05	9708/2	rgreenall	VBM:2005092107 Restrict the length of lines written by MCS for devices that have a maximum line limit.

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 15-Sep-05	9512/1	pduffin	VBM:2005091408 Committing changes to JIBX to use new schema

 13-Sep-05	9496/1	pduffin	VBM:2005091211 Replaced text-decoration with individual properties

 22-Jul-05	8859/1	emma	VBM:2005062006 Modify transformers to take account of Styles when flattening/optimizing tables

 14-Jul-05	9039/1	emma	VBM:2005071401 Adding get/setSynthesizedValues to PropertyValues

 30-Jun-05	8893/1	emma	VBM:2005062406 Annotate DOM elements generated from VDXML with styles

 06-May-05	8090/1	emma	VBM:2005050411 Fixing broken css underline emulation for WML

 06-May-05	8048/1	emma	VBM:2005050411 Fixing broken css underline emulation for WML

 19-Nov-04	5733/2	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 23-Sep-04	5599/1	geoff	VBM:2004092214 Port VDXML to MCS: port existing protocol code

 08-Jun-04	4575/13	geoff	VBM:2004051807 Minitel VDXML protocol support (fix rechercher color bug)

 02-Jun-04	4575/11	geoff	VBM:2004051807 Minitel VDXML protocol support (reverse video, tests and some cleanup)

 28-May-04	4575/9	geoff	VBM:2004051807 Minitel VDXML protocol support (add block test case)

 28-May-04	4575/6	geoff	VBM:2004051807 Minitel VDXML protocol support (fix underline)

 28-May-04	4575/4	geoff	VBM:2004051807 Minitel VDXML protocol support (incomplete inline integration)

 27-May-04	4575/1	geoff	VBM:2004051807 Minitel VDXML protocol support

 ===========================================================================
*/
