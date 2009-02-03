initializeProject {
    project, module ->

    dependencies {
        api {
            product("synergetics") {
                subsystem("cornerstone")
//                subsystem("metadata")
            }
        }

        tests {
            product("synergetics") {
                subsystem("runtime")
                subsystem("testtools")
            }

            ref("jibx-run")
        }
    }
}
