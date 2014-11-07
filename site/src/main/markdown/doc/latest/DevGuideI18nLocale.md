<ol class="toc" id="pageToc">
  <li><a href="#LocaleOverview">Overview</a></li>
  <li><a href="#LocaleProperty">Client Properties and the GWT Compilation Process</a></li>
  <li><a href="#LocaleModule">Adding Locale Choices to a Module</a></li>
  <li><a href="#LocaleDefault">The Default Locale</a></li>
  <li><a href="#LocaleSpecifying">Specifying the Locale to Load</a></li>
  <li><a href="#RuntimeLocales">Runtime Locales</a></li>
  <li><a href="#LocaleProvider">Creating a New Property Provider</a></li>
  <li><a href="#LocaleInfo">Programmatic Access to Locale Information</a></li>
  <li><a href="#ServerLocales">Server/Generator Manipulation of Locales</a></li>
</ol>

<h2 id="LocaleOverview">Overview</h2>

<p>GWT represents <tt>locale</tt> as a client property whose value can be set either using a meta tag embedded in the <a href="DevGuideOrganizingProjects.html#DevGuideHostPage">host
page</a> or in the query string of the host page's URL. Rather than being supplied by GWT, the set of possible values for the <tt>locale</tt> client property is entirely a
function of your <a href="DevGuideOrganizingProjects.html#DevGuideModules">module configuration</a>.</p>

<h2 id="LocaleProperty">Client Properties and the GWT Compilation Process</h2>

<p><i>Client properties</i> are key/value pairs that can be used to configure GWT modules. User agent, for example, is represented by a client property. Each client property can
have any number of values, but all of the values must be enumerable when the GWT compiler runs. GWT modules can define and extend the set of available client properties along with
the potential values each property might assume when loaded in an end user's browser using the <tt>&lt;extend-property&gt;</tt> directive. At compile time, the GWT compiler
determines all the possible permutations of a module's client properties, from which it produces multiple <i>compilations</i>. Each compilation is optimized for a different set of
client properties and is recorded into a file ending with the suffix <tt>.cache.html</tt>.</p>

<p>In deployment, the end-user's browser only needs one particular compilation,
which is determined by mapping the end user's client properties onto the
available compiled permutations. Thus, only the exact code required by the end
user is downloaded, no more. By making locale a client property, the standard
startup process in <tt>&lt;module&gt;.nocache.js</tt> chooses the appropriate
localized version of an application, providing ease of use, optimized
performance, and minimum script size. See the
<a href="FAQ_DebuggingAndCompiling.html#What's_with_all_the_cache/nocache_stuff_and_weird_filenames">Knowledge
Base</a> for more information about the logic of the <tt>&lt;modulename&gt;.nocache.js</tt> file.</p>

<h2 id="LocaleModule">Adding Locale Choices to a Module</h2>

<p>In any real-world application, you will define at least one locale in addition to the default locale. &quot;Adding a locale&quot; means extending the set of values of the <tt>locale</tt>
client property using the <tt>&lt;extend-property&gt;</tt> element in your <a
href="DevGuideOrganizingProjects.html#DevGuideModuleXml">module XML</a>. For
example, the following module adds multiple locale values:</p>

<pre class="prettyprint">
&lt;module&gt;
  &lt;inherits name=&quot;com.google.gwt.user.User&quot;/&gt;
  &lt;inherits name=&quot;com.google.gwt.i18n.I18N&quot;/&gt;
  
  &lt;!-- French language, independent of country --&gt;
  &lt;extend-property name=&quot;locale&quot; values=&quot;fr&quot;/&gt;

  &lt;!-- French in France --&gt;
  &lt;extend-property name=&quot;locale&quot; values=&quot;fr_FR&quot;/&gt;

  &lt;!-- French in Canada --&gt;
  &lt;extend-property name=&quot;locale&quot; values=&quot;fr_CA&quot;/&gt;
  
  &lt;!-- English language, independent of country --&gt;
  &lt;extend-property name=&quot;locale&quot; values=&quot;en&quot;/&gt;
&lt;/module&gt;
</pre>

