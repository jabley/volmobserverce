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
package com.volantis.mcs.protocols.forms;

import com.volantis.mcs.runtime.URLConstants;
import com.volantis.mcs.themes.StyleList;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleString;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.values.StyleKeywords;
import com.volantis.styling.NestedStyles;
import com.volantis.styling.PseudoElements;
import com.volantis.styling.PseudoStyleEntity;
import com.volantis.styling.Styles;
import com.volantis.styling.values.PropertyValues;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Data which describes a form fragment.
 *
 * The behaviour for XDIME2 form fragment links is as follows:
 * <ul>
 * <li>if the content property is set to a StyleString, the link should be
 * rendered out. The label will be inserted into the DOM from the Styles by the
 * DefaultContentInserter, and so shouldn't be done here</li>
 * <li>if the content property contains the CONTENTS StyleKeyword, then the
 * fragment label should be inserted by this class. The DefaultContentInserter
 * will handle inserting the group label into the right place in the specified
 * CONTENT</li>
 * <li>if the link PseudoElement's content property is none or normal then the
 * label of the group should be inserted</li>
 * <li>if the link PseudoElement's content property is none or normal and the
 * group has no label, then standard internationalised strings should be
 * inserted</li>
 * <li>if the PseudoElement is not set, then the link should not appear</li>
 * </ul>
 */
public final class FormFragmentData implements AbstractFormFragment {

    private final String name;

    private XFormGroup group;

    private List afterLinks;

    private List beforeLinks;

    private Link NEXT_DEFAULT;
    private Link PREVIOUS_DEFAULT;

    /**
     * At the point when we're creating the form fragment, we don't know the
     * next fragment, so we can't add it's label in as the link text (if it's
     * required). We therefore use this marker, and replace it with the
     * appropriate label when we've built the whole set of fragments.
     */
    private static final String REPLACE_LABEL = "REPLACE";

    private boolean regenerateLinks = true;

    /**
     * Initialize a new instance using the given parameters.
     *
     * @param name String identifier
     * @param group which causes this fragment to be created
     */
    public FormFragmentData(String name, XFormGroup group) {
        this.name = name;
        this.group = group;
    }

    private void createDefaultLinks(Styles styles) {
        NEXT_DEFAULT = new Link(NEXT_TEXT,
                URLConstants.NEXT_FORM_FRAGMENT, styles);
        PREVIOUS_DEFAULT = new Link(PREVIOUS_TEXT,
                URLConstants.PREV_FORM_FRAGMENT, styles);
    }

    /**
     * Generate the links for this form fragment. It will generate both the
     * links that should appear before the fragment and after. It removes the
     * specific PseudoElement styles that it uses when processing, so calling
     * it again on the same styles will not change the links, but calling it
     * again with new styles will.
     *
     * @param previous
     * @param next
     */
    private void generateLinks(AbstractFormFragment previous,
            AbstractFormFragment next) {
        if (regenerateLinks) {
            // Figure out which links are required for this fragment.
            Styles styles = group.getStyles();

            createDefaultLinks(styles);

            Styles beforeStyles = styles.removeNestedStyles(
                    PseudoElements.BEFORE);
            Styles afterStyles = styles.removeNestedStyles(
                    PseudoElements.AFTER);

            // Expect rule to be of the form:
            // element::before::mcs-previous {content: blah}
            beforeLinks = generateLinks((NestedStyles) beforeStyles);
            afterLinks = generateLinks((NestedStyles) afterStyles);

            // Make sure that there is a previous and next link if required.
            validateLinks();

            updateLabels(beforeLinks, previous, next, this);
            updateLabels(afterLinks, previous, next, this);

            // Set the flag so that the links are only regenerated if the
            // referenced group changes.
            regenerateLinks = false;
        }
    }

