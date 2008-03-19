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
 * $Header: /src/voyager/com/volantis/testtools/Executor.java,v 1.3 2003/04/23 13:08:09 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 21-Feb-03    Geoff           VBM:2003022004 - Created.
 * 25-Feb-03    Geoff           VBM:2003022506 - Added Null member class.
 * 25-Mar-03    Geoff           VBM:2003042306 - Update comments after adding
 *                              new AppExecutor.
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.testtools;

/**
 * An interface which defines a very basic Command pattern; for testing. <p>
 * Initially used by the {@link com.volantis.testtools.license.LicenseManager}
 * to allow test cases to provide code to be executed on their behalf, but may
 * be re-used by other classes of a similar ilk. <p>
 *
 * @deprecated This is one of those ideas that looked good but turned out bad.
 *             It ends up being quite confusing if unrelated test classes
 *             utilise the same interface for different things. For example,
 *             usage searchs return unrelated things. Therefore, I am
 *             deprecating this and intend that we ought to have separate
 *             executors for each manager class that needs them.
 */
public interface Executor {

    /**
     * The copyright statement.
     */
    String mark = "(c) Volantis Systems Ltd 2002. ";

    /**
     * Execute some code (using the Command pattern).
     *
     * @throws java.lang.Exception if there was a problem executing the code.
     */
    void execute() throws Exception;

    /**
     * A "null" implementation of Executor. <p> This does nothing, and since it
     * has no state, it can be just a single static instance to be shared by
     * all clients. <p> Generally useful for testing the test infrastructure.
     * Any other use is probably a code smell.
     */
    Executor Null = new ExecutorNull();
}

/**
 * This class exists to work around a javadoc 1.3.1 problem where it cannot
 * generate the correct javadoc for the anonymous version of this object.
 */
class ExecutorNull implements Executor {

    public void execute() throws Exception {
        // Do nothing
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Jul-03	32/1	byron	VBM:2003070207 Versioning now handled via librarian - v2

 10-Jun-03	15/1	allan	VBM:2003060907 Move some more testtools to here from MCS

 ===========================================================================
*/
