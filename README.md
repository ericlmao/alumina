![logo-transparent.png](assets%2Flogo-transparent.png)

![Sonatype Nexus (Releases)](https://img.shields.io/nexus/r/games.negative.alumina/alumina?server=https%3A%2F%2Frepo.negative.games&nexusVersion=3&logo=sonatype&label=version)
[![Javadoc](https://img.shields.io/badge/JavaDoc-Online-green)](https://jd.alumina.dev) ![Discord](https://img.shields.io/discord/822346437240815656?logo=discord&label=discord)

# Repository
## Maven

```xml
<repository>
    <id>Negative Games</id>
    <url>https://repo.negative.games/repository/development/</url>
</repository>
```

```xml
<dependency>
    <groupId>games.negative.alumina</groupId>
    <artifactId>alumina</artifactId>
    <version>{VERSION}</version>
    <scope>compile</scope>
</dependency>
```

## Gradle
```groovy
maven { url 'https://repo.negative.games/repository/development/' }
```

```groovy
implementation("games.negative.alumina:alumina:{VERSION}")
```

# Wiki
https://wiki.alumina.dev/
