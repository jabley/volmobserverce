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

package com.volantis.mcs.protocols.widgets.renderers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.volantis.mcs.googlemaps.GImage;
import com.volantis.mcs.googlemaps.GLatLng;
import com.volantis.mcs.googlemaps.GPoint;
import com.volantis.mcs.googlemaps.GoogleCalculator;
import com.volantis.mcs.googlemaps.GoogleCalculatorExtended;
import com.volantis.mcs.googlemaps.OperationHelper;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.AnchorAttributes;
import com.volantis.mcs.protocols.DivAttributes;
import com.volantis.mcs.protocols.ImageAttributes;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.TableAttributes;
import com.volantis.mcs.protocols.TableCellAttributes;
import com.volantis.mcs.protocols.TableRowAttributes;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.widgets.ActionName;
import com.volantis.mcs.protocols.widgets.EventName;
import com.volantis.mcs.protocols.widgets.PropertyName;
import com.volantis.mcs.protocols.widgets.attributes.MapAttributes;
import com.volantis.mcs.runtime.scriptlibrarymanager.ScriptModule;
import com.volantis.mcs.runtime.scriptlibrarymanager.ScriptModulesDefinitionRegistry;
import com.volantis.mcs.runtime.scriptlibrarymanager.WidgetScriptModules;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.properties.DisplayKeywords;
import com.volantis.mcs.themes.properties.OverflowKeywords;
import com.volantis.mcs.themes.properties.PositionKeywords;
import com.volantis.mcs.themes.values.LengthUnit;
import com.volantis.mcs.themes.values.StyleKeywords;
import com.volantis.mcs.utilities.StringConvertor;
import com.volantis.styling.Styles;
import com.volantis.styling.StylingFactory;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.values.MutablePropertyValues;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Renderer for Map widget suitable for HTML protocol
 */