    /**
     * Validate that a next and a previous link has been specified. If either
     * is missing then the missing link will be added to the after links. The
     * next link should preceed the previous link.
     */
    private void validateLinks() {
        boolean hasNext = false;
        boolean hasPrevious = false;


        //If there has been a before link created then test the before links
        //for the presence of the next and previous links. Once the links
        //have been found stop processing the links
        if (!beforeLinks.isEmpty()) {
            for (Iterator iterator = beforeLinks.iterator();
                 (!hasNext || !hasPrevious) && iterator.hasNext();) {
                Link link = (Link) iterator.next();
                String linkName = link.getLinkName();
                if (URLConstants.NEXT_FORM_FRAGMENT.equals(linkName)) {
                    hasNext = true;
                } else if (URLConstants.PREV_FORM_FRAGMENT.equals(linkName)) {
                    hasPrevious = true;
                }
            }

        }
        //If the next and previous links have not been found and some afterlinks
        //have been specified then test the before links for the presence of
        //the next and previous links. Once the links have been found stop
        // processing the links
        if (!afterLinks.isEmpty() && (!hasNext || !hasPrevious)) {
            for (Iterator iterator = afterLinks.iterator();
                 (!hasNext || !hasPrevious) && iterator.hasNext();) {
                Link link = (Link) iterator.next();
                String linkName = link.getLinkName();
                if (URLConstants.NEXT_FORM_FRAGMENT.equals(linkName)) {
                    hasNext = true;
                } else if (URLConstants.PREV_FORM_FRAGMENT.equals(linkName)) {
                    hasPrevious = true;
                }
            }
        }

        //If no next link has been specified add it to the start of the after
        //links
        if (!hasNext) {
            afterLinks.add(0, NEXT_DEFAULT);
        }
        //If no previous link has been specified add it to the end of the after
        //links
        if (!hasPrevious) {
            afterLinks.add(PREVIOUS_DEFAULT);
        }
    }

    /**
     * Generate the list of links which should be created from the specified
     * styles.
     *
     * @param styles    used to determine which links should appear
     * @return List of fragment links
     */
    private List generateLinks(NestedStyles styles) {

        List links = new ArrayList();
        if (styles != null) {

            // Iterate over the styles nested inside the provided styles. The
            // order should be preserved in order to render correctly.
            for (Iterator i = styles.iterator(); i.hasNext(); ) {
                NestedStyles nestedStyles = (NestedStyles) i.next();
                final PseudoStyleEntity pseudoStyleEntity =
                         nestedStyles.getPseudoStyleEntity();
                Link link = null;
                if (pseudoStyleEntity.equals(PseudoElements.MCS_NEXT)) {
                    link = createLink(nestedStyles,
                            URLConstants.NEXT_FORM_FRAGMENT);
                } else if (pseudoStyleEntity.equals(PseudoElements.MCS_PREVIOUS)) {
                    link = createLink(nestedStyles,
                            URLConstants.PREV_FORM_FRAGMENT);
                } else if (pseudoStyleEntity.equals(PseudoElements.MCS_RESET)) {
                    link = createLink(nestedStyles,
                            URLConstants.RESET_FORM_FRAGMENT);
                }
                if (link != null) {
                    links.add(link);
                }
            }
            styles.removeNestedStyles(PseudoElements.MCS_NEXT);
            styles.removeNestedStyles(PseudoElements.MCS_PREVIOUS);
            styles.removeNestedStyles(PseudoElements.MCS_RESET);
        }
        return links;
    }

    /**
     * Create a link from the given styles and link name.
     * @param nestedStyles  which determine what the link label should be
     * @param linkName      the name of the link
     * @return Link
     */
    private Link createLink(Styles nestedStyles, String linkName) {
        Link link = null;
        if (nestedStyles != null) {
            PropertyValues propertyValues = nestedStyles.getPropertyValues();
            StyleValue value = propertyValues.getSpecifiedValue(
                    StylePropertyDetails.CONTENT);

            // Determine the link text.
            if (!(URLConstants.RESET_FORM_FRAGMENT.equals(linkName) &&
                    containsOnlyNone(value))) {
                // The REPLACE_LABEL should be replaced by the name of the
                // group to which the link points (not known until later).
                link = new Link(REPLACE_LABEL, linkName, nestedStyles);
            }
        }
        return link;
    }

    // Javadoc inherited.
    public String getName() {
        return name;
    }

    /**
     * Check the value of the {@link StylePropertyDetails#MCS_BREAK_AFTER}
     * property in order to determine whether this group should cause the form
     * to be fragmented from this point on.
     *
     * @return true if the form should be fragmented from this point on, and
     * false otherwise.
     */
    public boolean causesFragmentation() {
        return group.causesFragmentation();
    }

    // Javadoc inherited.
    public Styles getStyles() {
        return group.getStyles();
    }

    // Javadoc inherited.
    public String getLabel() {
        return group.getLabel();
    }

    /**
     * Retrieve the inclusion path for this fragment. It will be the same as
     * the value returned by
     * {@link com.volantis.mcs.protocols.DeviceLayoutContext#getInclusionPath()}
     * at the point when the group is created It is used to create the
     * {@link com.volantis.mcs.runtime.FragmentationState.FragmentChange} in
     * {@link com.volantis.mcs.protocols.VolantisProtocol#doFormLink}.
     *
     * @return String inclusion path for this fragment.
     */
    public String getInclusionPath() {
        return group.getInclusionPath();
    }

