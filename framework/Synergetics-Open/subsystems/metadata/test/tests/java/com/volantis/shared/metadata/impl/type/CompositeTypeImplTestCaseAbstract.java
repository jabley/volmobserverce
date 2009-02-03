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

package com.volantis.shared.metadata.impl.type;

/**
 * Abstract test case for {@link CompositeTypeImpl}.
 */
public abstract class CompositeTypeImplTestCaseAbstract
        extends MetaDataTypeImplTestCaseAbstract{

    /**
     * Default Constructor
     * @param name The name of this test case.
     */
    public CompositeTypeImplTestCaseAbstract(String name) throws Exception {
        super(name);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Jan-05	6560/1	tom	VBM:2004122401 Began implementation of the new Metadata API

 ===========================================================================
*/
