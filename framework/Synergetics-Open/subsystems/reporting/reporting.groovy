initializeProject {
    project, module ->

    dependencies {
        api {
            subsystem("cornerstone")
            subsystem("localization")
            subsystem("runtime")

            ref("commons-dbcp")
            ref("jibx-run")
            ref("log4j")
            ref("osgi")
            ref("osgi-services")
        }

        tests {
            subsystem("mock")
            subsystem("testtools")
        }
    }
}