<h2 id="LocaleDefault">The Default Locale</h2>

<p>The <tt>com.google.gwt.i18n.I18N</tt> module defines only one locale by default, called <tt>default</tt>. This default locale is used when the <tt>locale</tt> client property
goes unspecified in deployment. The default locale is used internally as a last-resort match between a <a href="/javadoc/latest/com/google/gwt/i18n/client/Localizable.html">Localizable</a> interface and a localized resource or
class.</p>

<p>In general, you should avoid running the app in the <tt>default</tt> locale
&mdash; many things will produce surprising results.  For example, only a small set of
currencies will be supported, resulting in errors for applications that make use of
other currencies, and no plural forms will be supported (since
the language isn't known).  If you really want to allow the application
to continue running when the user requests an unsupported locale, you
are probably better off choosing some real language as a default, such as
<tt>en</tt>.  You can set what value is used for the default by including
the following in your <a
href="DevGuideOrganizingProjects.html#DevGuideModuleXml">module XML</a>:
<pre class="prettyprint">
&lt;set-property-fallback name="locale" value="en"/&gt;
</pre>

<h2 id="LocaleSpecifying">Specifying the Locale to Load</h2>

<p>The locale client property can be specified using either a meta tag or as part of the query string in the host page's URL. If both are specified, the query string takes
precedence. To specify the <tt>locale</tt> client property using a meta tag in the <a href="DevGuideOrganizingProjects.html#DevGuideHostPage">host page</a>, embed a meta tag for
<tt>gwt:property</tt> as follows:</p>

<pre class="prettyprint">
&lt;meta name=&quot;gwt:property&quot; content=&quot;locale=x_Y&quot;&gt;
</pre>

<p>For example, the following host HTML page sets the locale to &quot;ja_JP&quot;:</p>

<pre class="prettyprint">
&lt;html&gt;
  &lt;head&gt;
    &lt;meta name=&quot;gwt:property&quot; content=&quot;locale=ja_JP&quot;&gt;
  &lt;/head&gt;
  &lt;body&gt;
    &lt;!-- Load the GWT compiled module code                           --&gt;
    &lt;script src=&quot;com.google.gwt.examples.i18n.ColorNameLookupExample.nocache.js &quot; /&gt;
  &lt;/body&gt;
&lt;/html&gt;
</pre>

<p>To specify the <tt>locale</tt> client property using a query string, specify a value for the name <tt>locale</tt>. For example,</p>

<pre>
http://www.example.org/myapp.html?locale=fr_CA
</pre>

<p class="note"><strong>Tip:</strong> The preferred time to explicitly set <tt>locale</tt> is to do so before your GWT module is invoked. You can change the <tt>locale</tt> from within your GWT
module by adding or changing the <tt>locale</tt> query string in the current URL and reloading the page. Keep in mind that after reloading the page, your module will
restart.</p>

<p/>

<h2 id="RuntimeLocales">Runtime Locales</h2>

<p>For cases where the translated values are the same, but you still want
country-specific details, you can use runtime locales to reduce the number
of compiled permutations, but still get country-specific details like the
default currency, number/date formatting rules, etc.</p>

<p>As an example, you might have one set of translations for all of Spanish
as spoken in Latin America (<tt>es_419</tt>), yet allow users to choose
a country-specific locale such as Argentinian Spanish (<tt>es_AR</tt>).</p>

<p>The easy way to use runtime locales is simply to add:
<pre class="prettyprint">
&lt;inherits name=&quot;com.google.gwt.i18n.CldrLocales&quot;/&gt;
</pre>
<br/>to your <a href="DevGuideOrganizingProjects.html#DevGuideModuleXml">module
XML</a> file, and all locales that GWT knows about that inherit from
your compile-time locale will be automatically included.  You can see the
result in the <a
href="http://samples.gwtproject.org/samples/Showcase/Showcase.html"><tt>Showcase</tt>
sample application</a>.</p>

<h3>Caveats</h3>
<ul>
 <li>All the tables for all included runtime locales are included in the
 each appropriate compiled permutation, so this can increase download size.</li>
 <li>The tables for non-obvious locale inheritance and aliases are too large
 to be included in the selection script, so inheritance won't work properly
 in all cases.  This means you either need to specifically control the set
 of possible locales, such as in the locale selector in the <a
href="http://samples.gwtproject.org/samples/Showcase/Showcase.html"><tt>Showcase</tt>
sample application</a>, or have the server choose the locale using the
proper inheritance tables (<tt>GwtLocaleFactoryImpl</tt> will be helpful here,
and you will need a way to get the set of locales your application was built
with).</li>
 <li>Only currency data, number format, and date/time formats are affected
 by runtime locales currently &mdash; everything else will only use the compile-time
 locale from the locale deferred binding property.</li>
</ul>

<h2 id="LocaleProvider">Creating a New Property Provider</h2>

<p>If you are embedding your module into an existing application, there may be another way of determining locale that does not lend itself to using the <tt>&lt;meta&gt;</tt> tag
or specifying <tt>locale=</tt> as a query string. In this case, you could write your own property provider.</p>

<p>A property provider is specified in the <a href="DevGuideOrganizingProjects.html#DevGuideModuleXml">module XML file</a> as a JavaScript fragment that will return the value for the
named property at runtime. In this case, you would want to define the locale property using a property provider. To see examples of <tt>&lt;property-provider&gt;</tt> definitions
in action, see the files <a href="https://gwt.googlesource.com/gwt/+/master/user/src/com/google/gwt/i18n/I18N.gwt.xml">I18N.gwt.xml</a> and
<a href="https://gwt.googlesource.com/gwt/+/master/user/src/com/google/gwt/user/UserAgent.gwt.xml">UserAgent.gwt.xml</a> in the GWT source code.</p>

<h2 id="LocaleInfo">Programmatic Access to Locale Information</h2>

<p>To get information about the current locale or the available set of
locales, see the <a
href="/javadoc/latest/com/google/gwt/i18n/client/LocaleInfo.html">LocaleInfo</a>
class.  For example:</p>

<ul>
<li>To check if the current locale is a Right-to-Left locale:
<pre class="prettyprint">
if (LocaleInfo.getCurrentLocale().isRTL()) {
  ...
}
</pre></li>
</ul>

<ul>
<li>To get a list of supported locales, such as for a locale selector:
<pre class="prettyprint">
for (String localeName : LocaleInfo.getAvailableLocaleNames()) {
  String displayName = LocaleInfo.getLocaleNativeDisplayName(localeName);
  ...
}
</pre></li>
</ul>

<h2 id="ServerLocales">Server/Generator Manipulation of Locales</h2>

<p>GWT provides two classes to manipulate locales, which fully support aliases
and locale inheritance.</p>

<ul>
<li><a
href="/javadoc/latest/com/google/gwt/i18n/shared/GwtLocale.html">GwtLocale</a>
represents a GWT locale, and supports converting to canonical form, producing
search lists for locale inheritance and aliases, and provides accessors to
the components of a locale.</li>
</ul>

<ul>
<li><a
href="/javadoc/latest/com/google/gwt/i18n/shared/GwtLocaleFactory.html">GwtLocaleFactory</a>
provides a way of creating new <tt>GwtLocale</tt> objects from locale names
or their components (useful for converting from a <tt>java.util.Locale</tt>
object).</li>
</ul>

<ul>
<li><a
href="/javadoc/latest/com/google/gwt/i18n/rebind/LocaleUtils.html">LocaleUtils</a>
provides easy access to GWT's locale infrastructure for a generator.
<ul>
<li>Get a GwtLocaleFactory instance:
<pre class="prettyprint">
GwtLocaleFactory factory = LocaleUtils.getLocaleFactory();
</pre></li>
<li>Get all locales for this compile, including runtime locales:
<pre class="prettyprint">
Set&lt;GwtLocale&gt; locales = localeUtils.getAllLocales();
</pre></li>
</ul>
</li>
</ul>

<ul>
<li><a
href="/javadoc/latest/com/google/gwt/i18n/server/GwtLocaleFactoryImpl.html">GwtLocaleFactoryImpl</a>
provides a way to create GwtLocale instances on the server.</li>
</ul>