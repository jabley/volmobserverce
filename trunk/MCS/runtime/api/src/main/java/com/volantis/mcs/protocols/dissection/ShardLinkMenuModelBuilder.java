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
package com.volantis.mcs.protocols.dissection;

import com.volantis.mcs.dissection.DissectableAreaIdentity;
import com.volantis.mcs.dissection.links.ShardLinkAction;
import com.volantis.mcs.dissection.links.ShardLinkAttributes;
import com.volantis.mcs.dissection.links.ShardLinkGroupAttributes;
import com.volantis.mcs.layouts.DissectingPane;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.DissectingPaneAttributes;
import com.volantis.mcs.protocols.OutputBufferFactory;
import com.volantis.mcs.protocols.assets.LinkAssetReference;
import com.volantis.mcs.protocols.assets.implementation.LiteralLinkAssetReference;
import com.volantis.mcs.protocols.assets.implementation.LiteralTextAssetReference;
import com.volantis.mcs.protocols.menu.model.MenuLabel;
import com.volantis.mcs.protocols.menu.shared.model.ConcreteElementDetails;
import com.volantis.mcs.protocols.menu.shared.model.ConcreteMenuLabel;
import com.volantis.mcs.protocols.menu.shared.model.ConcreteMenuText;
import com.volantis.styling.Styles;

/**
 * This class builds a shard link specific "extended" menu model for a shard 
 * link menu.
 * <p>
 * The dissector requires that the normal menu markup for shard link menus 
 * is itself marked up with special dissector specific markup because the 
 * dissector decides which parts of the shard link menu are shown after it 
 * attempts dissection (either nothing, a next link, a previous link or both). 
 * <p>
 * The extended menu model created by this class contains shard link 
 * information which is designed to be picked up by the various 
 * ShardLink*Renderers which decorate the normal renderers. These decorating 
 * renderers in turn pick out the shard link information in the extended menu 
 * model and use this to create the extra shard link specific markup for the 
 * dissector around the various parts of the normal menu markup.
 * <p>
 * As such, this builder must only be used to create menus which are in turn
 * rendered using the ShardLink*Renderers, and vice versa.
 * <p>
 * Note that shard link menus must render with horizontal orientation by
 * default, unlike normal menus which render vertically by default.
 */ 
public final class ShardLinkMenuModelBuilder {

    private static final LinkAssetReference MAGIC_URL
            = new LiteralLinkAssetReference(DissectionConstants.URL_MAGIC_CHAR);

    /**
     * The factory to use for creating output buffers.
     */ 
    private final OutputBufferFactory outputBufferFactory;
    
    /**
     * Construct an instance of this class.
     */
    public ShardLinkMenuModelBuilder(
            OutputBufferFactory outputBufferFactory) {

        this.outputBufferFactory = outputBufferFactory;
    }

    /**
     * Build a menu for shard links from a dissecting pane attributes.
     * <p>
     * This extracts all the information which is relevant to the shard link
     * list from the dissecting pane attributes and uses it to populate an 
     * "extended" menu model. 
     * <p>
     * Users of this menu model must downcast to the
     * various ShardLinkMenu* classes to extract the full information 
     * available.
     * 
     * @param attributes the dissecting pane attributes to use as input.
     * @return the created extended menu.
     */ 
    public ShardLinkMenu buildShardLinkMenuModel(DissectingPaneAttributes attributes) {
        
        DissectingPane pane = attributes.getDissectingPane();
        Styles styles = attributes.getStyles();

        // Create the next shard link.
        // todo style these properly somehow.
        ShardLinkMenuItem nextLink = createShardLinkMenuItem(true,
                attributes.getLinkText(),
                styles, pane.getNextShardShortcut());

        // Create the previous shard link.
        // todo style these property somehow.
        ShardLinkMenuItem previousLink = createShardLinkMenuItem(false,
                attributes.getBackLinkText(),
                styles, pane.getPreviousShardShortcut());

        // Create the shard link menu with appropriate attributes.
        ShardLinkMenu menu = createShardLinkMenu(pane.getName(),
                attributes.getInclusionPath(),
                styles);

        // Add the shard link items to the menu in the correct order.
        if (attributes.isNextLinkFirst()) {
            menu.add(nextLink);
            menu.add(previousLink);
        } else {
            menu.add(previousLink);
            menu.add(nextLink);
        }

        return menu;
    }

