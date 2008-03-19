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

package com.volantis.mcs.clientapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Provider of fake weather data
 *
 * The implementation is stateless and thread safe.
 */
class WeatherFeed implements ItemsDataFeed {

    private final static int CONDITIONS = 16;
    private final static int MAX_TEMP = 45;
    private final static Random rnd = new Random(); 

    // The array of cities. It must be sorted, as we use binary search
    // to find items in this collection.
    private final static String[] CITIES = {
        "Ankara", "Beograd", "Berlin", "Bratislava", "Budapest", 
        "Bucharest", "Cracow", "Ljubljana", "Nicosia", "Podgorica", 
        "Prague", "Riga", "Sofia", "Tallinn", "Tirana", "Valletta", 
        "Vienna", "Vilnius", "Zagreb"
    };
    
    private final static String[] URLS = {
        "http://www.ankara-bel.gov.tr",
        "http://www.beograd.org.yu",
        "http://www.berlin.de",
        "http://www.bratislava.sk",
        "http://www.budapest.hu",
        "http://www.pmb.ro",
        "http://www.krakow.pl",
        "http://www.ljubljana.si",
        "http://www.nicosia.org.cy",
        "http://www.podgorica.cg.yu",
        "http://www.prague-city.cz",
        "http://www.rcc.lv",
        "http://www.sofia.bg",
        "http://www.tallinn.ee",
        "http://www.tirana.gov.al",
        "http://www.magnet.mt",
        "http://www.wien.gv.at/",
        "http://www.vilnius.lt",
        "http://www.zagreb.hr"             
    };
    
    public List getDataItems() {
        
        ArrayList items = new ArrayList();                  
        for (int i = 0; i < CITIES.length; i++) {
            items.add(getDataItem(i));
        }        
        return items;
    }

    public DataItem getDataItem(String id) {
        return getDataItem(Arrays.binarySearch(CITIES, id));
    }
    
    private DataItem getDataItem(int i) {
        DataItem item = new DataItem();
        item.linkText = ClientServiceHelper.getLocalizedString("city." + CITIES[i]);
        item.linkUrl = URLS[i];
            
        StringBuffer text = new StringBuffer();
        text.append(": ")
            .append(Math.abs(rnd.nextInt(MAX_TEMP)))
            .append("\u00B0C, ") // Celsius sign
            .append(ClientServiceHelper.getLocalizedString(
                    "conditions." + Math.abs(rnd.nextInt(CONDITIONS) + 1)));

        item.text = text.toString();
        return item;
    }
}        
