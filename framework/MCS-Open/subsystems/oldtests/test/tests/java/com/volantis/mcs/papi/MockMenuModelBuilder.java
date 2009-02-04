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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.papi;

import com.volantis.mcs.protocols.FormatReference;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.ShortcutProperties;
import com.volantis.mcs.protocols.assets.ImageAssetReference;
import com.volantis.mcs.protocols.assets.LinkAssetReference;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.protocols.menu.builder.BuilderException;
import com.volantis.mcs.protocols.menu.builder.MenuModelBuilder;
import com.volantis.mcs.protocols.menu.model.EventType;
import com.volantis.mcs.protocols.menu.model.Menu;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.protocols.assets.ScriptAssetReference;
import com.volantis.styling.values.PropertyValues;
import com.volantis.styling.Styles;

/**
 * Mock implementation for the menu model Builder.
 */
public class MockMenuModelBuilder implements MenuModelBuilder {
    private String title;
    private TextAssetReference shortcut;
    private LinkAssetReference href;
    private TextAssetReference prompt;
    private String offcolor;
    private ImageAssetReference offimage;
    private String oncolor;
    private ImageAssetReference onimage;
    private String segment;
    private String target;
    private OutputBuffer text;
    private ShortcutProperties shortcutProperties;


    // javadoc inherited.
    public void endIcon() throws BuilderException {
    }

    // javadoc inherited.
    public void endLabel() throws BuilderException {
    }

    // javadoc inherited.
    public Menu endMenu() throws BuilderException {
        return null;
    }

    // javadoc inherited.
    public void endMenuGroup() throws BuilderException {
    }

    // javadoc inherited.
    public void endMenuItem() throws BuilderException {
    }

    // javadoc inherited.
    public void endText() throws BuilderException {
    }

    // javadoc inherited.
    public Menu getCompletedMenuModel() {
        return null;
    }

    // javadoc inherited.
    public void setDeprecatedNormalColour(String colour) throws BuilderException {
        this.oncolor = colour;
    }

    /**
     * Get the oncolor member variable.
     *
     * @return the oncolor member variable.
     */
    public String getOncolor() {
        return oncolor;
    }

    // javadoc inherited.
    public void setDeprecatedOverColour(String colour) throws BuilderException {
        this.offcolor = colour;
    }

    public void setShortcutProperties(ShortcutProperties shortcutProperties)
            throws BuilderException {
        this.shortcutProperties = shortcutProperties;
    }

    /**
     * Get the offcolor member variable.
     *
     * @return the offcolor member variable.
     */
    public String getOffcolor() {
         return offcolor;
     }

    // javadoc inherited.
    public void setErrorMessage(TextAssetReference message) throws BuilderException {
    }

    // javadoc inherited.
    public void setEventHandler(EventType eventType,
                                ScriptAssetReference handler) throws BuilderException {
    }

    // javadoc inherited.
    public void setHelp(TextAssetReference help) throws BuilderException {
    }

    // javadoc inherited.
    public void setHref(LinkAssetReference href) throws BuilderException {
        this.href = href;
    }

    /**
     * Get the href member variable.
     *
     * @return the href member variable.
     */
    public LinkAssetReference getHref() {
         return href;
     }

    // javadoc inherited.
    public void setNormalImageURL(ImageAssetReference url) throws BuilderException {
        this.onimage = url;
    }

    /**
     * Get the onimage member variable.
     *
     * @return the onimage member variable.
     */

    public ImageAssetReference getOnimage() {
        return onimage;
    }

    // javadoc inherited.
    public void setOverImageURL(ImageAssetReference url) throws BuilderException {
        this.offimage = url;
    }

    /**
     * Get the offimage member variable.
     *
     * @return the offimage member variable.
     */
    public ImageAssetReference getOffimage() {
        return offimage;
    }

    // javadoc inherited.
    public void setPane(FormatReference pane) throws BuilderException {
    }

    // javadoc inherited.
    public void setPrompt(TextAssetReference prompt) throws BuilderException {
        this.prompt = prompt;
    }


    public TextAssetReference getPrompt() {
        return prompt;
    }

    // javadoc inherited.
    public void setSegment(String segment) throws BuilderException {
        this.segment = segment;
    }

    /**
     * Get the segment member variable.
     *
     * @return the segment member variable.
     */
    public String getSegment() {
        return segment;
    }

    // javadoc inherited.
    public void setShortcut(TextAssetReference shortcut) throws BuilderException {
        this.shortcut = shortcut;
    }

    /**
     * Get the shortcut member variable.
     *
     * @return the shortcut member variable.
     */
    public TextAssetReference getShortcut() {
        return shortcut;
    }

    // javadoc inherited.
    public void setElementDetails(
            String elementName,
            String id,
            Styles styles)
            throws BuilderException {
    }

    // javadoc inherited.
    public void setTarget(String target) throws BuilderException {
        this.target = target;
    }

    public String getTarget() {
        return target;
    }

    // javadoc inherited.
    public void setText(OutputBuffer text) throws BuilderException {
        this.text = text;
    }

    /**
     * Get the text member variable.
     *
     * @return the text member variable.
     */
    public OutputBuffer getText() {
        return text;
    }

    // javadoc inherited.
    public void setTitle(String title) throws BuilderException {
        this.title = title;
    }

    /**
     * Get the title member variable.
     *
     * @return the title member variable.
     */
    public String getTitle() {
        return title;
    }

    // javadoc inherited.
    public void startIcon() throws BuilderException {
    }

    // javadoc inherited.
    public void startLabel() throws BuilderException {
    }

    // javadoc inherited.
    public void startMenu() throws BuilderException {
    }

    // javadoc inherited.
    public void startMenuGroup() throws BuilderException {
    }

    // javadoc inherited.
    public void startMenuItem() throws BuilderException {
    }

    // javadoc inherited.
    public void startText() throws BuilderException {
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Aug-05	9353/1	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 27-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 16-Feb-05	6129/5	matthew	VBM:2004102019 yet another supermerge

 16-Feb-05	6129/3	matthew	VBM:2004102019 yet another supermerge

 23-Nov-04	6129/1	matthew	VBM:2004102019 Enable shortcut menu link rendering

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 13-Sep-04	5371/1	byron	VBM:2004083102 Title attribute on the <menuitem> element is being ignored

 ===========================================================================
*/
