initializeProject {
    project, module ->

    dependencies {
        api {
            subsystem("localization")

            product("synergetics") {
                subsystem("cache")
                subsystem("cornerstone")
                subsystem("localization")
                subsystem("net")
                subsystem("old-cache")
                subsystem("performance")
                subsystem("runtime")
                subsystem("xml-utils")
            }

            ref("log4j")
            ref("commons-httpclient")
            ref("servlet-api")
            ref("jsp-api")

            pom(org: "our.w3c", name: "tidy", rev: "1.0")
            pom(org: "our.apache", name: "commons.jxpath", rev: "1.1-b1")
            ref("volantis-xerces")

            pom(org: "com.ibm", name: "wsdl4j", rev: "1.4")
            pom(org: "com.volantis.apache", name: "wsif", rev: "5.0")
        }

        impl {
            ref("volantis-xalan")
            pom(org: "org.apache", name: "axis", rev: "1.2.1")
            pom(org: "javax", name: "xml.rpc", rev: "1.1")
        }

        support {
            product("synergetics") {
                subsystem("testtools")
            }
        }

        tests {
            product("synergetics") {
                subsystem("mock")
            }
        }
    }
}
