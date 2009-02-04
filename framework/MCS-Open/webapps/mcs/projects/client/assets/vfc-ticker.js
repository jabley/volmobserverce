/**
 * (c) Volantis Systems Ltd 2006.
 */

//moved from vfc-base.js as it is used only in this file
Object.extend(Widget,{
  /**
   * Adds callback function, which will be invoked when polling
   * enabled status is changed.
   */
  addPollingEnabledCallback: function(callback) {
    if (this.pollingEnabledCallbackHandler == null) {
      this.pollingEnabledCallbackHandler = new Widget.CallbackHandler()
    }

    this.pollingEnabledCallbackHandler.add(callback)
  }
});


//moved from vfc-base.js as it is used only in this file
/*
 * A delaying callback handler. Guarantees, that callbacks will
 * invoked at most once during given time interval (delay).
 *
 * This class may be used as a proxy between API object
 * and corresponding UI control, to make sure that it's not
 * refreshed too often (which may be very costly).
 *
 * Following classes makes use of this Callback Handler class:
 *  - Ticker.TickerTapeController
 *  - Ticker.CarouselController
 */
Widget.DelayCallbackHandler = Class.define(
{
  /*
   * Initialization.
   *
   * @param interval The interval in milliseconds (default: 1000)
   */
  initialize: function(interval) {
    this.baseCallbackHandler = new Widget.CallbackHandler()
    this.interval = interval ? interval : 1000; // Default: 1 second.
  },

  // See: Widget.CallbackHandler.add()
  add: function(callback) {
    this.baseCallbackHandler.add(callback)
  },

  // See: Widget.CallbackHandler.remove()
  remove: function(callback) {
    this.baseCallbackHandler.remove(callback)
  },

  // See: Widget.CallbackHandler.invoke()
  invoke: function() {
    if (this.timer == null) {
      this.timer = setTimeout(this.invokeNow.bind(this), this.interval)
    }
  },

  invokeNow: function() {
    this.timer = null
    this.baseCallbackHandler.invoke()
  }
})




var Ticker = {
  // Offline mode is designed to be used for debugging purposees only.
  // In offline mode, all requests are simulated locally.
  offlineMode: false,

  // An instance of the FeedPoller, which is
  // currently performing a request.
  // Note: If in the future there'll be a functionality
  // to include more than one feed poller on the page,
  // this must be reimplemented.
  requestingFeedPoller: null,

  /*
   * Returns requesting FeedPoller instance,
   * for use by the scripts enclosed within
   * FeedPoller responses.
   */
  getRequestingFeedPoller: function() {
    return this.requestingFeedPoller;
  },

  /*
   * Creates and returns an instance of FeedPoller,
   * if it's not already created.
   * Since this method enables you to create only one
   * instance of FeedPoller, it should be called only once.
   */
  createFeedPoller: function(options) {
    if (!this.feedPoller) {
      this.feedPoller = new Ticker.FeedPoller(options);
    }
  },

  /*
   * Returns an instance of FeedPoller, if already created
   * using createFeedPoller() method.
   */
  getFeedPoller: function() {
    return this.feedPoller;
  },

  /*
   * Returns an instance of ItemStorage, if already created.
   */
  getItemStorage: function() {
    var feedPoller = this.getFeedPoller();

    if (feedPoller) {
      return feedPoller.getStorage();
    }
  },

  /*
   * Returns an instance of ItemStorageStats, if already created.
   */
  getItemStorageStats: function() {
    if (!this.itemStorageStats) {
      var itemStorage = this.getItemStorage();

      if (itemStorage) {
        this.itemStorageStats = new Ticker.ItemStorageStats(itemStorage);
      }
    }

    return this.itemStorageStats;
  },

  /*
   * Returns an instance of TickerTapeController.
   */
  createTickerTapeController: function(options) {
    options.itemStorage = this.getItemStorage();

    return new Ticker.TickerTapeController(options);
  },

  /*
   * Returns an instance of CarouselController.
   */
  createCarouselController: function(options) {
    options.itemStorage = this.getItemStorage();

    return new Ticker.CarouselController(options);
  },

  /*
   * Creates an instance of UpdateStatus widget.
   */
  createUpdateStatus: function(options) {
    options.feedPoller = this.getFeedPoller();

    return new Ticker.UpdateStatus(options);
  },

  /*
   * Creates an instance of ItemsCount widget.
   */
  createItemsCount: function(options) {
    options.storageStats = this.getItemStorageStats();

    return new Ticker.ItemsCount(options);
  },

  /*
   * Creates an instance of ChannelsCount widget.
   */
  createChannelsCount: function(options) {
    options.storageStats = this.getItemStorageStats();

    return new Ticker.ChannelsCount(options);
  },

  /*
   * Creates an instance of ItemProperty widget.
   */
  createItemProperty: function(options) {
    return new Ticker.ItemProperty(options);
  },

  /*
   * Creates an instance of ItemDisplay widget.
   */
  createItemDisplay: function(options) {
    return new Ticker.ItemDisplay(options);
  }
};

/**
 * A feed item.
 *
 * Each item belongs to a single, named channel.
 * Each item contains an ID, title, icon, and description.
 * Each item may be read/unread and followed/unfollowed.
 */
