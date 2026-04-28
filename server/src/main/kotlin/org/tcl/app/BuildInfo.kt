package org.tcl.app

object BuildInfo {
    val version: String by lazy {
        val props = java.util.Properties()
        BuildInfo::class.java.classLoader
            .getResourceAsStream("buildinfo.properties")
            ?.use { props.load(it) }
        props.getProperty("version", "unknown")
    }
}