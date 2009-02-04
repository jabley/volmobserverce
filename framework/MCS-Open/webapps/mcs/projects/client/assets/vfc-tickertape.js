/**
 * (c) Volantis Systems Ltd 2006.
 */

/* ----------------------------------------------
 * Mix-in defining common properties and methods
 * for objects, which are animable
 * ---------------------------------------------- */
Widget.Animable = Class.define(
{
    // Animation smoothness, in frames per second.
    framesPerSecond: 50
});

/* ----------------------------------------------
 * Mix-in defining common properties and methods
 * for objects, which are scrollable.
 * ---------------------------------------------- */
Widget.Scrollable = Class.define(Widget.Animable,
{
    // Scroll speed as number of characters per second.
    charsPerSecond: 6,

    // Scroll direction: "left" of "right"
    direction: "left",

    /*
     * Returns scroll speed in characters per second.
     */
    getCharsPerSecond: function() {
        return this.charsPerSecond;
    },

    /*
     * Returns width of single character in pixels.
     */
    getCharWidth: function() {
        // Width of a single character will be calculated by
        // temporarily creating an "M" HTML Text node,
        // and measuring its actual width in pixels.
        // Once calculated, the width is cached.

        // If width is not already cached, calculate it now.
        if (!Widget.charWidth) {
            // First, create an invisible span element containing 'M' contents.
            this.mElement = document.createElement("span");

            // Make an element invisible.
            this.mElement.style.visibility = "hidden";
            this.mElement.style.padding ="0px";
            this.mElement.style.border ="0px";
            this.mElement.style.margin ="0px";

            this.mElement.appendChild(document.createTextNode("M"));

            // Now, append it to the document body
            document.body.appendChild(this.mElement);

            // Now, measure its width in pixels.
            // TODO: This code may be browser dependent.
            Widget.charWidth = this.mElement.scrollWidth;

            // After it's been measured, remove it from document body
            // Because the removeChild() method does not work on NetFront,
            // make it invisible by setting 'display' to 'none' to make
            // sure that it's not appearing on the page.
            this.mElement.style.display = "none";
            document.body.removeChild(this.mElement);

            // In case it was not possible to measure the width,
            // assume it's 16 px.
            Widget.charWidth = Widget.charWidth ? Widget.charWidth : 16;

        }

        return Widget.charWidth;
    },

    /*
     * Returns scroll speed as pixels per second.
     */
    getPixelsPerSecond: function() {
        return this.getCharsPerSecond() * this.getCharWidth();
    },

    /*
     * Returns scroll direction (left or right)
     */
    getDirection: function() {
        return this.direction;
    }
});

/* -----------------------------------------------
 * Mix-in defining common options and methods for
 * objects, which are repeatable in any way.
 * ----------------------------------------------- */
Widget.Repeatable = Class.define(
{
    /*
     * Number of repetitions as integral number greater than zero,
     * or "infinite" string.
     */
    repetitions: "infinite",

    /*
     * Returns number of repetitions as integral number
     * greater than zero, or "infinite" string.
     */
    getRepetitions: function() {
        return this.repetitions;
    }
});

/* ------------------------------------------------------------
 * Object responsible for rendering animation frames.
 *
 * It uses delegation/callback mechanism to delegate actual
 * rendering to another object, which could be any widget.
 *
 * Actual rendering is performed in frames. Subsequent frames
 * are rendered in given time intervals, which can be configured
 * using framesPerSecond property.
 *
 * This renderer provides methods to control the animation.
 * It's possible to start and stop the whole animation,
 * and to pause and resume at any moment in time.
 * ---------------------------------------------------------------*/
