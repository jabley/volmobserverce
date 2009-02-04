initializeProject {
    project, module ->

    dependencies {
        api {
            subsystem("localization")
            
            product("synergetics") {
                subsystem("cornerstone")
                subsystem("runtime")
            }

            ref("jibx-run")
        }
    }
}