    /**
     * Create a menu to contain menu items which are shard links.
     * 
     * @param paneName the name of the dissecting pane for the shard links.
     * @param inclusionPath The identity of the inclusion, this is a
     *      hierarchical string of regions and indexes of inclusions within
     *      the region.
     * @param styles the styles which should be applied to this menu
     * @return the created menu.
     */ 
    private ShardLinkMenu createShardLinkMenu(
            String paneName,
            String inclusionPath,
            Styles styles) {

        ShardLinkGroupAttributes groupAttrs = new ShardLinkGroupAttributes();
        groupAttrs.setDissectableArea(
                new DissectableAreaIdentity(inclusionPath, paneName));
        ShardLinkMenu menu = new ShardLinkMenu(
                createElementDetails(styles), groupAttrs);

        // Return the created menu for the shard links.
        return menu;
    }

    /**
     * Create a menu item which is a shard link.
     * 
     * @param isNextLink flag which indicates if this is the next or previous
     *      shard link.
     * @param content the textual content of the shard link.
     * @param styles the styles which should be applied to this menu item
     * @param shortcut the shortcut to use to navigate to the related shard.
     * @return the created menu item.
     */ 
    private ShardLinkMenuItem createShardLinkMenuItem(
            boolean isNextLink,
            String content,
            Styles styles,
            String shortcut) {

        // Create the text in a label (both with no style info).
        DOMOutputBuffer output = (DOMOutputBuffer) 
                outputBufferFactory.createOutputBuffer();
        ConcreteMenuText text = new ConcreteMenuText(null);
        output.writeText(content);
        text.setText(output);
        MenuLabel label = new ConcreteMenuLabel(null, text);

        // Create the shard link attributes
        ShardLinkAttributes shardLinkAttrs = new ShardLinkAttributes();
        if (isNextLink) {
            shardLinkAttrs.setAction(ShardLinkAction.NEXT);
        } else {
            shardLinkAttrs.setAction(ShardLinkAction.PREVIOUS);
        }

        // Create the menu item itself.
        ShardLinkMenuItem item =
                new ShardLinkMenuItem(createElementDetails(styles),
                                      label, shardLinkAttrs);
        item.setHref(MAGIC_URL);
        item.setShortcut(new LiteralTextAssetReference(shortcut));

        // Return the created menu item for the shard link.
        return item;
    }

    /**
     * Creates a suitable element details object for use within a shard link
     * menu. If the associated css emulator is not null, the style property
     * values of the dissecting pane layout are copied into the new element
     * details.
     *
     * @param styles        The styles which should be applied to the shard link.
     * @return An initialised element details with inherited styles where
     * appropriate.
     */
    private ConcreteElementDetails createElementDetails(Styles styles) {

        // Create element details for the menu item using shard link style class.
        ConcreteElementDetails itemStyle = new ConcreteElementDetails();
        itemStyle.setStyles(styles);

        // Return the created and initialised style info
        return itemStyle;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 30-Aug-05	9353/2	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 22-Aug-05	9298/2	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 09-Aug-05	9151/4	pduffin	VBM:2005080205 Recommitted after super merge

 04-Aug-05	9151/1	pduffin	VBM:2005080205 Removing a lot of unnecessary styling code

 22-Jul-05	8859/1	emma	VBM:2005062006 Modify transformers to take account of Styles when flattening/optimizing tables

 14-Jul-05	9039/1	emma	VBM:2005071401 Adding get/setSynthesizedValues to PropertyValues

 30-Jun-05	8893/1	emma	VBM:2005062406 Annotate DOM elements generated from VDXML with styles

 27-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 23-Jun-05	8483/5	emma	VBM:2005052410 Modifications after review

 22-Jun-05	8483/3	emma	VBM:2005052410 Modifications after review

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 01-Oct-04	5635/1	geoff	VBM:2004092216 Port VDXML to MCS: update menu support

 20-Sep-04	5575/1	tom	VBM:2004091402 Previous and Next work with correct names and are the right way around

 17-Sep-04	5527/3	tom	VBM:2004091402 Previous and Next work with correct names and are the right way around

 02-Sep-04	5354/3	tom	VBM:2004082008 Optimized imports and defuncted StandardStyleProperties

 02-Sep-04	5354/1	tom	VBM:2004082008 started device theme normalization

 17-May-04	4438/1	claire	VBM:2004051702 Fix null pointer with generating dissecting panes in WML with no style

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 11-May-04	4246/1	philws	VBM:2004050709 Fix StyleInfo handling for MenuIcon, MenuText and MenuLabel

 28-Apr-04	4048/4	geoff	VBM:2004042606 Enhance Menu Support: WML Dissection

 28-Apr-04	4048/2	geoff	VBM:2004042606 Enhance Menu Support: WML Dissection

 ===========================================================================
*/
