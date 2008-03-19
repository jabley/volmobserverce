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
package com.volantis.mcs.eclipse.controls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

/**
 * A specialisation of Preview that provides a
 * preview of an Image.
 */
public class ImagePreview extends Preview {

    /**
     * The Canvas child of the Preview's Group.
     */
    private Canvas canvas;

    /**
     * The image to be previewed.
     */
    private Image image;

    /**
     * Creates an ImagePreview control specialisation of Preview.
     * @param image The image to be displayed.
     */
    public ImagePreview(Composite parent, int style, Image image) {
        super(parent, style);
        this.image = image;
    }

    /**
     * Creates and returns a control consisting of a canvas with a custom
     * repainter which displays a centered image on the canvas. If the canvas
     * is smaller than the image, the image is scaled to fit inside the canvas,
     * and the aspect ratio is preserved. The canvas is added directly to the
     * Preview's group.
     */
    public Control getChild() {
        /**
         * @todo Double buffering needs to be fully investigated
         * Double buffering is used for drawing the canvas. This is not
         * supplied by SWT and so a simple mechanism is implemented below.
         * A buffer image of the same size as the current canvas area is
         * created, and a new graphics context is created to use it.
         * The buffer image is first cleared to the background colour
         * of the canvas before what will be the content of the
         * redrawn canvas area is rendered to the buffer using drawImage.
         * Finally, the buffer is drawn on the graphics context of the
         * canvas.
         *
         * The following ought to be noted about this solution and its
         * behaviour on Windows and Linux. Without double buffering on
         * Windows there is noticeable flashing of the image during
         * resizing. To see this you must ensure that your Windows desktop
         * is configured to draw the contents of windows when dragging and
         * resizing.
         *
         * On Linux, there is no flashing, and this solution does not appear
         * to do anything useful.
         *
         * However, on both platforms there is still some annoying flickering
         * during resizing. You will see grey (i.e. background-coloured) bars.
         * This effect is more pronounced on Linux with larger bars.
         */

        // You must specify SWT.NO_BACKGROUND for the canvas as we are
        // taking care of its complete rendering.
        if (canvas == null) {
            canvas = new Canvas(getGroup(), SWT.NO_BACKGROUND);
            canvas.addPaintListener(new PaintListener() {
                public void paintControl(PaintEvent pe) {
                    Rectangle canvasArea = canvas.getClientArea();
                    // Because image is passed into the constructor and the
                    // getChild method is called from Preview's constructor, we
                    // must have imageBounds as a local variable within paintControl
                    // which ensures that the constructors have finished their job
                    // before image is accessed.
                    Rectangle imageBounds = image.getBounds();
                    Image buffer = new Image(Display.getCurrent(), canvasArea);
                    // Default is to draw the entire image at the canvas origin
                    int xorg = canvasArea.x;
                    int yorg = canvasArea.y;
                    int width = imageBounds.width;
                    int height = imageBounds.height;
                    if (imageBounds.width < canvasArea.width
                            && imageBounds.height < canvasArea.height) {
                        // Canvas is larger so center the image and preserve
                        // its natural size.
                        xorg += (canvasArea.width - imageBounds.width) / 2;
                        yorg += (canvasArea.height - imageBounds.height) / 2;
                    } else {
                        // Canvas is smaller than image so calculate scaling factor so
                        // that aspect ratio is preserved.
                        float factor = Math.min((float) canvasArea.width / imageBounds.width, (float) canvasArea.height / imageBounds.height);
                        width = (int) (factor * imageBounds.width + 0.5);
                        height = (int) (factor * imageBounds.height + 0.5);
                        xorg += (canvasArea.width - width) / 2;
                        yorg += (canvasArea.height - height) / 2;
                    }
                    GC newGC = new GC(buffer);
                    newGC.setBackground(canvas.getBackground());
                    newGC.fillRectangle(canvasArea);
                    newGC.drawImage(image, imageBounds.x, imageBounds.y, imageBounds.width, imageBounds.height,
                            xorg, yorg, width, height);
                    pe.gc.drawImage(buffer, 0, 0);
                    buffer.dispose();
                    newGC.dispose();
                }
            });
        }
        return canvas;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Feb-04	3021/1	pcameron	VBM:2004020211 Committed for integration

 23-Oct-03	1608/20	pcameron	VBM:2003101607 Refactored Preview controls and made UnavailablePreview center the label and text

 21-Oct-03	1608/18	pcameron	VBM:2003101607 Added ImagePreview

 ===========================================================================
*/