    public void resetGroup(XFormGroup group) {
        this.group = group;
        regenerateLinks = true;
    }

    // Javadoc inherited.
    public List getBeforeFragmentLinks(AbstractFormFragment previous,
            AbstractFormFragment next) {
        generateLinks(previous, next);
        return beforeLinks;
    }

    // Javadoc inherited.
    public List getAfterFragmentLinks(AbstractFormFragment previous,
            AbstractFormFragment next) {
        generateLinks(previous, next);
        return afterLinks;
    }

    /**
     * Return true if the style value either is the none keyword or a list
     * which contains only none keywords.
     *
     * @param value which may contain only the none keyword
     * @return true if the style value either is the none keyword or a list
     * which contains only none keyword, and false otherwise
     */
    private boolean containsOnlyNone(final StyleValue value) {
        boolean containsOnlyNone = false;
        if (StyleKeywords.NONE.equals(value)) {
            containsOnlyNone = true;
        } else if (value instanceof StyleList) {
            // Iterate over the elements of the list.
            containsOnlyNone = true;
            List list = ((StyleList)value).getList();
            for (int i = 0; containsOnlyNone && i < list.size(); i++) {
                final StyleValue styleValue = (StyleValue) list.get(i);
                containsOnlyNone = containsOnlyNone(styleValue);
            }
        }
        return containsOnlyNone;
    }

    /**
     * Updates the text of the specified link according to its content style
     * value.
     *
     * @param defaultText the text to be used if no label was given to the
     * target form fragment of the link
     */
    private void updateLinkText(Link link, String defaultText) {

        final AbstractFormFragment formFragment = link.getFormFragment();
        if (formFragment != null) {
            final String label = formFragment.getLabel();
            final String contentsValue = label != null? label: defaultText;
            final StringBuffer linkTextBuffer = new StringBuffer();
            final Styles styles = link.getLinkStyles();
            if (styles != null) {
                final PropertyValues propertyValues = styles.getPropertyValues();
                final StyleValue styleValue = propertyValues.getSpecifiedValue(
                        StylePropertyDetails.CONTENT);
                buildLinkText(linkTextBuffer, styleValue, contentsValue);
                final String linkText = linkTextBuffer.toString();
                if (linkText.length() > 0) {
                    link.setLinkText(linkText);
                } else {
                    link.setLinkText(contentsValue);
                }
            } else {
                link.setLinkText(contentsValue);
            }

        }
    }

    /**
     * Adds the String representation of the specified style value to the
     * buffer. normal and contents keywords are replaced with the default#
     * contents value (as specified). none keyword is ignored.
     *
     * @param linkTextBuffer the buffer to add string representation to
     * @param styleValue the style value to convert
     * @param contentsValue the default contents value
     */
    private void buildLinkText(final StringBuffer linkTextBuffer,
                               final StyleValue styleValue,
                               final String contentsValue) {
        if (styleValue instanceof StyleString) {
            linkTextBuffer.append(((StyleString) styleValue).getString());
        } else if (styleValue instanceof StyleList) {
            final List list = ((StyleList) styleValue).getList();
            for (Iterator iter = list.iterator(); iter.hasNext(); ) {
                buildLinkText(
                    linkTextBuffer, (StyleValue) iter.next(), contentsValue);
            }
        } else if (StyleKeywords.NORMAL.equals(styleValue) ||
                   StyleKeywords.CONTENTS.equals(styleValue)) {
            linkTextBuffer.append(contentsValue);
        } else if (StyleKeywords.NONE.equals(styleValue)) {
            // do nothing
        }
    }

    /**
     * Update the template labels.
     * @param links
     * @param previous
     * @param next
     * @param current
     */
    private void updateLabels(List links, AbstractFormFragment previous,
             AbstractFormFragment next, AbstractFormFragment current) {

        for (int i = 0; i < links.size(); i++) {
            Link link = (Link) links.get(i);

            final String linkName = link.getLinkName();
            if (URLConstants.RESET_FORM_FRAGMENT.equals(linkName)) {
                link.setFormFragment(current);
                updateLinkText(link, AbstractFormFragment.RESET_TEXT);
            } else if (URLConstants.NEXT_FORM_FRAGMENT.equals(linkName)) {
                link.setFormFragment(next);
                updateLinkText(link, AbstractFormFragment.NEXT_TEXT);
            } else if (URLConstants.PREV_FORM_FRAGMENT.equals(linkName)) {
                link.setFormFragment(previous);
                updateLinkText(link, AbstractFormFragment.PREVIOUS_TEXT);
            }
            if (link.getFormFragment() == null) {
                links.remove(i--);
            }
        }
    }
}
