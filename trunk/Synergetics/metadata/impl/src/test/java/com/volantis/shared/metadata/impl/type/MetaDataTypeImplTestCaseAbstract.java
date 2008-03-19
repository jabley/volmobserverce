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

package com.volantis.shared.metadata.impl.type;

import com.volantis.shared.metadata.impl.MetaDataObjectImplTestCaseAbstract;
import com.volantis.shared.metadata.impl.persistence.MetadataJDOPersistence;
import com.volantis.shared.metadata.MetaDataObject;

import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Test case for <code>MetaDataType</code>.
 */
public abstract class MetaDataTypeImplTestCaseAbstract extends MetaDataObjectImplTestCaseAbstract{

    private MetadataJDOPersistence persistence;


    // Javadoc inherited.
    public MetaDataTypeImplTestCaseAbstract(String name) throws Exception {
        super(name);
        InputStream is = getClass().getClassLoader().getResourceAsStream(
            "com/volantis/shared/metadata/impl/jdo-persistence.properties");
        Properties props = new Properties();
        try {
            props.load(is);
            is.close();
            persistence =
                MetadataJDOPersistence.getInstance(props);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void testPersistence() throws Exception {
        MetaDataObject mdo = (MetaDataObject) getImmutableInhibitor();
        MetaDataObject result = checkPersistence(mdo);
        assertEquals("The detached object is not equal to the object " +
            "before persistence", mdo, result);
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
