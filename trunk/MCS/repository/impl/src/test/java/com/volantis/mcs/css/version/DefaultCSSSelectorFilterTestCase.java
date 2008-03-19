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
package com.volantis.mcs.css.version;

import com.volantis.mcs.themes.IdSelector;
import com.volantis.mcs.themes.PseudoElementSelector;
import com.volantis.mcs.themes.PseudoElementTypeEnum;
import com.volantis.mcs.themes.SelectorSequence;
import com.volantis.mcs.themes.StyleSheetFactory;
import com.volantis.synergetics.testtools.TestCaseAbstract;

public class DefaultCSSSelectorFilterTestCase extends TestCaseAbstract {

    public void testPseudoSelectorId() {
        DefaultCSSVersion cssVersion = new DefaultCSSVersion("test");
        cssVersion.addPseudoSelectorId(new String[] {
            PseudoElementTypeEnum.FIRST_LINE.getType()
        });
        cssVersion.markImmutable();

        DefaultCSSSelectorFilter selectorFilter =
                new DefaultCSSSelectorFilter(cssVersion);

        StyleSheetFactory factory = StyleSheetFactory.getDefaultInstance();

        {
            // Test a selector which is never filtered.
            IdSelector selector = factory.createIdSelector("monkey");
            assertEquals("Never filtered",
                         selector, selectorFilter.filter(selector));
        }

        {
            // Test a pseudo selector which is not supported in the css.
            PseudoElementSelector selector =
                    factory.createPseudoElementSelector(
                            PseudoElementTypeEnum.FIRST_LETTER.getType());
            assertNull("Not supported", selectorFilter.filter(selector));
        }

        {
            // Test a pseudo selector which is supported in the css.
            PseudoElementSelector selector =
                    factory.createPseudoElementSelector(
                            PseudoElementTypeEnum.FIRST_LINE.getType());
            assertEquals("Supported", selector, selectorFilter.filter(selector));
        }

        {
            // Test a compound selector which contains a filterable pseudo
            // selector
            PseudoElementSelector pseudoSelector =
                    factory.createPseudoElementSelector(
                            PseudoElementTypeEnum.FIRST_LETTER.getType());
            SelectorSequence selector =
                    factory.createSelectorSequence();

            selector.addSelector(pseudoSelector);

            assertNull("Compound filterable", selectorFilter.filter(selector));
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-05	10374/1	emma	VBM:2005111705 Interim commit

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 05-Sep-05	9407/8	pduffin	VBM:2005083007 Removed old themes model

 02-Sep-05	9407/6	pduffin	VBM:2005083007 Committing fixes to default CSS selector filter

 01-Sep-05	9412/1	adrianj	VBM:2005083007 CSS renderer using new model

 09-Dec-04	6417/1	philws	VBM:2004120703 Committing tidy up

 19-Nov-04	5733/3	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 ===========================================================================
*/