Widget.Animator = Class.define(Widget.Scrollable, Widget.OptionsContainer,
{
    /*
     * Creates an Animation object which would imitate animation
     * by rendering frames in given time intervals.
     */
    initialize: function(options) {
        this.installOptions (options);

        this.millisElapsed = 0;
        this.running = false;
    },

    /*
     * Sets a callback function to perform actual rendering.
     * Function will be invoked with the number of milliseconds
     * elapsed since the animation start.
     */
    setRenderCallback: function(callback) {
        this.callback = callback;
    },

    /*
     * Returns true, if animation is currently playing
     * (in other words if it has been started).
     * Note, that this method will return true even, if
     * animation is currently paused.
     */
    isPlaying: function() {
        return this.playing;
    },

    /*
     * Returns true, if animation is currently paused.
     * Note, that it is possible to pause the animation,
     * which has not been started yet.
     */
    isPaused: function() {
        return this.paused;
    },

    /*
     * Starts the animation from the beginning,
     * if it hasn't been already started.
     * After that, the isPlaying() method will return true.
     */
    start: function() {
        if (!this.playing) {
            this.playing = true;
            this.millisElapsed = 0;

            this.updateTimer();
        }
    },

    /*
     * Stops the animation, if it hasn't been already stopped.
     * After that, the isPlaying() method will return false.
     */
    stop: function() {
        if (this.playing) {
            this.playing = false;

            this.updateTimer();
        }
    },

    /*
     * Pauses the animation, if it's not already paused.
     * After that, the method isPaused() will return true.
     */
    pause: function() {
        if (!this.paused) {
            this.paused = true;

            this.updateTimer();
        }
    },

    /*
     * Resumes the animation, if it's not already resumed.
     * After that, the method isPaused() will return false.
     */
    resume: function() {
        if (this.paused) {
            this.paused = false;

            this.updateTimer();
        }
    },

    /*
     * Renders current animation frame immediately.
     */
    render: function() {
        if (this.callback) {
          this.callback(this.millisElapsed);
        }
    },

    /*
     * Internal method updating the timer responsible
     * for rendering subsequent frames
     */
    updateTimer: function() {
        if (this.playing && !this.paused) {
            if (!this.timer) {
                this.lastMillis = (new Date()).getTime();

                this.timer = setInterval(this.nextFrame.bind(this), 1000 / this.framesPerSecond);
            }
        } else {
            if (this.timer) {
                clearInterval(this.timer);

                this.timer = null;
            }
        }
    },

    /* Internal method called every frame */
    nextFrame: function() {
        var currentMillis = (new Date()).getTime();
        this.millisElapsed += currentMillis - this.lastMillis;
        this.lastMillis = currentMillis;

        this.render();
    }
});

/* --------------------------------------------------------------
 * Refresher can be used by client to obtain dynamic contents
 * from the server in asynchronous way, without reloading
 * the page.
 *
 * Refresher uses delegation/callback mechanism to perform actual refresh,
 * so it can be used with any widget to refresh its contents.
 * --------------------------------------------------------------*/
Widget.Refresher = Class.define(Widget.OptionsContainer,
{
    // Attributes (readonly).
    // To set attributes use accesor methods.
    url: null,
    interval: 60,
    callback: null,
    enabled: true,

    // Initialisation.
    initialize: function(options) {
        // Install options
        this.installOptions(options);

        // And update internal state of this refresher.
        this.updateState();
    },

    // Sets URL containing the contents.
    setURL: function(url) {
        this.url = url;
        this.updateState();
    },

    // Sets time interval (in seconds) for automatic refreshing.
    setInterval: function(interval) {
        this.clearTimer();
        this.interval = interval;
        this.updateState();
    },

    // Enables/disables automatic refreshing.
    setEnabled: function(enabled) {
        this.enabled = enabled;
        this.updateState();
    },

    // Sets callback function, which will be invoked when
    // new contents is available. The new contents will be
    // passed as an argument.
    setCallback: function(callback) {
        this.callback = callback;
        this.updateState();
    },

    // Refreshes the contents immediately, as a consequence of timeout.
    refreshOnTimeout: function () {
        if (Widget.isPollingEnabled()) {
            this.refresh();
        }
    },

    // Refreshes the contents immediately.
    refresh: function () {
        if (this.url) {
            //printfire("Sending AJAX Request: " + this.url)

            new Widget.AjaxRequest(this.url,
                             {method: "get",
                              onSuccess: this.refreshRequest.bind(this)});
        }
    },

    // Internal method updating state of the refresher.
    // Invoked on change of any parameter.
    updateState: function() {
        if (this.enabled && this.interval) {
            if (!this.timer) {
                this.timer = setInterval(this.refreshOnTimeout.bind(this), this.interval * 1000);
            }
        } else {
            this.clearTimer();
        }
    },

    clearTimer: function() {
        if (this.timer) {
            clearInterval(this.timer);
            this.timer = null;
        }
    },

    // Internal method - response for AJAX request.
    refreshRequest: function(request) {
        //printfire("Received AJAX response: " + request.responseText)
        if (this.callback) {
            this.callback(request.responseText);
        }
    }
});

