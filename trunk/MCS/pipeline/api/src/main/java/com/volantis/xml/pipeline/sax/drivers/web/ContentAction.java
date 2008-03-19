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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.pipeline.sax.drivers.web;

/**
 * Type safe enumerator for the actions that can be associated with Content.
 */
public class ContentAction {
    /**
     * The CONSUME action.
     */
    public static final ContentAction CONSUME = new ContentAction();

    /**
     * The IGNORE action.
     */
    public static final ContentAction IGNORE = new ContentAction();

    /**
     * For the specified action string return an appropriate ContentAction
     * object.
     *
     * @param  action the action (should be 'ignore' or 'consume'.
     * @return        an appropriate ContentAction object for the action, or
     *                null if the action does not match a valid ConentAction.
     */
    public static final ContentAction getContentAction(String action) {

        ContentAction result = null;
        if ("ignore".equals(action)) {
            result = ContentAction.IGNORE;
        } else if ("consume".equals(action)) {
            result = ContentAction.CONSUME;
        }
        return result;
    }

    /**
     * The private constructor.
     */
    private ContentAction() {
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 30-Jul-03	238/1	byron	VBM:2003072309 Create the adapter process for parent task - preliminary commit

 24-Jul-03	217/1	allan	VBM:2003071702 WebDriver implementation. Intermediate commit

 ===========================================================================
*/
