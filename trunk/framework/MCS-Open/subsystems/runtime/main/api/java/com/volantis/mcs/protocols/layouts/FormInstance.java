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
 * $Header: /src/voyager/com/volantis/mcs/protocols/FormInstance.java,v 1.7 2003/01/28 14:26:44 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 27-Jun-01    Paul            VBM:2001062704 - Created.
 * 09-Jul-01    Paul            VBM:2001062810 - Cleaned up.
 * 29-Oct-01    Paul            VBM:2001102901 - Moved from layouts package.
 * 28-Feb-02    Paul            VBM:2002022804 - Generalised this object to
 *                              allow it to use any type of OutputBuffer.
 * 05-Mar-02    Ian             VBM:2002030401 - Prevent null pointer in
 *                              isEmpty() when a form is not referenced.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from 
 *                              class to string.
 * 25-Apr-02    Paul            VBM:2002042202 - Added a content buffer into
 *                              which the contents of the form are written.
 * 31-Oct-02    Sumit           VBM:2002111103 - isEmptyImpl now takes
 *                              a FormatInstanceReference as an ignored param
 * 28-Jan-03    Geoff           VBM:2003012802 - fixed typo in isEmptyImpl.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.layouts;

import com.volantis.mcs.layouts.Form;
import com.volantis.mcs.layouts.FormFragment;
import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.forms.AbstractForm;
import com.volantis.mcs.protocols.forms.AbstractFormFragment;

/**
 * Contains all the state associated with a Form in a particular
 * MarinerPageContext.
 *
 * @mock.generate base="FormatInstance"
 */
public class FormInstance extends FormatInstance implements AbstractForm {

    /**
     * The preamble buffer.
     */
    private OutputBuffer preambleBuffer;

    /**
     * The content buffer.
     */
    private OutputBuffer contentBuffer;

    /**
     * The postamble buffer.
     */
    private OutputBuffer postambleBuffer;

    /**
     * Create a new <code>FormInstance</code>.
     */
    public FormInstance(NDimensionalIndex index) {
        super(index);
    }

    // Javadoc inherited from super class.
    protected boolean isEmptyImpl() {

        Form form = (Form) format;

        // This instance is only empty if all the buffers are empty and all the
        // children are empty too.
        if (preambleBuffer != null && !preambleBuffer.isEmpty()) {
            return false;
        }
        if (contentBuffer != null && !contentBuffer.isEmpty()) {
            return false;
        }
        if (postambleBuffer != null && !postambleBuffer.isEmpty()) {
            return false;
        }

        Format child = form.getChildAt(0);
        if (child != null) {
            return context.isFormatEmpty(child);
        }

        return true;
    }

    /** 
     * Get the preamble buffer
     * @param create True if the caller needs to make sure that the buffer has
     * been created and false otherwise.
     * @return The preamble buffer
     */
    public OutputBuffer getPreambleBuffer(boolean create) {
        if (create && preambleBuffer == null) {
            preambleBuffer = context.allocateOutputBuffer();
        }

        return preambleBuffer;
    }

    /** 
     * Get the content buffer
     * @param create True if the caller needs to make sure that the buffer has
     * been created and false otherwise.
     * @return The content buffer
     */
    public OutputBuffer getContentBuffer(boolean create) {
        if (create && contentBuffer == null) {
            contentBuffer = context.allocateOutputBuffer();
        }

        return contentBuffer;
    }

    /** 
     * Get the postamble buffer
     * @param create True if the caller needs to make sure that the buffer has
     * been created and false otherwise.
     * @return The postamble buffer
     */
    public OutputBuffer getPostambleBuffer(boolean create) {
        if (create && postambleBuffer == null) {
            postambleBuffer = context.allocateOutputBuffer();
        }

        return postambleBuffer;
    }

    // Javadoc inherited.
    public String getName() {
        return format.getName();
    }

    // Javadoc inherited.
    public boolean isFragmented() {
        return ((Form)format).isFragmented();
    }

    // Javadoc inherited.
    public AbstractFormFragment getPreviousFormFragment(
            AbstractFormFragment current) {
        FormFragmentInstance currentFormFragmentInstance = 
            (FormFragmentInstance) current;
        FormFragment fragment = ((Form)format).getPreviousFormFragment(
                (FormFragment) currentFormFragmentInstance.getFormat());
        FormFragmentInstance fragmentInstance = null;
        if (fragment != null) {
            fragmentInstance = (FormFragmentInstance)context.
                getFormatInstance(fragment,NDimensionalIndex.ZERO_DIMENSIONS);
        }
        return fragmentInstance;
    }

    // Javadoc inherited.
    public AbstractFormFragment getNextFormFragment(
            AbstractFormFragment current) {
        FormFragmentInstance currentFormFragmentInstance = 
                                               (FormFragmentInstance) current;
        FormFragment fragment = ((Form)format).getNextFormFragment(
                (FormFragment) currentFormFragmentInstance.getFormat());
        FormFragmentInstance fragmentInstance = null;
        if (fragment != null) {        
            fragmentInstance = (FormFragmentInstance)context.
                getFormatInstance(fragment,NDimensionalIndex.ZERO_DIMENSIONS);
        }
        return fragmentInstance;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jul-05	8862/1	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 05-Nov-04	6112/7	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 05-Nov-04	6112/5	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 02-Nov-04	5882/1	ianw	VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

 29-Jun-04	4713/2	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 14-Jun-04	4704/2	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 ===========================================================================
*/