/* ----------------------------------------------------------------
 * BaseTape is a JavaScript class used to wrap <div> HTML element
 * into JavaScript object.
 *
 * BaseTape provides methods to manipulate the contents and the
 * position of the tape contents.
 *
 * BaseTape is mainly used as a component of AbstractTape, and
 * all its subclasses.
 * ----------------------------------------------------------------*/
Widget.BaseTape = Class.define(Widget.OptionsContainer,
{
    /* Attributes - all private */
    outerElement: null,
    innerElement: null,
    offset: null,

    scrollTimer: null,
    scrollFPS: null,
    scrollPPF: null,
    scrollDirection: null,
    scrollStyle: null,

    minScrollOffset: null,
    maxScrollOffset: null,

    hasFocus: false,
    mouseOver: false,

    /*
     * Creates a tape widget from <div> HTML element of given ID.
     * Initial contents of the tape will be the same, as contents of
     * the <div> element.
     *
     * Following HTML element:
     *
     * <div id="my-tape">
     *     This is my tape
     * </div>
     *
     * will be transformed into:
     *
     * <div id="my-tape">
     *     <div style='overflow:hidden; text-align:left'>
     *         <div style='position:relative'>
     *             <span style='white-space:nowrap'>
     *                 This is my tape
     *             </span>
     *         </div>
     *     </div>
     * </div>
     *
     * 1. The outer-most div element remains unchanged, only the overflow style
     *    is set to 'hidden' to crop ticker tape content.
     * 2. First-level <div> element is stored under this.outerElement attribute,
     *    and is used to set global style properties for the whole tape.
     * 3. Second-level <div> element is stored under this.middleElement attribute,
     *    and is used to position the contents of the tape.
     * 4. Third-level <span> element is stored under this.innerElement attribute,
     *    and is used to store contents of the tape and its other properties.
     *    It's also used to retrieve the width of the contents in pixels.
     * 5. Fourth-level element is the actual contents of the tape.
     */
    initialize: function(id, options) {
        this.element = $(id);
        this.initialContents = this.element.innerHTML;

        this.element.style.overflow = "hidden";
        if (Prototype.nokiaOSSBrowser()) {
            this.element.innerHTML = "";
            this.widgetWidth = this.element.getStyle('width');
        }

        // setting position, left and top property is workaround for IE
        //- for static position overflow not working so relative position is set
        this.element.innerHTML = "<div style='overflow:hidden; text-align:left; width:100%; height:100%; position: relative; left: 0px; top: 0px;'></div>";

        this.outerElement = this.element.firstChild;

        this.outerElement.innerHTML = "<div style='position:relative; left:0'></div>";

        this.middleElement = this.outerElement.firstChild;

        this.middleElement.innerHTML = "<span style='white-space:nowrap'></span>";

        this.innerElement = this.middleElement.firstChild;

        this.innerElement.innerHTML = this.initialContents;

        if (Prototype.nokiaOSSBrowser()) {
            this.element.style.width = this.widgetWidth;
        }

        this.offset = 0;

        // Set fixed height
        this.outerElement.style.minHeight = this.outerElement.scrollHeight+"px";
    },

    /*
     * Returns the width of the whole tape in pixels.
     */
    getWidth: function() {
        return this.outerElement.clientWidth;
    },

    /*
     * Returns the height of the whole tape in pixels.
     */
    getHeight: function() {
        return this.outerElement.clientHeight;
    },

    /*
     * Returns the width of the tape contents in pixels.
     */
    getContentsWidth: function() {
        return this.innerElement.scrollWidth;
    },

    /*
     * Returns the width of the tape contents in pixels.
     */
    getContentsHeight: function() {
        return this.middleElement.scrollHeight;
    },

    /*
     * Returns the contents of the tape as HTML string.
     */
    getContents: function(html) {
        return this.innerElement.innerHTML;
    },

    /*
     * Sets the contents of the tape as HTML string.
     */
    setContent: function(html) {
        this.innerElement.innerHTML = html;
    },

    /*
     * Sets the contents of the tape as DOM element.
     */
    setContentElement: function(element) {
        // Remove current content
        while (this.innerElement.firstChild !== null) {
            this.innerElement.removeChild(this.innerElement.firstChild);
        }

        // Set the new content
        this.innerElement.appendChild(element);
    },

    /*
     * Returns the position of the tape contents
     * relative to the left border of the tape.
     */
    getOffset: function() {
        return this.offset;
    },

    /*
     * Sets the position of the tape contents
     * relative to the left border of the tape.
     */
    setOffset: function(offset) {
        //this.setContent("Ticker: " + this.getWidth() + "x" + this.getHeight() +
        //                 ", Contents: " + this.getContentsWidth() + "x" + this.getContentsHeight())

        // Set offset of the tape contents relative to
        // the left border of the tape
        this.middleElement.style.left = offset + "px";
        this.middleElement.style.top = ((this.getHeight() - this.getContentsHeight()) / 2) + "px";

        // Reset invisible scroll-bar to left position,
        // in case it was changed by user interaction
        this.outerElement.scrollLeft = 0;
        this.outerElement.scrollTop = 0;

        this.offset = offset;
    }
});

