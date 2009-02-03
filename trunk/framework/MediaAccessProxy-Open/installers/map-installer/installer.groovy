populateProject {
project, module ->

    def installer_name = "map-installer"
    def packs = project.properties.installer_packs
    project.properties.installer_pack_list = ["map", "ics", "sti", "example", "docs"]
    def ant = module.antBuilder;
    def product = project.properties.product
    def packsdir = project.properties.installer_packs_dir

    // regular expression used to find jars which should NOT be copied out when creating installer
    // in general: all the versions of servlet-api
    def provided_jars = /\/javax.servlet/

    // filling body of target 'prepare-packs' is required
    module.getTarget("prepare-packs").body = {
        target, original ->

        // invoke original body:
        original.call()

        //--------------- MAP Pack ----------------
        //copy webapps
        ant.copy(toDir:packs.map.resolve("webapps/map")) {
          fileset(dir:project.resolve("webapps/map"))
        }

        //--------------- ICS Pack ----------------
        //copy webapps
        ant.copy(toDir:packs.ics.resolve("webapps/map")) {
          fileset(dir:project.resolve("webapps/ics"))
        }

        //--------------- STI Pack ----------------
        //copy webapps
        ant.copy(toDir:packs.sti.resolve("webapps/map")) {
          fileset(dir:project.resolve("webapps/sti"))
        }

        //--------------- Docs Pack ---------------
       // ant.copy(toDir:packs.docs.resolve("/documentation/volantis")){
       //   fileset(dir:product.dirs.built.resolve("docs-dist")) {
       //     include(name:"**")
       //   }
       // }
    }

    module.getTarget("installer-$installer_name").required << addTarget("prepare-installer-$installer_name") {
        target ->

        // set properties used for filtering by IzPack Plugin
        project.properties.installer_properties = [
          "installer.product.revision" : "5.1.0"
        ]

        def installer_packs = module.getTarget("copy-packs-for-$installer_name").invoke()
        project.properties.installer_file = "MAP"

        //--------------- MAP Pack ----------------
        // prepare framework
        provided_jars = module.getTarget("installer-prepare-osgi-framework").invoke(
                installer_packs.map.resolve("webapps/map/WEB-INF"),
                provided_jars)

        def map_jars = []

        def copyBundles = {
            pack, subsystems ->

            def bundlesdir = pack.resolve("webapps/map/WEB-INF/osgi/bundles")
            bundlesdir.mkdirs()
            def jars = getSubsystemsDependencies(module, provided_jars, subsystems)
            // remove map_jars because map is required pack, so no need to copy it in another packs
            jars = jars - map_jars
            jars.each {
                ant.copy(file:it, toDir:bundlesdir)
            }
            return jars
        }

        //copy bundles
        map_jars = copyBundles(installer_packs.map, ["map-servlet"])
        copyBundles(installer_packs.ics, ["ics-servlet"])
        copyBundles(installer_packs.sti, ["sti-plugin"])
        copyBundles(installer_packs.example, ["identity-plugin"])

        //--------------- Docs Pack ---------------
        // copy pdf files
        ant.copy(toDir:installer_packs.docs.resolve("/documentation")){
          fileset(dir:project.resolve("documentation")) {
            include(name:"volantis/manuals/**")
          }
        }
    }
}
