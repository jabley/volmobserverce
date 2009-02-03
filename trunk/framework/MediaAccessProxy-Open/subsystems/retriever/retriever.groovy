initializeProject {
    project, module ->

    dependencies {
        api {
            subsystem("common")
            subsystem("localization")

            product("synergetics") {
                subsystem("cornerstone")
                subsystem("net")
                subsystem("runtime")
            }

            ref("commons-httpclient")
        }
    }
}
