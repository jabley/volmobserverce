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
 * $Header: /src/voyager/com/volantis/mcs/build/marlin/ElementInfo.java,v 1.5 2003/02/11 12:50:17 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 19-Dec-01    Paul            VBM:2001120506 - Created.
 * 14-Jan-02    Paul            VBM:2002011414 - Moved some helper code from
 *                              ParseMarinerSchema into GenerateUtilities.
 * 31-Jan-02    Paul            VBM:2001122105 - Added must set tag name
 *                              attribute.
 * 19-Feb-02    Paul            VBM:2001100102 - Added hasJspExtraInfo
 *                              attribute.
 * 12 Mar 02    Steve           VBM:2002022203 - Added maml attributes
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 02-Apr-02    Mat             VBM:2002022009 - Changed PAPI references to API
 *                              so that other generators (eg IMDAPI) can use
 *                              the same names.
 * 16-May-02    Paul            VBM:2002032501 - Moved from the
 *                              com.volantis.mcs.build package.
 * 20-May-02    Paul            VBM:2002032501 - Added array of extra info
 *                              class names indexed by jsp version.
 * 22-Nov-02    Paul            VBM:2002112214 - Removed unused references to
 *                              maml specific properties and renamed the others
 *                              to make them marlin specific.
 * 22-Nov-02    Geoff           VBM:2002111504 - Modified 
 *                              createAttributesStructure to pass the tag name
 *                              into the AttributesStructureInfo, cleaned up
 *                              some Javadoc.
 * 11-Feb-03    Ian             VBM:2003020607 - Ported Metis changes to add
 *                              setJspProcessingType and getJspProcessingType.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.build.marlin;

import com.volantis.mcs.build.GenerateUtilities;
import com.volantis.mcs.build.parser.ElementDefinition;
import com.volantis.mcs.build.parser.Scope;

import java.util.StringTokenizer;

/**
 * This class adds extra information to the
 * <code>ElementDefinition</code> which is needed to automatically
 * generate code.
 */
