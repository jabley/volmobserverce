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

package com.volantis.shared.metadata.value;

import com.volantis.shared.metadata.impl.MetaDataObjectImplTestCaseAbstract;
import com.volantis.shared.metadata.MetaDataObject;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.ByteArrayInputStream;

/**
 * Abstract test for implementations of <code>MetaDataValue</code>.
 */
public abstract class MetaDataValueTestCaseAbstract extends MetaDataObjectImplTestCaseAbstract {

    // Javadoc inherited.
    public MetaDataValueTestCaseAbstract(String name) throws Exception {
        super(name);
    }

    /**
     * Tests serialisation
     * @throws Exception
     */
    public void testSerialization() throws Exception {
        MetaDataObject originalObject = (MetaDataObject) getImmutableInhibitor();
        MetaDataObject result = checkSerialization(originalObject);
        assertEquals("The orignal and the serialized and deserialized object " +
            "must have the same hash code",
                     originalObject.hashCode(), result.hashCode());
        assertEquals("The original and the serialized and deserialized object " +
                "should be equal" , originalObject, result);
    }

    /**
     * Utility method that serializes the <code>entity</code> parameter and
     * then deserializes it and returns the result. Any error in serialization
     * or deserialization will cause an Exception to be thrown.
     *
     * @param object the HTTPMessageEntity whose serialisation you wish to test
     * @return the deserialized copy of the <code>entity</code> implementation.
     * @see java.io.ObjectInputStream, java.io.ObjectOutputStream for the
     * exceptions that can be thrown
     */
    public static MetaDataObject checkSerialization(
            MetaDataObject object) throws Exception {

        if (object == null) {
            throw new IllegalArgumentException("Argument must not be null");
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(object);
        oos.flush();
        byte[] data = baos.toByteArray();
        ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(data));
        MetaDataObject result = (MetaDataObject) ois.readObject();
        return result;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Jan-05	6560/5	tom	VBM:2004122401 Added Inhibitor base class

 10-Jan-05	6560/3	tom	VBM:2004122401 Began implementation of the new Metadata API

 ===========================================================================
*/
