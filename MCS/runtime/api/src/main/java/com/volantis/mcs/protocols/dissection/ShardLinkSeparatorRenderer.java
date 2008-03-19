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

import com.volantis.mcs.dissection.links.ShardLinkConditionalAttributes;
import com.volantis.mcs.dissection.links.rules.ShardLinkContentRule;
import com.volantis.mcs.dissection.links.rules.StandardContentRules;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.renderer.RendererException;
import com.volantis.mcs.protocols.separator.SeparatorRenderer;

/**
 * A decorating separator renderer which renders shard link conditional markup
 * around a standard separator renderer.
 * <p>
 * This is used to render separators for use in shard link menus.
 *
 * @see ShardLinkMenuModelBuilder
 */
public final class ShardLinkSeparatorRenderer implements SeparatorRenderer {

    /**
     * The copyright statement.
     */
    private static final String mark = "(c) Volantis Systems Ltd 2004.";

    /**
     * The standard separator rule to use for the shard link conditional
     * attributes.
     */
    private static final ShardLinkContentRule SEPARATOR_RULE =
            StandardContentRules.getSeparatorRule();

    /**
     * The separator which we delegate to to render the standard markup.
     */
    private final SeparatorRenderer delegate;

    /**
     * Construct an instance of this class.
     *
     * @param delegate used to render normal separator markup.
     */
    public ShardLinkSeparatorRenderer(SeparatorRenderer delegate) {
        this.delegate = delegate;
    }

    // Javadoc inherited.
    public void render(OutputBuffer buffer)
            throws RendererException {

        DOMOutputBuffer dom = (DOMOutputBuffer) buffer;

        // Open the SHARD LINK CONDITIONAL element
        Element shardLinkConditional = dom.openElement(
                DissectionConstants.SHARD_LINK_CONDITIONAL_ELEMENT);
        // Add the attributes for the element, luckily in this case
        // they are constant so we don't need any extra state for this
        // renderer.
        ShardLinkConditionalAttributes conditionalAttrs
                = new ShardLinkConditionalAttributes();
        conditionalAttrs.setContentRule(SEPARATOR_RULE);
        shardLinkConditional.setAnnotation(conditionalAttrs);

        // Render the child separator.
        delegate.render(buffer);

        // Close the SHARD LINK CONDITIONAL element
        dom.closeElement(shardLinkConditional);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Mar-05	7357/2	pcameron	VBM:2005030906 Fixed node annotation for dissection

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 28-Apr-04	4048/1	geoff	VBM:2004042606 Enhance Menu Support: WML Dissection

 ===========================================================================
*/
