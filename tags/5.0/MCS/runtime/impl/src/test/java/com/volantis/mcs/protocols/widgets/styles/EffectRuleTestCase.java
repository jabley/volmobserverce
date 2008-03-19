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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.widgets.styles;

import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test case for EffectRule class. Additionally test EffectDesc 
 * and EffectParameters classes, as they are internally used by EffectRule
 */
public class EffectRuleTestCase extends TestCaseAbstract {

    public void testEffectOnlyRule() throws Exception {

        EffectRule expectedRule = new EffectRule(
                new EffectDescriptor("pulsate", null),
                "none",
                "none");        
        EffectRule rule = EffectRule.parse("pulsate");    
        assertEquals(expectedRule, rule);
    }

    public void testEffectWithParamsRule() throws Exception {

        RandomEffectParameters params = new RandomEffectParameters();
        params.addAllowedEffect(new EffectDescriptor("wipe", "top"));
        params.addAllowedEffect(new EffectDescriptor("pulsate", null));
        EffectRule expectedRule = new EffectRule(
                new EffectDescriptor("random", null, params),
                "none",
                "none");        
        EffectRule rule = EffectRule.parse("random(wipe-top, pulsate)");    
        assertEquals(expectedRule, rule);
    }

    public void testEffectAndRepsRule() throws Exception {

        EffectRule expectedRule = new EffectRule(
                new EffectDescriptor("pulsate", null),
                "10",
                "none");        
        EffectRule rule = EffectRule.parse("pulsate 10");    
        assertEquals(expectedRule, rule);
    }
    
    public void testEffectAndTimeRule() throws Exception {

        EffectRule expectedRule = new EffectRule(
                new EffectDescriptor("pulsate", null),
                "none",
                "600");
        EffectRule rule = EffectRule.parse("pulsate 600s");    
        assertEquals(expectedRule, rule);
        
        // Space is allowed after number
        rule = EffectRule.parse("pulsate 600 s");    
        assertEquals(expectedRule, rule);

        // Minutes are allowed
        /* Uncomment when fixed
        rule = EffectRule.parse("pulsate 10min");    
        assertEquals(expectedRule, rule);
        */
}

    public void testEffectWithParamsAndRepsRule() throws Exception {
        
        RandomEffectParameters params = new RandomEffectParameters();
        params.addAllowedEffect(new EffectDescriptor("wipe", "top"));
        params.addAllowedEffect(new EffectDescriptor("slide", "left"));
        params.addAllowedEffect(new EffectDescriptor("slide", "right"));
        params.addAllowedEffect(new EffectDescriptor("random", null));
        params.addAllowedEffect(new EffectDescriptor("wipe", "top"));
        EffectRule expectedRule = new EffectRule(
                new EffectDescriptor("random", null, params),
                "16",
                "none");
        EffectRule rule = EffectRule.parse(
        "random(wipe-top, slide-left, slide-right, random, wipe-top) 16");        
        assertEquals(expectedRule, rule);
    }

    public void testEffectWithParamsAndTimeRule() throws Exception {

        RandomEffectParameters params = new RandomEffectParameters();
        params.addAllowedEffect(new EffectDescriptor("wipe", "top"));
        params.addAllowedEffect(new EffectDescriptor("slide", "left"));
        params.addAllowedEffect(new EffectDescriptor("slide", "right"));
        params.addAllowedEffect(new EffectDescriptor("random", null));
        params.addAllowedEffect(new EffectDescriptor("wipe", "top"));
        EffectRule expectedRule = new EffectRule(
                new EffectDescriptor("random", null, params),
                "none",
                "600");        

        EffectRule rule = EffectRule.parse(
            "random(wipe-top, slide-left, slide-right, random, wipe-top) 600s");        
        assertEquals(expectedRule, rule);

        // Space is allowed after number
        rule = EffectRule.parse(
            "random(wipe-top, slide-left, slide-right, random, wipe-top) 600 s");        
        assertEquals(expectedRule, rule);

        // Minutes are allowed 
        /* Uncomment when fixed
        rule = EffectRule.parse(
            "random(wipe-top, slide-left, slide-right, random, wipe-top) 10min");        
        assertEquals(expectedRule, rule);
        */
    
    }
}
