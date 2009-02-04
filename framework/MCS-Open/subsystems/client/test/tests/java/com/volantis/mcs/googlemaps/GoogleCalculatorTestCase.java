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
 * Data to GoogleCalculator test case can be taken from google maps page.
 * For example Warsaw has following paramters
 * for map image:
 * x = 571
 * y = 337 
 * zoom = 7
 * for photo image
 * t=trtqtrsrqs
 * @author kniemiec
 *
 */
public class GoogleCalculatorTestCase extends TestCaseAbstract {

	public void testFromGImageToGeoString(){
		GImage warsawImage = new GImage(571,337,7);
		
		GGeoString expectedGeoString = new GGeoString("trtqtrsrqrs");
		
		GoogleCalculator calculator = GoogleCalculator.getInstance();
		GGeoString resultGeoSTring = calculator.fromGImageToGeoString(warsawImage);
		assertEquals(expectedGeoString, resultGeoSTring);
	}
	
	
	public void testFromGeoStringToGImage(){
		GImage expectedWarsawImage = new GImage(571,337,7);
		
		GGeoString warsawGeoString = new GGeoString("trtqtrsrqrs");
		
		GoogleCalculator calculator = GoogleCalculator.getInstance();
		GImage resultGImage = calculator.fromGeoStringToGImage(warsawGeoString);
		
		assertEquals(new int []{expectedWarsawImage.getImgX()}, new int []{resultGImage.getImgX()});		
		assertEquals(new int []{expectedWarsawImage.getImgY()},  new int []{resultGImage.getImgY()});				
		
	}
	
	public void testReflexivity(){
		GImage warsawImage = new GImage(571,337,7);
		
		GoogleCalculator calculator = GoogleCalculator.getInstance();
		GGeoString warsawString = calculator.fromGImageToGeoString(warsawImage);
		GImage resultWarsawImage = calculator.fromGeoStringToGImage(warsawString);

		assertEquals(new int[]{warsawImage.getImgX()}, new int []{resultWarsawImage.getImgX()});		
		assertEquals(new int []{warsawImage.getImgY()},  new int []{resultWarsawImage.getImgY()});				
	}
	
	public void testGetZoomInGeoString(){
		GGeoString warsawGeoString = new GGeoString("trtqtrsrqrs");
		GoogleCalculator calculator = GoogleCalculator.getInstance();
		
		GGeoString expectedGeoString = new GGeoString("trtqtrsrqrsq");
		GGeoString resultGeoString = calculator.getZoomInGeoString(warsawGeoString);
		
		assertEquals(expectedGeoString,resultGeoString);
		
	}
	
	public void testGetZoomOutGeoString(){
		GGeoString warsawGeoString = new GGeoString("trtqtrsrqrs");
		GoogleCalculator calculator = GoogleCalculator.getInstance();

		GGeoString expectedGeoString = new GGeoString("trtqtrsrqr");
		GGeoString resultGeoString = calculator.getZoomOutGeoString(warsawGeoString);
		
		assertEquals(expectedGeoString,resultGeoString);
		
	}

	public void testFromCoordZomToTxtZoom(){
		int coordZoom = 7;
		int expectedZoom = 10;
		int resultZoom = GoogleCalculator.getInstance().fromCoordZomToTxtZoom(coordZoom);
		assertEquals(new int[]{expectedZoom},new int []{resultZoom});
	}
		
	public void testFromTxtZoomToCoordZoom(){
		int txtZoom = 10;
		int expectedZoom = 7;
		int resultZoom = GoogleCalculator.getInstance().fromCoordZomToTxtZoom(txtZoom);
		assertEquals(new int[]{expectedZoom},new int []{resultZoom});
		
	}
	
}
