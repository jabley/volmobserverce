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
package com.volantis.mcs.dom2theme.impl.generator.rule.clazz;

import com.volantis.mcs.dom2theme.impl.generator.rule.RuleExtractor;
import com.volantis.mcs.dom2theme.impl.model.OutputStyledElement;
import com.volantis.mcs.dom2theme.impl.model.OutputStyledElementIteratee;
import com.volantis.mcs.dom2theme.impl.model.OutputStyledElementList;
import com.volantis.mcs.dom2theme.impl.model.OutputStyles;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.themes.StyleSheet;
import com.volantis.shared.iteration.IterationAction;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.HashMap;
import java.util.Map;

/**
 * Extracts class rules from the set of elements.
 */
public class ClassRuleExtractor
        implements RuleExtractor, OutputStyledElementIteratee {

    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(ClassRuleExtractor.class);

    /**
     * Map from {@link OutputStyles} to the class name as a {@link String}.
     */
    private final Map stylesToClassName = new HashMap(); // "reverse" mapping

    private final StylesClassRuleExtractorFactory factory;

    /**
     * The generator for class names.
     */
    private final ClassNameGenerator classNameGenerator;

    private StylesClassRuleExtractor stylesExtractor;

    private int classIndex;

    /**
     * Initialise.
     *
     * @param factory The factory for creating
     *                {@link StylesClassRuleExtractor}s.
     */
    public ClassRuleExtractor(StylesClassRuleExtractorFactory factory) {
        this(factory, SimpleClassNameGenerator.getDefaultInstance());
    }

    /**
     * Initialise.
     *
     * @param factory            The factory for creating
     *                           {@link StylesClassRuleExtractor}s.
     * @param classNameGenerator The generator for class names.
     */
    public ClassRuleExtractor(StylesClassRuleExtractorFactory factory,
                              ClassNameGenerator classNameGenerator) {
        this.factory = factory;
        this.classNameGenerator = classNameGenerator;
    }
    
    public StyleSheet extractRules(OutputStyledElementList outputElementList,
            final StyleSheet styleSheet) {

        // ==================================================================
        // 2 - Extract rules from dom using generated class selectors.
        // ==================================================================
        //
        // 1. Iterate over the elements within the input DOM that have style
        // properties remaining, either on them or on nested pseudo styleable
        // entities.

        stylesExtractor = factory.create(styleSheet);

        outputElementList.iterate(this);

        return styleSheet;
    }

    public IterationAction next(OutputStyledElement element) {

        // 2. Search the mappings for a class associated with that set
        // of style properties.

        OutputStyles styles = element.getStyles();
        if (styles != null && !styles.isEmpty()) {
            String className = (String) stylesToClassName.get(styles);
            if (className != null) {

                // 3.a If it exists then use that class,
                if (logger.isDebugEnabled()) {
                    logger.debug("Using existing class " + className +
                            " for element " + element.getName());
                }

            } else {

                // 3.b Otherwise, create a new class and new rules that
                // uses a class selector and other pseudo styleable entity
                // selectors to associate the style properties with the
                // element and its pseudo styleable entities.

                className = classNameGenerator.getClassName(classIndex);

                if (logger.isDebugEnabled()) {
                    logger.debug("Using new class " + className +
                            " for element " + element.getName());
                }

                stylesExtractor.extractClassRules(styles, className);

                // Save a record of this styles -> class mapping for later.
                stylesToClassName.put(styles, className);

                // Increment the index we will use for the next class.
                classIndex++;
            }

            // 4. Set the class attribute on the element and clear all its
            // style properties.
            element.setClass(className);
            element.clearStyles();
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("Element " + element.getName() +
                        " has no styles so no class generated");
            }
        }

        return IterationAction.CONTINUE;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 18-Jul-05	8668/16	geoff	VBM:2005060302 XDIMECP: Generate optimised CSS for a DOM.

 ===========================================================================
*/