Ticker.Item = Class.define(Widget.OptionsContainer, Widget.CallbackMixin,
{
  id: null,
  channel: "",
  title: "",
  icon: "",
  description: "",
  plainDescription: "",
  read: false,
  followed: false,

  /**
   * Initialization.
   */
  initialize: function(options) {
    this.installOptions(options);

    this.callbackHandler = new Widget.CallbackHandler();
  },

  /*
   * Returns item's ID.
   */
  getId: function() {
    return this.id;
  },

  /*
   * Returns item's channel.
   */
  getChannel: function() {
    return this.channel;
  },

  /**
   * Returns item's title.
   */
  getTitle: function() {
    return this.title;
  },

  getTitleElement: function() {
    if (!this.titleElement) {
      this.titleElement = document.createElement("span");
      this.titleElement.innerHTML = this.title;
    }

    return this.titleElement;
  },

  /**
   * Returns item's icon.
   */
  getIcon: function() {
    return this.icon;
  },

  getIconElement: function() {
    if (this.iconElement === undefined) {
      if (!this.icon) {
        this.iconElement = null;
      } else {
        this.iconElement = document.createElement("span");
        this.iconElement.innerHTML = this.icon;
      }
    }

    return this.iconElement;
  },

  /**
   * Return item's description.
   */
  getDescription: function() {
    return this.description;
  },

  getDescriptionElement: function() {
    if (this.descriptionElement === undefined) {
      if (!this.description) {
        this.descriptionElement = null;
      } else {
        this.descriptionElement = document.createElement("div");
        this.descriptionElement.innerHTML = this.description;
      }
    }

    return this.descriptionElement;
  },

  /**
   * Return item's description.
   */
  getPlainDescription: function() {
    return this.plainDescription;
  },

  getPlainDescriptionElement: function() {
    if (this.plainDescriptionElement === undefined) {
      if (!this.plainDescription) {
        this.plainDescriptionElement = null;
      } else {
        this.plainDescriptionElement = document.createElement("span");
        this.plainDescriptionElement.innerHTML = this.plainDescription;
      }
    }

    return this.plainDescriptionElement;
  },

  /**
   * Returns true, if this item has already been read,
   * false otherwise.
   */
  isRead: function() {
    return this.read;
  },

  /**
   * Marks item as read.
   */
  setRead: function(read) {
    this.read = read;
    this.callbackHandler.invoke(this);
  },

  /**
   * Returns true, if this item has already been followed,
   * false otherwise.
   */
  isFollowed: function() {
    return this.followed;
  },

  /**
   * Marks this item's as followed.
   */
  setFollowed: function(followed) {
    this.followed = followed;
    this.callbackHandler.invoke(this);
  },

  /*
   * Returns true, if this item passes a filter specified
   * by 'channel', 'read' and 'followed' attributes.
   */
  filter: function(channel, read, followed) {
    return ((channel === undefined) || (channel === null) || (this.channel == channel)) &&
           ((read === undefined) || (read === null) || (this.read == read)) &&
           ((followed === undefined) || (followed === null) || (this.followed == followed));
  }
});


/**
 * Item Storage is used to maintain the current set of items.
 */
Ticker.ItemStorage = Class.define(Widget.CallbackMixin,
{
  /**
   * Initialization.
   */
  initialize: function() {
    // An array for item storage.
    // Items are ordered from the most recent to the most decent one,
    // regardless of the belonging channel.
    this.items = new Array();

    // This map will hold all removed items.
    this.zombies = {};

    this.callbackHandler = new Widget.CallbackHandler();

    this.updated = false;
  },

  /**
   * Adds new item to storage, and returns true.
   * If there already is an item with the same ID, this method
   * does nothing and returns false.
   *
   * @param item An item to add.
   * @return Success flag.
   */
  addItem: function(item) {
    this.updated = true;

    if (this.getItem(item)) {
      return false;
    } else {
      //Widget.log("ItemStorage", "Adding item: " + item.getId());

      this.items.splice(0,0,item);
      this.callbackHandler.invoke('add', item);

      return true;
    }
  },

  /**
   * Removes and returns item from storage.
   *
   * @param An item to remove
   * @return success flag
   */
  removeItem: function(itemToRemove) {
    var index = this.getItemIndex(itemToRemove);

    if (index >= 0) {
      var item = this.items[index];

      //Widget.log("ItemStorage", "Removing item: " + item.getId());

      this.zombies[item.getId()] = item;

      this.items.splice(index,1);
      this.callbackHandler.invoke('remove', item);
    }

    return index >= 0;
  },

  /**
   * Returns array of items from given channel.
   * If no channel name is specified - items
   * from all channels are returned.
   * Items are ordered from fresher to older - New messages go first
   *
   * @param channel The channel
   * @param read The 'read' flag
   * @param followed The 'followed' flag
   * @return array of items from given channnel
   */
  getItems: function(channel, read, followed) {
    var items;

    for (var i = 0; i < this.items.length; i++) {
      var item = this.items[i];

      if (item.filter(channel, read, followed)) {
        if (items) {
          items.push(item);
        }
      } else {
        if (!items) {
          items = this.items.slice(0, i);
        }
      }
    }

    return items || this.items;
  },

  /**
   * Returns item with specified id or null if such item does not exist
   * @param id item's id
   * @return item with given id or null if not found.
   */
  getItem: function(id) {
    var index = this.getItemIndex(id);
    var item = null;
    if (index >= 0) {
      item = this.items[index];
    }
    return item;
  },

  getItemIncludingZombies: function(id) {
    var item = this.getItem(id);
    if (!item) {
      item = this.zombies[id];
    }
    return item;
  },

  /**
   * Returns index of the item in items array.
   * If item is not found, it returns nothing.
   *
   * @param itemOrId An item or item's ID
   * @return item's index, if found.
   */
  getItemIndex: function(itemOrId) {
    var itemId = (typeof(itemOrId) == 'object' ? itemOrId.getId() : itemOrId);
    for (var i = 0 ; i < this.items.length ; i++) {
      if (itemId == this.items[i].getId()) {
        return i;
      }
    }
  },

  /**
   * Returns true, if this storage has been updated since it has been created.
   */
  isUpdated: function() {
    return this.updated;
  }
});

/*
 * ItemStorageStats produces statistics for ItemStorage.
 */
