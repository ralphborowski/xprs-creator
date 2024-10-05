# xprs creator #

The xprs creator is a maven plugin that reads a model file and runs the generators.

## How to use it ##

Create a new maven project and add the following plugin to your pom.xml:

```
#!xml

<build>
  ...
  <plugins>
    ...
    <plugin>
      <artifactId>creator</artifactId>
      <groupId>rocks.xprs</groupId>
      <version>0.1.0</version>
      <executions>
        <execution>
          <goals>
            <goal>generate</goal>
          </goals>
          <phase>generate-sources</phase>
          <configuration>
            <<!-- add a resource file or an URL for your project file -->
            <projectfile>project.xprsm</projectfile> 
            <generators>
              <!-- add the class names of your generators here -->
              <generator>your.Generator</generator>
            </generators>
          </configuration>
        </execution>
      </executions>
      <dependencies>
        <!-- add dependencies for your models and generators here -->
        <dependency>
          <groupId>some.group.id</groupId>
          <artifactId>artifact</artifactId>
          <version>1.0</version>
        </dependency>
        ...
      </dependencies>
    </plugin>
    ...
  </plugins>
  ...
</build>
```

The creator is added to two phases of the build cycle:

* generate-sources: in this phase the generators are called and the sources are created in /target
* build: in this phase the generated sources and the handwritten sources are combined and compiled

So all you need to do is a normal 


```

mvn clean install

```

