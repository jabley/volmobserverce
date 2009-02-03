initializeProject {
    project, module ->

    dependencies {
        api {
            subsystem("api")
            subsystem("localization")
            subsystem("repository")

            product("synergetics") {
//                subsystem("localization")
                subsystem("runtime")
            }

            ref("osgi-services")
        }
    }
}