Ticker.ItemStorageStats = new Class.define(Widget.CallbackMixin,
{
  initialize: function(storage) {
    // Store the storage.
    this.storage = storage;

    // When storage content changes, invoke the storageChanged() method
    // to update the statistics.
    this.storage.addCallback(this.storageChanged.bind(this));

    // Register a callback on every item in the storage,
    // so that when a status of any item in the storage changes,
    // the statistics are updated.
    this.itemChangedCallback = this.itemChanged.bind(this);

    var items = this.storage.getItems();

    for (var i = 0; i < items.length; i++) {
      items[i].addCallback(this.itemChangedCallback);
    }

    // Create callback handler to implement callbacks.
    this.callbackHandler = new Widget.CallbackHandler();
  },

  /*
   * Returns storage used to produce statistics for.
   */
  getStorage: function() {
    return this.storage;
  },

  /*
   * Invoked when storage content changes (items is added or removed).
   */
  storageChanged: function(operation, item) {
    if (operation == 'add') {
      // If an item has been added, add a callback updating
      // statistics on item status change (unread -> read).
      item.addCallback(this.itemChangedCallback);

    } else if (operation == 'remove') {
      // If an item has been removed, remove the callback.
      item.removeCallback(this.itemChangedCallback);
    }
    // Update statistics now, since items has been added/removed.
    this.updateStats();
  },

  /*
   * Invoked, when a read/unread status of an item changed.
   */
  itemChanged: function() {
    // Simply update the statistics.
    this.updateStats();
  },

  /*
   * Updates the current statistics.
   */
  updateStats: function() {
    //Widget.log("ItemStorageStats", "Stats changed");

    // Invoke callback indicating statistics change.
    this.callbackHandler.invoke();
  },

  /**
   * Counts items from given channel.
   * If no channel name is specified - items
   * from all channels are returned.
   * Items are ordered from fresher to older - New messages go first
   *
   * @param channel optional channel name
   * @return array of items from given channnel
   */
  countItems: function(channel, read, followed) {
    var items = this.storage.getItems();

    var count = 0;

    for (var i = 0; i < items.length; i++) {
      if (items[i].filter(channel, read, followed)) {
        count++;
      }
    }

    return count;
  },

  /*
   * Returns number of channels, filtered by its status.
   * If no status is specified, all channels are counted.
   *
   * @param status The status: 'read' or 'unread'
   * @return Number of channels
   */
  countChannels: function(read, followed) {
    var items = this.storage.getItems();

    var channels = {};
    var count = 0;

    for (var i = 0; i < items.length; i++) {
      item = items[i];

      if (item.filter(null, read, followed)) {
        channel = item.getChannel();

        if (!channels[channel]) {
          channels[channel] = 'dummy';
          count++;
        }
      }
    }

    return count;
  }
});

/*
 * The FeedPoller widget used to update ItemStorage with current
 * feed content.
 */
