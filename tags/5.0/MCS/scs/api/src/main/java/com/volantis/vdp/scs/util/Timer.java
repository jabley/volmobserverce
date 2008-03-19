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
* (c) Volantis Systems Ltd 2004. 
* ----------------------------------------------------------------------------
*/
package com.volantis.vdp.scs.util;

public class Timer {

    private long LIFE_EXPECTANCY;
    private long lastTime;
    private long currentTime;

    protected boolean run;

    public Timer(long life_expectancy) {
        this.LIFE_EXPECTANCY = life_expectancy;
        this.lastTime = System.currentTimeMillis();
        this.currentTime = System.currentTimeMillis();
        this.run = true;

        startTimer();
    }

    private void startTimer() {
        Runnable r = new Runnable() {
            public void run() {
                try {
                    while(run) {
                        currentTime = System.currentTimeMillis();
                        if(currentTime - lastTime  > LIFE_EXPECTANCY) {
                            run = false;
                            close();
                            System.out.println("s");
                        } else {
                            System.out.println(currentTime - lastTime);
                            System.out.println(LIFE_EXPECTANCY);
                            Thread.sleep(2000);
                        }
                    }
                } catch(InterruptedException ie) {
                }
            }
        };
        new Thread(r).start();
    }

    protected void update() {
        this.lastTime = System.currentTimeMillis();
    }

    public void close() {}
}
