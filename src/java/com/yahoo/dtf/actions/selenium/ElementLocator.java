package com.yahoo.dtf.actions.selenium;

/**
 * @dtf.feature Element Locator
 * @dtf.feature.group Selenium 
 * 
 * @dtf.feature.desc
 * 
 * <b>Taken directly from selenium documentation</b>
<p>
    Element Locators tell Selenium which HTML element a command refers to.
    The format of a locator is:</p>
<blockquote>
    <em>locatorType</em>
    <strong>=</strong>
    <em>argument</em>
</blockquote>
<p>
    We support the following strategies for locating elements:
</p>
<ul>
    <li>
        <strong>identifier</strong>
        =
        <em>id</em>
        :
        Select the element with the specified @id attribute. If no match is
        found, select the first element whose @name attribute is
        <em>id</em>
        .
        (This is normally the default; see below.)
    </li>
    <li>
        <strong>id</strong>
        =
        <em>id</em>
        :
        Select the element with the specified @id attribute.
    </li>
    <li>
        <strong>name</strong>
        =
        <em>name</em>
        :
        Select the first element with the specified @name attribute.
        <ul class="first last simple">
            <li>username</li>
            <li>name=username</li>
        </ul>
        <p>
            The name may optionally be followed by one or more
            <em>element-filters</em>
            , separated from the name by whitespace. If the
            <em>filterType</em>
            is not specified,
            <strong>value</strong>
            is assumed.
        </p>
        <ul class="first last simple">
            <li>name=flavour value=chocolate</li>
        </ul>
    </li>
    <li>
        <strong>dom</strong>
        =
        <em>javascriptExpression</em>
        :

        Find an element by evaluating the specified string. This allows you to traverse
        the HTML Document Object
        Model using JavaScript. Note that you must not return a value in this string;
        simply make it the last expression in the block.
        <ul class="first last simple">
            <li>dom=document.forms['myForm'].myDropdown</li>
            <li>dom=document.images[56]</li>
            <li>dom=function foo() { return document.links[1]; }; foo();</li>
        </ul>
    </li>
    <li>
        <strong>xpath</strong>
        =
        <em>xpathExpression</em>
        :
        Locate an element using an XPath expression.
        <ul class="first last simple">
            <li>xpath=//img[@alt='The image alt text']</li>
            <li>xpath=//table[@id='table1']//tr[4]/td[2]</li>
            <li>xpath=//a[contains(@href,'#id1')]</li>
            <li>xpath=//a[contains(@href,'#id1')]/@class</li>
            <li>xpath=(//table[@class='stylee'])//th[text()='theHeaderText']/../td
            </li>
            <li>xpath=//input[@name='name2' and @value='yes']</li>
            <li>xpath=//*[text()="right"]</li>
        </ul>
    </li>
    <li>
        <strong>link</strong>
        =
        <em>textPattern</em>
        :
        Select the link (anchor) element which contains text matching the
        specified
        <em>pattern</em>
        .
        <ul class="first last simple">
            <li>link=The link text</li>
        </ul>
    </li>
    <li>
        <strong>css</strong>
        =
        <em>cssSelectorSyntax</em>
        :
        Select the element using css selectors. Please refer to
        <a href="http://www.w3.org/TR/REC-CSS2/selector.html">CSS2 selectors</a>
        ,
        <a href="http://www.w3.org/TR/2001/CR-css3-selectors-20011113/">CSS3 selectors</a>
        for more information. You can also check the TestCssLocators test in the
        selenium test suite for an example of usage, which is included in the
        downloaded selenium core package.
        <ul class="first last simple">
            <li>css=a[href="#id3"]</li>
            <li>css=span#firstChild + span</li>
        </ul>
        <p>Currently the css selector locator supports all css1, css2 and css3
            selectors except namespace in css3, some pseudo
            classes(:nth-of-type, :nth-last-of-type, :first-of-type,
            :last-of-type, :only-of-type, :visited, :hover, :active, :focus,
            :indeterminate) and pseudo elements(::first-line, ::first-letter,
            ::selection, ::before, ::after). </p>
    </li>
</ul>
<p>
    Without an explicit locator prefix, Selenium uses the following default
    strategies:
</p>
<ul class="simple">
    <li>
        <strong>dom</strong>
        , for locators starting with "document."
    </li>
    <li>
        <strong>xpath</strong>
        , for locators starting with "//"
    </li>
    <li>
        <strong>identifier</strong>
        , otherwise
    </li>
</ul>
 */
public class ElementLocator {

}
