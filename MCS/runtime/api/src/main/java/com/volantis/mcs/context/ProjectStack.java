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

package com.volantis.mcs.context;

import com.volantis.mcs.runtime.RuntimeProject;
import com.volantis.shared.stack.ArrayListStack;
import com.volantis.shared.stack.Stack;

import java.util.EmptyStackException;

/**
 * A stack of projects.
 *
 * @mock.generate
 */
public class ProjectStack {

    private final Stack stack;

    public ProjectStack() {
        stack = new ArrayListStack();
    }

    /**
     * Push a new project onto the stack.
     * <p>
     * The new project will become the current one until it is popped off
     * the stack.
     * </p>
     *
     * @param project The project to push, may not be null.
     */
    public void pushProject(RuntimeProject project) {
        if (project == null) {
            throw new IllegalArgumentException("project cannot be null");
        }

        stack.push(project);
    }

    /**
     * Pop the current project off the stack.
     *
     * @param expected The project that is expected to be the current. If this
     *                 is specified and does not match the current one then an
     *                 exception is thrown. If this is null then no checking is
     *                 performed.
     * @return The project that was popped off the stack.
     */
    public RuntimeProject popProject(RuntimeProject expected) {
        RuntimeProject popped = null;

        try {
            popped = (RuntimeProject) stack.pop();
        } catch (EmptyStackException ese) {
            throw new IllegalStateException("The project stack is empty, " +
                    "which should never occur.");
        }

        if (expected != null && !expected.equals(popped)) {
            throw new IllegalStateException("Expected project " + expected
                    + ", but got " + popped);
        }

        return popped;
    }

    /**
     * Get the current project.
     * <p>
     * The current project is the last one that was pushed onto but has
     * not yet been popped off the stack. The stack always contains at least
     * one project as the default project specified in the configuration is
     * pushed onto the stack.
     * </p>
     *
     * @return The current project;
     */
    public RuntimeProject getCurrentProject() {
        return (RuntimeProject) stack.peek();
    }
}
