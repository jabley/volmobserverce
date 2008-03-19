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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.styling.impl.device;

import com.volantis.shared.iteration.IterationAction;
import com.volantis.styling.device.DeviceStyles;
import com.volantis.styling.device.DeviceStylingEngine;
import com.volantis.styling.engine.Attributes;
import com.volantis.styling.impl.engine.AfterEndElementIteratee;
import com.volantis.styling.impl.engine.BeforeStartElementIteratee;
import com.volantis.styling.impl.engine.Cascader;
import com.volantis.styling.impl.engine.CascaderImpl;
import com.volantis.styling.impl.engine.ElementStack;
import com.volantis.styling.impl.engine.MatcherContextImpl;
import com.volantis.styling.impl.engine.listeners.Listener;
import com.volantis.styling.impl.engine.listeners.ListenerIteratee;
import com.volantis.styling.impl.engine.listeners.MutableListeners;
import com.volantis.styling.impl.engine.listeners.MutableListenersImpl;
import com.volantis.styling.impl.engine.matchers.InternalMatcherContext;
import com.volantis.styling.impl.engine.sheet.StylerList;
import com.volantis.styling.impl.sheet.StyleSheetInternal;
import com.volantis.styling.impl.state.StateContainer;

/**
 * The implementation of the styling engine used to apply device styles to a
 * document.
 */
public class DeviceStylingEngineImpl
        implements DeviceStylingEngine {

    /**
     * The set of listeners to invoke.
     */
    private final MutableListeners listeners;

    /**
     * The stack of state related to all elements that have been opened but not
     * yet closed.
     */
    private final ElementStack elementStack;

    /**
     * The object responsible for implementing the CSS cascade.
     */
    private final Cascader cascader;

    /**
     * The object responsible for invoking the
     * {@link com.volantis.styling.impl.engine.matchers.composites.DepthChangeListener#beforeStartElement} method on all the
     * registered listeners.
     */
    private final ListenerIteratee beforeStartElementIteratee;

    /**
     * The object responsible for invoking the
     * {@link com.volantis.styling.impl.engine.matchers.composites.DepthChangeListener#afterEndElement} method on all the
     * registered listeners.
     */
    private final ListenerIteratee afterEndElementIteratee;

    /**
     * The context used by stylers.
     */
    private final DeviceStylerContext stylerContext;

    /**
     * The defaults to use.
     */
    private final Defaults defaults;

    /**
     * Initialise.
     */
    public DeviceStylingEngineImpl(StyleSheetInternal styleSheet) {

        this.elementStack = new ElementStack();

        InternalMatcherContext matcherContext = new MatcherContextImpl(
                elementStack);

        // Create iteratees that will invoke the depth change listeners before
        // start and after end element events.
        beforeStartElementIteratee = new BeforeStartElementIteratee(
                matcherContext);
        afterEndElementIteratee = new AfterEndElementIteratee(matcherContext);

        listeners = new MutableListenersImpl();

        StateContainer stateContainer = styleSheet.createStateContainer();
        matcherContext.setContainer(stateContainer);

        StylerList stylerList = styleSheet.getStylerList();

        // Add the listeners from the style sheet.
        styleSheet.getListeners().iterate(new ListenerIteratee() {
            public IterationAction next(Listener listener) {
                listeners.addListener(listener);
                return IterationAction.CONTINUE;
            }
        });

        // Create the cascader.
        stylerContext = new DeviceStylerContext(matcherContext);

        cascader = new CascaderImpl(stylerList, stylerContext);

        DeviceCompiledStyleSheet deviceSheet =
                (DeviceCompiledStyleSheet) styleSheet;

        defaults = deviceSheet.getDefaults();
    }

    // Javadoc inherited.
    public DeviceStyles startElement(
            String localName,
            Attributes attributes) {

        if (localName == null) {
            throw new IllegalArgumentException("localName cannot be null");
        }
        if (attributes == null) {
            throw new IllegalArgumentException("attributes cannot be null");
        }

        // Update the context with the new element information.
        elementStack.push("", localName, -1, null);

        // Invoke the depth change listeners to inform them that a start
        // element event is about to be processed.
        listeners.iterate(beforeStartElementIteratee);

        DeviceStylesBuilder stylesBuilder = new DeviceStylesBuilder(defaults);
        stylerContext.setStylesBuilder(stylesBuilder);

        // Do the cascade.
        cascader.cascade(attributes);

        return stylesBuilder.getStyles();
    }

    // Javadoc inherited.
    public void endElement(String localName) {

        // Invoke the depth change listeners to inform them that an end
        // element event has been processed.
        listeners.iterate(afterEndElementIteratee);

        // Discard the element information from the context.
        elementStack.pop("", localName);
    }
}
