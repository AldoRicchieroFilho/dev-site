<h2 id="Overview">Things to keep in mind</h2>

<h3>Modes</h3>
<p>
Running an application in IE9 does not necessarily mean you are running IE9
standards mode. IE9 has many modes that can be defined in the page head tag
(see the "Document Mode" section below). You can overwrite the page mode
manually by selecting F12, where you can set both browser mode and
document mode.
</p>

<p>GWT IE permutations work best with each version of "standards" mode. Mixing
modes, say browser mode = 7 and document mode = 9, is not recommended and the
behavior is undefined. To keep it simple, try to keep browser modes and
document modes the same. If you must use mixed mode, be aware that you may run
into issues that are still not supported. The exception is if you are
emulating an older browser when you still do not support the new version, for
instance, you emulate IE7 (EmulateIE7) on IE9.
</p>

<h3>Filling Bugs</h3>
<p>
Due to the many 'modes', when filling issues, make sure to add both the browser
and document mode; and the browser version. This will help us triage what
is IE9 specific, what is related to older versions or if the issue is related
to mixed 'mode' setting.
<ul>
<li>To get the browser mode and document mode select "Menu > Tools >
Developer"</li>
<li>To get the version, select "Help > About Internet Explorer"</li>
</ul>
</p>

<h3>Document Mode</h3>
<p>
It is important to understand how compatibility mode works before you release
a new version of your app. If you are using X-UA-compatible tag, test on older
browsers as well.
</p>

<p>
In short, whenever possible, use standards mode by adding &lt;!DOCTYPE html> as
the first element in your html file; and add &lt;meta http-equiv="X-UA-Compatible"
content="IE=9" > to &lt;head> to future proof your app. Avoid using 'edge' unless
you are sure what you are doing.
</p>

<pre class="prettyprint">
&lt;!DOCTYPE html&gt;
&lt;html&gt;
&lt;head&gt;
&lt;meta http-equiv="X-UA-Compatible" content="IE=9"&gt;
&lt;/head&gt;
&lt;body&gt;
&lt;script language='javascript'&gt;
..
</pre>

<p>
More information can be found here: <a href="http://msdn.microsoft.com/en-us/library/cc288325%28v=vs.85%29.aspx">http://msdn.microsoft.com/en-us/library/cc288325%28v=vs.85%29.aspx</a>
</p>

<h3>Standards Mode and Potential Hardware Acceleration/Performance Issues</h3>

<p>
In some cases IE9 will run in Software Rendering mode and bypass any hardware
acceleration. In these cases you may see degraded performance. To see if you are
running hardware accelerated environment, select "Tools > Internet Options >
Advanced Tab."
</p>

<p>
More information can be found here: <a href="http://support.microsoft.com/kb/2398082">http://support.microsoft.com/kb/2398082</a>
</p>

<h3>IE9 Compatibility View and Intranet Sites</h3>
</h3>

<p>
By default, all intranet sites are set to run in compatibility mode. To change
that, go to "Tools > Compatibility View Settings" and uncheck "Display intranet
sites in Compatibility View". Internet sites, except those added to the 
"Websiteslist you've added to Compatibility View" are by default in
non-compatibility view.
</p>


<h3>New User Agent</h3>

<p>
Fall back bindings have been implemented, thus the introduction of the new
user.agent should not cause build breaks. For instance, IE9 uses IE8 as fall
back. If your application has a specific binding for IE8 (say a custom widget)
that is not implemented for IE9, the compiler will automatically use the IE8
implementation. A warning will be raised during compilation (see below), and
it's suggested that you verify the implementation works as expected in IE9.
</p>

<p>
<i>
Could not find an exact match rule. Using 'closest' rule &lt;replace-with
class='com.google.gwt.widget.client.impl.MySuperDuperWidgetIE6'/> based on fall
back values. You may need to implement a specific binding in case the fall back
behavior does not replace the missing binding</i>
</i>

<p>
This is telling you that the IE9 permutation of your application will use
MySuperDuperWidgetIE6. Your module binds IE8 to this implementation, and since
there is no explicit binding for IE9, it will fall back to whatever binding IE8
is using (in this case a baseline IE6 implementation). The action item here is
to verify that this implementation works as expected in IE9 standards mode.
</p>

<h3>
Release Notes and References
</h3>

<p>
Release Notes:
<br/>
<a href="http://msdn.microsoft.com/en-us/ie/ff959805">http://msdn.microsoft.com/en-us/ie/ff959805</a>
<br/>
<a href="http://msdn.microsoft.com/en-us/ie/ff468705">http://msdn.microsoft.com/en-us/ie/ff468705</a>
</p>

<p>
References:
<br/>
<a href="http://msdn.microsoft.com/en-us/library/cc288325%28v=vs.85%29.aspx">http://msdn.microsoft.com/en-us/library/cc288325%28v=vs.85%29.aspx</a>
<br/>
<br/>
<a href="http://support.microsoft.com/kb/2398082">http://support.microsoft.com/kb/2398082</a>
<br/>
<a href="http://support.microsoft.com/kb/2528233">http://support.microsoft.com/kb/2528233</a>
</p>