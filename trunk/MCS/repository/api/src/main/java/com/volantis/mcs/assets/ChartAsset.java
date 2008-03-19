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
 * $Header: /src/voyager/com/volantis/mcs/assets/ChartAsset.java,v 1.17 2003/03/24 16:35:25 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 09-Jul-01    Paul            VBM:2001070902 - Added this header, sorted out
 *                              the copyright, changed default values for
 *                              xTitle and yTitle to null and made the equals
 *                              method compare the contents of the string
 *                              parameters and not their references.
 * 16-Jul-01    Paul            VBM:2001070508 - Added identityMatches method.
 * 24-Oct-01    Paul            VBM:2001092608 - Removed the identityMatches
 *                              method and added the createIdentity method.
 *                              Also made sure that any changes to those
 *                              fields which form part of this objects identity
 *                              causes the cached identity to be discarded.
 * 14-Jan-02    Paul            VBM:2002011414 - Added annotations to indicate
 *                              which of the properties of this class
 *                              constitute its identity.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from 
 *                              class to string.
 * 22-Mar-02    Adrian          VBM:2002031503 - add annotations for auto
 *                              generated accessors
 * 02-Apr-02    Mat             VBM:2002022009 - Added 
 *                              @mariner-generate-imd-accessor and also added
 *                              a constructor that takes an Attributes class.
 * 03-Apr-02    Mat             VBM:2002022009 - Removed constructor that 
 *                              took an Attributes class.
 * 26-Apr-02    Mat             VBM:2002040814 - Added 
 *                              @mariner-generate-remote-accessor.
 * 08-May-02    Paul            VBM:2002050305 - Added constructor which takes
 *                              an identity.
 * 17-May-02    Paul            VBM:2002050101 - Changed the names of some
 *                              annotations to make them more meaningful and
 *                              added annotations to constrain field lengths,
 *                              and flag the required fields.
 * 14-Jun-02    Allan           VBM:2002030615 - Added object-field-value 
 *                              javadoc for type property.
 * 26-Jul-02    Allan           VBM:2002072508 - Removed javadoc doclet
 *                              ignore field references. Modified the two
 *                              constructors that referenced assetGroupName
 *                              and value to not do this.
 * 09-Sep-02    Adrian          VBM:2002090901 - Updated annotations so that
 *                              xTitle and yTitle are respectively XTitle and
 *                              YTitle instead of both being YTitle.
 * 24-Mar-03    Steve           VBM:2003022403 - Added API doclet tags
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.assets;

import com.volantis.mcs.objects.RepositoryObjectIdentity;

/**
 * The ChartAsset class is the parent of all types of chart assets. It
 * provides common attributes and behaviour for all chart assets.
 *
 * @mariner-hidden-comment The following tags are used by a code generation
 * doclet to automatically generate an Identity class for this class. Each
 * tag specifies the name of a field which constitutes part of the identity.
 * The order of the fields is the order of the parameters to the Identity
 * classes constructor.
 *
 * @mariner-object-identity-field project
 * @mariner-object-identity-field name
 *
 * @mariner-object-guardian com.volantis.mcs.components.ChartComponent
 *
 * @mariner-generate-imd-accessor
 *
 * @mariner-generate-property-lookup
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 *
 * @deprecated See {@link com.volantis.mcs.policies}.
 *             This was deprecated in version 3.5.1.
 */
public class ChartAsset extends Asset {

    /**
     * Constant value indicating that a {@link ChartAsset} is a
     * line chart.
     */
    public static final String LINE_CHART = "line";

    /**
     * Constant used to indicate that a {@link ChartAsset} is a
     * legend.
     */
    public static final String LEGEND_CHART = "legend";

    /**
     * Constant used to indicate that a {@link ChartAsset} is a
     * column.
     */
    public static final String COLUMN_CHART = "column";

    /**
     * Constant used to indicate that a {@link ChartAsset} is a
     * bar chart.
     */
    public static final String BAR_CHART = "bar";

    /**
     * Constant used to indicate that a {@link ChartAsset} is a
     * pir chart.
     */
    public static final String PIE_CHART = "pie";

    /**
     * An approximation of the width of the image to be rendered
     * on the output device as a percentage of the original width.
     */
    private int widthHint;

    /**
     * An approximation of the height of the image to be rendered
     * on the output device as a percentage of the original height.
     */
    private int heightHint;

    /**
     * The interval at which markers will be placed on the x-axis.
     *
     * @mariner-object-field-xml-attribute XInterval
     */
    private int xInterval;

    /**
     * The interval at which markers will be placed on the y-axis.
     *
     * @mariner-object-field-xml-attribute YInterval
     */
    private int yInterval;

