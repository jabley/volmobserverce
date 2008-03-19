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

import com.volantis.styling.Styles;

/**
 * Encapsulates information about a link.
 */
public final class Link {

    private String linkText;
    private final String linkName;
    private Styles linkStyles;
    private AbstractFormFragment formFragment;

    public Link(String linkText, String linkName) {
        this.linkText = linkText;
        this.linkName = linkName;
    }

    public Link(String linkText, String linkName, Styles linkStyles) {
        this.linkText = linkText;
        this.linkName = linkName;
        this.linkStyles = linkStyles;
    }

    public String getLinkText() {
        return linkText;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkText(String linkText) {
        this.linkText = linkText;
    }

    public Styles getLinkStyles() {
        return linkStyles;
    }

    public AbstractFormFragment getFormFragment() {
        return formFragment;
    }

    public void setFormFragment(AbstractFormFragment formFragment) {
        this.formFragment = formFragment;
    }
}
