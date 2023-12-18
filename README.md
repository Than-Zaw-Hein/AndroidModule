# AndroidModule

For share module
For Database module need to add room.runtime in build.gradle.kts ( project )
{
id("com.google.devtools.ksp") version "1.9.0-1.0.12"
}
For Database module need to add ksp in build.gradle.kts( app )
plugins {
id("com.google.devtools.ksp")
}
and
{
'implementation("androidx.room:room-runtime:2.6.0")'
}
