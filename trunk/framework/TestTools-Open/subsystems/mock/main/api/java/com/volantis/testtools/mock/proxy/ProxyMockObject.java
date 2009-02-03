/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.proxy;

import com.volantis.testtools.mock.MockObject;

/**
 * A proxy for an object that has not been created yet.
 *
 * <p>Although it is not recommended to mock classes that have behaviour in
 * the constructor sometimes it is necessary to do so. The reason that this
 * is not recommended is because if the constructor invokes methods that are
 * mocked then there is a circular dependency, i.e. the expectations can only
 * be set once it has been constructed but it cannot be constructed until the
 * expectations have been set.</p>
 *
 * <p>This class forms part of the mechanism to support this. It basically acts
 * as a proxy to which expectations can be added in order to allow the real
 * object to be constructed at which point this proxy is not used.</p>
 */
public interface ProxyMockObject
        extends MockObject {

    /**
     * Store some additional information with this object.
     *
     * @param object The information to store.
     */
    void setObject(Object object);

    /**
     * Get the previously stored information.
     *
     * @return The previously stored information.
     */
    Object getObject();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Jun-05	8833/1	pduffin	VBM:2005042901 Merged changes from MCS 3.3.1, improved testability of the protocols

 ===========================================================================
*/
