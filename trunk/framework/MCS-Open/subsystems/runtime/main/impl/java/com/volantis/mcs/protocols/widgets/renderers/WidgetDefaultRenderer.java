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

package com.volantis.mcs.protocols.widgets.renderers;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.DivAttributes;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.ScriptAttributes;
import com.volantis.mcs.protocols.SpanAttributes;
import com.volantis.mcs.protocols.TableAttributes;
import com.volantis.mcs.protocols.TableCellAttributes;
import com.volantis.mcs.protocols.TableRowAttributes;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.widgets.ActionName;
import com.volantis.mcs.protocols.widgets.EventName;
import com.volantis.mcs.protocols.widgets.JavaScriptContainer;
import com.volantis.mcs.protocols.widgets.JavaScriptStringFactory;
import com.volantis.mcs.protocols.widgets.MemberName;
import com.volantis.mcs.protocols.widgets.MemberReference;
import com.volantis.mcs.protocols.widgets.PropertyName;
import com.volantis.mcs.protocols.widgets.WidgetDefaultModule;
import com.volantis.mcs.protocols.widgets.internal.attributes.JavaScriptArrayAttributes;
import com.volantis.mcs.protocols.widgets.internal.attributes.JavaScriptStringAttributes;
import com.volantis.mcs.protocols.widgets.styles.EffectRule;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.DisplayKeywords;
import com.volantis.mcs.runtime.scriptlibrarymanager.ScriptModule;
import com.volantis.mcs.runtime.scriptlibrarymanager.exceptions.UnregisteredScriptModuleDependencyException;
import com.volantis.styling.PseudoElements;
import com.volantis.styling.StatefulPseudoClasses;
import com.volantis.styling.Styles;
import com.volantis.styling.StylingFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

import java.io.IOException;
import java.io.Writer;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;


/**
 * Base class for widget renderer suitable for HTML-based protocols.
 *
 * This implementation of renderer is stateful and is not thread-safe.
 * It's lifecycle is supposed to match lifecycle of a single request.
 *
 * Implements common method - open/close Div element, open/close Script element
 */
public abstract class WidgetDefaultRenderer implements WidgetRenderer {

    /**            w
     * Used for logging
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(WidgetDefaultRenderer.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
        LocalizationFactory.createExceptionLocalizer(WidgetDefaultRenderer.class);
   
    /**
     * Default content string for folded marker.
     */
    protected static final String DEFAULT_FOLDED_MARKER = "+";

    /**
     * Default content string for unfolded marker.
     */
    protected static final String DEFAULT_UNFOLDED_MARKER = "-";
    
    /**
     * The empty array of supported action names.
     */
    private static final ActionName[] EMPTY_SUPPORTED_ACTION_NAMES = new ActionName[0];

    /**
     * The empty array of supported property names.
     */
    private static final PropertyName[] EMPTY_SUPPORTED_PROPERTY_NAMES = new PropertyName[0];

    /**
     * The empty array of supported event names.
     */
    private static final EventName[] EMPTY_SUPPORTED_EVENT_NAMES = new EventName[0];

    /**
     * Stores the original value  of isPreformatted flag
     * so it can be restored after writing the script.
     */
    private boolean isElementPreFormatted = false;

    /**
     * Stack of DivAttributes. Instantiated on first access.
     */
    private Stack divAttributesStack;
    
    /**
     * Stack of SpanAttributes. Instantiated on first access.
     */
    private Stack spanAttributesStack;
    
    /**
     * Stack of MCSAttributes. Instantiated on first access.
     */
    protected Stack attributesStack;
    
    private Stack javaScriptContainersStack = new Stack();
    
    // Javadoc inherited
    public final void renderOpen(VolantisProtocol protocol, MCSAttributes attributes) 
        throws ProtocolException {

        WidgetDefaultModule module = getWidgetDefaultModule(protocol);
        
        // If parent attributes is instance of JavaScriptArrayAttributes, 
        // add ID of this widget to the array attributes.
        if (!module.getOuterAttributesStack().empty()) {
            MCSAttributes currentAttributes = (MCSAttributes) module.getOuterAttributesStack().peek();
        
            if (currentAttributes instanceof JavaScriptArrayAttributes) {
                if (attributes.getId() == null) {
                    attributes.setId(protocol.getMarinerPageContext().generateUniqueFCID());
                }
            
                ((JavaScriptArrayAttributes) currentAttributes).addWidget(attributes.getId());
            }
        }
        
        // Push an instance of JavaScriptContainer associated with current rendering.
        javaScriptContainersStack.push(createJavaScriptContainer(protocol));
        
        // Push supported action and property names for this widget.
        pushWidgetId(protocol, attributes);
        
        // Push attributes on the outer stack.
        module.getOuterAttributesStack().push(attributes);

        // Invoke widget specific rendering. 
        doRenderOpen(protocol, attributes);

        // Push attributes on the inner stack.
        module.getInnerAttributesStack().push(attributes);
    }
    
    // Javadoc inherited
    public final void renderClose(VolantisProtocol protocol, MCSAttributes attributes) 
        throws ProtocolException {
        WidgetDefaultModule module = getWidgetDefaultModule(protocol);

        // Pop attributes from the inner stack.
        module.getInnerAttributesStack().pop();

        // Invoke widget specific rendering. 
        doRenderClose(protocol, attributes);
        
        // Pop attributes from the outer stack.
        module.getOuterAttributesStack().pop();

        // Pop supported action and property names for this widget.
        popWidgetId(protocol, attributes);

        // Pops an instance of JavaScriptContainer associated with current rendering.
        javaScriptContainersStack.pop();

        // In case any widget was rendered, disable generation of type rules in stylesheets.
        // If it was not disabled, in some cases styles would be associated with
        // element type in stylesheet, which may cause troubles for following reasons:
        //
        // The JavaScript code for widgets modifies client HTML in many ways, and
        // in most cases creates new HTML elements. If some styles were associated
        // with element type in stylesheet, those new HTML elements would is such case 
        // inherit that styles, which is not desired, because new HTML elements are 
        // expected to have no styles associated.
        //
        // Having type rules disabled, styles would always be associated with classes
        // in stylesheet. New HTML elements would not inherit undesired values anymore,
        // which is the expected behaviour, and the reason for that fix.
        ((DOMProtocol) protocol).getExtractorContext().setGenerateTypeRules(false);
    }

