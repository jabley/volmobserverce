/**
 * (c) Volantis Systems Ltd 2006.
 */

 //  This Widget creates a Javascript class, in order to control a progress
 //  bar created via the <widget:progress> XDIME2 widget markup.
 //
 //  Style
 //  -----
 //  All style is applied to the <DIV> progress bar tags, translated
 //  from the XDIME2.
 //
 //  Usage Modes
 //  -----------
 //  The Widget can operate in one of two modes.
 //
 //    i) ACTIVE
 //
 //     To enable this mode, a Widget must be instantiated with "refreshURL" and
 //     "refreshInterval" parameters. An ACTIVE Widget will be switched to PASSIVE
 //     if any of the public control methods are used to control the Widget.
 //     This change ensures that any interval timers are switched off.
 //
 //    ii) PASSIVE
 //
 //     The absence both "refreshURL" and "refreshInterval" parameters implies a PASSIVE
 //     mode. Once in PASSIVE mode, a Widget cannot be altered to be in
 //     ACTIVE mode.
 //
 //  Public Access Methods
 //  ---------------------
 //  The following Client-side methods are accessible publicly:
 //
 //          set(x), sets the progress tracker to "x" percent
 //          get(x), gets the progress
 //     progress(x), increments the progress tracker with "x" percent
 //         reset(), set the progress tracker to zero percent
 //        finish(), set the progress tracker to 100 percent
//         show(), set the progress bar visible from invisible state
//          hide(), set the progress bar invisible from visible state
 //
 //  These methods are intended for Widget operating in PASSIVE mode.
 //  Where used with an ACTIVE Widget, the mode of the Widget will be
 //  changed to PASSIVE.
 //
 //  Client Interaction
 //  ------------------
 //  The main assumption assumption made in this Widget is that the process
 //  that instantiated this Widget (MCS/XDIME2) also create a DIV with an
 //  "id" using the same name as the instantiated "id" of the Widget. This
 //  allows this Widgets methods and Ajax control to access the progress
 //  bar and adjust its width appropriately.
 //
 //  JSON Progress information
 //  -------------------------
 //  The optional URL specified for the Widget has to return information
 //  in JSON format, in this case to express 30% complete the URL response
 //  must be of the form:
 //
 //      {"percentage":30}

  Widget.ProgressBar = Class.create();

  Object.extend(Widget.ProgressBar.prototype, Widget.Appearable);
  Object.extend(Widget.ProgressBar.prototype, Widget.Disappearable);

  Object.extend(Widget.ProgressBar.prototype, Widget.Refreshable);

  Object.extend(Widget.ProgressBar.prototype, {

    initialize: function ( id, options ) {

      // Define the bounds of the percentage range within which to work. Also
      // allocate other constants here.
      MIN_PROGRESS = parseInt(0, 10);
      MAX_PROGRESS = parseInt(100, 10);

      // The following options MUST be set when instantiating the widget
      //
      //  id - (mandatory) This is not only the Javascript object name,
      //                   but also the DIV "id" identity of the progress
      //                   bar that will be targetted by Ajax callbacksg
      //                   and other methods in the instantiated JS object.
      //
      // refreshURL - (optional) URL to use if ACTIVE widget in use
      // refreshInterval - (optional) period between pooling for ACTIVE widget
      this.id = id;
      this.el = $(id);

      // Progress Bar does not use polling to refresh its content.
      // As a consequence, the Widget.isPollingEnabled() flag won't affect it.
      this.usesPolling = false;

      this.setup();

      Object.copyFields(this, options || {});  //refreshURL & refreshInterval if exists
      this.startRefresh();

      // The current progress value for the progress bar
      this.progressValue = MIN_PROGRESS;

      this.locked = false;
    },

    setup: function() {
      this.el.innerHTML =
        "<div style='width:0px; height:inherit; background-color: " +
        this.el.getStyle('color') +
        ";'></div>";
    },

    // Methods to set and test Widget operational mode
    // Get the progress value
    getProgressValue: function() {
      return this.progressValue;
    },

    // Set the progress value
    setProgressValue: function(newValue) {

      var progressValue = parseInt(newValue, 10);

      if (isNaN(progressValue)) {
        return;
      }

      if (progressValue < MIN_PROGRESS) {
        progressValue = MIN_PROGRESS;
      } else if (progressValue > MAX_PROGRESS) {
        progressValue = MAX_PROGRESS;
      }

      this.progressValue = progressValue;
    },

    // Process a successful Ajax response, capture the new progress value.
    // The value returned is a JSON expression and pick out "percentage"
    // eg. The response will be like {"percentage":30}, to express 30%.
    processAJAXResponse: function (originalRequest) {
      var myObject = eval('(' + originalRequest.responseText + ')');

      this.setProgressValue(myObject.percentage);
      this.refreshBAR();

      if(this.getProgressValue() >= MAX_PROGRESS) {
        this.disableUpdates();
      }
    },

    // Update the progress bar
    refreshBAR: function () {
      this.el.firstChild.style.width = this.getProgressValue() + '%';
    },

   /* Public API */

    /**
     * Shows the widget using the effect specified in the appropriate style.
     * Has no effect if the widget is already visible or when an appear effect is already running.
     *
     * @volantis-api-include-in PublicAPI
     */
    show: function() {
      if(this.el.getStyle('visibility') == 'hidden') {
        this.el.style.display = 'none';
        this.el.style.visibility = 'visible';
      }

      if(this.el.style.display != 'none' || this.locked) {
        return;
      }
      this.locked = true;
      var id = this.id;
      Widget.TransitionFactory.createAppearEffect(this.el, this, {
        afterFinish: function(effect) {
          Widget.getInstance(id).locked = false;
        }
      });
    },

    /**
     * Hides the widget using the effect specified in the appropriate style.
     * Has no effect if the widget is already hidden or when a disappear effect is already running.
     *
     * @volantis-api-include-in PublicAPI
     */
    hide: function() {
      if(this.el.style.display == 'none' || this.locked) {
        return;
      }

      this.locked = true;
      var id = this.id;
      Widget.TransitionFactory.createDisappearEffect(this.el , this, {
        afterFinish: function(effect) {
          Widget.getInstance(id).locked = false;
        }
      });
    },

    /**
     * Sets the current progress. If n is <=0 , the progress is set to 0.
     * If n >= 100, the progress is set to 100. If n is not a number, the progress is not changed.
     * Any use implies the Widget will become PASSIVE.
     *
     * @volantis-api-include-in PublicAPI
     */
    set: function (newValue) {
      this.setProgressValue(newValue);
      this.refreshBAR();
      this.disableUpdates();
    },

    /**
     * Returns the current progress.
     *
     * @volantis-api-include-in PublicAPI
     */
    get: function() {
      return this.getProgressValue();
    },

    /**
     * Equivalent to set(0)
     *
     * @volantis-api-include-in PublicAPI
     */
    reset: function () {
      this.setProgressValue(MIN_PROGRESS);
      this.refreshBAR();
      this.disableUpdates();
    },

    /**
     * Equivalent to set(100)
     * Set the progress to complete, stop polling if active.
     *
     * @volantis-api-include-in PublicAPI
     */
    finish: function () {
      this.setProgressValue(MAX_PROGRESS);
      this.refreshBAR();
      this.disableUpdates();
    },

    /**
     * Increments the progress by d, equivalent to set(get() + d)
     *
     * @volantis-api-include-in PublicAPI
     */
    progress: function (delta) {
      var deltaValue = parseInt(delta, 10);
      this.set(this.get() + deltaValue);
    }

  });
