<style>
code, .code {font-size: 9pt; font-family: Courier, Courier New, monospace; color:#007000;}
.highlight {background-color: #ffc;}
.strike {text-decoration:line-through; color:red;}
.header {margin-top: 1.5ex;}
.details {margin-top: 1ex;}
</style>

<p>
At this point, you've created the initial implementation of the StockWatcher application, simulating stock data in the client-side code.
</p>
<p>
In this section, you'll deploy this application on <a href="//developers.google.com/appengine">Google App Engine</a>.  Also, you'll learn about some of the App Engine service APIs and use them to personalize the StockWatcher application so that users can log into their Google Account and retrieve their list of stocks.
</p>

<ol>
  <li><a href="#intro">Get started with App Engine</a></li>
  <li><a href="#deploy">Deploy the application to App Engine</a></li>
  <li><a href="#user">Personalize the application with the User Service</a></li>
  <li><a href="#data">Store data in the datastore</a></li>
</ol>

<p class="note" style="margin-left: 1.2em; margin-right: 1.5em;">
<b>Note:</b> For a broader guide to deploying, see <a href="../DevGuideDeploying.html">Deploy a GWT Application</a>.
</p>

<p>
This tutorial builds on the GWT concepts and the StockWatcher application created in the <a href="gettingstarted.html">Build a Sample GWT Application</a> tutorial.  It also uses techniques covered in the <a href="RPC.html">GWT RPC</a> tutorial.  If you have not completed these tutorials and are familiar with basic GWT concepts, you can import the StockWatcher project as coded to this point, as instructed below.
</p>

<h2 id="intro">1. Get started with App Engine</h2>

<h3>Sign up for an App Engine account</h3>

<p>
<a href="https://appengine.google.com">Sign up</a> for an App Engine account.  After your account is activated, sign in and create an application.  Make a note of the application ID you choose because you will need this information when you configure the StockWatcher project.  After you've finished with this tutorial you will be able to reuse this application ID for other applications.
</p>

<h3>Download the App Engine SDK</h3>

<p>
If you plan to use Eclipse, you can download the App Engine SDK with the <a href="//developers.google.com/appengine/docs/java/tools/eclipse">Google Plugin for Eclipse</a>. Or <a href="//developers.google.com/appengine/downloads">download</a> the App Engine SDK for Java separately.
</ap>

<h3>Set up a project</h3>

<h4>Set up a project (with Eclipse)</h4>

<p>
If you initially created your StockWatcher Eclipse project using the Google Plugin for Eclipse with both GWT and Google App Engine enabled, your project is already ready to run on App Engine.  If not:
<ol>
  <li>If you haven't yet, install the <a href="//developers.google.com/appengine/docs/java/tools/eclipse">Google Plugin for Eclipse</a> with both GWT and App Engine SDK and restart Eclipse.</li>
  <li>Complete the <a href="gettingstarted.html">Build a Sample GWT Application</a> tutorial, making sure to create a project with both GWT and Google App Engine enabled.  Alternatively, if you would like to skip the Build a Sample GWT Application tutorial, then <a href="http://code.google.com/p/google-web-toolkit/downloads/detail?name=Tutorial-GettingStartedAppEngine-2.1.zip">download</a>, unzip and import the StockWatcher Eclipse project.  To import the project:
    <ol>
      <li>In the File menu, select the Import... menu option.</li>
      <li>Select the import source General &gt; Existing Projects into Workspace.  Click the Next button.</li>
      <li>At "Select root directory", browse to and select the StockWatcher directory (from the unzipped file).  Click the Finish button.</li>
      <li>Add the Google Web Toolkit and App Engine functionality to your newly created project (right-click on your project > Google > Web Toolkit / App Engine Settings...). This will add Google Plugin functionality to your project as well as copy required libraries to your project <code>WEB-INF/lib</code> directory automatically.</li>
    </ol>
  </li>
</ol>
</p>

<h4>Set up a project (without Eclipse)</h4>

<p>
<ol>
  <li>If you haven't yet, download the <a href="//developers.google.com/appengine/downloads">App Engine SDK</a> for Java.</li>
  <li>Complete the <a href="gettingstarted.html">Build a Sample GWT Application</a> tutorial,  using webAppCreator to create a GWT application.  Alternatively, If you would like to skip the Build a Sample GWT Application tutorial, then download and unzip <a href="http://code.google.com/p/google-web-toolkit/downloads/detail?name=Tutorial-GettingStarted-2.1.zip">this file</a>.  Edit the gwt.sdk property in the StockWatcher/build.xml, then proceed with the modifications below.</li>
  <li>App Engine requires its own web application deployment descriptor.  Create a file StockWatcher/war/WEB-INF/appengine-web.xml with these contents:
<pre class="code">
&lt;?xml version="1.0" encoding="utf-8"?&gt;
&lt;appengine-web-app xmlns="http://appengine.google.com/ns/1.0"&gt;
  &lt;application&gt;<span class="highlight">&lt;!-- Your App Engine application ID goes here --&gt;</span>&lt;/application&gt;
  &lt;version&gt;1&lt;/version&gt;
&lt;/appengine-web-app&gt;
</pre>
Substitute your App Engine application ID on the second line.  Read more about <a href="//developers.google.com/appengine/docs/java/config/appconfig">appengine-web.xml</a>.
  </li>
  <li>As we will be using <a href="//developers.google.com/appengine/docs/java/gettingstarted/usingdatastore">Java Data Objects (JDO)</a> later for storing data, create a file StockWatcher/src/META-INF/jdoconfig.xml with these contents:
<pre class="code">
&lt;?xml version="1.0" encoding="utf-8"?&gt;
&lt;jdoconfig xmlns="http://java.sun.com/xml/ns/jdo/jdoconfig"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="http://java.sun.com/xml/ns/jdo/jdoconfig"&gt;
  &lt;persistence-manager-factory name="<span class="highlight">transactions-optional</span>"&gt;
    &lt;property name="javax.jdo.PersistenceManagerFactoryClass" value="org.datanucleus.store.appengine.jdo.DatastoreJDOPersistenceManagerFactory"/&gt;
    &lt;property name="javax.jdo.option.ConnectionURL" value="appengine"/&gt;
    &lt;property name="javax.jdo.option.NontransactionalRead" value="true"/&gt;
    &lt;property name="javax.jdo.option.NontransactionalWrite" value="true"/&gt;
    &lt;property name="javax.jdo.option.RetainValues" value="true"/&gt;
    &lt;property name="datanucleus.appengine.autoCreateDatastoreTxns" value="true"/&gt;
  &lt;/persistence-manager-factory&gt;
&lt;/jdoconfig&gt;
</pre>
You will refrence this configuration later by its name "transactions-optional".  Read more about <a href="//developers.google.com/appengine/docs/java/datastore/usingjdo">jdoconfig.xml</a>.
  </li>
  <li>The GWT ant build file needs to be modified to support DataNucleus JDO compilation and use of the App Engine development server.  Edit StockWatcher/build.xml and add the following:
    <ol>
      <li>Add a property for the App Engine SDK directory.
<pre class="code">
  &lt;!-- Configure path to GWT SDK --&gt;
  &lt;property name="gwt.sdk" location="<i>Path to GWT</i>" /&gt;
<span class="highlight">  &lt;!-- Configure path to App Engine SDK --&gt;
  &lt;property name="appengine.sdk" location="<i>Path to App Engine SDK</i>" /&gt;</span>
</pre>
      </li>
      <li>Add a property for a App Engine tools class path.
<pre class="code">
  &lt;path id="project.class.path"&gt;
    &lt;pathelement location="war/WEB-INF/classes"/&gt;
    &lt;pathelement location="${gwt.sdk}/gwt-user.jar"/&gt;
    &lt;fileset dir="${gwt.sdk}" includes="gwt-dev*.jar"/&gt;
    &lt;!-- Add any additional non-server libs (such as JUnit) --&gt;
    &lt;fileset dir="war/WEB-INF/lib" includes="**/*.jar"/&gt;
  &lt;/path&gt;

<span class="highlight">  &lt;path id="tools.class.path"&gt;
    &lt;path refid="project.class.path"/&gt;
    &lt;pathelement location="${appengine.sdk}/lib/appengine-tools-api.jar"/&gt;
    &lt;fileset dir="${appengine.sdk}/lib/tools"&gt;
      &lt;include name="**/asm-*.jar"/&gt;
      &lt;include name="**/datanucleus-enhancer-*.jar"/&gt;
    &lt;/fileset&gt;
  &lt;/path&gt;</span>
</pre>
      </li>
      <li>Modify the "libs" ant target so that the required jar files are copied to WEB-INF/lib.
<pre class="code">
  &lt;target name="libs" description="Copy libs to WEB-INF/lib"&gt;
    &lt;mkdir dir="war/WEB-INF/lib" /&gt;
    &lt;copy todir="war/WEB-INF/lib" file="${gwt.sdk}/gwt-servlet.jar" /&gt;
    &lt;!-- Add any additional server libs that need to be copied --&gt;
<span class="highlight">    &lt;copy todir="war/WEB-INF/lib" flatten="true"&gt;
      &lt;fileset dir="${appengine.sdk}/lib/user" includes="**/*.jar"/&gt;
    &lt;/copy&gt;</span>
  &lt;/target&gt;
</pre>
      </li>
      <li>JDO is implemented with DataNucleus Java byte-code enhancement.  Modify the "javac" ant target to add byte-code enhancement.
<pre class="code">
  &lt;target name="javac" depends="libs" description="Compile java source"&gt;
    &lt;mkdir dir="war/WEB-INF/classes"/&gt;
    &lt;javac srcdir="src" includes="**" encoding="utf-8"
        destdir="war/WEB-INF/classes"
        source="1.5" target="1.5" nowarn="true"
        debug="true" debuglevel="lines,vars,source"&gt;
      &lt;classpath refid="project.class.path"/&gt;
    &lt;/javac&gt;
    &lt;copy todir="war/WEB-INF/classes"&gt;
      &lt;fileset dir="src" excludes="**/*.java"/&gt;
    &lt;/copy&gt;
<span class="highlight">    &lt;taskdef
       name="datanucleusenhancer"
       classpathref="tools.class.path"
       classname="org.datanucleus.enhancer.tools.EnhancerTask" /&gt;
    &lt;datanucleusenhancer
       classpathref="tools.class.path"
       failonerror="true"&gt;
      &lt;fileset dir="war/WEB-INF/classes" includes="**/*.class" /&gt;
    &lt;/datanucleusenhancer&gt;</span>
  &lt;/target&gt;
</pre>
      </li>
      <li>Modify the "devmode" ant target to use the App Engine development server instead of the servlet container which comes with GWT.
<pre class="code">
  &lt;target name="devmode" depends="javac" description="Run development mode""&gt;
    &lt;java failonerror="true" fork="true" classname="com.google.gwt.dev.DevMode""&gt;
      &lt;classpath&gt;
        &lt;pathelement location="src"/&gt;
        &lt;path refid="project.class.path"/&gt;
<span class="highlight">        &lt;path refid="tools.class.path"/&gt;</span>
      &lt;/classpath&gt;
      &lt;jvmarg value="-Xmx256M"/&gt;
      &lt;arg value="-startupUrl"/&gt;
      &lt;arg value="StockWatcher.html"/&gt;
      &lt;!-- Additional arguments like -style PRETTY or -logLevel DEBUG --&gt;
<span class="highlight">      &lt;arg value="-server"/&gt;
      &lt;arg value="com.google.appengine.tools.development.gwt.AppEngineLauncher"/&gt;</span>
      &lt;arg value="com.google.gwt.sample.stockwatcher.StockWatcher"/&gt;
    &lt;/java&gt;
  &lt;/target&gt;
</pre>
      </li>
    </ol>
  </li>
</ol>
</p>

<a name="test"></a>
<h3>Test locally</h3>

<p>
We will run the application in GWT development mode to verify the project was set up successfully.  However, instead of using the servlet container which comes with GWT, the application will run in the App Engine development server, the servlet container which comes with the App Engine SDK.  What's the difference?  The App Engine development server is configured to mimic the App Engine production environment.
</p>

<h4>Run the application in development mode (with Eclipse)</h4>

<p>
<ol>
  <li>In the Package Explorer view, select the StockWatcher project.</li>
  <li>In the toolbar, click the Run button (Run as Web Application).</li>
</ol>
</p>

<h4>Run the application in development mode (without Eclipse)</h4>

<p>
<ol>
  <li>From the command-line, change to the StockWatcher directory.</li>
  <li>Execute: <pre class="code">ant devmode</pre></li>
</ol>
</p>

<h2 id="deploy">2. Deploy the application to App Engine</h2>

<p>
Now that we've verified the StockWatcher project is running locally in GWT development mode and with the App Engine development server, we can run the application on App Engine.
</p>

<h4>Deploy the application to App Engine (with Eclipse)</h4>

<p>
<ol>
  <li>In the Package Explorer view, select the StockWatcher project.</li>
  <li>In the toolbar, click the Deploy App Engine Project button <img src="images/DeployAppEngineProject.png" alt="icon" style="vertical-align: -4px;"/>.</li>
  <li>(First time only) Click the "App Engine project settings..." link to specify your application ID.  Click the OK button when you're finished.</li>
  <li>Enter your Google Accounts email and password.  Click the Deploy button.  You can watch the deployment progress in the Eclipse Console.</li>
</ol>
</p>

<h4>Deploy the application to App Engine (without Eclipse)</h4>

<p>
<ol>
  <li>From the command-line, change to the StockWatcher directory.</li>
  <li>
Compile the application by executing:

<pre class="code">ant build</pre>

<p class="note" style="margin-top: 12px margin-bottom: 4px;;">
<b>Tip:</b> Add the ant bin directory to your environment PATH to avoid having to specify the full path to ant.
</p>
  </li>
  <li>
<p>
appcfg is a command-line tool which comes with the App Engine SDKs.  Upload the application by executing: <pre class="code">appcfg.sh update war</pre>
</p>

<p>
From the Windows command prompt, the command is <code>appcfg update war</code>.  The first parameter is the action to perform.  The second parameter is the directory with the update, which in this case is a relative directory containing the static files and output from the GWT compiler.  Enter your Google Accounts email and password when prompted.
</p>
<p class="note">
<b>Tip:</b> Add the App Engine SDK bin directory to your environment PATH to avoid having to specify the full path to appcfg.sh.
</p>
  </li>
</ol>
</p>

<h3>Test on App Engine</h3>

<p>
Test your uploaded application by opening a web browser to http://<i>application-id</i>.appspot.com/ where <i>application-id</i> is the App Engine application ID that you created earlier.  The StockWatcher application is now running on App Engine under your application ID.
</p>

<h2 id="user">3. Personalize the application with the User Service</h2>

<h3>Overview</h3>

<p>
Now that the StockWatcher is deployed on App Engine, we can start using some of the available services to enrich the application. We'll start by persisting stock quote listings on a per user basis. This is possible due to the datastore service, which allows us to save application data, as well as the User Service, which allows us to have users login and save stock quote listings for each user. For persistence, we'll use the Java Data Objects (JDO) interface provided by the App Engine SDK.
</p>

<p>
To implement login functionality we'll use the User Serivce. With this service in place, any user with a Google Account will be able to login using their account to access the StockWatcher application. In this section, you'll use the App Engine User API to add user login to the application.
</p>

<p>
The App Engine User Service is very easy to use. First, you need to instantiate the UserService class, as shown in the code snippet below:
<pre class="code">
      UserService userService = UserServiceFactory.getUserService();
</pre>
</p>

<p>
Next, you need to get the current user who is accessing the StockWatcher application:
<pre class="code">
     User user = userService.getCurrentUser();
</pre>
</p>

<p>
The UserService returns an instantiated User object if the current user who is accessing the application is logged into their Google Account. The User object contains useful information such as the email address associated with the account, as well as the account nickname. If the person accessing the application is not logged into their account, or doesn't have a Google Account, the returned User object will be null. In this case we have a number of options available to us in how we want to handle the situation, but for the purposes of the StockWatcher application, we will point the user to a login URL where they will be able to log into their Google Account.
</p>

<p>
The User API offers an easy way to generate the login URL. Simply calling the UserService <code>createLoginURL(String requestUri)</code> method, which gives you the redirect login URL to send the user to the Google Account login screen. Once they log in, the App Engine container will know where to redirect the user based on the <code>requestUri</code> that you provide when making the <code>createLoginURL()</code> call.
</p>

<h3>Define the Login RPC service</h3>

<p>
To make this more concrete, let's create a login RPC service for the StockWatcher application. If you're not familiar with GWT RPC, see the previous <a href="RPC.html">tutorial</a>.
</p>

<p>
First, create the LoginInfo object which will contain the login info from the User service.
</p>

<h4>LoginInfo.java:</h4>
<p>
<pre class="code">
package com.google.gwt.sample.stockwatcher.client;

import java.io.Serializable;

public class LoginInfo implements Serializable {

  private boolean loggedIn = false;
  private String loginUrl;
  private String logoutUrl;
  private String emailAddress;
  private String nickname;

  public boolean isLoggedIn() {
    return loggedIn;
  }

  public void setLoggedIn(boolean loggedIn) {
    this.loggedIn = loggedIn;
  }

  public String getLoginUrl() {
    return loginUrl;
  }

  public void setLoginUrl(String loginUrl) {
    this.loginUrl = loginUrl;
  }

  public String getLogoutUrl() {
    return logoutUrl;
  }

  public void setLogoutUrl(String logoutUrl) {
    this.logoutUrl = logoutUrl;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }
}
</pre>
</p>
<p>
LoginInfo is serializable since it is the return type of an RPC method.
</p>

<p>
Next, create the LoginService and LoginServiceAsync interfaces.
</p>

<h4>LoginService.java:</h4>

<p>
<pre class="code">
package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("login")
public interface LoginService extends RemoteService {
  public LoginInfo login(String requestUri);
}
</pre>
</p>

<p>
The path annotation "login" will be configured below.
</p>

<h4>LoginServiceAsync.java:</h4>

<p>
<pre class="code">
package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LoginServiceAsync {
  public void login(String requestUri, AsyncCallback&lt;LoginInfo&gt; async);
}
</pre>
</p>

<p>
Create the LoginServiceImpl class in the com.google.gwt.sample.stockwatcher.server package as follows:
</p>

<h4>LoginServiceImpl.java:</h4>

<p>
<pre class="code">
package com.google.gwt.sample.stockwatcher.server;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.sample.stockwatcher.client.LoginInfo;
import com.google.gwt.sample.stockwatcher.client.LoginService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class LoginServiceImpl extends RemoteServiceServlet implements
    LoginService {

  public LoginInfo login(String requestUri) {
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    LoginInfo loginInfo = new LoginInfo();

    if (user != null) {
      loginInfo.setLoggedIn(true);
      loginInfo.setEmailAddress(user.getEmail());
      loginInfo.setNickname(user.getNickname());
      loginInfo.setLogoutUrl(userService.createLogoutURL(requestUri));
    } else {
      loginInfo.setLoggedIn(false);
      loginInfo.setLoginUrl(userService.createLoginURL(requestUri));
    }
    return loginInfo;
  }

}
</pre>
</p>

<p>
Lastly, configure the servlet in your web.xml file.  The mapping is composed of the rename-to attribute in the GWT module definition (stockwatcher) and the RemoteServiceRelativePath annotation(login).  Also, because the greetServlet is not needed for this application, its configuration can be deleted.
</p>

<h4>web.xml:</h4>

<p>
<pre class="code">
&lt;?xml version="1.0" encoding="UTF-8"?&gt;

&lt;web-app&gt;

  &lt;!-- Default page to serve --&gt;
  &lt;welcome-file-list&gt;
    &lt;welcome-file&gt;StockWatcher.html&lt;/welcome-file&gt;
  &lt;/welcome-file-list&gt;

  &lt;!-- Servlets --&gt;
<span class="highlight">  &lt;servlet&gt;
    &lt;servlet-name&gt;loginService&lt;/servlet-name&gt;
    &lt;servlet-class&gt;com.google.gwt.sample.stockwatcher.server.LoginServiceImpl&lt;/servlet-class&gt;
  &lt;/servlet&gt;</span>

<span class="highlight">  &lt;servlet-mapping&gt;
    &lt;servlet-name&gt;loginService&lt;/servlet-name&gt;
    &lt;url-pattern&gt;/stockwatcher/login&lt;/url-pattern&gt;
  &lt;/servlet-mapping&gt;</span>

&lt;/web-app&gt;
</pre>
</p>

<h3>Update the StockWatcher UI</h3>

<p>
Now that the login RPC service is in place, the last thing to do is to make the call to the service from the StockWatcher entry point class. However, we must consider how the application flow changes now that we've added login functionality. In the previous version of the application, you could load the StockWatcher unconditionally because it didn't require any login. Now that we are requiring user login, we have to change the loading logic a bit.
</p>

<p>
For one, if the user is already logged in, the application can proceed and load the StockWatcher. If, however, the user is not logged in, we have to redirect them to the login page. Once logged in, they will be redirected back to the StockWatcher host page where we'll need to check once more that they have indeed been authenticated. If the authentication check passes, then we can load the stock watcher.
</p>

<p>
The key thing to notice is that loading the stock watcher is contingent on the result of the login. This means the logic that loads the StockWatcher must be called only once login has passed. This will require a bit of refactoring. If you're using Eclipse, this will be easy to do. Simply select the code in the StockWatcher <code>onModuleLoad()</code> method, select the "Refactor" menu, and click on the "Extract Method..." function. From there you can declare the extracted method something suitable, like <code>private void loadStockWatcher()</code>.
</p>

<p>
You should end up with something similar to the following:
</p>

<h4>StockWatcher.java:</h4>

<p>
<pre class="code">
  public void onModuleLoad() {
<span class="highlight">    loadStockWatcher();
  }</span>

<span class="highlight">  private void loadStockWatcher() {</span>
    // Create table for stock data.
    stocksFlexTable.setText(0, 0, "Symbol");
    stocksFlexTable.setText(0, 1, "Price");
    stocksFlexTable.setText(0, 2, "Change");
    stocksFlexTable.setText(0, 3, "Remove");
    ...
  }
</pre>
</p>

<p>
Now that you've refactored the StockWatcher loading logic to a callable method, we can make the login RPC service call in the <code>onModuleLoad()</code> method and call the <code>loadStockWatcher()</code> method when login passes. However, if the user isn't logged in, you'll need to give them some kind of indication that they need to log in to proceed. For this, it makes sense to use a Login panel along with accompanying label and button to instruct the user to proceed to login.
</p>

<p>
Considering all of these, you should add something similar to the following to your StockWatcher entry point class:
</p>

<h4>StockWatcher.java</h4>
<p>
<pre class="code">
<span class="highlight">import com.google.gwt.user.client.ui.Anchor;</span>

...

<span class="highlight">  private LoginInfo loginInfo = null;
  private VerticalPanel loginPanel = new VerticalPanel();
  private Label loginLabel = new Label(
      "Please sign in to your Google Account to access the StockWatcher application.");
  private Anchor signInLink = new Anchor("Sign In");</span>

  public void onModuleLoad() {
<span class="highlight">    // Check login status using login service.
    LoginServiceAsync loginService = GWT.create(LoginService.class);
    loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback&lt;LoginInfo&gt;() {
      public void onFailure(Throwable error) {
      }

      public void onSuccess(LoginInfo result) {
        loginInfo = result;
        if(loginInfo.isLoggedIn()) {</span>
          loadStockWatcher();
<span class="highlight">        } else {
          loadLogin();
        }
      }
    });</span>
  }

<span class="highlight">  private void loadLogin() {
    // Assemble login panel.
    signInLink.setHref(loginInfo.getLoginUrl());
    loginPanel.add(loginLabel);
    loginPanel.add(signInLink);
    RootPanel.get("stockList").add(loginPanel);
  }</span>
</pre>
</p>

<p>
Another important point about login functionality is the ability to sign out of the application. This is something you should add to the StockWatcher application as well. Fortunately, the User Service provides us with a logout URL through a similar call as the createLoginURL(String requestUri) method. We can add this to the StockWatcher sample application by adding the following snippets:
</p>

<h4>StockWatcher.java</h4>

<p>
<pre class="code">
  private Anchor signInLink = new Anchor("Sign In");
<span class="highlight">  private Anchor signOutLink = new Anchor("Sign Out");</span>

...

  private void loadStockWatcher() {
<span class="highlight">    // Set up sign out hyperlink.
    signOutLink.setHref(loginInfo.getLogoutUrl());</span>

    // Create table for stock data.
    stocksFlexTable.setText(0, 0, "Symbol");
    stocksFlexTable.setText(0, 1, "Price");
    stocksFlexTable.setText(0, 2, "Change");
    stocksFlexTable.setText(0, 3, "Remove");

  ...

    // Assemble Main panel.
<span class="highlight">    mainPanel.add(signOutLink);</span>
    mainPanel.add(stocksFlexTable);
    mainPanel.add(addPanel);
    mainPanel.add(lastUpdatedLabel);
</pre>
</p>

<h3>Test User Service features</h3>

<p>
You can repeat the instructions above to run the application <a href="#test">locally</a> or on <a href="#deploy">App Engine</a>.
</p>

<p>
If you run the application in development mode with the App Engine development server, the sign in page will allow you to enter any email address (for ease in testing).  If you deploy your application to App Engine, the sign in page will require users to sign in to a Google Account in order to access the application.
</p>

<h2 id="data">4. Store data in the datastore</h2>

<h3>Overview</h3>

<p>
The datastore service available to the App Engine Java runtime is the same service available to the Python runtime.  To access this service in Java, you may use the low-level <a href="//developers.google.com/appengine/docs/java/javadoc/com/google/appengine/api/datastore/package-summary">datastore API</a>, <a href="//developers.google.com/appengine/docs/java/datastore/usingjdo">Java Data Objects (JDO)</a>, or <a href="//developers.google.com/appengine/docs/java/datastore/usingjpa">Java Persistence API (JPA)</a>.  For this sample we will use JDO.
</p>

<h3>Define the Stock RPC service</h3>

<p>
We will create a basic stock service to handle the persistence of users' stocks.  We will also expose this service as a GWT RPC service.
</p>

<h4>StockService.java</h4>

<p>
<pre class="code">
package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("stock")
public interface StockService extends RemoteService {
  public void addStock(String symbol) throws NotLoggedInException;
  public void removeStock(String symbol) throws NotLoggedInException;
  public String[] getStocks() throws NotLoggedInException;
}
</pre>
</p>

<h4>StockServiceAsync.java</h4>

<p>
<pre class="code">
package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface StockServiceAsync {
  public void addStock(String symbol, AsyncCallback&lt;Void&gt; async);
  public void removeStock(String symbol, AsyncCallback&lt;Void&gt; async);
  public void getStocks(AsyncCallback&lt;String[]&gt; async);
}
</pre>
</p>

<h4>StockWatcher.java</h4>

<p>
<pre class="code">
public class StockWatcher implements EntryPoint {

  private static final int REFRESH_INTERVAL = 5000; // ms
  private VerticalPanel mainPanel = new VerticalPanel();
  private FlexTable stocksFlexTable = new FlexTable();
  private HorizontalPanel addPanel = new HorizontalPanel();
  private TextBox newSymbolTextBox = new TextBox();
  private Button addStockButton = new Button("Add");
  private Label lastUpdatedLabel = new Label();
  private ArrayList<String> stocks = new ArrayList<String>();
  private LoginInfo loginInfo = null;
  private VerticalPanel loginPanel = new VerticalPanel();
  private Label loginLabel = new Label("Please sign in to your Google Account to access the StockWatcher application.");
  private Anchor signInLink = new Anchor("Sign In");
  <span class="highlight">private final StockServiceAsync stockService = GWT.create(StockService.class);</span>
</pre>
</p>

<p>
A checked exception will indicate that the user is not logged in yet.  Such a scenario is possible since a RPC call can be received by the stock service even if there is no current user.  The class is serializable so that it may be returned by the RPC call via the AsyncCallback <code>onFailure(Throwable error)</code> method.  You could also implement security with a servlet filter or Spring security.
</p>

<h4>NotLoggedInException.java</h4>

<p>
<pre class="code">
package com.google.gwt.sample.stockwatcher.client;

import java.io.Serializable;

public class NotLoggedInException extends Exception implements Serializable {

  public NotLoggedInException() {
    super();
  }

  public NotLoggedInException(String message) {
    super(message);
  }

}
</pre>
</p>

<p>
The Stock class is what is persisted with JDO.  The specifics of how it is persisted are dictated by the JDO annotations.  In particular:
  <ul>
    <li>The PersistenceCapable annotation tells the DataNucleus byte-code enhancer to process this class.</li>
    <li>The PrimaryKey annotation designates an <code>id</code> attribute for storing its primary key.</li>
    <li>In this class, every attribute is persisted.  However you can designate attributes as not being persisted with the NotPersistent annotation.
    <li>The User attribute is a special App Engine type which can allow you to identify users across email address changes.</li>
  </ul>
</p>

<h4>Stock.java</h4>

<p>
<pre class="code">
package com.google.gwt.sample.stockwatcher.server;

import java.util.Date;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.google.appengine.api.users.User;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Stock {

  @PrimaryKey
  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
  private Long id;
  @Persistent
  private User user;
  @Persistent
  private String symbol;
  @Persistent
  private Date createDate;

  public Stock() {
    this.createDate = new Date();
  }

  public Stock(User user, String symbol) {
    this();
    this.user = user;
    this.symbol = symbol;
  }

  public Long getId() {
    return this.id;
  }

  public User getUser() {
    return this.user;
  }

  public String getSymbol() {
    return this.symbol;
  }

  public Date getCreateDate() {
    return this.createDate;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }
}
</pre>
</p>

<p>
This class implements the stock service and includes calls to the JDO API for persisting stock data.  Things to notice:
  <ul>
    <li>The messages logged by the logger are viewable when you inspect your application in the <a href="https://appengine.google.com/">App Engine Administration Console</a>.</ol>
    <li>The PersistenceManagerFactory singleton is created from the properties named "transactions-optional" in jdoconfig.xml above.</li>
    <li>The checkedLoggedIn method is called whenever we want to make sure a user is logged in.</li>
    <li>The getUser method uses the UserService.</li>
  </ul>
</p>

<h4>StockServiceImpl.java</h4>

<p>
<pre class="code">
package com.google.gwt.sample.stockwatcher.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.sample.stockwatcher.client.NotLoggedInException;
import com.google.gwt.sample.stockwatcher.client.StockService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class StockServiceImpl extends RemoteServiceServlet implements
StockService {

  private static final Logger LOG = Logger.getLogger(StockServiceImpl.class.getName());
  private static final PersistenceManagerFactory PMF =
      JDOHelper.getPersistenceManagerFactory("transactions-optional");

  public void addStock(String symbol) throws NotLoggedInException {
    checkLoggedIn();
    PersistenceManager pm = getPersistenceManager();
    try {
      pm.makePersistent(new Stock(getUser(), symbol));
    } finally {
      pm.close();
    }
  }

  public void removeStock(String symbol) throws NotLoggedInException {
    checkLoggedIn();
    PersistenceManager pm = getPersistenceManager();
    try {
      long deleteCount = 0;
      Query q = pm.newQuery(Stock.class, "user == u");
      q.declareParameters("com.google.appengine.api.users.User u");
      List&lt;Stock&gt; stocks = (List&lt;Stock&gt;) q.execute(getUser());
      for (Stock stock : stocks) {
        if (symbol.equals(stock.getSymbol())) {
          deleteCount++;
          pm.deletePersistent(stock);
        }
      }
      if (deleteCount != 1) {
        LOG.log(Level.WARNING, "removeStock deleted "+deleteCount+" Stocks");
      }
    } finally {
      pm.close();
    }
  }

  public String[] getStocks() throws NotLoggedInException {
    checkLoggedIn();
    PersistenceManager pm = getPersistenceManager();
    List&lt;String&gt; symbols = new ArrayList&lt;String&gt;();
    try {
      Query q = pm.newQuery(Stock.class, "user == u");
      q.declareParameters("com.google.appengine.api.users.User u");
      q.setOrdering("createDate");
      List&lt;Stock&gt; stocks = (List&lt;Stock&gt;) q.execute(getUser());
      for (Stock stock : stocks) {
        symbols.add(stock.getSymbol());
      }
    } finally {
      pm.close();
    }
    return (String[]) symbols.toArray(new String[0]);
  }

  private void checkLoggedIn() throws NotLoggedInException {
    if (getUser() == null) {
      throw new NotLoggedInException("Not logged in.");
    }
  }

  private User getUser() {
    UserService userService = UserServiceFactory.getUserService();
    return userService.getCurrentUser();
  }

  private PersistenceManager getPersistenceManager() {
    return PMF.getPersistenceManager();
  }
}
</pre>
</p>

<p>
Now that the GWT RPC service is implemented, we'll make sure the servlet container knows about it.  The mapping /stockwatcher/stock is composed of the rename-to attribute in the GWT module definition (stockwatcher) and the RemoteServiceRelativePath annotation (stock).
</p>

<h4>web.xml</h4>

<p>
<pre class="code">
&lt;?xml version="1.0" encoding="UTF-8"?&gt;

&lt;web-app&gt;

  &lt;!-- Default page to serve --&gt;
  &lt;welcome-file-list&gt;
    &lt;welcome-file&gt;StockWatcher.html&lt;/welcome-file&gt;
  &lt;/welcome-file-list&gt;

  &lt;!-- Servlets --&gt;
  &lt;servlet&gt;
    &lt;servlet-name&gt;loginService&lt;/servlet-name&gt;
    &lt;servlet-class&gt;com.google.gwt.sample.stockwatcher.server.LoginServiceImpl&lt;/servlet-class&gt;
  &lt;/servlet&gt;

<span class="highlight">  &lt;servlet&gt;
    &lt;servlet-name&gt;stockService&lt;/servlet-name&gt;
    &lt;servlet-class&gt;com.google.gwt.sample.stockwatcher.server.StockServiceImpl&lt;/servlet-class&gt;
  &lt;/servlet&gt;</span>

  &lt;servlet-mapping&gt;
    &lt;servlet-name&gt;loginService&lt;/servlet-name&gt;
    &lt;url-pattern&gt;/stockwatcher/login&lt;/url-pattern&gt;
  &lt;/servlet-mapping&gt;

<span class="highlight">  &lt;servlet-mapping&gt;
    &lt;servlet-name&gt;stockService&lt;/servlet-name&gt;
    &lt;url-pattern&gt;/stockwatcher/stock&lt;/url-pattern&gt;
  &lt;/servlet-mapping&gt;</span>

&lt;/web-app&gt;
</pre>
</p>

<h3>Update the StockWatcher UI</h3>

<h4>Retrieving stocks</h4>

<p>
When the StockWatcher application loads, it should be prepopulated with the user's stocks.  In order to reuse the existing code which displays a stock, we will refactor StockWatcher <code>addStock()</code> so that the logic for displaying the new stock is in a new <code>displayStock(String symbol)</code> method.
</p>

<p>
<pre class="code">
  private void addStock() {
    final String symbol = newSymbolTextBox.getText().toUpperCase().trim();
    newSymbolTextBox.setFocus(true);

    // Stock code must be between 1 and 10 chars that are numbers, letters, or dots.
    if (!symbol.matches("^[0-9a-zA-Z\\.]{1,10}$")) {
      Window.alert("'" + symbol + "' is not a valid symbol.");
      newSymbolTextBox.selectAll();
      return;
    }

    newSymbolTextBox.setText("");

    // Don't add the stock if it's already in the table.
    if (stocks.contains(symbol))
      return;

<span class="highlight">    displayStock(symbol);
  }

  private void displayStock(final String symbol) {</span>
    // Add the stock to the table.
    int row = stocksFlexTable.getRowCount();
    stocks.add(symbol);
    stocksFlexTable.setText(row, 0, symbol);
    stocksFlexTable.setWidget(row, 2, new Label());
    stocksFlexTable.getCellFormatter().addStyleName(row, 1, "watchListNumericColumn");
    stocksFlexTable.getCellFormatter().addStyleName(row, 2, "watchListNumericColumn");
    stocksFlexTable.getCellFormatter().addStyleName(row, 3, "watchListRemoveColumn");

    // Add a button to remove this stock from the table.
    Button removeStockButton = new Button("x");
    removeStockButton.addStyleDependentName("remove");
    removeStockButton.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        int removedIndex = stocks.indexOf(symbol);
        stocks.remove(removedIndex);
        stocksFlexTable.removeRow(removedIndex + 1);
      }
    });
    stocksFlexTable.setWidget(row, 3, removeStockButton);

    // Get the stock price.
    refreshWatchList();

  }
