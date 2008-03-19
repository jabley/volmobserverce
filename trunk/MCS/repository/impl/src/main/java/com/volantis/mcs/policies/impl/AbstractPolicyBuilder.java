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

package com.volantis.mcs.policies.impl;

import com.volantis.mcs.policies.CacheControl;
import com.volantis.mcs.policies.CacheControlBuilder;
import com.volantis.mcs.policies.InternalBuilder;
import com.volantis.mcs.policies.InternalPolicyBuilder;
import com.volantis.mcs.policies.Policy;
import com.volantis.mcs.repository.xml.PolicySchemas;
import org.jibx.runtime.IMarshallingContext;

/**
 * Base class for all implementations of {@link InternalPolicyBuilder}.
 *
 * <p>As this may be the root of the marshalled objects it needs to support
 * writing out an xsi:schemaLocation attribute. If this is not then root then
 * it is the actual root's responsibility to write it out. Otherwise,
 * this should always write out the attribute unless this was created from an
 * XML file that did not have an xsi:schemaLocation. In that case not writing
 * out a schema location makes it possible to perform a round trip.</p>
 *
 * <p>The xsi:schemaLocation is not exposed outside this class as it is
 * not necessary for any external code to know about it. In fact it would be
 * much better if this was supported by JIBX itself but that will require an
 * enhancement.</p>
 */
public abstract class AbstractPolicyBuilder
        extends AbstractBuilder
        implements InternalPolicyBuilder {

    /**
     * The fixed value of the xsi:schemaLocation attribute on this element.
     */
    private static final String XSI_SCHEMA_LOCATION =
            PolicySchemas.MARLIN_LPDM_CURRENT.getXSISchemaLocation();

    /**
     * True if this object is the marshalling root and false otherwise.
     */
    private boolean marshallingRoot;

    /**
     * True if the schema location should be written out false otherwise.
     */
    private boolean writeSchemaLocation = true;

    private String name;

    private CacheControlBuilder cacheControlBuilder;

    protected AbstractPolicyBuilder(Policy policy) {
        if (policy != null) {
            name = policy.getName();

            CacheControl cacheControl = policy.getCacheControl();
            if (cacheControl != null) {
                cacheControlBuilder = cacheControl.getCacheControlBuilder();
            }
        }
    }

    /**
     * Determines whether this policy is the marshalling root and therefore
     * responsible for writing out the xsi:schemaLocation.
     *
     * @param context The JIBX marshalling context.
     */
    public void jibxPreGet(IMarshallingContext context) {
        marshallingRoot = context.getStackDepth() == 1;
    }

    public String getXSISchemaLocation() {
        if (marshallingRoot && writeSchemaLocation) {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (!equals(this.name, name)) {
            stateChanged();
        }

        this.name = name;
    }

    public CacheControlBuilder getCacheControlBuilder() {
        return cacheControlBuilder;
    }

    public void setCacheControlBuilder(CacheControlBuilder cacheControlBuilder) {
        if (!equals(this.cacheControlBuilder, cacheControlBuilder)) {
            changedNestedBuilder((InternalBuilder) this.cacheControlBuilder,
                    (InternalBuilder) cacheControlBuilder);
        }

        this.cacheControlBuilder = cacheControlBuilder;
    }

    // Javadoc inherited.
    protected boolean castThenCheckEquality(Object obj) {
        return (obj instanceof AbstractPolicyBuilder) ?
                equalsSpecific((AbstractPolicyBuilder) obj) : false;
    }

    /**
     * @see #equalsSpecific(EqualsHashCodeBase)
     */
    protected boolean equalsSpecific(AbstractPolicyBuilder policy) {
        return super.equalsSpecific(policy) &&
                equals(name, policy.name) &&
                equals(cacheControlBuilder, policy.cacheControlBuilder);
    }

    public int hashCode() {
        int result = super.hashCode();
        result = hashCode(result, name);
        result = hashCode(result, cacheControlBuilder);
        return result;
    }

    protected Object getBuiltObject() {
        return getPolicy();
    }
}
