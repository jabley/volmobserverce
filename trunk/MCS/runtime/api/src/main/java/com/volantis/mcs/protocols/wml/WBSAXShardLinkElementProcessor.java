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
package com.volantis.mcs.protocols.wml;

import com.volantis.mcs.dissection.dom.ElementType;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom2wbsax.WBSAXProcessorContext;
import com.volantis.mcs.dom2wbsax.WBSAXValueProcessor;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.dissection.DissectionConstants;
import com.volantis.mcs.wbdom.dissection.ShardLinkOpaqueValue;
import com.volantis.mcs.wbsax.WBSAXException;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.Arrays;

/**
 * Element processor which registers an "href" attribute processor for
 * shard links.
 * <p/>
 * This will translate any href attributes containing the value defined in
 * {@link DissectionConstants#URL_MAGIC_CHAR} into a special subclass of
 * {@link com.volantis.mcs.wbsax.OpaqueValue} for to enable customised shard
 * link processing.
 */
public final class WBSAXShardLinkElementProcessor
        extends WBSAXDissectionElementProcessor {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(
                    WBSAXShardLinkElementProcessor.class);

    /**
     * Char array version of {@link DissectionConstants#URL_MAGIC_CHAR}.
     */
    private static final char[] SHARD_LINK_MAGIC_URL_VALUE =
            DissectionConstants.URL_MAGIC_CHAR.toCharArray();

    /**
     * The attribute value processor to use for shard link attribute values.
     */
    private final ShardLinkAttributeProcessor shardLinkAttributeProcessor;

    public WBSAXShardLinkElementProcessor(
            WBSAXProcessorContext context,
            ElementType type) {

        super(context, type);

        // Set up a special attribute value processor for shard links.
        shardLinkAttributeProcessor = new ShardLinkAttributeProcessor(
                defaultAttributeValueProcessor);

    }

    public void elementStart(
            Element element,
            boolean content) throws WBSAXException {

        super.elementStart(element, content);
        
        // Indicate that we should check elements in the contents for 
        // the magic URL marker
        if (content) {
            // Only need to set if the element has content.
            context.pushAttributeValueProcessor("href",
                                                shardLinkAttributeProcessor);
        }

    }

    public void elementEnd(Element element, boolean content) throws WBSAXException {

        context.popAttributeValueProcessor("href");

        super.elementEnd(element, content);

    }

    private final class ShardLinkAttributeProcessor implements WBSAXValueProcessor {
        private final WBSAXValueProcessor parent;

        public ShardLinkAttributeProcessor(WBSAXValueProcessor parent) {
            this.parent = parent;
        }

        public void value(char[] value, int length) throws WBSAXException {
            // If this attribute contains the magic url marker...
            if (Arrays.equals(SHARD_LINK_MAGIC_URL_VALUE, value)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Got magic url");
                }
                // Add an opaque value
                context.getContentHandler().addAttributeValueOpaque(
                        new ShardLinkOpaqueValue());
            } else {
                // No magic url marker found. Parse data as normal.
                parent.value(value, length);
            }
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 02-Oct-03	1469/4	geoff	VBM:2003091701 Emulate Openwave-Style menus for non Openwave WML 1.2 protocols

 ===========================================================================
*/
