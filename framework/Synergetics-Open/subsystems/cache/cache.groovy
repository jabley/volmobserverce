initializeProject {
    project, module ->

    dependencies {
        api {
            subsystem("cornerstone")
            subsystem("localization")
            subsystem("runtime")
        }

        tests {
            subsystem("testtools")
        }
    }
}
