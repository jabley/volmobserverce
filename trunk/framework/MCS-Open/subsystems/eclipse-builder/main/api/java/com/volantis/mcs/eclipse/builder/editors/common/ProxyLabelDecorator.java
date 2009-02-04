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
package com.volantis.mcs.eclipse.builder.editors.common;

import com.volantis.mcs.eclipse.common.EclipseCommonMessages;
import com.volantis.mcs.interaction.Proxy;
import com.volantis.mcs.layouts.Layout;
import com.volantis.mcs.policies.variants.VariantBuilder;
import com.volantis.mcs.policies.variants.VariantType;
import com.volantis.mcs.policies.variants.layout.InternalLayoutContentBuilder;
import org.eclipse.jface.resource.CompositeImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Decorator for adding error images to labels that provide images and represent
 * proxies with errors.
 */
public class ProxyLabelDecorator implements ILabelDecorator {
	
	/**
	 * cache object for decorated images
	 */	
	private static ImageRegistry registry = null;
	static {
		registry = new ImageRegistry();
	}

    // Javadoc inherited
    public Image decorateImage(Image image, Object o) {
        Proxy proxy = (Proxy) o;
        String key = "default";
        List diagnostics = proxy.getDiagnostics();
        if (diagnostics != null && !diagnostics.isEmpty()) {
        	
            Object model = proxy.getModelObject();
            if (model != null && model instanceof VariantBuilder) {
                VariantBuilder variant = (VariantBuilder) model;
                VariantType type = variant.getVariantType();

                if (type == VariantType.LAYOUT) {
                    InternalLayoutContentBuilder content =
                            (InternalLayoutContentBuilder)
                            variant.getContentBuilder();
                    if (content != null) {
                        Layout layout = content.getLayout();
                        if (layout != null) {
                            key = layout.getType().toString();
                        }
                    }
                } else {
                    key = type.toString();
                }
            }

        	Image cachedImage = null;
        	if((cachedImage = registry.get("ERROR_IMAGE_" + key)) != null) {
        		image = cachedImage;
        	} else {
                image = ImageErrorDecorator.getSingleton().decorateImage(image);
                registry.put("ERROR_IMAGE_" + key, image);
        	}
        }
        return image;
    }

    // Javadoc inherited
    public String decorateText(String text, Object o) {
        return text;
    }

    // Javadoc inherited
    public void addListener(ILabelProviderListener iLabelProviderListener) {
    }

    // Javadoc inherited
    public void dispose() {
    }

    // Javadoc inherited
    public boolean isLabelProperty(Object o, String s) {
        return true;
    }

    // Javadoc inherited
    public void removeListener(ILabelProviderListener iLabelProviderListener) {
    }

    /**
     * Class that will decorate an image with an error decorator.
     */
    private static class ImageErrorDecorator extends CompositeImageDescriptor {

        /**
         * The singleton instance.
         */
        private static ImageErrorDecorator singleton =
                new ImageErrorDecorator();

        /**
         * We assume all the images provided by this decorator are 16x16
         * pixels.
         */
        private static Point SIZE = new Point(16, 16);

        /**
         * The base image to decorate.
         */
        private Image baseImage;

        /**
         * The decorator image.
         */
        private Image errorDecorator;
        
        /**
         * The private constructor.
         */
        private ImageErrorDecorator() {
        }

        /**
         * Get the singleton instance of ImageErrorDecorator.
         */
        public static ImageErrorDecorator getSingleton() {
            return singleton;
        }

        // javadoc inherited
        protected void drawCompositeImage(int i, int i1) {
            // To draw a composite image, the base image should be
            // drawn first (first layer) and then the overlay image
            // (second layer)

            // Draw the base image using the base image's image data
            drawImage(baseImage.getImageData(), 0, 0);

            // Get the overlay image data
            if (errorDecorator == null) {
                errorDecorator =
                        EclipseCommonMessages.
                        getImage(EclipseCommonMessages.ERROR_DECORATOR_KEY);
                Display display = Display.getCurrent();
                display.disposeExec(new Runnable() {
                    public void run() {
                        errorDecorator.dispose();
                        errorDecorator = null;
                    }
                });
            }

            ImageData overlayImageData = errorDecorator.getImageData();

            // Overlaying the icon in the top left corner i.e. x and y
            // coordinates are both zero
            int xValue = 0;
            int yValue = 0;
            drawImage(overlayImageData, xValue, yValue);
        }

        // javadoc inherited
        protected Point getSize() {
            return SIZE;
        }

        /**
         * Provide a decorated version of the given image.
         *
         * @param baseImage The image to decorate.
         * @return The baseImage decorated with an error overlay icon.
         */
        public Image decorateImage(Image baseImage) {
            this.baseImage = baseImage;
            ImageData data = getImageData();
            return new Image(Display.getCurrent(), data);
        }
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Nov-05	9886/4	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 31-Oct-05	9886/3	adrianj	VBM:2005101811 New themes GUI

 28-Oct-05	9886/1	adrianj	VBM:2005101811 New theme GUI

 ===========================================================================
*/
