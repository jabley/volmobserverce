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
 * $Header: 
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 17-Jan-02    Adrian          VBM:2001121003 - Class for discribing how a
 *                              chart should be displayed.  Used as utility
 *                              by ChartElement.  Replaces
 *                              com/volantis/mcs/utilities/..
 *                              ..ChartDisplayDefinition.java
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * 11-Mar-03    Steve           VBM:2003022403 - Added public api doclet tags
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.papi.impl;

import com.volantis.styling.Styles;

import java.awt.*;

class ChartDefinition {

    public static final Color DEFAULT_BGCOLOR = Color.white;
    public static final Color DEFAULT_PENCOLOR = Color.black;
    public static final Color[] DEFAULT_FGCOLORS = {Color.red,
            Color.blue,
            Color.yellow
    };
    public static final Color DEFAULT_GRIDCOLOR = Color.black;
    public static final Color DEFAULT_BORDERCOLOR = Color.black;
    public static final String DEFAULT_FONTNAME = "Courier";
    public static final int DEFAULT_FONTSIZE = 10;
    public static final float DEFAULT_XANGLE = (float) 0.0;
    public static final float DEFAULT_YANGLE = (float) 0.0;

    private String fontName = ChartDefinition.DEFAULT_FONTNAME;
    private int fontSize = ChartDefinition.DEFAULT_FONTSIZE;
    private int xInterval = 0; // 0 = unset
    private int yInterval = 0; // 0 = unset
    private Color bgColor = ChartDefinition.DEFAULT_BGCOLOR;
    private Color gridColor = ChartDefinition.DEFAULT_GRIDCOLOR;
    private Color borderColor = ChartDefinition.DEFAULT_BORDERCOLOR;
    private Color[] fgColors = ChartDefinition.DEFAULT_FGCOLORS;
    private String type = "column";
    private Color penColor = ChartDefinition.DEFAULT_PENCOLOR;
    private float xAngle = ChartDefinition.DEFAULT_XANGLE;
    private float yAngle = ChartDefinition.DEFAULT_YANGLE;
    private int height = 100;
    private int width = 100;
    private boolean adornments = true;
    private int heightHint;
    private int widthHint;
    private String xTitle;
    private String yTitle;
    boolean labelValues = true;
    private Styles styles;

    /**
     * Get the value of labelValues.
     *
     * @return Value of labelValues.
     */
    boolean labelValues() {
        return labelValues;
    }

    /**
     * Set the value of labelValues.
     *
     * @param v Value to assign to labelValues.
     */
    void setLabelValues(boolean v) {
        this.labelValues = v;
    }


    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("fontName: ").append(fontName).append("\n");
        sb.append("fontSize: ").append(fontSize).append("\n");
        sb.append("xInterval: ").append(xInterval).append("\n");
        sb.append("yInterval: ").append(yInterval).append("\n");
        sb.append("bgColor: ").append(bgColor).append("\n");
        sb.append("gridColor: ").append(gridColor).append("\n");
        sb.append("borderColor: ").append(borderColor).append("\n");
        sb.append("penColor: ").append(penColor).append("\n");
        sb.append("type: ").append(type).append("\n");
        sb.append("xAngle: ").append(xAngle).append("\n");
        sb.append("yAngle: ").append(yAngle).append("\n");
        sb.append("xTitle: ").append(xTitle).append("\n");
        sb.append("yTitle: ").append(yTitle).append("\n");
        sb.append("adornments: ").append(adornments).append("\n");
        sb.append("heightHint: ").append(heightHint).append("\n");
        sb.append("widthHint: ").append(widthHint).append("\n");

