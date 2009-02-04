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

package com.volantis.mcs.runtime.project;

import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.mcs.context.BaseURLProvider;
import com.volantis.synergetics.cornerstone.stack.ArrayListStack;
import com.volantis.synergetics.cornerstone.stack.Stack;

/**
 * @mock.generate
 */
public class BaseURLTracker implements BaseURLProvider {

    private final Stack stack;

    public BaseURLTracker(MarinerURL initialURL) {
        stack = new ArrayListStack();
        if (initialURL != null) {
            stack.push(initialURL);
        }
    }

    public boolean startElement(String systemId) {
        boolean changed;
        MarinerURL enclosingBaseURL = getBaseURL();
        if (enclosingBaseURL == null ||
                !enclosingBaseURL.getExternalForm().equals(systemId)) {
            MarinerURL baseURL = new MarinerURL(systemId);
            stack.push(baseURL);
            changed = true;
        } else {
            stack.push(enclosingBaseURL);
            changed = false;
        }

        return changed;
    }

    public void endElement(String systemId) {
        MarinerURL url = (MarinerURL) stack.pop();
        String popped = url.getExternalForm();

        MarinerURL urlToCompare = new MarinerURL(systemId);
        if (!urlToCompare.getExternalForm().equals(popped)) {
            throw new IllegalStateException("Expected to pop '" + systemId +
                    "' but found '" + popped + "'");
        }
    }

    public MarinerURL getBaseURL() {
        MarinerURL result;
        if (stack.isEmpty()) {
            result = null;
        } else {
            result = (MarinerURL) stack.peek();
        }
        return result;
    }
}
