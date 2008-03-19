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
 * $Header: /src/voyager/com/volantis/mcs/build/marlin/AttributeInfo.java,v 1.3 2002/11/23 01:04:28 pduffin Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 19-Dec-01    Paul            VBM:2001120506 - Created.
 * 23-Jan-02    Paul            VBM:2002012202 - Made it explicit which parts
 *                              of the code relate to papi and added support
 *                              for specifying a jsp name.
 * 25-Jan-02    Paul            VBM:2002012503 - Added marinerExpression and
 *                              defaultComponentType.
 * 12 Mar 02    Steve           VBM:2002022203 - Added maml attributes
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 02-Apr-02    Mat             VBM:2002022009 - Changed PAPI references to API
 *                              so that other generators (eg IMDAPI) can use
 *                              the same names.
 * 03-Apr-02    Mat             VBM:2002022009 - Added get/setAttributeType
 * 16-May-02    Paul            VBM:2002032501 - Moved from the
 *                              com.volantis.mcs.build package.
 * 14-Oct-02    Mat             VBM:2002090207 - Added get/setMapRuleType
 * 22-Nov-02    Paul            VBM:2002112214 - Removed unused references to
 *                              maml specific properties and renamed the others
 *                              to make them marlin specific.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.build.marlin;

import com.volantis.mcs.build.parser.AttributeDefinition;
import com.volantis.mcs.build.parser.Scope;

/**
 * This class adds extra information to the
 * <code>AttributeDefinition</code> which is needed to automatically
 * generate code.
 */
public class AttributeInfo
        extends AttributeDefinition {

    /**
     * The type of the attribute (string, integer etc.)
     */
    private String attributeType;

    /**
     * If this flag is true then this attribute is deprecated and will be removed
     * in the future, otherwise it is not.
     */
    private boolean deprecated;

    /**
     * If this flag is true then this attribute is inherited and so should not
     * generate a member.
     */
    private boolean inherited;

    /**
     * The name to use for this attribute when generating api code.
     */
    private String apiName;

    /**
     * The name of the protocol attribute.
     */
    private String protocolName;

    /**
     * Create a new <code>AttributeInfo</code>.
     *
     * @param scope The scope within which this object belongs.
     * @param name  The name of the attribute.
     */
    public AttributeInfo(Scope scope, String name) {
        super(name, scope);

        setAPIName(name);
        // Default the type to String
        setAttributeType("String");
    }

    /**
     * Set the deprecated property.
     *
     * @param deprecated The new value of the deprecated property.
     */
    public void setDeprecated(boolean deprecated) {
        this.deprecated = deprecated;
    }

    /**
     * Get the deprecated property.
     *
     * @return The value of the deprecated property.
     */
    public boolean isDeprecated() {
        return deprecated;
    }

    /**
     * Set the inherited property.
     *
     * @param inherited The new value of the inherited property.
     */
    public void setInherited(boolean inherited) {
        this.inherited = inherited;
    }

    /**
     * Get the inherited property.
     *
     * @return The value of the inherited property.
     */
    public boolean isInherited() {
        return inherited;
    }

    /**
     * Set the name that should be used for this attribute when generating
     * api code.
     *
     * @param apiName The name of this attribute which should be used when
     *                generating api code.
     */
    public void setAPIName(String apiName) {
        this.apiName = apiName;
    }

    /**
     * Get the name that should be used for this attribute when generating
     * api code.
     *
     * @return The name of this attribute which should be used when
     *         generating api code, defaults to the name of this attribute.
     */
    public String getAPIName() {
        return apiName;
    }

    /**
     * Set the name that should be used for this attribute when accessing the
     * protocol.
     *
     * @param protocolName The name of this attribute which should be used when
     *                     accessing the protocol.
     */
    public void setProtocolName(String protocolName) {
        this.protocolName = protocolName;
    }

    /**
     * Get the name that should be used for this attribute when accessing the
     * protocol.
     *
     * @return The name of this attribute which should be used when
     *         accessing the protocol, defaults to the name of this attribute.
     */
    public String getProtocolName() {
        if (protocolName == null) {
            return apiName;
        } else if (protocolName.length() == 0) {
            return null;
        } else {
            return protocolName;
        }
    }

    /**
     * Get the type of the attribute
     *
     * @return The type of the attribute
     */
    public String getAttributeType() {
        return attributeType;
    }

    /**
     * Set the type of the attribute
     *
     * @param attributeType The type of the attribute
     */
    public void setAttributeType(String attributeType) {
        this.attributeType = attributeType;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 09-Feb-05	6914/1	geoff	VBM:2005020707 R821: Branding using Projects: Prerequisites: Fix schema and related expressions

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
