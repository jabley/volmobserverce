

/**
 * (c) Volantis Systems Ltd 2008. 
 */

/**
 * An module with internal building blocks
 */
Widget.Internal = Package.define()

/**
 * Mix-in which defines a method to store input fields list options.
 * Currently used only in vfc-map.js but it is part of building block so it is here 
 */
Widget.InputContainer = Class.define(
{
    /**
     * Storage for object's options. Internal use only.
     */
    inputFields: {},

    /**
     * Add new input to container
     *
     * Container hold map where input name is key and input id is value.
     */
    registerInputId: function(fieldName,fieldId) {
        this.inputFields[fieldName] = fieldId
    },

    /**
     * Get field named 'name'.
     */
    getInputId: function (fieldName) {
        return this.inputFields[fieldName]
    },

    /**
     * Return all input fields in hash map.
     */
    getAllInputs : function(){
        return this.inputFields
    }
})

/*
 * Mix-in which defines a method to install options, which
 * can be used during object initialization (inside initialize() method).
 */
Widget.OptionsContainer = Class.define(
{
    /*
     * Storage for object's options. Internal use only.
     */
    options: null,

    /*
     * Installs given options on this object.
     *
     * Once options are installed, they can be accessed
     * using getOption() method.
     */
    installOptions: function(options) {
        this.options = options

        if (options) {
            for (var option in options) {
                if (!(this[option] === undefined) && typeof(this[option]) != 'function') {
                    this[option] = options[option];
                }
            }
        }
    },

    /*
     * Get option named 'name'.
     * If option is not defined, it returns 'def' by default.
     */
    getOption: function (name, def) {
        return this.options ? (this.options[name] || def) : def
    }
})



/**
 * An internal class maintaining a set of observers,
 * and sending notifications to them.
 */
Widget.Internal.ObserverHandler = Class.define(
{
  /**
   * Adds an observer for given notification name.
   */
  addObserver: function(notificationName, observer, handlerName) {
    // Get a map of observers, keyed by notification name.
    // Create one, if not yet created
    var observersArray = this._getObserversArray(notificationName, true)

    // Check, if the observer exists.
    var index = this._getObserverIndex(observersArray, observer, handlerName)

    // If not, add it.
    if (index == null) {
      observersArray.push(observer)
      observersArray.push(handlerName)

      if (arguments.length <= 3) {
        observersArray.push(null)
      } else {
        observersArray.push($A(arguments).slice(3))
      }
    }
  },

  /**
   * Removes an observer for given notification name.
   */
  removeObserver: function(notificationName, observer, handlerName) {
    // Get a map of observers, keyed by notification name.
    // In not exist, return immediately.
    var observersArray = this._getObserversArray(notificationName)

    // Find an index of the observer.
    var index = this._getObserverIndex(observersArray, observer, handlerName)

    // If found, remove it from array.
    if (index != null) {
      observersArray.splice (i, 3)
    }
  },

  /**
   * Invokes handlers for all observers on given notification name.
   */
  notifyObservers: function(notificationName) {
    // Notify observers for specified notification name
    var observersArray = this._getObserversArray(notificationName)

    if (observersArray != null) {
      this.notifyObserversArray(observersArray, arguments)
    }

    // Notify observers for all notifications
    var observersArray = this._getObserversArray(null)

    if (observersArray != null) {
      this.notifyAllObserversArray(observersArray, arguments)
    }
  },

  notifyObserversArray: function(observersArray, args) {
      for (var index = 0; index < observersArray.length; index = index + 3) {
        var observer = observersArray[index]
        var handlerName = observersArray[index+1]
        var additionalArguments = observersArray[index+2]

        // Invoke a handler method on the observer object.
        if (additionalArguments == null) {
        if (args.length == 1) {
            // If there are no additional arguments apart from eventName,
            // invoke the handler with no arguments.
            observer[handlerName]()

          } else {
            // Otherwise, pass all arguments except first one (eventName).
            observer[handlerName].apply(observer, $A(args).slice(1))
          }
        } else {
          if (arguments.length == 1) {
            // If there are no additional arguments apart from eventName,
            // invoke the handler with additional arguments only.
            observer[handlerName].apply(observer, additionalArguments)

          } else {
            // Otherwise, concatenate additional arguments with notification arguments
            // except first one (eventName).
          observer[handlerName].apply(observer, additionalArguments.concat($A(args).slice(1)))
          }
        }
      }
  },

  notifyAllObserversArray: function(observersArray, args) {
    for (var index = 0; index < observersArray.length; index = index + 3) {
      var observer = observersArray[index]
      var handlerName = observersArray[index+1]
      var additionalArguments = observersArray[index+2]

      // Invoke a handler method on the observer object.
      if (additionalArguments == null) {
        // If there are no additional arguments, invoke the handler
        // with all notification arguments, including notification name.
        observer[handlerName].apply(observer, args)

      } else {
        // Otherwise, concatenate additional arguments with notification
        // arguments except that notification name goes first.
        var argsArray = $A(args)

        observer[handlerName].apply(observer, [argsArray[0]].concat(additionalArguments).concat(argsArray.slice(1)))
      }
    }
  },

  _getObserversArray: function(notificationName, create) {
    // Null notofication name means: all notifications.
    notificationName = (notificationName == null) ? "" : notificationName

    // Get a map of observers, keyed by notification name.
    // Create one, if not yet created
    var observers = this.observers

    if (observers == null && create == true) {
      observers = {}

      this.observers = observers
    }

    // Get an array of event observers, creating it if not yet created.
    if (observers != null) {
      var notificationObservers = observers[notificationName]

      if (notificationObservers == null && create) {
        notificationObservers = []

        observers[notificationName] = notificationObservers
      }
    }

    return notificationObservers
  },

  _getObserverIndex: function(observersArray, observer, handlerName) {
    for (var index = 0; index < observersArray; index = index + 3) {
      if (observersArray[index] === observer && observersArray[index+1] == handlerName) {
        return index
      }
    }
  }
})

