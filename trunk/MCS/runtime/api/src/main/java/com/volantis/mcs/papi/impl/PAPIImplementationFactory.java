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

import com.volantis.mcs.papi.PAPIAttributes;
import com.volantis.mcs.papi.PAPIElement;
import com.volantis.mcs.papi.PAPIElementFactory;

import java.lang.reflect.UndeclaredThrowableException;


public class PAPIImplementationFactory
        implements PAPIElementFactory {

    /**
     * The PAPIImplementationInfo object which defines what type of PAPIElement's and PAPIAttributes to create.
     */
    private final PAPIImplementationInfo papiImplementationInfo;

    /**
     * Protect our default constructor
     */
    private PAPIImplementationFactory() {
        papiImplementationInfo = null;
    }

    /**
     * Create a new PAPIImplementationFactory for the given PAPI implementation
     *
     * @param papiImplementationInfo The PAPIImplementationInfo object describing the implementation of the PAPI element.
     */
    public PAPIImplementationFactory(
            PAPIImplementationInfo papiImplementationInfo) {
        this.papiImplementationInfo = papiImplementationInfo;
    }

    //Javadoc inherited
    public PAPIElement createPAPIElement() {
        PAPIElement element = null;

        try {
            element = (PAPIElement) papiImplementationInfo.getElementImplClass()
                    .newInstance();
        } catch (InstantiationException e) {
            throw new UndeclaredThrowableException(e);
        } catch (IllegalAccessException e) {
            throw new UndeclaredThrowableException(e);
        }
        return element;
    }

    //Javadoc inherited
    public PAPIAttributes createElementSpecificAttributes() {
        PAPIAttributes attributes = null;

        try {
            attributes = (PAPIAttributes) papiImplementationInfo
                    .getAttributesImplClass().newInstance();
        } catch (InstantiationException e) {
            throw new UndeclaredThrowableException(e);
        } catch (IllegalAccessException e) {
            throw new UndeclaredThrowableException(e);
        }
        return attributes;
    }

    //Javadoc inherited
    public PAPIAttributes createGenericAttributes() {
        return new PAPIAttributesImpl();
    }

    /**
     * Get the name of the PAPI element for which this factory creates PAPI objects.
     *
     * @return The name of the PAPI element
     */
    public String getElement() {
        return papiImplementationInfo.getElement();
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 18-May-05	8196/1	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 ===========================================================================
*/
