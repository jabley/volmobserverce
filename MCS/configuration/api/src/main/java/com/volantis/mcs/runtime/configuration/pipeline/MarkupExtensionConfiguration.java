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
package com.volantis.mcs.runtime.configuration.pipeline;

/**
 * Configuration data holder for the
 * <pipeline-configuration>/<markup-extensions>/<markup-extension> element
 */
public class MarkupExtensionConfiguration {

    /**
     * The element name associated with this markup extension
     */
    private String localName;

    /**
     * Namespace URI associated with this markup extension
     */
    private String namespaceURI;

    /**
     * Configuration for either a nested rule or process element
     */
    private PipelinePluginConfiguration pipelinePluginConfiguration;

    // javadoc unnecessary
    public String getLocalName() {
        return localName;
    }

    // javadoc unnecessary
    public void setLocalName(String localName) {
        this.localName = localName;
    }

    // javadoc unnecessary
    public String getNamespaceURI() {
        return namespaceURI;
    }

    // javadoc unnecessary
    public void setNamespaceURI(String namespaceURI) {
        this.namespaceURI = namespaceURI;
    }

    // javadoc unnecessary
    public PipelinePluginConfiguration getPipelinePluginConfiguration() {
        return pipelinePluginConfiguration;
    }

    // javadoc unnecessary
    public void setPipelinePluginConfiguration(
                PipelinePluginConfiguration pipelinePluginConfiguration) {
        this.pipelinePluginConfiguration = pipelinePluginConfiguration;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Jun-05	7418/1	doug	VBM:2005021505 Simplified pipeline initialization

 ===========================================================================
*/
