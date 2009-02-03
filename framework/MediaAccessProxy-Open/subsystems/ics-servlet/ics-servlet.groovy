initializeProject {
    project, module ->

    dependencies {
        api {
            subsystem("common")
            subsystem("ics-configuration")
            subsystem("image-processor")
            subsystem("localization")
            subsystem("operation-engine")
            subsystem("retriever")

            ref("servlet-api")
            ref("osgi")
            ref("osgi-services")
        }

        impl {
            execute {
                // Required to enable the Declarative Services,
                // which ICS servlet uses to register itself
                ref("osgi-ds")

                product("synergetics") {
                    // Required for OSGiHttpBridgeServlet and OSGiBootListener (see web.xml)
                    // Note that it's treated in a special way by installer
                    subsystem("osgi-boot")
                    // Needed for servlet registration
                    subsystem("osgi-j2ee-bridge")
                }
            }
        }
    }
}
