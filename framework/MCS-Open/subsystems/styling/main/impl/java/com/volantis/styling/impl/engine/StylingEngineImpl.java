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

package com.volantis.styling.impl.engine;

import com.volantis.mcs.css.parser.CSSParser;
import com.volantis.mcs.css.parser.CSSParserFactory;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.themes.InlineStyleSelector;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleSheet;
import com.volantis.mcs.themes.StyleSheetFactory;
import com.volantis.mcs.themes.StyleValues;
import com.volantis.shared.iteration.IterationAction;
import com.volantis.styling.Styles;
import com.volantis.styling.compiler.StyleSheetCompiler;
import com.volantis.styling.compiler.StyleSheetCompilerFactory;
import com.volantis.styling.debug.DebugStyles;
import com.volantis.styling.debug.DebugStylingWriter;
import com.volantis.styling.engine.Attributes;
import com.volantis.styling.engine.StyleSheetMerger;
import com.volantis.styling.engine.StylingEngine;
import com.volantis.styling.expressions.EvaluationContext;
import com.volantis.styling.impl.StylesImpl;
import com.volantis.styling.impl.counter.CounterEngine;
import com.volantis.styling.impl.engine.inferring.Inferrer;
import com.volantis.styling.impl.engine.listeners.Listener;
import com.volantis.styling.impl.engine.listeners.ListenerIteratee;
import com.volantis.styling.impl.engine.listeners.MutableListeners;
import com.volantis.styling.impl.engine.listeners.MutableListenersImpl;
import com.volantis.styling.impl.engine.matchers.MatcherContext;
import com.volantis.styling.impl.engine.matchers.composites.DepthChangeListener;
import com.volantis.styling.impl.engine.sheet.AppendMerger;
import com.volantis.styling.impl.engine.sheet.ImmutableStylerList;
import com.volantis.styling.impl.engine.sheet.MutableStylerList;
import com.volantis.styling.impl.engine.sheet.MutableStylerListImpl;
import com.volantis.styling.impl.engine.sheet.StyleSheetStack;
import com.volantis.styling.impl.engine.sheet.StylerList;
import com.volantis.styling.impl.engine.sheet.StylerListMerger;
import com.volantis.styling.impl.expressions.EvaluationContextImpl;
import com.volantis.styling.impl.expressions.PropertyValuesEvaluator;
import com.volantis.styling.impl.sheet.StyleSheetInternal;
import com.volantis.styling.impl.sheet.Styler;
import com.volantis.styling.properties.MutableStylePropertySet;
import com.volantis.styling.properties.MutableStylePropertySetImpl;
import com.volantis.styling.sheet.CompiledStyleSheet;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.Iterator;
import java.util.List;

/**
 * An implementation of {@link StylingEngine}.
 */
