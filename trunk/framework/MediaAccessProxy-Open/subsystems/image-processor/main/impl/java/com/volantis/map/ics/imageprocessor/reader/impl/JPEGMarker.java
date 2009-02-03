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
 * (c) Copyright Volantis Systems Ltd. 2008.
 * ----------------------------------------------------------------------------
 */

package com.volantis.map.ics.imageprocessor.reader.impl;

/**
 * Represents a one-byte marker used inside JPEG files
 */
public class JPEGMarker {

    /** Internal representation of marker's value */
    private int value = -1;

    /** Internal representation of marker's name */
    private String name = null;

    /** Represents an unknown/unsupported marker */
    public static final JPEGMarker UNKNOWN = new JPEGMarker(-1, "[unknown]");

    /** Internal table for fast identification of markers */
    private static final JPEGMarker[] MARKERS = new JPEGMarker[0x100];
    static {
        for (int i = 0; i < 0xff; MARKERS[i++] = UNKNOWN);
    }

    private static JPEGMarker create(int value, String name) {
        // These two exception are just sanity checks to detect
        // early any stupid mistakes in the code.
        // No localization as these are internal exceptions
        if (value < 0 || value > 0xff) {
            throw new IllegalArgumentException("JPEGMarker can only hold values from the [0, 0xff] range");
        }
        if (!MARKERS[value].equals(UNKNOWN)) {
            throw new IllegalStateException("JPEGMarker already created");
        }
        JPEGMarker marker = new JPEGMarker(value, name);
        MARKERS[value] = marker;
        return marker;
    }

    // We do not support all markers, just those listed below

    /** Start of Frame 0 */
    public static final JPEGMarker SOF0 = create(0xc0, "SOF0");
    /** Huffman table */
    public static final JPEGMarker DHT = create(0xc4, "DHT");
     /** Application markers */
    public static final JPEGMarker APP0 = create(0xe0, "APP0");
    public static final JPEGMarker APP1 = create(0xe1, "APP1");
    public static final JPEGMarker APP2 = create(0xe2, "APP2");
    public static final JPEGMarker APP3 = create(0xe3, "APP3");
    public static final JPEGMarker APP4 = create(0xe4, "APP4");
    public static final JPEGMarker APP5 = create(0xe5, "APP5");
    public static final JPEGMarker APP6 = create(0xe6, "APP6");
    public static final JPEGMarker APP7 = create(0xe7, "APP7");
    public static final JPEGMarker APP8 = create(0xe8, "APP8");
    public static final JPEGMarker APP9 = create(0xe9, "APP9");
    public static final JPEGMarker APP10 = create(0xea, "APP10");
    public static final JPEGMarker APP11 = create(0xeb, "APP11");
    public static final JPEGMarker APP12 = create(0xec, "APP12");
    public static final JPEGMarker APP13 = create(0xed, "APP13");
    public static final JPEGMarker APP14 = create(0xee, "APP14");
    public static final JPEGMarker APP15 = create(0xef, "APP15");
    /** Start of Image marker */
    public static final JPEGMarker SOI = create(0xd8, "SOI");
    /** Start Of Scan*/
    public static final JPEGMarker SOS = create(0xda, "SOS");
    /** Define quantization table */
    public static final JPEGMarker DQT = create(0xdb, "DQT");
    /** Define Restart Interval */
    public static final JPEGMarker DRI = create(0xdd, "DRI");
    /** Comment marker */
    public static final JPEGMarker COM = create(0xfe, "COM");

    private JPEGMarker() { }

    private JPEGMarker(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public boolean equals(int value) {
        return (this.value == value);
    }

    public boolean equals(byte value) {
        return (this.value == (value & 0xff));
    }

    /**
     * Returns JPEGMarker representing marker identified by the specified byte
     * or JPEGMarker.UNKNOWN if it's not a known marker
     */
    public static JPEGMarker identify(int value) {
        if (value < 0 || value > 0xff) {
            return UNKNOWN;
        }
        return MARKERS[value];
    }

    /**
     * Returns JPEGMarker representing marker identified by the specified byte
     * or JPEGMarker.UNKNOWN if it's not a known marker
     */
    public static JPEGMarker identify(byte value) {
        return MARKERS[value & 0xff];
    }

    public byte byteValue() {
        return (byte)value;
    }

    /**
     * Returns true if this marker is a known/supported marker
     */
    public boolean isKnown() {
        // Using == and != operators is safe, because the only legal instances
        // are those we keep in MARKERS table
        return this != UNKNOWN;
    }

    /**
     * Returns true if this marker represents applications segment marker
     */
    public boolean isApplicationMarker() {
        return (value >= APP0.value && value <= APP15.value);
    }

    // javadoc inherited
    public String toString() {
        return isKnown() ?  name + " (0x" + Integer.toHexString(value & 0xff) + ")"
                : UNKNOWN.name;
    }
}
