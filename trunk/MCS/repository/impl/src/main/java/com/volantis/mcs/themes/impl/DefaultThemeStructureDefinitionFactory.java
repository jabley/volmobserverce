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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.themes.impl;

import com.volantis.mcs.themes.ThemeStructureDefinitionFactory;
import com.volantis.mcs.themes.impl.ThemeStructureDefinition;
import com.volantis.shared.jibx.ContentUnmarshaller;
import com.volantis.shared.content.BinaryContentInput;
import com.volantis.shared.throwable.ExtendedRuntimeException;

import java.util.Map;
import java.io.InputStream;
import java.io.IOException;

import org.jibx.runtime.JiBXException;

/**
 */
public class DefaultThemeStructureDefinitionFactory
        extends ThemeStructureDefinitionFactory {

    private final Map themeStructureDefinitionMap;

    private final ContentUnmarshaller contentUnmarshaller =
        new ContentUnmarshaller(ThemeStructureDefinition.class);

    public DefaultThemeStructureDefinitionFactory() {
        Object readObject = null;

        final String resourceName = "com/volantis/mcs/themes/ThemeStructure.xml";
        InputStream inputStream = getClass().getClassLoader().
                getResourceAsStream(resourceName);
        if (inputStream == null) {
            throw new IllegalStateException("Cannot load resource " +
                    resourceName);
        }

        try {
            BinaryContentInput content = new BinaryContentInput(inputStream);
            readObject = contentUnmarshaller.unmarshallContent(content,
                    resourceName);

        } catch (JiBXException e) {
            throw new ExtendedRuntimeException(
                    "Error loading ThemeStructure.xml", e);
        } catch (IOException e) {
            throw new ExtendedRuntimeException(
                    "Error loading ThemeStructure.xml", e);
        }

        themeStructureDefinitionMap =
                ((com.volantis.mcs.themes.impl.ThemeStructureDefinition) readObject).getPropertiesAsMap();
    }

    public Map getThemeStructureDefinitionMap() {
        return themeStructureDefinitionMap;
    }
}