    /**
     * Renders widget-specific opening.
     * 
     * @param protocol The protocol used.
     * @param attributes The widget attributes.
     * @throws ProtocolException
     */
    protected abstract void doRenderOpen(VolantisProtocol protocol, MCSAttributes attributes) 
        throws ProtocolException;
        
    /**
     * Renders widget-specific closure.
     * 
     * @param protocol The protocol used.
     * @param attributes The widget attributes.
     * @throws ProtocolException
     */
    protected abstract void doRenderClose(VolantisProtocol protocol, MCSAttributes attributes) 
        throws ProtocolException;

    /**
     * Opens and returns div element with the specified styles on specified DOM
     * output buffer.
     * 
     * @param styles The styles to use
     * @param dom The DOM output buffer to use.
     * @return The opened DOM element.
     * @deprecated
     */
    protected Element openDivElement(Styles styles, DOMOutputBuffer dom) {
        return dom.openStyledElement("div", styles);
    }

    /**
     * Opens and returns div element on specified DOM output buffer.
     * 
     * @param dom The DOM output buffer to use.
     * @return The opened DOM element.
     * @deprecated
     */
    protected Element openDivElement(DOMOutputBuffer dom) {
        return dom.openElement("div");
    }

    /**
     * Closes the div element on specified DOM output buffer.
     * 
     * @param dom The DOM output buffer to use.
     * @deprecated
     */
    protected void closeDivElement(DOMOutputBuffer dom) {
        dom.closeElement("div");
    }
    
    /**
     * Returns an array of action names supported by this widget. By default,
     * this method returns empty array. Overwrite it for customisation.
     * 
     * <p>
     * Important note: If this method was overwritten to return non-empty array,
     * the widget renderer MUST (!!!) render widget registration under id read
     * from the attributes.
     * </p>
     * 
     * @return an array of action names supported by this widget.
     */
    protected ActionName[] getSupportedActionNames() {
        return EMPTY_SUPPORTED_ACTION_NAMES;
    }

    /**
     * Returns an array of property names supported by this widget. By default,
     * this method returns empty array. Overwrite it for customisation.
     *
     * <p>
     * Important note: If this method was overwritten to return non-empty array,
     * the widget renderer MUST (!!!) render widget registration under id read
     * from the attributes.
     * </p>
     * 
     * @return an array of event names supported by this widget.
     */
    protected PropertyName[] getSupportedPropertyNames() {
        return EMPTY_SUPPORTED_PROPERTY_NAMES;
    }

    /**
     * Returns an array of event names supported by this widget. By default,
     * this method returns empty array. Overwrite it for customisation.
     *
     * <p>
     * Important note: If this method was overwritten to return non-empty array,
     * the widget renderer MUST (!!!) render widget registration under id read
     * from the attributes.
     * </p>
     * 
     * @return an array of event names supported by this widget.
     */
    protected EventName[] getSupportedEventNames() {
        return EMPTY_SUPPORTED_EVENT_NAMES;
    }

    /**
     * Pushes specified DivAttributes onto the stack.
     * 
     * @param divAttributes The div attributes to push.
     */
    private void pushDivAttributes(DivAttributes divAttributes) {
        // Create the stack, if it not already exists.
        if (divAttributesStack == null) {
            divAttributesStack = new Stack();
        }

        // Push the attributes onto the stack.
        divAttributesStack.push(divAttributes);
    }

    /**
     * Pops and returns DivAttributes from the top of the stack. If stack is
     * empty, it raises EmptyStackException.
     * 
     * @return The popped div attributes.
     * @throws EmptyStackException if stack is empty.
     */
    private DivAttributes popDivAttributes() {
        if (divAttributesStack == null) {
            // If there's no stack instantiated, it means that nothing has been
            // put. Throw EmptyStackException in that case.
            throw new EmptyStackException();

        } else {
            // Otherwise pop the element from the stack.
            // If the stack is empty, the pop() method will
            // throw EmptyStackException.
            return (DivAttributes) divAttributesStack.pop();
        }
    }

    /**
     * Pushes specified SpanAttributes onto the stack.
     * 
     * @param spanAttributes The span attributes to push.
     */
    private void pushSpanAttributes(SpanAttributes spanAttributes) {
        // Create the stack, if it not already exists.
        if (spanAttributesStack == null) {
            spanAttributesStack = new Stack();
        }

        // Push the attributes onto the stack.
        spanAttributesStack.push(spanAttributes);
    }

    /**
     * Pops and returns SpanAttributes from the top of the stack. If stack is
     * empty, it raises EmptyStackException.
     * 
     * @return The popped span attributes.
     * @throws EmptyStackException if stack is empty.
     */
    private SpanAttributes popSpanAttributes() {
        if (spanAttributesStack == null) {
            // If there's no stack instantiated, it means that nothing has been
            // put. Throw EmptyStackException in that case.
            throw new EmptyStackException();

        } else {
            // Otherwise pop the element from the stack.
            // If the stack is empty, the pop() method will
            // throw EmptyStackException.
            return (SpanAttributes) spanAttributesStack.pop();
        }
    }

    /**
     * Pushes specified DivAttributes onto the stack.
     * 
     * @param divAttributes The div attributes to push.
     */
    protected void pushMCSAttributes(MCSAttributes attributes) {
        // Create the stack, if it not already exists.
        if (this.attributesStack == null) {
            this.attributesStack = new Stack();
        }

        // Push the attributes onto the stack.
        this.attributesStack.push(attributes);
    }

    /**
     * Pops and returns DivAttributes from the top of the stack. If stack is
     * empty, it raises EmptyStackException.
     * 
     * @return The popped div attributes.
     * @throws EmptyStackException if stack is empty.
     */
    protected MCSAttributes popMCSAttributes() {
        if (this.attributesStack == null) {
            // If there's no stack instantiated, it means that nothing has been
            // put. Throw EmptyStackException in that case.
            throw new EmptyStackException();

        } else {
            // Otherwise pop the element from the stack.
            // If the stack is empty, the pop() method will
            // throw EmptyStackException.
            return (MCSAttributes)this.attributesStack.pop();
        }
    }

