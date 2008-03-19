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

package com.volantis.mcs.dom2theme.integration.renderer;

import com.volantis.mcs.css.renderer.shorthand.BorderRenderer;
import com.volantis.mcs.dom2theme.impl.normalizer.BorderNormalizer;
import com.volantis.mcs.dom2theme.impl.normalizer.PropertiesNormalizer;
import com.volantis.mcs.dom2theme.impl.optimizer.ShorthandOptimizer;
import com.volantis.mcs.dom2theme.impl.optimizer.border.BorderOptimizer;
import com.volantis.mcs.dom2theme.integration.optimizer.TestPropertyClearerChecker;
import com.volantis.mcs.themes.MutableShorthandSet;
import com.volantis.mcs.themes.PropertyGroups;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleShorthands;
import com.volantis.styling.properties.ImmutableStylePropertySet;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import junit.framework.Test;

public class BorderRendererTestCase
        extends TestCaseAbstract {

    protected static final ImmutableStylePropertySet ALL_PROPERTIES =
            StylePropertyDetails.getDefinitions()
            .getStandardDetailsSet()
            .getSupportedProperties();

    private static final String[] EDGE_NAMES =
            new String[PropertyGroups.BORDER_WIDTH_PROPERTIES.length];

    static {
        EDGE_NAMES[PropertyGroups.TOP] = "top";
        EDGE_NAMES[PropertyGroups.RIGHT] = "right";
        EDGE_NAMES[PropertyGroups.BOTTOM] = "bottom";
        EDGE_NAMES[PropertyGroups.LEFT] = "left";
    }

    public static Test suite() {

        final MutableShorthandSet allShorthands = new MutableShorthandSet();

        allShorthands.add(StyleShorthands.BORDER);
        allShorthands.add(StyleShorthands.BORDER_BOTTOM);
        allShorthands.add(StyleShorthands.BORDER_COLOR);
        allShorthands.add(StyleShorthands.BORDER_LEFT);
        allShorthands.add(StyleShorthands.BORDER_RIGHT);
        allShorthands.add(StyleShorthands.BORDER_STYLE);
        allShorthands.add(StyleShorthands.BORDER_TOP);
        allShorthands.add(StyleShorthands.BORDER_WIDTH);

        final MutableShorthandSet noShorthands = new MutableShorthandSet();

        TestPropertyClearerChecker checker = new TestPropertyClearerChecker();

        PropertiesNormalizer normalizer = new BorderNormalizer(ALL_PROPERTIES);

        ShorthandOptimizer allShorthandsOptimizer =
                new BorderOptimizer(checker, allShorthands);

        ShorthandOptimizer noShorthandOptimizer =
                new BorderOptimizer(checker, noShorthands);

        ShorthandRendererTestSuiteBuilder builder =
                new ShorthandRendererTestSuiteBuilder(normalizer,
                        new BorderRenderer(),
                        allShorthandsOptimizer,
                        noShorthandOptimizer,
                        checker);

//        builder.addWithAndWithout("border:none;", "border-top-style:none;border-right-style:none;border-bottom-style:none;border-left-style:none;");
        builder.addWithAndWithout("border:none;", "", "");

        builder.addWithAndWithout("border-color:red;",
                "border-top-color:red;border-right-color:red;border-bottom-color:red;border-left-color:red;");

        builder.addWithAndWithout("border-style:dotted;",
                "border-top-style:dotted;border-right-style:dotted;border-bottom-style:dotted;border-left-style:dotted;");

        builder.addWithAndWithout("border-width:10px;",
                "border-top-width:10px;border-right-width:10px;border-bottom-width:10px;border-left-width:10px;");

//        builder.addWithAndWithout("border-color:red;border-style:none;border-width:10px;",
//                "border:none;",
//                "border-top-style:none;border-right-style:none;border-bottom-style:none;border-left-style:none;");

        builder.addWithAndWithout("border-color:red;border-style:none;border-width:10px;",
                "", "");

        builder.addWithAndWithout("border-color:red;border-style:solid;border-width:0;",
                "border:0;",
                "border-top-width:0;border-right-width:0;border-bottom-width:0;border-left-width:0;");

//        builder.addTest("border-top-width:10px;", "border-top-width:10px;",
//                allShorthands);
//                noShorthands);
//
//        builder.addTest("border-top-color:red;", "border-top-color:red;",
//                allShorthands);
//        builder.addTest("border-color:red;", "border-color:red;",
//                allShorthands);
//        builder.addTest("border-color:red",
//                "border-top-color:red;border-right-color:red;border-bottom-color:red;border-left-color:red;",
//                noShorthands);
//
//        builder.addTest("border-top-style:dotted;", "border-top-style:dotted;",
//                allShorthands);
//        builder.addTest("border-style:dotted;", "border-style:dotted;",
//                allShorthands);
//        builder.addTest("border-style:dotted",
//                "border-top-style:dotted;border-right-style:dotted;border-bottom-style:dotted;border-left-style:dotted;",
//                noShorthands);
//
//        builder.addTest(
//                "border-right-width:10px;border-bottom-width:10px;border-left-width:10px;",
//                "border-right-width:10px;border-bottom-width:10px;border-left-width:10px;",
//                allShorthands);
//        builder.addTest(
//                "border-right-color:red;border-bottom-color:red;border-left-color:red;",
//                "border-right-color:red;border-bottom-color:red;border-left-color:red;",
//                allShorthands);
//        builder.addTest(
//                "border-right-style:dotted;border-bottom-style:dotted;border-left-style:dotted;",
//                "border-right-style:dotted;border-bottom-style:dotted;border-left-style:dotted;",
//                allShorthands);
//        builder.addTest(
//                "border-top-width:10px;border-right-width:11px;border-bottom-width:12px;border-left-width:13px;",
//                "border-width:10px 11px 12px 13px;", allShorthands);
//        builder.addTest(
//                "border-top-color:red;border-right-color:green;border-bottom-color:blue;border-left-color:white;",
//                "border-color:red green blue white;", allShorthands);
//        builder.addTest(
//                "border-top-style:none;border-right-style:hidden;border-bottom-style:dotted;border-left-style:dashed;",
//                "border-style:none hidden dotted dashed;", allShorthands);
//        builder.addTest("border:10px red dotted;", "border:red dotted 10px;",
//                allShorthands);
//        builder.addTest("border-width:10px;border-color:red;",
//                "border-color:red;border-width:10px;", allShorthands);
//        builder.addTest("border-width:10px;border-style:dotted;",
//                "border-style:dotted;border-width:10px;", allShorthands);
//        builder.addTest("border-color:red;border-style:dotted;",
//                "border-color:red;border-style:dotted;", allShorthands);
//
//        builder.addTest("border: thin red solid",
//                "border-top-color:red;" +
//                "border-right-color:red;" +
//                "border-bottom-color:red;" +
//                "border-left-color:red;" +
//                "border-top-style:solid;" +
//                "border-right-style:solid;" +
//                "border-bottom-style:solid;" +
//                "border-left-style:solid;" +
//                "border-top-width:thin;" +
//                "border-right-width:thin;" +
//                "border-bottom-width:thin;" +
//                "border-left-width:thin;",
//                noShorthands);
//
        for (int i = 0; i < EDGE_NAMES.length; i++) {
            String edgeName = EDGE_NAMES[i];

            builder.addWithAndWithout("border-" + edgeName + ":red solid thin;",
                    "border-" + edgeName + "-color:red;" +
                    "border-" + edgeName + "-style:solid;" +
                    "border-" + edgeName + "-width:thin;");

            builder.addSameWithAndWithout("border-" + edgeName + "-color:red;");
            builder.addSameWithAndWithout("border-" + edgeName + "-style:dotted;");
            builder.addSameWithAndWithout("border-" + edgeName + "-width:10px;");
        }

        builder.addWithAndWithout("border-width:1px 0 0 1px;" +
                "border-top-color:#fff;" +
                "border-left-color:#fff;" +
                "border-style:solid none none solid;",

                "border-top:#fff solid 1px;border-left:#fff solid 1px;",

                "border-top-color:#fff;border-left-color:#fff;border-top-style:solid;border-left-style:solid;border-top-width:1px;border-left-width:1px;");

        builder.addTest("border-width:0 0 1px;border-bottom-style:solid;",
                "border-width:0 0 1px;border-bottom-style:solid;");

        builder.addTest("border-width:0 0 1px;border-style:none none solid;",
                "border-style:none none solid;border-width:1px;");

        builder.addTest("border-width:1px 0 0;border-style:solid none none;",
                "border-top-style:solid;border-top-width:1px;");

        builder.addTest("border-width:0 0 0 1px;" +
                "border-style:none none none solid;",
                "border-left-style:solid;border-left-width:1px;");

        return builder.getSuite();
    }
}
