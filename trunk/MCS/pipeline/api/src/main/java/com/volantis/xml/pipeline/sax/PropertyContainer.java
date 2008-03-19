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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.pipeline.sax;

/**
 * Contains a set of properties.
 *
 * <p><strong>Warning: This is a facade provided for use by user code, not for
 * implementation. User implementations of this interface are highly likely to
 * be incompatible with future releases of the product at both binary and source
 * levels.</strong></p>
 *
 * <p>Properties in this class differ from normal bean properties in that they
 * are identified by an {@link Object} key instead of a {@link String} name.</p>
 * <p>The reason for this difference is simply that it means that it is possible
 * to create {@link Object} keys that are guaranteed to be unique within a
 * container. It is not possible to do this with {@link String} keys.</p>
 * <p>Objects used as keys must adhere to the defined semantics for the
 * {@link Object#hashCode} and {@link Object#equals} methods.</p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public interface PropertyContainer {

    /**
     * The volantis copyright statement
     */
    String mark = "(c) Volantis Systems Ltd 2003. ";

    /**
     * Sets the value of a custom property.
     * todo: Document the releaseOnChange flag here, what happens when this
     * todo: method is called for a property with the same value as the property
     * todo: already has and different flags, ...
     *
     * @param key             The key that identifies the property.
     * @param value           The value of the property.
     * @param releaseOnChange Indicates whether any resources owned by the
     * property value should be released when the property value changes. If
     * this is true and the object implements
     * {@link com.volantis.xml.pipeline.sax.ResourceOwner} then its
     * <code>release</code> method is invoked after the property is changed,
     * otherwise it is not.
     */
    public void setProperty(Object key, Object value, boolean releaseOnChange);

    /**
     * Gets the value of a custom property.
     * @param key The key that identifies the property.
     * @return The value of the property, or null if the property has not been
     *         set.
     */
    public Object getProperty(Object key);

    /**
     * Remove the property that has the specified key. <p>This is equivalent to
     * calling <code>setProperty</code> with the same key and a null
     * value.</p>
     * @param key The key that identifies the property.
     * @return The value of the property, of null if the property has not been
     *         set.
     */
    public Object removeProperty(Object key);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 10-Aug-03	264/4	adrian	VBM:2003072807 added error recovery support to pipeline context and associated object hierarchy

 23-Jul-03	236/1	doug	VBM:2003072206 PropertyContainer implementation

 16-Jul-03	208/1	doug	VBM:2003071603 Added as part of Pipeline public API

 ===========================================================================
*/