    /**
     * Opens the div element with specified attributes using specified protocol,
     * and returns opened DOM element. The element is set as locked.
     * 
     * @param protocol The protocol used.
     * @param divAttributes The attributes of the div element.
     * @return the opened element.
     * @throws ProtocolException
     */
    protected Element openDivElement(VolantisProtocol protocol,
            DivAttributes divAttributes) throws ProtocolException {
        // Push the div attributes on the stack, to make it available in the
        // closeDivElement method.
        pushDivAttributes(divAttributes);

        // Open div element on the protocol.
        protocol.writeOpenDiv(divAttributes);

        // Get the div element that has just been put.
        Element element = getCurrentBuffer(protocol).getCurrentElement();

        // Mark the element locked.
        setElementLocked(protocol, element);

        // Return opened element.
        return element;
    }

    /**
     * Closes the div element using specified protocol.
     * 
     * If there was no corresponding div element opened, a ProtocolException is
     * thrown.
     * 
     * Note, that this method is complementary only with following methods:
     * <li>openDivElement(VolantisProtocol protocol, DivAttributes
     * divAttributes)</li>
     * <li>openDivElement(VolantisProtocol protocol)</li>
     * <li>openDivElement(VolantisProtocol protocol, MCSAttributes attributes)</li>
     * <li>openDivElement(VolantisProtocol protocol, MCSAttributes attributes,
     * boolean generateUniqueId)</li>
     * 
     * It's not complementary with following deprecated methods:
     * <li>openDivElement(Styles styles, DOMOutputBuffer dom)</li>
     * <li>openDivElement(DOMOutputBuffer dom)</li>
     * 
     * @param protocol The protocol used.
     * @throws ProtocolException
     */
    protected Element closeDivElement(VolantisProtocol protocol)
            throws ProtocolException {
        // Pop div attributes from the stack.
        // Convert EmptyStackException to protocol exception.
        DivAttributes divAttributes;

        try {
            divAttributes = popDivAttributes();
        } catch (EmptyStackException e) {
            throw new ProtocolException("Div element not opened.");
        }

        // Get the div element that has just been put.
        Element element = getCurrentBuffer(protocol).getCurrentElement();
        
        // Close the div element on the protocol.
        protocol.writeCloseDiv(divAttributes);
        
        return element;
    }

    /**
     * Creates and returns an instance of DivAttributes.
     * 
     * If source attributes are specified, all standard values are copied from
     * it, which includes 'id', 'title', 'href', all event attributes and
     * styles.
     * 
     * If styles are not specified on DivAttributes, new instance of default
     * inherited styles are created for it, with 'display=block'.
     * 
     * If the 'id' attribute is not specified and generateDefaultId is true, it
     * generates unique value for it.
     * 
     * @param protocol The protocol used
     * @param sourceAttributes The source attributes, or null.
     * @param generateDefaultId The flag.
     * @return Created div attributes.
     */
    protected DivAttributes createDivAttributes(VolantisProtocol protocol,
            MCSAttributes sourceAttributes, boolean generateDefaultId) {
        // Create new instance of DivAttributes
        DivAttributes divAttributes = new DivAttributes();

        // If source attributes are specified, copy all attribute values.
        if (sourceAttributes != null) {
            divAttributes.copy(sourceAttributes);
        }

        // If styles are not specified, create new inherited styles.
        if (divAttributes.getStyles() == null) {
            Styles styles = StylingFactory.getDefaultInstance()
                    .createInheritedStyles(
                            protocol.getMarinerPageContext().getStylingEngine()
                            .getStyles(), DisplayKeywords.BLOCK);
            
            divAttributes.setStyles(styles);
        }

        // If there's no ID attribute, and generateDefaultId flag is set,
        // generate unique value for it.
        if (generateDefaultId && divAttributes.getId() == null) {
            divAttributes.setId(protocol.getMarinerPageContext()
                    .generateUniqueFCID());
        }

        // Return created DivAttributes
        return divAttributes;
    }

    /**
     * Open the div element on specified protocol, using specified attributes.
     * 
     * The actual DivAttributes are created using following method: -
     * createDivAttributes(protocol, attributes, generateDefaultId)
     * 
     * @param protocol The protocol to use.
     * @param attributes The attributes.
     * @param generateDefaultId The flag for generating default id attribute.
     * @return The DOM element written.
     * @see createDivAttributes
     */
    protected Element openDivElement(VolantisProtocol protocol,
            MCSAttributes attributes, boolean generateDefaultId)
            throws ProtocolException {
        // Create an instance of div attributes.
        DivAttributes divAttributes = createDivAttributes(protocol, attributes,
                generateDefaultId);

        // Open div element on the protocol.
        return openDivElement(protocol, divAttributes);
    }

    /**
     * Open the div element on specified protocol.
     * 
     * The actual DivAttributes are retrieved using
     * createDivAttributes(protocol, attributes, false) method.
     * 
     * @param protocol The protocol to use.
     * @param attributes The attributes.
     * @return The DOM element written.
     * @see createDivAttributes
     */
    protected Element openDivElement(VolantisProtocol protocol,
            MCSAttributes attributes) throws ProtocolException {
        // Create an instance of div attributes.
        DivAttributes divAttributes = createDivAttributes(protocol, attributes,
                false);

        // Open the div element on protocol.
        return openDivElement(protocol, divAttributes);
    }

    /**
     * Open the div element on specified protocol.
     * 
     * The actual DivAttributes are retrieved using
     * createDivAttributes(protocol, null, false) method.
     * 
     * @param protocol The protocol to use.
     * @return The DOM element written.
     * @see createDivAttributes
     */
    protected Element openDivElement(VolantisProtocol protocol)
            throws ProtocolException {
        // Create an instance of div attributes.
        DivAttributes divAttributes = createDivAttributes(protocol, null, false);

        // Open the div element on protocol.
        return openDivElement(protocol, divAttributes);
    }

    /**
     * Opens the span element with specified attributes using specified protocol,
     * and returns opened DOM element. The element is set as locked.
     * 
     * @param protocol The protocol used.
     * @param spanAttributes The attributes of the span element.
     * @return the opened element.
     * @throws ProtocolException
     */
    protected Element openSpanElement(VolantisProtocol protocol,
            SpanAttributes spanAttributes) throws ProtocolException {
        // Push the span attributes on the stack, to make it available in the
        // closeSpanElement method.
        pushSpanAttributes(spanAttributes);

        // Open span element on the protocol.
        protocol.writeOpenSpan(spanAttributes);

        // Get the div element that has just been put.
        Element element = getCurrentBuffer(protocol).getCurrentElement();

        // Mark the element locked.
        setElementLocked(protocol, element);

        // Return opened element.
        return element;
    }

