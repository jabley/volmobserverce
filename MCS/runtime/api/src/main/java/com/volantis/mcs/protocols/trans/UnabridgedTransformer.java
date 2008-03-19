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
 * $Header: /src/voyager/com/volantis/mcs/protocols/trans/UnabridgedTransformer.java,v 1.7 2003/04/28 09:53:20 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 24-Sep-02    Phil W-S        VBM:2002091901 - Created.
 * 28-Oct-02    Phil W-S        VBM:2002100201 - Add logging (if debug enabled)
 *                              of the DOM to be transformed.
 * 01-Nov-02    Byron           VBM:2002103106 - Modified transform to call
 *                              XHTMLBasic's transform after this transform.
 *                              Added additional logging information.
 * 05-Nov-02    Byron           VBM:2002103106 - Changes should have been in
 *                              XHTMLBasicUnabridgedTransformer. Modifications
 *                              reverted and moved to appropriate class.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug 
 *                              statements in if(logger.isDebugEnabled()) block
 * 24-Apr-03    Allan           VBM:2003042401 - Calls to logDOM now use the 
 *                              VisitorBasedDOMTransformer version. logDOM() 
 *                              member removed. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.trans;

import com.volantis.mcs.dom.Document;
import com.volantis.mcs.protocols.AbstractVisitorBasedDOMTransformer;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.DOMTransformer;

/**
 * This abstract DOMTransformer specialization provides all required
 * processing to utilize the trans package transformation functionality.
 */
public abstract class UnabridgedTransformer implements DOMTransformer {

    /**
     * {@link TransFactory} instance that should be used by this transformer.
     */
    private TransFactory transFactory;

    /**
     * Initialize a new instance using the given parameters.
     *
     * @param factory   with which to initialise the transformer
     */
    protected UnabridgedTransformer(TransFactory factory) {
        if (factory == null) {
            throw new IllegalStateException(
                    "The TransFactory should never be null. " +
                    "The UnabridgedTransformer implementations " +
                    "should initialise the correct TransFactory. ");
        }
        this.transFactory = factory;
    }

    // javadoc inherited
    public Document transform(DOMProtocol protocol, Document document) {

        //@todo why is this commmented out?
//        transFactory.setTransformationConfiguration(
//                protocol.getProtocolConfiguration());
        TransVisitor visitor = transFactory.getVisitor(protocol);

        // Output the incoming DOM if debug for this class has been
        // requested via Log4J configuration
        AbstractVisitorBasedDOMTransformer.logDOM(protocol, document,
                "Incoming DOM to transform");

        // Transformation is a two-phase operation (at least at the visitor
        // level).
        visitor.preprocess(document);
        visitor.process();

//        transFactory.release(visitor);

        return document;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Sep-05	9637/3	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 19-Aug-05	9289/4	emma	VBM:2005081602 Refactoring UnabridgedTransformers so they're not singletons

 17-Aug-05	9289/1	emma	VBM:2005081602 Refactoring TransMapper and TransFactory so that they're not singletons

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/1	doug	VBM:2004111702 Refactored Logging framework

 17-Feb-04	2974/1	steve	VBM:2004020608 SGML Quote handling

 ===========================================================================
*/
