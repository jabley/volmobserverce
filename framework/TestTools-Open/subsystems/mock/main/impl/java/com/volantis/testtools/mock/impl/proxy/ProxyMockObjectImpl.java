/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.impl.proxy;

import com.volantis.testtools.mock.MockObject;
import com.volantis.testtools.mock.generated.MockObjectBase;
import com.volantis.testtools.mock.proxy.ProxyMockObject;
import com.volantis.testtools.mock.value.ExpectedValue;

/**
 * Implementation of proxy mock object.
 */
public class ProxyMockObjectImpl
        extends MockObjectBase
        implements ProxyMockObject, ExpectedValue {

    /**
     * The additional information that is stored.
     */
    private Object object;

    /**
     * Initialise.
     *
     * @param mockedInterface The class that is being mocked.
     * @param identifier The identifier of the proxy object.
     */
    public ProxyMockObjectImpl(Class mockedInterface, String identifier) {
        super(mockedInterface, identifier);

        if (identifier == null) {
            throw new IllegalArgumentException("identifier cannot be null");
        }
    }

    // Javadoc inherited.
    public void setObject(Object object) {
        this.object = object;
    }

    // Javadoc inherited.
    public Object getObject() {
        return object;
    }

    /**
     * Override to match objects of the same type but will either no
     * identifier, or the same identifier as this one.
     */
    public boolean matches(Object object) {
        if (!(object instanceof MockObject)) {
            return false;
        }

        MockObject mock = (MockObject) object;
        String mockIdentifier = mock._getIdentifier();
        return _mockedClass.isInstance(object)
                && (mockIdentifier == null || mockIdentifier.equals(_identifier));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Jul-05	8996/1	pduffin	VBM:2005071103 Enhanced mock generation to support methods defined on Object better

 21-Jun-05	8833/1	pduffin	VBM:2005042901 Merged changes from MCS 3.3.1, improved testability of the protocols

 ===========================================================================
*/