    /**
     * Closes the span element using specified protocol.
     * 
     * If there was no corresponding span element opened, a ProtocolException is
     * thrown.
     *
     * @param protocol The protocol used.
     * @throws ProtocolException
     */
    protected Element closeSpanElement(VolantisProtocol protocol)
            throws ProtocolException {
        // Pop span attributes from the stack.
        // Convert EmptyStackException to protocol exception.
        SpanAttributes spanAttributes;

        try {
            spanAttributes = popSpanAttributes();
        } catch (EmptyStackException e) {
            throw new ProtocolException("Span element not opened.");
        }

        // Get the span element that has just been put.
        Element element = getCurrentBuffer(protocol).getCurrentElement();
        
        // Close the div element on the protocol.
        protocol.writeCloseSpan(spanAttributes);
        
        return element;
    }

    /**
     * Creates and returns an instance of SpanAttributes.
     * 
     * If source attributes are specified, all standard values are copied from
     * it, which includes 'id', 'title', 'href', all event attributes and
     * styles.
     * 
     * If styles are not specified on SpanAttributes, new instance of default
     * inherited styles are created for it, with 'display=block'.
     * 
     * If the 'id' attribute is not specified and generateDefaultId is true, it
     * generates unique value for it.
     * 
     * @param protocol The protocol used
     * @param sourceAttributes The source attributes, or null.
     * @param generateDefaultId The flag.
     * @return Created span attributes.
     */
    protected SpanAttributes createSpanAttributes(VolantisProtocol protocol,
            MCSAttributes sourceAttributes, boolean generateDefaultId) {
        // Create new instance of SpanAttributes
        SpanAttributes spanAttributes = new SpanAttributes();

        // If source attributes are specified, copy all attribute values.
        if (sourceAttributes != null) {
            spanAttributes.copy(sourceAttributes);
        }

        // If styles are not specified, create new inherited styles.
        if (spanAttributes.getStyles() == null) {
            Styles styles = StylingFactory.getDefaultInstance()
                .createInheritedStyles(
                    protocol.getMarinerPageContext().getStylingEngine()
                            .getStyles(), DisplayKeywords.BLOCK);
            
            spanAttributes.setStyles(styles);
        }

        // If there's no ID attribute, and generateDefaultId flag is set,
        // generate unique value for it.
        if (generateDefaultId && spanAttributes.getId() == null) {
            spanAttributes.setId(protocol.getMarinerPageContext()
                    .generateUniqueFCID());
        }

        // Return created DivAttributes
        return spanAttributes;
    }

    /**
     * Open the span element on specified protocol, using specified attributes.
     * 
     * The actual SpanAttributes are created using following method: -
     * createSpanAttributes(protocol, attributes, generateDefaultId)
     * 
     * @param protocol The protocol to use.
     * @param attributes The attributes.
     * @param generateDefaultId The flag for generating default id attribute.
     * @return The DOM element written.
     * @see createSpanAttributes
     */
    protected Element openSpanElement(VolantisProtocol protocol,
            MCSAttributes attributes, boolean generateDefaultId)
            throws ProtocolException {
        // Create an instance of span attributes.
        SpanAttributes spanAttributes = createSpanAttributes(protocol, attributes,
                generateDefaultId);

        // Open span element on the protocol.
        return openSpanElement(protocol, spanAttributes);
    }

    /**
     * Open the span element on specified protocol.
     * 
     * The actual SpanAttributes are retrieved using
     * createSpanAttributes(protocol, attributes, false) method.
     * 
     * @param protocol The protocol to use.
     * @param attributes The attributes.
     * @return The DOM element written.
     * @see createSpanAttributes
     */
    protected Element openSpanElement(VolantisProtocol protocol,
            MCSAttributes attributes) throws ProtocolException {
        // Create an instance of div attributes.
        SpanAttributes spanAttributes = createSpanAttributes(protocol, attributes,
                false);

        // Open the span element on protocol.
        return openSpanElement(protocol, spanAttributes);
    }

    /**
     * Open the span element on specified protocol.
     * 
     * The actual SpanAttributes are retrieved using
     * createSpanAttributes(protocol, null, false) method.
     * 
     * @param protocol The protocol to use.
     * @return The DOM element written.
     * @see createSpanAttributes
     */
    protected Element openSpanElement(VolantisProtocol protocol)
            throws ProtocolException {
        // Create an instance of span attributes.
        SpanAttributes spanAttributes = createSpanAttributes(protocol, null, false);

        // Open the div element on protocol.
        return openSpanElement(protocol, spanAttributes);
    }


    // dafault bahaviour for widget's content
    public boolean shouldRenderContents(VolantisProtocol protocol, MCSAttributes attributes) throws ProtocolException {
        return true;
    }

    /**
     * Opens script element with provided attributes
     */
    protected Element openScriptElement(ScriptAttributes scriptAttributes, DOMOutputBuffer dom) {
        Element element = dom.openElement("script");
        element.setAttribute("type", scriptAttributes.getType());
        this.isElementPreFormatted = dom.isPreFormatted();
        dom.setElementIsPreFormatted(true);
		return element;
    }

    /**
     * Opens script element with standard JavaScript attributes
     */
    protected Element openScriptElement(DOMOutputBuffer dom) {
        ScriptAttributes sa = new ScriptAttributes();
        sa.setType("text/javascript");
        return openScriptElement(sa, dom);
    }

    /**
     * Close script element
     */
    protected void closeScriptElement(DOMOutputBuffer dom){
        dom.setElementIsPreFormatted(this.isElementPreFormatted);
        dom.closeElement("script");
    }

    /**
     * Write contents of JavaScript element
     * @deprecated
     */
    protected void writeJavaScript(DOMOutputBuffer dom, String scriptContent)  throws IOException {
        dom.writeText(scriptContent);
    }
    
    /**
     * Writes JavaScript code to current JavaScriptContainer.
     * 
     * @param script The script.
     * @throws ProtocolException
     */
    protected void writeJavaScript(String script) throws ProtocolException {
        try {
            getCurrentJavaScriptContainer().getWriter().write(script);
        } catch (IOException e) {
            throw new ProtocolException("Error writing JavaScript", e);
        }
    }

    /**
     * Write script element with the provided script content
     * @deprecated
     */
    protected void writeScriptElement(DOMOutputBuffer dom, String scriptContent)  throws IOException {
        openScriptElement(dom);
        writeJavaScript(dom, scriptContent);
        closeScriptElement(dom);
    }