/**
 * A mixin for all observable objects.
 * Provides a method to notify observers on given event name.
 */
Widget.Observable = Class.define(
{
  /**
   * Internal method. Returns and instance of Widget.ObserverHandler
   * used by this object, creating it if nessecarry.
   */
  _getObserverHandler: function() {
    if (this.observerHandler == null) {
      this.observerHandler = new Widget.Internal.ObserverHandler()
    }

    return this.observerHandler
  },

  /**
   * Notifies all observers on given notification name.
   *
   * Examples:
   *  this.notifyObservers("pressed")
   *  this.notifyObservers("valueChanged", 12)
   */
  notifyObservers: function(notificationName) {
    var observerHandler = this._getObserverHandler()

    observerHandler.notifyObservers.apply(observerHandler, arguments)
  }
})

/**
 * A mixin for all objects, which has the ability to observe
 * other observable objects for events.
 */
Widget.Observer = Class.define(
{
  /**
   * Observes given object on given notification name.
   * If a notification is sent, a given handler is invoked on object.
   * If more that three arguments are specified, the additional ones
   * will be passed to the handler method, followed by notification arguments.
   *
   * Example:
   *  myGallery.observe(playButton, "pressed", "play")
   */
  observe: function(object, notificationName, handlerName) {
    var handler = object._getObserverHandler()

    if (handler != null) {
      if (arguments.length <= 3) {
        handler.addObserver(notificationName, this, handlerName)
      } else {
        handler.addObserver.apply(handler, [notificationName, this, handlerName].concat($A(arguments).slice(3)))
      }
    }
  },

  /**
   * Stops observing given object on given notification using given handler.
   *
   * Example:
   *  myGallery.stopObserving(playButton, "pressed", "play")
   */
  stopObserving: function(object, notificationName, handlerName) {
    var handler = object._getObserverHandler()

    if (handler != null) {
      handler.removeObserver(notificationName, this, handlerName)
    }
  }
})

/**
 * An object containing a set of named actions.
 */
Widget.ActionOwner = Class.define(
{
  /**
   * Returns action with specified name, if it exists.
   */
  getAction: function(name) {
    if (this.actions != null) {
      return this.actions[name]
    }
  },

  /**
   * Adds named action. To be called upon initialization.
   */
  addAction: function(name, options) {
    if (this.actions == null) {
      this.actions = {}
    }

    this.actions[name] = new Widget.Action(name, this, options)
  }
})

/**
 * Object containing a set of named properties.
 */
Widget.PropertyOwner = Class.define(
{
  /**
   * Returns property with specified name, is exists.
   */
  getProperty: function(name) {
    if (this.properties != null) {
      return this.properties[name]
    }
  },

  /**
   * Adds named property. To be called upon initialization.
   */
  addProperty: function(name, options) {
    if (this.properties == null) {
      this.properties = {}
    }

    this.properties[name] = new Widget.Property(name, this, options)
  }
})

