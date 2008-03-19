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

import com.volantis.styling.PseudoStyleEntities;
import com.volantis.styling.StylingFactory;
import com.volantis.styling.compiler.CompilerConfiguration;
import com.volantis.styling.compiler.FunctionResolver;
import com.volantis.styling.compiler.FunctionResolverBuilder;
import com.volantis.styling.compiler.Source;
import com.volantis.styling.compiler.SpecificityCalculator;
import com.volantis.styling.impl.expressions.CompositeFunctionResolver;
import com.volantis.styling.impl.functions.AttrFunction;
import com.volantis.styling.impl.functions.CounterFunction;
import com.volantis.styling.impl.functions.CountersFunction;

import java.util.ArrayList;
import java.util.List;

/**
 * Base implementation of {@link CompilerConfiguration}.
 */
public class CompilerConfigurationImpl
        implements CompilerConfiguration {

    /**
     * The source of the styles to be compiled.
     */
    private Source source;

    /**
     * The set of supported pseudo entities.
     */
    private PseudoStyleEntities supportedPseudoEntities;

    /**
     * The object to use to calculate the specificity of the selectors.
     */
    private SpecificityCalculator specificityCalculator;

    /**
     * The function resolvers.
     */
    private List functionResolvers;

    public CompilerConfigurationImpl() {
        specificityCalculator = new SpecificityCalculatorImpl();
        functionResolvers = new ArrayList();
        initialiseFunctionResolvers();
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public Source getSource() {
        return source;
    }

    public PseudoStyleEntities getSupportedPseudoEntities() {
        return supportedPseudoEntities;
    }

    public void setSupportedPseudoEntities(PseudoStyleEntities supportedPseudoEntities) {
        this.supportedPseudoEntities = supportedPseudoEntities;
    }

    public SpecificityCalculator getSpecificityCalculator() {
        return specificityCalculator;
    }

    public void setSpecificityCalculator(
            SpecificityCalculator specificityCalculator) {
        this.specificityCalculator = specificityCalculator;
    }

    // Javadoc inherited.
    public void addFunctionResolver(FunctionResolver resolver) {
        functionResolvers.add(resolver);
    }

    /**
     * Get a resolver that encapsulates all the function resolvers added to
     * this configuration.
     *
     * @return A function resolver.
     */
    public FunctionResolver getResolver() {
        return new CompositeFunctionResolver(functionResolvers);
    }

    /**
     * Initialise the function resolvers with the default set.
     */
    private void initialiseFunctionResolvers() {
        FunctionResolverBuilder builder =
                StylingFactory.getDefaultInstance().
                createFunctionResolverBuilder();
        builder.addFunction("attr", new AttrFunction());
        builder.addFunction("counter", new CounterFunction());
        builder.addFunction("counters", new CountersFunction());
        functionResolvers.add(builder.getResolver());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Sep-05	9635/1	adrianj	VBM:2005092817 Counter functions for CSS

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 18-Jul-05	9029/2	pduffin	VBM:2005071301 Adding ability for styling engine to support nested style sheets

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