</pre>
</p>

<p>
After the stock table is set up is an appropriate time to load the stocks.
</p>

<p>
<pre class="code">
  private void loadStockWatcher() {

  ...

    stocksFlexTable.setCellPadding(5);
    stocksFlexTable.addStyleName("watchList");
    stocksFlexTable.getRowFormatter().addStyleName(0, "watchListHeader");
    stocksFlexTable.getCellFormatter().addStyleName(0, 1, "watchListNumericColumn");
    stocksFlexTable.getCellFormatter().addStyleName(0, 2, "watchListNumericColumn");
    stocksFlexTable.getCellFormatter().addStyleName(0, 3, "watchListRemoveColumn");

<span class="highlight">    loadStocks();</span>

  ...

  }
</pre>
</p>

<p>
The <code>loadStocks()</code> method calls the StockService defined earlier.  The RPC returns an array of stock symbols, which are displayed individually using the method <code>displayStock(String symbol)</code>.
<p>
<pre class="code">
<span class="highlight">  private void loadStocks() {
    stockService.getStocks(new AsyncCallback&lt;String[]&gt;() {
      public void onFailure(Throwable error) {
      }
      public void onSuccess(String[] symbols) {
        displayStocks(symbols);
      }
    });
  }

  private void displayStocks(String[] symbols) {
    for (String symbol : symbols) {
      displayStock(symbol);
    }
  }</span>
