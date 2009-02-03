initializeProject {
    project, module ->

    dependencies {
        api {
            subsystem("localization")
            subsystem("runtime")

            ref("jibx-run")
        }

        impl {
            execute {
                ref("jpox")
            }
        }

        tests {
            subsystem("testtools")
            ref("apache-xerces")

            execute {
                ref("hsqldb")
            }
        }
    }
}
