<ol class="toc" id="pageToc">
  <li><a href="#PluralOverview">Overview</a></li>
  <li><a href="#PluralExample">Example</a></li>
  <li><a href="#ExactValues">Exact Values</a></li>
  <li><a href="#Offsets">Offsets</a></li>
  <li><a href="#Lists">Lists</a></li>
</ol>

<h2 id="PluralOverview">Overview</h2>

<p>Most languages alter the form of a word being counted based on the count.
For example, in English:
<pre class="prettyprint">
You have 1 tree.
You have 2 trees.
</pre></p>

<p>Other languages have different rules:
<ul>
  <li>In French, the singular form is used for 0 as well as 1</li>
  <li>Arabic has 5 special plural forms in addition to the default</li>
  <li>Some languages don't have plural forms at all</li>
</ul></p>

<p>GWT provides a way to choose different messages based on the count of
something at runtime, using the <a
  href="DevGuideI18nMessages.html"><tt>Messages</tt></a> interface, and
  provides plural rules for hundreds of languages by default.</p>

<h2 id="PluralExample">Example</h2>

<p>First, an example <tt>Messages</tt> interface:
<pre class="prettyprint">
@DefaultLocale("en") // not required since this is the default
public interface MyMessages extends Messages {
  @DefaultMessage("There are {0,number} items in your cart.")
  @AlternateMessage({"one", "There is 1 item in your cart."})
  String cartItems(@PluralCount int itemCount);
}
</pre></p>
<p>Note that the parameter which controls which plural form is used is marked
with the <a
href="/javadoc/latest/com/google/gwt/i18n/client/Messages.PluralCount.html"><tt>@PluralCount</tt></a>
annotation, and that the plural forms for the default language (<tt>en</tt>
unless specified with <a
href="/javadoc/latest/com/google/gwt/i18n/client/LocalizableResource.DefaultLocale.html"><tt>@DefaultLocale</tt></a>
are defined in the <a
href="/javadoc/latest/com/google/gwt/i18n/client/Messages.AlternateMessage.html"><tt>@AlternateMessage</tt></a>
annotation.  If your default language is not English, you may have a different
set of plural forms here.</p>

<p>Let's assume you have <a
  href="DevGuideI18nLocale.html#LocaleModule">added</a> the <tt>en</tt>,
<tt>fr</tt> and <tt>ar</tt> locales to your module.  Now you need translations
for each of these locales (except <tt>en</tt>, which will be picked up from the
annotations).  <i>Note: I am using English in these "translations" for
  clarity -- you would actually want to use real translations.</i></p>


<p><tt>MyMessages_fr.properties</tt>
<pre class="prettyprint">
cartItems=There are {0,number} items in your cart.
cartItems[one]=There is {0,number} item in your cart.
</pre></p>
<p>Note that the <tt>&quot;one&quot;</tt> plural form in French is used for
both 0 and 1, so you can't hard-code the count in the string like you can
for English.</p>

<p><tt>MyMessages_ar.properties</tt>
<pre class="prettyprint">
cartItems=There are {0,number} items in your cart.
cartItems[none]=There are no items in your cart.
cartItems[one]=There is one item in your cart.
cartItems[two]=There are two items in your cart.
cartItems[few]=There are {0,number} items in your cart, which are few.
cartItems[many]=There are {0,number} items in your cart, which are many.
</pre></p>
<p>The Arabic plural rules that GWT uses are:
<ul>
  <li>none - the count is 0
  <li>one - the count is 1
  <li>two - the count is 2
  <li>few - the last two digits are from 03-10
  <li>many - the last two digits are from 11-99
  <li>The default form is used for everything else, ie. 101, 202, etc.
</ul></p>

<p>The standards for how to represent plural forms in translations is still
a work in progress.  Properties files don't have any particular support, so
we invented the <tt>[<i>plural_form</i>]</tt> syntax to specify them.
Hopefully this will improve over time, and we can support more standard
approaches to getting translated messages with plural forms back into GWT.</p>



<h2 id="ExactValues">Exact Values</h2>

<p>Sometimes you need to provide special messages, even if the grammar of the
language doesn't require it.  For example, it is generally better to say
something like <tt>"You have no messages"</tt> rather than <tt>"You have 0
messages"</tt>.  You can specify that using a plural form <tt>"=N"</tt>, such
as:

<pre class="prettyprint">
public interface MyMessages extends Messages {
  @DefaultMessage("There are {0,number} items in your cart.")
  @AlternateMessage({
      "one", "There is 1 item in your cart.",
      "=0", "Your cart is empty."
  })
  String cartItems(@PluralCount int itemCount);
}
</pre></p>

<p>and the properties file entry would look like:


<pre class="prettyprint">
cartItems[\=0]=Your cart is empty.
</pre>


<p>Note the escaping of the equals sign, since that separates the key from the
value in a properties file.

<p>See the next item for another use of Exact Values.

<h2 id="Offsets">Offsets</h2>

<p>In some cases, you may want to alter the count before applying the plural
rules to it.  For example, if you are saying <tt>"Bob, Joe, and 3 others ate
pizza"</tt>, you probably have a list of 5 people.  You could specifically
code subtracting that and choosing different messages based on the number of
people, but it is much easier and likely to get better translations by keeping
all the different messages together.  You can do it like this:


<pre class="prettyprint">
public interface MyMessages extends Messages {
  @DefaultMessage("{1}, {2} and {0} others are here.")
  @AlternateMessage({
      "=0", "Nobody is here.",
      "=1", "{1} is  here.",
      "=2", "{1} and {2} are here.",
      "one", "{1}, {2}, and one other are here."
  })
  String peopleHere(@PluralCount @Offset(2) String[] names, String name1,
      String name2);
}

...

String[] names;
alert(peopleHere(names, names.length &gt; 0 ? names[0] : null,
    names.length &gt; 1 ? names[1] : null));
</pre></p>


<p>Note that you can pass in an array for a <tt>@PluralCount</tt> parameter --
its length is used for the count (<tt>java.util.List</tt> implementations work
similarly).  The <tt>@Offset</tt> annotation indicates that the supplied offset
should be applied before looking up the correct plural rule.  However, note
that exact value matches are compared before the offset is applied.  So, when
the count is 0, <tt>"Nobody is here"</tt> is chosen; if the count is 3,
<tt>"{1}, {2}, and one other are here"</tt> is chosen because 2 is subtracted
from the count before looking up the plural form to use.

<p>BTW, we know it is somewhat klunky to have to pass in the names this way.
In the future, we will add a way of referencing elements in the list/array
from the placeholders, where you could simply call <tt>peopleHere(names)</tt>.

<h2 id="Lists">Lists</h2>

<p>This is slightly off-topic for plurals, but it is related.  GWT supports
formatting lists, using the locale-appropriate separators.  For example:

<pre class="prettyprint">
public interface MyMessages extends Messages {
  @DefaultMessage("Orders {0,list,number} are ready for pickup.")
  @AlternateMessage({
      "=0", "No orders are ready for pickup.",
      "one", "Order {0,list,number} is ready for pickup."
  })
  String ordersReady(@PluralCount List&lt;Integer&gt; orders);
}
</pre></p>

The format specifier <tt>{0,list,number}</tt> says that argument 0 is to be
formatted as a list, with each element formatted as a number.  The same format
options are available as if it weren't an element in a list, so
<tt>{0,list,number:curcode=USD,currency}</tt> would work too.  As before,
either arrays or <tt>java.util.List</tt> instances work fine, and the
requirements of types for formatting remain the same as if it weren't a list.

<p>In English, the results would be:
<ul>
  <li><tt>ordersReady(Arrays.asList())</tt> =&gt; <tt>"No orders are ready for
    pickup."</tt>
  <li><tt>ordersReady(Arrays.asList(14)})</tt> =&gt; <tt>"Order 14 is ready for
    pickup."</tt>
  <li><tt>ordersReady(Arrays.asList(14, 17))</tt> =&gt; <tt>"Orders 14 and 17 are
    ready for pickup."</tt>
  <li><tt>ordersReady(Arrays.asList(14, 17, 21))</tt> =&gt; <tt>"Orders 14, 17,
    and 21 are ready for pickup."</tt>
</ul>

<p>Note that GWT only knows about the default list separators used for a
language, and that while you might want to say something like <tt>"a, b, or
c"</tt>, there is currently no way to express that.