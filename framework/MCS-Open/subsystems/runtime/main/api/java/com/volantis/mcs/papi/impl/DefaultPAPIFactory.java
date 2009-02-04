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


package com.volantis.mcs.papi.impl;

import com.volantis.mcs.papi.PAPIElementFactory;
import com.volantis.mcs.papi.PAPIFactory;
import com.volantis.mcs.pickle.PickleBlockAttributes;
import com.volantis.mcs.pickle.PickleHeadAttributes;
import com.volantis.mcs.pickle.PickleInlineAttributes;
import com.volantis.mcs.pickle.impl.PickleBlockElementImpl;
import com.volantis.mcs.pickle.impl.PickleHeadElementImpl;
import com.volantis.mcs.pickle.impl.PickleInlineElementImpl;
import com.volantis.mcs.pickle.impl.PickleNativeElementImpl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This class provides the non public API implementation of PAPIFactory.
 * <p>This factory will return a @link PAPIElementFactory gor a given element type, e.g "a", "big" etc.</p>
 */
public class DefaultPAPIFactory
        extends PAPIFactory {

    /**
     * The map of factories for all PAPI elements
     */
    private static final Map papiImplementationFactories = new HashMap();

    /**
     * The map of element names to PAPIImplementionInfo's derived from the CDM Schema
     */
    private static final Map papiImplementationInfos =
            PAPIImplementationData.getImplentationInfoMap();

    /**
     * The PAPIImplementationFactory for PickleBlock objects.
     */
    private static final PAPIImplementationFactory pickleBlockImplementationFactory =
            new PAPIImplementationFactory(new PAPIImplementationInfo(
                    "Pickle Block", PickleBlockElementImpl.class,
                    PickleBlockAttributes.class));

    /**
     * The PAPIImplementationFactory for PickleHead objects.
     */
    private static final PAPIImplementationFactory pickleHeadImplementationFactory =
            new PAPIImplementationFactory(new PAPIImplementationInfo(
                    "Pickle Head", PickleHeadElementImpl.class,
                    PickleHeadAttributes.class));

    /**
     * The PAPIImplementationFactory for PickleInline objects.
     */
    private static final PAPIImplementationFactory pickleInlineImplementationFactory =
            new PAPIImplementationFactory(new PAPIImplementationInfo(
                    "Pickle Inline", PickleInlineElementImpl.class,
                    PickleInlineAttributes.class));


    /**
     * The PAPIImplementationFactory for PickleNative objects.
     */
    private static final PAPIImplementationFactory pickleNativeImplementationFactory =
            new PAPIImplementationFactory(new PAPIImplementationInfo(
                    "Pickle Inline", PickleNativeElementImpl.class,
                    PickleInlineAttributes.class));

    static {

        Iterator implementationInfoIterator =
                papiImplementationInfos.values().iterator();

        while (implementationInfoIterator.hasNext()) {
            PAPIImplementationInfo papiImplementationInfo =
                    (PAPIImplementationInfo) implementationInfoIterator.next();

            PAPIElementFactory papiImplementationFactory =
                    new PAPIImplementationFactory(papiImplementationInfo);
            papiImplementationFactories.put(papiImplementationInfo.getElement(),
                    papiImplementationFactory);

        }
    }

    public DefaultPAPIFactory() {
    }

    /**
     * Get the PAPIElementFactory for the given PAPI Element
     *
     * @param element The name of the PAPIElement
     * @return The Factory responsible for creating implementations of the PAPI Element
     */
    public PAPIElementFactory getPAPIElementFactory(String element) {
        return (PAPIElementFactory) papiImplementationFactories.get(element);
    }

    /**
     * Get the PAPIElementFactory for PickleBlockElementImpl
     *
     * @return The Factory responsible for creating implementations of the PickleBlockElementImpl
     */
    public PAPIElementFactory getPickleBlockElementFactory() {
        return pickleBlockImplementationFactory;
    }

    /**
     * Get the PAPIElementFactory for PickleHeadElementImpl
     *
     * @return The Factory responsible for creating implementations of the PickleHeadElementImpl
     */
    public PAPIElementFactory getPickleHeadElementFactory() {
        return pickleHeadImplementationFactory;
    }

    /**
     * Get the PAPIElementFactory for PickleInlineElementImpl
     *
     * @return The Factory responsible for creating implementations of the PickleInlineElementImpl
     */
    public PAPIElementFactory getPickleInlineElementFactory() {
        return pickleInlineImplementationFactory;
    }

    /**
     * Get the PAPIElementFactory for PickleNativeElementImpl
     *
     * @return The Factory responsible for creating implementations of the PickleNativeElementImpl
     */
    public PAPIElementFactory getPickleNativeElementFactory() {
        return pickleNativeImplementationFactory;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-May-05	8196/3	ianw	VBM:2005051203 Fixed up javadoc

 18-May-05	8196/1	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 ===========================================================================
*/
