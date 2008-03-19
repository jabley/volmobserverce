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
/* ----------------------------------------------------------------------------
 * $Header: /src/voyager/com/volantis/mcs/accessors/xml/XMLAccessorConstants.java,v 1.20 2002/12/04 10:31:16 mat Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 17-Oct-01    Paul            VBM:2001101701 - Created.
 * 24-Oct-01    Paul            VBM:2001092608 - Added constants for device
 *                              pattern elements.
 * 05-Nov-01    Paul            VBM:2001092607 - Added constants for themes
 *                              and styles.
 * 19-Nov-01    Allan           VBM:2001102504 - Added constants for
 *                              policyDescriptors.
 * 10-Jan-02    Allan           VBM:2001121703 - Added constants for
 *                              pluginAttributes. Added entity constants.
 * 14-Jan-02    Allan           VBM:2001121703 - Renamed UI CLASS constants
 *                              to GROUP. Added UI entity constant.
 * 30-Jan-02    Mat             VBM:2002011410 - Added THEME_ATTRIBUTE
 *                              contants to themes.
 * 06-Feb-02    Paul            VBM:2001122103 - Added script constants.
 * 18-Feb-02    Allan           VBM:2002021304 - Added PluginDefinition
 *                              constants.
 * 21-Feb-02    Doug            VBM:2002011405 - Added revision contants
 * 27-Feb-02    Doug            VBM:2002011405 - Added more revision constants
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 26-Apr-02    Adrian          VBM:2002040811 - Added constants for the new
 *                              Theme and DeviceTheme accessors.
 * 02-May-02    Doug            VBM:2002040803 - Added constants for the
 *                              StyleProperty accessors.
 * 02-May-02    Allan           VBM:2002040804 - Added external stylesheet
 *                              device theme constant
 *  29-Jul-2002  Sumit          VBM:2002072906 - Added STYLE_TIME constant
 * 15-Aug-02    Ian             VBM:2002081303 - Added CONVERTIBLE_IMAGE_ASSET.
 * 23-Oct-02    Steve           VBM:2002071604 - Added application properties
 *                              constants
 * 29-Oct-02    Steve           VBM:2002071604 - capitilised application
 *                              properties
 * 28-Nov-02    Mat             VBM:2002112213 - Added DEVICE_AUDIO_ASSET
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.accessors.xml.jdom;

import com.volantis.mcs.repository.xml.XMLRepositoryConstants;
import com.volantis.mcs.objects.FileExtension;

/**
 * The constants used by the accessors.
 */
