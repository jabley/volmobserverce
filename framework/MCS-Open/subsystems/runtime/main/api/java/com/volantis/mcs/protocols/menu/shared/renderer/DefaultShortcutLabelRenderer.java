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
package com.volantis.mcs.protocols.menu.shared.renderer;

import com.volantis.mcs.policies.variants.text.TextEncoding;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.Inserter;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.ShortcutProperties;
import com.volantis.mcs.protocols.SpanAttributes;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.protocols.menu.MenuModuleCustomisation;
import com.volantis.mcs.protocols.menu.model.ElementDetails;
import com.volantis.mcs.protocols.menu.model.Menu;
import com.volantis.mcs.protocols.menu.model.MenuItem;
import com.volantis.mcs.protocols.renderer.RendererException;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.values.StyleKeywords;
import com.volantis.styling.Styles;

/**
 * This class provides a default implementation of ShortcutLabelRenderer.
 * Shortcut labels are the bit of text displays to indicate which shortcut key
 * can be used to access a menu item. ("A. thingy" where "A. " is the shortcut
 * label). Note that the ". " part is usually specified as a
 * menu:mcs-shortcut:after {content: ". "}
 * tag in the theme.
 */
public final class DefaultShortcutLabelRenderer implements ShortcutLabelRenderer {

    /**
     * The span output used to render the shortcut label.
     */
    private final DeprecatedSpanOutput span;

    /**
     * The menu customisation used to render this menu item.
     */
    private final MenuModuleCustomisation customisation;

    /**
     * The inserter used to insert content into the document.
     */
    private final Inserter inserter;

    /**
     * Constructor that takes an object that knows how to render a protocol
     * specific span
     * @param span the Object that can render a span.
     */
    public DefaultShortcutLabelRenderer(DeprecatedSpanOutput span,
                                        MenuModuleCustomisation customisation,
                                        Inserter inserter){
        this.span = span;
        this.customisation = customisation;
        this.inserter = inserter;
    }

    /**
     * Render a shortcut label for the specified menu item.
     * @param buffer the buffer to which we will render the shortcut.
     * @param item the menu item for which the label will be created.
     * @throws RendererException
     */
    public void render(OutputBuffer buffer, MenuItem item)
            throws RendererException {
        try {

            if (buffer != null && buffer instanceof DOMOutputBuffer) {
                DOMOutputBuffer domBuffer = (DOMOutputBuffer) buffer;

                // get the shortcut properties from the menu.
                Menu menu = item.getMenu();
                TextAssetReference shortcutValue = item.getShortcut();
                if (menu != null && shortcutValue != null &&
                        customisation.supportsAccessKeyAttribute()) {

                    String shortcutTextValue =
                            shortcutValue.getText(TextEncoding.PLAIN);

                    if (shortcutTextValue != null) {
                        // this is never null.
                        ShortcutProperties shortcutProps =
                                menu.getShortcutProperties();

                        ElementDetails elementDetails = menu.getElementDetails();

                        if (elementDetails != null) {

                            SpanAttributes attributes = new SpanAttributes();
                            // Should only render out a surrounding span
                            // element if the device supports it.
                            if (shortcutProps.supportsSpan()) {
                                // Create span attributes from the menu's
                                // attributes. There are two mechanisms used
                                // here to support real css and the css
                                // emulation provided by MCS.  The style class
                                // VE-... is used for devices that support css
                                // and the css renderers need to ensure they
                                // generate appropriate style rules.  The
                                // pseudo element is used by those protocols
                                // that work through css emulation.
                                attributes.setId(elementDetails.getId());

                                // Need to explicitly set the display to inline
                                // (even though the shortcut should use the
                                // menu styles, we know display should always
                                // be inline in this case).
                                final Styles styles =
                                        elementDetails.getStyles().copy();
                                styles.getPropertyValues().setSpecifiedValue(
                                        StylePropertyDetails.DISPLAY,
                                        StyleKeywords.INLINE);

                                // The tag name and style class are
                                // conditionally added into the attributes
                                // because CSS emulation requires the tag name
                                // but no descendent selector in order to work
                                // correctly while real CSS supporting protocols
                                // would create too many style classes on the
                                // shortcut if the tag is specified and would
                                // not generate the required markup without the
                                // style class.
                                if (customisation.supportsStyleSheets()) {
                                    attributes.setTagName(null);
                                    attributes.setStyles(styles);
                                } else {
                                    attributes.setTagName(
                                            elementDetails.getElementName());
                                    attributes.setStyles(styles);
                                }

                                span.openSpan(domBuffer, attributes);
                            }

                            // We explicitly tell MCS not to discard whitespace
                            // in this element, just in case the separator
                            // starts or ends with whitespace (this may be a
                            // work-around for an actual bug in whitespace
                            // handling in the output buffers, but that isn't
                            // clear at the moment).
                            domBuffer.setElementIsPreFormatted(true);

                            domBuffer.writeText(shortcutTextValue);

                            inserter.insertPreservingExistingContent(
                                    domBuffer.getCurrentElement(),
                                    shortcutProps.getSeparatorStyleValue());

                            if (shortcutProps.supportsSpan()) {
                                span.closeSpan(domBuffer, attributes);
                            }
                        }
                    }
                }

            }

        } catch (ProtocolException protException) {
            // just wrap it and rethrow
            throw new RendererException(protException);
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Aug-05	9353/4	pduffin	VBM:2005081912 Removed style class from MCS Attributes, also removed getColor() from Style

 30-Aug-05	9353/1	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 25-Aug-05	9377/1	schaloner	VBM:2005071102 Migrated mcs-shortcut-after to mcs-shortcut and after

 27-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 27-Jan-05	6129/4	matthew	VBM:2004102019 supermerge required

 23-Nov-04	6129/1	matthew	VBM:2004102019 Enable shortcut menu link rendering

 ===========================================================================
*/
