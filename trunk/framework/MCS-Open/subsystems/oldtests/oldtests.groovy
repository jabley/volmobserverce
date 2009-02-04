initializeProject {
    project, module ->

    dependencies {
        tests {
            subsystem("charset")
            subsystem("common")
            subsystem("configuration")
            subsystem("cornerstone")
            subsystem("dom")
            subsystem("eclipse-ab")
            subsystem("eclipse-builder")
            subsystem("eclipse-common")
            subsystem("eclipse-controls")
            subsystem("eclipse-validation")
            subsystem("eclipse-ucp")
            subsystem("localization")
            subsystem("runtime")
            subsystem("servlet")
            subsystem("testtools")
            subsystem("testtools-runtime")

            product("devices") {
                subsystem("device")
                subsystem("repository")
            }

            product("pipeline") {
                subsystem("pipeline")
            }

            product("synergetics") {
                subsystem("cornerstone")
                subsystem("repository")
            }

            ref("eclipse-core-resources")
            ref("eclipse-core-runtime")
            ref("eclipse-jface")
            ref("eclipse-swt")
            ref("eclipse-osgi")
            ref("mail")
            ref("our-httpclient")
            ref("regexp")
            ref("ucp")
            ref("volantis-xerces")

            execute {
                ref("hsqldb")
            }
        }
    }
}
