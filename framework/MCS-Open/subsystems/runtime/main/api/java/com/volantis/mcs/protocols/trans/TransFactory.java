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
 * $Header: /src/voyager/com/volantis/mcs/protocols/trans/TransFactory.java,v 1.4 2003/01/09 11:41:36 philws Exp $
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
 * 09-Jan-03    Phil W-S        VBM:2003010902 - Add protocol parameter to
 *                              getTable and getCell. Add optimization level
 *                              parameter to getTable.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.trans;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.html.RemoveTableRule;

/**
 * The creation and destruction of concrete specialization versions of trans
 * classes are managed via a class implementing this abstract class.
 *
 * @mock.generate
 */
public abstract class TransFactory {

    /**
     * Used to determine if an element has any style information that should
     * be preserved.
     */
    protected final TransformationConfiguration transformationConfiguration;

    /**
     * Initialize a new instance using the given parameters.
     *
     * @param configuration     protocol specific configuration. May be null.
     */
    protected TransFactory(TransformationConfiguration configuration) {
        this.transformationConfiguration = configuration;
    }

    /**
     * A (potentially cached or newly created) TransTable (specialization)
     * instance is returned, initialised using the given parameters.
     *
     * @param table             the DOM element to be wrappered by the
     *                          TransTable
     * @param protocol          the protocol which generated the DOM that is to
     *                          be processed
     * @return the TransTable used to represent the given DOM table element
     */
    public abstract TransTable getTable(Element table, DOMProtocol protocol);

    /**
     * A (potentially cached or newly created) TransCell (specialization)
     * instance is returned, initialised using the given parameters.
     *
     * @param row      the DOM table row element containing the cell element
     * @param cell     the DOM table cell element wrappered by the TransCell
     * @param startRow the starting row index where the cell is found in the
     *                 table
     * @param startCol the starting column index where the cell is found in the
     *                 table
     * @param protocol the protocol which generated the DOM to be processed
     * @return the TransTable used to represent the given DOM table element
     */
    public abstract TransCell getCell(Element row, Element cell, int startRow,
            int startCol, DOMProtocol protocol);

    /**
     * Returns the LCM implementation singleton in use.
     *
     * @return the LCM implementation singleton in use
     */
    public LCM getLCM() {
        return LCMImpl.getInstance();
    }

    /**
     * Returns the mapper implementation in use.
     * <p>
     * This will be non-null when "redundant" table removal and "remaining"
     * table removal are required (when nested tables are disabled). If only
     * "normal" table removal is required (i.e. table removal driven from the
     * layout, and nested tables are enabled) it may be null.
     *
     * @param protocol the protocol in use.
     * @return the mapper implementation in use, or null.
     */
    public AbridgedTransMapper getMapper(VolantisProtocol protocol) {
        // If the protocol does not support nested tables...
        if (!protocol.supportsNestedTables()) {
            // Then return the mapper so that we can do tasks which require
            // remapping, i.e. "redundant" table removal and "remaining" table
            // removal.
            return getNestedDisabledMapper();
        } else {
            // Else the protocol supports nested tables. For now we assume that
            // it will only ever do "normal" table removal and never require
            // any of the additional removals that require a mapper.
            //
            // In fact it is possible that protocols that support nested tables
            // may wish to use a mapper (in particular for "redundant" table
            // removal), but if that is the case then this would need to be
            // implemented explicitly rather than as a side effect of this L3.
            return null;
        }
    }

    /**
     * Get the mapper to use (if we do not support nested tables).
     *
     * @return the mapper to use.
     */
    protected abstract AbridgedTransMapper getNestedDisabledMapper();

    /**
     * Returns the element helper implementation singleton in use.
     *
     * @return the element helper implementation singleton in use
     */
    public abstract ElementHelper getElementHelper();

    /**
     * A newly created TransVisitor (specialization)
     * instance is returned, initialised using the given parameters.
     *
     * @param protocol the protocol for which the visitor is required.
     * @return the TransVisitor that can be used to process the protocol's
     *         DOM output
     */
    public abstract TransVisitor getVisitor(DOMProtocol protocol);

    /**
     * Allows a TransContext (specialization) to be obtained, initialized
     * using the given table row DOM element.
     *
     * @param row the DOM table row element used to initialize the context
     * @return the TransContext instance required
     */
    public TransContext getContext(Element row) {
        return new TransContext(row);
    }

    /**
     * Returns the container validator implementation singleton in use.
     *
     * @param protocol the protocol in use.
     * @return the container validator implementation singleton in use
     */
    public ContainerValidator getContainerValidator(VolantisProtocol protocol) {

        if (protocol.supportsNestedTables()) {
            return getNestedEnabledContainerValidator();
        } else {
            return getNestedDisabledContainerValidator();
        }

    }

    /**
     * Return the container validator to use when nested tables are supported.
     * <p/>
     * In this case the container validator needs to be set up to do very
     * little if anything as most tables will not be optimised away.
     */
    protected abstract ContainerValidator getNestedEnabledContainerValidator();

    /**
     * Return the container validator to use when nested tables are not
     * supported.
     * <p>
     * In this case the container validator needs to be set up to resolve
     * containments down to a single table.
     *
     * @return
     */
    protected abstract ContainerValidator getNestedDisabledContainerValidator();

    /**
     * Return the remove table rule in use.
     * <p>
     * This rule will be used to control the removal of "top level" tables. The
     * rule must be set up to decide if these tables are "redundant" or not.
     * <p>
     * Usually we say that single column tables which have no visual effects
     * (eg stylistic attibutes) associated with them are redundant.
     * <p>
     * Note that is only used if this factory returns a non-null mapper.
     *
     * @return the remove table rule in use.
     */
    public abstract RemoveTableRule getRemoveTableRule();

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 19-Aug-05	9289/3	emma	VBM:2005081602 Refactoring UnabridgedTransformers so they're not singletons

 17-Aug-05	9289/1	emma	VBM:2005081602 Refactoring TransMapper and TransFactory so that they're not singletons

 22-Jul-05	8859/3	emma	VBM:2005062006 Modify transformers to take account of Styles when flattening/optimizing tables

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
