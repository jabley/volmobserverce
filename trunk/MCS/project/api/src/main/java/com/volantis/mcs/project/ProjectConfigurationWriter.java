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
package com.volantis.mcs.project;

import com.volantis.mcs.runtime.configuration.project.RuntimeProjectConfiguration;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.JiBXException;

import java.io.OutputStream;
import java.io.Writer;

/**
 * Writer class for project containers
 */
public class ProjectConfigurationWriter {

    /**
     * Write the ProjectConfiguration to the writer.
     *
     * @param writer   writer for the project container
     * @throws org.jibx.runtime.JiBXException
     */
    public void writeProject(RuntimeProjectConfiguration configuration,
                             Writer writer)
            throws JiBXException {

        IMarshallingContext uctx = getMarshallingContext();

        uctx.marshalDocument(configuration, "UTF-8", null, writer);
    }

    /**
     * Write the ProjectConfiguration to the output stream.
     *
     * @param stream   stream for the project container
     * @throws org.jibx.runtime.JiBXException
     */
    public void writeProject(RuntimeProjectConfiguration configuration,
                             OutputStream stream)
            throws JiBXException {

        IMarshallingContext uctx = getMarshallingContext();

        uctx.marshalDocument(configuration, "UTF-8", null, stream);
    }

    private IMarshallingContext getMarshallingContext()
            throws JiBXException {
        IBindingFactory bfact =
                BindingDirectory.getFactory(RuntimeProjectConfiguration.class);

        IMarshallingContext uctx = bfact.createMarshallingContext();
        return uctx;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Nov-05	9990/1	ibush	VBM:2005102516 Enable Local and Remote Project Loading

 ===========================================================================
*/
