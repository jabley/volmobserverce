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
package com.volantis.mcs.build.themes.definitions.impl;

import com.volantis.mcs.build.themes.definitions.Rules;

import java.util.Map;
import java.util.HashMap;

public class RulesImpl implements Rules {

    private final Map ruleset = new HashMap();


    public Map getRuleSet() {
        return ruleset;
    }

    public void addRule(String from, String to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException(
                "Null arguments not allowed; "+from+", "+to);
        }
        ruleset.put(from, to);
    }
}
