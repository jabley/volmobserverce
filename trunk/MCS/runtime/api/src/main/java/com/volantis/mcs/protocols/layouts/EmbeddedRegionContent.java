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

package com.volantis.mcs.protocols.layouts;

import com.volantis.mcs.context.OutputBufferStack;
import com.volantis.mcs.dom.WalkingDOMVisitorStub;
import com.volantis.mcs.dom.DOMWalker;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.NodeSequence;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.OutputBufferFactory;
import com.volantis.mcs.protocols.RegionContent;
import com.volantis.mcs.protocols.renderer.layouts.FormatRendererContext;

/**
 * Encapsulates region content embedded within a {@link RegionInstance}.
 */
public class EmbeddedRegionContent
        implements RegionContent {

    /**
     * The output buffer from the {@link RegionInstance}.
     */
    private final DOMOutputBuffer outputBuffer;

    /**
     * A flag that indicates whether this contains special
     * {@link RegionInstance#REGION_CONTENT_ELEMENT} elements.
     *
     * <p>If this is true then any special elements should be replaced with
     * the results of processing the {@link RegionContent} with which they are
     * annotated.</p>
     */
    private final boolean containsNestedContent;

    /**
     * Initialise.
     *
     * @param outputBuffer          The buffer to wrap.
     * @param containsNestedContent True if the buffer contains special
     *                              elements that need rendering.
     */
    public EmbeddedRegionContent(
            DOMOutputBuffer outputBuffer,
            boolean containsNestedContent) {

        this.outputBuffer = outputBuffer;
        this.containsNestedContent = containsNestedContent;
    }

    // Javadoc inherited.
    public void render(final FormatRendererContext context) {
        OutputBufferStack outputBufferStack = context.getOutputBufferStack();
        DOMOutputBuffer currentBuffer = (DOMOutputBuffer)
                outputBufferStack.getCurrentOutputBuffer();

        // Get the contents from the region buffer and add them into the
        // current buffer.
        NodeSequence nodes = outputBuffer.removeContents();
        currentBuffer.addNodeSequence(nodes);

        if (containsNestedContent) {
            // Iterate over the nodes that have been added to
            DOMWalker walker = new DOMWalker(new RegionContentVisitor(context));
            walker.walk(nodes);
        }
    }

    /**
     * Visit the region content looking for
     * {@link RegionInstance#REGION_CONTENT_ELEMENT}.
     */
    private static class RegionContentVisitor
            extends WalkingDOMVisitorStub {

        /**
         * The context within which this is running.
         */
        private final FormatRendererContext context;

        /**
         * The factory to use to create new {@link OutputBuffer}s.
         */
        private final OutputBufferFactory outputBufferFactory;

        /**
         * The stack of {@link OutputBuffer}.
         */
        private final OutputBufferStack outputBufferStack;

        /**
         * Initialise.
         *
         * @param context The context within which this is running.
         */
        public RegionContentVisitor(FormatRendererContext context) {
            this.context = context;
            outputBufferStack = context.getOutputBufferStack();
            outputBufferFactory =
                    context.getLayoutModule().getOutputBufferFactory();
        }

        public void visit(Element element) {
            String name = element.getName();
            if (name.equals(RegionInstance.REGION_CONTENT_ELEMENT)) {

                // Create a buffer into which the content for the nested region
                // will be added and push it onto the stack.
                DOMOutputBuffer buffer = (DOMOutputBuffer)
                        outputBufferFactory.createOutputBuffer();
                outputBufferStack.pushOutputBuffer(buffer);

                // Render the nested region content.
                RegionContent content = (RegionContent) element.getAnnotation();
                content.render(context);

                // Pop the temporary buffer of the stack.
                outputBufferStack.popOutputBuffer(buffer);

                // Replace the current element with the contents of the
                // temporary buffer.
                NodeSequence sequence = buffer.removeContents();
                element.replaceWith(sequence);
            }
        }
    }
}
