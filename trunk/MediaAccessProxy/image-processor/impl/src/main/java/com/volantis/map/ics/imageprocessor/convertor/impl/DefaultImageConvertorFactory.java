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
 * (c) Copyright Volantis Systems Ltd. 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.map.ics.imageprocessor.convertor.impl;

import com.volantis.map.ics.imageprocessor.convertor.ImageConvertor;
import com.volantis.map.ics.imageprocessor.convertor.ImageConvertorFactory;
import com.volantis.map.ics.imageprocessor.convertor.ImageRule;

import java.util.HashMap;
import java.util.Map;

public class DefaultImageConvertorFactory extends ImageConvertorFactory {

    private static Map convertorMap;

    static {
        convertorMap = new HashMap();
    }

    // javadoc inherited
    public ImageConvertor getImageConvertor(int type) {
        Integer key = new Integer(type);
        ImageConvertor cvt = (ImageConvertor) convertorMap.get(key);
        if (cvt == null) {
            switch (type) {
                case ImageRule.MONOCHROME:
                    cvt = new GreyscaleNConvertor(1);
                    break;
                case ImageRule.GREY4:
                    cvt = new GreyscaleNConvertor(2);
                    break;
                case ImageRule.GREY16:
                    cvt = new GreyscaleNConvertor(4);
                    break;
                case ImageRule.GREY256:
                case ImageRule.GREY65536:
                    cvt = new GreyscaleConvertor();
                    break;
                case ImageRule.COLOUR256:
                    cvt = new Indexed256Convertor();
                    break;
                case ImageRule.TRUECOLOUR:
                    cvt = new RGBConvertor();
                    break;

                case ImageRule.INDEXEDCOLOURWITHPALETTE:
                    cvt = new AdoptingConvertor();
                    break;

                case ImageRule.INDEXEDGREY256:
                    cvt = new IndexedGreyscaleConvertor();
                    break;
            }
            convertorMap.put(key, cvt);
        }
        return cvt;
    }

}
