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
package com.volantis.mcs.googlemaps;

import java.util.HashMap;
import java.util.Map;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * GoogleCalculator is responsible for following things:
 * - converting from geoghraphical coordination into google map coordinates (GImage and GGeoString)
 * - converting between various representation of images
 * - performing some basic operation on coordinates like zoomin,zoomout, left,right,up, down
 * 
 * This class is based on internal google maps api functionality and it could
 * be incompatibilie in some areas with official google maps api but many tests
 * proved it's correctness. 
 *
 */
public class GoogleCalculator {

    /**
     * The logger used by this class.
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(GoogleCalculator.class);    
	
	
    protected static int BASE = 256;
    
    private static String Q = "q";
    private static String S = "s";
    private static String R = "r";
    private static String T = "t";
    
    public final static String DIR_UP = "u";
    public final static String DIR_LEFT = "l";
    public final static String DIR_BOTTOM = "b";
    public final static String DIR_RIGHT = "r";
    public final static String MAIN_IMAGES = "main";
    public final static String BG_IMAGES = "bg";    
    
    
    private static double DEF_HI = 0.9999d;
    
    private static double DEF_LO = -0.9999d;
    
    public static int INITIAL_ZOOM = 8;
    
    public static long INITIAL_XE = 128;
    
    
    /**
     * Used to default map when geocoder request returns no results
     */
    public static int WORLD_ZOOM = 17;
        
    /**
     * Used to default map when geocoder request returns no results
     */
    public static GLatLng WORLD_LAT_LNG = new GLatLng(0,0);
        
    public long [] XE_ARRAY = null;

    public Map shiftMap = null;
    
    public GImage [][]  boundList = null;
    

    /**
     * const list taken from google api
     */
    public static double [] ZE_ARRAY = new double[]{
            0.7111111111111111d,
            1.4222222222222223d,
            2.8444444444444446d,
            5.688888888888889d,
            11.377777777777778d,
            22.755555555555556d,
            45.51111111111111d,
            91.02222222222223d,
            182.04444444444445d,
            364.0888888888889d,
            728.1777777777778d,
            1456.3555555555556d,
            2912.711111111111d,
            5825.422222222222d,
            11650.844444444445d,
            23301.68888888889d,
            46603.37777777778d,
            93206.75555555556d,
            186413.51111111112d
     };
    
    /**
     * const list taken from google api
     */
    public static double [] E_ARRAY = new double [] {
            40.74366543152521d,
            81.48733086305042d,
            162.97466172610083d,
            325.94932345220167d,
            651.8986469044033d,
            1303.7972938088067d,
            2607.5945876176133d,
            5215.189175235227d,
            10430.378350470453d,
            20860.756700940907d,
            41721.51340188181d,
            83443.02680376363d,
            166886.05360752725d,
            333772.1072150545d,
            667544.214430109d,
            1335088.428860218d,
            2670176.857720436d,
            5340353.715440872d,
            10680707.430881744d            
    };
    
    private static GoogleCalculator instance = null;
    
    public static GoogleCalculator getInstance(){
        if(instance == null){
            instance = new GoogleCalculator();
        }
        return instance;
    }
    
    protected GoogleCalculator(){
        initialize();
    }
    
    private void initialize(){
        shiftMap = new HashMap();
        shiftMap.put(DIR_LEFT, new int[][] {        {-1,-2},
                                            {-2,-1},
                                            {-2,0},
                                            {-2,1},
                                                    {-1,2}});
        shiftMap.put(DIR_RIGHT, new int[][] {{1,-2},
                                                    {2,-1},
                                                    {2,0},
                                                    {2,1},
                                              {1,2}});
        
        shiftMap.put(DIR_UP, new int[][] {      {-1,-2},{0,-2},{1,-2},
                                        {-2,-1},                        {2,-1}});
        
        shiftMap.put(DIR_BOTTOM, new int[][] {{-2,1},                       {2,1}, 
                                                      {-1,2},{0,2},{1,2}});
        
        shiftMap.put(MAIN_IMAGES, new int[][] {{-1,-1},{0,-1},{1,-1},
                                               {-1,0}, {0,0}, {1,0},
                                               {-1,1}, {0,1}, {1,1}});
        
        shiftMap.put(BG_IMAGES, new int[][] {{-1,-2},{0,-2},{1,-2},
                                {-2,-1}                      ,{2,-1},
                                {-2,0}                       ,{2,0},
                                {-2,1}                       ,{2,1},
                                        {-1,2}, {0,2} ,{1,2}});        

        XE_ARRAY = new long[GMapConst.MAX_ZOOM+1];
        long value = INITIAL_XE;
        for(int i = 0;i<XE_ARRAY.length;i++){
            XE_ARRAY[i] = value;
            value *=2;
        }        

        this.boundList = new GImage[GMapConst.MAX_ZOOM+1][2];
        for(int zoom = 0;zoom<boundList.length;zoom++){
            // @todo - make sure that this is right 
            boundList[zoom][0] = fromGPixelToGImage(fromLatLngToPixel(new GLatLng(90,-180),zoom));
            boundList[zoom][1] = fromGPixelToGImage(fromLatLngToPixel(new GLatLng(-90,180),zoom));
        }
        
    }
    
