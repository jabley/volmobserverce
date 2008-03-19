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


package com.volantis.mcs.remote.impl;

import com.volantis.mcs.policies.PolicyBuilder;
import com.volantis.mcs.policies.impl.AbstractPolicyBuilder;
import com.volantis.mcs.remote.PolicyBuilders;
import com.volantis.mcs.repository.xml.PolicySchemas;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Collection of {@link PolicyBuilder}s.
 *
 * <p>Actually this maintains a list of {@link BuilderContainer} that is
 * created by JIBX but when asked will convert it into a list of
 * {@link PolicyBuilder} instances.</p>
 *
 * <p>This can be the root of the object hierarchy when marshalling so see
 * {@link AbstractPolicyBuilder}. The only difference is that if this object is
 * being marshalled then it is always the root.</p>
 */
public class PolicyBuildersImpl
        implements PolicyBuilders {

    /**
     * The fixed value of the xsi:schemaLocation attribute on this element.
     */
    private static final String XSI_SCHEMA_LOCATION =
            PolicySchemas.MARLIN_RPDM_CURRENT.getXSISchemaLocation();

    /**
     * True if the schema location should be written out false otherwise.
     */
    private boolean writeSchemaLocation = true;

    /**
     * The list of {@link BuilderContainer}.
     *
     * <p>Used by JIBX.</p>
     */
    List containers;

    // Javadoc inherited.
    public List getPolicyBuilders() {
        List builders = new ArrayList();
        for (Iterator i = containers.iterator(); i.hasNext();) {
            BuilderContainer container = (BuilderContainer) i.next();
            PolicyBuilder builder = container.getPolicyBuilder();
            builder.setName(container.getUrl());
            builders.add(builder);
        }

        return builders;
    }

    public String getXSISchemaLocation() {
        if (writeSchemaLocation) {
            return XSI_SCHEMA_LOCATION;
        } else {
            return null;
        }
    }

    public void setXSISchemaLocation(String xsiSchemaLocation) {
        if (xsiSchemaLocation == null) {
            writeSchemaLocation = false;
        } else if (!xsiSchemaLocation.equals(XSI_SCHEMA_LOCATION)) {
            throw new IllegalArgumentException("Unexpected schema location");
        } else {
            writeSchemaLocation = true;
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Oct-05	9500/4	ianw	VBM:2005091308 Rationalise RPDM and LPDM

 29-Sep-05	9500/1	ianw	VBM:2005091308 Interim commit for Ian B

 ===========================================================================
*/
