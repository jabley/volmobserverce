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

package com.volantis.map.ics.imageprocessor.tool.clip;

import junit.framework.TestCase;

import com.volantis.map.ics.configuration.ImageConstants;

public class ClipXTestCase extends TestCase {

    final static int SIZE = 100;

    public void testLeft0() {

        ClipX clipper = new ClipX(SIZE, 0, 19);
        clipper.clipTo(200);
        assertEquals(0, clipper.getClipOffset());
        assertEquals(SIZE, clipper.getClipSize());

        clipper.clipTo(40);
        assertEquals(0, clipper.getClipOffset());
        assertEquals(40, clipper.getClipSize());

        clipper.clipTo(5);
        assertEquals(0, clipper.getClipOffset());
        assertEquals(20, clipper.getClipSize());
    }

    public void testRight0() {

        ClipX clipper = new ClipX(SIZE, 80, ImageConstants.NO_CLIP_RIGHT);
        clipper.clipTo(200);
        assertEquals(0, clipper.getClipOffset());
        assertEquals(SIZE, clipper.getClipSize());

        clipper.clipTo(40);
        assertEquals(60, clipper.getClipOffset());
        assertEquals(40, clipper.getClipSize());

        clipper.clipTo(5);
        assertEquals(80, clipper.getClipOffset());
        assertEquals(20, clipper.getClipSize());
    }

    public void testNoClip() {
        ClipX clipper = new ClipX(SIZE, ImageConstants.NO_CLIP_LEFT,
                                  ImageConstants.NO_CLIP_RIGHT);
        clipper.clipTo(200);
        assertEquals(0, clipper.getClipOffset());
        assertEquals(SIZE, clipper.getClipSize());

        clipper.clipTo(40);
        assertEquals(0, clipper.getClipOffset());
        assertEquals(SIZE, clipper.getClipSize());

        clipper.clipTo(5);
        assertEquals(0, clipper.getClipOffset());
        assertEquals(SIZE, clipper.getClipSize());
    }

    public void testRoundEqual() {
        ClipX clipper = new ClipX(SIZE, 25, 74);

        // the left clip should be 0
        clipper.clipTo(99);
        assertEquals(0, clipper.getClipOffset());
        assertEquals(99, clipper.getClipSize());

        clipper.clipTo(200);
        assertEquals(0, clipper.getClipOffset());
        assertEquals(SIZE, clipper.getClipSize());

        clipper.clipTo(5);
        assertEquals(25, clipper.getClipOffset());
        assertEquals(50, clipper.getClipSize());

    }

    public void testRoundLeftBigger() {
        ClipX clipper = new ClipX(SIZE, 26, 75);

        // the left clip should be 1
        clipper.clipTo(99);
        assertEquals(1, clipper.getClipOffset());
        assertEquals(99, clipper.getClipSize());

        clipper.clipTo(200);
        assertEquals(0, clipper.getClipOffset());
        assertEquals(SIZE, clipper.getClipSize());

        clipper.clipTo(5);
        assertEquals(26, clipper.getClipOffset());
        assertEquals(50, clipper.getClipSize());

    }

    public void testRoundRightBigger() {
        ClipX clipper = new ClipX(SIZE, 24, 73);

        // the left clip should be 0
        clipper.clipTo(99);
        assertEquals(0, clipper.getClipOffset());
        assertEquals(99, clipper.getClipSize());

        clipper.clipTo(200);
        assertEquals(0, clipper.getClipOffset());
        assertEquals(SIZE, clipper.getClipSize());

        clipper.clipTo(5);
        assertEquals(24, clipper.getClipOffset());
        assertEquals(50, clipper.getClipSize());
    }

    public void testIllegal() {
        ClipX clipper = null;
        clipper = new ClipX(SIZE, 10, 9);
        try {
            clipper.clipTo(10);
            fail("emprty protected area accepted");
        } catch (IllegalArgumentException ign) {
        }

        clipper = new ClipX(SIZE, SIZE, ImageConstants.NO_CLIP_RIGHT);
        try {
            clipper.clipTo(10);
            fail("Left bound outside area accepted");
        } catch (IllegalArgumentException ign) {
        }

        clipper = new ClipX(SIZE, ImageConstants.NO_CLIP_LEFT, -2);
        try {
            clipper.clipTo(10);
            fail("Rigth bound outside area accepted");
        } catch (IllegalArgumentException ign) {
        }
    }

    public void testAdjust() {
        ClipX clipper = new ClipX(SIZE, 40, 79);
        clipper.clipTo(40);
        assertEquals(40, clipper.getClipOffset());
        assertEquals(40, clipper.getClipSize());

        //frame to adjust on the left of preserved area and smaller
        clipper.adjustTo(20, 40);
        assertEquals(40, clipper.getClipOffset());
        assertEquals(20, clipper.getClipSize());

        //frame to adjust on the left of preserved area and bigger
        clipper.clipTo(40);
        clipper.adjustTo(20, 70);
        assertEquals(40, clipper.getClipOffset());
        assertEquals(40, clipper.getClipSize());

        //frame to adjust starts inside preserved area and bigger
        clipper.clipTo(40);
        clipper.adjustTo(50, 40);
        assertEquals(50, clipper.getClipOffset());
        assertEquals(30, clipper.getClipSize());

        //frame to adjust starts inside preserved area and smaller
        clipper.clipTo(40);
        clipper.adjustTo(50, 20);
        assertEquals(50, clipper.getClipOffset());
        assertEquals(20, clipper.getClipSize());

        //frame outside of preserved area
        clipper.clipTo(40);
        clipper.adjustTo(10, 20);
        assertEquals(10, clipper.getClipOffset());
        assertEquals(1, clipper.getClipSize());

        clipper.clipTo(40);
        clipper.adjustTo(90, 50);
        assertEquals(90, clipper.getClipOffset());
        assertEquals(1, clipper.getClipSize());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 04-Nov-05	554/4	pszul	VBM:2005102504 intendation corrected to 4 spaces

 04-Nov-05	554/2	pszul	VBM:2005102504 intelligent clipping implemented

 ===========================================================================
 */
