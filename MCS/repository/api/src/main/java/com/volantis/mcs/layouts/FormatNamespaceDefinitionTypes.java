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
package com.volantis.mcs.layouts;

import com.volantis.mcs.model.validation.integrity.DefinitionTypeBuilder;
import com.volantis.mcs.model.validation.integrity.DefinitionTypeHandler;
import com.volantis.mcs.model.validation.integrity.DefinitionType;
import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.mcs.model.validation.SourceLocation;
import com.volantis.mcs.model.validation.DiagnosticLevel;
import com.volantis.mcs.model.path.Path;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Collection;

/**
 * Manages the definition types that are associated with format namespaces.
 * <p>
 * Each format has a namespace, each namespace maps to a definition type.
 * <p>
 * These definition types are used to ensure that all formats of that type
 * have a unique name within the logical "scope" (either format or fragment).
 */
public class FormatNamespaceDefinitionTypes {

    static final Map formatNamespace2DefinitionType = new HashMap();
    static {
        DefinitionTypeBuilder builder = new DefinitionTypeBuilder();

        // Iterate through all the format types, creating a
        // definition type for each type's (unique) namespace.
        // We do this to try and ensure we only use the "real" namespaces,
        // however I think perhaps we need some more structure do this this
        // properly (eg FormatType has Empty, FormatNamespace has Container).
        // TODO: later: ensure we only add "real" namespaces.
        Iterator formatTypes = FormatType.iterator();
        while (formatTypes.hasNext()) {
            final FormatType formatType = (FormatType) formatTypes.next();
            FormatNamespace formatNamespace = formatType.getNamespace();
            if (formatNamespace2DefinitionType.get(formatNamespace) == null) {
                DefinitionTypeHandler handler =
                        new FormatNamespaceDefinitionTypeHandler(formatNamespace);
                builder.setDefinitionTypeHandler(handler);
                formatNamespace2DefinitionType.put(formatNamespace,
                        builder.getDefinitionType());
            }
        }
    }

    /**
     * Return a collection of definition types, one for each format's
     * namespace.
     *
     * @return
     */
    public static Collection getTypes() {

        return formatNamespace2DefinitionType.values();
    }

    /**
     * Return the definition type for the format type provided.
     *
     * @param formatNamespace the format type to get a definition type for,
     * @return the definition type for the namespace provided.
     */
    public static DefinitionType getType(
            final FormatNamespace formatNamespace) {

        DefinitionType definitionType = (DefinitionType)
                formatNamespace2DefinitionType.get(formatNamespace);

        return definitionType;
    }


    private static class FormatNamespaceDefinitionTypeHandler
            implements DefinitionTypeHandler {

        final FormatNamespace formatNamespace;

        public FormatNamespaceDefinitionTypeHandler(
                FormatNamespace formatNamespace) {
            this.formatNamespace = formatNamespace;
        }

        public void reportDuplicate(ValidationContext context,
                SourceLocation source, Path path, Object identifier) {
            context.addDiagnostic(source, path, DiagnosticLevel.ERROR,
                    context.createMessage("format-name-duplicate",
                            formatNamespace.getName(), identifier));
        }
    }

}
