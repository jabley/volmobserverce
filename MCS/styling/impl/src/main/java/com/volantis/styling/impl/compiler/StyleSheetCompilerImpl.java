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

package com.volantis.styling.impl.compiler;

import com.volantis.mcs.themes.Priority;
import com.volantis.mcs.themes.PropertyValue;
import com.volantis.mcs.themes.Selector;
import com.volantis.mcs.themes.StyleProperties;
import com.volantis.mcs.themes.StyleSheet;
import com.volantis.mcs.themes.Rule;
import com.volantis.styling.PseudoStyleEntities;
import com.volantis.styling.PseudoStyleEntity;
import com.volantis.styling.compiler.CompilerConfiguration;
import com.volantis.styling.compiler.FunctionResolver;
import com.volantis.styling.compiler.Source;
import com.volantis.styling.compiler.Specificity;
import com.volantis.styling.compiler.SpecificityCalculator;
import com.volantis.styling.compiler.StyleSheetCompiler;
import com.volantis.styling.impl.engine.Prioritised;
import com.volantis.styling.impl.engine.PropertySplitter;
import com.volantis.styling.impl.engine.PropertySplitterImpl;
import com.volantis.styling.impl.engine.StylerImpl;
import com.volantis.styling.impl.engine.StylerSpecificityComparator;
import com.volantis.styling.impl.engine.listeners.ImmutableListeners;
import com.volantis.styling.impl.engine.listeners.MutableListeners;
import com.volantis.styling.impl.engine.listeners.MutableListenersImpl;
import com.volantis.styling.impl.engine.matchers.Matcher;
import com.volantis.styling.impl.engine.matchers.MatcherBuilderContext;
import com.volantis.styling.impl.engine.sheet.CompiledStyleSheetImpl;
import com.volantis.styling.impl.engine.sheet.ImmutableStylerList;
import com.volantis.styling.impl.engine.sheet.MutableStylerList;
import com.volantis.styling.impl.engine.sheet.MutableStylerListImpl;
import com.volantis.styling.impl.sheet.Styler;
import com.volantis.styling.impl.sheet.StylesDelta;
import com.volantis.styling.impl.state.ImmutableStateRegistry;
import com.volantis.styling.impl.state.MutableStateRegistry;
import com.volantis.styling.impl.state.StateFactory;
import com.volantis.styling.sheet.CompiledStyleSheet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Compile a {@link StyleSheet}.
 */
