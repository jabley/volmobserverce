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
package com.volantis.mcs.protocols.trans;

import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.DOMVisitorBasedTransformer;
import com.volantis.mcs.protocols.TransformingVisitor;

/**
 * This transformer will ensure that: <ol>
 *
 * <li>Containment rules are enforced. For example, a bold element may not
 * contain a paragraph element (the bold element should be pushed down into the
 * paragraph element.</li>
 *
 * <li>Redundant style information is removed and styles inverted</li>
 *
 * <li>Indivisible elements are preserved and styles pushed in appropriately</li>
 * </ol>
 *
 * <u>To illustrate 1 above:</u>
 * <pre>
 * &lt;body&gt;
 *   &lt;b&gt;
 *     &lt;p&lt;Text 1&lt;/p&gt; &lt;-- p should not contain b --&gt;
 *     Text 2
 *   &lt;/b&gt;
 * &lt;/body&gt;
 * </pre>
 *
 * should be transformed to:
 *
 * <pre>
 * &lt;body&gt;
 *   &lt;p&gt;
 *     &lt;b&gt;Text 1&lt;/b&gt;
 *   &lt;/p&gt;
 *   &lt;b&gt;Text 2&lt;/b&gt;
 * &lt;/body&gt;
 * </pre>
 *
 * <u>To illustrate 2 above:</u>
 *
 * <pre>
 * <font color="blue">&lt;b&gt;</font>
 *   This is some
 *     <font color="blue">&lt;anti-b&gt;</font>  &lt;!-- promoted up to counterpart and removed --&gt;
 *       <font color="red">&lt;anti-strike&gt;</font>  &lt;!-- redundant --&gt;
 *          text
 *       <font color="red">&lt;anti-strike&gt;</font>
 *     <font color="blue">&lt;/anti-b&gt;</font>
 *     which is mainly, but not all, bold.
 * <font color="blue">&lt;/b&gt;</font >
 * </pre>
 *
 * should be transformed to:
 *
 * <pre>
 * &lt;b&gt;
 *   This is some
 * &lt;/b&gt;
 * text
 * &lt;b&gt;
 *   which is mainly, but not all, bold.
 * &lt;/b&gt;
 * </pre>
 *
 * <u>To illustrate 3 above:</u>
 *
 * <pre>
 * &lt;b&gt;
 *  Block text before
 *  <font color="red">&lt;a href=\"...\"&gt;</font>  &lt;!-- indivisible element --&gt;
 *    Bold link text
 *    &lt;anti-b&gt;
 *      Normal link Text
 *    &lt;/anti-b&gt;
 *    Bold link text
 *  <font color="red">&lt;/a&gt;</font>
 *  Bold text after
 * &lt;/b&gt;
 * </pre>
 *
 * should be transformed to:
 *
 * <pre>
 * &lt;b&gt;
 *   Block text before
 * &lt;/b&gt;
 * <font color="red">&lt;a href=\"...\"&gt;</font>
 *   &lt;b&gt;
 *     Bold link text
 *   &lt;/b&gt;
 *   Normal link Text
 *   &lt;b&gt;
 *     Bold link text
 *   &lt;/b&gt;
 * <font color="red">&lt;/a&gt;</font>
 * &lt;b&gt;
 *   Bold text after
 * &lt;/b&gt;
 * </pre>
 *
 * @see StyleEmulationVisitor
 */
public final class StyleEmulationTransformer
        extends DOMVisitorBasedTransformer {

    /**
     * The style configuration associated with this transformer.
     */
    private final StyleEmulationElementConfiguration styleConfiguration;

    /**
     * Intialize the new instance.
     */
    public StyleEmulationTransformer(
            StyleEmulationElementConfiguration styleConfiguration) {
        this.styleConfiguration = styleConfiguration;
    }

    protected TransformingVisitor getDOMVisitor(DOMProtocol protocol) {
        return new StyleEmulationVisitor(
                protocol.getDOMFactory(), styleConfiguration,
                new StyleEmulationElementTracker());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Aug-05	9184/1	pabbott	VBM:2005080508 Move CSS eumlator to post process step

 10-Aug-05	9211/1	pabbott	VBM:2005080902 End to End CSS emulation test

 26-Jul-05	8923/1	pabbott	VBM:2005063010 Done

 21-Jun-05	8856/1	geoff	VBM:2005062005 Refactoring for XDIMECP: Generate optimised CSS for a DOM.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 26-Oct-04	5877/1	byron	VBM:2004101911 Style Emulation: fix definition and implementation of Atomic Elements

 14-Jul-04	4783/2	geoff	VBM:2004062302 Implementation of theme style options: WML Family (fixes after style inversion approval)

 14-Jul-04	4752/7	byron	VBM:2004062301 Implementation of theme style options: Style Inversion Facilities - addressed rework issues

 13-Jul-04	4752/5	byron	VBM:2004062301 Implementation of theme style options: Style Inversion Facilities

 13-Jul-04	4752/3	byron	VBM:2004062301 Implementation of theme style options: Style Inversion Facilities

 13-Jul-04	4752/1	byron	VBM:2004062301 Implementation of theme style options: Style Inversion Facilities

 ===========================================================================
*/