/* -------------------------------------------------
 * A superclass of kind of Ticker Tapes.
 *
 * Provides tape animation and automatic
 * contents refreshing.
 * -------------------------------------------------*/
Widget.AbstractTape = Class.define(Widget.OptionsContainer,Widget.Focusable,
{
    /*
     * AbstractTape initialisation.
     *
     * Important! When overwriting this method in subclass,
     * always call this.initializeAbstractTape(id, options).
     * This is JavaScript equivalent for super.initialize(is, options)
     */
    initialize: function(id, options) {
        this.initializeAbstractTape(id, options);
    },

    /*
     * Actual AbstractTape initialization.
     */
    initializeAbstractTape: function(id, options) {
        this.installOptions(options);

        // Create BasicTape out of given DOM element.
        this.tape = new Widget.BaseTape(id, options);

        // Install event handlers on BasicTape DOM element
        this.tapeElement = $(id);

        this.installObservers(this.tapeElement);

        // Animator - for tape animation
        this.animator = new Widget.Animator(this.getOption("scroll"));
        this.animator.setRenderCallback(this.doRender.bind(this));
        this.animator.start();

        // Refresher - for automatic contents refreshing
        this.refresher = new Widget.Refresher(this.getOption("refresh"));
        this.refresher.setCallback(this.setContent.bind(this));
    },

    installObservers: function(tapeElement){
        this.setFocus(tapeElement,this.handleFocus.bindAsEventListener(this),
          this.handleBlur.bindAsEventListener(this));
    },

    removeObservers: function(tapeElement){
        this.unsetFocus(tapeElement,this.handleFocus.bindAsEventListener(this),
          this.handleBlur.bindAsEventListener(this));
    },

    /*
     * Sets tape contents as inline HTML string.
     */
    setContent: function(html) {
        this.setAbstractTapeContents(html);
    },

    /*
     * Sets tape contents, as inline DOM element
     */
    setContentElement: function(element) {
        this.setAbstractTapeContentElement(element);
    },

    /*
     * Sets tape contents now. By default it call setContent()
     */
    setContentNow: function(html) {
        this.setContent(html);
    },

    /*
     * Sets tape contents now. By default it call setContentElement()
     */
    setContentElementNow: function(element) {
        this.setContentElement(element);
    },

    /*
     * Sets tape contents
     */
    setAbstractTapeContents: function(html) {
        this.removeObservers(this.tapeElement);
        this.tape.setContent(html);
        this.installObservers(this.tapeElement);
    },

    /*
     * Sets tape contents
     */
    setAbstractTapeContentElement: function(element) {
        this.removeObservers(this.tapeElement);
        this.tape.setContentElement(element);
        this.installObservers(this.tapeElement);
    },

    /*
     * Refreshes tape contents (AJAX request)
     */
    refreshContent: function() {
        this.refresher.refresh();
    },

    /*
     * Abstract method rendering current tape contents.
     * Overwrite this method for custom animation.
     */
    doRender: function(millis){
        if(this.tapeElement.isVisible()){
          this.render(millis);
        }
    },

    render: function(milis){},

    /* Event handling - internal use only */
    handleMouseOver: function() {
        this.mouseOver = true;

        this.animator.pause();
    },

    handleMouseOut: function() {
        this.mouseOver = false;

        if (!this.hasFocus) {
            this.animator.resume();
        }
    },

    handleFocus: function() {
        this.hasFocus = true;

        this.animator.pause();
    },

    handleBlur: function() {
        this.hasFocus = false;

        if (!this.mouseOver) {
            this.animator.resume();
        }
    }
});

