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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mps.bms.impl;

import com.volantis.mps.bms.MessageService;
import com.volantis.mps.bms.MessageServiceFactory;
import com.volantis.synergetics.factory.MetaFactory;

/**
 * This class provides a split between the local and remote action
 * implementation. It can do this since
 * {@link com.volantis.synergetics.factory.MetaFactory} can try to create an
 * instance of the class, but it will only throw an Exception when the client
 * tries to create an instance of the object defined by the class. Since we
 * should never try to create the wrong instance without having the classpath
 * set correctly, this provides a fine place for the remote / local separation.
 */
public class DefaultMessageServiceFactory extends MessageServiceFactory {

    // MetaFactory can try to create an instance of the class, but it will only
    // throw an Exception when you try to create an instance. Since we should
    // never try to create an the wrong instance without having the classpath
    // set correctly, this provides a fine place for the remote / local
    // separation.

    /**
     * Local factory
     */
    private static final MetaFactory LOCAL = new MetaFactory(
            "com.volantis.mps.bms.impl.local.InternalMessageService",
            DefaultMessageServiceFactory.class.getClassLoader(),
            new Class[] {String.class});

    /**
     * Remote factory
     */
    private static final MetaFactory REMOTE = new MetaFactory(
      "com.volantis.mps.bms.impl.remote.RemoteMessageService",
            DefaultMessageServiceFactory.class.getClassLoader(),
            new Class[] {String.class});

    // javadoc inherited
    public MessageService createMessageService(String endpoint) {
        if (null == endpoint || endpoint.startsWith("internal:")) {
            return (MessageService) LOCAL.createInstance(new Object[] { endpoint });
        } else {
            return (MessageService) REMOTE.createInstance(new Object[] { endpoint });
        }
    }
}
