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
 * $Header:$
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 16-May-03    Geoff           VBM:2003042904 - Created; token table values. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbsax;

/**
 * Token table values.
 * <p>
 * This can be used to configure the standard tag, attribute start and 
 * attribute value Factories with appropriate values, in bulk.
 * <p>
 * Client code which needs to serialise XML to WBXML needs to be able to 
 * identify substrings according to this information, before it uses the 
 * Factories, so this it is designed to enable client code to extract the 
 * information as well. 
 */ 
public interface TokenTable {

    /**
     * The copyright statement.
     */
    String mark = "(c) Volantis Systems Ltd 2003. ";
    
    /**
     * Return the version of the standard that these tokens implement.
     * 
     * @return the version code
     */ 
    VersionCode getVersion();
    
    /**
     * Return the public id of the DTD that these tokens implement.
     *  
     * @return the public ID of the DTD
     */ 
    PublicIdCode getPublicId();
    
    /**
     * Register token table information for tags, via the tag registrar
     * provided.
     *  
     * @param tags the tag registrar which will be used to register each tag's
     *      token and name.
     */
    void registerTags(ElementRegistrar tags);

    /**
     * Register token table information for attribute starts, via the attribute
     * start registrar provided.
     *  
     * @param attrStarts the attribute start registrar which will be used to 
     *      register each attribute starts's token and name, or token, name 
     *      and value prefix.
     */
    void registerAttrStarts(AttributeStartRegistrar attrStarts);

    /**
     * Register token table information for attribute values, via the attribute
     * value registrar provided.
     *  
     * @param attrValues the attribute value registrar which will be used to 
     *      register each attribute value's token and value part.
     */
    void registerAttrValues(AttributeValueRegistrar attrValues);
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 14-Jul-03	790/3	geoff	VBM:2003070404 manual merge and just copy wbsax from metis to ensure we got everything

 11-Jul-03	781/1	geoff	VBM:2003070404 first clean up of WBSAX; javadocs and todos

 27-Jun-03	559/1	geoff	VBM:2003060607 make WML use protocol configuration again

 ===========================================================================
*/
