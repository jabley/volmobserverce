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
import com.volantis.styling.compiler.SpecificityCalculator;
import com.volantis.styling.impl.engine.matchers.MatcherBuilderContext;

public class MatcherBuilderConfiguration {

    private MatcherFactory factory;

    private PseudoStyleEntities entities;

    private SpecificityCalculator specificityCalculator;

    private MatcherBuilderContext builderContext;

    public PseudoStyleEntities getEntities() {
        return entities;
    }

    public void setEntities(PseudoStyleEntities entities) {
        this.entities = entities;
    }

    public MatcherFactory getFactory() {
        return factory;
    }

    public void setFactory(MatcherFactory factory) {
        this.factory = factory;
    }

    public SpecificityCalculator getSpecificityCalculator() {
        return specificityCalculator;
    }

    public void setSpecificityCalculator(SpecificityCalculator specificityCalculator) {
        this.specificityCalculator = specificityCalculator;
    }

    public MatcherBuilderContext getBuilderContext() {
        return builderContext;
    }

    public void setBuilderContext(MatcherBuilderContext builderContext) {
        this.builderContext = builderContext;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
