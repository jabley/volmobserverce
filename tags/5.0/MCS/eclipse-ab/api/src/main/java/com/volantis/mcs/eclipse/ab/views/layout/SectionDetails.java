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

package com.volantis.mcs.eclipse.ab.views.layout;

/**
 * Class to store the section details of the configuration file. Each section
 * may have a title and contain additional section specific details.
 */
class SectionDetails {
    /**
     * The title.
     */
    private String title;

    /**
     * The section's details.
     */
    private FormatAttributesViewDetails details;

    /**
     * Construct this object with the title which may be null.
     *
     * @param title the title which may be null.
     */
    public SectionDetails(String title) {
        this.title = title;
    }

    /**
     * Return this section's details.
     * @return this section's details.
     */
    public FormatAttributesViewDetails getDetails() {
        return details;
    }

    /**
     * Setter for setting the details for this section.
     * @param details the details for this section.
     */
    public void setDetails(FormatAttributesViewDetails details) {
        this.details = details;
    }

    /**
     * Return the title.
     * @return the title.
     */
    public String getTitle() {
        return title;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 13-Jan-04	2483/1	byron	VBM:2003121504 Eclipse PM Layout Editor: Format Attributes View: XML Config

 ===========================================================================
*/