</pre>
</p>

<h4>Adding stocks</h4>

<p>
Instead of just displaying stocks when they are added, we will call the StockService to save the new stock symbol to the datastore.
</p>

<p>
<pre class="code">
  private void addStock() {
    final String symbol = newSymbolTextBox.getText().toUpperCase().trim();
    newSymbolTextBox.setFocus(true);

    // Stock code must be between 1 and 10 chars that are numbers, letters, or dots.
    if (!symbol.matches("^[0-9a-zA-Z\\.]{1,10}$")) {
      Window.alert("'" + symbol + "' is not a valid symbol.");
      newSymbolTextBox.selectAll();
      return;
    }

    newSymbolTextBox.setText("");

    // Don't add the stock if it's already in the table.
    if (stocks.contains(symbol))
      return;

    <span class="strike">displayStock(symbol);</span>
<span class="highlight">    addStock(symbol);</span>
  }

<span class="highlight">  private void addStock(final String symbol) {
    stockService.addStock(symbol, new AsyncCallback&lt;Void&gt;() {
      public void onFailure(Throwable error) {
      }
      public void onSuccess(Void ignore) {
        displayStock(symbol);
      }
    });
  }</span>

  private void displayStock(final String symbol) {
    // Add the stock to the table.
    int row = stocksFlexTable.getRowCount();
    stocks.add(symbol);

...

  }
