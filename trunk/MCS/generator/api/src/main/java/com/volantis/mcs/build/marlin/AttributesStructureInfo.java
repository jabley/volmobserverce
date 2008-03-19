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
 * $Header: /src/voyager/com/volantis/mcs/build/marlin/AttributesStructureInfo.java,v 1.4 2002/11/28 11:11:01 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 19-Dec-01    Paul            VBM:2001120506 - Created.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 02-Apr-02	Mat		VBM:2002022009 - Changed references to PAPI to
 *				API so that other API's can use the names.
 * 16-May-02    Paul            VBM:2002032501 - Moved from the
 *                              com.volantis.mcs.build package.
 * 23-May-02    Paul            VBM:2002042202 - Added the method
 *                              getInterfaceAttributeGroups.
 * 14-Jun-02    Paul            VBM:2002053105 - Support groups which act like
 *                              anonymous interfaces.
 * 22-Nov-02    Geoff           VBM:2002111504 - Add the elementName attribute
 *                              and accessors, cleaned up Javadoc.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.build.marlin;

import com.volantis.mcs.build.parser.AttributeGroup;
import com.volantis.mcs.build.parser.AttributeGroupDefinition;
import com.volantis.mcs.build.parser.AttributesStructure;
import com.volantis.mcs.build.parser.SchemaAttribute;
import com.volantis.mcs.build.parser.SchemaObject;
import com.volantis.mcs.build.parser.Scope;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * This class adds extra information to the
 * <code>AttributesStructure</code> which is needed to automatically
 * generate code.
 */