        return sb.toString();


    }

    /**
     * Get the value of yTitle.
     *
     * @return Value of yTitle.
     */
    String getYTitle() {
        return yTitle;
    }

    /**
     * Set the value of yTitle.
     *
     * @param v Value to assign to yTitle.
     */
    void setYTitle(String v) {
        this.yTitle = v;
    }

    /**
     * Get the value of xTitle.
     *
     * @return Value of xTitle.
     */
    String getXTitle() {
        return xTitle;
    }

    /**
     * Set the value of xTitle.
     *
     * @param v Value to assign to xTitle.
     */
    void setXTitle(String v) {
        this.xTitle = v;
    }

    /**
     * Get the value of widthHint.
     *
     * @return Value of widthHint.
     */
    int getWidthHint() {
        return widthHint;
    }

    /**
     * Set the value of widthHint.
     *
     * @param v Value to assign to widthHint.
     */
    void setWidthHint(int v) {
        this.widthHint = v;
    }

    /**
     * Get the value of heightHint.
     *
     * @return Value of heightHint.
     */
    int getHeightHint() {
        return heightHint;
    }

    /**
     * Set the value of heightHint.
     *
     * @param v Value to assign to heightHint.
     */
    void setHeightHint(int v) {
        this.heightHint = v;
    }

    /**
     * Get the value of adornments.
     *
     * @return Value of adornments.
     */
    boolean hasAdornments() {
        return adornments;
    }

    /**
     * Set the value of adornments.
     *
     * @param v Value to assign to adornments.
     */
    void setAdornments(boolean v) {
        this.adornments = v;
    }

    /**
     * Get the value of width.
     *
     * @return Value of width.
     */
    int getWidth() {
        return width;
    }

    /**
     * Set the value of width.
     *
     * @param v Value to assign to width.
     */
    void setWidth(int v) {
        this.width = v;
    }

    /**
     * Get the value of height.
     *
     * @return Value of height.
     */
    int getHeight() {
        return height;
    }

    /**
     * Set the value of height.
     *
     * @param v Value to assign to height.
     */
    void setHeight(int v) {
        this.height = v;
    }

    /**
     * Get the value of borderColor.
     *
     * @return Value of borderColor.
     */
    Color getBorderColor() {
        return borderColor;
    }

    /**
     * Set the value of borderColor.
     *
     * @param v Value to assign to borderColor.
     */
    void setBorderColor(Color v) {
        this.borderColor = v;
    }

    /**
     * Get the value of penColor.
     *
     * @return Value of penColor.
     */
    Color getPenColor() {
        return penColor;
    }

    /**
     * Set the value of penColor.
     *
     * @param v Value to assign to penColor.
     */
    void setPenColor(Color v) {
        this.penColor = v;
    }

    /**
     * Get the value of type.
     *
     * @return Value of type.
     */
    String getType() {
        return type;
    }

    /**
     * Set the value of type.
     *
     * @param v Value to assign to type.
     */
    void setType(String v) {
        this.type = v;
    }

    /**
     * Get the value of fgColors.
     *
     * @return Value of fgColors.
     */
    Color[] getFgColors() {
        return fgColors;
    }

    /**
     * Set the value of fgColors.
     *
     * @param v Value to assign to fgColors.
     */
    void setFgColors(Color[] v) {
        this.fgColors = v;
    }

    /**
     * Get the value of yAngle.
     *
     * @return Value of yAngle.
     */
    float getYAngle() {
        return yAngle;
    }

    /**
     * Set the value of yAngle.
     *
     * @param v Value to assign to yAngle.
     */
    void setYAngle(float v) {
        this.yAngle = v;
    }

    /**
     * Get the value of xAngle.
     *
     * @return Value of xAngle.
     */
    float getXAngle() {
        return xAngle;
    }

    /**
     * Set the value of xAngle.
     *
     * @param v Value to assign to xAngle.
     */
    void setXAngle(float v) {
        this.xAngle = v;
    }

    /**
     * Get the value of gridColor.
     *
     * @return Value of gridColor.
     */
    Color getGridColor() {
        return gridColor;
    }

    /**
     * Set the value of gridColor.
     *
     * @param v Value to assign to gridColor.
     */
    void setGridColor(Color v) {
        this.gridColor = v;
    }


    /**
     * Get the value of bgColor.
     *
     * @return Value of bgColor.
     */
    Color getBgColor() {
        return bgColor;
    }

    /**
     * Set the value of bgColor.
     *
     * @param v Value to assign to bgColor.
     */
    void setBgColor(Color v) {
        this.bgColor = v;
    }

    /**
     * Get the value of xInterval.
     *
     * @return Value of xInterval.
     */
    int getXInterval() {
        return xInterval;
    }

    /**
     * Set the value of xInterval.
     *
     * @param v Value to assign to xInterval.
     */
    void setXInterval(int v) {
        this.xInterval = v;
    }

    /**
     * Get the value of yInterval.
     *
     * @return Value of yInterval.
     */
    int getYInterval() {
        return yInterval;
    }

    /**
     * Set the value of yInterval.
     *
     * @param v Value to assign to yInterval.
     */
    void setYInterval(int v) {
        this.yInterval = v;
    }

    /**
     * Get the value of fontSize.
     *
     * @return Value of fontSize.
     */
    int getFontSize() {
        return fontSize;
    }

    /**
     * Set the value of fontSize.
     *
     * @param v Value to assign to fontSize.
     */
    void setFontSize(int v) {
        this.fontSize = v;
    }

    /**
     * Get the value of fontName.
     *
     * @return Value of fontName.
     */
    String getFontName() {
        return fontName;
    }

    /**
     * Set the value of fontName.
     *
     * @param v Value to assign to fontName.
     */
    void setFontName(String v) {
        this.fontName = v;
    }

    Styles getStyles() {
        return styles;
    }

    void setStyles(Styles styles) {
        this.styles = styles;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 18-May-05	8196/2	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
