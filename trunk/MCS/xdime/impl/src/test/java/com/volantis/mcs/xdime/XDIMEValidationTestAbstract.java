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

package com.volantis.mcs.xdime;

import com.volantis.mcs.xml.schema.validation.ValidationTestAbstract;
import com.volantis.mcs.xml.schema.model.SchemaNamespaces;
import com.volantis.mcs.xml.schema.compiler.CompiledSchema;
import com.volantis.mcs.xdime.schema.XDIME2Elements;
import com.volantis.mcs.xdime.validation.XDIME2CompiledSchema;

public abstract class XDIMEValidationTestAbstract
        extends ValidationTestAbstract {

    protected SchemaNamespaces getSchemaNamespaces() {
        return XDIME2Elements.getDefaultInstance();
    }

    protected CompiledSchema getCompiledSchema() {
        return XDIME2CompiledSchema.getCompiledSchema();
    }
}
