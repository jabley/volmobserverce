initializeProject {
    project, module ->

    dependencies {
        api {
            uses {
                subsystem("api")
            }
            
            subsystem("device")
            subsystem("localization")

            product("synergetics") {
                subsystem("cornerstone")
                subsystem("old-cache")
                subsystem("metadata")
                subsystem("repository")
                subsystem("runtime")
            }

            ref("activation")
            ref("mail")
            ref("our-httpclient")
            ref("volantis-jdom")
            ref("volantis-xerces")
        }

        support {
            product("synergetics") {
//                subsystem("runtime")
                subsystem("testtools")
            }
        }

        tests {

            execute {
                ref("hsqldb")
            }
        }
    }
}
