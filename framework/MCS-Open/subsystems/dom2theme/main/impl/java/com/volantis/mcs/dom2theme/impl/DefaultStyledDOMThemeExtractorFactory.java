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
package com.volantis.mcs.dom2theme.impl;

import com.volantis.mcs.dom2theme.ExtractorContext;
import com.volantis.mcs.dom2theme.StyledDOMStyleAttributeRenderer;
import com.volantis.mcs.dom2theme.StyledDOMThemeExtractor;
import com.volantis.mcs.dom2theme.StyledDOMThemeExtractorFactory;
import com.volantis.mcs.dom2theme.StyledDocumentOptimizer;
import com.volantis.mcs.dom2theme.extractor.ExtractorConfiguration;
import com.volantis.mcs.dom2theme.extractor.ExtractorConfigurationBuilder;
import com.volantis.mcs.dom2theme.impl.extractor.ExtractorConfigurationBuilderImpl;
import com.volantis.mcs.dom2theme.impl.generator.DOMThemeGenerator;
import com.volantis.mcs.dom2theme.impl.generator.DefaultDOMThemeGenerator;
import com.volantis.mcs.dom2theme.impl.generator.rule.GroupRuleExtractor;
import com.volantis.mcs.dom2theme.impl.generator.rule.RuleExtractor;
import com.volantis.mcs.dom2theme.impl.generator.rule.builder.RuleBuilderFactory;
import com.volantis.mcs.dom2theme.impl.generator.rule.clazz.ClassRuleExtractor;
import com.volantis.mcs.dom2theme.impl.generator.rule.clazz.OptimalClassNameGenerator;
import com.volantis.mcs.dom2theme.impl.generator.rule.clazz.StylesClassRuleExtractorFactory;
import com.volantis.mcs.dom2theme.impl.generator.rule.clazz.StylesClassRuleExtractorFactoryImpl;
import com.volantis.mcs.dom2theme.impl.generator.rule.type.TypeRuleExtractor;
import com.volantis.mcs.dom2theme.impl.model.OutputStyledElementIteratee;
import com.volantis.mcs.dom2theme.impl.optimizer.DefaultStyledDOMOptimizer;
import com.volantis.mcs.dom2theme.impl.optimizer.InputPropertiesOptimizer;
import com.volantis.mcs.dom2theme.impl.optimizer.PropertiesOptimizer;
import com.volantis.mcs.dom2theme.impl.optimizer.StyledDOMOptimizer;
import com.volantis.mcs.dom2theme.impl.rewriter.DOMStyleAttributeRewriter;
import com.volantis.mcs.themes.ShorthandSet;
import com.volantis.styling.properties.PropertyDetailsSet;

/**
 * Implementation of {@link StyledDOMThemeExtractorFactory}
 * @todo better name for this class is DefaultStyledDOMThemeFactory 
 * because now it is responsible not only for Exctractor creation but
 * also for Optimizer and Renderer 
 */
public class DefaultStyledDOMThemeExtractorFactory
        extends StyledDOMThemeExtractorFactory {

    public StyledDOMThemeExtractor createExtractor(
            final ExtractorConfiguration configuration,
            final ExtractorContext context) {

        StyledDOMOptimizer optimizer = createStyledDOMOptimizer(configuration,
                context);

        // Create the generator.
        DOMThemeGenerator generator = new DefaultDOMThemeGenerator();
        RuleBuilderFactory ruleBuilderFactory = new RuleBuilderFactory();

        // Add the type rule extractor, but only if type rules are to be
        // generated.
        if (context.generateTypeRules()) {
            RuleExtractor typeRuleExtractor = new TypeRuleExtractor(
                    ruleBuilderFactory);
            generator.addRuleExtractor(typeRuleExtractor);
        }

        // Add the class rule extractor.
        StylesClassRuleExtractorFactory stylesClassRuleExtractorFactory =
                new StylesClassRuleExtractorFactoryImpl(ruleBuilderFactory);
        RuleExtractor classRuleExtractor = new ClassRuleExtractor(
                stylesClassRuleExtractorFactory,
                OptimalClassNameGenerator.getDefaultInstance());
        generator.addRuleExtractor(classRuleExtractor);

        // Add the rule group extractor.
        RuleExtractor groupRuleExtractor = new GroupRuleExtractor();
        generator.addRuleExtractor(groupRuleExtractor);

        // Create the extractor which uses both the optimizer and the
        // generator and return it.
        return new DefaultStyledDOMThemeExtractor(optimizer, generator);
    }

    private StyledDOMOptimizer createStyledDOMOptimizer(
            final ExtractorConfiguration configuration,
            ExtractorContext context) {

        PropertyDetailsSet detailsSet = configuration.getPropertyDetailsSet();
        ShorthandSet supportedShorthands =
                configuration.getSupportedShorthands();
        InputPropertiesOptimizer propertiesOptimizer
                = new PropertiesOptimizer(detailsSet, context,
                        supportedShorthands);

        return new DefaultStyledDOMOptimizer(propertiesOptimizer,
                detailsSet.getRootStyleValues(),
                configuration.getDeviceStyleSheet(),
                context);
    }

    public StyledDocumentOptimizer createOptimizer(
            ExtractorConfiguration configuration,
            ExtractorContext context) {

        StyledDOMOptimizer styledDOMOptimizer = createStyledDOMOptimizer(
               configuration, context);

        return new StyledDocumentOptimizerImpl(styledDOMOptimizer);
    }

    public ExtractorConfigurationBuilder createConfigurationBuilder() {
        return new ExtractorConfigurationBuilderImpl();
    }

    // javadoc inherited
    public StyledDOMStyleAttributeRenderer createRenderer(
            final ExtractorConfiguration configuration,
            final ExtractorContext context) {
        
        OutputStyledElementIteratee rewriter = 
            new DOMStyleAttributeRewriter();
        
        StyledDOMOptimizer optimizer = createStyledDOMOptimizer(
                configuration, context);

        return new DefaultStyledDOMStyleAttributeRenderer(rewriter,optimizer);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10527/3	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 02-Dec-05	10512/2	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (4)

 29-Nov-05	10370/2	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (4)

 18-Nov-05	10370/1	geoff	VBM:2005111405 interim commit

 30-Sep-05	9676/1	geoff	VBM:2005080505 Support the display property correctly.

 01-Sep-05	9407/1	pduffin	VBM:2005083007 Changed MIB2_1 and Netfront3 configuration to remove device specific theme, and replaced it with a new initial value finder that is device aware

 18-Jul-05	8668/6	geoff	VBM:2005060302 XDIMECP: Generate optimised CSS for a DOM.

 ===========================================================================
*/
