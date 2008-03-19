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

package com.volantis.shared.metadata;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.shared.metadata.type.MetaDataTypeFactory;
import com.volantis.shared.metadata.value.MetaDataValueFactory;

/**
 * Test case for <code>MetaDataFactory</code>.
 */
public abstract class MetaDataFactoryTestCaseAbstract extends TestCaseAbstract{

    /**
     * Constructor
     * @param name The name of this test.
     */
    public MetaDataFactoryTestCaseAbstract(String name) {
        super(name);
    }

    /**
     * Helper method which gets a <code>MetaDataFactory</code>. This method should be
     * overridden for every test relating to subclasses of <code>MetaDataFactory</code>.
     * @return
     */
    protected abstract MetaDataFactory getMetaDataFactory();

    /**
     * Test to ensure that the getMetaDataTypeFactory() method returns a valid
     * {@link MetaDataTypeFactory}.
     */
    public void testGetTypeFactory() {
        MetaDataFactory factory = getMetaDataFactory();
        MetaDataTypeFactory typeFactory = factory.getTypeFactory();
        assertNotNull("The type factory should not be null ", typeFactory);
    }

    /**
     * Test to ensure that the getMetaDataValueFactory() method returns a valid
     * {@link MetaDataValueFactory}.
     */
    public void testGetDataFactory() {
        MetaDataFactory factory = getMetaDataFactory();
        MetaDataValueFactory valueFactory = factory.getValueFactory();
        assertNotNull("The value factory should not be null ", valueFactory);
    }



}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Jan-05	6560/3	tom	VBM:2004122401 Began implementation of the new Metadata API

 ===========================================================================
*/
