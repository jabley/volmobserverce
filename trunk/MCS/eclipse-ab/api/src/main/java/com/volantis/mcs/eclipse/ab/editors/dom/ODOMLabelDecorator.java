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
package com.volantis.mcs.eclipse.ab.editors.dom;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.resource.CompositeImageDescriptor;
import org.eclipse.jface.util.ListenerList;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;

import com.volantis.mcs.eclipse.ab.ABPlugin;
import com.volantis.mcs.eclipse.common.EclipseCommonMessages;
import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.eclipse.common.PolicyUtils;
import com.volantis.mcs.eclipse.common.ProblemMarkerFinder;
import com.volantis.mcs.eclipse.common.odom.ODOMAttribute;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.common.odom.ODOMObservable;
import com.volantis.mcs.eclipse.common.odom.xpath.ODOMXPath;
import com.volantis.mcs.eclipse.controls.ItemContainer;
import com.volantis.mcs.xml.xpath.XPath;

/**
 * A label decorator for ODOM elements and attributes. This decorator not
 * only decorates images assoicated with ODOM elements and attributes as
 * needed it also adds a tool tip to items associated with the
 * element/attribute if in error.
 *
 * The ODOMLabelDecorator determines whether or not to decorate a label based
 * on the existance of problem markers on a given IResource. If there are
 * problem markers associated with an ODOM element/attribute the associated
 * label will be decorated and the tool tip will be set as the message found
 * in the first marker.
 */
public class ODOMLabelDecorator implements ILabelDecorator {

    /**
     * An image cache used for caching and disposal of label images.
     */
    private final Map imageCache = new HashMap();

    /**
     * A Default ProblemMarkerFinder that delegates to the
     * {@link PolicyUtils#findProblemMarkers}(IResource, XPath) method
     */
    private static final ProblemMarkerFinder DEFAULT_PROBLEM_MARKER_FINDER =
                new ProblemMarkerFinder() {
                    // javado inherited
                    public IMarker[] findProblemMarkers(IResource resource,
                                                        XPath xPath)
                                throws CoreException {
                        // delegate to the PolicyUtils class
                        return PolicyUtils.findProblemMarkers(resource, xPath);

                    }
                };
    /**
     * The resource prefix.
     */
    public static final String RESOURCE_PREFIX = "ODOMLabelDecorator."; //$NON-NLS-1$

    /**
     * The list of listeners added to this ODOMLabelDecorator.
     */
    private final ListenerList listenerList = new ListenerList();

    /**
     * The IResource associated with this ODOMLabelDecorator.
     */
    private final IResource resource;

    /**
     * Used to locate
     */
    private ProblemMarkerFinder problemMarkerFinder;

    /**
     * Construct a new ODOMLabelDecorator.
     * @param resource The IResource upon which to search for error markers
     * that form part of the decoration. Must not be null.
     * @param problemMarkerFinder a <code>ProblemMarkerFinder</code> that will
     * be used to locate IMarkers for nodes being decorated
     * @param itemContainer The ItemContainer that contains the items that
     * will ultimately be decorated by this ODOMLabelDecorator. Must not be
     * null.
     * @throws IllegalArgumentException If the resource, itemContainer or
     * problem are null.
     */
    public ODOMLabelDecorator(IResource resource,
                              ProblemMarkerFinder problemMarkerFinder,
                              ItemContainer itemContainer) {

        if (resource == null) {
            throw new IllegalArgumentException(
                        "Cannot be null: resource"); //$NON-NLS-1$
        }
        if (problemMarkerFinder == null) {
            throw new IllegalArgumentException(
                        "Cannot be null: problemMarkerFinder"); //$NON-NLS-1$
        }
        if (itemContainer == null) {
            throw new IllegalArgumentException(
                        "Cannot be null: itemContainer"); //$NON-NLS-1$
        }

        this.resource = resource;
        this.problemMarkerFinder = problemMarkerFinder;

        // Create an ODOMItemToolTipper to add tooltips to items that
        // have problems.
        // todo later consider moving this to classes that call this method
        new ODOMItemToolTipper(resource, itemContainer);
    }

    /**
     * Construct a new ODOMLabelDecorator.
     * @param resource The IResource upon which to search for error markers
     * that form part of the decoration. Must not be null.
     * @param itemContainer The ItemContainer that contains the items that
     * will ultimately be decorated by this ODOMLabelDecorator. Must not be
     * null.
     * @throws IllegalArgumentException If resource or itemContainer is null.
     */
    public ODOMLabelDecorator(IResource resource,
                              ItemContainer itemContainer) {
        this(resource, DEFAULT_PROBLEM_MARKER_FINDER, itemContainer); 
    }

