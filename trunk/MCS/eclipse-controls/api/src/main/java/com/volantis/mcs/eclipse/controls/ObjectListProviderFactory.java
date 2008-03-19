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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.eclipse.controls;

import org.eclipse.swt.widgets.Composite;
import com.volantis.mcs.themes.parsing.ObjectParser;

/**
 * A {@link ValidatedObjectControlFactory} for {@link ObjectListProvider}s.
 */
public class ObjectListProviderFactory
        implements ValidatedObjectControlFactory {
    /**
     * The objects to display in the list.
     */
    private Object[] inList;

    /**
     * The parser to use for viewing the objects.
     */
    private ObjectParser parser;

    /**
     * Create a new ObjectListProviderFactory.
     *
     * @param inList The objects to display in the lists created by this
     *               factory
     * @param parser The parser to use for viewing the objects in the list
     */
    public ObjectListProviderFactory(Object[] inList, ObjectParser parser) {
        this.inList = inList;
        this.parser = parser;
    }

    // Javadoc inherited
    public ValidatedObjectControl buildValidatedObjectControl(Composite parent,
                                                              int style) {
        return new ObjectListProvider(parent, style, inList, parser);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Sep-05	9407/1	pduffin	VBM:2005083007 Removed old themes model

 21-Jul-05	8713/2	adrianj	VBM:2005060902 Enhanced GUI for creating new selectors

 ===========================================================================
*/
