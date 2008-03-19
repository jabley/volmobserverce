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
package com.volantis.mcs.eclipse.builder.wizards.variants;

import com.volantis.mcs.eclipse.controls.ImagePreview;
import com.volantis.mcs.eclipse.controls.Preview;
import com.volantis.mcs.eclipse.controls.UnavailablePreview;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Composite;

/**
 * AssetPreviewer for images.
 */
public class ImageAssetPreviewer {

    /**
     * The preview control for this image asset or an UnavailablePreview
     * if the image cannot be loaded for some reason.
     */
    private Preview preview = null;


    /**
     * Generate the preview for a given image.
     * @param container the Composite to contain the preview control
     * @param style flags to set on the preview control
     * @param image the image we are obtaining the details for.
     */
    public ImageAssetPreviewer(Composite container,
                             int style,
                             ImageData image) {

        if (image == null) {
            preview = new UnavailablePreview(container, style);
        } else {
            // Create the SWT Image from the passed image data and create
            // a preview for it. When the preview control is destroyed, the
            // Image is is displaying will be destroyed with it.
            final Image swtImage = new Image(null, image);
            preview = new ImagePreview(container, style, swtImage);
            preview.addDisposeListener(new DisposeListener() {
                public void widgetDisposed(DisposeEvent e) {
                    swtImage.dispose();
                }
            });
        }
    }


    /**
     * Return the preview object for this image
     * @return an ImagePreview object for this image asset or an
     * UnavailablePreview object if the image is not defined.
     */
    public Preview getPreview() {
        return preview;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Feb-05	6749/3	allan	VBM:2005012102 Rework issues

 02-Feb-05	6749/1	allan	VBM:2005012102 Drag n Drop support

 ===========================================================================
*/
