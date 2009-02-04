initializeProject {
    project, module ->

    dependencies {
        api {
            subsystem("configuration")
            subsystem("repository")
            product("synergetics") {
                subsystem("cornerstone")
                subsystem("testtools")
            }

            ref("eclipse-core-resources")
            ref("eclipse-core-runtime")
        }
    }
}