public abstract class StyleSheetCompilerImpl
        implements StyleSheetCompiler {

    /**
     * The comparator to use to compare {@link Styler}s by specificity.
     */
    private static final
    StylerSpecificityComparator STYLER_SPECIFICITY_COMPARATOR =
            new StylerSpecificityComparator();

    private static final StateFactory STATE_FACTORY =
            StateFactory.getDefaultInstance();

    private final Source source;

    private final MatcherBuilder matcherBuilder;

    private final PropertySplitter splitter;

    private final MutableStateRegistry stateRegistry =
            STATE_FACTORY.createStateRegistry();

    private final MutableListeners listeners = new MutableListenersImpl();

    /**
     * Initialise.
     *
     * @param configuration The configuration for this compiler.
     * @param matcherFactory
     */
    public StyleSheetCompilerImpl(
            CompilerConfiguration configuration,
            MatcherFactory matcherFactory) {

        source = configuration.getSource();
        if (source == null) {
            throw new IllegalArgumentException("source cannot be null");
        }

        PseudoStyleEntities supportedPseudoEntities =
                configuration.getSupportedPseudoEntities();
        SpecificityCalculator specificityCalculator =
                configuration.getSpecificityCalculator();

        // Create an object to uniquely identify a style sheet.
        MatcherBuilderContext builderContext =
                new MatcherBuilderContextImpl(stateRegistry, listeners);

        MatcherBuilderConfiguration matcherConfiguration =
                new MatcherBuilderConfiguration();
        matcherConfiguration.setFactory(matcherFactory);
        matcherConfiguration.setEntities(supportedPseudoEntities);
        matcherConfiguration.setSpecificityCalculator(specificityCalculator);
        matcherConfiguration.setBuilderContext(builderContext);

        matcherBuilder = new MatcherBuilderImpl(matcherConfiguration);

        // Create a value compiler that will be used to compile all the values.
        FunctionResolver resolver = ((CompilerConfigurationImpl) configuration)
                .getResolver();
        ValueCompiler valueCompiler = new ValueCompilerImpl(resolver);

        // Create an
        splitter = new PropertySplitterImpl(valueCompiler);
    }

    // Javadoc inherited.
    public CompiledStyleSheet compileStyleSheet(StyleSheet styleSheet) {

//        List importantStylers = new ArrayList();

        List stylers = createStylers(styleSheet, source);

        ImmutableStylerList orderedStylerList =
                createOrderedStylerList(stylers);
        
        CompiledStyleSheet compiledStyleSheet = createCompiled(
                orderedStylerList, stateRegistry.createImmutableStateRegistry(),
                listeners.createImmutableListeners());

        return compiledStyleSheet;
    }

    protected CompiledStyleSheetImpl createCompiled(
            final ImmutableStylerList orderedStylerList,
            final ImmutableStateRegistry registry,
            final ImmutableListeners listeners) {

        return new CompiledStyleSheetImpl(orderedStylerList,
                registry, listeners);
    }

    /**
     * Create the {@link Styler}s from the style sheet.
     *
     * @param styleSheet The style sheet, may be null.
     *
     * @param source
     * @return A non null but possibly empty list of {@link Styler}s.
     */
    private List createStylers(StyleSheet styleSheet, Source source) {

        // If the style sheet is empty then return an empty list.
        if (styleSheet == null) {
            return Collections.EMPTY_LIST;
        }

        List stylers = new ArrayList();
        List rules = styleSheet.getRules();
        for (Iterator i = rules.iterator(); i.hasNext();) {
            Rule rule = (Rule) i.next();
            List selectors = rule.getSelectors();
            StyleProperties properties = rule.getProperties();

            if (properties != null) {
                addStylers(properties, selectors, source, stylers);
            }
        }
        return stylers;
    }

    /**
     * Split the properties into normal and important, and add the stylers for
     * each group of properties if it contains values.
     *
     * <p>This only needs to be done once per rule as the properties can be
     * shared.</p>
     *
     * @param properties The properties to split
     * @param selectors The selectors in the associated rule
     * @param source                                                           
     * @param stylers The list of stylers to which any newly created ones should
     *                be added
     */
    private void addStylers(StyleProperties properties, List selectors,
                            Source source, List stylers) {
        // Split the properties into normal and important, if there are
        // no important properties then do nothing. This only needs to be
        // done once per rule as the properties can be shared.
        Prioritised[] prioritised = splitter.split(properties);

        // Iterate over the list of selectors.
        for (Iterator s = selectors.iterator(); s.hasNext();) {
            Selector selector = (Selector) s.next();

            // Get the matcher and associated information from the builder.
            Matcher matcher = matcherBuilder.getMatcher(selector);

            // Get the possibly empty list of pseudo style entities.
            List pseudoStyleEntities = matcherBuilder.getPseudoStyleEntities();

            // Get the specificity.
            Specificity specificity = matcherBuilder.getSpecificity();

            // Create an array of entities from the list.
            PseudoStyleEntity[] entities;
            int entityCount = pseudoStyleEntities.size();
            if (entityCount == 0) {
                entities = null;
            } else {
                entities = new PseudoStyleEntity[entityCount];
                pseudoStyleEntities.toArray(entities);
            }

            for (int i = 0; i < prioritised.length; i++) {
                Prioritised p = prioritised[i];

                Priority priority = p.getPriority();
                PropertyValue[] values = p.getValues();

                if (values != null && values.length > 0) {
                    StylesDelta delta = createStylesDelta(entities, values);
                    Styler styler = new StylerImpl(source, priority,
                            specificity, matcher, delta);
                    stylers.add(styler);
                }
            }
        }
    }

    /**
     * Create a {@link ImmutableStylerList} whose contents are ordered by
     * specificity.
     *
     * @param stylers The {@link List} of {@link Styler}s.
     *
     * @return An {@link ImmutableStylerList}.
     */
    private ImmutableStylerList createOrderedStylerList(List stylers) {
        // Sort the stylers by specificity.
        Collections.sort(stylers, STYLER_SPECIFICITY_COMPARATOR);
        MutableStylerList list = new MutableStylerListImpl();
        for (Iterator iterator = stylers.iterator(); iterator.hasNext();) {
            Styler styler = (Styler) iterator.next();
            list.append(styler);
        }
        return list.createImmutableStylerList();
    }

    protected abstract StylesDelta createStylesDelta(
            PseudoStyleEntity[] entities, PropertyValue[] values);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10641/1	geoff	VBM:2005113024 Pagination page rendering issues

 06-Dec-05	10621/1	geoff	VBM:2005113024 Pagination page rendering issues

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 05-Sep-05	9407/3	pduffin	VBM:2005083007 Removed old themes model

 31-Aug-05	9407/1	pduffin	VBM:2005083007 Fixed issue with build

 22-Aug-05	9298/1	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 18-Aug-05	9007/2	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 22-Jul-05	9110/1	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 18-Jul-05	9029/7	pduffin	VBM:2005071301 Adding ability for styling engine to support nested style sheets

 15-Jul-05	9067/4	geoff	VBM:2005071415 More refactoring for: XDIMECP: Generate optimised CSS for a DOM.
 18-Jul-05	9029/5	pduffin	VBM:2005071301 Adding ability for styling engine to support nested style sheets

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
