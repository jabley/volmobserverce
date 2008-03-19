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
 * $Header: /src/mps/com/volantis/testtools/config/ChannelConfigBuilder.java,v 1.2 2003/03/20 10:15:36 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 20-Feb-03    Mat             VBM:2003022002 - Created. The base class for 
 *                              ChannelConfigBuilderClasses
 * 19-Mar-03    Geoff           VBM:2003032001 - Refactored to use external
 *                              ConfigValues.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.config;

/**
 * Class to provide methods common to ChannelConfigBuilders
 */
public abstract class ChannelConfigBuilder {

    ConfigValueChannel value;
    
    public ChannelConfigBuilder(ConfigValueChannel value) {
        this.value = value;
    }
    
    protected String renderChannelConfig() {
        StringBuffer config = new StringBuffer("<channel ");
        addAttribute(config, "name", value.name);
        addAttribute(config, "class", value.channelClass);
        config.append(">");
        renderChannelAttributes(config);
        config.append("</channel>");
        return config.toString();
    }

    /**
     * Subclasses should override this method and call {@link #addAttribute} 
     * to add the individual attributes they require.
     * 
     * @param buffer the buffer to add the attributes to. 
     */ 
    protected abstract void renderChannelAttributes(StringBuffer buffer);

    
    /* 
     * Convenience method to add an attribute to the string being build.
     *
     * @param buffer The StringBuffer being built
     * @param name The attribute name
     * @param value The attribute value
     */
    protected void addAttribute(StringBuffer buffer, String name, Object value) {
        if(value != null) {
            buffer.append(name).append("=\"").
            append(value).append("\"").append('\n');
        }
    }
    /* 
     * Convenience method to add an argument element to the string being build.
     *
     * @param buffer The StringBuffer being built
     * @param name The attribute name
     * @param value The attribute value
     */
    protected void addArgumentElement(StringBuffer buffer, String name, Object value) {
        if(value != null) {
            
            buffer.append("<argument name=\"").append(name).append("\" value=\"").
            append(value).append("\"/>\n");
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Jun-04	121/1	ianw	VBM:2004060111 Made to work with main 3.2 MCS stream

 ===========================================================================
*/