</pre>
</p>

<h4>Removing stocks</h4>

<p>
And instead of simply removing stocks from display, we will call the StockService to remove the stock symbol from the datastore.
</p>

<p>
<pre class="code">
  private void displayStock(final String symbol) {

  ...

    // Add a button to remove this stock from the table.
    Button removeStock = new Button("x");
    removeStock.addStyleDependentName("remove");

    removeStock.addClickHandler(new ClickHandler(){
      public void onClick(ClickEvent event) {
<span class="highlight">        removeStock(symbol);</span>
      }
    });
    stocksFlexTable.setWidget(row, 3, removeStock);

    // Get the stock price.
    refreshWatchList();

  }

<span class="highlight">  private void removeStock(final String symbol) {
    stockService.removeStock(symbol, new AsyncCallback&lt;Void&gt;() {
      public void onFailure(Throwable error) {
      }
      public void onSuccess(Void ignore) {
        undisplayStock(symbol);
      }
    });
  }

  private void undisplayStock(String symbol) {</span>
    int removedIndex = stocks.indexOf(symbol);
    stocks.remove(removedIndex);
    stocksFlexTable.removeRow(removedIndex+1);
<span class="highlight">  }</span>
</pre>
</p>

<h3>Error handling</h3>

<p>
When one of the RPC calls results in an error, we want to display the message to the user.
</p>