    /**
     * Write script element with the provided script content. All scripts written
     * using this method will be invoked in Widget.startup() method, in the same
     * order as they were written.
     * @deprecated
     */
    protected void writeStartupScriptElement(DOMOutputBuffer dom, String scriptContent)  throws IOException {
        writeScriptElement(dom, "Widget.addStartupItem(function(){" + scriptContent + "})");
   }

    protected DOMOutputBuffer getCurrentBuffer(VolantisProtocol protocol){
        return (DOMOutputBuffer)protocol.getMarinerPageContext().getCurrentOutputBuffer();
    }

    /**
     * Adds requirements for a given script module
     *
     * @param sm
     * @param protocol
     * @param attributes
     * @param allEffectsNeeded
     * @throws UnregisteredScriptModuleDependencyException
     */
    public void require(ScriptModule sm, VolantisProtocol protocol,
                        MCSAttributes attributes, boolean allEffectsNeeded)
            throws UnregisteredScriptModuleDependencyException {
        protocol.getMarinerPageContext().getRequiredScriptModules().require(sm, attributes, allEffectsNeeded);
    }

    /**
     * Adds requirements for a given script module
     *
     * @param sm
     * @param protocol
     * @param attributes
     * @throws UnregisteredScriptModuleDependencyException
     */
    public void require(ScriptModule sm, VolantisProtocol protocol, MCSAttributes attributes)
            throws UnregisteredScriptModuleDependencyException {
        protocol.getMarinerPageContext().getRequiredScriptModules().require(sm, attributes, false);
    }

    /**
     * Convenience method for checking if this widget is supported by the protocol
     *
     * Default implementation assumes that wiudget is supported if framework client
     * is supported. Derived classes may override with more sophisticated check.
     */
    public boolean isWidgetSupported(VolantisProtocol protocol) {
        return (protocol.getProtocolConfiguration().isFrameworkClientSupported() 
                && protocol.supportsJavaScript());
    }

    /**
     * Retrieve marker styles when folding-item folded
     * @param protocol
     * @param attributes
     * @return
     */
    protected Styles getFoldedMarkerStyles(VolantisProtocol protocol, MCSAttributes attributes){
        Styles styles = attributes.getStyles();
        return styles.findNestedStyles(PseudoElements.MARKER);
    }

    /**
     * Render folded marker element and returns it's id, folded marker has display: inline
     * @param protocol
     * @param defaultMarkerValue
     * @param markerStyles Styles assign to marker pseudo element ::marker
     * @param alternativeMarkerStyles Styles assign to alternative element and his pseudo element ::marker
     * It is widget|folding-item as alternative to widget|summary. It might be null if widget doesn't has alternative marker styling element.
     * @return
     */
    protected String renderFoldedMarker(VolantisProtocol protocol, String defaultMarkerValue,
                   Styles markerStyles, Styles alternativeMarkerStyles) throws ProtocolException{

        DOMOutputBuffer currentBuffer = getCurrentBuffer(protocol);
        Element markerElement = currentBuffer.addElement("span");

        if(markerStyles != null) {
            markerElement.setStyles(markerStyles);
        } else {
            markerElement.setStyles(alternativeMarkerStyles);            
        }

        String uId = protocol.getMarinerPageContext().generateUniqueFCID();
        markerElement.setAttribute("id",uId);

        StyleValue markerValue = null;
        if(markerStyles != null) {
            markerValue = markerStyles.getPropertyValues().getSpecifiedValue(StylePropertyDetails.CONTENT);
        } else {
            if(alternativeMarkerStyles != null) {
                markerValue = alternativeMarkerStyles.getPropertyValues().getSpecifiedValue(StylePropertyDetails.CONTENT);
            }    
        }

        if( null != markerValue){
            ((DOMProtocol) protocol).getInserter().insert(markerElement, markerValue);
        } else if(null != defaultMarkerValue){
            markerElement.addText(defaultMarkerValue);
        } else {
            throw new ProtocolException("Marker has neither specified nor default value.");
            }
        return uId;
        }


    /**
     * Render unfolded marker, unfolded marker has display: none in opposite to 
     * folded marker display: inline
     * @param protocol
     * @param defaultMarkerValue
     * @param markerStyles Styles assign to ::marker pseudo element in :mcs-unfolded pseudo class      
     * @return
     * @throws ProtocolException
     */
    protected String renderUnfoldedMarker(VolantisProtocol protocol, String defaultMarkerValue,
                        Styles markerStyles, Styles alternativeMarkerStyles) throws ProtocolException {

        DOMOutputBuffer currentBuffer = getCurrentBuffer(protocol);
        Element markerElement = currentBuffer.addElement("span");

        if(markerStyles != null) {
            markerStyles.getPropertyValues().setComputedAndSpecifiedValue(StylePropertyDetails.DISPLAY, DisplayKeywords.NONE);
            markerElement.setStyles(markerStyles);
        } else {
            markerElement.setStyles(alternativeMarkerStyles);            
        }

        StyleValue markerValue = null;
        if(markerStyles != null) {
            markerValue = markerStyles.getPropertyValues().getSpecifiedValue(StylePropertyDetails.CONTENT);
        } else {
            if (alternativeMarkerStyles != null) {
                markerValue = alternativeMarkerStyles.getPropertyValues().getSpecifiedValue(StylePropertyDetails.CONTENT);
            }
        }

        String uId = protocol.getMarinerPageContext().generateUniqueFCID();
        markerElement.setAttribute("id",uId);
        if( null != markerValue ){
            ((DOMProtocol) protocol).getInserter().insert(markerElement, markerValue);
        } else if(null != defaultMarkerValue){
            markerElement.addText(defaultMarkerValue);
        } else {
            throw new ProtocolException("Marker has neither specified nor default value.");
        }
        return uId;
    }

    /**
     * Retrieve marker styles when folding-item unfolded
     * @param protocol
     * @param attributes
     * @return Styles or null if not specified
     */
    protected Styles getUnfoldedMarkerStyles(VolantisProtocol protocol, MCSAttributes attributes){
        Styles styles = attributes.getStyles();
        Styles unfoldedStyles = styles.findNestedStyles(StatefulPseudoClasses.MCS_UNFOLDED);
        Styles markerStyles = null;
        if(null != unfoldedStyles){
            markerStyles = unfoldedStyles.findNestedStyles(PseudoElements.MARKER);
        }
        return markerStyles;
    }

