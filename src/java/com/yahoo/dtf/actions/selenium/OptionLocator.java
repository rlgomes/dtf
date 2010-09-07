package com.yahoo.dtf.actions.selenium;

/**
 * @dtf.feature Option Locator
 * @dtf.feature.group Selenium 
 * 
 * @dtf.feature.desc
 * 
 * <b>Taken directly from selenium documentation</b>
<DD>
    Select an option from a drop-down using an option locator.

    <p>
        Option locators provide different ways of specifying options of an HTML
        Select element (e.g. for selecting a specific option, or for asserting
        that the selected option satisfies a specification). There are several
        forms of Select Option Locator.
</p>
    <ul>
        <li>
            <strong>label</strong>
            =
            <em>labelPattern</em>
            :
            matches options based on their labels, i.e. the visible text. (This
            is the default.)
            <ul class="first last simple">
                <li>label=regexp:^[Oo]ther</li>
            </ul>
        </li>
        <li>
            <strong>value</strong>
            =
            <em>valuePattern</em>
            :
            matches options based on their values.
            <ul class="first last simple">
                <li>value=other</li>
            </ul>
        </li>
        <li>
            <strong>id</strong>
            =
            <em>id</em>
            :

            matches options based on their ids.
            <ul class="first last simple">
                <li>id=option1</li>
            </ul>
        </li>
        <li>
            <strong>index</strong>
            =
            <em>index</em>
            :
            matches an option based on its index (offset from zero).
            <ul class="first last simple">
                <li>index=2</li>
            </ul>
        </li>
    </ul>
    <p>
        If no option locator prefix is provided, the default behaviour is to
        match on
        <strong>label</strong>
        .
    </p>
    <P>
 */
public class OptionLocator {

}
