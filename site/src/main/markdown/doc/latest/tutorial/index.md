
<style>
code, .code {font-size: 9pt; font-family: Courier, Courier New, monospace; color:#007000;}
.highlight {background-color: #ffc;}
.strike {text-decoration:line-through; color:red;}
.header {margin-top: 1.5ex;}
.details {margin-top: 1ex;}
</style>

<p>
These tutorials are intended for developers who wish to write rich AJAX applications using Google Web Toolkit. You might be a Java developer who would like to be able to apply the software engineering principles of object-oriented programming and leverage the tools in your Java IDE when writing applications for the web. Or you might be a JavaScript guru curious about GWT's ability to generate highly optimized JavaScript with permutations for multiple browsers.
</p>
<p>
Although a knowledge of HTML, CSS, and Java is assumed, it is not required to run these tutorials.
</p>

<a name="prerequisites"></a>
<h2>Before You Begin</h2>
<p>Before you begin these tutorials, we assume that you've done the following:</p>
<ul class="instructions">
    <li>
        <div class="header">Installed the Java SDK.</div>
        <div class="details">If you don't have a recent version of the Java SDK installed, download and install <a href="http://java.sun.com/javase/downloads/index.jsp">Sun Java Standard Edition SDK</a>.</div>
    </li>
    <li>
        <div class="header">Installed Eclipse or your favorite Java IDE.</div>
        <div class="details">In these tutorials, we use <a href="http://www.eclipse.org/">Eclipse</a> because it is open source. However, GWT does not tie you to Eclipse. You can use <a href="http://www.jetbrains.com/idea/">IntelliJ</a>, <a href="http://www.netbeans.org/">NetBeans</a> or any Java IDE you prefer. If you use a Java IDE other than Eclipse, the screenshots and some of the specific instructions in the tutorial will be different, but the basic GWT concepts will be the same.</div>
        <div class="details">If your Java IDE does not include Apache Ant support, you can download and unzip <a href="http://ant.apache.org">Ant</a> to easily compile and run GWT applications.</div>
    </li>
    <li>
        <div class="header">Installed the Google Plugin for Eclipse.</div>
        <div class="details">The <a href="//developers.google.com/appengine/docs/java/tools/eclipse">Google Plugin for Eclipse</a> adds functionality to Eclipse for creating and developing GWT applications.</div>
    </li>
    <li>
        <div class="header">Downloaded Google Web Toolkit.</div>
        <div class="details">Google Web Toolkit can be downloaded with the Google Plugin for Eclipse.  Alternatively, download the most recent distribution of <a href="../../../download.html">Google Web Toolkit</a> for your operating system.</div>
    </li>
    <li>
        <div class="header">Unzipped the GWT distribution in directory you want to run it in.</div>
        <div class="details">GWT does not have an installation program. All the files you need to run and use GWT are located in the extracted directory.</div>
    </li>
</ul>

<p>You may also optionally do the following:</p>
<ul class="instructions">
    <li>
        <div class="header">Install the Google App Engine SDK.</div>
        <div class="details">Google App Engine allows you to run Java web applications, including GWT applications, on Google's infrastructure.  The App Engine SDK can be downloaded with the Google Plugin for Eclipse.  Alternatively, download the <a href="//developers.google.com/appengine/downloads">App Engine SDK</a> for Java separately.</div>
    </li>
    <li>
        <div class="header"><a href="../../../gettingstarted.html#create">Create and run your first web application</a> - A few, simple steps to familiarize you with the command line commands.
    </li>
</ul>

<h2 id="gwt_tutorials">GWT Tutorials</h2>

<h3>Build a Sample GWT Application</h3>
<ul>
    <li>
        <div class="header"><a href="gettingstarted.html">Build a Sample GWT Application</a></div>
        <div class="details">Get started with Google Web Toolkit by developing the StockWatcher application from scratch. You'll learn to create a GWT project, build the UI with GWT wigdets and panels, code the client-side functionality in the Java language, debug in development mode, apply CSS styles, compile the Java into JavaScript, and run the application in production mode.</div>
    </li>
</ul>

<h3>Client-Server Communication</h3>
<ul>
    <li>
        <div class="header">Communicating with the server via <a href="RPC.html">GWT RPC</a></div>
        <div class="details">Add a call to a server using GWT RPC. You'll learn how to make asynchronous calls, serialize Java objects, and handle exceptions.</div>
    </li>
    <li>
        <div class="header"><a href="JSON.html">Retrieving JSON data via HTTP</a></div>
        <div class="details">Make HTTP requests to retrieve JSON data from a server. The same technique can be used to retrieve XML data.</div>
    </li>
    <li>
        <div class="header"><a href="Xsite.html">Making cross-site requests</a></div>
        <div class="details">Make a call to a remote server, working around SOP (Same Origin Policy) constraints.</div>
    </li>
</ul>
<h3>Internationalization</h3>
<ul>
    <li>
        <div class="header"><a href="i18n.html">Internationalizing a GWT application</a></div>
        <div class="details">Translate the user interface of a GWT application into another language using Static String Internationalization.</div>
    </li>
</ul>

<h3>JUnit Testing</h3>
<ul>
    <li>
        <div class="header"><a href="JUnit.html">Unit testing with JUnit</a></div>
        <div class="details">Add unit tests to a GWT application using JUnit.</div>
    </li>
</ul>

<h3>Deploying to Google App Engine</h3>
<ul>
    <li>
        <div class="header"><a href="appengine.html">Deploying to Google App Engine</a></div>
        <div class="details">Deploy a GWT application to <a href="//developers.google.com/appengine">App Engine</a>.</div>
    </li>
</ul>