    /**
     * Convert zoom from form acceptable by satellite mode to map mode. 
     * @param txtZoom
     * @return
     */
    public int fromTxtZoomToCoordZoom(int txtZoom){
        return GMapConst.MAX_ZOOM - txtZoom;
    }
    
    /**
     * Convert zoom from form acceptable by map mode to satellite mode.
     * @param coordZoom
     * @return
     */
    public int fromCoordZomToTxtZoom(int coordZoom){
        return GMapConst.MAX_ZOOM - coordZoom;
    }
    
    /**
     * Tailor double value that exceeds provided range. 
     * @param value
     * @param lo
     * @param hi
     * @return
     */
    private double fixDouble(double value, double lo,double hi){
        if(value > hi) return hi;
        if(value < lo) return lo;
        return value;
    }
    
    /**
     * 
     * Zoom calculation is done to achieve compatibility with google protocol
     * where zoom in image request and zoom in caltulations comply to 17
     * @param point
     * @param outerZoom
     * @return
     */
    public GPoint fromLatLngToPixel(GLatLng point, int outerZoom){
        int zoom  = GMapConst.MAX_ZOOM - outerZoom;
        long xFactor = Math.round(XE_ARRAY[zoom]+point.getLng()*ZE_ARRAY[zoom]);
        double lat_sinus = Math.sin(Math.toRadians(point.getLat()));
        lat_sinus = fixDouble(lat_sinus,DEF_LO,DEF_HI);
        long yFactor = XE_ARRAY[zoom]-
            Math.round((0.5d)*Math.log(((1.0d+lat_sinus)/(1.0d-lat_sinus)))*E_ARRAY[zoom]);
        return new GPoint(xFactor,yFactor);
    }
    
    /**
     * Extract Q,R,T,S symbols - building blocks for satellite mode image addressing. 
     * @param xByte
     * @param yByte
     * @return
     */
    private String symbolExtractor(long xByte,long yByte){
        if((xByte == 0) && (yByte == 0)) return Q;
        if((xByte == 1) && (yByte == 0)) return R;
        if((xByte == 0) && (yByte == 1)) return T;
        return S;
    }
    
    
    /**
     * Convert textual representation building blocks into numbers. 
     * @param s
     * @return
     */
    protected int numberExtractor(String s){
        if(s.equals(Q)) return 0;
        if(s.equals(T)) return 1;
        if(s.equals(R)) return 2;        
        return 3; 
    }
    
    /**
     * Converts pixel coordinates into image coordinates. 
     * @param pixelPoint
     * @return
     */      
    public GImage fromGPixelToGImage(GPoint pixelPoint){
    	//@kniemiec 0 is max zoom in map mode
        return new GImage((int)(pixelPoint.getX()/BASE),
                          (int)(pixelPoint.getY()/BASE),0);
    }
        
