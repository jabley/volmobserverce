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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.html;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.trans.AbridgedTransMapper;
import com.volantis.mcs.protocols.trans.TransFactory;
import com.volantis.mcs.protocols.trans.TransTable;
import com.volantis.mcs.protocols.trans.TransVisitor;
import com.volantis.mcs.protocols.trans.TransformationConfiguration;
import com.volantis.mcs.protocols.trans.UnabridgedTransformer;

/**
 * An UnabridgedTransformer for HTML iMode. This transformer will remove
 * nested tables and single column tables that have no stylistic attributes.
 */
public class HTML_iModeUnabridgedTransformer extends UnabridgedTransformer {

    /**
     * Initialize a new instance using the given parameters.
     *
     * @param configuration protocol specific configuration used when
     *                      transforming
     */
    public HTML_iModeUnabridgedTransformer(
            TransformationConfiguration configuration) {
        super(new HTML_iModeTransFactory(configuration));
    }

    /**
     * Initialize a new instance using the given parameters.
     *
     * @param factory with which to initialise the transformer.
     */
    public HTML_iModeUnabridgedTransformer(HTML_iModeTransFactory factory) {
        super(factory);
    }

    /**
     * The {@link TransFactory} for this transformer.
     */
    protected static class HTML_iModeTransFactory
            extends XHTMLBasicTransFactory {

        /**
         * Initialize a new instance using the given parameters.
         *
         * @param configuration protocol specific configuration to use when
         *                      transforming
         */
        public HTML_iModeTransFactory(TransformationConfiguration configuration) {
            super(configuration);
        }

        // javadoc inherited
        public AbridgedTransMapper getNestedDisabledMapper() {
            return new HTML_iModeTransMapper(transformationConfiguration);
        }

        // javadoc inherited
        public TransVisitor getVisitor(DOMProtocol protocol) {
            XHTMLBasicTransVisitor visitor =
                    new XHTMLBasicTransVisitor(protocol, this) {
                        // javadoc inherited
                        protected String[] getPromotePreserveStyleAttributes() {
                            final String[] attributes =
                                    {"align", "bgcolor", "border",
                                     "cellpadding", "cellspacing",
                                     "width"};

                            return attributes;
                        }
                    };

            return visitor;
        }

        // javadoc inherited
        public TransTable getTable(Element table, DOMProtocol protocol) {

            TransTable trans = new XHTMLBasicTransTable(table, protocol) {
                        // javadoc inherited
                        protected String[] getPreserveStyleAttributes() {
                            // The class attribute is handled by the superclass
                            // so is not included here. colspan, height,
                            // rowspan and width are explicitly not included
                            // since it is not meaningful to do so.
                            String[] attributes =
                                    {"abbr", "align", "axis", "bgcolor",
                                     "headers" /* TODO keep? */,
                                     "nowrap", "valign"};

                            return attributes;
                        }
                    };

            trans.setFactory(this);

            return trans;
        }

        public RemoveTableRule getRemoveTableRule() {
            // For now we share the same remove table rule as the other
            // XHTMLFull protocols. If they turn out to be different we can
            // always have different specialisations.
            return new XHTMLFullRemoveTableRule(this);
        }
    }


}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 19-Aug-05	9289/4	emma	VBM:2005081602 Refactoring UnabridgedTransformers so they're not singletons

 17-Aug-05	9289/1	emma	VBM:2005081602 Refactoring TransMapper and TransFactory so that they're not singletons

 02-Jun-05	8005/4	pduffin	VBM:2005050404 Moved dom to its own subsystem

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 01-Jun-05	8606/1	pcameron	VBM:2005052409 Fixed align attribute for table rendering in iMode

 01-Jun-05	8604/4	pcameron	VBM:2005052409 Fixed align attribute for table rendering in iMode

 11-May-05	8167/4	allan	VBM:2005040701 Rework javadoc issues

 11-May-05	8167/1	allan	VBM:2005040701 Prevent iMode table optimization when td has style

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 11-Oct-04	5773/7	tom	VBM:2004093007 created an i-mode transmapper to resolve align disappearance problem after table optimisation

 13-Oct-03	1540/1	allan	VBM:2003101101 Fix pane styles and single column table removal

 ===========================================================================
*/
