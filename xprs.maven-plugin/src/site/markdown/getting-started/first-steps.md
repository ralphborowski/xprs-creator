# Your first application

Welcome to the getting started guide. Here you will learn how to create your first project with xprs. For more detailed information on the xprs modeling language and concepts of xprs, refer to the [reference section](../reference/index.html).

## Table of content

  * [Requirements](#Requirements)
  * [The sample application](#The_sample_application)
  * [Setup a project](#Setup_a_project)
  * [Creating the model](#Creating_the_model)
  * [Relations between models](#Relations_between_models)
  * [Cascading deletes](#Cascading_deletes)
  * [Organize your models](#Organize_your_models)
  * [Configure your generators](#Configure_your_generators)
  * [What you have learned](#What_you_have_learned)


## Requirements

Before we start, make sure you have Java and Maven up and running. This should not be the biggest problem for you, because you maybe a Java developer and Maven support comes with your IDE.

### I never used Maven. What is it?

Maven helps you building your Java projects. Like other tools like Ant or Gradle, it controles the whole process of compiling your code, downloading dependencies from central repositories and uploading build artefacts to repositories. Maven can be extended with plugins. Some of them can help you with documentation and some of them generate code for you. The information needed for compiling, is found in the file `pom.xml` in the root of your project folder. This file differs from Ant or Gradle scripts, because it does not contain commands like "download dependency 1 and copy it to the /libs folder" or "compile the folder /src and copy the binaries to /target". Maven has a fixed structure: Java classes have to be in `/src/main/java`, the result is always compiled to `/target`. So in your `pom.xml` you only find meta information about your project, like the names and versions of your dependencies or the repository where the result should be uploaded to.

Every Maven project has three fields that describe it: groupID, artifactID and version. They are used as an unique identifier of a project. Usually groupID is the project's or company's URL in reverse order. For xprs it is `rocks.xprs`, for you it could be `com.mycompany` or `com.github.myusername`. The artifactID can be the name of the project, like `creator` or `myproject.webservice`. A dependency could look like this

    <dependency>
      <groupId>javax</groupId>
      <artifactId>javaee-web-api</artifactId>
      <version>7.0</version>
      <scope>provided</scope>
    </dependency>

Usually these coordinates can be found on the project websites. You don't have to download the ZIP files on your own. Maven has a central website, where it downloads all needed libraries during the build process.


## The sample application

The web application we want to create is a simple addressbook. We want to create a web application where we can  create contacts and assign as much addresses and telephone numbers to it as we like. This gives us the opportunity to have a look at the following topics:

  * available data types
  * required fields and validation
  * inheritence
  * m-to-n relations
  
After you finished this tutorial you'll be able to extend this example and create more complex applications like Customer Relationship Management (CRM) or Enterprise Resource Planing (ERP) software. 


## Setup a project

There are two ways to setup a xprs project:
  * manually 
  * or by using the archetype.

To give you a deeper understanding, what's happening, we start our sample project by creating all projects on our own. Open your favorite IDE and create a new Maven Java application named "addressbook.model":

    <groupId>com.example</groupId>
    <artifactId>adressbook.model</artifactId>
    <version>0.0.1</version>

Now create a Maven Web Application named "Addressbook Application":

    <groupId>com.example</groupId>
    <artifactId>adressbook.application</artifactId>
    <version>0.0.1</version>

Open the `pom.xml` of your second project and add the following blocks to it:

    <project>
      ...
      <dependencies>
        ...
        <dependency>
          <groupId>rocks.xprs</groupId>
          <artifactId>runtime.sdk</artifactId>
          <version>0.1.0</version>
          <type>jar</type>
        </dependency>
        <dependency>
          <groupId>rocks.xprs</groupId>
          <artifactId>runtime.webservice</artifactId>
          <version>0.1.0</version>
          <type>jar</type>
        </dependency>
        <dependency>
          <groupId>org.freemarker</groupId>
          <artifactId>freemarker</artifactId>
          <version>2.3.23</version>
          <type>jar</type>
        </dependency>
        ...
      </dependencies>
      ..
      <build>
        <plugins>
          ...
          <!-- XPRS CODE GENERATOR -->
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
                  <!-- PROJECT FILE -->
                  <projectfile>com/example/addressbook/Project.xprsm</projectfile> 
                  <!-- USE GENERATORS -->
                  <generators>
                    <generator>rocks.xprs.generators.db.ModelGenerator</generator>
                    <generator>rocks.xprs.generators.db.FilterGenerator</generator>
                    <generator>rocks.xprs.generators.db.ValidatorGenerator</generator>
                    <generator>rocks.xprs.generators.db.DaoGenerator</generator>
                    <generator>rocks.xprs.generators.db.OrmXmlGenerator</generator>
                    <generator>rocks.xprs.generators.gui.EditModuleGenerator</generator>
                  </generators>
                </configuration>
              </execution>
            </executions>
            <dependencies>
              <!-- PROJECT CONTAINING THE PROJECT FILE -->
              <dependency>
                <groupId>com.example</groupId>
                <artifactId>addressbook.model</artifactId>
                <version>0.1.0</version>
              </dependency>
              <!-- PROJECT CONTAINING THE CODE GENERATORS -->
              <dependency>
                <groupId>rocks.xprs</groupId>
                <artifactId>generators</artifactId>
                <version>0.1.0</version>
              </dependency>
            </dependencies>
          </plugin>
        </plugins>
      </build>
      ..
    </project>

This code installes the xprs code generator in the application project and calls it in the pahse "generate-sources" that is done before Maven calls the compiler. The code also sets up the connection to the model file `com/example/addressbook/Project.xprsm` and makes the process dependent on the model project and the generators project. We go into details later in this tutorial.


## Creating the model

As we have set up the basic structure of our project, we are able to start creating the data structures. This is done in xprs Modeling Language (xprsm). So, go to your model project and create a file called `/src/main/resources/com/example/addressbook/Project.xprsm`, because this is what we referenced in the other project's `pom.xml`. Don't create it in `/src/main/java/com/example/addressbook/Project.xprsm` because it might be droped automatically by Maven before compiling the code.

Let's start with the header. You have to give your project a name and a base package:

    PROJECT "addressbook";
    BASEPACKAGE "com.example.addressbook";

Choose a short name for `PROJECT` without spaces or special chars. So you can use it in your generators later as a kind of namespace or brand. The `BASEPACKAGE` is the root package for all code in your project. Model classes will go to `BASEPACKAGE`.model, DAOs to `BASEPACKAGE`.dao and so on.

To make it easier to set the form of address we introduce a enumeration before we create the first model:

    DEFINE ENUM Gender
    WITH OPTIONS
      UNSET,
      MALE,
      FEMALE;

As you see the syntax is very easy to read. It always starts with the keyword `DEFINE` followed by the type we want to define, which can be `ENUM` or `MODEL`. Then we give it a name, like "Gender" in this case. Our enum can be set to one of the three values "UNSET", "MALE" or "FEMALE". Before you ask: no it is not possible to set specific values to it and no, it's also not possible to add methods to this enum. So, keep in mind to add new options at the end of the list when adding new options later on. While storing objects "MALE" will be stored as "1" to the database. So if you have stored your first values and change the code to 

    DEFINE ENUM Gender
    WITH OPTIONS
      UNSET,
      FEMALE,
      MALE;

you will also change the interpretation of each value. After this change "1" is interpreted as "FEMALE".

Because we want to use the existing xprs generators, we have to do a second thing before we can start creating ou first model. We have to 'implement' the interface `rocks.xprs.runtime.db.Entity`. Every entity has to have an ID and information about its creation and last modification. So we create an abstract model called DataItem, that will be the parent class for all our models:

    DEFINE ABSTRACT MODEL DataItem
    WITH FIELDS
      id Long PRIMARY KEY DEFAULT(NULL),
      createUser String(50),
      createDate DateTime,
      modifyUser String(50),
      modifyDate DateTime; 

Analyzing this block, you get a first impression, how to create a model. Instead of the block `WITH OPTIONS` we have a block `WITH FIELDS` where we set up the name, data type and modifiers of each field. The name equals to the attribute of our entity class. Data type correspond to the Java classes with the following exceptions:

  * You have to use allways the class. Primitive types are not allowed. So use `Integer` instead of `int`, `Float` instead of `float` and so on.
  * As you see in `createUser` String can have a limit. In our example the limit is set to max. 50 characters and it will create a field of type `VARCHAR(50)` in the database. If you don't set a limit it will use `TEXT` instead.
  * Temporal types are destinguished between `Date` (only date), `Time` (only time) and `DateTime` (a complete timestamp).

After all this preparations we are finally ready to start with our first real model: 

    DEFINE MODEL Contact
    INHERITS FROM DataItem
    WITH FIELDS
      gender Gender,
      firstname String(100) REQUIRED,
      lastname String (100) REQUIRED,
      birthday Date;

Curious about the result? Let's compile first the `addressbook.model` project and then the `addressbook.application` project! Right-click on your project in your IDE and choose "Clean and build" or open both project folders in an terminal and run `mvn clean install` in each one of them.

If everything worked out well, you get no compiler errors and see two new things in your project tree:

  * There are new packages in `/src/main/java/com/example/addressbook` containing classes for `dao`, `filter`, `model`... In NetBeans these classes appear in the subtree "Source Packages". These classes are only created once and can be edited by you to change the behaviour of your application.
  * There are a lot of classes named `Abstract*` in the folder `/target/generated-sources/xprs/com/example/addressbook`. NetBeans shows them in the subtree "Generated Sources (xprs)". These classes are deleted and recreated on each "Clean and Build". Changes you made to this classes will always be lost when you run the compile process. So don't touch them ;)


## Relations between models

Every contact should now have various adresses and telephone numbers. In Java you would do something like this:

    public class Contact {

      ...
      private List<Address> addresses;
      private List<TelephoneNumber> telephoneNumbers;
      ...

    }

This solution has one disadvantage: if you make a list of contacts you grab the whole database. So we reference the relation from the other side - which by the way is the only possibility supported by xprs. Because I promised an example with inheritence we create a model `Medium` that hosts the reference. Afterwards we create `TelephoneNumber` and `Address` to store the _real_ data.

    DEFINE MODEL Medium
    INHERIT FROM DataItem
    WITH FIELDS
      contact Contact;

    DEFINE MODEL TelephoneNumber
    INHERIT FROM Medium
    WITH FIELDS
      number String(100) MATCHES("\+?[0-9 \-\(\)]*");

    DEFINE MODEL Address
    INHERIT FROM Medium
    WITH FIELDS
      street String(200),
      zipCode String(10),
      city String(100),
      country String(2);

As you have noticed, `Medium` has no keyword `ABSTRACT` like in the definition of `DataItem`. It is possible to make `Medium` abstract, but then the default generators will not share the same database table for address and telephone number. So the access performance might slow down slightly. But in small applications you won't notice the difference.


## Cascading deletes

One basic problem in database applications is to resolve foreign key relations then related entities are deleted. In our example whe have to define, what happens if a `Contact` will be deleted. There are two options:

  * we can set the `Medium`'s contact attribute to `NULL` or
  * we delete the `Medium` as well.

In our case it would be useless to have a phone number or an address without the information to whom it belongs to. So we will tell the generators to delete them. This can be done by modifying the definition of our `Contact`:

    DEFINE MODEL Contact
    INHERITS FROM DataItem
    WITH FIELDS
      gender Gender,
      firstname String(100) REQUIRED,
      lastname String (100) REQUIRED,
      birthday Date
    ON DELETE
      IN Medium DELETE;


## Organize your models

If your project grows, your `Project.xprsm` file will grow very long. With xprs you can group your models in seperate files. For example you could create a new file called `Addressbook.xprsm` and fill it with models. Don't forget header, when doing so:

    PROJECT "addressbook";
    BASEPACKAGE "com.example.addressbook";

    ## PUT YOUR MODELS HERE!

In your `Project.xprsm` you can import your `Addressbook.xprsm` with the `IMPORT` keyword.

    PROJECT "addressbook";
    BASEPACKAGE "com.example.addressbook";

    INCLUDE "com/example/addressbook/Addressbook.xprsm";

Reference the included xprs model file like you would reference a ressource in Java, that means replace the dots in the package name with slashes. And because you are smart, you noticed that it is possible to build and reuse model libraries with this mechanism, right? Refer to the [xprs model language reference](../reference/xprsm.html) to learn more about it.


## Configure your generators

During the setup of our target project we already copied the configuration of the generators to our `pom.xml`. Let's have a short look, what it does:

    <configuration>
      ...
      <generators>
        <generator>rocks.xprs.generators.db.ModelGenerator</generator>
        <generator>rocks.xprs.generators.db.FilterGenerator</generator>
        <generator>rocks.xprs.generators.db.ValidatorGenerator</generator>
        <generator>rocks.xprs.generators.db.DaoGenerator</generator>
        <generator>rocks.xprs.generators.db.OrmXmlGenerator</generator>
        <generator>rocks.xprs.generators.gui.EditModuleGenerator</generator>
      </generators>
      ...
    </configuration>

As you might have noticed, the values between the `<generator>` tags are canonical names, that means package names followed by the class name. Each of this classes implements the rocks.xprs.creator.generator.Generator interface. So xprs will create a new instance of each class when it starts up and calls the `init()` method. Then reads your xprs models and runs the `generate()` method on each one of it. After all models have been processed, it runs the `close()` method. There is no way to run a Generator only on specific models. It's all or nothing.

If you wonder, where these classes have been added to the class path, have a look down. You'll find the plugin dependency

    <dependency>
      <groupId>rocks.xprs</groupId>
      <artifactId>generators</artifactId>
      <version>0.1.0</version>
    </dependency>

This is how you can add own generators to your project. Just create a new project, add classes that implement the Generator interface and add them as a dependency to the plugin dependencies. The basic generators use Freemarker to make the templating easier. But you can also use any other templating engine or even output files through streams. How to create own generators? [Find it out here!](generators.html)


## What you have learned

This is the end of our small introduction. You learned how to set up projects with xprs and you are able to write simple models. You know how to generate the code (by clean and build both projects) and you know how to configure generators. We suggest, that you play around with this application to get a deeper understanding about what xprs can do for you. The [getting started section](index.html) provides you more tutorials to dive deeper into xprs.