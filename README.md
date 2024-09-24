![logo](assets/alumina.png)


![Sonatype Nexus (Releases)](https://img.shields.io/nexus/r/games.negative.alumina/alumina?server=https%3A%2F%2Frepo.negative.games&nexusVersion=3&logo=sonatype&label=version)
[![Javadoc](https://img.shields.io/badge/JavaDoc-Online-green)](https://jd.alumina.dev) ![Discord](https://img.shields.io/discord/822346437240815656?logo=discord&label=discord)

***
![features](assets/Features.png)
***
![install](assets/Installation%20Details.png)
![maven](assets/Maven.png)
```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```
```xml
<dependency>
    <groupId>com.github.ericlmao</groupId>
    <artifactId>alumina</artifactId>
    <version>2.9.3</version>
</dependency>
```

### Optional Shading
In case other software in your JVM is using this library, you should shade it into your jar and relocate it to your preferred namespace using the `maven-shade-plugin`.
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-shade-plugin</artifactId>
    <version>3.5.0</version>
    <executions>
        <execution>
            <phase>package</phase>
            <goals>
                <goal>shade</goal>
            </goals>
            <configuration>
                <createDependencyReducedPom>false</createDependencyReducedPom>
                <relocations>
                    <relocation>
                        <pattern>games.negative.alumina</pattern>
                        <shadedPattern>[YOUR NAMESPACE].alumina</shadedPattern>
                    </relocation>
                </relocations>
            </configuration>
        </execution>
    </executions>
</plugin>
```
***
![gradle](assets/Gradle.png)
```groovy
maven { url 'https://jitpack.io' }
```
```groovy
implementation("com.github.ericlmao:alumina:2.9.3")
```
***
![discord](assets/Discord.png)
[![Discord](https://discord.com/api/guilds/822346437240815656/widget.png?style=banner2)](https://discord.gg/XnHEn6BB2j)
