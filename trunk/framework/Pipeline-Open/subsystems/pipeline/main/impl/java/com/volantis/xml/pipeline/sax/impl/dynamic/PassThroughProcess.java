package com.volantis.xml.pipeline.sax.impl.dynamic;

import com.volantis.xml.pipeline.sax.XMLProcessImpl;
import com.volantis.xml.pipeline.sax.dynamic.PassThroughController;

public class PassThroughProcess
        extends XMLProcessImpl
        implements PassThroughController {
    
    /**
     * Flag that indicates whether this process is in pass through mode.
     */
    protected boolean inPassThroughMode;

    // javadoc inherited
    public void startPassThrough() {
        inPassThroughMode = true;
    }

    // javadoc inherited
    public void stopPassThrough() {
        inPassThroughMode = false;
    }
}
