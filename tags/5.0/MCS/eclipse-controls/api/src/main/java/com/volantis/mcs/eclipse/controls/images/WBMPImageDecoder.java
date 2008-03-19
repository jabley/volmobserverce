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
 * $Header: /src/voyager/com/volantis/mcs/gui/repository/components/WBMPImageDecoder.java,v 1.2 2002/03/18 12:41:15 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 10-Aug-01    Paul            VBM:2001072505 - Created.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.eclipse.controls.images;

import com.sun.media.jai.codec.ImageDecodeParam;
import com.sun.media.jai.codec.ImageDecoderImpl;
import com.sun.media.jai.codec.SeekableStream;

import java.awt.image.RenderedImage;
import java.io.IOException;

public class WBMPImageDecoder
        extends ImageDecoderImpl {

    public WBMPImageDecoder(SeekableStream input,
                            ImageDecodeParam param) {
        super(input, param);
    }

    public RenderedImage decodeAsRenderedImage(int page)
            throws IOException {

        if (page != 0) {
            throw new IOException("Illegal page requested from WBMP image."); //$NON-NLS-1$
        }

        return new WBMPImage(input);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 06-Nov-03	1795/1	pcameron	VBM:2003102804 Added ImageProvider infrastructure and implementation

 ===========================================================================
*/
