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
package com.volantis.mcs.protocols.wml.menu;

import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.TestDOMOutputBuffer;
import com.volantis.mcs.protocols.assets.implementation.LiteralLinkAssetReference;
import com.volantis.mcs.protocols.menu.builder.BuilderException;
import com.volantis.mcs.protocols.menu.builder.MenuModelBuilder;
import com.volantis.mcs.protocols.menu.model.Menu;
import com.volantis.mcs.protocols.menu.model.MenuItem;
import com.volantis.mcs.protocols.menu.shared.builder.ConcreteMenuModelBuilder;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuItemBracketingRendererTestAbstract;

import java.util.MissingResourceException;

/**
 * Test {@link OpenwaveOptionMenuItemRenderer}.
 */ 
public class OpenwaveOptionMenuItemRendererTestCase
        extends MenuItemBracketingRendererTestAbstract {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2004.";

    /**
     * Test the minimal valid rendering of the option, i.e. with a null title.
     *  
     * @throws Exception
     */ 
    public void testEmpty() throws Exception {
        // href is mandatory (in the builder)
        checkMenuItem("empty item href", null);
    }

    /**
     * Test the maximal valid rendering of the option, i.e. with a title.
     * 
     * @throws Exception
     */ 
    public void testFull() throws Exception {
        checkMenuItem("full item href", "full item title");
    }

    /**
     * Helper method to check the rendering of an Openwave option menu item
     * renderer. This renders an option with the href and title supplied and 
     * checks that the rendered result is as expected.
     * 
     * @param href the href of the option; must not be null.
     * @param title the title of the option, may be null.
     * @throws Exception
     */ 
    private void checkMenuItem(String href, String title) 
            throws Exception {

        // Create the menu item.
        String text = "item text";
        Menu menu = createSimpleMenu(title, href, text);
        MenuItem item = (MenuItem) menu.get(0);
        
        // Render the menu item.
        OpenwaveOptionMenuItemRenderer renderer =
                new OpenwaveOptionMenuItemRenderer();

        String actual = getRenderOutputAsString(renderer, item, text);
        //System.out.println(actual);
        
        // Assemble the expected value (rendered menu item).
        String expected =
                "<option" + 
                        attr("onpick", href) +
                        attr("title", title) + ">" +
                    text +
                "</option>";
        expected = DOMUtilities.provideDOMNormalizedString(expected);
        
        // Compare the expected value we calculated with the actual value 
        // which was rendered.
        assertEquals("Simple menu item not as expected", expected, actual);
    }

    /**
     * Create a simple menu containing a single menu item which can be used to 
     * exercise a menu item renderer.
     * 
     * @param title the title of each menu item.
     * @param href the href of each menu item.
     * @param text the text of each menu item.
     * @return a completed menu.
     * @throws BuilderException if there was a problem building the menu.
     */ 
    private Menu createSimpleMenu(String title, String href, String text) 
            throws BuilderException {
        try {
            // Build a very simple menu with a single item which contains a 
            // label with some text.
            MenuModelBuilder builder = new ConcreteMenuModelBuilder();
    
            builder.startMenu();
    
            builder.startMenuItem();
            builder.setTitle(title);
            builder.setHref(new LiteralLinkAssetReference(href));

            builder.startLabel();
    
            builder.startText();
            DOMOutputBuffer dom = new TestDOMOutputBuffer();
            dom.writeText(text);
            builder.setText(dom);
            builder.endText();
            
            builder.endLabel();
    
            builder.endMenuItem();
            
            builder.endMenu();
            
            return builder.getCompletedMenuModel();
        } catch (MissingResourceException e) {
            // work around bug in junit?
            throw new BuilderException(e);
        }
    }
    
    /**
     * Create an attribute name=value pair, if the value is not null.
     *
     * @param name name of the attribute
     * @param value value of the attribute, may be null.
     * @return the name=value pair, or empty string if the value is null.
     * @todo this was cut and pasted from 
     * {@link com.volantis.testtools.config.ConfigFileBuilder}. Maybe we should
     * factor this out somewhere?
     */
    private String attr(String name, Object value) {
        if (value != null) {
            return " " + name + "=\"" + String.valueOf(value) + "\"";
        } else {
            return "";
        }
    }
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 29-Apr-04	4013/1	pduffin	VBM:2004042210 Restructure menu item renderers

 26-Apr-04	3920/2	geoff	VBM:2004041910 Enhance Menu Support: Renderers: HTML Menu Item Renderers

 21-Apr-04	3681/1	philws	VBM:2004033104 Menu Separator Renderer and Menu Buffer Management updates and initial DefaultMenuRenderer implementation

 15-Apr-04	3645/3	geoff	VBM:2004032904 Enhance Menu Support: Open Wave Menu Renderer

 15-Apr-04	3645/1	geoff	VBM:2004032904 Enhance Menu Support: Open Wave Menu Renderer

 ===========================================================================
*/