/**
 * An object containing a set of named events.
 */
Widget.EventOwner = Class.define(
{
  /**
   * Returns event with specified name, if it exists.
   */
  getEvent: function(name) {
    if (name != "") {
      // Standard event
    if (this.events != null) {
      return this.events[name]
    }

    } else {
      // Meta event
      if (this.metaEvent == null) {
        this.metaEvent = new Widget.Event("", this)
      }

      return this.metaEvent
    }
  },

  /**
   * Adds named event. To be called upon initialization.
   */
  addEvent: function(name, options) {
    // Create event
    var event = new Widget.Event(name, this, options)

    // Add event
    if (this.events == null) {
      this.events = {}
    }

    this.events[name] = event

    // Store entry in mapping between notification name and event name.
    if (event.notificationName != null) {
      if (this.notification2EventNameMap == null) {
        this.notification2EventNameMap = {}
      }

      this.notification2EventNameMap[event.notificationName] = name
    }
  },

  getEventNameForMetaEvent: function(notificationName) {
    if (this.notification2EventNameMap != null) {
      return this.notification2EventNameMap[notificationName]
    }
  }
})

/**
 * Member owner provides single method to access Actions, Properties and Events.
 */
Widget.MemberOwner = Class.define(Widget.ActionOwner, Widget.PropertyOwner, Widget.EventOwner,
{
  getMember: function(name) {
    var member = this.getAction(name)

    if (member == null) {
      member = this.getProperty(name)

      if (member == null) {
        member = this.getEvent(name)
      }
    }

    return member
  }
})

/**
 * An internal class performing member type conversion.
 */
Widget.Internal.MemberTypeConverter = Class.define(
{
  /**
   * Validates the string value before it's set.
   */
  validate: function(type, value) {
    if (type == "boolean") {
      return (value == "yes") || (value == "no")
    } else if (type == "int") {
      return !isNaN(parseInt(value))
    } else if (type == "number") {
      return !isNaN(parseFloat(value))
    } else if (type == "string") {
      return true
    } else if (type == "widget") {
      return (Widget.getInstance(value) != null)
    } else if (type == "list") {
      return true;
    } else {
      throw "Invalid type: " + type;
    }
  },

  /**
   * Convert non-null JavaScript value into string value
   */
  convertForGet: function(type, value) {
    if (type == "boolean") {
      return value ? "yes" : "no"
    } else if (type == "int") {
      return value.toString()
    } else if (type == "number") {
      return value.toString()
    } else if (type == "string") {
      return value
    } else if (type == "widget") {
      return (value._widgetId == null) ? "" : value._widgetId
    } else if (type == "list") {
      return value.toString()
    } else {
      throw "Invalid type: " + type;
    }
  },

  /**
   * Converts already validated string value into JavaScript value.
   */
  convertForSet: function(type, value) {
    if (type == "boolean") {
      return (value == "yes")
    } else if (type == "int") {
      return parseInt(value)
    } else if (type == "number") {
      return parseFloat(value)
    } else if (type == "string") {
      return value
    } else if (type == "widget") {
      return Widget.getInstance(value)
    } else if (type == "list") {
      return value.split(',');
    } else {
      throw "Invalid type: " + type;
    }
  }
})

/**
 * Returns a singleton instance of MemberTypeConverter.
 */
Widget.Internal.getMemberTypeConverter = function() {
  if (this._memberTypeConverter == null) {
    this._memberTypeConverter = new Widget.Internal.MemberTypeConverter()
  }

  return this._memberTypeConverter
}

/**
 * Action is something, which may be invoked.
 *
 * An action may be in enabled, or disabled state.
 * In disabled state, invoking the action does nothing.
 */
