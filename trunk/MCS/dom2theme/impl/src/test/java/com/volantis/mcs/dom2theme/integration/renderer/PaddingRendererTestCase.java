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

import com.volantis.mcs.dom2theme.impl.optimizer.EdgeShorthandOptimizer;
import com.volantis.mcs.dom2theme.impl.optimizer.ShorthandOptimizer;
import com.volantis.mcs.dom2theme.integration.optimizer.TestPropertyClearerChecker;
import com.volantis.mcs.css.renderer.shorthand.ShorthandPropertyRenderer;
import com.volantis.mcs.themes.MutableShorthandSet;
import com.volantis.mcs.themes.StyleShorthand;
import com.volantis.mcs.themes.StyleShorthands;
import com.volantis.mcs.dom2theme.integration.optimizer.TestPropertyClearerChecker;
import com.volantis.styling.values.InitialValueFinder;
import junit.framework.Test;

public class PaddingRendererTestCase {

    public static Test suite() {


        MutableShorthandSet supportedShorthands = new MutableShorthandSet();
        supportedShorthands.add(StyleShorthands.PADDING);

        final TestPropertyClearerChecker checker =
                new TestPropertyClearerChecker();

        ShorthandOptimizer allShorthands =
                new EdgeShorthandOptimizer(StyleShorthands.PADDING,
                        checker, supportedShorthands);

        ShorthandOptimizer noShorthands =
                new EdgeShorthandOptimizer(StyleShorthands.PADDING,
                        checker, new MutableShorthandSet());

        ShorthandRendererTestSuiteBuilder builder =
                new ShorthandRendererTestSuiteBuilder(
                        null, new ShorthandPropertyRenderer(StyleShorthands.PADDING),
                        allShorthands, noShorthands, checker);

        builder.addSameWithAndWithout("padding-top:1%;");
        builder.addSameWithAndWithout("padding-right:2%;");
        builder.addSameWithAndWithout("padding-top:1%;padding-right:2%;");
        builder.addSameWithAndWithout("padding-bottom:3%;");
        builder.addSameWithAndWithout("padding-top:1%;padding-bottom:3%;");
        builder.addSameWithAndWithout("padding-right:2%;padding-bottom:3%;");
        builder.addSameWithAndWithout("padding-top:1%;padding-right:2%;padding-bottom:3%;");
        builder.addSameWithAndWithout("padding-left:4%;");
        builder.addSameWithAndWithout("padding-top:1%;padding-left:4%;");
        builder.addSameWithAndWithout("padding-right:2%;padding-left:4%;");
        builder.addSameWithAndWithout("padding-top:1%;padding-right:2%;padding-left:4%;");
        builder.addSameWithAndWithout("padding-bottom:3%;padding-left:4%;");
        builder.addSameWithAndWithout("padding-top:1%;padding-bottom:3%;padding-left:4%;");
        builder.addSameWithAndWithout("padding-right:2%;padding-bottom:3%;padding-left:4%;");

        builder.addWithAndWithout("padding:10% 10% 5% 5%;", "padding-top:10%;padding-right:10%;padding-bottom:5%;padding-left:5%;");
        builder.addWithAndWithout("padding:10% 5%;", "padding-top:10%;padding-right:5%;padding-bottom:10%;padding-left:5%;");
        builder.addWithAndWithout("padding:5% 10% 10% 5%;", "padding-top:5%;padding-right:10%;padding-bottom:10%;padding-left:5%;");
        builder.addWithAndWithout("padding:10% 10% 5% 6%;", "padding-top:10%;padding-right:10%;padding-bottom:5%;padding-left:6%;");
        builder.addWithAndWithout("padding:10% 5% 10% 6%;", "padding-top:10%;padding-right:5%;padding-bottom:10%;padding-left:6%;");
        builder.addWithAndWithout("padding:10% 5% 6% 10%;", "padding-top:10%;padding-right:5%;padding-bottom:6%;padding-left:10%;");
        builder.addWithAndWithout("padding:5% 6% 10% 10%;", "padding-top:5%;padding-right:6%;padding-bottom:10%;padding-left:10%;");
        builder.addWithAndWithout("padding:5% 10% 6%;", "padding-top:5%;padding-right:10%;padding-bottom:6%;padding-left:10%;");
        builder.addWithAndWithout("padding:5% 10% 10% 6%;", "padding-top:5%;padding-right:10%;padding-bottom:10%;padding-left:6%;");
        builder.addWithAndWithout("padding:10% 10% 10% 5%;", "padding-top:10%;padding-right:10%;padding-bottom:10%;padding-left:5%;");
        builder.addWithAndWithout("padding:10% 10% 5%;", "padding-top:10%;padding-right:10%;padding-bottom:5%;padding-left:10%;");
        builder.addWithAndWithout("padding:10% 5% 10% 10%;", "padding-top:10%;padding-right:5%;padding-bottom:10%;padding-left:10%;");
        builder.addWithAndWithout("padding:5% 10% 10%;", "padding-top:5%;padding-right:10%;padding-bottom:10%;padding-left:10%;");
        builder.addWithAndWithout("padding:10%;", "padding-top:10%;padding-right:10%;padding-bottom:10%;padding-left:10%;");

        return builder.getSuite();
    }
}
