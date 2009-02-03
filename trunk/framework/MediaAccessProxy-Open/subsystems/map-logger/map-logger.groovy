initializeProject {
    project, module ->

    dependencies {
        impl {
            product("synergetics") {
                subsystem("localization")
            }

            ref("osgi")
            ref("osgi-services")
            ref("servlet-api")
        }
    }
}