Widget.Action = Class.define(Widget.OptionsContainer, Widget.Observable, Widget.Observer, Widget.MemberOwner,
{
  methodName: null,
  canMethodName: null,
  canChangedNotificationName: null,

  /**
   * Initialization.
   */
  initialize: function(name, owner, options) {
    this.installOptions(options)

    this.name = name
    this.owner = owner

    // Generate default method/callback names based on action name.
    if (this.methodName == null) {
      this.methodName = this.name.camelize()
    }

    if (this.canMethodName == null) {
      this.canMethodName = ("can-" + this.name).camelize()
    }

    if (this.canChangedNotificationName == null) {
      this.canChangedNotificationName = ("can-" + this.name + "-changed").camelize()
    }

    // Generate invoker and enabler getter/callback handler.
    this.observe(this.owner, this.canChangedNotificationName, "isEnabledChanged")

    this.addProperty("is-enabled")

    if (Widget.isLogEnabled()) {
      Widget.log("Action", "Created action: " + this.toString())
    }
  },

  /**
   * Invokes this action with specified arguments, if it's enabled.
   */
  invoke: function() {
    if (this.isEnabled()) {
      this.owner[this.methodName].apply(this.owner, arguments)
    }
  },

  /**
   * Returns true, if this action is enabled, false otherewise.
   */
  isEnabled: function() {
    var isEnabled = true

    var canFunction = this.owner[this.canMethodName]

    if (canFunction != null) {
      isEnabled = canFunction.apply(this.owner)
    }

    return isEnabled
  },

  isEnabledChanged: function() {
    this.notifyObservers('isEnabledChanged')
  }
})

/**
 * A property holding a value as a string.
 * It can work in read-only or writable mode.
 */
Widget.Property = Class.define(Widget.OptionsContainer, Widget.Observable, Widget.Observer, Widget.MemberOwner,
{
  type: null,
  getMethodName: null,
  setMethodName: null,
  changedNotificationName: null,
  defaultValue: "",
  writable: null,
  readable: null,
  validator: null,

  /**
   * Initialization
   */
  initialize: function(name, owner, options) {
    this.installOptions(options)

    this.name = name
    this.owner = owner

    // Get implementation of a super method, so it'll be possible to
    // call it from overwritten one.
    this.superGetMember = Widget.MemberOwner.prototype.getMember.bind(this)

    // Generate default method/callback names.
    if (this.getMethodName == null) {
      if (this.name.substring(0, 3) == "is-" ||
          this.name.substring(0, 4) == "can-" ||
          this.name.substring(0, 4) == "has-" ||
          this.name.substring(0, 5) == "does-") {
        this.getMethodName = this.name.camelize()
      } else {
        this.getMethodName = ("get-" + this.name).camelize()
      }
    }

    if (this.setMethodName == null) {
      if (this.name.substring(0, 3) == "is-") {
        this.setMethodName = ("set-" + this.name.slice(3)).camelize()
      } else if (this.name.substring(0, 4) == "can-") {
        this.setMethodName = ("set-" + this.name.slice(4)).camelize()
      } else if (this.name.substring(0, 4) == "has-") {
        this.setMethodName = ("set-" + this.name.slice(4)).camelize()
      } else if (this.name.substring(0, 5) == "does-") {
        this.setMethodName = ("set-" + this.name.slice(5)).camelize()
      } else {
        this.setMethodName = ("set-" + this.name).camelize()
      }
    }

    if (this.changedNotificationName == null) {
      this.changedNotificationName = (this.name + "-changed").camelize()
    }

    // Figure the type from property name.
    if (this.type == null) {
      if (this.name.substring(0, 3) == "is-" ||
          this.name.substring(0, 4) == "can-" ||
          this.name.substring(0, 4) == "has-" ||
          this.name.substring(0, 5) == "does-") {
        this.type = "boolean"
      } else if (this.name.substring(this.name.length-6) == "-count" ||
                 this.name.substring(this.name.length-7) == "-number") {
        this.type = "int"
      } else {
        this.type = "string"
      }
    }

    // Determine, if a property is readable and writable
    if (this.readable == null) {
      this.readable = (this.owner[this.getMethodName] != null)
    }

    if (this.writable == null) {
      this.writable = (this.owner[this.setMethodName] != null)
    }

    // Observe property owner on value change.
    this.observe(this.owner, this.changedNotificationName, "ownerChanged")

    this.addEvent("value-changed")

    if (Widget.isLogEnabled()) {
      Widget.log("Property", "Created property: " + this.toString())
    }
  },

  /**
   * Returns a value of the property as a string.
   * The value is never null, but it may be an empty string.
   */
  getValue: function() {
    // If the property is not readable, return immediately with default value.
    if (!this.readable) {
      return this.defaultValue
    }

    // Get the value from get method.
    var value = this.owner[this.getMethodName]()

    // Convert it to string
    if (value == null) {
      // Nulls should be converted to default value
      value = this.defaultValue

    } else {
      // Non-null values should be converted appropriately by its type.
      value = Widget.Internal.getMemberTypeConverter().convertForGet(this.type, value)
    }

    return value
  },

  /**
   * Sets the value of this property, unless it's read-only.
   */
  setValue: function(value) {
    // If this property is not writable, return immediately.
    if (!this.writable) {
      return
    }

    var valueToSet = null

    // Non-default values should be validated, and converted appropriately by its type.
    if (value != this.defaultValue) {
      var converter = Widget.Internal.getMemberTypeConverter()

      if (!converter.validate(this.type, value)) {
        return
      }

      // Convert value through converter.
      valueToSet = converter.convertForSet(this.type, value)
    }

    // Set the value using setter method.
    this.owner[this.setMethodName](valueToSet)

    // A 'valueChanged' notification should be sent now,
    // and it'll done at the moment the owner sends change notification.
    // in the ownerChanged() method.
  },

  /**
   * Returns true, if this property is writable.
   */
  isWritable: function() {
    return this.writable
  },

  /**
   * Returns true, if this property is readable.
   */
  isReadable: function() {
    return this.readable
  },

  /**
   * Indicates, that the value of this property has changed.
   */
  valueChanged: function() {
    this.notifyObservers('valueChanged')
  },

  /**
   * A callback for widget event.
   */
  ownerChanged: function() {
    this.valueChanged()
  },

  /**
   * When widget-type property is asked for a member,
   * return member of a widget held in the property value.
   */
  getMember: function(name) {
    // Call method on super class.
    var member = this.superGetMember(name)

    if (member == null && this.type == "widget" && this.readable) {
      var widget = this.owner[this.getMethodName]()

      if (widget != null) {
        member = widget.getMember(name)
      }
    }

    return member
  }
})

