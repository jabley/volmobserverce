/**
 * (c) Volantis Systems Ltd 2006. 
 */

/*
 * The log console
 */
Widget.Log = Class.define(Widget.OptionsContainer,
{
  id: null,
  topics: null,

  /**
   * Initialization
   */
  initialize: function(options) {
    this.installOptions(options)

    if (this.id != null) {
      // If the Log element is specified, use it.
      this.element = $(this.id)
    } else {
      // Otherwise create the log element automatically, with default styles.
      this.element = document.createElement('div')
      this.element.style.overflow = 'auto'
      this.element.style.position = 'fixed'
      this.element.style.height = '20%'
      this.element.style.width = '100%'
      this.element.style.bottom = '0%'
      this.element.style.left = '0%'
      this.element.style.borderTopWidth = '2px'
      this.element.style.borderTopStyle = 'solid'
      this.element.style.borderTopColor = 'black'
      this.element.style.backgroundColor = '#EEEEFF'
      document.body.appendChild(this.element)
    }

    // The z-index styling property should be high enough,
    // so that the debug window float over all other widgets.
    this.element.style.zIndex = 10

    // Clicking on the log window will clear its content.
    Widget.addElementObserver(this.element, Widget.CLICK, this.clear.bindAsEventListener(this))

    // Initialize strips colours.
    this.backgroundColors = ["#BBBBFF", "#DDDDFF"]
    this.backgroundColorIndex = 0

    // Initialize topics.
    this.defaultTopic = "Default"

    if (this.topics != null) {
      this.topics = this.topics.split(",")
    }

    Widget.currentLog = this
  },

  write: function() {
    if (arguments.length == 0) {
      // No-op

    } else if (arguments.length == 1) {
      // Write log message with default topic.
      this.writeContent(arguments[0])

    } else {
      // Write log message with topic and content.
      this.writeTopicAndContent(arguments[0], arguments[1])
    }
  },

  /**
   * Writes log message with topic and content specified.
   *
   * @param topic the topic of the message
   * @param content the content of the message
   */
  writeTopicAndContent: function(topic, content) {
    if (!this.includesTopic(topic)) {
      return
    }

    var scroll = (this.element.scrollTop == this.element.scrollHeight - this.element.clientHeight)

    messageElement = document.createElement("div")
    messageElement.style.backgroundColor = this.getBackgroundColor()
    messageElement.innerHTML = "<span style='font-weight:bold; font-style:italic'>" + topic + ": </span>" + content
    this.element.appendChild(messageElement)

    if (scroll) {
        this.element.scrollTop = this.element.scrollHeight - this.element.clientHeight
    }

    this.switchBackgroundColor()
  },

  /**
   * Writes log message with default topic.
   *
   * @param content the content of the message
   */
  writeContent: function(content) {
    this.writeTopicAndContent(this.defaultTopic, content)
  },

  /**
   * Clears log content
   */
  clear: function() {
    this.element.innerHTML = ""
  },

  /**
   * Switches the colour of the next stripe.
   */
  switchBackgroundColor: function() {
    this.backgroundColorIndex++

    if (this.backgroundColorIndex >= this.backgroundColors.length) {
      this.backgroundColorIndex = 0
    }
  },

  /**
   * Returns colour for the next stripe
   *
   * @returns the CSS colour of the next stripe
   */
  getBackgroundColor: function() {
    return this.backgroundColors[this.backgroundColorIndex]
  },

  /**
   * Returns true if the list of displayed topics includes
   * the specified one.
   *
   * @param topic the topic to check for inclusion
   */
  includesTopic: function(topic) {
    if (this.topics == null) {
      return true
    } else {
      for (var i = 0; i < this.topics.length; i++) {
        if (this.topics[i] == topic) {
          return true
        }
      }
    }

    return false
  }
})

/**
 * Writes a log message.
 * If called with one parameter, it uses the default topic.
 * If called with two parameters, the first one describes the topic,
 * and the second the content of the message.
 */
Widget.log = function() {
  if (Widget.currentLog != null) {
    Widget.currentLog.write.apply(Widget.currentLog, arguments)
  }
}

/**
 * Returns true if logging is enabled.
 */
Widget.isLogEnabled = function() {
  return true
}