/* -------------------------------------------------
 * Sliding Tape.
 *
 * The contents of the tape slides in from left or right
 * side and stops.
 *
 * When updating tape contents, following effect occurs:
 *  - the old contents disappears instantly
 *  - the new contents slides in and stops
 * -------------------------------------------------*/
Widget.SlidingTape = Class.define(Widget.AbstractTape,
{
    /*
     * Renders tape contents for given amount of millseconds
     * elapsed since animation start.
     */
    render: function(millis) {
        var shift = (millis / 1000) * this.animator.getPixelsPerSecond();

        if (shift >= this.tape.getWidth()) {
            shift = this.tape.getWidth();
            this.animator.stop();
        }

        if (this.animator.direction == "left") {
            this.tape.setOffset(this.tape.getWidth() - shift);
        } else {
            this.tape.setOffset(shift - this.tape.getContentsWidth());
        }
    },

    /*
     * Updates contents of this tape.
     */
    setContent: function(html) {
        this.animator.stop();
        this.setAbstractTapeContents(html);
        this.animator.start();
    },

    /*
     * Updates contents of this tape.
     */
    setContentElement: function(element) {
        this.animator.stop();
        this.setAbstractTapeContentElement(element);
        this.animator.start();
    }
});

/* -------------------------------------------------
 * Alternating Tape.
 *
 * Initially, contents of the tape is centered.
 * Then, it slides in left or right direction.
 * If the contents reaches tape border, it starts to
 * slide in the opposite direction.
 * The effect is repeated given number of times.
 * After that the tape stops in its initial position.
 *
 * When updating tape contents, following effect occurs:
 *  - the old contents disappears instantly
 *  - the new contents appears instantly in the centre of the tape
 *  - the contents starts to scroll
 * -------------------------------------------------*/
