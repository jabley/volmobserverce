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
package com.volantis.mcs.eclipse.common;

import com.volantis.mcs.eclipse.common.odom.undo.UndoRedoManager;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.xml.sax.InputSource;


/**
 * Utility class containing workarounds to eclipse problems.
 * <p>
 */
public class EclipsePatches {

    //javadoc unnecessary
    private final static boolean IS_SWT_PLATFORM_GTK =
            "gtk".equalsIgnoreCase(SWT.getPlatform());

    //javadoc unnecessary
    private final static boolean IS_SWT_VERSION_2 =
            String.valueOf(SWT.getVersion()).startsWith("2");


    /**
     * Workaround for Combo not firing focusGained/focusLost events on Linux.
     * <p>
     * The bug prevents the use of focus events to demarcate UndoRedo Units of
     * Work.
     * This workaround attaches a ModifyListener to Combo objects,
     * and uses its events to demarcate undo/redo UOWs
     */
    public static class ComboFocusBugOnSwtGtk2 {

        /**
         * whether to apply the workaround or not
         */
        private static boolean APPLY_WORKAROUND = IS_SWT_PLATFORM_GTK &&
                                                  IS_SWT_VERSION_2;

        private final ModifyListener modifyListener;
        private final UndoRedoManager urManager;

        public ComboFocusBugOnSwtGtk2(UndoRedoManager undoRedoManager) {
            this.urManager = undoRedoManager;
            this.modifyListener = new ModifyListener() {
                // source of last event - used to avoid demarcation on
                //  every key press !
                private Object lastSource;

                public void modifyText(ModifyEvent event) {
                    Object source = event.getSource();
                    if (source != lastSource) {
                        urManager.demarcateUOW();
                        lastSource = source;
                    }
                }
            };
        }


        /**
         * applies the workaround
         *
         * @param control
         */
        public void workaround(Control control) {
            if (APPLY_WORKAROUND) {
                if (control instanceof Combo) {
                    ((Combo) control).addModifyListener(modifyListener);
                }
            }
        }
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-05	10374/1	emma	VBM:2005111705 Interim commit

 13-Dec-05	10345/1	adrianj	VBM:2005111601 Add style rule view

 13-Nov-05	9896/1	geoff	VBM:2005101906 Avoid using JDOM in MCS at runtime: rewrite runtime XML device repository

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-Feb-04	2924/4	eduardo	VBM:2004021003 codestyle'n'typos fixes

 12-Feb-04	2924/2	eduardo	VBM:2004021003 editor actions demarcate UndoRedo UOWs

 ===========================================================================
*/