Ticker.FeedPoller = Class.define(Widget.OptionsContainer, Widget.CallbackMixin,
{
  storage: null,
  url: null,
  newURL: null,
  minPollingInterval: null,
  maxPollingInterval: null,
  status: 'normal',
  firstRequestSucceeded: false,
  redirecting: false,

  /*
   * Initialization
   */
  initialize: function(options) {
    this.installOptions(options);

    this.pollingInterval = this.checkPollingInterval(this.getOption('pollingInterval'));

    // If storage is not specified, create a default one.
    if (!this.storage) {
      this.storage = new Ticker.ItemStorage();
    }

    // Initialise the request. If in offline mode, initialise the stub request.
    if (!Ticker.offlineMode) {
      this.request = new Ticker.FeedPollerRequest(this);
    } else {
      this.request = new Ticker.FeedPollerRequestStub(this);
    }

    this.callbackHandler = new Widget.CallbackHandler();

    this.duringTimedUpdate = false;

    this.resume(true);

    // If polling is enabled/disabled, the status of FeedPoller may change.
    Widget.addPollingEnabledCallback(this.pollingEnabledChanged.bind(this));
  },

  /**
   * Invoked, when polling enabled status has changed
   */
  pollingEnabledChanged: function(enabled) {
    // Suspend/resume this feed poller.
    // Note, that the getStatus() method will return 'suspended'
    // if we are currently during skip times, or polling is disabled.
    if (this.getStatus() == 'suspended') {
      this.suspend();
    } else {
      this.resume(true);
    }

    // The status of the feed-poller may have changed as a consequence,
    // so invoke the callback
    this.callbackHandler.invoke();
  },

  /*
   * Returns storage updated by this FeedPoller.
   *
   * @volantis-api-include-in PublicAPI
   */
  getStorage: function() {
    return this.storage;
  },

  /*
   * A timed update.
   */
  timedUpdate: function() {
    // This method was invoked from timer, so it's ok to
    // clear the timer field, which indicates that the timer is
    // running
    this.timer = null;

    if (!this.duringSkipTimes()) {
      // If currently we are not during skip times,
      // perform an update
      this.duringTimedUpdate = true;

      this.update();
    } else {
      //Widget.log("FeedPoller", "Skipping request because of skip times");

      // Otherwise, schedule the next timed update,
      // which of course may be still during skip times.
      this.setStatus('suspended');

      this.scheduleUpdate();
    }
  },

  /*
   * Updates storage content with current items immediately.
   */
  update: function() {
    if (this.getStatus() != "suspended") {
      //Widget.log("FeedPoller", "Sending request: " + this.getRequestURL());

      // First, set requesting FeedPoller instance,
      // for use by the scripts within the AJAX response.
      Ticker.requestingFeedPoller = this;

      this.setStatus('busy');

      // Send a request.
	    this.request.send();

      // Now, this method returns immediately after sending request.
      // After request is finished, one of the methods will be invoked
      // depending on request result:
      //  - requestSucceeded()
      //  - requestFailed()
    }
  },

  /*
   * Sets status.
   */
  setStatus: function(status) {
    this.status = status;

    this.callbackHandler.invoke();
  },

  /*
   * Returns status.
   */
  getStatus: function() {
    return Widget.isPollingEnabled() ? this.status : "suspended";
  },

  /*
   * Sets new URL, for the forthcoming requests.
   * If three subsequent requests under the new URL
   * fails, it's reverted to the original UR, which was
   * specified in the constructor.
   */
  setURL: function(url) {
    //Widget.log("FeedPoller", "Redirectring: " + url);

    this.newURL = url;
    this.newURLFailures = 0;
    this.redirecting = true;
  },

  /*
   * Suspends polling.
   */
  suspend: function() {
    if (this.timer) {
      clearInterval(this.timer);

      delete this.timer;
    }
  },

  /*
   * Resumes polling.
   *
   * @param requestImmediately If true, first request is sent immediately.
   */
  resume: function(requestImmediately) {
    if (!this.timer) {
      if (requestImmediately) {
        // Invoke the next request immediately.
        this.timedUpdate();
      } else {
        // Schedule the next request
        this.scheduleUpdate();
      }
    }
  },

  /*
   * Schedules the timed update to be invoked
   * after specified interval.
   */
  scheduleUpdate: function() {
    //Widget.log("FeedPoller", "Scheduling request in: " + this.pollingInterval + " seconds");

    // Send further requests in intervals
    this.timer = setTimeout(this.timedUpdate.bind(this), this.pollingInterval * 1000);
  },

  /*
   * Updates the polling interval. If min/max polling
   * intervals were specified, the actual interval is
   * truncated to fit within that range.
   *
   * @param interval The interval in seconds.
   */
  setPollingInterval: function(interval) {
    // Truncate to fit within min-max range.
    interval = this.checkPollingInterval(interval);

    //Widget.log("FeedPoller", "Updating polling interval to " + interval + " seconds");

    this.pollingInterval = interval;
  },

  /**
   * Makes sure that the specified interval is valid.
   * If it exceeds min/max range, it's truncated.
   * If its null, the default is used.
   * This method returns valid interval (possibly truncated).
   */
  checkPollingInterval: function(interval) {
    interval = (!interval) ? 60 : interval;
    interval = this.minPollingInterval ? Math.max(interval, this.minPollingInterval) : interval;
    interval = this.maxPollingInterval ? Math.min(interval, this.maxPollingInterval) : interval;
    return interval;
  },

  /*
   * Sets skip times.
   *
   * The skipTimes property is an array of skip times,
   * Each skip time is represented as 2-element array
   * with 'from' and 'to' time.
   *
   * The time is represented as integer value from
   * range [0-1440), indicating number of minutes
   * since midnight.
   *
   * Example: To set skip times between 5:00-6:30 and 22:00-1:00,
   * write the following statement:
   *  setSkipTimes([[300,390],[1320-60])
   *
   * @param skipTimes The skipTimes, or null.
   */
  setSkipTimes: function(skipTimes) {
    this.skipTimes = skipTimes;
  },

  /*
   * Returns current time as integer value representing
   * number of full minutes elapsed since midnight.
   *
   * @returns The number of minutes elapsed since midnight.
   */
  getCurrentTime: function() {
    var date = new Date();

    return date.getHours() * 60 + date.getMinutes();
  },

  /*
   * Returns true, if we are currently during skip times.
   *
   * @returns True, if during skip times.
   */
  duringSkipTimes: function() {
    if (!this.skipTimes) {
      return;
    }

    var currentTime = this.getCurrentTime();

    for (var i = 0; i < this.skipTimes.length; i++) {
      var skipTime = this.skipTimes[i];
      var fromTime = skipTime[0];
      var toTime = skipTime[1];

      if (fromTime <= toTime) {
        // Case 1: The skip time within the same day, ie 10:00-13:00
        if ((currentTime >= fromTime) && (currentTime < toTime)) {
          return true;
        }
      } else {
        // Case 2: The skip time passes midnight, ie 23:00-2:00
        if ((currentTime >= fromTime) || (currentTime < toTime)) {
          return true;
        }
      }
    }

    return false;
  },

  /*
   * Returns URL used for the request.
   */
  getRequestURL: function() {
    return this.newURL || this.url;
  },

  /*
   * Invoked on request success.
   */
  requestSucceeded: function() {
    // Clear the varialbe containing currently requesting
    // FeedPoller, since we've just finished the request.
    Ticker.requestingFeedPoller = null;

    // Set the flag indicating, that this is the first
    // request which succeeded, unless the first request
    // was redirection - we want the redirected request
    // which did not modify the storage to be initial as well.
    if (!this.redirecting || this.storage.isUpdated()) {
      this.firstRequestSucceeded = true;
    }

    // Reset the counter of failed requests.
    this.newURLFailures = 0;

    // Invoke common action on request finish.
    this.requestFinished();

    // Set FeedPoller status.
    this.setStatus('normal');

    //Widget.log("FeedPoller", "Request succeeded");
  },

  requestFailed: function() {
    // Increase the counter with failed requests.
    this.newURLFailures++;

    // If there are three subsequent request failures
    // using new URL (not the original one), revert URL
    // to the original.
    if (this.newURLFailures >= 3) {
      //Widget.log("FeedPoller", "Reseting to original URL, because 3 subsequent requests failed");

      delete this.newURL;
      delete this.newURLFailures;
    }

    // Invoke common action on request finish.
    this.requestFinished();

    // Set FeedPoller status.
    this.setStatus('failed');

    //Widget.log("FeedPoller", "Request failed");
  },

  /*
   * Invoked, when request is finished (finished or failed).
   */
  requestFinished: function() {
    // Clear the varialbe containing currently requesting
    // FeedPoller, since we've just finished the request.
    Ticker.requestingFeedPoller = null;

    // If request was invoked from a timer, schedule the
    // next request now.
    if (this.duringTimedUpdate) {
      this.duringTimedUpdate = false;

      // If URL was set during a request, send new requests immediately
      // Otherwise do it after specified interval.
      var resumeImmediately = this.redirecting;

      // Clear the flag
      this.redirecting = false;

      // Schedule next request using timer, because it must be done
      // after this request is finished completely. Setting timeout to 0
      // makes it to be called after JavaScript returns to event loop.
      setTimeout(this.resume.bind(this, resumeImmediately), 0);
    }
  },

  /*
   * Returns true, if the next request to be send it initial
   */
  isInitialRequest: function() {
    return !this.firstRequestSucceeded;
  }
});