Widget.AlternatingTape = Class.define(Widget.AbstractTape, Widget.Repeatable,
{
    /*
     * Renders tape contents for given amount of millseconds
     * elapsed since animation start.
     */
    render: function(millis) {
        var shift = (millis / 1000) * this.animator.getPixelsPerSecond();

        d = (this.tape.getWidth() - this.tape.getContentsWidth()) / 2;
        d1 = Math.abs(d);
        d2 = d1 * 2;
        d3 = d1 * 3;
        d4 = d1 * 4;

        // Check number of repetitions
        if (this.repetitions && (this.repetitions != "infinite") && (shift >= d4 * this.repetitions)) {
            s = 0;
            this.animator.stop();
        } else {
            s = shift % d4;

            if (s < d1) {
              s = -s;
            } else if (s < d3) {
              s = s - d2;
            } else {
              s = d4 - s;
            }

            if (this.animator.direction == "right") {
              s = -s;
            }
        }

        this.tape.setOffset(d + s);
    },

    /*
     * Updates contents of this tape.
     */
    setContent: function(html) {
        this.animator.stop();
        this.setAbstractTapeContents(html);
        this.animator.start();
    },

    /*
     * Updates contents of this tape.
     */
    setContentElement: function(element) {
        this.animator.stop();
        this.setAbstractTapeContentElement(element);
        this.animator.start();
    }
});

/* -------------------------------------------------
 * Scrolling Tape.
 *
 * The contents of the tape slides in from one side
 * and then slides out of the tape at the opposite side.
 * After that, the operation is repeated.
 *
 * When updating tape contents, following effect occurs:
 *  - the old contents slides out of the tape completely
 *  - the new contents slides in and continues to scroll
 * -------------------------------------------------*/
Widget.ScrollingTape = Class.define(Widget.AbstractTape, Widget.Repeatable,
{
    /* Private attributes */
    newContents: null,
    newOffset: null,
    offset: 0,
    shift: 0,

    /*
     * Renders tape contents for given amount of millseconds
     * elapsed since animation start.
     */
    render: function(millis) {
        this.offset = (millis / 1000) * this.animator.getPixelsPerSecond() - this.shift;

        cycleWidth = this.tape.getWidth() + this.tape.getContentsWidth();

        // Stop the tape after given number of repetitions
        if (this.repetitions && (this.repetitions != "infinite") && (this.offset >= cycleWidth * this.repetitions)) {
            this.animator.stop();

            return;
        }

        // Check, if tape content needs to be updated with new content.
        if (this.newOffset && this.offset >= this.newOffset) {
            // This line temporarily makes the contents of the tape
            // invisible, to avoid quirks
            this.tape.setOffset(this.tape.getWidth());

            // One of newContent or newContentElement has been set
            // Choose the correct one and set new contents.
            if (this.newContents != null) {
              this.setAbstractTapeContents(this.newContents);
            }else if (this.newContentElement != null) {
              this.setAbstractTapeContentElement(this.newContentElement);
            }
            // Update offset and shift
            this.shift += this.newOffset;
            this.offset -= this.newOffset;

            this.newContents = null;
            this.newContentElement = null;
            this.newOffset = null;
        }

        var tapeOffset = this.offset % (this.tape.getWidth() + this.tape.getContentsWidth());

        if (this.animator.direction == "left") {
            this.tape.setOffset(this.tape.getWidth() - tapeOffset);
        } else {
            this.tape.setOffset(-this.tape.getContentsWidth() + tapeOffset);
        }
    },

    /*
     * Updates contents of this tape.
     */
    setContent: function(html) {
        if (this.animator.isPlaying()) {
            // If the tape is currently scrolling, defer content update
            // after current content slides out from the screen
            this.newContents = html;

            var cycleLength = (this.tape.getWidth() + this.tape.getContentsWidth());

            this.newOffset = Math.ceil(this.offset / cycleLength) * cycleLength;
        } else {
            // If the tape is not currently scrolling,
            // simply update tape content and re-start the
            // scroll.
            this.setAbstractTapeContents(html);

            this.shift = 0;

            this.animator.start();
        }
    },

    /*
     * Updates contents of this tape.
     */
    setContentElement: function(element) {
      if (this.animator.isPlaying()) {
            Widget.log("TickerTape"," playing adding content element");
            // If the tape is currently scrolling, defer content update
            // after current content slides out from the screen
            this.newContentElement = element;
            var cycleLength = (this.tape.getWidth() + this.tape.getContentsWidth());
            this.newOffset = Math.ceil(this.offset / cycleLength) * cycleLength;
        } else {
            // If the tape is not currently scrolling,
            // simply update tape content and re-start the
            // scroll.
            Widget.log("TickerTape"," not playing adding content element");
            this.setAbstractTapeContentElement(element);

            this.shift = 0;

            this.animator.start();
        }
    },

    /*
     * Updates contents of this tape.
     */
    setContentNow: function(html) {
        this.animator.stop();

        this.shift = 0;

        this.render(0);

        this.setAbstractTapeContents(html);

        this.animator.start();
    },

    /*
     * Updates contents of this tape.
     */
    setContentElementNow: function(element) {
        this.animator.stop();

        this.shift = 0;

        this.render(0);

        this.setAbstractTapeContentElement(element);

        this.animator.start();
    }

});

