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
 * $Header: /src/voyager/com/volantis/testtools/swing/SwingTestHelper.java,v 1.2 2003/04/02 15:29:42 payal Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 28-Mar-03    Payal           VBM:2002101401 - Created class used by tests 
 *                              that use swing.
 * 02-Apr-03    Payal           VBM:2002101401 - Added method 
 *                              createSplashScreen().
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.swing;

import javax.swing.SwingUtilities;

import java.lang.InterruptedException;
import java.lang.reflect.InvocationTargetException;

import java.awt.Image;
import java.net.URL;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Graphics;

import javax.swing.JFrame;

import java.awt.Window;

/**
 * This class is used by tests that use swing.
 */
public class SwingTestHelper {

    /**
     * The copyright statement.
     */
    public static String mark = "(c) Volantis Systems Ltd 2003";

    /**
     * This requests the event-dispatching thread to run certain code,  
     * and return only when event-dispatching thread has executed the 
     * specified code. 
     * @param swingInvoker The SwingInvoker to invoke.
     */
    public static void doInvokeAndWait(final SwingInvoker swingInvoker)
            throws InterruptedException, InvocationTargetException {
        final Runnable run = new Runnable() {
            public void run() {
                swingInvoker.invoke();
            }
        };
        SwingUtilities.invokeAndWait(run);
    }

    /**
     * This causes the window to appear on the screen
     * while other application is loading.
     * @param frame as its owner.
     * @param fileName the name of a file containing pixel data 
     * in a recognized file format
     */
    public static Window createSplashScreen(JFrame frame, String fileName)
            throws InterruptedException {
        final Image splashImage;
        URL url = SwingTestHelper.class.getResource(fileName);

        MediaTracker mt = new MediaTracker(frame);
        splashImage = Toolkit.getDefaultToolkit().
                getImage(url);
        mt.addImage(splashImage, 0);

        mt.waitForID(0);

        Window splashScreen = new Window(frame) {
            public void paint(Graphics g) {
                if(splashImage != null) {
                    g.drawImage(splashImage, 0, 0, this);
                }
            }
        };
        splashScreen.setSize(250, 250);
       
        /* Center the window */
        Dimension screenDim =
                Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle winDim = splashScreen.getBounds();
        splashScreen.setLocation((screenDim.width - winDim.width) / 2,
                                 (screenDim.height - winDim.height) / 2);
        
        return splashScreen;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
