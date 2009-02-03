initializeProject {
    project, module ->

    dependencies {
        api {
            subsystem("cache")
            subsystem("cornerstone")
            subsystem("localization")
            subsystem("runtime")

            ref("commons-httpclient")
        }

        tests {
            subsystem("testtools")

            product("testtools") {
                subsystem("mock")
            }
        }
    }
}
