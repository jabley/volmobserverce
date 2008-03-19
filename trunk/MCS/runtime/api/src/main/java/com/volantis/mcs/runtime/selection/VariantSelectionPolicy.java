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
 * $Header: /src/voyager/com/volantis/mcs/integration/VariantSelectionPolicy.java,v 1.6 2002/05/29 13:22:56 pduffin Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 11-Feb-02    Paul            VBM:2001122105 - Created to generalize object
 *                              selection.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from 
 *                              class to string.
 * 22-Mar-02    Adrian          VBM:2002031503 - Added Object parameter to
 *                              method retrieveBestObject so that extra info
 *                              can be passed into the selection policy if the
 *                              search criteria requires more information than
 *                              the identity of the Object
 * 30-Apr-02    Allan           VBM:2002040804 - Added retrieveBestObject() 
 *                              that takes a "name" param.
 * 29-May-02    Paul            VBM:2002050301 - Added getSupportedClass and
 *                              removed retrieveBestObject which takes a String
 *                              name.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime.selection;

import com.volantis.mcs.policies.variants.metadata.EncodingCollection;
import com.volantis.mcs.runtime.policies.RuntimePolicyReference;
import com.volantis.mcs.runtime.policies.SelectedVariant;
import com.volantis.mcs.runtime.policies.SelectionContext;

/**
 * This interface defines the method(s) which must be implemented by those
 * classes which want to define the selection policy for a particular class
 * of object.
 *
 * @mock.generate
 */
public interface VariantSelectionPolicy {

    /**
     * Select the best dependent RepositoryObject of the specified object
     * within the current context.
     * <p>
     * The meaning of best is defined by the implementations of this interface.
     * </p>
     *
     * @param context   The context within which the selection is to be
     *                  done.
     * @param reference The identity of the object whose dependent object is
     *                  requested.
     * @param requiredEncodings The required encodings for the variant, may be null.
     * @return The best dependent of the specified object.
     */
    SelectedVariant retrieveBestObject(
            SelectionContext context,
            RuntimePolicyReference reference,
            EncodingCollection requiredEncodings);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Aug-05	9211/1	pabbott	VBM:2005080902 End to End CSS emulation test

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 ===========================================================================
*/
