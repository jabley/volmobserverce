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
package com.volantis.mcs.build.themes.definitions.values.impl;

import com.volantis.mcs.build.themes.definitions.values.ValueSource;

import java.io.PrintStream;
import java.util.Map;
import java.util.Iterator;

public class ComputedValueSource implements ValueSource {

    private final String propertyName;
    private final Map ruleSet;

    public ComputedValueSource(String propertyName, Map rules) {
        this.propertyName = propertyName;
        this.ruleSet = rules;
    }

    public void writeSetter(String indent, PrintStream out, String variable) {
        out.print(indent);
        out.print(variable);
        out.print(".setPropertyInitialValue(");
        out.print("\"" + propertyName + "\"");
        out.println(");");

        writeRuleSet(indent, out);

        out.print(indent);
        out.print(variable);
        out.println(".setInitialValueRules(rules);");
    }

    private void writeRuleSet(String indent, PrintStream out) {

        out.print(indent);
        out.println("rules = new HashMap();");

        Iterator ruleIterator = ruleSet.keySet().iterator();
        while(ruleIterator.hasNext()) {
            String from = (String) ruleIterator.next();
            String to = (String) ruleSet.get(from);

            out.print(indent);
            out.print("rules.put(");
            out.print("StyleKeywords." + from.toUpperCase());
            out.print(", ");
            out.print("StyleKeywords." + to.toUpperCase());
            out.println(");");
        }
    }
}
