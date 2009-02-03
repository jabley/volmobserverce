initializeProject {
    project, module ->

    dependencies {
        api {
            subsystem("localization")
            subsystem("osgi-jpox")
            subsystem("runtime")
        }

        impl {
            ref("osgi")
            ref("jdo2-api")
            ref("jpox")
        }

        tests {
            subsystem("testtools")

            execute {
                ref("hsqldb")
            }
        }
    }
}
 
