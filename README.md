![mailjudge-java](https://socialify.git.ci/nightowl-devs/mailjudge-java/image?custom_language=Java&description=1&forks=1&issues=1&language=1&name=1&owner=1&pulls=1&stargazers=1&theme=Dark)

## Installation

### Maven
Add the following repository and dependency to your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>nightowldev-repo</id>
        <url>https://repo.nightowl.dev</url>
    </repository>
</repositories>

<dependency>
    <groupId>dev.nghtowl</groupId>
    <artifactId>mailjudge</artifactId>
    <version>version</version>
</dependency>
```

### Gradle
Add the following repository and dependency to your `build.gradle.kts`:

```kotlin
repositories {
    maven {
        url = uri("https://repo.nightowl.dev")
    }
}

dependencies {
    implementation("dev.nghtowl:mailjudge:version")
}
```

Replace `version` with the latest version of the library.