/*
 * Abstract FeedPollerRequest class, which can be used as a
 * template for actual implementation.
 */
Ticker.AbstractFeedPollerRequest = Class.define(
{
  /*
   * Initialization.
   * Note: This method must be called within subclassed initialize() method.
   */
  initializeAbstract: function(feedPoller) {
    this.feedPoller = feedPoller;
  },

  /*
   * Sends a request
   */
  send: function() {
    this.onSuccess();
  },

  /*
   * Returns the URL to be used by the next sent request.
   */
  getURL: function() {
    return this.feedPoller.getRequestURL();
  },

  /*
   * Returns true, if the next request is an initial one.
   */
  isInitial: function() {
    return this.feedPoller.isInitialRequest();
  },

  /*
   * Invoked on request success
   */
  onSuccess: function() {
    this.feedPoller.requestSucceeded();
  },

  /*
   * Invoked on request failure.
   */
  onFailure: function() {
    this.feedPoller.requestFailed();
  }
});

/*
 * FeedPoller Request.
 */
Ticker.FeedPollerRequest = Class.define(Ticker.AbstractFeedPollerRequest,
{
  /*
   * Intialization
   */
  initialize: function(feedPoller) {
    this.initializeAbstract(feedPoller);
  },

  /*
   * Sends request.
   */
  send: function() {
    this.request = this.encodeAJAXRequest();
  },

  /*
   * Creates an AJAX Request
   */
  encodeAJAXRequest: function() {
    var url = this.getURL();
    var parameters = this.getURLParameters();

    return new Widget.AjaxRequest(url, {
                parameters: parameters,
                method: "get",
                onSuccess: this.onAJAXRequestSuccess.bind(this),
                onFailure: this.onAJAXRequestFailure.bind(this),
                onException: this.onAJAXRequestFailure.bind(this)});
  },

  /*
   * Returns URL query parameters
   */
  getURLParameters: function() {
    if (this.isInitial()) {
      return "";
    } else {
      return this.encodeURLParameters();
    }
  },

  /*
   * Returns HTTP query string with encoded FeedPoller context.
   */
  encodeURLParameters: function() {
    // The seed attribute is there to avoid response caching.
    return $H({context: this.encodeContext()}).toQueryString();
  },

  /*
   * Returns FeedPoller context string.
   */
  encodeContext: function() {
    return this.encodeItemIds(this.feedPoller.getStorage().getItems(null, false, false)) + '~' +
           this.encodeItemIds(this.feedPoller.getStorage().getItems(null, true, false)) + '~' +
           this.encodeItemIds(this.feedPoller.getStorage().getItems(null, null, true));
  },

  /*
   * Returns stirng with encoded list of item IDs.
   */
  encodeItemIds: function(items) {
    var result = "";

    for (var i = 0; i < items.length; i++) {
      var item = items[i];

      if (i > 0) {
        result += ".";
      }

      result += this.encodeItemId(item);
    }

    return result;
  },

  /*
   * Return string with encoded item ID.
   */
  encodeItemId: function(item) {
    return item.getId();
  },

  /*
   * Invoked on AJAX request success.
   */
  onAJAXRequestSuccess: function(request) {
    //Widget.log("FeedPoller", "AJAX request succeeded");

    // Get response text out of the request.
    var response = request.responseText;

    // Insert response content, stripping unnessecary scripts.
    this.getResponseArea().innerHTML = response.stripScripts();

    // Evaluates scripts from the response content.
    // This is done using small timeout. I don't know
    // what is it for, but this is how they do it in
    // the prototype.js library.
    setTimeout(this.evalResponseScripts.bind(this, response), 10);
  },

  /*
   * Invoked on AJAX request failure.
   */
  onAJAXRequestFailure: function() {
    //Widget.log("FeedPoller", "AJAX request failed");

    this.onFailure();
  },

  /*
   * Evaluates scripts enclosed in response content.
   */
  evalResponseScripts: function(response) {
    // Evaluate response scripts, which will invoke
    // processResponse() method.
    response.evalScripts();

    // Clear response area, after it's been processed.
    this.getResponseArea().innerHTML = "";

    // Request succedded.
    this.onSuccess();
  },

  /*
   * Creates and returns new HTML element, which can act
   * as a placeholder for response content.
   */
  createResponseArea: function() {
    var responseArea = document.createElement("div");

    var responseAreaElement = Element.extend(responseArea);

    responseAreaElement.setStyle({display: 'none'});

    responseAreaElement.setStyle({visibility: 'hidden'});

    document.body.appendChild(responseArea);

    return responseAreaElement;
  },

  /*
   * Returns a HTML element, which acts as a placeholder
   * for response content.
   */
  getResponseArea: function() {
    if (!this.responseArea) {
      this.responseArea = this.createResponseArea();
    }

    return this.responseArea;
  }
});

/*
 * The stub request, to be used in offline mode (for debugging purposes only).
 */
