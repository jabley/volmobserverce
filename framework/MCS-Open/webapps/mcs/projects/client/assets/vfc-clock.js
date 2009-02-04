/**
 * (c) Volantis Systems Ltd 2007.
 */
 
/**
* widget:digital-clock
*/
Widget.Clock = Widget.define(Widget.Refreshable,
{
  initialize: function(id, options) {
    this.initializeWidget(id, options)
    
    this.timeDisplay = new Widget.TimeDisplay(id, options,'clock');

    //used to update this.time using AJAX request
    this.setTimeMethods = [
        'setFullYear',
        'setMonth',
        'setDate',
        'setHours',
        'setMinutes',
        'setSeconds'];
     
    //delay between clock refreshes its displayed value
    //possible values are: 1000, 60000 or 3600000
    this.delay = this.timeDisplay.getAccuracy();
    
    this.time = new Date();
    
    this.sgn = Prototype.nokiaOSSBrowser() ? -1 : 1;

    // Clock does not use polling to refresh its content.
    // As a consequence, the Widget.isPollingEnabled() flag won't affect it.
    this.usesPolling = false;

    this.startSync();
    
    this.addAction("force-sync");
  },
  
  startSync: function() {
    if (this.refreshURL) {
      this.startRefresh();
      this.forceUpdate();
    }  
    this.preInterval = setInterval(this.preUpdate.bind(this), 1000);
    this.timeDisplay.setTimeToDisplay(this.time);
  },
  

  /**
   * If refreshing should take place every minute or more,
   * then it should be just after the change occurs.
   */
  preUpdate: function() {
    switch (this.delay) {
      case 1000:
        this.startUpdate();
        break;
      case 60000:
        var min = this.time.getMinutes();
        this.time.setSeconds(this.time.getSeconds() + 1);
        if (min != this.time.getMinutes()) {
          this.startUpdate();;
        }
        break;
      case 3600000:
        var h = this.time.getHours();
        this.time.setSeconds(this.time.getSeconds() + 1);
        if (h != this.time.getHours()) {
          this.startUpdate();
        }
        break;
    }
    var timeCurr = new Date();
    this.timeDiff = this.time.getTime() - timeCurr.getTime();
    Widget.log("diff2", this.timeDiff)
  },
  
  /** 
   * Should be called from preUpdate()
   * Starts periodically changes the time represented by the clock
   */
  startUpdate: function() {
    this.timeDisplay.setTimeToDisplay(this.time);        
    setInterval(this.update.bind(this), this.delay);   
    clearInterval(this.preInterval);
  },

  update: function() {
    var dateTmp = new Date();
    if (this.refreshURL == "") {
      this.time = dateTmp;
    } else {
      this.time.setTime(dateTmp.getTime() + this.timeDiff*this.sgn);
    }
    this.timeDisplay.setTimeToDisplay(this.time);
  },
  
  /**
   * forces a synchronisation of the clock by AJAX request
   *
   * @volantis-api-include-in PublicAPI
   */
  forceSync: function() {
    if (this.refreshURL == '') {
      this.update();
    } else {
      this.forceUpdate();
    } 
  },
  
  /**
   * Synchronizes time with the local device or service if defined
   */
  syncLocalTime: function() {
    this.time = new Date();
    this.timeDisplay.setTimeToDisplay(this.time);
  },
  
  // Process a successful Ajax response, capture the new progress value.
  // The response format is {date: [Year, Month, Day, Hours, Minutes, Seconds]}
  processAJAXResponse: function (originalRequest) {
    var myObject = eval('(' + originalRequest.responseText + ')');
    var dateArray = myObject.date;
    var tmp = new Date();
    tmp.setTime(dateArray[6]);
    var offset = tmp.getTimezoneOffset()*60*1000;
    this.time.setTime(dateArray[6]+offset*this.sgn);  
    tmp = new Date();
    this.timeDiff = this.sgn*(this.time.getTime() - tmp.getTime());
    this.timeDisplay.setTimeToDisplay(this.time);
  }
}
);