<p>
Furthermore, recall that the StockService throws a NotLoggedInException if for some reason the user is no longer logged in to his Google Account:
</p>

<p>
<pre class="code">
  private void checkLoggedIn() throws NotLoggedInException {
    if (getUser() == null) {
      throw new NotLoggedInException("Not logged in.");
    }
  }
</pre>
</p>

<p>
If we receive this error, we will redirect the user to the logout URL.
</p>

<p>
Here's a helper method to accomplish these two error handling requirements.
</p>

<p>
<pre class="code">
<span class="highlight">  private void handleError(Throwable error) {
    Window.alert(error.getMessage());
    if (error instanceof NotLoggedInException) {
      Window.Location.replace(loginInfo.getLogoutUrl());
    }
  }</span>
</pre>
</p>

<p>
We can add this to each AsyncCallback <code>onFailure(Throwable error)</code> method.
<pre class="code">
    loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback&lt;LoginInfo&gt;() {
      public void onFailure(Throwable error) {
<span class="highlight">        handleError(error);</span>
      }

    ...

    }
</pre>
</p>

<p>
<pre class="code">
    stockService.getStocks(new AsyncCallback&lt;String[]&gt;() {
      public void onFailure(Throwable error) {
<span class="highlight">        handleError(error);</span>
      }

    ...

    });
