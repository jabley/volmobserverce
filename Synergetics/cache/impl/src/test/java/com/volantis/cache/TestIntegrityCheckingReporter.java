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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.cache;

import com.volantis.cache.impl.integrity.IntegrityCheckingReporter;

public class TestIntegrityCheckingReporter
        implements IntegrityCheckingReporter {

    private Issues root;

    private Issues current;

    public TestIntegrityCheckingReporter() {
    }

    public void beginChecking(String name) {
        current = new Issues(current);

        if (root == null) {
            root = current;
        }

        current.begin(name);
    }

    public void reportIssue(String issue) {
        current.addIssue(issue);
    }

    public void endChecking(String name) {
        current.end(name);
        current = current.getParent();
    }

    public StringBuffer getIssues() {
        return root.getIssues();
    }

    private static class Issues {

        private final Issues parent;

        private final StringBuffer buffer;

        private final String indent;

        private boolean issues;

        public Issues(Issues parent) {
            this.parent = parent;
            buffer = new StringBuffer();

            if (parent == null) {
                indent = "";
            } else {
                indent = parent.getIndent() + "  ";
            }
        }

        public void begin(String name) {
            buffer.append("Start checking ").append(name).append("\n");
        }

        public void addIssue(String issue) {
            buffer.append(indent).append(issue).append("\n");
            issues = true;
        }

        public void end(String name) {
            if (issues) {
                buffer.append("Finished checking ").append(name).append("\n");
                if (parent != null) {
                    parent.addNestedIssues(buffer);
                }
            }
        }

        private void addNestedIssues(StringBuffer buffer) {
            this.buffer.append(buffer);
            issues = true;
        }

        public StringBuffer getBuffer() {
            return buffer;
        }

        public String getIndent() {
            return indent;
        }

        public Issues getParent() {
            return parent;
        }

        public StringBuffer getIssues() {
            if (issues) {
                return buffer;
            } else {
                return null;
            }
        }
    }
}
