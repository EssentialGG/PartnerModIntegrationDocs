// fixme remove after 1.0.0
allprojects {
    repositories {
        mavenLocal()
    }
}

// Enable reproducible builds
allprojects {
    tasks.withType<AbstractArchiveTask> {
        isPreserveFileTimestamps = false
        isReproducibleFileOrder = true
    }
}