    /**
     * Clear pseudo element marker's CONTENT style property from element and from mcs-unfolded pseudo class applied to element
     * @param attributes MCSAttributes of element
     */
    protected void clearMarkerContent(MCSAttributes attributes) {

        // Retrieve styles from attributes
        Styles styles = attributes.getStyles();

        // Get marker styles
        Styles markerStyles = styles.findNestedStyles(PseudoElements.MARKER);

        if (markerStyles != null) {
            // clear content in marker pseudo element
            markerStyles.getPropertyValues().clearPropertyValue(StylePropertyDetails.CONTENT);
        }

        // Get styles from mcs-unfolded pseudo class
        Styles unfoldedStyles = styles.findNestedStyles(StatefulPseudoClasses.MCS_UNFOLDED);

        if (unfoldedStyles != null) {
            // Get marker styles from mcs-unfolded pseudo class
            markerStyles = unfoldedStyles.findNestedStyles(PseudoElements.MARKER);

            if (markerStyles != null) {
                // clear content in marker pseudo element
                markerStyles.getPropertyValues().clearPropertyValue(StylePropertyDetails.CONTENT);
            }
        }
    }

    /**
     * Creates and returns styles extractor instance,
     * ready to extract values from specified styles.
     *
     * @param protocol The protocol.
     * @param styles The styles to extract values from.
     * @return The styles extractor instance.
     */
    public StylesExtractor createStylesExtractor(VolantisProtocol protocol, Styles styles) {
        return WidgetHelper.createStylesExtractor(protocol, styles);
    }

    /**
     * Returns owning widget module (the one, which this renderer was created by).
     *
     * @param protocol The protocol used.
     * @return The owning widget module.
     */
    protected WidgetDefaultModule getWidgetDefaultModule(VolantisProtocol protocol) {
        return (WidgetDefaultModule) protocol.getWidgetModule();
    }

    /**
     * Locks an element. Locked element will not be modified/removed
     * by DOM transformers/optimisers.
     *
     * @param protocol The protocol to use for locking.
     * @param element The element to lock.
     */
    protected void setElementLocked(VolantisProtocol protocol, Element element) {
        ((DOMProtocol)protocol).setElementLocked(element);
    }

    /**
     * Renders the opening of the String element. The serialized HTML content of
     * the string element will be available in JavaScript by calling
     * Widget.getInstance(stringId).
     *
     * @param protocol The protocol to use for rendering.
     * @param stringId The string ID.
     * @throws ProtocolException
     */
    protected void renderOpenString(VolantisProtocol protocol, String stringId) throws ProtocolException {
        JavaScriptStringAttributes stringAttributes = new JavaScriptStringAttributes();

        stringAttributes.setId(stringId);

        stringAttributes.setStyles(StylingFactory.getDefaultInstance()
                .createInheritedStyles(
                        protocol.getMarinerPageContext().getStylingEngine().getStyles(),
                        DisplayKeywords.NONE));

        renderWidgetOpen(protocol, stringAttributes);
    }

    /**
     * Renders the closure of the String element. Returns ID of the String
     * element being closed (the one that was passed in the renderOpenString()
     * method).
     * 
     * @param protocol The protocol to use for rendering.
     * @return The ID of the String widget being closed.
     * @throws ProtocolException
     */
    protected String renderCloseString(VolantisProtocol protocol) throws ProtocolException {
        return renderWidgetClose(protocol).getId();
    }

    /**
     * Construct array of JavaScript objects describing effects 
     * to be provided to Widget constructor.
     * 
     * This list is initial data for repeater but it is provided 
     * for each widget so it is general method
     */
    protected String getEffectRulesList(StylesExtractor stylesExtractor){
        StringBuffer result = new StringBuffer();
        Iterator effectsIterator = stylesExtractor.getEffectRules().iterator();
        result.append("[");
        while(effectsIterator.hasNext()){
            EffectRule rule = (EffectRule)effectsIterator.next();
            result.append(rule.toScriptConstructor());
            if(effectsIterator.hasNext()){
                result.append(",");
            }
        }
        result.append("]");
        return result.toString();
     }

    /** 
     * Extracts options of Appearable mix-in from the provided
     * attributes and returns them in JavaScript applicable form 
     */
    protected String getAppearableOptions(MCSAttributes attrs) {        
        StylesExtractor extractor = new StylesExtractor(attrs.getStyles());
        return getAppearableOptions(extractor);
    }
    
    /** 
     * Extracts options of Disppearable mix-in from the provided
     * attributes and returns them in JavaScript applicable form 
     */
    protected String getDisappearableOptions(MCSAttributes attrs) {
        StylesExtractor extractor = new StylesExtractor(attrs.getStyles());
        extractor.setPseudoClass(StatefulPseudoClasses.MCS_CONCEALED);
        return getDisappearableOptions(extractor);
    }

    /** 
     * Extracts options of Appearable mix-in from the provided
     * style extractor and returns them in JavaScript applicable form 
     */
    protected String getAppearableOptions(StylesExtractor styles) {        
        StringBuffer options = new StringBuffer();
        options.append(" appRules: ").append(getEffectRulesList(styles));
        return options.toString();
    }

    /** 
     * Extracts options of Disppearable mix-in from the provided
     * style extractor and returns them in JavaScript applicable form 
     */
    protected String getDisappearableOptions(StylesExtractor styles) {
        StringBuffer options = new StringBuffer();
        options.append(" disRules: ").append(getEffectRulesList(styles));
        return options.toString();
    }
    
    /**
     * Converts specified string into JavaScript string ready to be rendered on
     * protocol's output.
     * 
     * <p>
     * Currently, it works by escaping specified string, and enclosing it with quotes.
     * </p>
     * 
     * @param string The string to convert
     * @return the JavaScript string
     */
    protected String createJavaScriptString(String string) {
        return JavaScriptStringFactory.getInstance().
            createJavaScriptString(string);
    }
    
    /**
     * Converts specified action name into JavaScript render-ready string
     * containg JavaScript member name.
     * 
     * @param memberName The member name
     * @return The JavaScript string with member name
     */
    protected String createJavaScriptString(MemberName memberName) {
        return JavaScriptStringFactory.getInstance().
            createJavaScriptString(memberName);
    }