Ticker.FeedPollerRequestStub = Class.define(Ticker.AbstractFeedPollerRequest,
{
  initialize: function(feedPoller) {
    this.initializeAbstract(feedPoller);
  },

  send: function() {
    //Widget.log("FeedPollerRequestStub", "Sending request: " + this.getURL() + (this.isInitial() ? "(initial)" : ""));

    setTimeout(this.result.bind(this), Math.random() * 3000);
  },

  result: function() {
    // Succeed or fail
    var canFailOnInitial = false;

    if ((canFailOnInitial || !this.isInitial()) && Math.random() < 0.25) {
      this.fail();
    } else {
      this.succeed();
    }
  },

  fail: function() {
    this.onFailure();

    //Widget.log("FeedPollerRequestStub", "Request failed");
  },

  succeed: function() {
    // Redirect or update items
    if (Math.random() < 0.15) {
      this.redirect();
    } else {
      this.update();
    }

    this.onSuccess();

    //Widget.log("FeedPollerRequestStub", "Request succeeded");
  },

  redirect: function() {
    //Widget.log("FeedPollerRequestStub", "Redirecting");

    Ticker.Response.setURL("http://dummy.url/" + Math.random());
  },

  update: function() {
    // Remove random number of items
    while (Math.random() < 0.25) {
      var item = this.anItem();

      if (item) {
        //Widget.log("FeedPollerRequestStub", "Removing item " + item.getId());

        Ticker.Response.removeItem(item);
      }
    }

    // Add random number of items, but if it's an initial
    // request, add at least one.
    var firstItem = true;

    while ((this.isInitial() && firstItem) || Math.random() < (this.isInitial() ? 0.90 : 0.25)) {
      var id = Math.random().toString().slice(0,4);

      //Widget.log("FeedPollerRequestStub", "Adding item " + id);

      this.feedPoller.getStorage().addItem(new Ticker.Item({
        id: id,
        channel: id[0],
        title: 'Item ' + id,
        icon: '<b>F1</b>',
        description: 'Blah blah blah about: ' + id + ' <a href="http://www.google.pl">Read more...</a>'}));

      firstItem = false;
    }

    // Set polling interval on initial request,
    // and on further requests change it from time to time
    if (this.isInitial() || (Math.random() < 0.5)) {
      var interval = Math.random() * 3 + 3;

      //Widget.log("FeedPollerRequestStub", "Setting polling interval " + interval);

      Ticker.Response.setPollingInterval(interval);
    }
  },

  anItem: function() {
    var items = this.feedPoller.getStorage().getItems();

    if (items.length > 0) {
      return items[items.length - 1];
    }
  }
});

/*
 * An interface used directly in the AJAX response scripts.
 */
Ticker.Response = {
  addItem: function(options) {
    //Widget.log("TickerResponse", "Adding item " + options.id);

    Ticker.getRequestingFeedPoller().getStorage().addItem(new Ticker.Item({
      id: options.id,
      channel: options.channel,
      title: $(options.title).innerHTML,
      icon: options.icon ? $(options.icon).innerHTML : null,
      description: options.description ? $(options.description).innerHTML : null,
      plainDescription: options.plainDescription ? $(options.plainDescription).innerHTML : null}));
  },

  removeItem: function(id) {
    //Widget.log("TickerResponse", "Removing item " + id);

    var storage = Ticker.getRequestingFeedPoller().getStorage();
    var item = storage.getItem(id);
    if (item) {
      storage.removeItem(item);
    }
  },

  setURL: function(url) {
    //Widget.log("TickerResponse", "Setting URL: " + url);

    Ticker.getRequestingFeedPoller().setURL(url);
  },

  setSkipTimes: function(skipTimes) {
    //Widget.log("TickerResponse", "Setting skip times: " + skipTimes);

    Ticker.getRequestingFeedPoller().setSkipTimes(skipTimes);
  },

  setPollingInterval: function(interval) {
    //Widget.log("TickerResponse", "Setting polling interval: " + interval);

    Ticker.getRequestingFeedPoller().setPollingInterval(interval);
  },

  followLink: function(itemId, href) {
    //Widget.log("TickerResponse", "Following a link (" + itemId + "): " + href);

    var item = Ticker.getItemStorage().getItem(itemId);

    if (item) {
      // Open link in a new window
      window.open(href);

      // Mark item as followed
      item.setFollowed(true);
    }
  }
};

Ticker.ItemTemplate = Widget.define(
{
  initialize: function(inlineContent, properties, options) {
    this.initializeWidget(null, options);

    this.inlineContent = inlineContent;

    this.properties = properties;
  },

  /**
   * Creates a DOM element with item content based on this template.
   */
  createElement: function(item) {
    // First, create a copy if the tmeplate element
    var itemElement = this.inlineContent.clone().getElement();

    // Second, inject properties, removing element IDS.
    this.injectProperties(item, itemElement);

    // Return the item element
    return itemElement;
  },

  injectProperties: function(item, element) {
    var id = element.id;

    if (id) {
      var property = this.properties[id];

      if (property) {
      	this.injectProperty(item, element, property);
      }

      element.id = null;
    }

    var childNodes = element.childNodes;

    for (var i = 0; i < childNodes.length; i++) {
      var node = childNodes[i];

      // Inject only for Element nodes.
      if (node.nodeType == 1) {
        this.injectProperties(item, node);
      }
    }
  },

  injectProperty: function(item, element, property) {
    var methodName;

    if (property == "title") {
      methodName = "getTitleElement";
    } else if (property == "icon") {
      methodName = "getIconElement";
    } else if (property == "plainDescription") {
      methodName = "getPlainDescriptionElement";
    } else {
      throw "Invalid property: " + property;
    }

    var propertyElement = item[methodName]();

    if (propertyElement) {
      propertyElement = propertyElement.cloneNode(true);

      element.appendChild(propertyElement);
    }
  }
});

/*
 * WidgetController is an abstract class implementing communication
 * between ItemStorage and Widget.
 *
 * Subclasses should implement one method: updateWidget()
 */