/*
 * Ticker Tape.
 *
 * Depending on tape style, it wraps one of following tapes:
 *
 *  - "scroll"    -> Widget.ScrollingTape
 *  - "alternate" -> Widget.AlternatingTape
 *  - "slide"     -> Widget.SlidingTape
 */
Widget.TickerTape = Class.define(Widget.OptionsContainer,
{
    // Tape style: "scroll", "alternate", "slide", "none"
    style: "scroll",

    // Contstructor
    initialize: function(id, options) {
        this.installOptions(options);

        if (this.style == "slide") {
            this.tape = new Widget.SlidingTape(id, options);
        } else if (this.style == "alternate") {
            this.tape = new Widget.AlternatingTape(id, options);
        } else if (this.style == "scroll") {
            this.tape = new Widget.ScrollingTape(id, options);
        } else if (this.style == "none") {
            this.tape = new Widget.BaseTape(id, options);
        } else { // default is scroll
            this.tape = new Widget.ScrollingTape(id, options);
        }
    },

    /*
     * Refreshes tape contents with current contents.
     *
     * Current contents will be asynchronously downloaded
     * from provided URL.
     */
    refreshContent: function() {
        this.tape.refreshContent();
    },

    /*
     * Refreshes tape contents with current contents.
     *
     * Current contents will be asynchronously downloaded
     * from provided URL.
     *
     * @volantis-api-include-in PublicAPI
     */
    forceUpdate: function() {
        this.refreshContent();
    },

    /**
     * Replaces the contents of the Ticker Tape with the specified HTML text.
     * Note, that if 'mcs-marquee-effect' style is 'scroll',
     * the contents will be replaced after the current one scrolls out of the page.
     *
     * @volantis-api-include-in PublicAPI
     */
    setContent: function(htmlText) {
        this.tape.setContent(htmlText);
    },

    /**
     * Replaces the contents of the Ticker Tape with the specified HTML text immediately.
     */
    setContentNow: function(htmlText) {
        this.tape.setContentNow(htmlText);
    },

    /**
     * Replaces the contents of the Ticker Tape with the specified HTML text.
     * Note, that if 'mcs-marquee-effect' style is 'scroll',
     * the contents will be replaced after the current one scrolls out of the page.
     *
     * @volantis-api-include-in PublicAPI
     */
    setContentElement: function(element) {
        this.tape.setContentElement(element);
    },

    /**
     * Replaces the contents of the Ticker Tape with the specified HTML text immediately.
     */
    setContentElementNow: function(element) {
        this.tape.setContentElementNow(element);
    }
});
