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
 * $Header: /src/voyager/com/volantis/mcs/protocols/html/XHTMLBasicTransFactory.java,v 1.5 2003/01/17 12:03:40 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 24-Sep-02    Phil W-S        VBM:2002091901 - Created.
 * 11-Oct-02    Phil W-S        VBM:2002100804 - Changed return class of
 *                              getMapper.
 * 09-Jan-03    Phil W-S        VBM:2003010902 - Update getTable and getCell
 *                              signatures to match interface updates.
 * 17-Jan-03    Phil W-S        VBM:2003010606 - Rework: Refactor management of
 *                              the container validator and use a
 *                              GenericContainerValidator. This involves
 *                              pulling containerValidator and
 *                              initializeContainerValidator up from
 *                              XHTMLFullTransFactory and putting a call
 *                              to initializeContainerValidator in the
 *                              constructor. Also involves putting the
 *                              content from the XHTMLBasicContainerValidator
 *                              initialization into the
 *                              GenericContainerValidator (anonymous)
 *                              specialization's initialize method.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.html;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.trans.AbridgedTransMapper;
import com.volantis.mcs.protocols.trans.ContainerValidator;
import com.volantis.mcs.protocols.trans.ElementHelper;
import com.volantis.mcs.protocols.trans.TransCell;
import com.volantis.mcs.protocols.trans.TransFactory;
import com.volantis.mcs.protocols.trans.TransTable;
import com.volantis.mcs.protocols.trans.TransVisitor;
import com.volantis.mcs.protocols.trans.TransformationConfiguration;

/**
 * The trans factory for the XHTMLBasic unabridged transformer algorithm.
 *
 * @todo move implementations up now that parent is an abstract class.
 */
public class XHTMLBasicTransFactory extends TransFactory {

    /**
     * Initialize a new instance using the given parameters.
     *
     * @param configuration protocol specific configuration to use when
     *                      transforming
     */
    public XHTMLBasicTransFactory(TransformationConfiguration configuration) {
        super(configuration);
    }

    // Javadoc inherited.
    public TransTable getTable(Element table, DOMProtocol protocol) {

        TransTable trans = new XHTMLBasicTransTable(table, protocol);
        trans.setFactory(this);
        return trans;
    }

    // Javadoc inherited.
    public TransCell getCell(Element row, Element cell, int startRow,
            int startCol, DOMProtocol protocol) {
        TransCell trans = new XHTMLBasicTransCell(row, cell, startRow,
                startCol, protocol);
        trans.setFactory(this);

        return trans;
    }

    // Javadoc inherited.
    public TransVisitor getVisitor(DOMProtocol protocol) {

        return new XHTMLBasicTransVisitor(protocol, this);
    }

    protected AbridgedTransMapper getNestedDisabledMapper() {
        return new XHTMLBasicTransMapper(transformationConfiguration);
    }

    // Javadoc inherited.
    public ElementHelper getElementHelper() {
        return XHTMLBasicElementHelper.getInstance();
    }

    // Javadoc inherited.
    protected ContainerValidator getNestedEnabledContainerValidator() {

        return new XHTMLBasicNestedEnabledContainerValidator();
    }

    // Javadoc inherited.
    protected ContainerValidator getNestedDisabledContainerValidator() {

        return new XHTMLBasicNestedDisabledContainerValidator();
    }

    // Javadoc inherited.
    public RemoveTableRule getRemoveTableRule() {

        return new XHTMLBasicRemoveTableRule();
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 19-Aug-05	9289/3	emma	VBM:2005081602 Refactoring UnabridgedTransformers so they're not singletons

 17-Aug-05	9289/1	emma	VBM:2005081602 Refactoring TransMapper and TransFactory so that they're not singletons

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