</pre>
</p>

<p>
<pre class="code">
    stockService.addStock(symbol, new AsyncCallback&lt;Void&gt;() {
      public void onFailure(Throwable error) {
<span class="highlight">        handleError(error);</span>
      }

    ...

    });
</pre>
</p>

<p>
<pre class="code">
    stockService.removeStock(symbol, new AsyncCallback&lt;Void&gt;() {
      public void onFailure(Throwable error) {
<span class="highlight">        handleError(error);</span>
      }

    ...

    });
</pre>
</p>

<h3>Test Datastore features</h3>

<p>
You can repeat the instructions above to run the application <a href="#test">locally</a> or on <a href="#deploy">App Engine</a>.
</p>

<p>
If you encounter runtime errors, examine the logs in the <a href="https://appengine.google.com/">App Engine Administration Console</a>.
</p>

<h2 id="more">More resources</h2>

<h3>Further exercises</h3>

<p>
Users can now sign in to Google Account and manage their own stock lists in the StockWatcher application running on App Engine.  Here are some suggested enhancements you can try as exercises:
<ul>
  <li>Loading the stock list has a noticeable delay.  Add a UI element to indicate that the stock list is loading.</li>
  <li>Add more attributes to the Stock class.  What happens to the data which was saved before these attributes were added?</li>
  <li>The StockService does not detect when one user has signed out and another user has signed in.  How would you modify the application to handle this edge case?</li>
</ul>
</p>

<h3>Learn more about App Engine</h3>

<p>
The App Engine <a href="//developers.google.com/appengine/docs/java/gettingstarted">Java Getting Started tutorial</a> gives more details on building an App Engine application including topics such as creating a project from scratch, using JSPs, managing different application versions, and more details on the web application descriptor files.
</p>

<p>
The App Engine <a href="//developers.google.com/appengine/docs/java">Java documentation</a> covers the User service and datastore service in greater detail.  In particular, it documents how to use JPA to access the datastore service.  Other services documented include Memcache, HTTP client, and, Java Mail.  The limitations of the App Engine Java runtime are also itemized.
</p>

