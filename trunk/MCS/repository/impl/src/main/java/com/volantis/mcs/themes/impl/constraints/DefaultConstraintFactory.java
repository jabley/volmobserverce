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
package com.volantis.mcs.themes.impl.constraints;

import com.volantis.mcs.themes.constraints.ConstraintFactory;
import com.volantis.mcs.themes.constraints.Contains;
import com.volantis.mcs.themes.constraints.ContainsWord;
import com.volantis.mcs.themes.constraints.EndsWith;
import com.volantis.mcs.themes.constraints.Equals;
import com.volantis.mcs.themes.constraints.MatchesLanguage;
import com.volantis.mcs.themes.constraints.Set;
import com.volantis.mcs.themes.constraints.StartsWith;

/**
 */
public class DefaultConstraintFactory extends ConstraintFactory {

    public Contains createContains() {
        return new ContainsImpl();
    }

    public ContainsWord createContainsWord() {
        return new ContainsWordImpl();
    }

    public EndsWith createEndsWith() {
        return new EndsWithImpl();
    }

    public MatchesLanguage createMatchesLanguage() {
        return new MatchesLanguageImpl();
    }

    public Equals createEquals() {
        return new EqualsImpl();
    }

    public Set createSet() {
        return new SetImpl();
    }

    public StartsWith createStartsWith() {
        return new StartsWithImpl();
    }
}