    /**
     * The type of ChartAsset. One of:
     * <ul>
     *  <li> {@link #BAR_CHART} </li>
     *  <li> {@link #COLUMN_CHART} </li>
     *  <li> {@link #LEGEND_CHART} </li>
     *  <li> {@link #LINE_CHART} </li>
     *  <li> {@link #PIE_CHART} </li>
     * </ul>
     *
     * @mariner-object-field-value ChartAsset.BAR_CHART
     * @mariner-object-field-value ChartAsset.COLUMN_CHART
     * @mariner-object-field-value ChartAsset.LEGEND_CHART
     * @mariner-object-field-value ChartAsset.LINE_CHART
     * @mariner-object-field-value ChartAsset.PIE_CHART
     *
     * @mariner-object-field-length 10
     */
    private String type;

    /**
     * The title to be used on the x axis of this chart.
     *
     * @mariner-object-field-xml-attribute XTitle
     * @mariner-object-field-length 64
     */
    private String xTitle;

    /**
     * The title to be used on the y axis of this chart.
     *
     * @mariner-object-field-xml-attribute YTitle
     * @mariner-object-field-length 64
     */
    private String yTitle;

    /**
     * Initialises a ChartAsset of type {@link #LINE_CHART} with all
     * other instance variables set to default values.
     */
    public ChartAsset () {
        this ((String)null);
    }

    /**
     * Initialises an instance with the supplied parameters.
     *
     * @param name the name to be given to this ChartAsset
     */
    public ChartAsset (String name) {
        this (name, null, null );
    }

    /**
     * Initialises an instance with the supplied parameters.
     *
     * @param name The name to be given to this ChartAsset.
     * @param xTitle The x axis title.
     * @param yTitle The y axis title.
     */
    public ChartAsset (String name, String xTitle, String yTitle) {
        this (name, 0, 0, 0, 0, LINE_CHART, xTitle, yTitle);
    }

    /**
     * Initialises an instance with the supplied parameters.
     *
     * @param name The name to be given to this ChartAsset.
     * @param widthHint Approximation of the width of the image
     *        to be rendered on the output device as a percentage of the
     *        original width.
     * @param heightHint Approximation of the height of the image to be
     *        rendered on the output device as a percentage of the original
     *        height.
     * @param xInterval The interval at which markers will be placed on the
     *        x-axis.
     * @param yInterval The interval at which markers will be placed on the
     *        y-axis.
     * @param type The type of ChartAsset.  One of:
     * <ul>
     *  <li> {@link #BAR_CHART} </li>
     *  <li> {@link #COLUMN_CHART} </li>
     *  <li> {@link #LEGEND_CHART} </li>
     *  <li> {@link #LINE_CHART} </li>
     *  <li> {@link #PIE_CHART} </li>
     * </ul>
     *
     * @param xTitle The title to be used for the x-axis.
     * @param yTitle The title to be used for the y-axis.
     */
    public ChartAsset (String name, int widthHint, int heightHint, int xInterval,
                     int yInterval, String type, String xTitle, String yTitle) {
        setName (name);
        setWidthHint (widthHint);
        setHeightHint (heightHint);
        setXInterval (xInterval);
        setYInterval (yInterval);
        setType (type);
        setXTitle (xTitle);
        setYTitle (yTitle);
    }

    /**
     * Create a new <code>ChartAsset</code>.
     * @param identity The identity of the <code>ChartAsset</code>.
     */
    public ChartAsset (ChartAssetIdentity identity) {
        super (identity);
    }

    /**
     * Returns the type of this ChartAsset.
     *
     * @return the type of ChartAsset. One of:
     * <ul>
     *  <li> {@link #BAR_CHART} </li>
     *  <li> {@link #COLUMN_CHART} </li>
     *  <li> {@link #LEGEND_CHART} </li>
     *  <li> {@link #LINE_CHART} </li>
     *  <li> {@link #PIE_CHART} </li>
     * </ul>
     */
    public String getType () {
        return type;
    }

    /**
     * Sets the ChartAsset type.
     *
     * @param type The type of ChartAsset.  Should use one
     * of the following constants:
     *
     * <ul>
     *  <li> {@link #BAR_CHART} </li>
     *  <li> {@link #COLUMN_CHART} </li>
     *  <li> {@link #LEGEND_CHART} </li>
     *  <li> {@link #LINE_CHART} </li>
     *  <li> {@link #PIE_CHART} </li>
     * </ul>
     */
    public void setType (String type) {
        this.type = type;
    }

    /**
     * Returns the title of the x-axis.
     *
     * @return Title of the x-axis.
     */
    public String getXTitle () {
        return xTitle;
    }

