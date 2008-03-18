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

package com.volantis.map.ics.imageprocessor.convertor;

/**
 * Rule values.
 */
public interface ImageRule {

    int UNKNOWN_RULE = 0;

    int MONOCHROME = 1;

    int GREY4 = 2;

    int GREY16 = 3;

    int GREY256 = 4;

    int GREY65536 = 5;

    int COLOUR256 = 6;

    int TRUECOLOUR = 7;

    int INDEXEDCOLOURWITHPALETTE = 8;

    int INDEXEDGREY256 = 9;
}
