initializeProject {
    project, module ->

    dependencies {
        api {
            subsystem("localization")
            subsystem("operation-engine")

            ref("servlet-api")
            ref("osgi")
            ref("osgi-services")
            ref("soap")
            ref("jibx-run")
        }

        impl {
            subsystem("common")
            subsystem("retriever")
            subsystem("sti-configuration")

            product("synergetics") {
                subsystem("osgi-boot")
            }

            execute {
              // These are needed to automatically configure STIOperation
              // from config/com.volantis.map.sti.impl.STIOperation file
              product("synergetics") {
                  subsystem("cm-monitor")
                  subsystem("osgi-cm")
              }
              // cm referenced above refuses to work w/o osgi-log
              // todo: investigate and move this dependency to synergetics
              ref("osgi-log")

              // AXIS is needed to provide SOAP implementation
              // used for making STI requests
              ref("axis")
            }
        }

        tests {

            product("synergetics") {
                subsystem("mock")
            }
        }
    }
}