    /**
     * Converts form image to text representation, usable in satellite mode.  
     * @param pixelPoint
     * @return
     */
    public GGeoString fromGImageToGeoString(GImage imagePoint){
        StringBuffer geoStringBuffer = new StringBuffer();
        // get zoom in string mode
        // to add needed amount of characters
        int zoom = this.fromCoordZomToTxtZoom(imagePoint.getZoom());
        int x = imagePoint.getImgX();
        int y = imagePoint.getImgY();
        int xByte;
        int yByte;
        int closure = zoom;;
        while( (x >0) || (y>0)){
            closure--;
            xByte = x % 2;
            yByte = y % 2;
            geoStringBuffer.append(symbolExtractor(xByte,yByte));
            x = x/2;
            y = y/2;            
        }
        for(int i = closure;i>0;i--){
            geoStringBuffer.append(symbolExtractor(0,0));
        }
        String result = "t"+geoStringBuffer.reverse().toString(); // result must be reversed and first is always 't'
        return new GGeoString(result);
    }
    
    /**
     * converts from geoString into gImage. 
     * @param geoString
     * @return
     */
    public GImage fromGeoStringToGImage(GGeoString geoString){
        int x = 0,y = 0;
        int number;
        String withoutInitial = geoString.toString().substring(1);
        for(int i = 0;i<withoutInitial.length();i++){
            number = numberExtractor(withoutInitial.substring(i,i+1));
            x = (x << 1) + number / 2;
            y = (y << 1) + number % 2;
        }
        int imageZoom = this.fromTxtZoomToCoordZoom(geoString.getZoom());
        return new GImage(x,y,imageZoom);
    }
	/**
	 * In extended version for simplicity zoom on photo will be done
	 * by convergint photo coordination into map coordinations
	 * then zoom in will be performed and coordinates transformed back
	 * into string version
	 */
    public GGeoString getZoomInGeoString(GGeoString geoString){
    	GImage image = this.fromGeoStringToGImage(geoString);
    	image.setImgX(image.getImgX() << 1);
    	image.setImgY(image.getImgY() << 1);
    	image.setZoom(image.getZoom() - 1);
    	
    	return this.fromGImageToGeoString(image);
    }


    public GGeoString getZoomOutGeoString(GGeoString geoString){
    	GImage image = this.fromGeoStringToGImage(geoString);
    	image.setImgX(image.getImgX() >> 1);
    	image.setImgY(image.getImgY() >> 1);
    	image.setZoom(image.getZoom() + 1);
    	
    	return this.fromGImageToGeoString(image);
    }
    
    /**
     * convert from list of geostring into string.
     * @param photoList
     * @return
     */
    public String gGeoStringListToString(GGeoString [] photoList){
        StringBuffer result = new StringBuffer("[");
        for(int i = 0;i<photoList.length;i++){
            if(i!=0){
                result.append(",");
            }
            result.append("{").append("t : '").append(photoList[i].toString());
            result.append("'}");
        }
        result.append("]");
        return result.toString();    
    }

    /**
     * Converts image representation into string representation.
     * @param imageList
     * @return
     */
    public String mapImageListToString(GImage [] imageList){
        StringBuffer result = new StringBuffer("[");
        for(int i = 0;i<imageList.length;i++){
            if(i!=0){
                result.append(",");
            }
            result.append("{").append("x : ").append(imageList[i].getImgX());
            result.append(", y : ").append(imageList[i].getImgY());
            result.append("}");
        }
        result.append("]");
        return result.toString();
    }

    /**
     * Get list of images where image specified as center image will be base for list calculation
     * @param centerImage - image base for shiftList
     * @param shiftList - shiftList is list of pair like (1,1) that mens that image.
     * Example:
     * for center image that have x = 5, y = 6
     * and shiftElement (1,2)
     * new image with x = 6, y = 8 will be returned.
     * @return - list of images corresponding to centerImage and shiftList
     */
    public GImage [] getMapImages(GImage centerImage, int [][] shiftList){
        GImage [] newImages = new GImage[shiftList.length];
        for(int i = 0;i<shiftList.length;i++){
            newImages[i] = getRelativeImage(centerImage, shiftList[i][0],shiftList[i][1]);
        }        
        return newImages;
    }
    
    /**
     * Calculate next image according to current image and offset (x,y)
     * @param centerImage
     * @param xRel
     * @param yRel
     * @return
     */
    private GImage getRelativeImage(GImage centerImage, int xRel,int yRel){
    	int zoom = centerImage.getZoom();
        int x = (centerImage.getImgX()+boundList[zoom][1].getImgX()+xRel) % boundList[zoom][1].getImgX();
        int y = centerImage.getImgY()+yRel;
        return new GImage(x,y,zoom);
    }
    
}