public interface XMLAccessorConstants
        extends XMLRepositoryConstants {

    public static final String ATTRIBUTE_VALUE_ATTRIBUTE = "value";

    // The common element name for all the null device asset elements.
    public static final String NULL_DEVICE_ASSET_ELEMENT = "nullDeviceAsset";

    // The common deviceName attribute for all the null device asset elements
    public static final String NULL_DEVICE_NAME_ATTRIBUTE = "deviceName";

    public static final String DEVICE_NAME_ATTRIBUTE = "name";

    public static final String IMAGE_COMPONENT_ELEMENT = "imageComponent";

    public static final String IMAGE_ASSET_SEQUENCE_ATTRIBUTE = "sequence";
    public static final String IMAGE_ASSET_SEQUENCE_SIZE_ATTRIBUTE =
            "sequenceSize";

    public static final String CONVERTIBLE_IMAGE_ASSET_ELEMENT =
            "convertibleImageAsset";
    public static final String DEVICE_IMAGE_ASSET_ELEMENT = "deviceImageAsset";
    public static final String GENERIC_IMAGE_ASSET_ELEMENT =
            "genericImageAsset";

    public static final String DEVICE_LAYOUT_DEVICE_NAME_ATTRIBUTE
            = "deviceName";

    public static final String SEGMENT_GRID_FORMAT_ELEMENT =
            "segmentGridFormat";

    public static final String SPATIAL_FORMAT_ITERATOR_ELEMENT =
            "spatialFormatIterator";


    // ========================================================================
    //     MetaDataType related constants.
    // ========================================================================

    // ========================================================================
    //     MetaDataValue related constants.
    // ========================================================================

    public static final String STYLE_PROPERTIES_ELEMENT = "styleProperties";

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Oct-05	9729/1	geoff	VBM:2005100507 Mariner Export fails with NPE

 15-Sep-05	9512/1	pduffin	VBM:2005091408 Committing changes to JIBX to use new schema

 07-Jul-05	8967/1	pduffin	VBM:2005070702 Refactored resolving of expressions into component identities

 29-Jun-05	8552/1	pabbott	VBM:2005051902 JIBX Theme accessors

 03-May-05	7963/1	pduffin	VBM:2005042906 Removed DDM components, e.g. ApplicationProperties, URLMappers, DDMProxy, etc

 11-Apr-05	7376/4	allan	VBM:2005031101 SmartClient bundler - commit for testing

 21-Mar-05	7457/1	philws	VBM:2005030811 Allow the MCS Source editor to work again on existing LPDM file types

 09-Mar-05	7315/1	allan	VBM:2005030711 Add sequences of image assets.

 19-Jan-05	6686/1	pduffin	VBM:2005010506 Completed code to read and write XML versions of the resource components, asset and templates. Also, fixed a few problems with the implementation of the MetaData API

 17-Jan-05	6670/4	adrianj	VBM:2005010506 Implementation of resource asset continued

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 13-Aug-04	5075/2	geoff	VBM:2004073008 Implement Null Assets (Umbrella) (supermerge)

 09-Aug-04	5130/1	doug	VBM:2004080310 MCS

 06-Aug-04	5081/3	geoff	VBM:2004080306 Implement Null Assets: JDBC Accessor (remove chart and dynvis)

 05-Aug-04	5081/1	geoff	VBM:2004080306 Implement Null Assets: JDBC Accessor (make it simpler)

 12-Aug-04	5111/1	doug	VBM:2004080405 Added Theme overlay support

 11-Jun-04	4678/2	geoff	VBM:2004061001 old gui cleanup: remove folder support code

 26-Apr-04	4037/1	doug	VBM:2004042301 Provided mechanism for obtaining an EntityResolver that resolves all MCS repository schemas

 04-Mar-04	3284/9	pcameron	VBM:2004022007 Rework issues with TextPolicyValueModifier

 03-Mar-04	3284/1	pcameron	VBM:2004022007 Added TextPolicyValueModifier

 13-Feb-04	3025/1	mat	VBM:2004021304 Changes to make import work

 12-Feb-04	2962/1	allan	VBM:2004021113 Replace old 3 char file extensions with new 4 char ones.

 21-Jan-04	2592/1	doug	VBM:2003112712 Implementation of the ThemeElementRenderer and ThemeElementParser interfaces

 12-Jan-04	2326/4	geoff	VBM:2003121707 Import/Export: XML Accessors: Modify Themes accessors (supermerge)

 07-Jan-04	2326/1	geoff	VBM:2003121707 Import/Export: XML Accessors: Modify Themes accessors (supermerge and fix marinerComponentURI)

 09-Jan-04	2343/5	geoff	VBM:2003121708 Import/Export: XML Accessors: Modify Layout accessors (import works now)

 07-Jan-04	2304/1	tony	VBM:2003121708 temp for handover to GS

 02-Jan-04	2343/2	geoff	VBM:2003121708 change xml layout accessors to write mostly in new format structure, at least for grids

 02-Jan-04	2346/1	andy	VBM:2003121705 implemented xsd validation framework

 30-Dec-03	2252/5	andy	VBM:2003121703 changed element name constant back to correct value

 30-Dec-03	2252/3	andy	VBM:2003121703 changed file suffix constants to correct values

 23-Dec-03	2252/1	andy	VBM:2003121703 removed policy desriptor file, removed single-file support, flattened xml repository structure

 17-Dec-03	2242/1	andy	VBM:2003121702 vbm2003121702

 30-Sep-03	1475/2	byron	VBM:2003092606 Move contents of accessors.xml package to jdom package

 ===========================================================================
*/