/**
 * An event.
 *
 * Invokes callbacks, when sent.
 */
Widget.Event = Class.define(Widget.OptionsContainer, Widget.Observable, Widget.Observer, Widget.MemberOwner,
{
  notificationName: null,

  initialize: function(name, owner, options) {
    this.installOptions(options)

    this.name = name
    this.owner = owner

    // Figure out a notification name
    if (this.notificationName == null) {
      if (this.name != "") {
        this.notificationName = this.name.camelize()
      } else {
        // Notification name is null, means listen to all notifications.
      }
    }

    if (this.notificationName != null) {
      this.observe(this.owner, this.notificationName, "handleOwnerEvent")
    } else {
      this.observe(this.owner, null, "handleOwnerMetaEvent")
    }
  },

  /**
   * Handles owner notification.
   */
  handleOwnerEvent: function() {
    // Send this event.
    if (arguments.length == 0) {
      // This is a optimisation for parameter-less notifications.
      this.notifyObservers("sent")

    } else {
      // Send an event passing all notification arguments.
      this.notifyObservers.apply(this, ["sent"].concat($A(arguments)))
    }
  },

  /**
   * Handles owner notification.
   */
  handleOwnerMetaEvent: function() {
    var eventName = this.owner.getEventNameForMetaEvent(arguments[0])

    if (eventName != null) {
      var args = $A(arguments)

      args[0] = eventName

      this.notifyObservers.apply(this, ["sent"].concat(args))
    }
  }
})


/*
 * Callback Handler provides an API to add callbacks,
 * and invoke added callbacks.
 *
 * It can be used to implement callback facilities
 * for your classes.
 *
 * Example:
 * MyClass = Class.create({
 *     // Constructor
 *     initialize: function() {
 *         dismissCallbackHandler = new Widget.CallbackHandler()
 *     },
 *
 *     // Adds callback on dismiss
 *     addDismissCallback: function(callback) {
 *         dismissCallbackHandler.add(callback)
 *     },
 *
 *     // Fictional function which serves as an example
 *     // on how to invoke callbacks.
 *     someFunctionProducingDismissCallback: function(event) {
 *         ...
 *         dismissCallbackHandler.invoke()
 *         ...
 *     }
 * })
 */