Ticker.WidgetController = Class.define(Widget.OptionsContainer, Widget.CallbackMixin,
{
  widget: null,
  itemStorage: null,
  channel: null,
  itemDisplayId: null,
  mostRecentFirst: true,
  itemTemplate: null,

  initializeWidgetController: function(options) {
    this.installOptions(options);

    // Parse the itemDisplayID, as comma separated string.
    if (this.itemDisplayId) {
      this.itemDisplayIds = this.itemDisplayId.split(",");
    }

    // Register this widget, so it can be referenced from onclick event.
    // TODO: Currently ID is generated randomly, which should be enough for now.
    this.widgetID = Math.random().toString();
    Widget.register(this.widgetID, this);

    // Make sure that TickerTape is not refreshed too often,
    // if subsequent items are added/removed from the storage
    // in short time interval.
    this.delayedCallbackHandler = new Widget.DelayCallbackHandler(500);
    this.delayedCallbackHandler.add(this.storageCallback.bind(this));
    this.itemStorage.addCallback(this.delayedCallbackHandler.invoke.bind(this.delayedCallbackHandler));

    this.callbackHandler = new Widget.CallbackHandler();

    this.update();
  },

  /*
   * Returns the widget to update
   */
  getWidget: function() {
    return this.widget;
  },

  /*
   * Invoked on storage change
   */
  storageCallback: function(operation, item) {
    this.update();
  },

  /*
   * Updates widget content with current ItemStorage content.
   */
  update: function() {
    if (!this.itemStorage.isUpdated()) {
      return;
    }

    var itemElements = [];

    var items = this.itemStorage.getItems(this.channel);

    for (var i = 0; i < items.length; i++) {
      itemElements.push(this.getItemElement(items[i]));
    }

    // By default, items are ordered from the most recent ones.
    // If the 'mostRecentFirst' property is false, then
    // reverse the order of items, before putting them to the widget.
    if (!this.mostRecentFirst) {
      itemElements.reverse();
    }

    this.updateWidget(itemElements);
  },

  /*
   * Updates the widget with pre-processed items' content.
   */
  updateWidget: function(itemContents, itemElements) {
    // Abstract method, implement in subclass
  },

  getItemIconElement: function(item) {
    var html = item.getIcon();

    if (html) {
      var element = document.createElement("span");

      element.innerHTML = html;

      return element;
    }
  },

  getItemTitleElement: function(item) {
    var html = item.getTitle();

    var element = document.createElement("span");

    element.innerHTML = html;

    return element;
  },

  getDefaultItemElement: function(item) {
    var iconElement = this.getItemIconElement(item);
    var titleElement = this.getItemTitleElement(item);

    if (!iconElement) {
      return titleElement;
    } else {
      var iconElementWithMargin = document.createElement("span");
      iconElementWithMargin.style.marginRight = "0.5em";
      iconElementWithMargin.appendChild(iconElement);

      var element = document.createElement("span");
      element.appendChild(iconElementWithMargin);
      element.appendChild(titleElement);

      return element;
    }
  },

  getItemElement: function(item) {
    var element;

    if (!this.itemTemplate) {
      element = this.getDefaultItemElement(item);
    } else {
      element = this.itemTemplate.createElement(item);
    }

    element = $(element);

    Widget.addElementObserver(element, Widget.CLICK, this.itemClicked.bind(this, item.getId()).bindAsEventListener(this));
    Widget.makeFocusable(element);

    return element;
  },

  /*
   * Invoked when user clicks on the item.
   */
  itemClicked: function(id, event) {
    // alert("Item Clicked: " + id + ":" + event)
    var item = this.itemStorage.getItemIncludingZombies(id);

    if (item) {
      //alert("Item clicked: " + item.getTitle())

      this.callbackHandler.invoke('click', item);

      // Display the item in the ItemDisplay widget, if provided.
      if (this.itemDisplayIds) {
        for (var i = 0; i < this.itemDisplayIds.length; i++) {
          var itemDisplayId = this.itemDisplayIds[i];

          var widget = Widget.getInstance(itemDisplayId);

          if (widget) {
            widget.setItem(item);

            if (item) {
              if (widget.show) {
                widget.show();
              }
            } else {
              if (widget.hide) {
                widget.hide();
              }
            }

            // Mark an item as read
            item.setRead(true);
          }
        }
      }
    }
  }
});

/*
 * TickerTapeController refreshes the TickerTape widget
 * with the current ItemStorage content.
 */
Ticker.TickerTapeController = Class.define(Ticker.WidgetController,
{
  separatorId: null,
  firstWidgetUpdate: true,

  /*
   * Initialization.
   */
  initialize: function(options) {
    //Widget.log("TickerTapeController", "Initializing...");

    // It's possible to pass a Carousel widget on 'tickerTape' option.
    // so, pass it to the 'widget' option in the super-class constructor.
    options.widget = options.widget ? options.widget : options.tickerTape;

    // Invoke super-class constructor.
    this.initializeWidgetController(options);
  },

  /*
   * Updates widget content with items' content.
   */
  updateWidget: function(itemElements) {
    var element = document.createElement("span");

    for (var i = 0; i < itemElements.length; i++) {
      var itemElement = itemElements[i];

      // Separator
      if (i > 0) {
        element.appendChild(this.getSeparatorElement());
      }

      element.appendChild(itemElement);
    }

    if (this.firstWidgetUpdate) {
      //Widget.log("TickerTapeController", "Setting content immediately");
      this.getWidget().setContentElementNow(element);
    } else {
      //Widget.log("TickerTapeController", "Setting content");

      this.getWidget().setContentElement(element);
    }

    if (itemElements.length != 0) {
      this.firstWidgetUpdate = false;
    }
  },

  getSeparator: function() {
    var separator = " ";

    if (this.separatorId) {
      separator = $(this.separatorId).innerHTML;
    }

    return separator;
  },

  getSeparatorElement: function() {
    var html = this.getSeparator();

    var element = document.createElement("span");
    element.innerHTML = html;
    return element;
  }
});

/*
 * CarouselController refreshes the Carousel widget
 * with the current ItemStorage content.
 */