    /**
     * Sets the title of the x-axis.
     *
     * @param xTitle The title to be set.
     */
    public void setXTitle (String xTitle) {
        this.xTitle = xTitle;
    }

    /**
     * Returns the title of the y-axis.
     *
     * @return Title of the y-axis.
     */
    public String getYTitle () {
        return yTitle;
    }

    /**
     * Sets the title of the y-axis.
     *
     * @param yTitle The title to be set on the y-axis.
     */
    public void setYTitle (String yTitle) {
        this.yTitle = yTitle;
    }

    /**
     * Returns the percentage of the original width of the asset that is
     * expected to be rendered on the ouput device (approximately).
     *
     * @return percentage of the original width of the asset that is
     * expected to be rendered on the ouput device (approximately).
     */
    public int getWidthHint () {
        return widthHint;
    }

    /**
     * Sets the percentage of the original width of the asset that is
     * expected to be rendered on the ouput device.
     *
     * @param widthHint Percentage of the original width of the asset that is
     * expected to be rendered on the ouput device.
     */
    public void setWidthHint (int widthHint) {
        this.widthHint = widthHint;
    }

    /**
     * Returns the percentage of the original height of the asset that is
     * expected to be rendered on the ouput device (approximately).
     *
     * @return percentage of the original height of the asset that is
     * expected to be rendered on the ouput device (approximately).
     */
    public int getHeightHint () {
        return heightHint;
    }

    /**
     * Sets the percentage of the original height of the asset that is
     * expected to be rendered on the ouput device.
     *
     * @param heightHint Percentage of the original width of the asset that is
     * expected to be rendered on the ouput device.
     */
    public void setHeightHint (int heightHint) {
        this.heightHint = heightHint;
    }

    /**
     * Returns the interval at which markers will be placed on the x-axis.
     *
     * @return The interval at which markers will be placed on the x-axis.
     */
    public int getXInterval () {
        return xInterval;
    }

    /**
     * Sets the interval at which markers will be placed on the x-axis.
     *
     * @param xInterval The interval at which markers will be placed on the
     * x-axis.
     */
    public void setXInterval (int xInterval) {
        this.xInterval = xInterval;
    }

    /**
     * Returns the interval at which markers will be placed on the y-axis
     * @return The interval at which markers will be placed on the y-axis
     */
    public int getYInterval () {
        return yInterval;
    }

    /**
     * Sets the interval at which markers will be placed on the y-axis.
     *
     * @param yInterval he interval at which markers will be placed on the
     * y-axis.
     */
    public void setYInterval (int yInterval) {
        this.yInterval = yInterval;
    }

    // Javadoc inherited from super class.
    protected RepositoryObjectIdentity createIdentity () {
        return new ChartAssetIdentity(getProject(), getName());
    }

    /**
     * Returns true if the supplied object is equal to this
     * ChartAsset.  Instances of ChartAsset are considered equal
     * if all instance variables are equal.
     *
     * @param object The object to be tested for equality.
     *
     * @return true if object is equal to this ChartAsset; otherwise
     * false.
     */
    public boolean equals (Object object) {
        if (object instanceof ChartAsset) {
          ChartAsset o = (ChartAsset) object;
          return super.equals (o)
            && widthHint == o.widthHint
            && heightHint == o.heightHint
            && xInterval == o.xInterval
            && yInterval == o.yInterval
            && equals (type, o.type)
            && equals (xTitle, o.xTitle)
            && equals (yTitle, o.yTitle);
        }

        return false;
    }

    // Javadoc inherited
    public int hashCode () {
        return super.hashCode ()
          + widthHint
          + heightHint
          + xInterval
          + yInterval
          + hashCode (type)
          + hashCode (xTitle)
          + hashCode (yTitle);
    }

    // Javadoc inherited
    protected String paramString () {
        return super.paramString ()
          + "," + widthHint
          + "," + heightHint
          + "," + xInterval
          + "," + yInterval
          + "," + type
          + "," + xTitle
          + "," + yTitle;
    }

}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Oct-05	9789/1	emma	VBM:2005101113 Refactor JDBC Accessors to use chunked accessor

 28-Sep-05	9445/1	gkoch	VBM:2005090603 Introduced ComponentContainers to bring components and their assets together

 20-May-05	8365/1	rgreenall	VBM:2005051614 Added Javadoc

 19-May-05	8302/1	rgreenall	VBM:2005051614 Added Javadoc

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 09-Feb-04	2846/1	claire	VBM:2004011915 Adding project init to identities. Fixing assetURL rewrite

 30-Jan-04	2767/1	claire	VBM:2004012701 Add project

 03-Nov-03	1698/1	pcameron	VBM:2003102411 Added some new PolicyValue methods and refactored

 ===========================================================================
*/
