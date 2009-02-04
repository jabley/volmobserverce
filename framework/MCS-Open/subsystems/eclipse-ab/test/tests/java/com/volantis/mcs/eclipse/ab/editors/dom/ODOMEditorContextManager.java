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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.eclipse.ab.editors.dom;

/**
 * Class to manage the testing of code using {@link ODOMEditorContext}s.
 * <p>
 * This creates and disposes the context for each testing operation that it
 * is asked to perform.
 * <p>
 * This is necessary because we leave background threads running if we
 * don't dispose of the context properly.
 */
public class ODOMEditorContextManager {

    /**
     * Class to create the contexts we will test.
     */
    private ODOMEditorContextCreator creator;

    /**
     * Initialise.
     *
     * @param creator the class to create contexts to test.
     */
    public ODOMEditorContextManager(ODOMEditorContextCreator creator) {
        this.creator = creator;
    }

    /**
     * Perform the operation passed in, constructing the context to test
     * with the creator passed in the constructor and disposing of the context
     * when the operation has completed.
     *
     * @param operation the operation to perform.
     * @throws Exception if there was a problem performing the operation. Note
     *      that the context will be disposed even if an exception is thrown.
     */
    public void performOperation(ODOMEditorContextOperation operation)
            throws Exception {

        ODOMEditorContext context = creator.create();
        try {
            operation.perform(context);
        } finally {
            // Need to dispose or we leave a background thread running.
            context.dispose();
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 26-Aug-04	5294/1	geoff	VBM:2004082405 Reduce unnecessary background threads in testsuite

 ===========================================================================
*/
