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
package com.volantis.mcs.runtime.configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Container for the servlet-filter configuration read from the mariner
 * configuration file.
 */
public class ServletFilterConfiguration {
    /**
     * A list of ExcludeDeviceConfigurations that the servlet filter uses.
     */
    private List excludedDevices = new ArrayList();

    /**
     * A list of MimeTypeConfigurations that the servlet filter uses.
     */
    private List mimeTypes = new ArrayList();

    /**
     * The rendered page cache configuration
     */
    private RenderedPageCacheConfiguration renderedPageCacheConfig = null;

    /**
     * The session identifier. Note that this should contain the session ID
     * name as it would appear in the body of the document, not the header,
     * since this is where we will be substituting it.
     */
    private String jsessionidName = null;

    /**
     * Adds an excluded device.
     * @param device the device to add
     */
    public void addExcludedDevice(ExcludeDeviceConfiguration device) {
        excludedDevices.add(device);
    }

    /**
     * Adds an XDIME mime type.
     * @param type the mime type to add
     */
    public void addMimeType(MimeTypeConfiguration type) {
        mimeTypes.add(type);
    }

    /**
     * Get the list of excluded devices.
     * @return the list of excluded devices
     */
    public List getExcludedDevices() {
        List devices = new ArrayList();
        for (int i = 0; i < excludedDevices.size(); i++) {
            ExcludeDeviceConfiguration edc =
                    (ExcludeDeviceConfiguration) excludedDevices.get(i);
            devices.add(edc.getName());
        }
        return devices;
    }


    /**
     * Get the list of XDIME mime types.
     * @return the list of mime types
     */
    public List getMimeTypes() {
        List types = new ArrayList();
        for (int i = 0; i < mimeTypes.size(); i++) {
            MimeTypeConfiguration mtc =
                    (MimeTypeConfiguration) mimeTypes.get(i);
            types.add(mtc.getValue());
        }
        return types;
    }

    /**
     * Get the rendered page cache configuration.
     *
     * @return The rendered page cache configuration
     */
    public RenderedPageCacheConfiguration getRenderedPageCacheConfig() {
        return renderedPageCacheConfig;
    }

    /**
     * Set the rendered page cache configuration.
     *
     * @param renderedPageCacheConfig The new rendered page cache configuration
     */
    public void setRenderedPageCacheConfig(RenderedPageCacheConfiguration renderedPageCacheConfig) {
        this.renderedPageCacheConfig = renderedPageCacheConfig;
    }

    /**
     * Get the session ID name.
     * @return The session ID name
     */
    public String getJsessionIdName() {
        return jsessionidName;
    }

    /**
     * Set the session ID name.
     *
     * @param jsessionidName The new session ID name
     */
    public void setJsessionIdName(String jsessionidName) {
        this.jsessionidName = jsessionidName;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Feb-05	6786/4	adrianj	VBM:2005012506 Rendered page cache rework

 11-Feb-05	6786/2	adrianj	VBM:2005012506 Added rendered page cache

 11-Jan-05	6413/1	pcameron	VBM:2004120702 Servlet filter integration for XDIME

 ===========================================================================
*/
