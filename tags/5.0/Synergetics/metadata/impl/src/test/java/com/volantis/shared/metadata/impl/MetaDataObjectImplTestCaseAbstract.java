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

package com.volantis.shared.metadata.impl;

import com.volantis.shared.inhibitor.InhibitorTestCaseAbstract;
import com.volantis.shared.metadata.MetaDataFactory;
import com.volantis.shared.metadata.MetaDataObject;
import com.volantis.shared.metadata.impl.persistence.MetadataJDOPersistence;
import com.volantis.shared.metadata.type.MetaDataTypeFactory;
import com.volantis.shared.metadata.type.constraint.ConstraintFactory;
import com.volantis.shared.metadata.value.MetaDataValueFactory;
import com.volantis.synergetics.jdo.JDOPersistence;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Properties;


/**
 * Abstract test case for all implementations of <code>MetaDataObject</code>.
 */
public abstract class MetaDataObjectImplTestCaseAbstract extends InhibitorTestCaseAbstract {
    public static final MetaDataFactory META_DATA_FACTORY =
        MetaDataFactory.getDefaultInstance();
    public static final MetaDataTypeFactory TYPE_FACTORY =
        META_DATA_FACTORY.getTypeFactory();
    public static final ConstraintFactory CONSTRAINT_FACTORY =
        TYPE_FACTORY.getConstraintFactory();
    public static final MetaDataValueFactory VALUE_FACTORY =
        META_DATA_FACTORY.getValueFactory();
    private MetadataJDOPersistence persistence;

    /**
     * Constructor.
     * @param name The name of this test.
     */
    public MetaDataObjectImplTestCaseAbstract(String name) throws Exception {
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

    /**
     * Utility method that serializes the <code>object</code> parameter and
     * then deserializes it and returns the result. Any error in serialization
     * or deserialization will cause an Exception to be thrown.
     *
     * @param object the MetaDataObject whose serialisation you wish to test
     * @return the deserialized copy of the <code>object</code> implementation.
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

    /**
     * Utility method that persists the <code>object</code> parameter to a DB
     * and then retrieves it and returns the result. Any error in persistence
     * will cause an Exception to be thrown.
     *
     * @param object the MetaDataObject whose persistence you wish to test
     * @return the detached copy of the <code>object</code>
     *         implementation.
     *
     * @see com.volantis.synergetics.jdo.JDOException for exceptions that can
     * be thrown
     */
    public MetaDataObject checkPersistence(MetaDataObject object)
        throws Exception {
        if (object == null) {
            throw new IllegalArgumentException("Argument must not be null");
        }
        Object id = persistence.persistObject(object);
        MetaDataObject result = (MetaDataObject) persistence.retrieveObject(id);
        assertEquals(object.hashCode(), result.hashCode());
        assertEquals(object, result);
        return result;
    }


    /**
     * Utility method that serializes the <code>object</code> parameter
     * then deserializes it. It then asserts that the deserialized version
     * is equal to the original.
     * @param object the HTTPMessageEntity to test
     *
     */
    public static void checkEquality(MetaDataObject object)
            throws Exception {
        MetaDataObject result = checkSerialization(object);
        assertEquals("The deserialized object is not equal to the object " +
                "before serialization", object, result);
    }

    /**
     * Test the implementors or the MetaDataObject interface for
     * correct Serialization behaviour.
     * @throws java.lang.Exception
     */
    public void testHTTPSerialization() throws Exception {
        MetaDataObject mdo = (MetaDataObject) getImmutableInhibitor();
        checkEquality(mdo);
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

 17-Jan-05	6560/7	tom	VBM:2004122401 Added Simple Test for Serialisation

 14-Jan-05	6560/5	tom	VBM:2004122401 Added Inhibitor base class

 13-Jan-05	6560/3	tom	VBM:2004122401 More Metadata API implementation

 10-Jan-05	6560/1	tom	VBM:2004122401 Began implementation of the new Metadata API

 ===========================================================================
*/
