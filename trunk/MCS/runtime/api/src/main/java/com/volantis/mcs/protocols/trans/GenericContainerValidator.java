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
 * $Header: /src/voyager/com/volantis/mcs/protocols/trans/GenericContainerValidator.java,v 1.3 2003/01/17 12:03:40 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 06-Jan-03    Phil W-S        VBM:2003010606 - Created. Refactoring of code
 *                              from XHTMLBasicContainerValidator to allow
 *                              better code reuse.
 * 17-Jan-03    Phil W-S        VBM:2003010606 - Rework: Refactored to remove
 *                              AbstractContainerValidator.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.trans;

import com.volantis.mcs.dom.Element;

import java.util.HashMap;
import java.util.Map;

/**
 * Hook class for simple ContainerValidator implementations. Specializations
 * should: <ol> <li>possibly implement the singleton pattern</li> <li>supply
 * the implementation of <code>initialize</code> to define their container
 * validator rules. This is done by adding entries to the
 * <code>containerActionMap</code>.</li> <li>define their required default
 * action (by construction or in the initialize method implementation.</li>
 * </ol> <p>The <code>containerActionMap</code> is keyed by the container
 * element type (tag) and must map this to an Integer wrapper for the
 * ContainerAction associated with that element type.</p> <p>The default action
 * (used by getAction for element types not found in the map) is PROMOTE unless
 * redefined using the alternative constructor or in the initialize
 * method.</p>
 *
 * @author <a href="mailto:phil.weighill-smith@volantis.com">Phil W-S</a>
 */
public abstract class GenericContainerValidator implements ContainerValidator,
    ContainerActions {
    /**
     * The set of special-case containers.
     *
     * @associates Integer
     */
    protected final Map containerActionMap = new HashMap();

    /**
     * The container action to be applied to any container not found in
     * {@link #containerActionMap}.
     */
    private final int defaultAction;

    /**
     * Initializes the new instance using the PROMOTE default action.
     *
     * @see #initialize
     */
    public GenericContainerValidator() {
        this(PROMOTE);
    }

    /**
     * Initializes the new instance using the given defaultAction.
     *
     * @param defaultAction the default action used for all element types not
     *                      found in the containerActionMap mapping
     * @see #initialize
     */
    public GenericContainerValidator(int defaultAction) {
        this.defaultAction = defaultAction;

        initialize();
    }

    public int getAction(Element container,
                         Element table) {
        int result = defaultAction;
        Integer action = (Integer)containerActionMap.get(container.getName());

        if (action != null) {
            result = action.intValue();
        }

        return result;
    }

    protected abstract void initialize();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
