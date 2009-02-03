initializeProject {
    project, module ->

    dependencies {
        api {
            uses {
                subsystem("runtime")

                ref("log4j")
                ref("volantis-jdom")
                ref("httpunit")
                ref("junit")
                ref("junit.addons")
                ref("servlet-api")
                ref("volantis-mock")
                ref("xmlunit")
            }
        }
    }
}
