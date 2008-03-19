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

import com.volantis.mcs.accessors.xml.jibx.JiBXReader;
import com.volantis.mcs.runtime.configuration.project.RuntimeProjectConfiguration;
import com.volantis.shared.content.BinaryContentInput;
import com.volantis.shared.content.TextContentInput;
import com.volantis.xml.schema.validator.SchemaValidator;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/**
 * Reader class for project containers
 */
public class ProjectConfigurationReader {

    private static SchemaValidator schemaValidator = new SchemaValidator();
    static {
        schemaValidator.addSchema(ProjectSchemas.MCS_3_5_PROJECT);
    }

    private final JiBXReader jibxReader;

    public ProjectConfigurationReader() {
        this.jibxReader = new JiBXReader(RuntimeProjectConfiguration.class,
                schemaValidator);
    }

    /**
     * Create the ProjectConfiguration using the given reader.
     *
     * @param reader   reader for the project container
     * @param systemID the location from which the configuration is being read.
     * @return
     * @throws IOException
     */
    public RuntimeProjectConfiguration readProject(
            Reader reader, final String systemID)
            throws IOException {

        RuntimeProjectConfiguration readObject = (RuntimeProjectConfiguration)
                jibxReader.read(new TextContentInput(reader), systemID);

        return readObject;
    }

    /**
     * Create the ProjectConfiguration using the given input stream.
     *
     * @param stream   stream for the project container
     * @param url the location from which the configuration is being read.
     * @return
     * @throws IOException
     */
    public RuntimeProjectConfiguration readProject(
            InputStream stream, final String url)
            throws IOException {

        RuntimeProjectConfiguration readObject = (RuntimeProjectConfiguration)
                jibxReader.read(new BinaryContentInput(stream), url);

        return readObject;
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
