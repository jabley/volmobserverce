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

package com.volantis.mcs.model.impl.validation.integrity;

import com.volantis.mcs.model.path.Path;
import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.mcs.model.validation.SourceLocation;
import com.volantis.mcs.model.validation.integrity.DefinitionScope;
import com.volantis.mcs.model.validation.integrity.DefinitionTypeHandler;
import com.volantis.mcs.model.validation.integrity.DefinitionType;

import java.util.HashMap;
import java.util.Map;

public class DefinitionScopeImpl
        implements DefinitionScope {

    private final DefinitionTypeHandler handler;
    private final Map definitions;

    public DefinitionScopeImpl(DefinitionType definitionType) {
        handler = definitionType.getHandler();
        definitions = new HashMap();
    }

    public void define(
            ValidationContext context, SourceLocation source, Object identifier) {

        if (context == null) {
            throw new IllegalArgumentException("context cannot be null");
        }
        if (identifier == null) {
            throw new IllegalArgumentException("identifier cannot be null");
        }

        Path path = context.getCurrentPath();

        Definition definition = (Definition) definitions.get(identifier);
        if (definition == null) {
            definition = new Definition(source, path);
            definitions.put(identifier, definition);
        } else if (definition.getPath().getAsString().equals(path.getAsString())) {
            // Redefining the same object twice so just ignore it.
        } else {
            // Duplicate definitions found. If the definition is not already
            // marked as duplicated then mark it and report it as a duplicate.
            if (!definition.isDuplicated()) {
                SourceLocation firstSource = definition.getSource();
                Path firstPath = definition.getPath();

                reportDuplicate(context, firstSource, firstPath, identifier);
                definition.setDuplicated(true);
            }

            // Also, report the new source as a duplicate.
            reportDuplicate(context, source, path, identifier);
        }
    }

    private void reportDuplicate(
            ValidationContext context, SourceLocation source, Path path,
            Object identifier) {

        handler.reportDuplicate(context, source, path, identifier);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Nov-05	10287/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 11-Nov-05	10199/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 26-Oct-05	9961/3	pduffin	VBM:2005101811 Added stack of objects in validation context

 26-Oct-05	9961/1	pduffin	VBM:2005101811 Improved validation, checked for duplicate devices, added support for validation in the runtime

 ===========================================================================
*/
