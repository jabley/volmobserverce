initializeProject {
    project, module ->

    dependencies {
        api {
            subsystem("common")
            subsystem("localization")

            product("synergetics") {
                subsystem("descriptor-store")
                subsystem("runtime")
            }

            ref("servlet-api")
            ref("osgi")
            ref("osgi-services")
        }

        impl {
            product("synergetics") {
                subsystem("descriptor-store")
            }

            execute {
                // I'm not sure about these dependencies, so feel free
                // to move them to other subsystem, if you know what you are doing
                subsystem("map-logger")
                subsystem("svg-reader")
            }
        }
    }
}