public class MapDefaultRenderer extends WidgetDefaultRenderer
        implements MapRenderer {

    public static final ScriptModule MODULE = createAndRegisterModule();

    private static ScriptModule createAndRegisterModule() {
        Set dependencies = new HashSet();
        // always needed directly dependend module
        dependencies.add(WidgetScriptModules.BASE_BB);
        dependencies.add(WidgetScriptModules.BASE_AJAX);
        ScriptModule module = new ScriptModule("/vfc-map.mscr", dependencies,
                30100, true);
        ScriptModulesDefinitionRegistry.register(module);
        return module;
    }


    private static final StyleValueFactory STYLE_VALUE_FACTORY =
        StyleValueFactory.getDefaultInstance();

    /** 
     * Max image size is 256 because with current
     * quality improvement implementation there is 
     * no way to get map bigger than 256x256  due to 
     * some constraints described in FS for google maps
     */
    private static final int MAX_DISPLAY_SIZE = 256;
    
    /**
     * parameters prefix when transcoder in transforce mode
     */
    private static final String ICS_TRANSFORCE_PREFIX = "tf";
    
    /**
     * parameters prefix when transcoder in native mode
     */

    private static final String ICS_NATIVE_PREFIX = "v";
    
    /**
     * parameters name for internal usage
     */
    private static final String HOST_PARAM = "host";
    
    private static final String PORT_PARAM = "port";

    private static final String WIDTH_PARAM = "width";

    
    /**
     * Map for parameters names when ics in transforce mode. 
     */
    private static final Map transforceModeParameters = new HashMap();
    static {
        transforceModeParameters.put(HOST_PARAM, "tf.source.host");
        transforceModeParameters.put(PORT_PARAM,"tf.source.port");
        transforceModeParameters.put(WIDTH_PARAM,"tf.width");
    }

    /**
     * Map for parameters names when ics in native mode. 
     */
    private static final Map nativeModeParameters = new HashMap();
    static {
        nativeModeParameters.put(HOST_PARAM, "v.imgHost");
        nativeModeParameters.put(PORT_PARAM,"v.imgPort");
        nativeModeParameters.put(WIDTH_PARAM,"v.width");
    }
    
    
    /**
     * Visible map size is 3x3
     */
    private static final int MAIN_IMAGES_TABLE_SIZE = 3;

    /**
     * Hidden map size is 5x5 
     */
    private static final int MOVABLE_IMAGES_TABLE_SIZE = 5;

    /**
     * path for noerror image that is displayed if error when original image loading
     */
    private static final String GEN_IMAGE_PATH = "projects/client/assets/";

    /**
     * Name of error image.
     */
    private static final String GEN_IMAGE_NAME = "noimage.jpg";

    /**
     * host for map images
     */
    private static final String GOOGLE_MAP_HOST = "mt1.google.com";

    /**
     * host for sattelite images
     */
    private static final String GOOGLE_PHOTO_HOST = "mt1.google.com";
    
    /**
     * ports for google servers
     */    
    private static final int GOOGLE_MAP_PORT = 80;

    private static final int GOOGLE_PHOTO_PORT = 80;
    
    /**
     * mandatory part pf map image request
     * @todo - this value is changed sometimes by google, when we get
     * full access to google api this must be changed
     */
    private static final String GOOGLE_MAP_PARAMETERS = "mt?v=w2.80";

    /**
     * mandatory part of sattelite image request
     */
    private static final String GOOGLE_PHOTO_PARAMETERS = "mt?v=w2t.80";

    
    private static final String ICS_DEFAULT_APP = "/ics/ics/";

    /**
     * image size for device - this value depends on image display width
     * but also on width defined by user.
     */
    private int imageSizePx;
    
    private boolean containsMapView = false;
    
    //id of current map element
    private String mapId;
    
    /**
     * Path for error image used to calculate mandatory ICS parameters
     * that will be used on client-side for constructing URL's for map images. 
     * Error image is also displayed when no google ma/photo image found. 
     */
    private String calculatedImagePath;
    
    /**
     * main table has padding set to 0 to avoid 
     * gaps betwen images.
     */
    private static Map mainTableStyles = new HashMap();
    static {
        mainTableStyles.put(StylePropertyDetails.PADDING_LEFT,
            STYLE_VALUE_FACTORY.getLength(null, 0, LengthUnit.PX));
        mainTableStyles.put(StylePropertyDetails.PADDING_RIGHT,
            STYLE_VALUE_FACTORY.getLength(null, 0, LengthUnit.PX));
        mainTableStyles.put(StylePropertyDetails.PADDING_BOTTOM,
            STYLE_VALUE_FACTORY.getLength(null, 0, LengthUnit.PX));
        mainTableStyles.put(StylePropertyDetails.PADDING_TOP,
            STYLE_VALUE_FACTORY.getLength(null, 0, LengthUnit.PX));
        mainTableStyles.put(StylePropertyDetails.BORDER_SPACING,
            STYLE_VALUE_FACTORY.getLength(null, 0, LengthUnit.PX));
    }


    /**
     * table cells hasve paddings and border spacing set to 0 to avoid 
     * gaps betwen images.
     */
    private static Map tableCellStyles = new HashMap();
    static {
        tableCellStyles.put(StylePropertyDetails.PADDING_LEFT,
            STYLE_VALUE_FACTORY.getLength(null, 0, LengthUnit.PX));
        tableCellStyles.put(StylePropertyDetails.PADDING_RIGHT,
            STYLE_VALUE_FACTORY.getLength(null, 0, LengthUnit.PX));
        tableCellStyles.put(StylePropertyDetails.PADDING_BOTTOM,
            STYLE_VALUE_FACTORY.getLength(null, 0, LengthUnit.PX));
        tableCellStyles.put(StylePropertyDetails.PADDING_TOP,
            STYLE_VALUE_FACTORY.getLength(null, 0, LengthUnit.PX));
        tableCellStyles.put(StylePropertyDetails.BORDER_SPACING,
            STYLE_VALUE_FACTORY.getLength(null, 0, LengthUnit.PX));
        // only to cheat style optimizer to not remove style properties
        tableCellStyles.put(StylePropertyDetails.BACKGROUND_COLOR,
            STYLE_VALUE_FACTORY.getColorByRGB(null, 0x000001));        

    }
        
    /**
     * Used for logging.
     */
    private static final LogDispatcher logger = LocalizationFactory
            .createLogger(MapDefaultRenderer.class);

    /**
     * Lists of ids of table cells that contains images.
     * Lists are send to client-side to inform widget about 
     * images that composes map.  
     */
    private List mainAreaImagesIdList = new LinkedList();

    private List movableAreaImagesIdList = new LinkedList();

    private List bgAreaImagesIdList = new LinkedList();

    /**
     * Id of div that contains main map area - set of visible images.
     */
    private String mainAreaDivId;

    /**
     * Id of div that contains movable map area - used for scrolling. 
     */
    private String movableAreaDivId;

    /**
     * List of actions for Map Widget
     */
    private final static ActionName[] MAP_ACTIONS = { ActionName.PAN_LEFT,
            ActionName.PAN_RIGHT, ActionName.PAN_UP, ActionName.PAN_DOWN,
            ActionName.ZOOM_IN, ActionName.ZOOM_OUT,
            ActionName.SET_MAP_STYLE_MAP, ActionName.SET_MAP_STYLE_PHOTO,
            ActionName.SEARCH };

    private final static EventName[] MAP_EVENTS = { 
        EventName.CHANGED
    };    
    

    /**
     * List of properties for Map Widet.
     */
    private final static PropertyName[] MAP_PROPERTIES = { 
        PropertyName.QUERY,
        PropertyName.LATITUDE,
        PropertyName.LONGITUDE,
        PropertyName.ZOOM
    };
    
    /**
     * Called on open of map element
     */
    public void doRenderOpen(VolantisProtocol protocol, MCSAttributes attributes)
            throws ProtocolException {

        if (!isWidgetSupported(protocol)) {
            return;
        }

        require(MODULE, protocol, attributes);        

        String pageBase = protocol.getMarinerPageContext().getVolantisBean().getPageBase();
        if(pageBase.equals("") || pageBase.equals("/")){
            // when application istalled as root
            calculatedImagePath = "/"+ GEN_IMAGE_PATH;
        } else {
            calculatedImagePath = "/"+ pageBase+"/"+GEN_IMAGE_PATH;
        }
        
        MapAttributes mapAttributes = (MapAttributes) attributes;
        mapAttributes.setImageAttributes(createErrorImage(protocol));
        
        if (attributes.getId() == null) {
            attributes.setId(protocol.getMarinerPageContext()
                    .generateUniqueFCID());
        }
        this.mapId = attributes.getId();
    }

    private void updateNoImageAttributes(ImageAttributes imageAttributes) {
        
        String noImageIcsUrl = imageAttributes.getSrc();
        int widthIndex = noImageIcsUrl.indexOf("width=");
        if (widthIndex > 0) {
            imageAttributes.setSrc(
                    noImageIcsUrl.substring(0, widthIndex+6)
                    + this.imageSizePx);
        }
        imageAttributes.setWidth(StringConvertor.valueOf(this.imageSizePx));
        imageAttributes.setHeight(StringConvertor.valueOf(this.imageSizePx));
    }

    /**
     * Called on open of map-view element
     */
    public void renderMapView(VolantisProtocol protocol,
            MCSAttributes attributes) throws ProtocolException {
        
        if (!isWidgetSupported(protocol)) {
            return;
        }
        
        setImageSize(getMapWidth(protocol, attributes));
        if (this.containsMapView) {
            throw new ProtocolException("More than one widget:map-view "
                    + "element inside widget:maps element.");
        }
        this.containsMapView = true;
        this.mainAreaImagesIdList.clear();
        this.movableAreaImagesIdList.clear();
        this.bgAreaImagesIdList.clear();
        
        this.mainAreaDivId = renderMainArea(protocol, this.mainAreaImagesIdList);
        this.movableAreaDivId = renderMovableArea(protocol,
                this.movableAreaImagesIdList);
        renderBgArea(protocol, this.bgAreaImagesIdList);
    }

    /**
     * Returns integer value of width style of widget:map-view element 
     * @return
     */
    private int getMapWidth(VolantisProtocol protocol, MCSAttributes attributes) {
        StylesExtractor stylesExtractor = createStylesExtractor(
                    protocol, 
                    attributes.getStyles());
               
        return stylesExtractor.getWidthIfSpecifiedInPixels(
                protocol.getProtocolConfiguration().getDevicePixelsX());
    }
    
    
    /**
     * Called on close of map element
     */
    public void doRenderClose(VolantisProtocol protocol,
            MCSAttributes attributes) throws ProtocolException {

        if (!isWidgetSupported(protocol)) {
            return;
        }
        
        if (!this.containsMapView) {
            throw new ProtocolException("Missed widget:map-view element.");
        }
        this.containsMapView = false;
        
        MapAttributes mapAttributes = (MapAttributes) attributes;
        
        Styles styles = mapAttributes.getStyles();
        
        String mapStyle = "map";
        	
        
        if (styles.getPropertyValues().getComputedValue(
        		StylePropertyDetails.MCS_MAP_STYLE) == StyleKeywords.PHOTO) {
            mapStyle = "photo";
        } else if (styles.getPropertyValues().getComputedValue(
        		StylePropertyDetails.MCS_MAP_STYLE) == StyleKeywords.HYBRID) {
            logger.warn("property-not-supported-yet", 
                    new Object[]{"hybrid", "map"});
        }
        
        String pageBase = protocol.getMarinerPageContext().getVolantisBean().getPageBase();
        if(pageBase.length()>0){
            pageBase = "/"+pageBase;
        }

        StringBuffer scriptBuffer = new StringBuffer();
 
        scriptBuffer.append("Widget.register('").append(attributes.getId())
                .append("', new Widget.GoogleMap('").append(attributes.getId())
                .append("',{cacheOn : true,")
                .append("mode : '").append(mapStyle).append("', ")
                .append(prepareICSConfig(mapAttributes.getImageAttributes()))
                .append(",imageSize : ").append(StringConvertor.valueOf(this.imageSizePx))
                .append(",pageBase : '").append(pageBase).append("'")
                .append(getInitialLocation(mapAttributes)).append("},{")
                .append(createInitializationList())
                .append("}));");

        addCreatedWidgetId(attributes.getId());

        writeJavaScript(scriptBuffer.toString());
        this.mapId = null;
    }
    
    public String getCurrentMapId() {
        return this.mapId;
    }

    /**
     * @inheritDoc
     */
    protected ActionName[] getSupportedActionNames() {
        return MAP_ACTIONS;
    }
    
    protected EventName[] getSupportedEventNames() {
        return MAP_EVENTS;
    }

    protected PropertyName[] getSupportedPropertyNames() {
        return MAP_PROPERTIES;
    }

    /**
     * Convert list to javaScript array list that looks like 
     * [ item1, item2, item3, ... ]. 
     */
    private StringBuffer listToJSArray(List list) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[");
        for (Iterator i = list.iterator(); i.hasNext();) {
            buffer.append("'").append((String) i.next()).append("'");
            if (i.hasNext())
                buffer.append(",");
        }
        buffer.append("]");
        return buffer;
    }

    /**
     * Check if param is from Native mode - it means starts from 'v'.
     * @param paramName
     * @return
     */
    private boolean isICSModeNative(String paramName){
    	return paramName.startsWith(ICS_NATIVE_PREFIX);
    }
    
    
    /**
     * Check if param is from Transforce mode - it means starts from 'tf'.
     * @param paramName
     * @return
     */
    private boolean isICSModeTransforce(String paramName){
    	return paramName.startsWith(ICS_TRANSFORCE_PREFIX);
    }
    
    /**
     * check transcoder mode and return suitable params map
     * when in native mode - params with prefix 'v'
     * when in transforce - with prefix 'tf'
     * @param paramName
     * @return
     */
    private Map getTranscoderParamsMap(String paramName){
        Map paramsMap = null;
        if (isICSModeNative(paramName)) {
        	paramsMap = nativeModeParameters;
        } else if (isICSModeTransforce(paramName)) {
        	paramsMap = transforceModeParameters;
        }
        return paramsMap;
    }
 
    
    /**
     * sets map width, specified by the width style of map-view element
     * @param mapWidth
     */
    private void setImageSize(int mapWidth) {
        if(mapWidth > MAX_DISPLAY_SIZE){
            // if map width bigger than max - set to max
            this.imageSizePx = MAX_DISPLAY_SIZE / MAIN_IMAGES_TABLE_SIZE;
        } else {
            this.imageSizePx = mapWidth / MAIN_IMAGES_TABLE_SIZE;
        }
    }
    
    /**
     * Prepare ICS configuration that is send directly to client side,
     * result is java script list of pairs attribute, value.  
     * <p>  
     * ICS config string contains:
     * - maps server
     * - photos server
     * - additional parameters necessary for ICS to download image from google servers
     * and transform it to phone compatible format.  
     * On client-side these parameters will be used to creating valid image URL
     * do be downloaded from google and transformed by ICS application.

     * Algorithm works using as follow:
     * 
     * when we have noImageIcsUrl like: 
     * "/volantis/projects/client/assets/cj24/noimage.jpg?v.width=256"
     * icsAppAddress will be "/ics/ics/" because there is no host there so default location on local host is used
     * icsImageCode will be "cj24/"
     * icsParameters will be "v.width=256"
     * </p>
     * 
     * @param ImageAttributes imageAttributes (for error image) contain transcoder location
     * @return
     * @throws ProtocolException
     */
    private String prepareICSConfig(ImageAttributes imageAttributes)
            throws ProtocolException {
        String noImageIcsUrl = imageAttributes.getSrc();
        String icsParameters;
        
        updateNoImageAttributes(imageAttributes);
        
        int icsAppAddressEnd = noImageIcsUrl.indexOf(calculatedImagePath);
        
        // icsAppAddres points where ics application is installed
        String icsAppAddress = noImageIcsUrl.substring(0, icsAppAddressEnd);
        if (icsAppAddress.length() == 0) {
            icsAppAddress = ICS_DEFAULT_APP;
            // we also must update error image src - without root
            imageAttributes.setSrc(ICS_DEFAULT_APP+imageAttributes.getSrc().substring(1));
        }

        // we are looking for transcoder parameters so we 
        // start from name of error image followed by 
        icsParameters = noImageIcsUrl.substring(
                icsAppAddressEnd + calculatedImagePath.length());
                
        int imageNameStart = icsParameters.indexOf(GEN_IMAGE_NAME);

        // image code contains part of ICS url describing ICS conversion for this image
        // like cj24 for jpeg 24 bits - this code is placed before image name
        String icsImageCode = icsParameters.substring(0, imageNameStart);
        // ics parameters points into parameters used by ics to get original
        // image location and transformation parameters
        // like 'host:port' and desired 'width'
        icsParameters = icsParameters.substring(imageNameStart  +GEN_IMAGE_NAME.length() + 1);
        
        Map transcoderParamsMap = getTranscoderParamsMap(icsParameters);
        if(transcoderParamsMap == null){
            logger.error("ics-server-not-configured");
            throw new ProtocolException();
        }

        String mapHostPrefix;
        String mapParameters;
        
        String photoHostPrefix;
        String photoParameters;

        mapHostPrefix = icsAppAddress + icsImageCode + GOOGLE_MAP_PARAMETERS;
        mapParameters = transcoderParamsMap.get(HOST_PARAM).toString()+"="+GOOGLE_MAP_HOST + "&" +
        transcoderParamsMap.get(PORT_PARAM).toString()+"="+GOOGLE_MAP_PORT+"&"+
        transcoderParamsMap.get(WIDTH_PARAM)+ "=" + imageAttributes.getWidth(); 

        photoHostPrefix = icsAppAddress + icsImageCode + GOOGLE_PHOTO_PARAMETERS;
        photoParameters = transcoderParamsMap.get(HOST_PARAM).toString()+"="+GOOGLE_PHOTO_HOST + "&" +
        	transcoderParamsMap.get(PORT_PARAM).toString()+"="+GOOGLE_PHOTO_PORT+"&"+
        	transcoderParamsMap.get(WIDTH_PARAM)+ "=" + imageAttributes.getWidth();

        StringBuffer result = new StringBuffer();
        result.append("icsMapHostPrefix : '").append(mapHostPrefix).append("'")
              .append(", icsMapHostSuffix : '").append(mapParameters)
              .append("'").append(", icsSatHostPrefix : '").append(photoHostPrefix).append("'")
              .append(", icsSatHostSuffix : '").append(photoParameters).append("'")
              .append(", errorImageSrc: '").append(imageAttributes.getSrc()).append("'");
        return result.toString();
    }

    
    /**
     * Prepare image attributes for error image, displayed when no 
     * 
     * @param protocol
     * @return
     */
    private ImageAttributes createErrorImage(VolantisProtocol protocol) {
        
        ImageAttributes imageAttributes = new ImageAttributes();
        imageAttributes.setSrc(calculatedImagePath + GEN_IMAGE_NAME);
        imageAttributes.setWidth(StringConvertor.valueOf(this.imageSizePx));
        imageAttributes.setHeight(StringConvertor.valueOf(this.imageSizePx));
        return imageAttributes;
    }

    /**
     * Render main google maps area, which will hold visible images
     * for google map widget.
     * <p>Visible images mean images that normally are displayed
     * and hidden when map is scrolled.</p>
     * 
     * @param protocol
     * @param idCollector
     * @return
     * @throws ProtocolException
     */
    private String renderMainArea(VolantisProtocol protocol, List idCollector)
            throws ProtocolException {
        String mainDivId;
        DivAttributes divAttributes;
        divAttributes = createDivAttributes(protocol, null, true);
        mainDivId = divAttributes.getId();
        Map stylesMap = new HashMap();

        stylesMap.put(StylePropertyDetails.WIDTH,
            STYLE_VALUE_FACTORY.getLength(null, 3 * this.imageSizePx, LengthUnit.PX));
        stylesMap.put(StylePropertyDetails.HEIGHT,
            STYLE_VALUE_FACTORY.getLength(null, 3 * this.imageSizePx, LengthUnit.PX));
        stylesMap.put(StylePropertyDetails.DISPLAY,DisplayKeywords.NONE);
        
        updateStyles(divAttributes.getStyles(),stylesMap);        
        openDivElement(protocol, divAttributes);
        
        divAttributes = createDivAttributes(protocol, null, true);
        mainDivId = divAttributes.getId();
        stylesMap.clear();
        
        //be aware that it is not respected on Opera, IE, netFront
        //it must be set in javascript
        stylesMap.put(StylePropertyDetails.POSITION, PositionKeywords.ABSOLUTE);
        
        stylesMap.put(StylePropertyDetails.DISPLAY,DisplayKeywords.NONE);
        
        updateStyles(divAttributes.getStyles(),stylesMap);
        
        openDivElement(protocol);
        openDivElement(protocol, divAttributes);

        TableAttributes attributes = createTableAttributes(protocol, false);
        updateStyles(attributes.getStyles(), mainTableStyles);
        createTableElement(protocol,attributes);
        for (int i = 0; i < MAIN_IMAGES_TABLE_SIZE; i++) {
            createTableRowElement(protocol);
            for (int j = 0; j < MAIN_IMAGES_TABLE_SIZE; j++) {
                createTableCellElement(protocol, idCollector, true);
                AnchorAttributes aAttributes = new AnchorAttributes();
                aAttributes.setHref("javascript:void(0)");
                protocol.writeOpenAnchor(aAttributes);
                ImageAttributes iAttributes = new ImageAttributes();
                iAttributes.setSrc("");
                protocol.writeImage(iAttributes);
                protocol.writeCloseAnchor(aAttributes);
                closeTableDataCellElement(protocol);
            }
            closeTableRowElement(protocol);
        }
        closeTableElement(protocol);
        closeDivElement(protocol);
//        closeDivElement(protocol);
        closeDivElement(protocol);        
        return mainDivId;
    }

    /**
     * Movable area is used to move effect displayed when user walk through the map. 
     * <p>Movable area is hidden all the time except scrolling effect</p>. 
     * 
     * @param protocol
     * @param idCollector
     * @return
     * @throws ProtocolException
     */
    private String renderMovableArea(VolantisProtocol protocol, List idCollector)
            throws ProtocolException {
        String movableDivId;
        // outer div
        DivAttributes divOuterAttributes = createDivAttributes(protocol, null,
                true);
        Map stylesMap = new HashMap();
        stylesMap.put(StylePropertyDetails.DISPLAY,DisplayKeywords.NONE);
        stylesMap.put(StylePropertyDetails.OVERFLOW, OverflowKeywords.HIDDEN);
        stylesMap.put(StylePropertyDetails.HEIGHT,
            STYLE_VALUE_FACTORY.getLength(null, MAIN_IMAGES_TABLE_SIZE * this.imageSizePx,LengthUnit.PX));
        stylesMap.put(StylePropertyDetails.WIDTH,
            STYLE_VALUE_FACTORY.getLength(null, MAIN_IMAGES_TABLE_SIZE * this.imageSizePx,LengthUnit.PX));
        updateStyles(divOuterAttributes.getStyles(),stylesMap);
        movableDivId = divOuterAttributes.getId();
        openDivElement(protocol, divOuterAttributes);
        // inner div that moves inside outer div
        DivAttributes divInnerAttributes = createDivAttributes(protocol, null,
                true);
        stylesMap = new HashMap();
        stylesMap.put(StylePropertyDetails.POSITION,PositionKeywords.RELATIVE);
        stylesMap.put(StylePropertyDetails.TOP,
            STYLE_VALUE_FACTORY.getLength(null, -1 * this.imageSizePx, LengthUnit.PX));
        stylesMap.put(StylePropertyDetails.LEFT,
            STYLE_VALUE_FACTORY.getLength(null, -1 * this.imageSizePx, LengthUnit.PX));
        updateStyles(divInnerAttributes.getStyles(),stylesMap);
        openDivElement(protocol, divInnerAttributes);

        TableAttributes attributes = createTableAttributes(protocol, false);
        updateStyles(attributes.getStyles(), tableCellStyles);
        createTableElement(protocol,attributes);
        
        for (int i = 0; i < MOVABLE_IMAGES_TABLE_SIZE; i++) {
            createTableRowElement(protocol);
            for (int j = 0; j < MOVABLE_IMAGES_TABLE_SIZE; j++) {
                if ((j == 0 && i == 0)
                        || (j == 0 && i == (MOVABLE_IMAGES_TABLE_SIZE - 1))
                        || (i == 0 && j == (MOVABLE_IMAGES_TABLE_SIZE - 1))
                        || (i == (MOVABLE_IMAGES_TABLE_SIZE - 1) && j == (MOVABLE_IMAGES_TABLE_SIZE - 1))) {
                    createTableCellElement(protocol, idCollector, false);
                } else {
                    createTableCellElement(protocol, idCollector, true);
                    AnchorAttributes aAttributes = new AnchorAttributes();
                    aAttributes.setHref("javascript:void(0)");
                    protocol.writeOpenAnchor(aAttributes);
                    ImageAttributes iAttributes = new ImageAttributes();
                    iAttributes.setSrc("");
                    protocol.writeImage(iAttributes);
                    protocol.writeCloseAnchor(aAttributes);
                }
                closeTableDataCellElement(protocol);
            }
            closeTableRowElement(protocol);
        }
        closeTableElement(protocol);
        closeDivElement(protocol);
        closeDivElement(protocol);
        return movableDivId;
    }

    /**
     * Background area stores hidden images used for preloading images for caching purposes.
     * When scrolling is enabled backround imagers are presented after scrolling,
     * when disabled they allow to show choosen part of map immediatelly without
     * waiting for loading invisible part of map. 
     * 
     * @param protocol
     * @param idCollector
     * @throws ProtocolException
     */
    private void renderBgArea(VolantisProtocol protocol, List idCollector)
            throws ProtocolException {
        DivAttributes divBgAttributes = createDivAttributes(protocol, null,
                true);
        Map stylesMap = new HashMap();
        stylesMap.put(StylePropertyDetails.DISPLAY,DisplayKeywords.NONE);
        updateStyles(divBgAttributes.getStyles(),stylesMap);
        openDivElement(protocol, divBgAttributes);

        TableAttributes attributes = createTableAttributes(protocol, false);
        createTableElement(protocol,attributes);
        createTableRowElement(protocol);
        for (int j = 0; j < 4 * MAIN_IMAGES_TABLE_SIZE; j++) {
            createTableCellElement(protocol, idCollector, true);
            AnchorAttributes aAttributes = new AnchorAttributes();
            aAttributes.setHref("javascript:void(0)");
            protocol.writeOpenAnchor(aAttributes);
            ImageAttributes iAttributes = new ImageAttributes();
            iAttributes.setSrc("");
            protocol.writeImage(iAttributes);
            protocol.writeCloseAnchor(aAttributes);
            closeTableDataCellElement(protocol);
        }
        closeTableRowElement(protocol);
        closeTableElement(protocol);
        closeDivElement(protocol);
    }

    /**
     * Create TableCellElement and collect it's id for further use. 
     * 
     * @param protocol
     * @param idCollector
     * @param collectId
     * @throws ProtocolException
     */
    private void createTableCellElement(VolantisProtocol protocol,
            List idCollector, boolean collectId) throws ProtocolException {
        TableCellAttributes tableCellAttributes = this
                .createTableDataCellAttributes(protocol, null, true);
        if (collectId) {
            idCollector.add(tableCellAttributes.getId());
        }
        openTableCellElement(protocol, tableCellAttributes);
    }

    
    /**
     * Create attributes for TableDataCellElement.
     * 
     * @param protocol
     * @param sourceAttributes
     * @param generateDefaultId
     * @return
     */
    protected TableCellAttributes createTableDataCellAttributes(
            VolantisProtocol protocol, MCSAttributes sourceAttributes,
            boolean generateDefaultId) {
        // Create new instance of TableCellAttributes
        TableCellAttributes tableAttributes = new TableCellAttributes();

        // If source attributes are specified, copy all attribute values.
        if (sourceAttributes != null) {
            tableAttributes.copy(sourceAttributes);
        }

        // If styles are not specified, create new inherited styles.
        Styles styles = tableAttributes.getStyles();
        if (styles == null) {
            styles = StylingFactory.getDefaultInstance().createInheritedStyles(
                    protocol.getMarinerPageContext().getStylingEngine()
                            .getStyles(), null);
            tableAttributes.setStyles(styles);
        }

        if (generateDefaultId && tableAttributes.getId() == null) {
            tableAttributes.setId(protocol.getMarinerPageContext()
                    .generateUniqueFCID());
        }
        return tableAttributes;
    }

    /**
     * Update styles with provided style values.
     * 
     * @param protocol
     * @param dstStyles
     * @param srcStylesMap
     * @return
     */
    private Styles updateStyles(Styles dstStyles, Map srcStylesMap){
        MutablePropertyValues props = dstStyles.getPropertyValues();
        StyleValue styleValue;
        StyleProperty styleProperty;
        
        for(Iterator iter = srcStylesMap.keySet().iterator();iter.hasNext();){
            styleProperty = (StyleProperty)iter.next();
            styleValue = (StyleValue)srcStylesMap.get(styleProperty);
            props.setComputedAndSpecifiedValue(styleProperty,styleValue);            
        }
        return dstStyles;
    }
    
    /**
     * Create attributes for TableElement.
     * 
     *  @param protocol
     *  @param generateDefaultId
     *  @result tableAttributes
     */
    private TableAttributes createTableAttributes(VolantisProtocol protocol,
            boolean generateDefaultId) {
        TableAttributes tableAttributes = new TableAttributes();

        Styles styles = tableAttributes.getStyles();
        if (styles == null) {
            styles = StylingFactory.getDefaultInstance().createInheritedStyles(
                    protocol.getMarinerPageContext().getStylingEngine()
                            .getStyles(), null);
            tableAttributes.setStyles(styles);
        }
        if (generateDefaultId && tableAttributes.getId() == null) {
            tableAttributes.setId(protocol.getMarinerPageContext()
                    .generateUniqueFCID());
        }
        return tableAttributes;
    }

    /**
     * Create attributes for TableRowElement.
     * 
     * @param protocol
     * @param sourceAttributes
     * @param generateDefaultId
     * @return
     */
    private TableRowAttributes createTableRowAttributes(
            VolantisProtocol protocol, MCSAttributes sourceAttributes,
            boolean generateDefaultId) {
        TableRowAttributes tableAttributes = new TableRowAttributes();

        Styles styles = tableAttributes.getStyles();
        if (styles == null) {
            styles = StylingFactory.getDefaultInstance().createInheritedStyles(
                    protocol.getMarinerPageContext().getStylingEngine()
                            .getStyles(), null);
            tableAttributes.setStyles(styles);
        }

        if (generateDefaultId && tableAttributes.getId() == null) {
            tableAttributes.setId(protocol.getMarinerPageContext()
                    .generateUniqueFCID());
        }
        return tableAttributes;
    }

    /**
     * Create TableElement with provided {@link TableAttributes}.
     * 
     * @param protocol
     * @param attributes
     * @throws ProtocolException
     */
    private void createTableElement(VolantisProtocol protocol,TableAttributes attributes)
        throws ProtocolException {
        openTableElement(protocol, attributes);
    }
        
    /**
     * Create TableRowElement
     * @param protocol
     * @throws ProtocolException
     */
    private void createTableRowElement(VolantisProtocol protocol)
            throws ProtocolException {
        TableRowAttributes tableRowAttributes = createTableRowAttributes(
                protocol, null, false);
        openTableRowElement(protocol, tableRowAttributes);

    }
    
    /**
     * Create list of elements ids and return it as JavaScript arrays,
     * Id's are gathered during google map markup generation in 
     * doRenderOpen method. 
     * @return
     */
    private StringBuffer createInitializationList(){
        StringBuffer initializationList = new StringBuffer();
        initializationList.append("mainIdList : ").append(
                listToJSArray(this.mainAreaImagesIdList));
        initializationList.append(", movableIdList : ").append(
                listToJSArray(this.movableAreaImagesIdList));
        initializationList.append(", bgIdList : ").append(
                listToJSArray(this.bgAreaImagesIdList));
        initializationList.append(", mainAreaDivId : ").append("'").append(
                this.mainAreaDivId).append("'");
        initializationList.append(", movableAreaDivId : ").append("'").append(
                this.movableAreaDivId).append("'");
        return initializationList;
    }
    
    
    /**
     * Check if there are any parameters for initial location and if there are
     * create initial location initialization string and return it. 
     * @param mapAttributes
     * @return
     */
    private StringBuffer getInitialLocation(MapAttributes mapAttributes){
        StringBuffer initialLocation = new StringBuffer();
        int zoom = mapAttributes.getZoom();
        if (zoom > -1) {
            initialLocation.append(", zoom : ");
            initialLocation.append(zoom);
        }
        String query = mapAttributes.getQuery();
        double latitude = mapAttributes.getLatitude();
        if (query != null) {
            initialLocation.append(", query : ").append(
                    createJavaScriptString(query));
        } else if (!Double.isNaN(latitude)){
            
            double longitude = mapAttributes.getLongitude();

            GoogleCalculator calc = GoogleCalculatorExtended.getInstance();
            GLatLng gLatLng = new GLatLng(latitude, longitude);
            GPoint gPoint = calc.fromLatLngToPixel(gLatLng,
                    GoogleCalculator.INITIAL_ZOOM);
            GImage gImage = calc.fromGPixelToGImage(gPoint);
            initialLocation.append(", longitude: ").append(longitude)
                    .append(", latitude: ").append(latitude)
                    .append(", initialLocation: ")
                    .append(OperationHelper.getInstance()
                    .getMapImagesList(gImage.getImgX(), gImage.getImgY(),
                            GoogleCalculator.INITIAL_ZOOM,
                            GoogleCalculator.MAIN_IMAGES));
        }
        return initialLocation;
    }
      
}
