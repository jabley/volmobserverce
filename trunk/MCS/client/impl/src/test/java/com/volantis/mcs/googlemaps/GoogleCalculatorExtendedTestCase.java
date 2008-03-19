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
package com.volantis.mcs.googlemaps;

import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Data can be taken from google maps page see GoogleCalculatorTestCase.
 * Extended coordinates mean:
 * For GImage
 * each coordination is multiplied by 3, and certain offset is added, allowing to 
 * specify part of original image. Offset is calculated in extended calculator version.
 * For GGeoString
 * two additional attributes are added: offx, offy allowing
 * to specify piece of original image. 
 */
public class GoogleCalculatorExtendedTestCase extends TestCaseAbstract {
	
	

	public void testFromGImageToGeoString(){
		GImage warsawImage = new GImage(1713,1011,7);
		
		GGeoString expectedGeoString = new GGeoString("trtqtrsrqrs",0,0);
		
		GoogleCalculator calculator = GoogleCalculatorExtended.getInstance();
		GGeoString resultGeoSTring = calculator.fromGImageToGeoString(warsawImage);
		assertEquals(expectedGeoString, resultGeoSTring);
	}
	
	
	public void testFromGeoStringToGImage(){
		GImage expectedWarsawImage = new GImage(1713,1011,7);
		
		GGeoString warsawGeoString = new GGeoString("trtqtrsrqrs",0,0);
		
		GoogleCalculator calculator = GoogleCalculatorExtended.getInstance();
		GImage resultGImage = calculator.fromGeoStringToGImage(warsawGeoString);
		assertEquals(new int [expectedWarsawImage.getImgX()], new int [resultGImage.getImgX()]);		
		assertEquals(new int [expectedWarsawImage.getImgY()],  new int [resultGImage.getImgY()]);				
	}
	
	public void testReflexivity(){
		GImage warsawImage = new GImage(1713,1011,7);
		
		GoogleCalculator calculator = GoogleCalculatorExtended.getInstance();
		GGeoString warsawString = calculator.fromGImageToGeoString(warsawImage);
		GImage resultWarsawImage = calculator.fromGeoStringToGImage(warsawString);

		assertEquals(new int [warsawImage.getImgX()], new int [resultWarsawImage.getImgX()]);		
		assertEquals(new int [warsawImage.getImgY()],  new int [resultWarsawImage.getImgY()]);				
	}
	
	public void testFromGPixelToGImage(){
		GPoint point = new GPoint(20000,10000);
		GImage expectedImage = new GImage(234,117,0);
		
		GoogleCalculator calculator = GoogleCalculatorExtended.getInstance();
		GImage resultImage = calculator.fromGPixelToGImage(point);
		
		assertEquals(new int [expectedImage.getImgX()], new int [resultImage.getImgX()]);		
		assertEquals(new int [expectedImage.getImgY()],  new int [resultImage.getImgY()]);						
	}
}
