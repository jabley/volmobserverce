initializeProject {
    project, module ->

    dependencies {
        api {
            subsystem("localization")

            product("synergetics") {
                subsystem("runtime")
            }

            ref("osgi-services")
            ref("batik")

	    execute {
	        pom(org: "org.apache", name: "xerces", rev: "2.8.1")
	    }
        }
    }
}
