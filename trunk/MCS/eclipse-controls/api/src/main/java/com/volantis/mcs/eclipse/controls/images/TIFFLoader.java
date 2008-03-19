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
package com.volantis.mcs.eclipse.controls.images;

import com.sun.media.jai.codec.FileSeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.SeekableStream;
import com.sun.media.jai.codec.TIFFDecodeParam;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;

import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;

import java.io.File;
import java.io.IOException;

import javax.media.jai.PlanarImage;
import javax.media.jai.RenderedImageAdapter;


/**
 * Load a TIFF image using JAI and return it as SWT ImageData
 */
public class TIFFLoader {
    /**
     * Load a TIFF image from a file and return it as SWT image data.
     * @param input the <code>File</code> containing the image
     * @return an SWT <code>ImageData</code> version of the image data
     */
    public ImageData load(File input) throws IOException  {

        SeekableStream s = new FileSeekableStream(input);

        TIFFDecodeParam param = new TIFFDecodeParam();
        param.setDecodePaletteAsShorts( false );
        ImageDecoder dec = ImageCodec.createImageDecoder("tiff", s, param); //$NON-NLS-1$
        int imageToLoad = 0;
        RenderedImageAdapter planarImage = new RenderedImageAdapter(
                                    dec.decodeAsRenderedImage( imageToLoad ));

        return readPlanarImage( planarImage );
    }

    /**
     * Create an SWT ImageData from a JAI PlanarImage
     * @param image the PlanarImage to convert
     * @return the ImageData representation of the PlanarImage
     */    
    private ImageData readPlanarImage( PlanarImage image ) {
        ColorModel cm = image.getColorModel();
        int width = image.getWidth();
        int height = image.getHeight();
        PaletteData palette = null;
        ImageData imageData = null;
        
        // Image is indexed and has a palette.
        if ( cm instanceof IndexColorModel) {
            
            // Create SWT flavour palette from the colour model
            IndexColorModel icm = (IndexColorModel)cm;
            int colourCount = icm.getMapSize();
            byte[][] data = new byte[3][colourCount];
            icm.getReds(data[0]);
            icm.getGreens(data[1]);
            icm.getBlues(data[2]);
            RGB[] colours = new RGB[ colourCount ];
            for( int i = 0; i < colourCount; i++ ) {
                int red = data[0][i] & 0xFF;
                int green = data[1][i] & 0xFF;
                int blue = data[2][i] & 0xFF;
                colours[i] = new RGB( red, green, blue );                
            }
            palette = new PaletteData( colours );
            
            // Create indexed image            
            imageData = new ImageData( width, height, 
                                       icm.getPixelSize(), 
                                       palette );
                                       
            // Copy the pixel data
            copyIndexed( imageData, image.getData() );
            
        // Greyscale if 8 bits per pixel but not indexed            
        } else if( cm.getPixelSize() == 8 ) {              
            // Create greyscale palette
            RGB[] colours = new RGB[ 256 ];
            for( int i = 0; i < 256; i++ ) {
                colours[i] = new RGB( i, i, i );
            }
            palette = new PaletteData( colours );
            
            // Create greyscale image
            imageData = new ImageData( width, height, 8, palette );
            
            // Copy pixel data
            copyIndexed( imageData, image.getData() );
                                                  
        // Monochrome if 1 bit per pixel
        } else if( cm.getPixelSize() == 1 ) {
            // Create monochrome palette
            RGB[] colours = new RGB[ 2 ];
            colours[0] = new RGB( 255, 255, 255);
            colours[1] = new RGB( 0, 0, 0 );
            palette = new PaletteData( colours );

            // Create monochrome image
            imageData = new ImageData( width, height, 1, palette );
            
            // Copy pixel data
            copyIndexed( imageData, image.getData() );
            
        // True colour if 24 bits per pixel
        } else if( cm.getPixelSize() == 24 ) {
            palette = new PaletteData(0xFF0000, 0xFF00, 0xFF);
            imageData = new ImageData( width,height,24,palette );
            copyTrueColour( imageData, image.getData() );
        }
        
        // this will be null if we could not determine the type of
        // the image.
        return imageData;
    }

    /**
     * Copy the pixel values from an indexed tiled image into an ImageData.
     * This assumes that all pixel data is in sample 0 of the image.
     */    
    private void copyIndexed( ImageData dest, Raster src ) {
        int count = dest.height * dest.width;
        int[] pixels = new int[ count ];
        pixels = src.getSamples( 0, 0, dest.width, dest.height, 0, pixels );
        dest.setPixels( 0, 0, count, pixels, 0 );
    }

    /**
     * Copy a true colour image from a TiledImage to an ImageData.
     * This assumes that samples 0,1 and 2 are red, green and blue
     * respectively.
     */
    private void copyTrueColour( ImageData dest, Raster src ) {
        int red, green, blue, pixel;
        for( int y = 0; y < dest.height; y++ ) {
            for( int x = 0; x < dest.width; x++ ) {
                red = src.getSample( x, y, 0 );
                green = src.getSample( x, y, 1 );
                blue = src.getSample( x, y, 2 );
                pixel = ( red << 16 ) + ( green << 8 ) + blue;
                dest.setPixel( x, y, pixel );                                        
            }
        }                    
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

 07-Nov-03	1795/2	pcameron	VBM:2003102804 Changed the ImageProvider exception processing

 06-Nov-03	1801/3	steve	VBM:2003102804 Truncate TIFF palette for monochrome

 05-Nov-03	1801/1	steve	VBM:2003102804 TIFF Image Loader

 ===========================================================================
*/