public class StylingEngineImpl
        implements StylingEngine {

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
             LocalizationFactory.createLogger(StylingEngineImpl.class);

    /**
     * Set bits for those properties that are interesting and hence will be
     * output during debugging.
     */
    private static final MutableStylePropertySet INTERESTING;
    private static final DebugStylingWriter DEBUG_INTERESTING_STYLES_WRITER;
    static {
        INTERESTING = new MutableStylePropertySetImpl();

        INTERESTING.add(StylePropertyDetails.COLOR);
        INTERESTING.add(StylePropertyDetails.FONT_SIZE);
        INTERESTING.add(StylePropertyDetails.FONT_FAMILY);
        INTERESTING.add(StylePropertyDetails.FONT_WEIGHT);
        INTERESTING.add(StylePropertyDetails.BACKGROUND_COLOR);
        INTERESTING.add(StylePropertyDetails.BACKGROUND_IMAGE);
        INTERESTING.add(StylePropertyDetails.WIDTH);
        INTERESTING.add(StylePropertyDetails.HEIGHT);
        INTERESTING.add(StylePropertyDetails.TEXT_ALIGN);
        INTERESTING.add(StylePropertyDetails.VERTICAL_ALIGN);
        INTERESTING.add(StylePropertyDetails.BORDER_BOTTOM_STYLE);
        INTERESTING.add(StylePropertyDetails.BORDER_BOTTOM_WIDTH);
        INTERESTING.add(StylePropertyDetails.BORDER_BOTTOM_COLOR);
        INTERESTING.add(StylePropertyDetails.BORDER_TOP_STYLE);
        INTERESTING.add(StylePropertyDetails.BORDER_TOP_WIDTH);
        INTERESTING.add(StylePropertyDetails.BORDER_LEFT_STYLE);
        INTERESTING.add(StylePropertyDetails.BORDER_LEFT_WIDTH);
        INTERESTING.add(StylePropertyDetails.BORDER_RIGHT_STYLE);
        INTERESTING.add(StylePropertyDetails.BORDER_RIGHT_WIDTH);
        INTERESTING.add(StylePropertyDetails.BORDER_SPACING);
        INTERESTING.add(StylePropertyDetails.PADDING_BOTTOM);
        INTERESTING.add(StylePropertyDetails.PADDING_TOP);
        INTERESTING.add(StylePropertyDetails.PADDING_LEFT);
        INTERESTING.add(StylePropertyDetails.PADDING_RIGHT);
        INTERESTING.add(StylePropertyDetails.TEXT_ALIGN);
        INTERESTING.add(StylePropertyDetails.VERTICAL_ALIGN);
        INTERESTING.add(StylePropertyDetails.FONT_STYLE);
        INTERESTING.add(StylePropertyDetails.DISPLAY);

        DEBUG_INTERESTING_STYLES_WRITER = new DebugStylingWriter(logger,
                INTERESTING);
    }

    private static final DebugStylingWriter DEBUG_ALL_STYLES_WRITER =
            new DebugStylingWriter(logger, null);

    private static final AppendMerger MERGER = new AppendMerger();

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
     * {@link DepthChangeListener#beforeStartElement} method on all the
     * registered listeners.
     */
    private final ListenerIteratee beforeStartElementIteratee;

    /**
     * The object responsible for invoking the
     * {@link DepthChangeListener#afterEndElement} method on all the
     * registered listeners.
     */
    private final ListenerIteratee afterEndElementIteratee;

    /**
     * The values of the root node.
     */
    private final StyleValues rootValues;

    /**
     * The list of stylers that will be applied by the engine.
     */
    private final MutableStylerList stylerList;

    /**
     * A stack of style sheets that have been pushed.
     */
    private final StyleSheetStack styleSheetStack;

    /**
     * The context used by the {@link Styler}s.
     */
    private final StandardStylerContext stylerContext;

    private final Inferrer inferrer;

    /**
     * The {@link CounterEngine} that will be used to handle counters within
     * the document.
     */
    private final CounterEngine counterEngine;

    /**
     * StyleSheetCompilerFactory to be used for creating styleSheetCompilers
     */
    private final StyleSheetCompilerFactory styleSheetCompilerFactory;

    /**
     * The context within which functions will be evaluated.
     */
    private final EvaluationContextImpl evaluationContext;

    /**
     * The parser used for processing inline style attribute values.
     */
    private CSSParser inlineStyleAttributeParser;

    /**
     * The styles that result from matching the element.
     */
    private Styles styles;

    /**
     * Initialise.
     */
    public StylingEngineImpl(
            StyleSheetCompilerFactory styleSheetCompilerFactory) {

        this.elementStack = new ElementStack();

        MatcherContext matcherContext = new MatcherContextImpl(elementStack);

        listeners = new MutableListenersImpl();

        this.stylerContext = new StandardStylerContextImpl(matcherContext,
                listeners);

        this.styleSheetStack = new StyleSheetStack();

        // Create iteratees that will invoke the depth change listeners before
        // start and after end element events.
        beforeStartElementIteratee = new BeforeStartElementIteratee(
                matcherContext);
        afterEndElementIteratee = new AfterEndElementIteratee(
                matcherContext);

        stylerList = new MutableStylerListImpl();

        counterEngine = new CounterEngine();

        this.rootValues = StylePropertyDetails.getDefinitions()
                .getStandardDetailsSet().getRootStyleValues();

        // Create the evaluation context.
        evaluationContext = new EvaluationContextImpl(counterEngine);

        // Create the styles evaluator.
        PropertyValuesEvaluator propertyValuesEvaluator =
                new PropertyValuesEvaluator(evaluationContext);

        // Create the cascader.
        cascader = new CascaderImpl(stylerList, stylerContext,
                listeners);

        inferrer = new Inferrer(counterEngine, propertyValuesEvaluator);

        this.styleSheetCompilerFactory = styleSheetCompilerFactory;

        debug("Initialisation");
    }

    /**
     * Initialise.
     */
    public StylingEngineImpl() {
        this(null);
    }

    /**
     * Wrap the listeners in ones that will update the context appropriately
     * for the source style sheet and add them to the list of listeners.
     * @param styleSheet The style sheet whose listeners should be added.
     * @param depth
     */
    private void addEngineListeners(
            StyleSheetInternal styleSheet,
            final int depth) {

        styleSheet.getListeners().iterate(new ListenerIteratee() {
            public IterationAction next(Listener listener) {
                listeners.addListener(new EngineDepthChangeListener(
                        (DepthChangeListener) listener,
                        depth));
                return IterationAction.CONTINUE;
            }
        });
    }

    private void debug(String when) {
        if (logger.isDebugEnabled()) {
            DEBUG_ALL_STYLES_WRITER
                    .print("Styling Engine Contents (" + when + ")").newline()
                    .print(stylerList);
            DEBUG_ALL_STYLES_WRITER.flush();
        }
    }

    private MutableStylerList createEngineStylerList(
            StyleSheetInternal internalStyleSheet,
            int depth) {

        ImmutableStylerList stylerList = internalStyleSheet.getStylerList();
        MutableStylerList engineStylerList = new MutableStylerListImpl();
        for (Iterator i = stylerList.iterator(); i.hasNext();) {
            Styler styler = (Styler) i.next();
            EngineStyler engineStyler = new EngineStylerImpl(
                    styler, depth);
            engineStylerList.append(engineStyler);
        }

        return engineStylerList;
    }

    // Javadoc inherited.
    public void startElement(String namespace, String localName,
                             Attributes attributes) {

        if (namespace == null) {
            throw new IllegalArgumentException("namespace cannot be null");
        }
        if (localName == null) {
            throw new IllegalArgumentException("localName cannot be null");
        }
        if (attributes == null) {
            throw new IllegalArgumentException("attributes cannot be null");
        }

        // Process any inline styles being applied through the style attribute.
        // If a style attribute value has been specified it is compiled into a
        // style sheet and pushed into the style sheet container. Style
        // attributes should only effect the element they were specified for
        // so to ensure this happens a Matcher is used to match element ids.
        // These element ids are set using the current style depth before the
        // additional style sheet is added.
        String inlineStyleValue = attributes.getAttributeValue(null, "style");
        CompiledStyleSheet compiledStyleSheet = null;
        int currentStyleDepth = styleSheetStack.depth();
        if (inlineStyleValue != null) {
            compiledStyleSheet = compileInlineStyleValue(inlineStyleValue);
            if (compiledStyleSheet != null) {
                pushStyleSheet(compiledStyleSheet);
            }
        }

        // Get the parent's properties, if this is the root then use the
        // initial values, otherwise use the parent element's values.
        StyleValues inheritableValues;
        ElementStackFrame parent = elementStack.getCurrentElementStackFrame();

        if (parent == null) {
            inheritableValues = rootValues;
        } else {
            inheritableValues = parent.getValues();
        }

        // Update the context with the new element information.
        elementStack.push(namespace, localName,
                currentStyleDepth, compiledStyleSheet);

        // Invoke the depth change listeners to inform them that a start
        // element event is about to be processed.
        listeners.iterate(beforeStartElementIteratee);

        // Do the cascade.
        styles = new StylesImpl();
        stylerContext.setStyles(styles);

        cascader.cascade(attributes);

        // Perform inferrance, counters, evaluation of values. Making sure that
        // the functions have access to attribute values.
        evaluationContext.setAttributes(attributes);
        inheritableValues = inferrer.infer(styles, inheritableValues);

        // Store the inheritable values so that they are available
        // should nested elements need to inherit values.
        elementStack.setPropertyValues(inheritableValues);

        if (logger.isDebugEnabled()) {
            DebugStyles debugStyles = new DebugStyles(INTERESTING, false, true);
            String debug = "Styled {" + namespace + "} " + localName;
            String value = attributes.getAttributeValue(null, "class");
            if (value != null) {
                debug += " class='" + value + "'";
            }
            value = attributes.getAttributeValue(null, "id");
            if (value != null) {
                debug += " id='" + value + "'";
            }
            debug += " with " + debugStyles.output(styles, "");
            
            logger.debug(debug);
        }
    }

    // Javadoc inherited.
    public void endElement(String namespace, String localName) {
    	
    	ElementStackFrame current = elementStack.getCurrentElementStackFrame();
        List nestedStyleSheet=current.getNestedStyleSheets();
        if (nestedStyleSheet!=null) {
	        for (int i = 0; i < nestedStyleSheet.size(); i++) {
	            this.popStyleSheet((CompiledStyleSheet) nestedStyleSheet.get(i));
	        } 
	        nestedStyleSheet.clear();
        }
        
        counterEngine.endElement();

        // Invoke the depth change listeners to inform them that an end
        // element event has been processed.
        listeners.iterate(afterEndElementIteratee);
        
        // Discard the element information from the context.
        ElementStackFrame elementStackFrame =
                elementStack.pop(namespace, localName);
        
        //process any inline style sheet which may have been added to the stack
        CompiledStyleSheet inlineStyleSheet =
                elementStackFrame.getInlineStyleSheet();

        if (inlineStyleSheet != null) {
            popStyleSheet(inlineStyleSheet);
        }
    }

    /**
     * Process the style string into a compiled style sheet.
     *
     * The compiled style sheet will be configured for processing style
     * attributes.
     * @param inlineStyleValue
     * @return A compiled style sheet representation of the style string.
     */
    private CompiledStyleSheet compileInlineStyleValue(String inlineStyleValue) {
        //lazily instantiate the inlineStyleAttributeParser
        if (inlineStyleAttributeParser == null) {
            CSSParserFactory parserFactory =
                    CSSParserFactory.getDefaultInstance();
            inlineStyleAttributeParser = parserFactory.createLaxParser();
        }

        InlineStyleSelector styleSelector =
                StyleSheetFactory.getDefaultInstance().
                        createInlineStyleSelector(styleSheetStack.depth());

        //Using the parser, process the style attribute into a Style Sheet.
        StyleSheet styleSheet =
                inlineStyleAttributeParser.parseInlineStyleAttribute(
                inlineStyleValue, null, styleSelector);

        StyleSheetCompiler styleSheetCompiler =
                styleSheetCompilerFactory.
                        createStyleSheetCompiler();

        return styleSheetCompiler.compileStyleSheet(styleSheet);
    }

    // Javadoc inherited.
    public Styles getStyles() {
        return styles;
    }

    // Javadoc inherited.
    public void pushStyleSheet(
            CompiledStyleSheet styleSheet, StyleSheetMerger merger) {

        // todo: Add a check to make sure that the pushed style sheets are using the same set of style properties.

        // Push the style sheet.
        styleSheetStack.push(styleSheet);

        // Get the depth after pushing style sheet.
        int depth = styleSheetStack.depth();

        // Create a state container.
        StyleSheetInternal internalStyleSheet = (StyleSheetInternal) styleSheet;

        // Create a list of EngineStyler wrappers around the style sheets
        // Styler objects.
        StylerList deltaEngineStylerList = createEngineStylerList(
                internalStyleSheet, depth);

        // Merge the list of EngineStyler's into the current one.
        StylerListMerger stylerListMerger = (StylerListMerger) merger;
        stylerListMerger.merge(stylerList, deltaEngineStylerList);

        // Add the listeners from the style sheet.
        addEngineListeners(internalStyleSheet, depth);

        debug("After Pushing");
    }

    public void pushStyleSheet(CompiledStyleSheet styleSheet) {
        pushStyleSheet(styleSheet, MERGER);
    }

    // Javadoc inherited.
    public void popStyleSheet(CompiledStyleSheet styleSheet) {

        // Get the depth of the stack before popping the style sheet.
        int depth = styleSheetStack.depth();

        // Pop the style sheet.
        styleSheetStack.pop(styleSheet);

        // Iterate over the stylers removing those that were added as part
        // of adding the style sheet, i.e. those that were added at the same
        // depth as the style sheet that has just been popped.
        for (Iterator i = stylerList.listIterator(); i.hasNext();) {
            EngineStyler engineStyler = (EngineStyler) i.next();
            if (engineStyler.getDepth() == depth) {
                i.remove();
            }
        }

        // Iterate over the listeners removing those that were added as part
        // of adding the style sheet, i.e. those that were added at the same
        // depth as the style sheet that has just been popped.
        for (Iterator i = listeners.iterator(); i.hasNext();) {
            DepthLevel level = (DepthLevel)i.next();
            if( level.getDepth() == depth) {
                i.remove();
            }
        }
    }

    // Javadoc inherited.
    public void pushPropertyValues(final StyleValues propertyValues) {
        final ElementStackFrame current =
                elementStack.getCurrentElementStackFrame();

        ElementStackFrame frame = new DelegatingElementStackFrame(
                current, propertyValues);
        elementStack.pushElementStackFrame(frame);
    }

    // Javadoc inherited.
    public void popPropertyValues(StyleValues propertyValues) {
    	
        ElementStackFrame popped = elementStack.popElementStackFrame();
        StyleValues poppedValues = popped.getValues();
        if (poppedValues != propertyValues) {
            throw new IllegalStateException(
                    "Property values being popped " + poppedValues +
                    " do not match expected " + propertyValues);
        }
    }

    // Javadoc inherited
    public int getCounterValue(String counterName) {
        return counterEngine.getCounter(counterName, true).intValue();
    }

    public int[] getCounterValues(String counterName) {
        return counterEngine.getCounterValues(counterName, true);
    }

    // Javadoc inherited.
    public EvaluationContext getEvaluationContext() {
        return evaluationContext;
    }

    public void addNestedStyleSheet(CompiledStyleSheet styleSheet)
    {
    	ElementStackFrame current =
            elementStack.getCurrentElementStackFrame();
    	
    	current.addNestedStyleSheet(styleSheet);
    	this.pushStyleSheet(styleSheet);
    }
    
    private static class DelegatingElementStackFrame implements ElementStackFrame {
        private final ElementStackFrame delegate;
        private final StyleValues propertyValues;

        public DelegatingElementStackFrame(
                ElementStackFrame delegate, StyleValues propertyValues) {
            this.delegate = delegate;
            this.propertyValues = propertyValues;
        }

        public String getNamespace() {
            return delegate.getNamespace();
        }

        public void setNamespace(String namespace) {
            delegate.setNamespace(namespace);
        }

        public String getLocalName() {
            return delegate.getLocalName();
        }

        public void setLocalName(String localName) {
            delegate.setLocalName(localName);
        }

        public int getChildCount() {
            return delegate.getChildCount();
        }

        public void setChildCount(int childCount) {
            delegate.setChildCount(childCount);
        }

        public StyleValues getValues() {
            return propertyValues;
        }

        public void setValues(StyleValues values) {
            throw new UnsupportedOperationException();
        }

        public void incrementChildCount() {
            // This may be called when this object is the top of the stack and
            // therefore does not have an object to which it can delegate. It
            // this case simple do nothing.
            if (delegate != null) {
                delegate.incrementChildCount();
            }
        }

        //javadoc inherited
        public void setElementId(int elementId) {
            delegate.setElementId(elementId);
        }

        //javadoc inherited
        public int getElementId() {
            return delegate.getElementId();
        }

        public void setInlineStyleSheet(CompiledStyleSheet styleSheet) {
            delegate.setInlineStyleSheet(styleSheet);
        }

        public CompiledStyleSheet getInlineStyleSheet() {
            return delegate.getInlineStyleSheet();
        }

        public void addNestedStyleSheet(CompiledStyleSheet styleSheet) {
        	delegate.addNestedStyleSheet(styleSheet);
        }
        
        public List getNestedStyleSheets() {
            return delegate.getNestedStyleSheets();
        }
        
        
    }
   
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10647/1	ibush	VBM:2005113021 Fix Border Bottom Styling by fixing styles merger

 06-Dec-05	10628/2	ibush	VBM:2005113021 Fix Border Bottom Styling by fixing styles merger

 06-Dec-05	10641/1	geoff	VBM:2005113024 Pagination page rendering issues

 06-Dec-05	10621/1	geoff	VBM:2005113024 Pagination page rendering issues

 05-Dec-05	10527/3	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 05-Dec-05	10512/4	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 25-Nov-05	10394/1	ibush	VBM:2005111812 interim commit for Willobs

 28-Nov-05	10467/1	ibush	VBM:2005111812 Styling Fixes for Orange Test Page

 25-Nov-05	10394/1	ibush	VBM:2005111812 interim commit for Willobs

 30-Sep-05	9635/3	adrianj	VBM:2005092817 Counter functions for CSS

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 22-Sep-05	9578/4	adrianj	VBM:2005092102 Fixing merge conflicts

 22-Sep-05	9578/1	adrianj	VBM:2005092102 Integrate counters into styling engine

 20-Sep-05	9128/1	pabbott	VBM:2005071114 Add XHTML 2 elements

 22-Aug-05	9298/1	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 22-Jul-05	9110/1	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 18-Jul-05	9029/4	pduffin	VBM:2005071301 Adding ability for styling engine to support nested style sheets

 15-Jul-05	9067/3	geoff	VBM:2005071415 More refactoring for: XDIMECP: Generate optimised CSS for a DOM.

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
