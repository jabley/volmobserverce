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

package com.volantis.mcs.protocols.separator.shared;

import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.renderer.RendererException;
import com.volantis.mcs.protocols.separator.SeparatedContent;
import com.volantis.mcs.protocols.separator.SeparatorArbitrator;
import com.volantis.mcs.protocols.separator.SeparatorRenderer;

/**
 * Default implementation of the separator manager.
 * <p>This extends {@link DefaultArbitratorDecision} purely for implementation
 * convenience as it saves one object.</p>
 */
public class DefaultSeparatorManager
        extends AbstractSeparatorManager {

    /**
     * The content that was used previously.
     */
    private SeparatedContent previousContent;

    /**
     * The deferred separator.
     */
    private SeparatorRenderer deferredSeparator;

    /**
     * Initialise this instance.
     * @param outputBuffer The outputBuffer to which the renderers managed by
     * this separator will render themselves, may not be null.
     * @param arbitrator The object responsible for deciding whether a renderer
     * is invoked, may not be null.
     */
    public DefaultSeparatorManager(OutputBuffer outputBuffer,
                                   SeparatorArbitrator arbitrator) {
        super(outputBuffer, arbitrator);

    }

    // Javadoc inherited
    public void queueSeparator(SeparatorRenderer separator)
            throws RendererException {

        // Make sure that this is in a valid state before this method starts.
        checkInvariant();

        if (deferredSeparator == null) {
            deferredSeparator = separator;
        } else {

            decision.setAllowDeferral(true);

            // There is a deferred separator so choose between it and the
            // current one.
            decision.setDeferredSeparator(deferredSeparator);
            decision.setPreviousContent(null);
            decision.setTriggeringContent(null);
            decision.setTriggeringSeparator(separator);

            arbitrator.decide(this, decision);

            // If a renderer was chosen then we may have to do something.
            SeparatorRenderer chosen = decision.getChosenSeparator();
            if (chosen == null) {
                // The deferred separator was ignored so the triggering
                // separator replaces it as the deferred one.
                deferredSeparator = separator;
            } else if (decision.isDecisionDeferred()) {

                // The chosen separator was deferred.
                deferredSeparator = chosen;
            } else {

                // The chosen separator must be rendered.
                renderSeparator(outputBuffer, chosen);

                // If the arbitrator chose the deferred separator then the
                // triggering separator becomes the deferred one. This is to
                // allow both separators to be used.
                if (chosen == deferredSeparator) {
                    deferredSeparator = separator;
                } else {
                    deferredSeparator = null;
                }

                previousContent = null;
            }
        }

        // Make sure that this is in a valid state after this method ends.
        checkInvariant();
    }

    // Javadoc inherited
    public void beforeContent(SeparatedContent content)
            throws RendererException {

        if (content == null) {
            throw new IllegalArgumentException("content must not be null");
        }

        // Make sure that this is in a valid state before this method starts.
        checkInvariant();

        // Make sure that any deferred renderers are written out before the
        // content.
        beforeContentImpl(content);

        // Make sure that this is in a valid state after this method ends.
        checkInvariant();

    }

    /**
     * Ensures that the renderer is written out given that some content is about
     * to be written.
     * @param content The content that is about to be written.
     * @throws RendererException If there was a problem rendering the separator.
     */
    private void beforeContentImpl(SeparatedContent content)
            throws RendererException {

        // If there is a deferred renderer then check to see whether it should
        // be written out.
        if (deferredSeparator != null) {

            // The arbitrator must not defer the separator so set this flag to
            // detect if it tries.
            decision.setAllowDeferral(false);

            // Initialise the decision with the information needed by the
            // arbitrator to make its decision.
            decision.setDeferredSeparator(deferredSeparator);
            decision.setPreviousContent(previousContent);
            decision.setTriggeringContent(content);
            decision.setTriggeringSeparator(null);

            // Ask the arbitrator to decide whether to output a separator.
            arbitrator.decide(this, decision);

            SeparatorRenderer chosen = decision.getChosenSeparator();
            if (chosen != null) {
                renderSeparator(outputBuffer, chosen);
            }
        }

        // The last thing written was content so set the state accordingly.
        deferredSeparator = null;
        previousContent = content;
    }

    public void flush()
            throws RendererException {

        // Make sure that this is in a valid state before this method starts.
        checkInvariant();

        // Make sure that any deferred renderers are written out. Do this by
        // pretending to write out some content.
        beforeContentImpl(null);

        // Make sure that this is in a valid state after this method ends.
        checkInvariant();
    }

    /**
     * Check to make sure that the invariants on this class are satisfied.
     */
    private void checkInvariant() {
    }

    /**
     * Renders the separator.
     *
     * <p>Derived classes can override this if they need to do something before
     * or after a separator is rendered.</p>
     *
     * @param separator The separator to render.
     *
     * @throws RendererException If there was a problem rendering the
     * separator.
     */
    protected void renderSeparator(OutputBuffer outputBuffer,
                                   SeparatorRenderer separator)
            throws RendererException {
        separator.render(outputBuffer);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-May-04	4318/1	pduffin	VBM:2004051207 Integrated separators into menu rendering

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 07-May-04	4164/3	pduffin	VBM:2004050404 Refactored separator arbitrator and started integrating with rest of menus

 08-Apr-04	3610/3	pduffin	VBM:2004032509 Added separator API and default implementation

 ===========================================================================
*/
