initializeProject {
    project, module ->

    dependencies {
        api {
            subsystem("cornerstone")
            subsystem("localization")
            subsystem("runtime")

            ref("jibx-run")
        }

        tests {
            subsystem("testtools")
        }
    }
}