    /**
     * Decorate the image if the element (that should be an ODOMElement or
     * an ODOMAttribute) has any errors.
     */
    // rest of javadoc inherited
    public Image decorateImage(Image image, Object element) {
        checkObject(element);
        final XPath xPath = new ODOMXPath((ODOMObservable) element);
        Image decoratedImage = image;
        try {
            IMarker markers[] = problemMarkerFinder.findProblemMarkers(
                    resource, xPath);
            
            if (markers.length > 0) {
                final Integer imgKey = new Integer(image.hashCode());
                
                synchronized (imageCache) {
                    
                    Image cachedImage = (Image) imageCache.get(imgKey);
                    if (cachedImage == null) {
                        cachedImage = ImageErrorDecorator.getSingleton()
                                .decorateImage(image);
                        imageCache.put(imgKey, cachedImage);
                    }
                    decoratedImage = cachedImage;
                }
            }
            
        } catch (CoreException e) {
            EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
        }

        return decoratedImage;
    }

    /**
     * This decorator never decorates text. So just return the text as recieved.
     */
    // rest of javadoc inherited
    public String decorateText(String text, Object element) {
        return text;
    }

    // javadoc inherited
    public void addListener(ILabelProviderListener iLabelProviderListener) {
        listenerList.add(iLabelProviderListener);
    }

    /**
     * Disposes of any image resources used by the labels, and also
     * removes any listeners.
     */
    public void dispose() {
        for (Iterator it = imageCache.values().iterator(); it.hasNext();) {
            final Image image = (Image) it.next();
            image.dispose();
        }
        imageCache.clear();
        removeAllListeners();
        ImageErrorDecorator decorator = ImageErrorDecorator.getSingleton();
    }

    /**
     * Always return true.
     * 
     * @return true.
     */
    // rest of javadoc inherited
    public boolean isLabelProperty(Object o, String s) {
        return true;
    }

    // javadoc inherited
    public void removeListener(ILabelProviderListener iLabelProviderListener) {
        listenerList.remove(iLabelProviderListener);
    }

    /**
     * Remove all of the listeners from this ODOMLabelDecorator.
     */
    private void removeAllListeners() {
        Object [] listeners = listenerList.getListeners();
        for (int i = 0; i < listeners.length; i++) {
            listenerList.remove(listeners[i]);
        }
    }

    /**
     * Check the given object to ensure that it meets some basic requirements:
     * not null, is an ODOMObservable, is an ODOMElement or an ODOMAttribute.
     * @param o The Object.
     * @throws IllegalArgumentException If o is null, o is not an
     * ODOMObservable or o is not an ODOMElement or an ODOMAttribute.
     */
    private void checkObject(Object o) {
        if (o == null) {
            throw new IllegalArgumentException("Cannot be null: o"); //$NON-NLS-1$
        }
        if (!(o instanceof ODOMObservable)) {
            throw new IllegalArgumentException("Expected o to be an " + //$NON-NLS-1$
                    "ODOMObservable but was: " + o.getClass().getName()); //$NON-NLS-1$
        }
        if (!(o instanceof ODOMElement) && !(o instanceof ODOMAttribute)) {
            throw new IllegalArgumentException("Unsupported ODOMObservable: " + //$NON-NLS-1$
                    o.getClass().getName());
        }
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

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 11-May-04	4272/1	allan	VBM:2004050503 Unique problem markers fix for device editor.

 22-Apr-04	3878/4	doug	VBM:2004032405 Created a basic DeviceEditor and overview page

 22-Apr-04	3878/2	doug	VBM:2004032405 Created a basic DeviceEditor and overview page

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 21-Jan-04	2659/1	allan	VBM:2003112801 RuleSection basics (read only)

 18-Jan-04	2562/2	allan	VBM:2003112010 ODOMOutlinePage displaying, decorating and tooltipping.

 08-Jan-04	2431/2	allan	VBM:2004010404 Fix validation and display update issues.

 07-Jan-04	2436/6	pcameron	VBM:2003121720 Changed a comment

 07-Jan-04	2436/4	pcameron	VBM:2003121720 A few tweaks

 07-Jan-04	2436/2	pcameron	VBM:2003121720 Added icon resources for asset errors

 17-Dec-03	2219/2	doug	VBM:2003121502 Added dom validation to the eclipse editors

 12-Dec-03	2123/3	allan	VBM:2003102005 Candidate ImageComponentEditor - no validation.

 ===========================================================================
*/
