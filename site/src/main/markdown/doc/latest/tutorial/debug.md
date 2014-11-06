
<style>
code, .code {font-size: 9pt; font-family: Courier, Courier New, monospace; color:#007000;}
.highlight {background-color: #ffc;}
.strike {text-decoration:line-through; color:red;}
.header {margin-top: 1.5ex;}
.details {margin-top: 1ex;}
</style>

<p>
At this point, you've finished implementing the StockWatcher UI and all its client-side functionality. However, you've noticed that there is an error in the Change field. The percentage of change is not calculating correctly.
</p>
<p>
In this section, you'll use Eclipse to debug your Java code while running StockWatcher in development mode.
</p>
<ol>
    <li><a href="#findBug">Find the bug.</a></li>
    <li><a href="#fixBug">Fix the bug.</a></li>
    <li><a href="#testFix">Test the bug fix by running StockWatcher in development mode.</a></li>
</ol>
<h4>Benefits</h4>
<p>
You can debug the Java source code before you compile it into JavaScript. This GWT development process help you take advantage of the debugging tools in your Java IDE. You can:
</p>
<ul>
    <li>Set break points.</li>
    <li>Step through the code line by line.</li>
    <li>Drill down in the code.</li>
    <li>Inspect the values of variables.</li>
    <li>Display the stack frame for suspended threads.</li>
</ul>
<p>
One of attractions of developing in JavaScript is that you can make changes and see them immediately by refreshing the browser&mdash;without having to do a slow compilation step. GWT development mode provides the exact same development cycle. You do <b>not</b> have to recompile for every change you make; that's the whole point of development mode. Just click "Refresh" to see your updated Java code in action.
</p>
<a name="findBug"></a>
<h2>1. Finding the bug</h2>

<h3>Analyzing the problem</h3>
<img src="images/CodeClientBug.png" alt="screenshot StockWatcher Bug" />
<p>
Looking at the values in the Price and Change fields, you can see that, for some reason, all of the change percentages are only 1/10 the size of the correct values.
</p>
<p>
The values for the Change field are loaded by the updateTable(StockPrice) method.
</p>
<pre class="code">
  /**
   * Update a single row in the stock table.
   *
   * @param price Stock data for a single row.
   */
  private void updateTable(StockPrice price) {
    // Make sure the stock is still in the stock table.
    if (!stocks.contains(price.getSymbol())) {
      return;
    }

    int row = stocks.indexOf(price.getSymbol()) + 1;

    // Format the data in the Price and Change fields.
    String priceText = NumberFormat.getFormat("#,##0.00").format(
        price.getPrice());
    NumberFormat changeFormat = NumberFormat.getFormat("+#,##0.00;-#,##0.00");
    String changeText = changeFormat.format(price.getChange());
    String <span class="highlight">changePercentText</span> = changeFormat.format(price.getChangePercent());

    // Populate the Price and Change fields with new data.
    stocksFlexTable.setText(row, 1, priceText);
<span class="highlight">    stocksFlexTable.setText(row, 2, changeText + " (" + changePercentText
        + "%)");</span>
  }</pre>
<p>
Just glancing at the code, you can see that the value of the changePercentText variable is being set elsewhere, in price.getChangePercent. So, first set a breakpoint on that line and then drill down to determine where the error in calculating the change percentage is.
</p>
<h3>Setting break points</h3>
<ol class="instructions">
<li>
    <div class="header">Set a breakpoint on the lines of code you want to step into and where you want to examine variable values.</div>
    <div class="details">In StockWatcher.java, in the updateTable(StockPrice price) method, set a breakpoints on these two lines</div>
    <div class="details"><pre class="code">
String changePercentText = changeFormat.format(price.getChangePercent());</pre></div>
    <div class="details"><pre class="code">
stocksFlexTable.setText(row, 1, priceText);</pre></div>
    <div class="details">Eclipse switches to Debug perspective.</div>
<li>
    <div class="header">Run the code that has the error.</div>
    <div class="details">To run the code in the updateTable method where you suspect the error, just add a stock to the stock list in the browser running in development mode.</div>
    <div class="details">Execution will stop at the first breakpoint.</div>
</li>
<li>
    <div class="header">Check the values of the variables priceText and changeText.</div>
    <div class="details">In the Eclipse Debug perspective, look at the Variables pane.</div>
</li>
<li>
    <div class="header">Run the code to the next break point, where priceText is set.</div>
    <div class="details">In the Debug pane, press the Resume icon.</div>
</li>
<li>
    <div class="header">Check the values of the variables priceText, changeText, changePercentText.</div>
    <div class="details">In the Eclipse Debug perspective, look at the Variables pane. If you like, double-check the math to see the error.</div>
    <div class="details"><img src="images/DebugVariablesBug.png" alt="StockWatcher variables" /></div>
</li>
<li>
    <div class="header">Loop back to the first break point, where changePercentText is set.</div>
    <div class="details">In the Debug pane, press the Resume icon.</div>
</li>

</ol>

<h3>Stepping through the code</h3>
<p>
Now step into the code to see where and how the changePercentText is being calculated.
</p>
<ol class="instructions">
    <li>
        <div class="header">Step into the getChangePercent method to see how it's calculating the value of changePercentText.</div>
        <div class="details"><pre class="code">
  public double getChangePercent() {
    return 10.0 * this.change / this.price;
  }</pre></div>
    </li>
</ol>
<p>
Looking at the getChangePercent method, you can see the problem: it's multiplying the change percentage by 10 instead of 100. That corresponds exactly with the output you saw before: all of the change percentages were only 1/10 the size of the correct values.
</p>

<a name="fixBug"></a>
<h2>2. Fixing the bug</h2>
<ol class="instructions">
    <li>
        <div class="header">Fix the error in calculating the percentage of the price change.</div>
        <div class="details">In StockPrice.java, edit the getChangePercent method.</div>
        <div class="details"><pre class="code">
  public double getChangePercent() {
    return <span class="highlight">100.0</span> * this.change / this.price;
  }</pre></div>
        <p class="note"><b>Tip:</b> In Eclipse, if you find it easier to edit in the Java perspective rather than the Debug perspective, you can switch back and forth while running StockWatcher in development mode.</p>
    </li>
</ol>

<a name="testFix"></a>
<h2>3. Testing the bug fix in development mode</h2>
<p>
At this point when you enter a stock code, the calculation of the Change field should be accurate. Try it and see.
</p>
<ol class="instructions">
    <li>
        <div class="header">In Eclipse, toggle all the breakpoints off and press Resume.</div>
    </li>
    <li>
        <div class="header">In the browser running in development mode, press Refresh.</div>
    </li>
    <li>
        <div class="header">Add a stock.</div>
    </li>
     <li>
        <div class="header">Check the calculation of the value in the Change field.</div>
    </li>
</ol>


<h2>What's Next</h2>
<p>
At this point you've implemented all your functional requirements. StockWatcher is running and you've found and fixed a bug.
</p>
<p>
Now you're ready to enhance StockWatcher's visual design. You'll apply CSS style rules to the GWT widgets and add a static element (a logo) to the page.
</p>
<p>
<a href="style.html">Step 7: Applying Style</a>
</p>


