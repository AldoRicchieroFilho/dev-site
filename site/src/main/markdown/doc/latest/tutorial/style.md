
<style>
code, .code {font-size: 9pt; font-family: Courier, Courier New, monospace; color:#007000;}
.highlight {background-color: #ffc;}
.strike {text-decoration:line-through; color:red;}
.header {margin-top: 1.5ex;}
.details {margin-top: 1ex;}
</style>

<p>
At this point, StockWatcher is functional. Now you want to give it some visual style.
</p>
<p>
<img src="images/StyleBefore.png" alt="StockWatcher before applying styles" />
</p>
<p>
In this section, you'll:
</p>
<ol>
    <li><a href="#projectStylesheets">Associate style sheets with the project.</a></li>
    <li><a href="#GWTtheme">Change the theme.</a></li>
    <li><a href="#secondary">Create a secondary style.</a></li>
    <li><a href="#dependent">Create a dependent secondary style.</a></li>
    <li><a href="#dynamic">Update styles dynamically.</a></li>
    <li><a href="#HTMLelement">Set an element's HTML attributes</a></li>
    <li><a href="#staticResource">Add images or other static HTML elements.</a></li>
</ol>


<h3>Benefits of CSS</h3>
<p>
GWT provides very few Java methods directly related to style. Rather, we encourage you to define styles in Cascading Style Sheets.
</p>
<p>
When it comes to styling web applications, CSS is ideal. In addition to cleanly separating style from application logic, this division of labor helps applications load and render more quickly, consume less memory, and even makes them easier to tweak during edit/debug cycles because there's no need to recompile for style tweaks. </p>

<a name="projectStylesheets"></a>
<h2>1. Associating Style Sheets with a Project</h2>
<p>
Two style sheets are already associated with the StockWatcher project.
</p>
<ul>
    <li>a theme style sheet, standard.css: where the GWT default styles are defined</li>
    <li>the application style sheet, StockWatcher.css: where you define the specific styles for StockWatcher</li>
</ul>
<p>
When you used webAppCreator to create StockWatcher, it created the application style sheet (StockWatcher.css). It also referenced the theme in the GWT module.
</p>
<ol class="instructions">
    <li>
        <div class="header">Open the GWT module, StockWatcher/src/com/google/gwt/sample/stockwatcher/StockWatcher.gwt.xml.  Notice that the Standard theme is being used by default.</div>
        <div class="details"><pre class="code">
&lt;?xml version="1.0" encoding="UTF-8"?&gt;
&lt;module rename-to='stockwatcher'&gt;
  &lt;!-- Inherit the core Web Toolkit stuff.                        --&gt;
  &lt;inherits name='com.google.gwt.user.User'/&gt;

  &lt;!-- Inherit the default GWT style sheet.  You can change       --&gt;
  &lt;!-- the theme of your GWT application by uncommenting          --&gt;
  &lt;!-- any one of the following lines.                            --&gt;
<span class="highlight">  &lt;inherits name='com.google.gwt.user.theme.standard.Standard'/&gt;</span>
  &lt;!-- &lt;inherits name="com.google.gwt.user.theme.chrome.Chrome"/&gt; --&gt;
  &lt;!-- &lt;inherits name="com.google.gwt.user.theme.dark.Dark"/&gt;     --&gt;

  &lt;!-- Other module inherits                                      --&gt;

  &lt;!-- Specify the app entry point class.                         --&gt;
  &lt;entry-point class='com.google.gwt.sample.stockwatcher.client.StockWatcher'/&gt;
&lt;/module&gt;</pre></div>
    </li>
    <li>
        <div class="header">Open StockWatcher/war/StockWatcher.html.  Notice that the application style sheet is StockWatcher.css.</div>
        <div class="details"><pre class="code">
&lt;!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"&gt;

&lt;html&gt;
  &lt;head&gt;
    &lt;meta http-equiv="content-type" content="text/html; charset=UTF-8"&gt;

<span class="highlight">    &lt;link type="text/css" rel="stylesheet" href="StockWatcher.css"&gt;</span>

    &lt;title&gt;StockWatcher&lt;/title&gt;

    &lt;script type="text/javascript" language="javascript" src="stockwatcher/stockwatcher.nocache.js"&gt;&lt;/script&gt;
  &lt;/head&gt;

  &lt;body&gt;

    &lt;h1&gt;StockWatcher&lt;/h1&gt;

    &lt;div id="stockList"&gt;&lt;/div&gt;

    &lt;iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1'
style="position:absolute;width:0;height:0;border:0"&gt;&lt;/iframe&gt;

  &lt;/body&gt;
&lt;/html&gt;
</pre></div>
    </li>
</ol>
<h4>Automatic Resource Inclusion</h4>
<p>
Like images, CSS files are static resources that are stored in the public directory and referenced from the HTML host page. You can associate a style sheet with your application either of two ways.
</p>
<ul>
    <li>Preferred: in the module XML file (StockWatcher.gwt.xml)</li>
    <li>Alternate: in the HTML host page (StockWatcher.html)</li>
</ul>
<p>
Whichever method you choose, you can associate one or more application style sheets with your project. They cascade in the order they are listed, just as they do in an HTML document.
</p>
<p>
For StockWatcher, you'll follow the preferred strategy. Rather than put links to the style sheets in the HTML host page, you'll use the module XML file. Then, when you compile StockWatcher, the GWT compiler will bundle all the static resources required to run your application including the style sheets. This mechanism is called Automatic Resource Inclusion.
</p>
<p>
In most cases, it is the better strategy because the style sheet will follow your module wherever it is used in new contexts, no matter what HTML host page you embed it in. As you get into more complex development, you will want to reuse or share modules. Shared modules do not include a host page and therefore, you cannot guarantee the availability of the application style sheet unless you use Automatic Resource Inclusion.
</p>
<p>
If you have a case where you want whatever host page your module is embedded in to dictate the styles for its widgets, then don't include the style sheet in the module XML file.
</p>
<a name="GWTtheme"></a>
<h2>2. Changing the Theme</h2>
<p>
GWT ships with three themes: Standard, Chrome, and Dark. The Standard theme is selected by default when the GWT module is created. Each application can use only one theme at a time. However, if you have an existing style or you prefer to design one from scratch, you don't have to use any theme at all. Take a moment to see  what the other themes look like.
</p>
<p>Change the theme from Standard to Dark.</p>
<ol class="instructions">
    <li>
        <div class="header">In StockWatcher.gwt.xml, comment out the line referencing the Standard theme.</div>
        <div class="details">Eclipse shortcut: Source &gt; Toggle Comment.</div>
    </li>
    <li>
        <div class="header">Uncomment the line referencing the Dark theme.</div>
        <div class="details">Eclipse shortcut: Source &gt; Toggle Comment.</div>
        <div class="details"><pre class="code">
  &lt;!-- Inherit the default GWT style sheet.  You can change       --&gt;
  &lt;!-- the theme of your GWT application by uncommenting          --&gt;
  &lt;!-- any one of the following lines.                            --&gt;
<span class="highlight">  &lt;!-- &lt;inherits name='com.google.gwt.user.theme.standard.Standard'/&gt; --&gt;</span>
  &lt;!-- &lt;inherits name="com.google.gwt.user.theme.chrome.Chrome"/&gt; --&gt;
<span class="highlight">  &lt;inherits name="com.google.gwt.user.theme.dark.Dark"/&gt;</span></pre></div>
    </li>
    <li>
        <div class="header">Test the change.</div>
        <div class="details">Save your changes to the StockWatcher.gwt.xml file.</div>
        <div class="details">If development mode is still running, terminate it and then relaunch StockWatcher.</div>
    </li>
</ol>
<p>
For StockWatcher, you are going to build on the Standard theme. So after you've played around to see how themes work, set the theme back to Standard.
</p>

<p class="note">
<b>Note:</b> GWT themes also come in RTL (right-to-left) versions to support languages written from right-to-left, such as Arabic. To use a right-to-left theme, append RTL to the theme name.
<code>&lt;inherits name='com.google.gwt.user.theme.standard.<span class="highlight">StandardRTL</span>'/&gt;</code>
</p>

<h3>Deciding on Implementation Strategies</h3>
<p>
There are various ways you can modify GWT's default styles.
</p>
<ul>
    <li>
        <div class="header">You can create new style rules in the application style sheet, Stockwatcher.css.</div>
        <div class="details">Changes made with this approach apply to all widgets of a type, for example, to all Buttons.</div>
    </li>
    <li>
        <div class="header">You can append a secondary style to the HTML element by adding another class attribute.</div>
        <div class="details">Changes made with this approach apply to a single widget, for example just the Remove button in StockWatcher.</div>
    </li>
    <li>
        <div class="header">You can override a widget's primary style name.</div>
        <div class="details">If you need to clear any existing styles that you are inheriting from the theme or other style sheets, you can change the default primary class associated with the widget.</div>
    </li>
    <li>
        <div class="header">If you are embedding a GWT application into an existing page, you can edit your own style sheet.</div>
    </li>
    <li>
        <div class="header">You can start completely from scratch.</div>
    </li>
</ul>
<p>
For the StockWatcher application, you'll focus mostly on the second approach: you'll learn to append a secondary style.
</p>

<a name="secondary"></a>
<h2>3. Associating style rules with GWT-generated HTML elements</h2>

<h3>An Element's Primary Style</h3>
<p>
You might have noticed the buttons for StockWatcher have a gradient background. Where is the button style coming from?
</p>
<p>
If, after you compile StockWatcher, you look at the generated JavaScript for the Add button, you will see that the button has a class attribute of gwt-Button:<br /><code>&lt;button class="gwt-Button" tabindex="0" type="button"&gt;Add&lt;/button&gt;</code>
</p>
<p>
In GWT, each class of widget has an associated style name (like gwt-Button) that binds it to a CSS style rule. This is the widget's primary style. Default values are defined for the primary style in the theme style sheet.
</p>
<table>
    <tr>
        <th>Type of Element</th>
        <th>HTML Tag</th>
        <th>CSS Selector</th>
    </tr>
    <tr>
        <td>Buttons in static HTML and<br />GWT-generated buttons</td>
        <td>&lt;button&gt;</td>
        <td>button</td>

    </tr>
    <tr>
        <td>Only GWT-generated buttons</td>
        <td>&lt;button class="gwt-Button"&gt;</td>
        <td>button.gwt-Button</td>
    </tr>
    <tr>
        <td>Only my special GWT-generated button</td>
        <td>&lt;button class="gwt-Button my-button"&gt;</td>
        <td>button.my-button</td>
    </tr>
</table>
<p class="note">
<b>Tip:</b> You can look up the name of the style rule (the CSS selector) for each widget by accessing the GWT API Reference via the <a href="../RefWidgetGallery.html">Widget Gallery</a>.
</p>
<p>
GWT takes advantage of the fact that you can associate multiple classes with an HTML element so that you can specify a style for a specific GWT-generated element and not affect others of the same type. In this section, you'll learn how to set the secondary class on a GWT-generated HTML element.
</p>


<h3>Creating a Secondary Style</h3>
<p>
Creating a secondary style for an HTML element is a two-step process:
</p>
<ol>
    <li>Specify the style rule in StockWatcher.css.</li>
    <li>Apply the style by setting HTML class attributes programmatically in StockWatcher.java.</li>
</ol>
<p>
Let's make one quick change to see how the mechanism works. Then you can make the rest of the changes in one pass. We'll start by changing the colors of the first row where we've stored header information.
</p>

<h3>Defining the Style in CSS</h3>
<p>
When you created the StockWatcher application, webAppCreator generated the application style sheet (StockWatcher.css) and added a pointer to it in the module XML file (StockWatcher.gwt.xml). So, you're ready to start defining style rules.
</p>
<ol class="instructions">
    <li>
        <div class="header">Open the application style sheet.</div>
        <div class="details">Open StockWatcher/war/StockWatcher.css.</div>
    </li>
    <li>
        <div class="header">For any HTML element with the class attribute of watchListHeader, set the color and text properties.</div>
        <div class="details">Replace the contents of StockWatcher.css with the following style rules.</div>
        <div class="details"><pre class="code">
<span class="highlight">/* Formatting specific to the StockWatcher application */

body {
  padding: 10px;
}

/* stock list header row */
.watchListHeader {
  background-color: #2062B8;
  color: white;
  font-style: italic;
}</span></pre></div>
    </li>
    <li>
        <div class="header">Save your changes to StockWatcher.css.</div>
    </li>
</ol>

<h3>Applying the style with the addStyleName method</h3>
<p>
In a web page with static elements, you would now go through the HTML source and add class attributes to various elements to associate them with the styles defined in the CSS file. For example:<br /> <code>&lt;tr class="watchListHeader"&gt;</code>
</p>
<p>
However, GWT elements are created dynamically at runtime. So you'll set the HTML class attributes in the Java source using the addStyleName method. You'll specify the row (the header is row 0) and the name of the secondary class, watchListHeader.
</p>
<ol class="instructions">
    <li>
        <div class="header">In StockWatcher.java, in the onModuleLoad method, add a secondary style to the header row in the stock table.</div>
        <div class="details"><pre class="code">
  public void onModuleLoad() {
    // Create table for stock data.
    stocksFlexTable.setText(0, 0, "Symbol");
    stocksFlexTable.setText(0, 1, "Price");
    stocksFlexTable.setText(0, 2, "Change");
    stocksFlexTable.setText(0, 3, "Remove");

<span class="highlight">    // Add styles to elements in the stock list table.
    stocksFlexTable.getRowFormatter().addStyleName(0, "watchListHeader");</span></pre></div>
    </li>
    <li>
        <div class="header">Save your changes to StockWatcher.java and then press Refresh in the browser running development mode to see them.</div>
        <div class="details">The the header row in the flex table displays white italic headings against a blue background.</div>

    </li>
</ol>

<h3>Implementing the Remaining Secondary Style Changes for StockWatcher</h3>
<p>
Each of the changes we will make by creating a secondary style is outlined below. You can apply them one at a time to see the effect of each change or you can copy and paste the set of changes summarized at the end of this section.
</p>
<ul>
    <li>Put a border around the stock list.</li>
    <li>Right-align numeric data in the stock list.</li>
    <li>Center the Remove buttons and make them wider.</li>
    <li>Add whitespace (padding) to the Add Stock panel.</li>
</ul>

<h4>Put a border around the stock list</h4>
<ol class="instructions">
    <li>
        <div class="header">Define the style.</div>
        <div class="details">In StockWatcher.css, create a style rule for HTML elements with a class attribute of watchList.</div>
        <div class="details"><pre class="code">
/* stock list header row */
.watchListHeader {
  background-color: #2062B8;
  color: white;
  font-style: italic;
}

<span class="highlight">/* stock list flex table */
.watchList {
  border: 1px solid silver;
  padding: 2px;
  margin-bottom:6px;
}</span></pre></div>
    </li>
    <li>
        <div class="header">Apply the style.</div>
        <div class="details">In StockWatcher.java, add a secondary class attribute to the stock flex table.</div>
        <div class="details"><pre class="code">
    // Add styles to elements in the stock list table.
    stocksFlexTable.getRowFormatter().addStyleName(0, "watchListHeader");
<span class="highlight">    stocksFlexTable.addStyleName("watchList");</span></pre></div>
    </li>
    <li>
        <div class="header">Save your changes and then press Refresh in the browser running development mode to see them.</div>
        <div class="details">The stock list table has a silver border.</div>
    </li>
</ol>

<h4>Right-align numeric data in the stock list</h4>
<p>
First, you'll format the text in the stock table's header row, which loads when StockWatcher is launched. Later, you'll apply the same style rules to the table rows that contain the stock data.
</p>
<ol class="instructions">
    <li>
        <div class="header">Define the style.</div>
        <div class="details">In StockWatcher.css, create a style rule that will right-align the columns that contain the Price and Change fields.</div>
        <div class="details"><pre class="code">
/* stock list flex table */
.watchList {
  border: 1px solid silver;
  padding: 2px;
  margin-bottom:6px;
}

<span class="highlight">/* stock list Price and Change fields */
.watchListNumericColumn {
  text-align: right;
  width:8em;
}</span></pre></div>
    </li>
    <li>
        <div class="header">Apply the style.</div>
        <div class="details">In StockWatcher.java, add a secondary class attribute to both the Price and Change fields.</div>
        <div class="details"><pre class="code">
    // Add styles to elements in the stock list table.
    stocksFlexTable.getRowFormatter().addStyleName(0, "watchListHeader");
    stocksFlexTable.addStyleName("watchList");
<span class="highlight">    stocksFlexTable.getCellFormatter().addStyleName(0, 1, "watchListNumericColumn");
    stocksFlexTable.getCellFormatter().addStyleName(0, 2, "watchListNumericColumn");</span></pre></div>
    </li>
    <li>
        <div class="header">Save your changes and then press Refresh in the browser running development mode to see them.</div>
        <div class="details">The Price and Change columns have a set width and the text in the header row is right-aligned.</div>
    </li>
</ol>

<h4>Center the Remove buttons and make them wider</h4>
<ol class="instructions">
    <li>
        <div class="header">Define the style.</div>
        <div class="details">In StockWatcher.css, create a style rule that will center the text in the column that contains the Remove button.</div>
        <div class="details"><pre class="code">
/* stock list Price and Change fields */
.watchListNumericColumn {
  text-align: right;
  width:8em;
}

<span class="highlight">/* stock list Remove column */
.watchListRemoveColumn {
  text-align: center;
}</span></pre></div>
    </li>
    <li>
        <div class="header">Apply the style.</div>
        <div class="details">In StockWatcher.java, add a secondary class attribute to the Remove field.</div>
        <div class="details"><pre class="code">
    // Add styles to elements in the stock list table.
    stocksFlexTable.getRowFormatter().addStyleName(0, "watchListHeader");
    stocksFlexTable.addStyleName("watchList");
    stocksFlexTable.getCellFormatter().addStyleName(0, 1, "watchListNumericColumn");
    stocksFlexTable.getCellFormatter().addStyleName(0, 2, "watchListNumericColumn");
<span class="highlight">    stocksFlexTable.getCellFormatter().addStyleName(0, 3, "watchListRemoveColumn");</span></pre></div>
    </li>
    <li>
        <div class="header">Save your changes and then press Refresh in the browser running development mode to see them.</div>
        <div class="details">The caption takes up the entire width of the field. You'll be able to see that the buttons are centered in the Remove column after you format the data rows in the next step.</div>
    </li>
</ol>

<h4>Apply the same cell formatting to the rows that hold the stock data</h4>
<p>
You've formatted the header row of the flex table, which is displayed when StockWatcher starts up. Remember, however, that in a flex table, the rows holding the stocks aren't created until the user adds a stock to the list. Therefore, you will add the code for formatting the stock data in the addStock method rather than in the onLoad method.
</p>
<ol class="instructions">
    <li>
        <div class="header">You have already defined the style in StockWatcher.css.</div>
    </li>
    <li>
        <div class="header">Apply the style.</div>
        <div class="details">In StockWatcher.java, in the addStock method, add secondary class attribute to the table cells in the Price, Change, and Remove columns.</div>
        <div class="details"><pre class="code">
    // Add the stock to the table.
    int row = stocksFlexTable.getRowCount();
    stocks.add(symbol);
    stocksFlexTable.setText(row, 0, symbol);
<span class="highlight">    stocksFlexTable.getCellFormatter().addStyleName(row, 1, "watchListNumericColumn");
    stocksFlexTable.getCellFormatter().addStyleName(row, 2, "watchListNumericColumn");
    stocksFlexTable.getCellFormatter().addStyleName(row, 3, "watchListRemoveColumn");</span></pre></div>
    </li>
    <li>
        <div class="header">Save your changes and then press Refresh in the browser running development mode to see them.</div>
        <div class="details">Add stocks to the list. The Price and Change data is right-aligned. The Remove button is centered.</div>
    </li>
</ol>

<h4>Add margins around the Add Stock panel</h4>
<p>Add  whitespace around the text box and Add button in the Add Stock panel.</p>
<ol class="instructions">
    <li>
        <div class="header">Define the style.</div>
        <div class="details">In StockWatcher.css, create a style rule to widen the margins around the Add Stock panel.</div>
        <div class="details"><pre class="code">
/* stock list Remove column */
.watchListRemoveColumn {
  text-align: center;
}

<span class="highlight">/* Add Stock panel */
.addPanel {
  margin: 10px 0px 15px 0px;
}</span></pre></div>
    </li>
    <li>
        <div class="header">Apply the style.</div>
        <div class="details">In StockWatcher.java, in the onModuleLoad method add a secondary class attribute to the addPanel.</div>
        <div class="details"><pre class="code">
    // Assemble Add Stock panel.
    addPanel.add(newSymbolTextBox);
    addPanel.add(addStockButton);
<span class="highlight">    addPanel.addStyleName("addPanel");</span></pre></div>
    </li>
    <li>
        <div class="header">Save your changes and then press Refresh in the browser running development mode to see them.</div>
        <div class="details">The margin between the stock table and the Add Stock panel has increased.</div>
    </li>
</ol>

<h4>Summary of Changes</h4>

<p>
Here the summary of the changes we've done so far.
</p>
<img src="images/StyleSecondary.png" alt="StockWatcher Summary" />

<h5>Changes to StockWatcher.css</h5>
<pre class="code">
/* Formatting specific to the StockWatcher application */

body {
  padding: 10px;
}

/* stock list header row */
.watchListHeader {
  background-color: #2062B8;
  color: white;
  font-style: italic;
}

/* stock list flex table */
.watchList {
  border: 1px solid silver;
  padding: 2px;
  margin-bottom:6px;
}

/* stock list Price and Change fields */
.watchListNumericColumn {
  text-align: right;
  width:8em;
}

/* stock list Remove column */
.watchListRemoveColumn {
  text-align: center;
}

/* Add Stock panel */
.addPanel {
  margin: 10px 0px 15px 0px;
}</pre>
<h5>Changes to StockWatcher.java, onModuleLoad</h5>
<pre class="code">
  public void onModuleLoad() {
    // Create table for stock data.
    stocksFlexTable.setText(0, 0, "Symbol");
    stocksFlexTable.setText(0, 1, "Price");
    stocksFlexTable.setText(0, 2, "Change");
    stocksFlexTable.setText(0, 3, "Remove");

<span class="highlight">    // Add styles to elements in the stock list table.
    stocksFlexTable.getRowFormatter().addStyleName(0, "watchListHeader");
    stocksFlexTable.addStyleName("watchList");
    stocksFlexTable.getCellFormatter().addStyleName(0, 1, "watchListNumericColumn");
    stocksFlexTable.getCellFormatter().addStyleName(0, 2, "watchListNumericColumn");
    stocksFlexTable.getCellFormatter().addStyleName(0, 3, "watchListRemoveColumn");</span>

    // Assemble the Add Stock panel
    addPanel.add(newSymbolTextBox);
    addPanel.add(addStockButton);
<span class="highlight">    addPanel.addStyleName("addPanel");</span>
    .
    .
    .
  }</pre>
<h5>Changes to StockWatcher.java, addStock</h5>
<pre class="code">
    // Add the stock to the table.
    int row = stocksFlexTable.getRowCount();
    stocks.add(symbol);
    stocksFlexTable.setText(row, 0, symbol);
<span class="highlight">    stocksFlexTable.getCellFormatter().addStyleName(row, 1, "watchListNumericColumn");
    stocksFlexTable.getCellFormatter().addStyleName(row, 2, "watchListNumericColumn");
    stocksFlexTable.getCellFormatter().addStyleName(row, 3, "watchListRemoveColumn");</span></pre>


<a name="dependent"></a>
<h2>4. Creating secondary styles dependent on a primary style</h2>
<p>
Next you want to change the style of the Remove button. The Remove button inherits its style from the Button widget. Default styles for all GWT Button widgets are defined by GWT in standard.css.
</p>
<pre class="code">
&lt;button class="gwt-Button" tabindex="0" type="button"&gt;x&lt;/button&gt;</pre>

<pre class="code">
.gwt-Button {
    background:transparent url(images/hborder.png) repeat-x scroll 0px -27px;
    border:1px outset #CCCCCC;
    cursor:pointer;
    font-size:small;
    margin:0pt;
    padding:3px 5px;
    text-decoration:none;
}</pre>
<p>
For StockWatcher, you want your style change to apply only to the Remove button. So you'll do just as you've been doing: add a secondary style to the Remove button element. But this time, you'll make the secondary style dependent on the primary style. Dependent styles are powerful because they are automatically updated whenever the primary style name changes. In contrast, secondary style names that are not dependent style names are not automatically updated when the primary style name changes.
</p>
<p>
To do this, you'll use the addStyleDependentName method instead of the addStyleName method.
</p>
<ol class="instructions">
    <li>
        <div class="header">Define the style rule.</div>
        <div class="details"></div>
        <div class="details"><pre class="code">
/* Add Stock panel */
.addPanel {
  margin: 10px 0px 15px 0px;
}

<span class="highlight">/* stock list, the Remove button */
.gwt-Button-remove {
  width: 50px;
}</span></pre></div>
    </li>
    <li>
        <div class="header">Apply the style.</div>
        <div class="details">In StockWatcher.java, use addStyleDependentName to add a secondary, dependent class attribute to the Remove button.</div>
        <div class="details"><pre class="code">
    // Add a button to remove this stock from the table.
    Button removeStockButton = new Button("x");
<span class="highlight">    removeStockButton.addStyleDependentName("remove");</span></pre></div>
    </li>
    <li>
        <div class="header">Save your changes and then press Refresh in the browser running development mode to see them.</div>
        <div class="details">The Remove button is wider than it is tall. The Add button is unaffected by this change.</div>
    </li>
    <li>
        <div class="header">Now the resulting generated HTML has two class attributes.</div>
        <div class="details"><pre class="code">
&lt;button class="gwt-Button gwt-Button-remove" tabindex="0" type="button"&gt;x&lt;/button&gt;</pre></div>
    </li>
</ol>
<a name="dynamic"></a>
<h2>5. Updating styles dynamically</h2>
<p>
The final style change you want to implement is changing the color of the price change. If the stock price goes up, StockWatcher displays it in green; down, in red; no change, in black. This is the one style that changes dynamically as StockWatcher runs.
</p>
<p>
You've already applied an HTML class attribute to the cell element to right-align the numeric values inside the cells. To keep the code simple, it would be nice if you could apply the HTML class attributes just to the text inside the cell. An easy way to do this would be to use a nested widget. In this case, you'll insert a Label widget inside every cell inside column 2.
</p>

<ol class="instructions">
    <li>
        <div class="details">Define the style.</div>
        <div class="details">In StockWatcher.css, add these style rules.<pre class="code">
/* stock list, the Remove button */
.gwt-Button-remove {
  width: 50px;
}

<span class="highlight">/* Dynamic color changes for the Change field */
.noChange {
  color: black;
}

.positiveChange {
  color: green;
}

.negativeChange {
  color: red;
}</span></pre></div>
    </li>
    <li>
        <div class="header">Insert a Label widget in a table cell.</div>
        <div class="details">In StockWatcher.java, in the addStock method, create a Label widget for every cell in column 2.</div>
        <div class="details"><pre class="code">
    // Add the stock to the table.
    int row = stocksFlexTable.getRowCount();
    stocks.add(symbol);
    stocksFlexTable.setText(row, 0, symbol);
<span class="highlight">    stocksFlexTable.setWidget(row, 2, new Label());</span>
    stocksFlexTable.getCellFormatter().addStyleName(row, 1, "watchListNumericColumn");
    stocksFlexTable.getCellFormatter().addStyleName(row, 2, "watchListNumericColumn");
    stocksFlexTable.getCellFormatter().addStyleName(row, 3, "watchListRemoveColumn");</pre></div>
        <div class="details">Instead of setting the text on the table cells, now you have to set the text for the Label widget.</div>
    </li>
    <li>
        <div class="header">Set text on the changeWidget.</div>
        <div class="details">In the updateTable(StockPrice) method, delete the call to setText for the Change column (column 2).</div>
        <div class="details">Create an instance of the Label widget and call it changeWidget.</div>
        <div class="details">Set the text on changeWidget.</div>
        <div class="details"><pre class="code">
    // Populate the Price and Change fields with new data.
    stocksFlexTable.setText(row, 1, priceText);
    <span class="strike">stocksFlexTable.setText(row, 2, changeText + " (" + changePercentText + "%)");</span>
<span class="highlight">    Label changeWidget = (Label)stocksFlexTable.getWidget(row, 2);
    changeWidget.setText(changeText + " (" + changePercentText + "%)");</span></pre></div>
    </li>
        <li>
        <div class="header">Change the color of each changeWidget based on its value.</div>
        <div class="details"><pre class="code">
    // Populate the Price and Change fields with new data.
    stocksFlexTable.setText(row, 1, priceText);
    Label changeWidget = (Label)stocksFlexTable.getWidget(row, 2);
    changeWidget.setText(changeText + " (" + changePercentText + "%)");

<span class="highlight">    // Change the color of text in the Change field based on its value.
    String changeStyleName = "noChange";
    if (price.getChangePercent() &lt; -0.1f) {
      changeStyleName = "negativeChange";
    }
    else if (price.getChangePercent() &gt; 0.1f) {
      changeStyleName = "positiveChange";
    }

    changeWidget.setStyleName(changeStyleName);</span></pre></div>
        </li>
    <li>
        <div class="header">Save your changes and then press Refresh in the browser running development mode to see them.</div>
        <div class="details">The color of the values in the Change field are red, green, or black depending on whether the change was negative, positive, or none.</div>
    </li>
</ol>

<a name="HTMLelement"></a>
<h2>6. Setting an element's HTML attributes</h2>
<p>
Occasionally, you do want to set style attributes directly on an HTML element rather than define a style rule in CSS. For example, the HTML table element has a cellpadding attribute that is convenient for setting the padding on all the cells in the table.
</p>
<p>
In GWT, depending on the HTML element, you can set some attributes in the Java code to generate the appropriate HTML.
</p>
<ol class="instructions">
    <li>
        <div class="header">Specify the cellpadding for the stock table.</div>
        <div class="details">In StockWatcher.java, in the onModuleLoad method, add the setCellPadding method.</div>
        <div class="details"><pre class="code">
  public void onModuleLoad() {
    // Create table for stock data.
    stocksFlexTable.setText(0, 0, "Symbol");
    stocksFlexTable.setText(0, 1, "Price");
    stocksFlexTable.setText(0, 2, "Change");
    stocksFlexTable.setText(0, 3, "Remove");

    // Add styles to elements in the stock list table.
<span class="highlight">    stocksFlexTable.setCellPadding(6);</span></pre></div>
    </li>
    <li>
        <div class="header">Save your changes and then press Refresh in the browser running development mode to see them.</div>
    </li>
</ol>

<a name="staticResource"></a>
<h2>7. Adding images or other static HTML elements</h2>
<p>
Your application's HTML host page can include whatever additional static HTML elements you require. For example, in StockWatcher, you'll add the Google Code logo. To include images, put them in the project's public directory. The GWT compiler will copy all the necessary files to the output directory for deployment.
</p>
<p><img src="images/GoogleCode.png" alt="Google Code logo" />
</p>
<p>
To include static images in the application.
</p>
<ol class="instructions">
     <li>
        <div class="header">Create an directory to hold the image files associated with this application.</div>
        <div class="details">In the war directory, create an images directory.</div>
        <div class="details"><pre class="code">
StockWatcher/war/images</pre></div>
        </li>
        <li>
        <div class="header">From this page, copy the image of the logo and paste it into the images directory.</div>
        <div class="details"><pre class="code">
 StockWatcher/war/images/GoogleCode.png</pre></div>
        </li>
        <li>
        <div class="details">In StockWatcher.html, insert an img tag pointing to the logo file.</div>
        <div class="details"><pre class="code">
&lt;!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"&gt;

&lt;html&gt;
  &lt;head&gt;
    &lt;meta http-equiv="content-type" content="text/html; charset=UTF-8"&gt;

    &lt;link type="text/css" rel="stylesheet" href="StockWatcher.css"&gt;

    &lt;title&gt;StockWatcher&lt;/title&gt;

    &lt;script type="text/javascript" language="javascript" src="stockwatcher/stockwatcher.nocache.js"&gt;&lt;/script&gt;
  &lt;/head&gt;

  &lt;body&gt;

<span class="highlight">    &lt;img src="images/GoogleCode.png" /&gt;</span>

    &lt;h1&gt;StockWatcher&lt;/h1&gt;

    &lt;div id="stockList"&gt;&lt;/div&gt;

    &lt;iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1' style="position:absolute;width:0;height:0;border:0"&gt;&lt;/iframe&gt;

  &lt;/body&gt;
&lt;/html&gt;</pre></div>
      <p class="note"><b>Note:</b> HTML comments have been omitted for brevity.</p>
    </li>
    <li>
        <div class="header">Save your changes and then press Refresh in the browser running development mode to see them.</div>
    </li>
</ol>

<p class="note"><b>In Depth:</b> For more information on including style sheets, JavaScript files, and other GWT modules, see the Developer's Guide, <a href="../DevGuideOrganizingProjects.html#DevGuideAutomaticResourceInclusion">Automatic Resource Inclusion</a>.</p>



<h2>What's Next</h2>
<p>
At this point you've finished with the initial implementation of StockWatcher. The client-side functionality is working and the user interface has a new visual design.
</p>
<img src="images/StyleAfter.png" alt="StockWatcher after applying styles" />
<p class="note">
  <b>Note:</b> For the sake of simplicity, we created the user interface for this tutorial programmatically using widgets.  This works
  fine for StockWatcher because the UI is fairly simple.  However, GWT has a powerful tool called
  <a href="../DevGuideUiBinder.html">UiBinder</a> that allows you to create complex interfaces using declarative XML
  files, which can reduce code size and complexity.  Check out Developer's Guide sections on
  <a href="../DevGuideUiBinder.html">Declarative Layout with UiBinder</a> and
  <a href="../DevGuideUi.html">Build User Interfaces</a> for more info about UiBinder and UI design in general. 
</p>
<p>
Now you're ready to compile StockWatcher. You'll compile your Java code into JavaScript and check that StockWatcher runs the same way in production mode as it has in development mode.
</p>
<p>
<a href="compile.html">Step 8: Compiling a GWT Application</a>
</p>

