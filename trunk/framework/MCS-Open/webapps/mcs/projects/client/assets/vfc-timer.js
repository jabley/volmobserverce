/**
 * (c) Volantis Systems Ltd 2007.
 */ 

/**
* widget:timer
*/
Widget.Timer = Widget.define(
{
  initialize: function(id, options) {
    this.initializeWidget(id, options)

    this.start_time = null;
    this.stop_time = null;
    this.load_src = null;

    this.installOptions(options);
    
    this.startTime = this.start_time;
    this.stopTime = this.stop_time;

    this.timeDisplay = new Widget.TimeDisplay(id, options, 'timer');

    var d = new Date(0);
    this.sgn = Prototype.nokiaOSSBrowser() ? -1 : 1;
    this.offset = d.getTimezoneOffset()*60*1000*this.sgn;
  
    //delay between clock refreshes its displayed value
    //possible values are: 123, 1000, 60000 or 3600000
    this.delay = this.timeDisplay.getAccuracy();

    // Clock does not use polling to refresh its content.
    // As a consequence, the Widget.isPollingEnabled() flag won't affect it.
    this.usesPolling = false;

    //initilalize Date object that will be used to count elapsed time
    this.elapsedTime = new Date(0);
    this.stopped = true;        

    //load strart/stop times from configuration or from attributes
    if (this.load_src == null){
      this.startMS = parseInt(this.startTime);
      this.initialStartMS = parseInt(this.startTime);
      this.stopMS = parseInt(this.stopTime);
    }else {
      this.loadConfiguration();
    }
    
    // Add actions and events an properties
    this.addAction('start');
    this.addAction('stop');
    this.addAction('reset');    
    this.addEvent('finished');
    this.addProperty('start-time', {type: "int"});
    this.addProperty('stop-time', {type: "int"});
    this.displayTime = new Date(this.offset);
    this.reset();
  },

   // Loads configuraton object
   loadConfiguration: function () {
     new Widget.AjaxRequest(this.load_src,
                             {method: "get",
                              onSuccess: this.configurationLoaded.bind(this)})
    },

    // Process AJAX response
    configurationLoaded: function(request) {
      var myObject = eval('(' + request.responseText + ')');
      this.startMS = parseInt(myObject.start);
      this.stopMS = parseInt(myObject.stop);
      this.initialStartMS = this.startMS;
      this.reset();
    },

  /**
   * Resets timer.
   * @volantis-api-include-in PublicAPI
   */  
  reset : function (){
    clearInterval(this.interval);
    this.stopped = true;
    this.finished = false;
    var s = this.initialStartMS+this.offset
    this.displayTime.setTime(this.initialStartMS+this.offset);
    this.elapsedMS = 0;
    this.startMS = this.initialStartMS;
    this.timeDisplay.calculateMinDigitsCount(this.displayTime)
    this.timeDisplay.setTimeToDisplay(this.displayTime);
  },

  getDisplayTime : function (){
    this.currentTime = new Date();
    this.elapsedMS = this.currentTime.getTime() - this.startTime.getTime(); 
    this.displayMS = this.startMS - this.elapsedMS;
    this.displayTime.setTime(this.displayMS+this.offset);
    return this.displayTime;
  },

  /**
   * Start counting down
   * @volantis-api-include-in PublicAPI
   */
  start : function(){
    if (this.stopped && !this.finished && this.startMS>0){
      this.stopped = false;
      this.startTime = new Date();
      this.currentTime = new Date();    
      this.preInterval = setInterval(this.preUpdate.bind(this), 123);
      this.timeDisplay.setTimeToDisplay(this.getDisplayTime());
    }
  },

  /**
   * Stop counting down
   * @volantis-api-include-in PublicAPI
   */
  stop : function(){
    if (!this.stopped){
      this.stopped = true;
      this.timeDisplay.setTimeToDisplay(this.getDisplayTime());
      this.startMS = this.startMS - this.elapsedMS;
      clearInterval(this.interval);
    }
  },


   /**
   * If refreshing should take place every minute or more,
   * then it should be just after the change occurs.
   */
  preUpdate: function() {
    this.startUpdate(); 
  },
  
  /** 
   * Should be called from preUpdate()
   * Starts periodically changes the time represented by the clock
   */
  startUpdate: function() {
    this.timeDisplay.setTimeToDisplay(this.getDisplayTime());
    this.interval = setInterval(this.update.bind(this), this.delay);
    clearInterval(this.preInterval);
  },


  update: function() {    
      this.getDisplayTime();
      var i = this.displayTime.getTime()-this.offset;
      if (i<=this.stopMS) { 
        //stop timer
        clearInterval(this.interval);
        this.stopped = true;
        this.finished = true;
        this.displayTime.setTime(this.offset+this.stopMS);
        this.timeDisplay.setTimeToDisplay(this.displayTime);

        this.notifyObservers('finished');
      }else{
        this.timeDisplay.setTimeToDisplay(this.displayTime);
      }
  },

  setStartTime:function(time){
    if (time!= null && time != this.startTime && time>=0) {      
      this.startTime = time;
      this.notifyObservers("startTimeChanged");
      this.startMS = parseInt(this.startTime);
      this.initialStartMS = parseInt(this.startTime);
      this.reset();
    }
  },

  setStopTime:function(time){
    if (time!= null && time != this.stopTime && time>=0) {
      this.stopTime = time;
      this.notifyObservers("stopTimeChanged");
      this.stopMS = parseInt(this.stopTime);
      this.reset();
    }
  },

  getStartTime:function(){
    return this.startTime;
  },

  getStopTime:function(){
    return this.stopTime;
  }

}
);




