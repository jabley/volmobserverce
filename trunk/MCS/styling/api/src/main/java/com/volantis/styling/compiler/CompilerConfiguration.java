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

package com.volantis.styling.compiler;

import com.volantis.styling.PseudoStyleEntities;

/**
 * Configuration for the {@link StyleSheetCompiler}.
 *
 * @mock.generate
 */
public interface CompilerConfiguration {

    /**
     * Set the source of the styles to be compiled by this compiler.
     *
     * @param source source of the styles to be compiled.
     */
    void setSource(Source source);

    /**
     * Get the source of the styles to be compiled by this compiler.
     *
     * @return the source of the styles to be compiled.
     */
    Source getSource();

    /**
     * Get the pseudo style entities that should be supported by the builder.
     *
     * @return The pseudo style entities supported by the builder.
     */
    PseudoStyleEntities getSupportedPseudoEntities();

    /**
     * Set the pseudo style entities that should be supported by the builder.
     *
     * @param entities The pseudo style entities supported by the builder.
     */
    void setSupportedPseudoEntities(PseudoStyleEntities entities);

    SpecificityCalculator getSpecificityCalculator();

    void setSpecificityCalculator(
            SpecificityCalculator specificityCalculator);

    /**
     * Add a function resolver to the list of resolvers.
     *
     * <p>When a function is being compiled the resolvers are tried in the
     * order that they were added until one of them returns a function. If
     * none of them do then it is an error.</p>
     *
     * @param resolver The function resolver.
     */
    void addFunctionResolver(FunctionResolver resolver);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 18-Aug-05	9007/3	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 01-Aug-05	9110/1	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 18-Jul-05	9029/2	pduffin	VBM:2005071301 Adding ability for styling engine to support nested style sheets

 15-Jul-05	9067/3	geoff	VBM:2005071415 More refactoring for: XDIMECP: Generate optimised CSS for a DOM.

 08-Jun-05	7997/3	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 02-Jun-05	7997/1	pduffin	VBM:2005050324 Added styling API

 ===========================================================================
*/
