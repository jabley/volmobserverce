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
 * $Header: /src/voyager/com/volantis/mcs/protocols/html/XHTMLBasicUnabridgedTransformer.java,v 1.4 2003/04/28 09:53:20 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 24-Sep-02    Phil W-S        VBM:2002091901 - Created.
 * 05-Nov-02    Byron           VBM:2002103106 - Modified transform to call
 *                              XHTMLBasic's transform after this transform.
 *                              Added additional logging information.
 * 24-Apr-03    Allan           VBM:2003042401 - Modified transform() to use 
 *                              the VisitorBasedDOMTransformer logDOM method. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.html;

import com.volantis.mcs.dom.Document;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.AbstractVisitorBasedDOMTransformer;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.DOMTransformer;
import com.volantis.mcs.protocols.trans.TransFactory;
import com.volantis.mcs.protocols.trans.TransformationConfiguration;
import com.volantis.mcs.protocols.trans.UnabridgedTransformer;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * This is the transformation algorithm used to perform complete nested table
 * removal for XHTMLBasic. This algorithm retains as much of the original
 * layout and stylistic content as possible. This follows the singleton design
 * pattern.
 * 
 * Known issues/limitations are:
 * <ol>
 * <li>tables nested within forms within tables have to be remapped away (to
 * optimized div tags) in case the nested table contains inputs that must
 * themselves be nested within the form</li>
 * <li>tables generated as portlet content will not have stylistic rules
 * applied correctly if the portal's layout is defined using a table. This is
 * because the portlet is generated within a div with an ID set for it where
 * the ID is used as part of the theme stylesheet generation. This allows
 * portlets to use different themes from the portal that contains it. Because
 * the table must be promoted out of the div, the ID information is lost. The
 * ID cannot be duplicated onto the table's content during merging as IDs must
 * be unique within a page.</li>
 * <li>descendent and child selector rules may no longer work. This is because
 * the parent/child relationships may be dramatically altered as part of the
 * transformation.</li>
 * </ol>
 */
public class XHTMLBasicUnabridgedTransformer extends UnabridgedTransformer {
    
    /**
     * Used for logging
     */
    private static final LogDispatcher logger = LocalizationFactory.
            createLogger(XHTMLBasicUnabridgedTransformer.class);

    /**
     * The visitor based transformer which is used by this unabridged
     * transformer.
     */
    private final DOMTransformer visitorBasedTransformer =
            new XHTMLBasicTransformer();

    /**
     * Initialize a new instance using the given parameters.
     *
     * @param configuration protocol specific configuration used when
     *                      transforming
     */
    public XHTMLBasicUnabridgedTransformer(
            TransformationConfiguration configuration) {
        super(new XHTMLBasicTransFactory(configuration));
    }

    protected XHTMLBasicUnabridgedTransformer(TransFactory factory) {
        super(factory);
    }

    // javadoc inherited
    public Document transform (DOMProtocol protocol,
                               Document document) {

        document = super.transform(protocol, document);

        if (logger.isDebugEnabled()) {
            logger.debug("Finished 1st transformation pass, " +
                         "starting 2nd pass...");
            AbstractVisitorBasedDOMTransformer.logDOM(
                        protocol,
                   document,
                   "Outgoing DOM");
        }

        visitorBasedTransformer.transform(protocol, document);

        if (logger.isDebugEnabled()) {
            AbstractVisitorBasedDOMTransformer.logDOM(
                        protocol,
                   document,
                   "After 2nd transformation pass");
        }
        return document;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Dec-05	10799/1	geoff	VBM:2005081506 Port 2005071314 forward to MCS

 30-Sep-05	9637/3	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 19-Aug-05	9289/4	emma	VBM:2005081602 Refactoring UnabridgedTransformers so they're not singletons

 17-Aug-05	9289/1	emma	VBM:2005081602 Refactoring TransMapper and TransFactory so that they're not singletons

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 ===========================================================================
*/
