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

import java.util.HashMap;
import java.util.Map;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Provider of fake progress data
 *
 * The implementation is stateful but thread safe.
 */
class DummyOperationProgressFeed implements ProgressDataFeed {

    /**
     * The logger used by this class.
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(DummyOperationProgressFeed.class);

    /**
     * Keeps currently running operations
     */
    private OperationsRegistry ops = new OperationsRegistry();
    
    public int getProgress(String operationId) {
        return ops.getOrCreate(operationId).getProgress();
    }
    
    /**
     * Class implementing a dummy operation with measurable progress.
     * 
     * Intended to be run in a separate thread.
     */
    private static class Operation implements Runnable {
        
        private String operationId;
        private OperationsRegistry registry;
        private int progress = 0;
        
        public Operation(String operationId, OperationsRegistry registry) {
            this.registry = registry;
            this.operationId = operationId;

            if (logger.isDebugEnabled()) {
                logger.debug("Operation id = " + operationId + " created");
            }
        }
        
        /**
         * Returns current progres which is in range [0, 100]
         */
        public synchronized int getProgress() {
            return progress;
        }

        /**
         * Increments the operation progress. 
         * 
         * @return true if the operation still continues and false if finished
         */
        private synchronized boolean progress(int delta) {
            if (delta < 0) {
                delta = 0;
            }
                
            if (progress + delta < 100) {
                progress += delta;
            } else {
                progress = 100;                    
            }

            if (logger.isDebugEnabled()) {
                logger.debug("Operation id = " + operationId + " progressed to " + progress);
            }

            return (progress < 100);
        }

        /**
         * The method run by the operation thread.
         */
        public void run() {                            
            if (logger.isDebugEnabled()) {
                logger.debug("Operation id = " + operationId + " started");
            }
            
            while (progress(10)) {            
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    // Be nice and break when interrupted
                    break;
                }
            }

            if (logger.isDebugEnabled()) {
                logger.debug("Operation id = " + operationId + " finished");
            }
            
            // Keep the operation's status available 
            // for another 10s and then unregister it 
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                // OK
            }
            // Cleanup
            registry.unregister(operationId);
        }
    }

    /**
     *  Factory and registry of operations
     */
    private static class OperationsRegistry {
            
        /**
         * Collection of operations
         */
        private Map operations = new HashMap();
        
        public synchronized void unregister(String id) {
            operations.remove(id);            

            if (logger.isDebugEnabled()) {
                logger.debug("Operation id = " + id + " unregistered");
            }
        }
        
        public synchronized Operation getOrCreate(String id) {
            Operation op = (Operation)operations.get(id);
            if (null == op) {
                op = new Operation(id, this);
                register(id, op);
                Thread runner = new Thread(op);
                runner.setDaemon(true);
                runner.start();
            }
            return op;
        }
        
        private void register(String id, Operation op) {
            operations.put(id, op);

            if (logger.isDebugEnabled()) {
                logger.debug("Operation id = " + id + " registered");
            }
        }
    }           
}
