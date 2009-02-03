initializeProject {
    project, module ->

    dependencies {
        api {
            subsystem("common")
            subsystem("localization")
            subsystem("operation-engine")

            product("synergetics") {
                subsystem("runtime")
            }

            ref("servlet-api")
            ref("osgi")
            ref("osgi-services")
        }

        impl {
            execute {
                // Required to enable the Declarative Services,
                // which MAP servlet uses to register itself
                ref("osgi-ds")

                product("synergetics") {
                    // Required for OSGiHttpBridgeServlet and OSGiBootListener (see web.xml)
                    subsystem("osgi-boot")
                    // Needed for servlet registration
                    subsystem("osgi-j2ee-bridge")
                }
            }
        }
    }
}
