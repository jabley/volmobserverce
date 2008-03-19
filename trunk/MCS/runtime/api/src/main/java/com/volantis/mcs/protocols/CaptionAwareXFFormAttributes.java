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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols;

import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.protocols.layouts.ContainerInstance;
import com.volantis.styling.Styles;

/**
 * Should be implemented by xfform elements that want to be able to direct
 * their label and entry content to different panes
 */
public interface CaptionAwareXFFormAttributes {

    /**
     * Set the value of the caption property.
     *
     * @param caption The new value of the caption property.
     */
    public void setCaption(TextAssetReference caption);

    /**
     * Get the value of the caption property.
     *
     * @return The value of the caption property.
     */
    public TextAssetReference getCaption();

    /**
     * Get the {@link com.volantis.styling.Styles} that should be applied to this xfform field's
     * caption.
     *
     * @return the styles that should be applied to this xfform field's caption
     */
    public Styles getCaptionStyles();

    /**
     * Set the {@link Styles} that should be applied to this xfform field's
     * caption.
     *
     * @param captionStyles     the styles that should be applied to this
     *                          xfform field's caption*
     */
    public void setCaptionStyles(Styles captionStyles);

    /**
     * Set the container instance to which the caption markup should be output.
     *
     * @param captionContainerInstance      the container instance to which the
     *                                      caption markup should be output.
     */
    public void setCaptionContainerInstance(
            ContainerInstance captionContainerInstance);

    /**
     * Get the container instance to which the caption markup should be output.
     *
     * @return ContainerInstance to which the caption markup should be output.
     */
    public ContainerInstance getCaptionContainerInstance();

    /**
     * Set the container instance to which the entry markup should be output.
     *
     * @param entryContainerInstance        the container instance to which the
     *                                      entry markup should be output.
     */
    public void setEntryContainerInstance(
            ContainerInstance entryContainerInstance);

    /**
     * Get the container instance to which the entry markup should be output.
     *
     * @return ContainerInstance to which the entry markup should be output.
     */
    public ContainerInstance getEntryContainerInstance();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Sep-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 ===========================================================================
*/
