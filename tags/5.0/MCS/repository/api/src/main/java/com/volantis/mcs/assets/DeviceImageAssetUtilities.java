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
 * This code was automatically generated by PropertyValueLookupUtilities
 * on 3/14/08 7:12 PM
 *
 * YOU MUST NOT MODIFY THIS FILE
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.assets;

import java.util.HashMap;
import java.util.Map;

public class DeviceImageAssetUtilities {

  /**
   * An array of the allowable values for the image asset localSrc.
   */
  private static Object [] localSrcArray;

  /**
   * A bidirectional map between internal and external representations of the
   * image asset localSrc.
   */
  private static Map localSrcMap;

  /**
   * An array of the allowable values for the image asset rendering.
   */
  private static Object [] renderingArray;

  /**
   * A bidirectional map between internal and external representations of the
   * image asset rendering.
   */
  private static Map renderingMap;

  /**
   * An array of the allowable values for the image asset encoding.
   */
  private static Object [] encodingArray;

  /**
   * A bidirectional map between internal and external representations of the
   * image asset encoding.
   */
  private static Map encodingMap;

  static {
    Object internal;
    String external;

    localSrcArray = new Boolean [] {
      new Boolean (false),
      new Boolean (true),
    };

    localSrcMap = new HashMap ();

    external = "remote";
    localSrcMap.put (localSrcArray [0], external);
    localSrcMap.put (external, localSrcArray [0]);

    external = "local";
    localSrcMap.put (localSrcArray [1], external);
    localSrcMap.put (external, localSrcArray [1]);

    renderingArray = new Integer [] {
      new Integer (ImageAsset.COLOR),
      new Integer (ImageAsset.MONOCHROME),
    };

    renderingMap = new HashMap ();

    external = "color";
    renderingMap.put (renderingArray [0], external);
    renderingMap.put (external, renderingArray [0]);

    external = "monochrome";
    renderingMap.put (renderingArray [1], external);
    renderingMap.put (external, renderingArray [1]);

    encodingArray = new Integer [] {
      new Integer (ImageAsset.BMP),
      new Integer (ImageAsset.GIF),
      new Integer (ImageAsset.JPEG),
      new Integer (ImageAsset.PJPEG),
      new Integer (ImageAsset.PNG),
      new Integer (ImageAsset.TIFF),
      new Integer (ImageAsset.WBMP),
      new Integer (ImageAsset.VIDEOTEX),
    };

    encodingMap = new HashMap ();

    external = "bmp";
    encodingMap.put (encodingArray [0], external);
    encodingMap.put (external, encodingArray [0]);

    external = "gif";
    encodingMap.put (encodingArray [1], external);
    encodingMap.put (external, encodingArray [1]);

    external = "jpeg";
    encodingMap.put (encodingArray [2], external);
    encodingMap.put (external, encodingArray [2]);

    external = "pjpeg";
    encodingMap.put (encodingArray [3], external);
    encodingMap.put (external, encodingArray [3]);

    external = "png";
    encodingMap.put (encodingArray [4], external);
    encodingMap.put (external, encodingArray [4]);

    external = "tiff";
    encodingMap.put (encodingArray [5], external);
    encodingMap.put (external, encodingArray [5]);

    external = "wbmp";
    encodingMap.put (encodingArray [6], external);
    encodingMap.put (external, encodingArray [6]);

    external = "videotex";
    encodingMap.put (encodingArray [7], external);
    encodingMap.put (external, encodingArray [7]);
  }

  public static Object [] getLocalSrcArray () {
    return localSrcArray;
  }

  public static Map getLocalSrcMap () {
    return localSrcMap;
  }

  public static Object [] getRenderingArray () {
    return renderingArray;
  }

  public static Map getRenderingMap () {
    return renderingMap;
  }

  public static Object [] getEncodingArray () {
    return encodingArray;
  }

  public static Map getEncodingMap () {
    return encodingMap;
  }
}
