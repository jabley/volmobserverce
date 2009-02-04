
/**
 * (c) Volantis Systems Ltd 2008. 
 */

/**
 * A widget displaying a string value.
 * It's also possible to display rich content, as a fragment of HTML.
 */
Widget.Internal.Display = Class.define(Widget.OptionsContainer, Widget.Observable,
{
  content: "",

  initialize: function(id, options) {
    this.installOptions(options)

    this.id = id

    this.updateHTML()
  },

  getElement: function() {
    return $(this.id)
  },

  setContent: function(content) {
    if (this.content != content) {
      this.content = content

      this.updateHTML()

      this.notifyObservers("contentChanged")
      this.notifyObservers("canClearContentChanged")
    }
  },

  getContent: function() {
    return this.content
  },

  clearContent: function() {
    this.setContent("")
  },

  canClearContent: function() {
    return this.content != ""
  },

  updateHTML: function() {
    this.getElement().innerHTML = this.content.escapeHTML()
  }
})

/**
 * A property display widget.
 *
 * It can display any string value.
 * Additionally, it may be associated with a widget property
 */
Widget.Display = Widget.define(
{
  property: null,

  initialize: function(id, options) {
    this.initializeWidget(id, options)

    // Create a Display widget.
    this.display = new Widget.Internal.Display(id, options)

    this.observe(this.display, "contentChanged", "contentChanged")
    this.observe(this.display, "canClearContentChanged", "canClearContentChanged")

    if (this.property != null) {
      this.observe(this.property, "valueChanged", "updateWithProperty")

      this.updateWithProperty()
    }

    this.addAction("clear-content")
    this.addProperty("content")
  },

  /**
   * Sets displayed content.
   */
  setContent: function(string) {
    if (this.property == null) {
      this.display.setContent(string)
    }
  },

  getContent: function() {
    return this.display.getContent()
  },

  clearContent: function() {
    this.display.clearContent()
  },

  canClearContent: function() {
    return this.display.canClearContent()
  },

  canClearContentChanged: function() {
    this.notifyObservers("canClearContentChanged")
  },

  contentChanged: function() {
    this.notifyObservers("contentChanged")
  },

  updateWithProperty: function() {
    this.display.setContent(this.property.getValue())
  }
})
