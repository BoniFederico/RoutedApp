# RoutedApp
Web application for web programming course in university of Trieste

------------- DESCRIPTION --------------------------

In the doc.pdf file there is a brief description of the project and its functions.

--------------- DEMO ---------------------------

At the link https://www.youtube.com/watch?v=2wluhOgK98s there is a demo video of the application.

--------------- CODE -----------------------------

In the backend folder there is the api project (code: backend \ routed \ src \ main \ java \ com \ federicoboni)
In the frontend folder there is the Vue.js project (code: frontend \ routed \ src)


------------ INSTALLATION -----------------------

1. It is necessary to use MySql. The tables are backed up in the db_backup folder. They have to be saved in a database called "routed".
   The db server must be listening on localhost: 3306. For the root user in mysql, during the configuration, choose the root password "root".
2. The environment variables required are CATALINA_HOME = <tomcat installation path> and JAVA_HOME = <jdk installation path>.
3. In the "server" folder there is the installation of TomCat 9.0.56 for Windows 64bit. It already contains the webapp
   accessible to "localhost:8080/routedapp". The server can be started from bin\startup.bat.




--------------- DEPLOY -------------------------------

If you want to deploy webapp and api on another instance of TomCat, in the deploy folder there are:
the api deployment war file, the dist folder containing the build of the Vue.js project.

For the correct functioning of routing during the refresh of pages it is also necessary to:

- Add the following valve in TomCat's conf\server.xml file:
 <Host> <Valve className = "org.apache.catalina.valves.rewrite.RewriteValve" /> </Host>

- Add the file conf\Catalina\localhost\rewrite.config with the following content:
RewriteCond% {REQUEST_PATH}! -F
RewriteRule ^ /routedapp/(.*)/routedapp/


To change the project configurations:
-In the database\manager\HibernateManager.java file of the backend project there is the configuration for accessing the MySql database.
- In the Constants \ Constants.js file of the frontend project you will find the URLs for the api. The base url where the application tries to contact the api is "http://localhost:8080/routed/api"