    /**
     * Creates string containing rendered JavaScript Object, with specified
     * key-value mapping.
     * 
     * @param map Key-value mapping.
     * @return
     */
    protected String createJavaScriptObject(Map map) {
        return JavaScriptStringFactory.getInstance().
            createJavaScriptObject(map);
    }
    
    /**
     * Creates string containing rendered JavaScript Array, with specified
     * list of values.
     * 
     * @param list The list of values.
     * @return The string with rendered JavaScript Array.
     */
    protected String createJavaScriptArray(List list) {
        return JavaScriptStringFactory.getInstance().
            createJavaScriptArray(list);
    }
    
    /**
     * Creates and returns an instance of JavaScriptContainer for given protocol.
     * 
     * @param protocol The protocol used.
     * @return Created JavaScript container.
     * @throws ProtocolException
     */
    protected JavaScriptContainer createJavaScriptContainer(
            VolantisProtocol protocol) throws ProtocolException {
        return getWidgetDefaultModule(protocol).createJavaScriptContainer(
                protocol);
    }
   
    /**
     * close TableElement. 
     * @param protocol
     * @throws ProtocolException
     * Returns an instance of JavaScriptContainer associated with current renderering.
     * 
     * @return an instance of JavaScriptContainer associated with current renderering.
     */
    protected JavaScriptContainer getCurrentJavaScriptContainer() {
        return (JavaScriptContainer) javaScriptContainersStack.peek();
    }
    
    /**
     * Returns the writer of the current JavaScript container.
     * 
     * @return the writer of the current JavaScript container.
     * @see getCurrentJavaScriptContainer
     */
    protected Writer getJavaScriptWriter() {
        return getCurrentJavaScriptContainer().getWriter();
    }
        
    /**
     * Adds ID of the created widget to the current JavaScript container.
     * 
     * @param id An ID of the created widget.
     * @see getCurrentJavaScriptContainer
     */
    protected void addCreatedWidgetId(String id) {
        //TODO: Convert IllegalArgumentException to checked ProtocolException.
        getCurrentJavaScriptContainer().addCreatedWidgetId(id);
    }
    
    /**
     * Adds ID of the used widget to the current JavaScript container.
     * 
     * @param id An ID of the used widget.
     * @see getCurrentJavaScriptContainer
     */
    protected void addUsedWidgetId(String id) {
        getCurrentJavaScriptContainer().addUsedWidgetId(id);
    }
    
    /**
     * Pushes ID of this widget on the stacks associated with all action and
     * property names supported by rendered widget.
     * 
     * @param protocol The protocol used.
     * @param attributes The widget attributes.
     */
    private void pushWidgetId(VolantisProtocol protocol, MCSAttributes attributes) {
        ActionName[] actionNames = getSupportedActionNames();
        
        PropertyName[] propertyNames = getSupportedPropertyNames();
        
        String widgetId = attributes.getId();
        
        if (actionNames.length != 0 || propertyNames.length != 0) {
            if (widgetId == null) {
                widgetId = protocol.getMarinerPageContext().generateUniqueFCID();
                
                attributes.setId(widgetId);
            }
        }
        
        WidgetDefaultModule module = getWidgetDefaultModule(protocol);
        
        for (int i = 0; i < actionNames.length; i++) {
            module.pushWidgetId(widgetId, actionNames[i]);            
        }

        for (int i = 0; i < propertyNames.length; i++) {
            module.pushWidgetId(widgetId, propertyNames[i]);            
        }
    }
    
    /**
     * Pops ID of this widget from the stacks associated with all action and
     * property names supported by rendered widget.
     * 
     * @param protocol The protocol used.
     * @param attributes The widget attributes.
     */
    private void popWidgetId(VolantisProtocol protocol, MCSAttributes attributes) {
        ActionName[] actionNames = getSupportedActionNames();
        
        PropertyName[] propertyNames = getSupportedPropertyNames();

        WidgetDefaultModule module = getWidgetDefaultModule(protocol);

        for (int i = 0; i < actionNames.length; i++) {
            module.popWidgetId(actionNames[i]);            
        }

        for (int i = 0; i < propertyNames.length; i++) {
            module.popWidgetId(propertyNames[i]);            
        }
    }

    /**
     * Open TableElement on protocol
     * @param protocol
     * @param tableAttributes
     * @throws ProtocolException
     */
    protected void openTableElement(VolantisProtocol protocol,
            TableAttributes tableAttributes) throws ProtocolException {
        pushMCSAttributes(tableAttributes);
        protocol.writeOpenTable(tableAttributes);
    }

    /**
     * close TableElement. 
     * @param protocol
     * @throws ProtocolException
     */
    protected void closeTableElement(VolantisProtocol protocol)
            throws ProtocolException {
        TableAttributes tableAttributes;
        try {
            tableAttributes = (TableAttributes) popMCSAttributes();
        } catch (EmptyStackException e) {
            throw new ProtocolException(exceptionLocalizer.format("widget-table-not-opened"),e);
        } catch (ClassCastException e) {
            throw new ProtocolException(exceptionLocalizer.format(
                    "widget-unexpected-attributes-type",
                    TableCellAttributes.class.getName()), e);
        }
        protocol.writeCloseTable(tableAttributes);

    }

    
    /**
     * Open TableRowElement on protocol.
     * 
     * @param protocol
     * @param attributes
     * @throws ProtocolException
     */
    protected void openTableRowElement(VolantisProtocol protocol,
            TableRowAttributes attributes) throws ProtocolException {
        pushMCSAttributes(attributes);
        protocol.writeOpenTableRow(attributes);
    }
    
    /**
     * Close TableRowElement on protocol. 
     * 
     * @param protocol
     * @throws ProtocolException
     */
    protected void closeTableRowElement(VolantisProtocol protocol)
            throws ProtocolException {
        TableRowAttributes tableRowAttributes;
        try {
            tableRowAttributes = (TableRowAttributes) popMCSAttributes();
        } catch (EmptyStackException e) {
            throw new ProtocolException(exceptionLocalizer.format("widget-tr-not-opened"),e);
        } catch (ClassCastException e) {
            throw new ProtocolException(exceptionLocalizer.format(
                    "widget-unexpected-attributes-type",
                    TableCellAttributes.class.getName()), e);
        }
        protocol.writeCloseTableRow(tableRowAttributes);
    }
    
