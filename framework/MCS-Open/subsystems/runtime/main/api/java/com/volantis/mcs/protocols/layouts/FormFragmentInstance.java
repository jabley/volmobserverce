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
 * $Header: /src/voyager/com/volantis/mcs/protocols/FormFragmentInstance.java,v 1.6 2002/11/25 15:21:47 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 8-Feb-02     Steve           VBM:2001101803 - Created
 * 21-Feb-02    Steve           VBM:2001101803 - Added more overrides to the 
 *                              form fragment 
 * 21-Feb-02    Steve           VBM:2002021404 - Added more overrides to the 
 *                              form fragment 
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * 28-Mar-02    Steve           VBM:2002021404 - Fixed for method name changes
 *                              in FormFragment.
 * 31-Oct-02    Sumit           VBM:2002111103 - isEmptyImpl now takes
 *                              a FormatInstanceReference as an ignored param
 * ----------------------------------------------------------------------------
 */


package com.volantis.mcs.protocols.layouts;

import com.volantis.mcs.layouts.FormFragment;
import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.protocols.forms.AbstractFormFragment;
import com.volantis.mcs.protocols.forms.Link;
import com.volantis.mcs.runtime.URLConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains all the state associated with a FormFragment in a particular
 * MarinerPageContext.
 *
 * @mock.generate base="FormatInstance"
 */
public class FormFragmentInstance extends FormatInstance
        implements AbstractFormFragment {

    /**
     * Property to store the text to use for the link to the fragment
     */
    private String previousLinkText = null;

    /**
     * Property to store the text to use for the link from the fragment
     */
    private String nextLinkText = null;

    /** 
     * Style class property 
     */
    private String previousLinkStyleClass = null;
    private String nextLinkStyleClass = null;

    /**
     * Whether fragment has a reset button
     */
    private String resetFlag = null;

    /** 
     * Whether fragment links appear above or below the form
     */
    private String prevLinkBefore = null;
    private String nextLinkBefore = null;

    /**
     * Create a new <code>FormFragmentInstance</code>.
     */
    public FormFragmentInstance(NDimensionalIndex index) {
        super(index);
    }

    /**
     * Retrieve the link to text.
     * @return the text
     */
    public String getNextLinkText() {
        return nextLinkText;
    }

    /**
     * Set the value of the link to text
     */
    public void setNextLinkText(String txt) {
        nextLinkText = txt;
    }

    /**
     * Retrieve the link from text.
     */
    public String getPreviousLinkText() {
        return previousLinkText;
    }

    /**
     * Set the value of the link from text
     */
    public void setPreviousLinkText(String txt) {
        previousLinkText = txt;
    }

    /**
     * Set the name of the style class
     */
    public void setNextLinkStyleClass(String styleClass) {
        nextLinkStyleClass = styleClass;
    }

    /**
     * Set the name of the style class
     */
    public void setPreviousLinkStyleClass(String styleClass) {
        previousLinkStyleClass = styleClass;
    }

    /**
     * Get the value of the style class
     */
    public String getPreviousLinkStyleClass() {
        return previousLinkStyleClass;
    }

    /**
     * Get the value of the style class
     */
    public String getNextLinkStyleClass() {
        return nextLinkStyleClass;
    }

    /**
     * Get whether or not the link is rendered before the form contents
     */
    public String isPreviousLinkBefore() {
        return prevLinkBefore;
    }

    /**
     * Set whether or not the link is rendered before the form contents
     */
    public void setPreviousLinkBefore(String before) {
        prevLinkBefore = before;
    }

    /**
     * Get whether or not the link is rendered before the form contents
     */
    public String isNextLinkBefore() {
        return nextLinkBefore;
    }

    /**
     * Set whether or not the link is rendered before the form contents
     */
    public void setNextLinkBefore(String before) {
        nextLinkBefore = before;
    }


    /**
     * Get whether or not the fragment has a reset button
     */
    public String hasReset() {
        return resetFlag;
    }

    /**
     * Set whether or not the fragment has a reset button
     */
    public void setReset(String flag) {
        resetFlag = flag;
    }

    /**
     * This is called after the format and the context have been set and allows
     * the sub class to do any initialisation which depends on those values.
     */
    public void initialise() {
        FormFragment fragment = (FormFragment) format;
        previousLinkText = fragment.getPreviousLinkText();
        nextLinkText = fragment.getNextLinkText();
        previousLinkStyleClass = fragment.getPreviousLinkStyleClass();
        nextLinkStyleClass = fragment.getNextLinkStyleClass();
        prevLinkBefore = fragment.isPreviousLinkBefore() ? "true" : "false";
        nextLinkBefore = fragment.isNextLinkBefore() ? "true" : "false";
        resetFlag = fragment.isResetEnabled() ? "true" : "false";

        super.initialise();
    }

    // Javadoc inherited from super class.
    protected boolean isEmptyImpl() {

        FormFragment currentFragment = context.getCurrentFormFragment();
        FormFragment fragment = (FormFragment) format;

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

    // Javadoc inherited.
    public List getBeforeFragmentLinks(AbstractFormFragment previous,
            AbstractFormFragment next) {
        List beforeLinks = new ArrayList();
        getLink(previous, true, true, beforeLinks);
        getLink(next, false, true, beforeLinks);
        return beforeLinks;
    }

    // Javadoc inherited.
    public List getAfterFragmentLinks(AbstractFormFragment previous,
            AbstractFormFragment next) {
        List afterLinks = new ArrayList();
        getLink(previous, true, false, afterLinks);
        getLink(next, false, false, afterLinks);
        if (((FormFragment)this.getFormat()).isResetEnabled()) {
            afterLinks.add(new Link(RESET_TEXT,
                    URLConstants.RESET_FORM_FRAGMENT));
        }
        return afterLinks;
    }

    /**
     * Gets link.
     * 
     * @param fragment form fragment wrapper
     * @param prev determines if previous form fragment
     * @param before determines if we get before frgament links
     * @param linkList list of links
     */
    private void getLink(AbstractFormFragment fragment, boolean prev,
            boolean before, List linkList) {
        if (fragment == null) {
            return;
        }
        FormFragmentInstance instance = (FormFragmentInstance) fragment;
        FormFragment formFragment = (FormFragment) instance.getFormat();
        boolean isLink = prev ? formFragment.isPreviousLinkBefore() :
            formFragment.isNextLinkBefore();
        if ((before && isLink) || (!before && !isLink)) {
            addLink(instance, formFragment, prev, linkList);
        }
    }
    
    /**
     * Helper method that adds links to the list.
     * 
     * @param fragmentInstance fragment instance 
     * @param formFragment form fragment
     * @param prev determines if previous form fragment
     * @param linkList list of links
     */
    private void addLink(FormFragmentInstance fragmentInstance,
            FormFragment formFragment, boolean prev, List linkList) {
        String linkName = prev ? URLConstants.PREV_FORM_FRAGMENT
                : URLConstants.NEXT_FORM_FRAGMENT;
        String linkText = prev ? fragmentInstance.getPreviousLinkText() :
            fragmentInstance.getNextLinkText();
        if (linkText == null) {
            linkText = prev ? formFragment.getPreviousLinkText() : formFragment
                    .getNextLinkText();
        }        
        Link link = new Link(linkText, linkName);
        link.setFormFragment(fragmentInstance);
        linkList.add(link);        
    }
    
    // Javadoc inherited.
    public String getLabel() {
        // previous link text is the text to use to link to the fragment, which
        // is what is required by this method.
        return previousLinkText;
    }

    // Javadoc inherited.
    public String getName() {
        return format.getName();
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
