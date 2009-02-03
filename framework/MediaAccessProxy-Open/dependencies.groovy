// =============================================================================
//     Standard Java API Jars
// =============================================================================

pom(id: "jai", org: "javax", name: "media.jai", rev: "1.1.3")
pom(id: "servlet-api", org: "javax", name: "servlet", rev: "2.4")
pom(id: "soap", org:"javax", name: "xml.soap", rev:"1.2")

// =============================================================================
//     Volantis Modified 3rd Party Jars
// =============================================================================

pom(id: "imageio", org: "com.sun", name: "media.imageio", rev: "1.1.patched")

// =============================================================================
//     Apache 3rd Party Jars
// =============================================================================

pom(id: "axis", org: "org.apache", name: "axis", rev: "1.4")
pom(id: "batik", org: "org.apache", name: "batik", rev: "1.6")
pom(id: "commons-httpclient", org: "org.apache", name: "commons.httpclient", rev: "3.0.1")

// =============================================================================
//     Eclipse 3rd Party Jars
// =============================================================================

pom(id: "osgi", org: "org.eclipse", name: "equinox.osgi", rev: "3.3.1.1")
pom(id: "osgi-services", org: "org.eclipse", name: "equinox.osgi.services", rev: "3.1.200")
pom(id: "osgi-ds", org: "org.eclipse", name: "equinox.ds", rev: "1.0.0.v20070226")
pom(id: "osgi-log", org: "org.eclipse", name: "equinox.log", rev: "1.0.100.v20070226")

// =============================================================================
//     Other 3rd Party Jars
// =============================================================================

pom(id: "jibx-run", org: "org.jibx", name: "api", rev: "1.1.5")
pom(id: "junit", org: "org.junit", name: "api", rev: "3.8.2")
pom(id: "junit.addons", org: "net.sourceforge", name: "junit.addons", rev: "1.4")

// =============================================================================
//     Commercial 3rd Party Jars
// =============================================================================

//
// =============================================================================
//     Build Collections
// =============================================================================

collection(id: "common-test-support") {
    ref("junit")
    ref("junit.addons")

    product("synergetics") {
        subsystem("testtools")
    }
}

collection(id: "volantis-mock-task") {
    product("testtools") {
        subsystem("mock-generator", id: "volantis-mock-generator")
    }
}