public class AttributesStructureInfo
        extends AttributesStructure {

    /**
     * A comparator which will order SchemaAttribute objects by name.
     */
    private static final Comparator attributeComparator = new Comparator() {
        public int compare(Object o1, Object o2) {
            SchemaAttribute a1 = (SchemaAttribute) o1;
            SchemaAttribute a2 = (SchemaAttribute) o2;
            String n1 = a1.getName();
            String n2 = a2.getName();

            return n1.compareTo(n2);
        }
    };

    /**
     * Flag which indicates whether this attribute group's api attributes class
     * should be abstract or not.
     */
    private boolean abstractAPIAttributesClass;

    /**
     * The natural name of this element.
     */
    private String naturalName;

    /**
     * The name to use for this element's api attributes class.
     */
    private String apiAttributesClass;

    /**
     * The name to use for this element's api attributes interface.
     */
    private String apiAttributesInterface;

    /**
     * This attribute group's base attribute group.
     */
    private String apiBaseAttributeGroup;

    /**
     * The base name of the associated component class (if any)
     */
    private String apiComponentClassBase;

    /**
     * The name of the associated asset class (if any)
     */
    private String apiAssetClassName;

    /**
     * The name to use for this element's api element class.
     */
    //private String apiElementClass;

    public AttributesStructureInfo(SchemaObject owner) {
        super(owner);
    }

    /**
     * If this is an asset or component, this is the name of the associated class
     * in the assets package
     *
     * @param apiAssetClassName The asset class name
     */
    public
    void setApiAssetClassName(String apiAssetClassName) {
        this.apiAssetClassName = apiAssetClassName;
    }

    /**
     * If this is an asset or component, this is the name of the associated class
     * in the components package.
     * This name will be used to form the element and attributes class names.
     * element = base + "Element"
     * attributes = base + "Attributes"
     *
     * @param apiComponentClassBase The component class name
     */
    public
    void setApiComponentClassBase(String apiComponentClassBase) {
        this.apiComponentClassBase = apiComponentClassBase;
    }

    /**
     * Get the name of the associated class
     * in the assets package
     */
    public
    String getApiAssetClassName() {
        return apiAssetClassName;
    }

    /**
     * Get the name of the associated class
     * in the components package
     */
    public
    String getApiComponentClassBase() {
        return apiComponentClassBase;
    }

    /**
     * Set whether the API attributes class generated for this attribute group
     * should be abstract or not.
     *
     * @param abstractAPIAttributesClass True if the API attribute class
     *                                   generated for this attribute group is abstract and false otherwise.
     */
    public
    void setAbstractAPIAttributesClass(boolean abstractAPIAttributesClass) {
        this.abstractAPIAttributesClass = abstractAPIAttributesClass;
    }

    /**
     * Return true if the API attributes class generated for this attribute
     * group should be abstract and false otherwise.
     *
     * @return True if the API attribute class generated for this attribute
     *         group is abstract and false otherwise.
     */
    public boolean isAbstractAPIAttributesClass() {
        return abstractAPIAttributesClass;
    }

    /**
     * Set the natural name of the element.
     *
     * @param naturalName The natural name of the element.
     */
    public void setNaturalName(String naturalName) {
        this.naturalName = naturalName;
    }

    /**
     * Get the natural name of the element.
     *
     * @return The natural name of the element.
     */
    public String getNaturalName() {
        if (naturalName == null) {
            if (apiAttributesClass != null) {
                int index = apiAttributesClass.indexOf("Attributes");
                return apiAttributesClass.substring(0, index).toLowerCase();
            } else {
                int index = apiAttributesInterface.indexOf("Attributes");
                return apiAttributesInterface.substring(0, index).toLowerCase();
            }
        }

        return naturalName;
    }

    /**
     * Set the name of the API attributes class.
     *
     * @param apiAttributesClass The name of the API attributes class.
     */
    public void setAPIAttributesClass(String apiAttributesClass) {
        this.apiAttributesClass = apiAttributesClass;
    }

    /**
     * Get the name of the API attributes class.
     *
     * @return The name of the API attributes class.
     */
    public String getAPIAttributesClass() {
        return apiAttributesClass;
    }

    /**
     * Set the name of the API attributes interface.
     *
     * @param apiAttributesInterface The name of the API attributes interface.
     */
    public void setAPIAttributesInterface(String apiAttributesInterface) {
        this.apiAttributesInterface = apiAttributesInterface;
    }

    /**
     * Get the name of the API attributes interface.
     *
     * @return The name of the API attributes interface.
     */
    public String getAPIAttributesInterface() {
        return apiAttributesInterface;
    }

    /**
     * Set the name of the base attribute group.
     *
     * @param apiBaseAttributeGroup The name of the base attribute group.
     */
    public
    void setAPIBaseAttributeGroup(String apiBaseAttributeGroup) {
        this.apiBaseAttributeGroup = apiBaseAttributeGroup;
    }

    /**
     * Get the name of the base attribute group.
     *
     * @return The name of the base attribute group.
     */
    private String getAPIBaseAttributeGroup() {
        return apiBaseAttributeGroup;
    }

    /**
     * Get the <code>Scope</code> of the owner.
     *
     * @return The <code>Scope</code> of the owner.
     */
    private Scope getScope() {
        SchemaObject owner = getOwner();
        return owner.getScope();
    }

    /**
     * Check to see whether the class generated for this set of attributes is
     * a descendant of the specified class name.
     *
     * @param className The name of the class.
     */
    public boolean instanceOf(String className) {

        if (className.equals(apiAttributesClass)) {
            return true;
        }

        AttributesStructureInfo attributesStructureInfo;
        AttributeGroupInfo info;
        List attributeGroups = getAttributeGroups();
        for (Iterator i = attributeGroups.iterator(); i.hasNext();) {
            AttributeGroup group = (AttributeGroup) i.next();
            info = (AttributeGroupInfo) group.getDefinition();
            attributesStructureInfo = info.getAttributesStructureInfo();

            if (attributesStructureInfo.instanceOf(className)) {
                return true;
            }
        }

        // Try the base attribute group, if any.
        if (apiBaseAttributeGroup != null) {
            Scope scope = getScope();
            AttributeGroupDefinition definition
                    = scope.getAttributeGroupDefinition(apiBaseAttributeGroup);

            info = (AttributeGroupInfo) definition;
            attributesStructureInfo = info.getAttributesStructureInfo();

            return attributesStructureInfo.instanceOf(className);
        }

        return false;
    }

    /**
     * Find the <code>AttributeGroupInfo</code> which this object should be
     * based on.
     *
     * @return The <code>AttributeGroupInfo</code> whose API attributes class
     *         should be used as the base class of the class generated for these
     *         structures.
     */
    public AttributeGroupInfo findBaseAttributeGroup() {

        // If the specified info has an explicit base attribute group set then
        // use it.
        String baseGroupName = getAPIBaseAttributeGroup();
        if (baseGroupName != null) {
            return (AttributeGroupInfo)
                    getScope().getAttributeGroupDefinition(baseGroupName);
        }

        System.out.println("Checking structure info " + getNaturalName());

        // Search through the attribute groups which this refers to and see whether
        // any of them have an attributes class set, if so then they can be used
        // as a base class.
        AttributeGroupInfo baseInfo = null;
        List attributeGroups = getAttributeGroups();
        for (Iterator i = attributeGroups.iterator(); i.hasNext();) {
            AttributeGroup group = (AttributeGroup) i.next();
            System.out.println("Checking group " + group.getName());
            AttributeGroupInfo groupInfo
                    = (AttributeGroupInfo) group.getDefinition();
            AttributesStructureInfo groupAttributesStructureInfo
                    = groupInfo.getAttributesStructureInfo();

            if (groupAttributesStructureInfo.getAPIAttributesClass() != null) {
                if (baseInfo == null) {
                    baseInfo = groupInfo;
                } else {
                    throw new IllegalStateException
                            ("Multiple base attribute groups found "
                            + baseInfo.getName() +
                            " and " +
                            groupInfo.getName());
                }
            }
        }

        // If we found a base attribute group then return it.
        if (baseInfo != null) {
            return baseInfo;
        }

        return null;
    }

    /**
     * Get the names of all the interfaces which have to be implemented by the
     * API attributes class generated for these attributes. It does not include
     * interfaces which are implemented by base classes.
     *
     * @return A collection of the names of all the interfaces which have to be
     *         implemented by the API attributes class generated for these attributes.
     */
    public Collection getInterfaceNames() {
        List names = new ArrayList();

        List attributeGroups = getAttributeGroups();
        for (Iterator i = attributeGroups.iterator(); i.hasNext();) {
            AttributeGroup group = (AttributeGroup) i.next();
            AttributeGroupInfo groupInfo
                    = (AttributeGroupInfo) group.getDefinition();

            AttributesStructureInfo groupAttributesStructureInfo
                    = groupInfo.getAttributesStructureInfo();

            String apiAttributesInterface
                    = groupAttributesStructureInfo.getAPIAttributesInterface();

            if (apiAttributesInterface != null) {
                names.add(apiAttributesInterface);
            }
        }

        Collections.sort(names);

        return names;
    }

    /**
     * Get the attribute groups associated with the interfaces which have to be
     * implemented by the API attributes class generated for these attributes.
     * It does not include interfaces which are implemented by base classes.
     *
     * @return A collection of the attribute groups associated with all the
     *         interfaces which have to be implemented by the API attributes class
     *         generated for these attributes.
     */
    public Collection getInterfaceAttributeGroups() {
        List groups = new ArrayList();

        List attributeGroups = getAttributeGroups();
        for (Iterator i = attributeGroups.iterator(); i.hasNext();) {
            AttributeGroup group = (AttributeGroup) i.next();
            AttributeGroupInfo groupInfo
                    = (AttributeGroupInfo) group.getDefinition();

            AttributesStructureInfo groupAttributesStructureInfo
                    = groupInfo.getAttributesStructureInfo();

            String apiAttributesInterface
                    = groupAttributesStructureInfo.getAPIAttributesInterface();

            if (apiAttributesInterface != null) {
                groups.add(groupInfo);
            }
        }

        return groups;
    }

    /**
     * Get a collection of SchemaAttributes which must be implemented for this
     * object. This includes all those attributes inherited from interfaces but
     * does not include any attributes which are defined in the base class.
     *
     * @return A collection of <code>SchemaAttributes</code>.
     */
    public Collection getImplementedAttributes() {
        List list = new ArrayList();
        addImplementedAttributes(list);
        Collections.sort(list, attributeComparator);

        return list;
    }

    /**
     * Add the attributes which need implementing by the generated classes
     * for these attributes and all generated class which have to implement
     * this to the specified list.
     *
     * @param list The list of <code>SchemaAttributes</code>.
     */
    private void addImplementedAttributes(List list) {

        for (Iterator i = getAttributes().iterator(); i.hasNext();) {
            list.add(i.next());
        }

        for (Iterator i = getAttributeGroups().iterator(); i.hasNext();) {
            AttributeGroup group = (AttributeGroup) i.next();
            AttributeGroupInfo groupInfo
                    = (AttributeGroupInfo) group.getDefinition();

            AttributesStructureInfo attributesStructureInfo
                    = groupInfo.getAttributesStructureInfo();

            // If one of the dependent groups is an interface then we need to
            // implement them, if the group does not have either an interface or a
            // class name set then we need to implement its attributes as well.
            if (attributesStructureInfo.getAPIAttributesInterface() != null) {
                attributesStructureInfo.addImplementedAttributes(list);
            } else if (attributesStructureInfo.getAPIAttributesClass() ==
                    null) {
                System.out.println("Treating group " + group.getName()
                        + " as an anonymous interface");
                attributesStructureInfo.addImplementedAttributes(list);
            }
        }
    }

    /**
     * Get all the <code>SchemaAttributes</code>.
     * <p>
     * If there are any clashes then the <code>SchemaAttributes</code> defined
     * in the most specific class override those of the more general base
     * classes.
     * </p>
     *
     * @return A Collection of all the <code>SchemaAttributes</code>.
     */
    public Collection getAllAttributes() {
        Set all = new TreeSet(attributeComparator);

        addAllAttributes(all);

        return all;
    }

    /**
     * Add all the <code>SchemaAttributes</code> to the specified Set.
     * <p>
     * If there are any clashes then the <code>SchemaAttributes</code> defined
     * in the most specific class override those of the more general base
     * classes.
     * </p>
     *
     * @param all The Set of all the <code>SchemaAttributes</code>.
     */
    private void addAllAttributes(Set all) {

        for (Iterator i = getAttributes().iterator(); i.hasNext();) {
            SchemaAttribute attribute = (SchemaAttribute) i.next();
            if (!all.contains(attribute)) {
                all.add(attribute);
            }
        }

        for (Iterator i = getAttributeGroups().iterator(); i.hasNext();) {
            AttributeGroup group = (AttributeGroup) i.next();
            AttributeGroupInfo groupInfo
                    = (AttributeGroupInfo) group.getDefinition();

            AttributesStructureInfo groupAttributesStructureInfo
                    = groupInfo.getAttributesStructureInfo();

            groupAttributesStructureInfo.addAllAttributes(all);
        }
    }

    // Javadoc inherited from super class.
    protected String paramString() {
        return super.paramString()
                + ", abstractAPIAttributesClass=" + abstractAPIAttributesClass
                + ", naturalName=" + naturalName
                + ", apiAttributesClass=" + apiAttributesClass
                + ", apiBaseAttributeGroup=" + apiBaseAttributeGroup;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Dec-05	10845/1	pduffin	VBM:2005121511 Moved architecture files from CVS to MCS Depot

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
