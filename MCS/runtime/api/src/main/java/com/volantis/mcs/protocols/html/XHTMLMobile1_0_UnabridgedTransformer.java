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
package com.volantis.mcs.protocols.html;

import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.trans.TransTable;
import com.volantis.mcs.protocols.trans.TransVisitor;
import com.volantis.mcs.protocols.trans.TransformationConfiguration;

/**
 */
public class XHTMLMobile1_0_UnabridgedTransformer
        extends XHTMLBasicUnabridgedTransformer {

    /**
     * Initialize a new instance using the given parameters.
     *
     * @param configuration protocol specific configuration used when
     *                      transforming
     */
    public XHTMLMobile1_0_UnabridgedTransformer(
            TransformationConfiguration configuration) {
        super(new XHTMLMobile1_0_TransFactory(configuration));
    }

    /**
     * The TransFactoy for this transformer.
     */
    private static class XHTMLMobile1_0_TransFactory
            extends XHTMLBasicTransFactory {

        /**
         * Initialize a new instance using the given parameters.
         *
         * @param configuration protocol specific configuration to use when
         * transforming
         */
        public XHTMLMobile1_0_TransFactory(
                TransformationConfiguration configuration) {
            super(configuration);
        }

        // javadoc inherited.
        public TransVisitor getVisitor(DOMProtocol protocol) {
            TransVisitor visitor = new XHTMLBasicTransVisitor(protocol, this) {
                // javadoc inherited
                public void preprocess(Document document) {
                    super.preprocess(document);

                    InternalDevice device =
                            this.protocol.getMarinerPageContext().getDevice();
                    TableWidthTransformer tableWidthProcess =
                            new TableWidthTransformer(device);
                    int useableWidth = device.getPixelsX();
                    for (int i=0; i<tables.size(); i++) {
                        TransTable table = (TransTable) tables.get(i);
                        tableWidthProcess.transformPercentagesToPixels(
                                table, useableWidth);
                    }
                }
            };

            return visitor;
        }

    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Dec-05	10799/2	geoff	VBM:2005081506 Port 2005071314 forward to MCS

 ===========================================================================
*/