public class ElementInfo
        extends ElementDefinition {

    /**
     * The base APIElement class to use when generating this element.
     */
    private String baseAPIElementClass;

    /**
     * The prefix which is generated from the natural name, this is used if the
     * prefix has not been explicitly set.
     */
    private String generatedPrefix;

    /**
     * Flag which indicates whether a API attributes class should be generated
     * for this element.
     */
    private boolean generateAPIAttributes;

    /**
     * Flag which indicates whether a API element class should be generated
     * for this element.
     */
    private boolean generateAPIElement;

    /**
     * Flag which indicates whether a Marlin element class should be generated
     * for this element.
     */
    private boolean generateMarlinElementHandler;

    /**
     * Flag which controls whether the api element has to set the tag name of
     * the protocol attributes.
     */
    private boolean mustSetTagName;

    /**
     * The natural name of this element.
     */
    private String naturalName;

    /**
     * The name to use for this element's api element class.
     */
    private String apiElementClass;

    /**
     * The name to use for this element's api attributes class.
     */
    private String apiAttributesClass;

    /**
     * Flag which indicates whether a API element class already exists
     * for this element.
     */
    private boolean apiElementExists;

    /**
     * The prefix which has been explicitly set, this is used to generate
     * some of the others fields if they have not been explicitly set.
     */
    private String prefix;

    /**
     * The name of the protocol attributes class, this is usually generated
     * from the class prefix but where there are inconsistencies this can
     * be explicitly set.
     */
    private String protocolAttributesClass;

    /**
     * Create a new <code>ElementInfo</code>.
     *
     * @param scope The scope within which this object belongs.
     * @param name  The name of the element.
     */
    public ElementInfo(Scope scope, String name) {
        super(name, scope);

        generateAPIElement = true;
        generateMarlinElementHandler = true;
        generateAPIAttributes = true;
    }

    /**
     * Get the extended attributes structure.
     *
     * @return The extended attributes structure.
     */
    public AttributesStructureInfo getAttributesStructureInfo() {
        return (AttributesStructureInfo) getComplexType()
                .getAttributesStructure();
    }

    /**
     * Set the name of the base API element class.
     *
     * @param baseAPIElementClass The name of the base API element class.
     */
    public void setBaseAPIElementClass(String baseAPIElementClass) {
        this.baseAPIElementClass = baseAPIElementClass;
    }

    /**
     * Get the name of the API element class.
     *
     * @return The name of the API element class.
     */
    public String getBaseAPIElementClass() {
        return baseAPIElementClass;
    }

    /**
     * Control whether a APIAttributes class should be generated for this
     * element.
     *
     * @param generateAPIAttributes True if a APIAttributes class should be
     *                              generated and false otherwise.
     */
    public void setGenerateAPIAttributes(boolean generateAPIAttributes) {
        this.generateAPIAttributes = generateAPIAttributes;
    }

    /**
     * Check whether a APIAttributes class should be generated for this
     * element.
     *
     * @return True if a APIAttributes class should be generated and
     *         false otherwise.
     */
    public boolean getGenerateAPIAttributes() {
        return generateAPIAttributes;
    }

    /**
     * Control whether a APIElement class should be generated for this element.
     *
     * @param generateAPIElement True if a APIElement class should be
     *                           generated and false otherwise.
     */
    public void setGenerateAPIElement(boolean generateAPIElement) {
        this.generateAPIElement = generateAPIElement;
    }

    /**
     * Check whether a APIElement class should be generated for this element.
     *
     * @return True if a APIElement class should be generated and
     *         false otherwise.
     */
    public boolean getGenerateAPIElement() {
        return generateAPIElement;
    }

    /**
     * Control whether MarlinElement class should be generated for this element.
     *
     * @param generateMarlinElementHandler True if a MarlinElement class should
     *                                     be generated and false otherwise.
     */
    public void setGenerateMarlinElementHandler(
            boolean
            generateMarlinElementHandler) {
        this.generateMarlinElementHandler = generateMarlinElementHandler;
    }

    /**
     * Check whether a MarlinElement class should be generated for this element.
     *
     * @return True if a MarlinElement class should be generated and
     *         false otherwise.
     */
    public boolean getGenerateMarlinElementHandler() {
        return generateMarlinElementHandler;
    }

    /**
     * Set the mustSetTagName property.
     *
     * @param mustSetTagName The mustSetTagName property.
     */
    public void setMustSetTagName(boolean mustSetTagName) {
        this.mustSetTagName = mustSetTagName;
    }

    /**
     * Get the mustSetTagName property.
     *
     * @return The mustSetTagName property.
     */
    public boolean getMustSetTagName() {
        return mustSetTagName;
    }

    /**
     * Set the natural name of the element.
     *
     * @param naturalName The natural name of the element.
     */
    public void setNaturalName(String naturalName) {
        this.naturalName = naturalName;

        StringBuffer prefixBuffer = new StringBuffer();
        StringTokenizer tokenizer = new StringTokenizer(naturalName, " ");
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            token = GenerateUtilities.getTitledString(token);
            prefixBuffer.append(token);
        }
        generatedPrefix = prefixBuffer.toString();

        // Make sure that the attributes structure info is updated.
        getAttributesStructureInfo().setNaturalName(naturalName);
    }

    /**
     * Get the natural name of the element.
     *
     * @return The natural name of the element.
     */
    public String getNaturalName() {
        return naturalName;
    }

    /**
     * Get the name of the Marlin element class.
     *
     * @return The name of the Marlin element class.
     */
    public String getMarlinElementClass() {
        return "Marlin" + getPrefix() + "Element";
    }

    /**
     * Set the name of the API element class.
     *
     * @param apiElementClass The name of the API element class.
     */
    public void setAPIElementClass(String apiElementClass) {
        this.apiElementClass = apiElementClass;
    }

    /**
     * Get the name of the API element class.
     *
     * @return The name of the API element class.
     */
    public String getAPIElementClass() {
        if (apiElementClass == null) {
            return getPrefix() + "Element";
        } else {
            return apiElementClass;
        }
    }

    /**
     * Specify whether a APIElement class already exists for this element.
     *
     * @param apiElementExists True if a APIElement class already exists
     *                         and false otherwise.
     */
    public void setAPIElementExists(boolean apiElementExists) {
        this.apiElementExists = apiElementExists;
    }

    /**
     * Check whether a APIElement class already exists for this element.
     *
     * @return True if a APIElement class already exists and
     *         false otherwise.
     */
    public boolean getAPIElementExists() {
        return apiElementExists;
    }

    /**
     * Set the prefix of the element.
     *
     * @param prefix The prefix of the element.
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * Get the prefix of the element.
     *
     * @return The prefix of the element.
     */
    protected String getPrefix() {
        if (prefix == null) {
            return generatedPrefix;
        } else {
            return prefix;
        }
    }

    /**
     * Set the protocol attributes class name.
     *
     * @param protocolAttributesClass The protocol attributes class name.
     */
    public void setProtocolAttributesClass(String protocolAttributesClass) {
        this.protocolAttributesClass
                = "com.volantis.mcs.protocols." + protocolAttributesClass;
    }

    /**
     * Get the protocol attributes class name.
     *
     * @return The protocol attributes class name.
     */
    public String getProtocolAttributesClass() {
        if (protocolAttributesClass == null) {
            return "com.volantis.mcs.protocols." + getPrefix() + "Attributes";
        } else {
            return protocolAttributesClass;
        }
    }

    /**
     * Get the protocol method suffix.
     *
     * @return The protocol method suffix.
     */
    public String getProtocolMethodSuffix() {
        return getPrefix();
    }

    public String getAPIAttributesClass() {
        return apiAttributesClass;
    }

    public void setAPIAttributesClass(String apiAttributesClass) {
        this.apiAttributesClass = apiAttributesClass;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Dec-05	10845/1	pduffin	VBM:2005121511 Moved architecture files from CVS to MCS Depot

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 06-Dec-04	5800/1	ianw	VBM:2004090605 New Build system

 ===========================================================================
*/
