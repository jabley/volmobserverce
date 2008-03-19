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
package com.volantis.mcs.runtime.configuration.project;



/**
 * Holds configuration information about a project's assets.
 * <p>
 * This corresponds to the mcs-config/projects/[default|project]/assets
 * and also to the mcs-config/assets sections of the configuration file.
 * element.
 */ 
public class AssetsConfiguration {

    /**
     * Specifies the base URL for assets belonging to the enclosing project.
     * <p>
     * It is used to resolve host relative server side asset URLs against 
     * after they have been from the asset and asset group meta data but 
     * before any custom AssetURLRewriters are executed.
     */ 
    private String baseUrl;

    /**
     * Audio asset configuration.
     */ 
    private AssetConfiguration audioAssets;

    /**
     * Dynamic visual asset configuration.
     */ 
    private AssetConfiguration dynamicVisualAssets;

    /**
     * Image asset configuration.
     */ 
    private AssetConfiguration imageAssets;

    /**
     * Script asset configuration.
     */ 
    private AssetConfiguration scriptAssets;

    /**
     * Text asset configuration.
     */ 
    private AssetConfiguration textAssets;

    /**
     * Returns the base url.
     */ 
    public String getBaseUrl() {
        return baseUrl;
    }

    /**
     * Sets the base url.
     */ 
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * Returns the audio asset configuration.
     */ 
    public AssetConfiguration getAudioAssets() {
        return audioAssets;
    }

    /**
     * Sets the audio asset configuration.
     */ 
    public void setAudioAssets(AssetConfiguration audioAssets) {
        this.audioAssets = audioAssets;
    }

    /**
     * Returns the dynamic visual asset configuration.
     */ 
    public AssetConfiguration getDynamicVisualAssets() {
        return dynamicVisualAssets;
    }

    /**
     * Sets the dynamic visual asset configuration.
     */ 
    public void setDynamicVisualAssets(AssetConfiguration dynamicVisualAssets) {
        this.dynamicVisualAssets = dynamicVisualAssets;
    }

    /**
     * Returns the image asset configuration.
     */ 
    public AssetConfiguration getImageAssets() {
        return imageAssets;
    }

    /**
     * Sets the image asset configuration.
     */ 
    public void setImageAssets(AssetConfiguration imageAssets) {
        this.imageAssets = imageAssets;
    }

    /**
     * Returns the script asset configuration.
     */ 
    public AssetConfiguration getScriptAssets() {
        return scriptAssets;
    }

    /**
     * Sets the script asset configuration.
     */ 
    public void setScriptAssets(AssetConfiguration scriptAssets) {
        this.scriptAssets = scriptAssets;
    }

    /**
     * Returns the text asset configuration.
     */ 
    public AssetConfiguration getTextAssets() {
        return textAssets;
    }

    /**
     * Sets the text asset configuration.
     */ 
    public void setTextAssets(AssetConfiguration textAssets) {
        this.textAssets = textAssets;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Nov-05	9990/1	ibush	VBM:2005102516 Enable Local and Remote Project Loading

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 09-Feb-04	2846/1	claire	VBM:2004011915 Refactoring URL handling

 26-Jan-04	2724/3	geoff	VBM:2004011911 Add projects to config (whoops - add javadoc)

 26-Jan-04	2724/1	geoff	VBM:2004011911 Add projects to config

 ===========================================================================
*/
