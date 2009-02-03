initializeProject {
    project, module ->

    dependencies {
        api {
//            subsystem("localization")
//            subsystem("runtime")
//            subsystem("servlet")
//
//            product("devices") {
//                subsystem("api")
//                subsystem("repository")
//            }
//
            product("synergetics") {
//                subsystem("cache")
//                subsystem("cornerstone")
                subsystem("localization")
//                subsystem("runtime")
            }
//
//            ref("servlet-api")
//            ref("volantis-xerces")
//            ref("our-digester")
//            ref("commons-cli")

//            subsystem("runtime")
//
//            ref("jibx-run")
        }

        impl {
//            execute {
//                ref("jpox")
//            }
        }

        tests {
//            subsystem("testtools", product: "synergetics")
//            ref("testtools")
//            ref("xercesImpl")
//
//            execute {
//                ref("hsqldb")
//            }
        }
    }
}
