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
package com.volantis.devrep.device.impl.integration.xml;

import com.volantis.devrep.device.api.xml.definitions.Boolean;
import com.volantis.devrep.device.api.xml.definitions.Category;
import com.volantis.devrep.device.api.xml.definitions.DefinitionSet;
import com.volantis.devrep.device.api.xml.definitions.Field;
import com.volantis.devrep.device.api.xml.definitions.Int;
import com.volantis.devrep.device.api.xml.definitions.OrderedSet;
import com.volantis.devrep.device.api.xml.definitions.Policy;
import com.volantis.devrep.device.api.xml.definitions.Range;
import com.volantis.devrep.device.api.xml.definitions.Selection;
import com.volantis.devrep.device.api.xml.definitions.Structure;
import com.volantis.devrep.device.api.xml.definitions.Text;
import com.volantis.devrep.device.api.xml.definitions.Type;
import com.volantis.devrep.device.api.xml.definitions.TypeContainer;
import com.volantis.devrep.device.api.xml.definitions.TypeDeclaration;
import com.volantis.devrep.device.api.xml.definitions.UnorderedSet;
import com.volantis.devrep.device.impl.integration.ResourceJiBXTester;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import org.jibx.runtime.JiBXException;

import java.io.IOException;
import java.util.Iterator;

public class DefinitionsTestCase extends TestCaseAbstract {

    private ResourceJiBXTester tester = new ResourceJiBXTester(
            HierarchyTestCase.class);

    public void testUnmarshall() throws JiBXException, IOException {

        DefinitionSet definitionSet = (DefinitionSet) tester.unmarshall(
                DefinitionSet.class, "DefinitionsTest.xml");

        StringBuffer output = new StringBuffer();
        dumpDefinitions(definitionSet, output);
        String actual = output.toString();
        System.out.println(actual);

        String expected = tester.getResource("DefinitionsTest.txt");

        assertEquals("", expected, actual);
    }

    private void dumpDefinitions(DefinitionSet definitionSet, StringBuffer buffer) {
        Iterator types = definitionSet.types();
        while (types.hasNext()) {
            TypeDeclaration typeDeclaration = (TypeDeclaration) types.next();
            dumpTypeDeclaration(typeDeclaration, buffer);

        }
        Iterator categories = definitionSet.categories();
        while (categories.hasNext()) {
            Category category = (Category) categories.next();
            dumpCategory(category, buffer);
        }

    }

    private void dumpTypeDeclaration(TypeDeclaration typeDeclaration,
            StringBuffer buffer) {
        buffer.append("Type Declaration:\n");
        buffer.append("  name=" + typeDeclaration.getName() + "\n");
        Type type = typeDeclaration.getType();
        dumpType(type, "  ", buffer);

    }

    private void dumpCategory(Category category, StringBuffer buffer) {
        buffer.append("Category:\n");
        buffer.append("  name=" + category.getName() + "\n");
        Iterator policies = category.policies();
        while (policies.hasNext()) {
            Policy policy = (Policy) policies.next();
            dumpPolicy(policy, buffer);
        }
    }

    private void dumpPolicy(Policy policy, StringBuffer buffer) {
        buffer.append("  Policy:\n");
        buffer.append("    name=" + policy.getName() + "\n");
        if (policy.getCCPPVocabulary() != null) {
            buffer.append("    ccppVocabulary=" + policy.getCCPPVocabulary() + "\n");
        }
        if (policy.getUAProfAttribute() != null) {
            buffer.append("    uaProfAttribute=" + policy.getUAProfAttribute() + "\n");
        }

        TypeContainer typeContainer = policy.getTypeContainer();
        if (typeContainer != null) {
            dumpTypeContainer(typeContainer, buffer);
        }
    }

    private void dumpTypeContainer(TypeContainer typeContainer,
            StringBuffer buffer) {

        buffer.append("    Type Container:\n");
        if (typeContainer.getName() != null) {
            buffer.append("      name=" + typeContainer.getName() + "\n");
        }
        if (typeContainer.getType() != null) {
            Type type = typeContainer.getType();
            dumpType(type, "      ", buffer);
        }

    }

    private void dumpType(Type type, String indent, StringBuffer buffer) {
        if (type instanceof Int) {
            dumpSimple(buffer, indent, "Int");
        } else if (type instanceof Text) {
            dumpSimple(buffer, indent, "Text");
        } else if (type instanceof Boolean) {
            dumpSimple(buffer, indent, "Text");
        } else if (type instanceof Text) {
            dumpSimple(buffer, indent, "Text");
        } else if (type instanceof Range) {
            dumpRange(buffer, indent, (Range) type);
        } else if (type instanceof UnorderedSet) {
            dumpUnorderedSet(buffer, indent, (UnorderedSet) type);
        } else if (type instanceof OrderedSet) {
            // todo: test me
            dumpOrderedSet(buffer, indent, (OrderedSet) type);
        } else if (type instanceof Selection) {
            dumpSelection(buffer, indent, (Selection) type);
        } else if (type instanceof Structure) {
            dumpStructure(buffer, indent, (Structure) type);
        } else {
//            throw new IllegalStateException("unexpected Type " + type);
            buffer.append("    UNEXPECTED TYPE=" + type + "\n");
        }
    }

    private void dumpSimple(StringBuffer buffer, String indent,
            final String name) {
        buffer.append(indent + "type=" + name + "\n");
    }

    private void dumpRange(StringBuffer buffer, String indent, Range range) {
        buffer.append(indent + "type=Range\n");
        buffer.append(indent + "  minInclusive=" + range.getMinInclusive() + "\n");
        buffer.append(indent + "  maxInclusive=" + range.getMaxInclusive() + "\n");
    }

    private void dumpSelection(StringBuffer buffer, String indent, Selection selection) {
        buffer.append(indent + "type=Selection\n");
        Iterator policies = selection.keywords();
        while (policies.hasNext()) {
            String keyword = (String) policies.next();
            buffer.append(indent + "  keyword=" + keyword + "\n");
        }
    }

    private void dumpStructure(StringBuffer buffer, String indent,
            Structure structure) {
        buffer.append(indent + "type=Structure\n");
        Iterator fields = structure.fields();
        while (fields.hasNext()) {
            Field field = (Field) fields.next();
            buffer.append(indent + "  Field:\n");
            buffer.append(indent + "    name=" + field.getName() + "\n");
            dumpType(field.getType(), indent + "    ", buffer);
        }
    }


    private void dumpOrderedSet(StringBuffer buffer, String indent, OrderedSet set) {
        buffer.append(indent + "type=OrderedSet\n");
        dumpType(set.getType(), indent + "  ", buffer);
    }

    private void dumpUnorderedSet(StringBuffer buffer, String indent, UnorderedSet set) {
        buffer.append(indent + "type=UnorderedSet\n");
        dumpType(set.getType(), indent + "  ", buffer);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 24-Nov-05	10404/1	geoff	VBM:2005112301 Implement meta data for JiBX device repository accessor

 ===========================================================================
*/
