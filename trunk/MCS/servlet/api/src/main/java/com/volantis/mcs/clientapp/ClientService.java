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

import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

/** 
 * Sample client service
 * 
 * This servlet implements a sample service feeding data to AJAX-enabled widgets
 * of Volantis Framework Client. This is a part of Client Sample Application.  
 */
public class ClientService extends HttpServlet {
    
    /**
     * The logger used by this class.
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(ClientService.class);

    private HashMap builders = new HashMap();
    
    public ClientService() {
        ItemsDataFeed weatherFeed = new WeatherFeed(); 
        builders.put("carousel", new CarouselResponseBuilder(weatherFeed));
        builders.put("ticker", new TickerResponseBuilder(weatherFeed));
        builders.put("folding-item", new FoldingItemBuilder(weatherFeed));

        ClockDataFeed clockFeed = new ClockFeed();
        builders.put("clock", new ClockResponseBuilder(clockFeed));
        
        TimerDataFeed timerFeed = new TimerFeed();
        builders.put("timer", new TimerResponseBuilder(timerFeed));
        
        ProgressDataFeed progressFeed = new DummyOperationProgressFeed();
        builders.put("progress", new ProgressResponseBuilder(progressFeed));
        
        FormValidator validator = new SampleFormValidator();
        builders.put("validator", new ValidatorResponseBuilder(validator));
        
        Autocompleter autocomp = new CountryNameAutocompleter();
        builders.put("autocomplete", new AutocompleteResponseBuilder(autocomp));
        
        ItemGalleryFeed gallery = new GalleryFeed();
        builders.put("itemgallery", new GalleryResponseBuilder(gallery));
        builders.put("date-picker", new DatePickerBuilder());

        DeckFeed deckFeed = new DeckWizardInOzFeed();
        builders.put("deck", new DeckResponseBuilder(deckFeed));
        
        CountryFinder countryFinder = new CountryFinder();
        builders.put("table", new TableResponseBuilder(countryFinder));
        
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
        
        String widgetId = null;
        String pathInfo = request.getPathInfo();
        if (null != pathInfo) {
            StringTokenizer tokenizer = new StringTokenizer(pathInfo, "/?");
            if (tokenizer.hasMoreTokens()) {
                widgetId = tokenizer.nextToken();
            }
        }
        
        if (null == widgetId) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }  
        
        SampleAppResponseBuilder builder = (SampleAppResponseBuilder)builders.get(widgetId);
        if (null == builder) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Data for the requested widget not found");
            return;            
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Handling response for " + widgetId);
        }
        
        try {
        builder.buildResponse(request, response);            
        } catch (Exception x) {
            throw new ServletException(x);
        }            
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        
        // Handle POST in the same way as GET
        doGet(request, response);
    }
    
}
