Startseite
Was ist xprs? Konzept/Idee, Anwendungsfall.

First Steps
* Basic Project/Archetype
* Introduction xprs Modeling Language
  * Simple Entities
  * Listen
  * n-zu-m Relationen
  * Generierung
  * eigene Generatoren
  * mehr Struktur durch imports
  * separation SDK/webservice
  
  
Technische Doku
  * Grundarchitektur: Teilung in Model, Generaoren, Produkt
  * Idee und Sprachreferenz xprsm
  * Projektübersicht + Interfaces
  * Einstellungen Maven-Plugin
  * Generators


# Welcome to xprs

xprs makes it easier to create database applications. It uses an SQL-like language to describe the structure of your data. Generators transform this information to model files, DAOs (data access objects), REST webservices/clients and even simple user interfaces.
xprs is a plugin for Apache Maven and can be easily integrated into Continous Integration (CI) processes, for example using the Jenkins Build Server.


## What is it for?

When developing database applications, sometimes it is not possible to use inheritence to solve problems that are equal but not the same. Imagine the following sample application: You want to create a REST webservice with Java EE and JAX-RS to read and write data from your database. You use simple Java classes to describe your data structures and other classes (DAOs) to read and write data to your database. There are two classes: Customer and Invoice. Up to this point, you can use inheritance to save and retrieve data from your database. But if you want to filter customers by their names or revenues and invoices by their status (payed, open), you have to write the filter code on your own. There are lots of tasks in database development that are not difficult to solve but take time to implement, because you have to code it by hand like

  * web service endpoints
  * web forms (with error messages and localization)
  * converting post request data to objects
  * validation

It doesn't matter if you generate Java, HTML, JavaScript. It's completely up to you. You can even generate code in any other progamming language you like, for example Python, Ruby, Scala, C/C++. The only limitation is the building process. xprs relies on Maven to look up modeling projects and generators. It's not possible at the moment to use xprs without Maven.


## How does it work?

In short, you have to create two Maven projects: one for your xprs model files and one for your target project. In the modeling project you open a text file and write your database schema in a SQL like language. In your target project you add the xprs creator plugin to your pom.xml and reference your modeling project. Then you configure your generators.Use our sample generators or create your own ones. You have full control over the generated code. Use `mvn clean install` to compile first your modeling project and afterwards again to compile your target project. If you like, create a parent project to compile all in one step. Now you can start modifing and extending the generated application.

[Start the guided tour and learn more!](getting-started/index.html)


## Benefits

  * accelerate your development and get rid of boring tasks
  * increase code quality by eleminating copy-paste-errors
  * make changes to your architecture in a central place and distribute them to varias projects
  * full control over generated code
  * override or extend your generated code
  * only depends on Maven, no need to install node.js, Yeoman or other tools
  * get all the benefits of using Maven:
    * organize your build artifacts in a central repository
    * integrate with CI servers like Jenkins
    * create archetypes to bootstrap new projects easily