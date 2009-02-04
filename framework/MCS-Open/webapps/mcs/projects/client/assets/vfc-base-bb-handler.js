/**
 * (c) Volantis Systems Ltd 2008. 
 */


/**
 * A handler widget, connects event with action.
 */
Widget.Handler = Widget.define(
{
  initialize: function(event, action, options) {
    this.initializeWidget(null, options)

    this.enabled = true
    this.event = event
    this.action = action

    if (this.getOption("isEnabled") != null) {
      this.setEnabled(this.getOption("isEnabled"))
    }

    this.observe(event, "sent", "eventSent")

    this.addProperty("is-enabled")
  },

  isEnabled: function() {
    return this.enabled
  },

  setEnabled: function(enabled) {
    if (this.enabled != enabled) {
      this.enabled = enabled

      this.notifyObservers("isEnabledChanged")
    }
  },

  eventSent: function() {
    if (this.enabled) {
      this.action.invoke.apply(this.action, arguments)
    }
  }
})