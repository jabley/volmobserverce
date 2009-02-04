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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
 
package volantis.mcs.example.utilities;

import com.volantis.mcs.integration.MarkupPlugin;
import com.volantis.mcs.context.MarinerRequestContext;

import java.util.Map;
import java.util.Iterator;
import java.util.Set;
import java.io.Writer;
import java.io.OutputStreamWriter;
import java.io.IOException;

/**
 * This class logs information back to a specified Writer.
 */
public class ExampleMarkupPlugin implements MarkupPlugin {
    
    /**
     * The Volantis copyright statement
     */
    private static final String mark =
            "(c) Volantis Systems Ltd 2003. ";
    
    /**
     * We use this static as a bodgy way of returning information from our
     * methods. 
     */ 
    public static Writer outputWriter;
    
    /**
     * Create a new ExampleMarkupPlugin which writes its results to System.out
     */ 
    public ExampleMarkupPlugin() {
        outputWriter = new OutputStreamWriter(System.out);    
    }
    
    /**
     * Write the specified string to the the Writer.
     * @param output The String to write.
     */ 
    private void write(String output) {
        try {
            outputWriter.write(output);            
        } catch (Exception e) {
            e.printStackTrace();
        }   
    }
    
    /**
     * Write the contents of the specified Map to the Writer.
     * @param map the Map to write.
     */ 
    private void write(Map map) {
        Set keys = map.keySet();
        write("<ul>");
        for (Iterator i = keys.iterator(); i.hasNext();) {
            String key = (String) i.next();
            String value = (String) map.get(key);
            
            write("<li>" + key + ": " + value + "</li>");
        }
        write("</ul>");
    }
    
    private void flush() {
        try {
            outputWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initialize(Map arguments) {
        write("<h2>Initialize invoked.<h2>");
        write("<h3>Arguments...</h3>");
        write(arguments);        
        flush();
    }

    public void process(MarinerRequestContext context, Map arguments) {
        write("<h2>Process invoked.</h2>");
        write("<h3>Arguments...</h3>");
        write(arguments);        
        flush();
    }

    public void release() {
        write("<h2>Release invoked.</h2>");            
        flush();
    }

    public static void setWriter(Writer writer) {
        outputWriter = writer;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 19-Jul-03	820/1	adrian	VBM:2003071701 Added jsp invocation integration tags

 ===========================================================================
*/