Widget.CallbackHandler = Class.define(
{
    /*
     * Adds a callback.
     *
     * @param callback The callback function to add.
     * @returns The callback added.
     */
    add: function(callback) {
        if (!this.callbacks) {
            // Case 1: Adding first callback.
            // Store the callback directly.
            this.callbacks = callback
            this.singleCallback = true

        } else {
            // Case 2: Adding subsequent callback.
            if (this.singleCallback) {
                // Case 2a: This is the second callback to add.
                // Create an array to store both callbacks
                this.callbacks = new Array(this.callbacks, callback)
                this.singleCallback = false
            } else {
                // Case 2b: This is a subsequent callback
                // to push into the array
                this.callbacks.push(callback)
            }
        }

        return callback
    },

    /*
     * Removes a callback.
     *
     * @param callback The callback function to remove.
     * @returns The success flag (true if callback has been removed)
     */
    remove: function(callback) {
        if (this.callbacks) {
            if (this.singleCallback) {
                if (this.callbacks === callback) {
                    delete this.callbacks
                    return true
                }
            } else {
                for (var i = 0; i < this.callbacks.length; i++) {
                    if (this.callbacks[i] === callback) {
                        this.callbacks.splice (i, 1)
                        return true
                    }
                }
            }
        }
        return false
    },

    /*
     * Invokes all added callbacks. All parameters are passed
     * to all of the invoked callback functions.
     */
    invoke: function() {
        if (this.callbacks) {
            if (this.singleCallback) {
                this.invokeCallback(this.callbacks, arguments)
            } else {
                for (var i = 0; i < this.callbacks.length; i++) {
                    this.invokeCallback(this.callbacks[i], arguments)
                }
            }
        }
    },

    /*
     * Invoke single callback in safe environment (catching exceptions)
     */
    invokeCallback: function(callback, args) {
        try {
            //Widget.log("CallbackHandler", "Invoking callback...")

            callback.apply(this, args)

            //Widget.log("CallbackHandler", "Callback finished without problems")
        } catch (error) {
            //Widget.log("CallbackHandler", "Exception catched in callback: " + error)
        }
    }
})



/**
 * A super class for all widgets.
 *
 * To define a widget subclass, write:
 *
 * Widget.MyWidget = Class.define(Widget.Widget,
 * {
 *   initialize: function(id, options) {
 *     // Initialize the superclass.
 *     this.initializeWidget(id, options)
 *
 *     // ... subclass initialization goes here ...
 *   }
 * })
 */
Widget.Widget = Class.define(Widget.MemberOwner, Widget.OptionsContainer, Widget.Observable, Widget.Observer,
{
  /**
   * Initializes this widget with ID of the element (may be null),
   * initial property values and options.
   */
  initializeWidget: function(id, options) {
    // Install possible options
    this.installOptions(options)

    this.id = id

    this.element = $(this.id)

    // Create callback handler (deprecated, replaced with Widget.Observable).
    this.callbackHandler = new Widget.CallbackHandler()
  },

  /**
   * Returns element for this widget.
   */
  getElement: function() {
    return this.element
  }
})

/**
 * A super class for all internal widgets.
 *
 * To define an internal widget subclass, write:
 *
 * Widget.Internal.MyWidget = Class.define(Widget.Internal.Widget,
 * {
 *   initialize: function(id, options) {
 *     // Initialize the superclass.
 *     this.initializeWidget(id, options)
 *
 *     // ... subclass initialization goes here ...
 *   }
 * })
 */
Widget.Internal.Widget = Class.define(Widget.OptionsContainer, Widget.Observable, Widget.Observer,
{
  /**
   * Initializes this widget with ID of the element (may be null),
   * initial property values and options.
   */
  initializeWidget: function(id, options) {
    // Install possible options
    this.installOptions(options)

    this.id = id

    this.element = $(this.id)

    // Create callback handler (deprecated, replaced with Widget.Observable).
    this.callbackHandler = new Widget.CallbackHandler()
  },

  /**
   * Returns element for this widget.
   */
  getElement: function() {
    return this.element
  }
})

/**
 * Defines and returns new widget class.
 *
 * This is equivalent to Class.define(Widget.Widget, ...)
 */
Widget.define = function(a1, a2) {
  return Class.define.apply(Class, [Widget.Widget].concat($A(arguments)))
}

/**
 * Defines and returns new internal widget class.
 *
 * This is equivalent to Class.define(Widget.Internal.Widget, ...)
 */
Widget.Internal.define = function(a1, a2) {
  return Class.define.apply(Class, [Widget.Internal.Widget].concat($A(arguments)))
}
