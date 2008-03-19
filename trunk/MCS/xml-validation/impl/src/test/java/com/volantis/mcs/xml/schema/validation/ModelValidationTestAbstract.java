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

package com.volantis.mcs.xml.schema.validation;

import com.volantis.mcs.xml.schema.compiler.CompiledSchema;
import com.volantis.mcs.xml.schema.compiler.SchemaCompiler;
import com.volantis.mcs.xml.schema.compiler.SchemaCompilerFactory;
import com.volantis.mcs.xml.schema.model.AbstractSchema;
import com.volantis.mcs.xml.schema.model.ElementSchema;
import com.volantis.mcs.xml.schema.model.ElementType;
import com.volantis.mcs.xml.schema.model.Namespace;
import com.volantis.mcs.xml.schema.model.Schema;
import com.volantis.mcs.xml.schema.model.SchemaNamespaces;
import com.volantis.mcs.xml.schema.model.SchemaNamespacesBuilder;

public abstract class ModelValidationTestAbstract
        extends ValidationTestAbstract {

    private static final SchemaCompilerFactory SCHEMA_COMPILER_FACTORY =
            SchemaCompilerFactory.getDefaultInstance();

    private CompiledSchema COMPILED_SCHEMA;

    protected ModelValidationTestAbstract(Schema schema) {
        SchemaCompiler compiler =
                SCHEMA_COMPILER_FACTORY.createSchemaCompiler();
        compiler.addSchema(schema);
        COMPILED_SCHEMA = compiler.getCompiledSchema();
    }

    protected static class TestElements {

        /**
         * The namespace containing all these elements.
         */
        public static final Namespace NAMESPACE =
                new Namespace("test");

        /**
         * Get the element type for the specified local name in the current
         * namespace.
         *
         * @param localName The local name of the element.
         * @return The element type.
         */
        private static ElementType getElement(String localName) {
            return NAMESPACE.addElement(localName);
        }

        public static final ElementType A = getElement("a");
        public static final ElementType B = getElement("b");
        public static final ElementType C = getElement("c");
        public static final ElementType D = getElement("d");
        public static final ElementType E = getElement("e");
        public static final ElementType F = getElement("f");
        public static final ElementType G = getElement("g");
        public static final ElementType H = getElement("h");
        public static final ElementType I = getElement("i");
        public static final ElementType J = getElement("j");

    }

    public static class TestSchema
            extends AbstractSchema {

        protected ElementSchema a = createElementSchema(TestElements.A);
        protected ElementSchema b = createElementSchema(TestElements.B);
        protected ElementSchema c = createElementSchema(TestElements.C);
        protected ElementSchema d = createElementSchema(TestElements.D);
        protected ElementSchema e = createElementSchema(TestElements.E);
        protected ElementSchema f = createElementSchema(TestElements.F);
        protected ElementSchema g = createElementSchema(TestElements.G);
        protected ElementSchema h = createElementSchema(TestElements.H);
        protected ElementSchema i = createElementSchema(TestElements.I);
        protected ElementSchema j = createElementSchema(TestElements.J);

        public TestSchema() {
            // Make sure that they all have a content model.
            a.setContentModel(EMPTY);
            b.setContentModel(EMPTY);
            c.setContentModel(EMPTY);
            d.setContentModel(EMPTY);
            e.setContentModel(EMPTY);
            f.setContentModel(EMPTY);
            g.setContentModel(EMPTY);
            h.setContentModel(EMPTY);
            i.setContentModel(EMPTY);
            j.setContentModel(EMPTY);
        }
    }

    private static final SchemaNamespaces TEST_NAMESPACES;

    static {
        SchemaNamespacesBuilder namespaces = new SchemaNamespacesBuilder();
        namespaces.addNamespace(TestElements.NAMESPACE);
        TEST_NAMESPACES = namespaces;
    }


    protected SchemaNamespaces getSchemaNamespaces() {
        return TEST_NAMESPACES;
    }


    protected CompiledSchema getCompiledSchema() {
        return COMPILED_SCHEMA;
    }
}
