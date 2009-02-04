/**
 * (c) Volantis Systems Ltd 2007.
 */

/**
 * widget:stopwatch
 */
Widget.Stopwatch = Widget.define(
{
  initialize: function(id, count_mode, splitContainer, options) {
    this.initializeWidget(id, options)
    //Block widget for holding split and lap times
    this.splitContainer = splitContainer    
    
    this.count_mode = count_mode;

    this.options = options;
    
    this.installOptions(options);

    this.timeDisplay = new Widget.TimeDisplay(id, options, 'timer');
  
    //delay between clock refreshes its displayed value
    //possible values are: 123, 1000, 60000 or 3600000
    this.delay = this.timeDisplay.getAccuracy();
    
    var d = new Date();
    //on nokia oss offset has different sign
    this.sgn = Prototype.nokiaOSSBrowser() ? -1 : 1;
    // to handle timezones corractly
    this.offset = d.getTimezoneOffset()*60*1000*this.sgn;

    // Stopwatch does not use polling to refresh its content.
    // As a consequence, the Widget.isPollingEnabled() flag won't affect it.
    this.usesPolling = false;

    //initilalize Date object that will be used to count elapsed time
    this.elapsedTime = new Date(0);
    this.lapTime = new Date(0);
    this.savedMS = 0;
    this.displayTime = new Date(0);
    this.stopped = true;
    this.paused = false;

    this.startTime = new Date();
    this.currentTime = new Date();

    //start time of last lap
    this.lastLap = new Date();


    this.addAction('start');
    this.addAction('stop');
    this.addAction('reset');
    this.addAction('split');
    this.reset();
  },

  /**
   *Resets stoper and cleans lap/split records  
   * @volantis-api-include-in PublicAPI
   */
  reset : function (){
    this.currentTime = new Date();
    this.stop();
    this.paused = false;
    this.savedMS=0;
    this.displayTime = new Date(this.offset);
    this.timeDisplay.setTimeToDisplay(this.displayTime);
    this.splitContainer.setContent(null);
  },

   /*
    * Calculates the time that is to be displayed.
    * @returns this.displayTime; - time with proper offset.
    */
  getDisplayTime : function (){
    this.lastLapMS = this.currentTime.getTime() - this.lastLap.getTime();

    if (this.count_mode == 'lap') {
      this.displayTime.setTime(this.lastLapMS+this.offset);
    } else {
      this.displayTime.setTime(this.lastLapMS+this.savedMS+this.offset);
    }

    return this.displayTime;
  },

  getDisplaySummaryTime : function (){
    this.lastLapMS = this.currentTime.getTime() - this.lastLap.getTime();
    this.displayTime.setTime(this.lastLapMS+this.savedMS+this.offset);
    return this.displayTime;
  },

  /**
   * Starts stopwatch.
   * @volantis-api-include-in PublicAPI
   */
  start : function(){
    this.currentTime = new Date();
    if (this.stopped){
      this.stopped = false;
      if (!this.paused) {
        this.startTime.setTime(this.currentTime.getTime());
        this.lastLap.setTime(this.currentTime.getTime());

      } else {
        this.lastLap.setTime(this.currentTime.getTime() - this.lastLapMS);
        this.paused = false;
      }

      this.preInterval = setInterval(this.preUpdate.bind(this), 75 );
      this.timeDisplay.setTimeToDisplay(this.getDisplayTime());
    }

  },

  /**
   * Stops stopwatch  
   * @volantis-api-include-in PublicAPI
   */
  stop : function(){
    this.currentTime = new Date();

    if (!this.stopped){

      this.stopped = true;
      this.paused = true;

      this.timeDisplay.setTimeToDisplay(this.getDisplaySummaryTime());


      clearInterval(this.interval);
    }
  },

  /**
   * Takes split time and appends it to BlokContent container
   * @volantis-api-include-in PublicAPI
   */
  split: function(){
    if (!this.stopped || this.paused) {
      this.currentTime = new Date();
      (this.count_mode=='lap') ? this.doLap() : this.doSplit();
    }
  },

  doSplit : function(){
    var content = new Widget.Internal.BlockContent(null, "<div></div>");
    this.splitContainer.addLast(content)
    var d = new Widget.TimeDisplay(content.getElement(), this.options, 'timer');
    d.setTimeToDisplay(this.displayTime);
  },


  doLap : function(){
    var content = new Widget.Internal.BlockContent(null, "<div></div>");
    this.splitContainer.addLast(content)
    var d = new Widget.TimeDisplay(content.getElement(), this.options,'timer');
    if (!this.stopped){
      this.lastLapMS = this.currentTime.getTime() - this.lastLap.getTime();
      this.lastLap.setTime(this.currentTime.getTime());
      this.lapTime.setTime(this.lastLapMS+this.offset);
      this.savedMS += this.lastLapMS;
    } else {
      this.lapTime.setTime(this.lastLapMS+this.offset);
      this.savedMS += this.lastLapMS;
      this.lastLapMS=0;
    }
    d.setTimeToDisplay(this.lapTime);
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
    //clears the PRE interval
    clearInterval(this.preInterval);
  },


  update: function() {
    this.currentTime = new Date();
    this.timeDisplay.setTimeToDisplay(this.getDisplayTime());
  }
}
);