Ticker.CarouselController = Class.define(Ticker.WidgetController,
{
  firstWidgetUpdate: true,

  /*
   * Initialization.
   */
  initialize: function(options) {
    //Widget.log("CarouselController", "Initializing...");

    // It's possible to pass a Carousel widget on 'carousel' option.
    // so, pass it to the 'widget' option in the super-class constructor.
    options.widget = options.widget ? options.widget : options.carousel;

    // Invoke super-class constructor.
    this.initializeWidgetController(options);
  },

  /*
   * Updates widget content with items' content.
   */
  updateWidget: function(itemElements) {
    if (this.firstWidgetUpdate) {
      //Widget.log("CarouselController", "Setting content immediately");

      this.getWidget().setContentElementsNow(itemElements);
    } else {
      //Widget.log("CarouselController", "Setting content");

      this.getWidget().setContentElements(itemElements);
    }

    if (itemElements.length != 0) {
      this.firstWidgetUpdate = false;
    }
  }
});

/**
 * Displays current status of the FeedPoller.
 */
Ticker.UpdateStatus = Class.define(Widget.OptionsContainer,
{
  feedPoller: null,
  normalId: null,
  busyId: null,
  failedId: null,
  suspendedId: null,
  displayStyle: 'inline',

  /**
   * Initialization
   */
  initialize: function(options) {
    this.installOptions(options);
    this.normal = $(this.normalId);
    this.busy = $(this.busyId);
    this.failed = $(this.failedId);
    this.suspended = $(this.suspendedId);

    this.normal.style.display = 'none';
    this.busy.style.display = 'none';
    this.failed.style.display = 'none';
    this.suspended.style.display = 'none';

    this.feedPoller.addCallback(this.update.bind(this));

    this.update();
  },

  /*
   * Updates widget with current FeedPoller status.
   */
  update: function() {
    this.normal.style.display = 'none';
    this.busy.style.display = 'none';
    this.failed.style.display = 'none';
    this.suspended.style.display = 'none';

    this.getStatusElement(this.feedPoller.getStatus()).style.display = this.displayStyle;
  },

  getStatusElement: function(status) {
    return this[status];
  }
});

/*
 * Displays current items count statistics.
 */
Ticker.ItemsCount = Class.define(Widget.OptionsContainer,
{
  storageStats: null,
  id: null,
  channel: null,
  read: null,
  followed: null,

  /*
   * Initialization.
   */
  initialize: function(options) {
    this.installOptions(options);
    this.element = $(this.id);

    this.storageStats.addCallback(this.update.bind(this));

    this.update();
  },

  /*
   * Updates the widget with current value.
   */
  update: function() {
    this.element.innerHTML = this.storageStats.countItems(this.channel, this.read, this.followed).toString();
  }
});

/*
 * Displays current channels count statistics.
 */
Ticker.ChannelsCount = Class.define(Widget.OptionsContainer,
{
  storageStats: null,
  id: null,
  read: null,
  followed: null,

  /*
   * Initialization.
   */
  initialize: function(options) {
    this.installOptions(options);
    this.element = $(this.id);

    this.storageStats.addCallback(this.update.bind(this));

    this.update();
  },

  /*
   * Updates the widget with current value.
   */
  update: function() {
    this.element.innerHTML = this.storageStats.countChannels(this.read, this.followed).toString();
  }
});

/*
 * Displays one of the item's property
 */
Ticker.ItemProperty = Class.define(Widget.OptionsContainer,
{
  id: null,
  propertyName: null,
  item: null,

  /*
   * @param id id of property displayer
   * @param propertyType name of property ('id', 'channel', 'title', 'icon' or 'description')
   * @param item reperesented item
   */
  initialize: function(options) {
    this.installOptions(options);

    this.element = $(this.id);

    this.functionName = 'get' + this.propertyName.slice(0, 1).toUpperCase() + this.propertyName.slice(1);

    this.update();
  },

  /*
   * Sets item to display, or null to clear
   */
  setItem: function(item) {
    this.item = item;
    this.update();
  },

  /*
   * @param item new item to be represented. If it's null then dispalyed value is erased.
   */
  update: function() {
    if (this.item) {
      this.element.innerHTML = this.item[this.functionName]();
    } else {
      this.element.innerHTML = '';
    }
  }
});

/*
 * Displays an item.
 */
Ticker.ItemDisplay = Widget.define(
{
  popupId: null,
  itemPropertyId: null,

  initialize: function(options) {
    this.initializeWidget(null, options);

    // Parse the itemDisplayID, as comma separated string.
    if (this.itemPropertyId) {
      this.itemPropertyIds = this.itemPropertyId.split(",");
    }
    
    this.addAction("dismiss");
  },

  /*
   * Sets item to display, or null to clear
   */
  setItem: function(item) {
    // Display the item in the ItemDisplay widget, if provided.
    if (this.itemPropertyIds) {
      for (var i = 0; i < this.itemPropertyIds.length; i++) {
        var itemPropertyId = this.itemPropertyIds[i];

        var widget = Widget.getInstance(itemPropertyId);

        if (widget) {
          widget.setItem(item);
        }
      }
    }
  },

  /*
   * Shows the item, if not already visible.
   */
  show: function() {
    var popup = this.getPopup();

    if (popup) {
      popup.show();
    }
  },

  /*
   * Hides the item, if not already hidden.
   *
   * @volantis-api-include-in PublicAPI
   */
  hide: function() {
    this.dismiss();
  },

  /*
   * Hides the item, if not already hidden.
   *
   * @volantis-api-include-in PublicAPI
   */
  dismiss: function(dismissType) {
    var popup = this.getPopup();

    if (popup) {
      popup.hide();
    }
  },

  /*
   * Returns popup instance used to display the item.
   */
  getPopup: function() {
    if (!this.popup) {
      if (this.popupId) {
        this.popup = Widget.getInstance(this.popupId);
      }
    }

    return this.popup;
  }
});
