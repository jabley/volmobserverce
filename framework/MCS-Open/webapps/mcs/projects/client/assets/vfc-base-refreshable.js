

/**
 * (c) Volantis Systems Ltd 2008. 
 */

/*------------------ Widget.Refreshable ----------------------------*/
// NOTE: Widget have to implement processAJAXResponse callback function

Widget.Refreshable = {
  refreshURL: '',
  refreshInterval: '',

  // This flag controls, whether the Widget using this mixin
  // is treated as using polling or not. If set to 'false',
  // the Widget.isPollingEnabled() flag will not affect it.
  usesPolling: true,

//implemenation
  refreshMode: 'PASSIVE',
  refreshIntervalId: null,

  startRefresh: function() {
    if (this.refreshInterval !== "" && this.refreshInterval !== null &&
      this.refreshURL !== "" && this.refreshURL !== null) {
      this.refreshMode = 'ACTIVE';
      this.refreshIntervalId = setInterval(this.forceUpdateOnTimeout.bind(this), this.refreshInterval * 1000);
    }
  },

  /**
   * Changes widget state to passive so no Ajax updates will be possible.
   *
   * @volantis-api-include-in PublicAPI
   */
  disableUpdates: function() {
    if(this.refreshIntervalId !== null)
    {
      clearInterval(this.refreshIntervalId);
      this.refreshIntervalId=null;
    }
    this.refreshMode = 'PASSIVE';
  },

  /**
   * Forces an AJAX update as a consequence of timeout.
   */
  forceUpdateOnTimeout: function () {
    if (!this.usesPolling || Widget.isPollingEnabled()) {
      this.forceUpdate();
    }
  },

  /**
   * Forces an AJAX update. Does nothing if there was no <widget:refresh> tag for this widget.
   *
   * @volantis-api-include-in PublicAPI
   */
  forceUpdate: function () {
    if (this.refreshMode == "ACTIVE") {

    // NOTE: that you MUST use bind in the onComplete specification, to
    // ensure that the function executes with "this" bound as intended
    // ie. from the Widget we instantiated.

      if (this.processAJAXResponse && typeof this.processAJAXResponse == 'function') {
        // The rid parameter represents unique RequestID.
        // Some malicious browsers may cache the response,
        // and this simple trick helps to avoid caching.
        this.requestId = (this.requestId === null) ? 0 : this.requestId + 1;

        var ar = new Widget.AjaxRequest(
          this.refreshURL,
          { method: 'get',
          parameters: {rid: this.requestId},
          onSuccess: this.processAJAXResponse.bind(this) } );
      } else {
        this.disableUpdates();
      }
    }
  }
}; // Refreshable
