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
package com.volantis.mcs.marlin.sax;

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

import java.util.HashMap;
import java.util.Map;

/**
* This singleton class provides a map of namespaces to the corresponding
* {@link AbstractContentHandlerFactory} for use with the
 * {@link NamespaceSwitchContentHandler}.
*/
public class NamespaceSwitchContentHandlerMap {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(
                    NamespaceSwitchContentHandlerMap.class);

    /**
     * A map from namespace to the corresponding AbstractContentHandlerFactory.
     */
    private Map contentHandlers = new HashMap();

    private static NamespaceSwitchContentHandlerMap instance =
        new NamespaceSwitchContentHandlerMap();

    /**
     * Protect our constructor from bein insantiated
     */
    private NamespaceSwitchContentHandlerMap() {
    }

    public static NamespaceSwitchContentHandlerMap getInstance() {
        return instance;
    }

    /**
     * Register the given {@link AbstractContentHandlerFactory} to be used to
     * create {@link MCSInternalContentHandler} instances to process elements
     * with the specified namespace String.
     *
     * @param namespace      of the elements that we wish to process with
     *                       the specified ContentHandler
     * @param handlerFactory creates {@link MCSInternalContentHandler}
     *                       instances which process the elements
     */
    public void addContentHandler(String namespace,
            AbstractContentHandlerFactory handlerFactory) {

        if (logger.isDebugEnabled()) {
            logger.debug("Registering Namespace " + namespace +
                    " against " + handlerFactory);
        }
        contentHandlers.put(namespace, handlerFactory);
    }

    /**
     * Retrieve the specified {@link AbstractContentHandlerFactory} from the
     * Map of ContentHandlers keyed on the specified namespace String.
     *
     * @param namespace     The namespace of the ContentHandler
     * @return AbstractContentHandlerFactory to use for creating handlers
     */
    AbstractContentHandlerFactory getContentHandlerFactory(String namespace) {
        return (AbstractContentHandlerFactory)contentHandlers.get(namespace);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 31-Aug-05	9391/1	emma	VBM:2005082604 Integrate the new XDIMEContentHandler and refactor NamespaceSwitchContentHandler (& Map) as required

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 20-May-04	4513/1	adrian	VBM:2004052007 Updated NamespaceSwitchContentHandler to use a HashMap instead of HashTable

 16-Feb-04	2966/1	ianw	VBM:2004011923 Fixed namespace issues

 02-Feb-04	2802/3	ianw	VBM:2004011921 Fixed copyright and added into AppServerInterface

 02-Feb-04	2802/1	ianw	VBM:2004011921 Added mechanism to enable AppServer interfaces to configure NamespaceSwitchContentHandler

 ===========================================================================
*/
