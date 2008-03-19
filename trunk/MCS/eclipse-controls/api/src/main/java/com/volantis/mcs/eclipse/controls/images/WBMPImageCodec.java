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
 * $Header: /src/voyager/com/volantis/mcs/gui/repository/components/WBMPImageCodec.java,v 1.4 2003/03/20 15:15:31 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 10-Aug-01    Paul            VBM:2001072505 - Created.
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug 
 *                              statements in if(logger.isDebugEnabled()) block
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.eclipse.controls.images;

import com.sun.media.jai.codec.*;

import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class WBMPImageCodec
        extends ImageCodec {

    public WBMPImageCodec() {
    }

    private static WBMPImageCodec registered;

    static {
        register();
    }

    public static void register() {
        if (registered == null) {
            registered = new WBMPImageCodec();
            registerCodec(registered);
        }
    }

    public String getFormatName() {
        return "wbmp"; //$NON-NLS-1$
    }

    /**
     * Returns <code>Object.class</code> to indicate that no paramters are
     * required for decoding.
     */
    public Class getDecodeParamClass() {
        return Object.class;
    }

    protected ImageDecoder createImageDecoder(InputStream src,
                                              ImageDecodeParam param) {
        return new WBMPImageDecoder(new ForwardSeekableStream(src), null);
    }

    protected ImageDecoder createImageDecoder(SeekableStream src,
                                              ImageDecodeParam param) {
        return new WBMPImageDecoder(src, null);
    }

    public int getNumHeaderBytes() {
        return 2;
    }

    /* This is not needed for WBMP type 0 but will be needed in future.
       public boolean isFormatRecognized (SeekableStream input) {
       return (readMultiByteInteger (input) == 0
       && readMultiByteInteger (input) == 0);
       }
    */

    /**
     * A WBMP type 0 file starts with two 0 bytes followed by multi-byte
     * width and height fields.
     */
    public boolean isFormatRecognized(byte[] header) {
        return (header[0] == 0 && header[1] == 0);
    }

    /**
     * Returns <code>null</code> to indicate that encoding is not supported.
     */
    public Class getEncodeParamClass() {
        return null;
    }

    public boolean canEncodeImage(RenderedImage image,
                                  ImageEncodeParam param) {
        return false;
    }

    public ImageEncoder createImageEncoder(OutputStream destination,
                                           ImageEncodeParam param) {
        throw new UnsupportedOperationException("Encoding not supported"); //$NON-NLS-1$
    }

    public static int readMultiByteInteger(InputStream input)
            throws IOException {

        int value = 0;
        int b;

        while ((b = input.read()) > 0) {
            value = (value << 7) + (b & 0x7f);
            if ((b & 0x80) == 0) {
                return value;
            }
        }

        return -1;
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
