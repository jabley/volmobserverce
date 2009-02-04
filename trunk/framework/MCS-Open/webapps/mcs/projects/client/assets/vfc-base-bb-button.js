

/**
 * (c) Volantis Systems Ltd 2008. 
 */


/**
 * A button widget, for internal use only.
 *
 * The public button is represented by Widget.ActionButton.
 */
Widget.Internal.Button = Class.define(Widget.OptionsContainer, Widget.Observable,
{
  enabled: true,
  disabledStyle: {},

  /**
   * Initialization.
   */
  initialize: function(id, options) {
    this.installOptions(options)

    this.id = id

    var element = this.getElement()

    // TODO: Maybe listen to DOMActivate event?
    // DOMActivate do not work on Opera browser
    if (this.isNative() && (Prototype.nokiaOSSBrowser() || Prototype.firefoxBrowser())) {
      // Native HTML buttons sends "DOMActivate" event on press.
      Widget.addElementObserver(element, "DOMActivate", this.press.bindAsEventListener(this));
    } else {
      // Emulated buttons needs to be handled differently.
    Widget.addElementObserver(element, Widget.CLICK, this.press.bindAsEventListener(this));
    Widget.addElementObserver(element, Widget.KEYPRESS, this.keyPressed.bindAsEventListener(this));
    Widget.makeFocusable(element);
    }

    this.update()
  },

  /**
   * Returns an element this button is based on.
   */
  getElement: function() {
    return $(this.id)
  },

  /**
   * Presses the button programatically.
   */
  press: function() {
    if (this.enabled) {
      this.notifyObservers('pressed')
    }
  },

  /**
   * Equivalent to press (backward compability method).
   */
  click: function() {
    this.press()
  },

  /**
   * Handles key press
   */
  keyPressed: function(ev) {
    // Enter activates the button (but only the non-native one)
    if (!this.isNative()) {
      if (ev.keyCode == 13) {
        this.press()
      }
    }
  },

  /**
   * Enables or disables the button.
   *
   * @param enabled The enabled flag to set.
   */
  setEnabled: function(enabled) {
    this.enabled = enabled

    this.update()

    this.notifyObservers('isEnabledChanged')
  },

  /**
   * Returns true if this button is enabled, false otherwise.
   */
  isEnabled: function() {
    return this.enabled
  },

  /**
   * Updates button state
   */
  update: function() {
    if (this.enabled) {
      if (this.isNative()) {
        this.getElement().disabled = false
      }

      this.getElement().vfcRevertStyle()
    } else {
      if (this.isNative()) {
        this.getElement().disabled = true
      }

      this.getElement().vfcReplaceStyle(this.disabledStyle)
    }
  },

  isNative: function() {
    return this.getElement().tagName.toUpperCase() == "BUTTON"
  }
})

/**
 * A button widget.
 */
Widget.Button = Widget.define(
{
  action: null,

  /**
   * Intialization
   */
  initialize: function(id, options) {
    // Initalize superclass
    this.initializeWidget(id, options)

    // Create internal button
    this.button = new Widget.Internal.Button(id, options)

    // Handle internal button events
    this.observe(this.button, "pressed", "internalButtonPressed")

    this.observe(this.button, "isEnabledChanged", "internalButtonEnabledChanged")

    // If action is specified, synchronize this button with action.
    if (this.action != null) {
      this.observe(this.action, "isEnabledChanged", "updateButtonWithAction")

      this.updateButtonWithAction()
    }

    this.addAction("press")
    this.addAction("enable")
    this.addAction("disable")
    this.addProperty("is-enabled")
    this.addEvent("pressed")
  },

  /**
   * Presses the button programatically.
   * If a button is enabled, a callback and the action are invoked.
   */
  press: function() {
    // Simulate the click by pressing an internal simple button.
    this.button.press()
  },

  canPress: function() {
    return this.isEnabled()
  },

  /**
   * Invoked, when the internal button is pressed
   */
  internalButtonPressed: function() {
    this.notifyObservers("pressed")

    if (this.action != null) {
      this.action.invoke()
    }
  },

  /**
   * Invoked, when the internal button is pressed
   */
  internalButtonEnabledChanged: function() {
    this.notifyObservers("isEnabledChanged")
    this.notifyObservers("canPressChanged")
    this.notifyObservers("canEnableChanged")
    this.notifyObservers("canDisableChanged")
  },

  /**
   * Returns true, if this button is enabled.
   */
  isEnabled: function() {
    return this.button.isEnabled()
  },

  /**
   * Enabled/disables this button, unless button is associated with an action.
   */
  setEnabled: function(enabled) {
    if (this.action == null) {
      this.button.setEnabled(enabled)
    }
  },

  enable: function() {
    this.setEnabled(true)
  },

  canEnable: function() {
    return !this.isEnabled()
  },

  disable: function() {
    this.setEnabled(false)
  },

  canDisable: function() {
    return this.isEnabled()
  },

  updateButtonWithAction: function() {
    this.button.setEnabled(this.action.isEnabled())
  }
})
