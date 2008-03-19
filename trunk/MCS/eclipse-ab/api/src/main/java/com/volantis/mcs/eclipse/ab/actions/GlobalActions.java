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
package com.volantis.mcs.eclipse.ab.actions;

import org.eclipse.jface.action.IAction;

/**
 * Data holder for global actions.
 */
public class GlobalActions {
    private final IAction copy;
    private final IAction cut;
    private final IAction delete;
    private final IAction paste;
    private final IAction selectAll;

    /**
     * Construct a new GlobalActions.
     * @param copy the copy action
     * @param cut the cut action
     * @param delete the delete action
     * @param paste the paste action
     * @param selectAll the select all action
     */
    public GlobalActions(IAction copy, IAction cut, IAction delete,
                           IAction paste, IAction selectAll) {
        this.copy = copy;
        this.cut = cut;
        this.delete = delete;
        this.paste = paste;
        this.selectAll = selectAll;
    }

    // javadoc unnecessary
    public IAction getCopy() {
        return copy;
    }

    // javadoc unnecessary
    public IAction getCut() {
        return cut;
    }

    // javadoc unnecessary
    public IAction getDelete() {
        return delete;
    }

    // javadoc unnecessary
    public IAction getPaste() {
        return paste;
    }

    // javdoc unnecessary
    public IAction getSelectAll() {
        return selectAll;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-May-05	8213/1	pcameron	VBM:2005031015 Added global actions to the Layout Outline Page

 11-Mar-05	6895/1	allan	VBM:2005020412 Commit to allow access to changes

 ===========================================================================
*/