    /**
     * Open tableCell element on protocol. 
     * 
     * @param protocol
     * @param tableCellAttributes
     * @throws ProtocolException
     */
    protected void openTableCellElement(VolantisProtocol protocol,
            TableCellAttributes tableCellAttributes) throws ProtocolException {
        pushMCSAttributes(tableCellAttributes);
        protocol.writeOpenTableDataCell(tableCellAttributes);
    }

    /**
     * Close TableDataCellElement. 
     * @param protocol
     * @throws ProtocolException
     */
    protected void closeTableDataCellElement(VolantisProtocol protocol)
            throws ProtocolException {
        TableCellAttributes tableCellAttributes;
        try {
            tableCellAttributes = (TableCellAttributes) this.attributesStack
                    .pop();
        } catch (EmptyStackException e) {
            throw new ProtocolException(exceptionLocalizer.format("widget-td-not-opened"),e);            
        } catch (ClassCastException e) {
            throw new ProtocolException(exceptionLocalizer.format(
                    "widget-unexpected-attributes-type",
                    TableCellAttributes.class.getName()), e);
            
        }
        protocol.writeCloseTableDataCell(tableCellAttributes);
    }
    
    /**
     * Creates and returns a string, which may be renderer in JavaScript code as a member reference.
     * 
     * @param reference The member reference to create string for.
     * @return The string.
     */
    protected String createJavaScriptExpression(MemberReference reference) {
        return JavaScriptStringFactory.getInstance().
            createJavaScriptExpression(reference);
    }

    /**
     * Creates and returns a JavaScript string, which contains expression evaluating to
     * the widget of specified ID.
     * 
     * @param widgetId The ID of the referenced widget.
     * @return The JavaScript string expression returning referenced widget.
     */
    protected String createJavaScriptWidgetReference(String widgetId) {
        return createJavaScriptWidgetReference(widgetId, false);
    }

    /**
     * Creates and returns a JavaScript string, which contains expression evaluating to
     * the widget of specified ID.
     *
     * If addUsedWidgetId is true, then it automatically adds widgetId as
     * used widget ID.
     * 
     * @param widgetId The ID of the referenced widget.
     * @return The JavaScript string expression returning referenced widget.
     * @see addUsedWidgetId
     */
    protected String createJavaScriptWidgetReference(String widgetId, boolean addUsedWidgetId) {
        if (addUsedWidgetId) {
            addUsedWidgetId(widgetId);
        }
        
        return "$W(" + createJavaScriptString(widgetId) + ")";
    }

    /**
     * Creates and returns a JavaScript string, which can be render to open
     * widget registration expression. Currently, it renders "$RW('widgetId',"
     * string.
     * 
     * @param widgetId The ID of the registered widget.
     * @return The JavaScript string
     */
    protected String createJavaScriptWidgetRegistrationOpening(String widgetId) {
        return createJavaScriptWidgetRegistrationOpening(widgetId, false);
    }

    /**
     * Creates and returns a JavaScript string, which can be render to open
     * widget registration expression. Currently, it renders "$RW('widgetId',"
     * string.
     * 
     * If addCreatedWidgetId is true, then it automatically adds widgetId as
     * created widget ID.
     * 
     * @param widgetId The ID of the registered widget.
     * @param addCreatedWidgetId
     * @return The JavaScript string
     * @see addCreatedWidgetId
     */
    protected String createJavaScriptWidgetRegistrationOpening(String widgetId, boolean addCreatedWidgetId) {
        if (addCreatedWidgetId) {
            addCreatedWidgetId(widgetId);
        }
        
        return "$RW(" + createJavaScriptString(widgetId) + ",";
    }

    /**
     * Creates and returns a JavaScript string, which can be render to close
     * widget registration expression. Currently, it renders ")" string.
     * 
     * @param widgetId The ID of the registered widget.
     * @return The JavaScript string
     */
    protected String createJavaScriptWidgetRegistrationClosure() {
        return ")";
    }
    
    /**
     * Renders widget opening.
     * 
     * @param protocol The protocol used.
     * @param attributes The widget attributes.
     * @throws ProtocolException
     */
    protected void renderWidgetOpen(VolantisProtocol protocol, MCSAttributes attributes) throws ProtocolException {
        getWidgetDefaultModule(protocol)
            .getWidgetRenderer(attributes)
            .renderOpen(protocol, attributes);
    }

    /**
     * Renders widget closure.
     * 
     * @param protocol The protocol used.
     * @param attributes The widget attributes.
     * @throws ProtocolException
     */
    protected void renderWidgetClose(VolantisProtocol protocol, MCSAttributes attributes) throws ProtocolException {
        getWidgetDefaultModule(protocol)
            .getWidgetRenderer(attributes)
            .renderClose(protocol, attributes);
    }
    
    /**
     * Renders widget closure of the currently rendered widget. Returns
     * attributes of widget being closed (the attributes which were passed to
     * the renderWidgetOpen() method).
     * 
     * @param protocol The protocol used.
     * @returns closed widget attributes.
     * @throws ProtocolException
     */
    protected MCSAttributes renderWidgetClose(VolantisProtocol protocol) throws ProtocolException {
        MCSAttributes attributes = getCurrentAttributes(protocol);
        
        renderWidgetClose(protocol, attributes);
        
        return attributes;
    }
    
    /**
     * Returns attributes of the parent widget being currently rendered,
     * or null if no parent widget is currently rendered.
     * This method should be called only between doRenderOpen() or doRenderClose() 
     * methods calls.
     * 
     * @return parent widget attributes.
     */
    protected MCSAttributes getParentAttributes(VolantisProtocol protocol) {
        MCSAttributes parentAttributes = null;
        
        Stack stack = getWidgetDefaultModule(protocol).getInnerAttributesStack();
        
        if (stack.size() >= 2) {
            parentAttributes = (MCSAttributes) stack.get(stack.size() - 2);
        }
        
        return parentAttributes;
    }

    /**
     * Returns attributes of the currently rendered widget, or null if no widget
     * is being currently rendered.
     * 
     * In other words, this method returns the attributes of the widget, which
     * has just finished invoking its doRenderOpen() method.
     * 
     * @return The widget attributes.
     */
    protected MCSAttributes getCurrentAttributes(VolantisProtocol protocol) {
        MCSAttributes parentAttributes = null;
        
        Stack stack = getWidgetDefaultModule(protocol).getInnerAttributesStack();
        
        if (stack.size() >= 1) {
            parentAttributes = (MCSAttributes) stack.get(stack.size() - 1);
        }
        
        return parentAttributes;
    }
}
