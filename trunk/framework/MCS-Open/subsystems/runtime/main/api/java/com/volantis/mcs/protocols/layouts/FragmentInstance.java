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
 * $Header: /src/voyager/com/volantis/mcs/protocols/FragmentInstance.java,v 1.5 2002/11/25 15:21:47 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 28-Feb-01    Paul            Created.
 * 27-Jun-01    Paul            VBM:2001062704 - Sorted out the copyright.
 * 09-Jul-01    Paul            VBM:2001062810 - Cleaned up.
 * 29-Oct-01    Paul            VBM:2001102901 - Moved from layouts package.
 * 02-Nov-01    Paul            VBM:2001102403 - Cleaned up.
 * 19-Dec-01    Doug            VBM:2001121701 - Added properties to store the
 *                              text to use for generating to and from links.
 *                              Implemented an initialise method so that
 *                              the link properties could be initialised from
 *                              the associated Fragment object. Added the 
 *                              methods getLinkToText(), getLinkFromText(),
 *                              setLinkToText() and setLinkFromText()
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * 31-Oct-02    Sumit           VBM:2002111103 - isEmpty and isEmptyImpl now
 *                              take FormatInstanceReferences in order to 
 *                              identify the object they must clean up
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.layouts;

import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.layouts.Fragment;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.protocols.OutputBuffer;

/**
 * Contains all the state associated with a Fragment in a particular
 * MarinerPageContext.
 *
 * @mock.generate base="FormatInstance"
 */
public class FragmentInstance
        extends FormatInstance {

    /**
     * Property to store the text to use for the link to the fragment
     */
    private String linkToText;

    /**
     * Property to store complex links from the fragment. This property should
     * have precedence over the linkToText property.
     */
    private OutputBuffer linkToBuffer;

    /**
     * Property to store the text to use for the link from the fragment
     */
    private String linkFromText;

    /**
     * Property to store complex links to the fragment. This property should
     * have precedence over the linkFromText property.
     */
    private OutputBuffer linkFromBuffer;
    private int linkToInstanceIndex;
    private int linkFromInstanceIndex;

    /**
     * Retrieve the link to text.
     * @return the text
     */
    public String getLinkToText() {
        return linkToText;
    }

    /**
     * Set the value of the link to text
     * @param linkToText the text 
     */
    public void setLinkToText(String linkToText) {
        this.linkToText = linkToText;
    }

    /**
     * Sets the LinkTo output buffer. Only sets with lower instance index than
     * the last successful set are honored (and the very first set), other
     * sets will be ignored.
     *
     * Returns true if the set modified the buffer.
     *
     * @param buffer the (possibly) new output buffer
     * @param instanceIndex the instance index of the container that wants to
     * set the buffer.
     * @return true iff the set was carried out
     */
    public boolean setLinkToBuffer(final OutputBuffer buffer,
                                   final int instanceIndex) {

        boolean modified = false;
        if (linkToBuffer == null || linkToInstanceIndex > instanceIndex) {
            linkToBuffer = buffer;
            linkToInstanceIndex = instanceIndex;
            modified = true;
        }
        return modified;
    }

    /**
     * Returns the LinkTo output buffer.
     *
     * @return the output buffer for the LinkTo link
     */
    public OutputBuffer getLinkToBuffer() {
        return linkToBuffer;
    }

    /**
     * Retrieve the link from text.
     * @return the text
     */
    public String getLinkFromText() {
        return linkFromText;
    }

    /**
     * Set the value of the link from text
     * @param linkFromText the text 
     */
    public void setLinkFromText(String linkFromText) {
        this.linkFromText = linkFromText;
    }

    /**
     * Sets the LinkFrom output buffer. Only sets with lower instance index than
     * the last successful set are honored (and the very first set), other
     * sets will be ignored.
     *
     * Returns true if the set modified the buffer.
     *
     * @param buffer the (possibly) new output buffer
     * @param instanceIndex the instance index of the container that wants to
     * set the buffer.
     * @return true iff the set was carried out
     */
    public boolean setLinkFromBuffer(final OutputBuffer buffer,
                                     final int instanceIndex) {
        boolean modified = false;
        if (linkFromBuffer == null || linkFromInstanceIndex > instanceIndex) {
            linkFromBuffer = buffer;
            linkFromInstanceIndex = instanceIndex;
            modified = true;
        }
        return modified;
    }

    /**
     * Returns the LinkFrom output buffer.
     *
     * @return the output buffer for the LinkFrom link
     */
    public OutputBuffer getLinkFromBuffer() {
        return linkFromBuffer;
    }

    /**
     * Retrieve the name of the Fragment that this Fragment instance represents
     * @return the name
     */
    public String getFragmentName() {
        return ((Fragment) format).getName();
    }

    /**
     * Create a new <code>FragmentInstance</code>.
     */
    public FragmentInstance(NDimensionalIndex index) {
        super(index);
    }

    /**
     * This is called after the format and the instance have been set and allows
     * the sub class to do any initialisation which depends on those values.
     */
    public void initialise() {
        Fragment fragment = (Fragment) format;
        this.linkToText = fragment.getLinkText();
        this.linkFromText = fragment.getBackLinkText();
        super.initialise();
    }

    // Javadoc inherited from super class.
    protected boolean isEmptyImpl() {

        Fragment currentFragment = context.getCurrentFragment();
        Fragment fragment = (Fragment) format;

        Format child = fragment.getChildAt(0);

        if (child != null) {
            if (currentFragment == null || fragment == currentFragment) {
                return context.isFormatEmpty(child);
            } else {
                // If the child is a fragment that is not needed by this page,
                // then the tags will have not written anything to it, or its 
                // children but we will still need to create a link to the 
                // fragment so the space will not be empty.
                return false;
            }
        }

        return true;
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

 14-Jun-04	4704/1	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 ===========================================================================
*/
