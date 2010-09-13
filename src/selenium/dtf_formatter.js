/**
 * Parsing is currently not supported.
 * 
 * @param testCase TestCase to update
 * @param source The source to parse
 */
function parse(testCase, source) {
    alert('Parsing DTF output is not supported yet.');
}

function tagL(name, command) {
    return '<' + name + ' locator="' + command.target + '"/>';
}

function tagP(name, command) {
    return '<' + name + ' pattern="' + command.target + '"/>';
}

function tagLC(name, command) {
    return '<' + name + ' locator="' + command.target + '" coordstring="' + 
           command.value + '"/>';
}

function tagLV(name, command) {
    return '<' + name + ' locator="' + command.target + '" value="' + 
           command.value + '"/>';
}

function tagT(name, timeout) {
    return '<' + name + ' timeout="' + timeout + '"/>';
}

function tagA(xml) {
    return '<assert>' + xml + '</assert>';
}

function tagWhileNot(tab, xml) { 
    var result = '';
    result += tab + '<while>\n';
    result += tab + '    <not>' + xml + '</not>\n';
    result += tab + '    <sleep time="1s"/>\n';
    result += tab + '</while>\n'; 
    return result; 
}

/**
 * Format TestCase and return the source.
 * 
 * @param testCase
 * @param name
 */
function format(testCase, name) {
    var result = '';
    var tab = '';
    
    if (options.type == 'function') {
        result = '<function name="' + options.name + '">\n';
        tab += '    ';
    } else {
        result += '<script name="' + options.name + '">\n';
        result += '    <info>\n' + 
                  '        <author>\n' + 
                  '            <name>' + options.auth + '</name>\n' +
                  '            <email>' + options.mail + '</email>\n' +
                  '        </author>\n' +
                  '        <description>' + options.desc + '</description>\n' +
                  '    </info>\n\n' + '    <sequence>\n';
        tab += '    ';
        tab += '    ';
    }
  
    result += tab + '<selenium host="' + options.host + '" ' + 
                              'port="' + options.port + '" ' +
                              'baseurl="http://' + options.url + '" >\n';
    tab += '    ';

    var id = 0;
    var commands = testCase.commands;
    for ( var i = 0; i < commands.length; i++) {
        var command = commands[i];
        if (command.type == 'command') {
            if (command.command == 'open') {
                result += tab;
                result += '<open url="' + command.target + '"/>\n';
            } else if (command.command == 'type') {
                result += tab + tagLV('type', command) + '\n';
            } else if (command.command == 'waitForPageToLoad') {
                result += tab + tagT('waitForPageToLoad', command.target) +'\n';
                
            // click commands
            } else if (command.command == 'click') {
                result += tab + tagL('click', command) +'\n';
            } else if (command.command == 'clickAt') {
                result += tab + tagL('clickAt', command) + '\n';
            } else if (command.command == 'doubleClick') {
                result += tab + tagL('doubleClick', command) +'\n';
            } else if (command.command == 'doubleClickAt') {
                result += tab + tagLC('doubleClickAt', command) +'\n';
            } else if ( command.command == 'clickAndWait' ){
                result += tab + tagL('click', command) +'\n';
                result += tab + tagT('waitForPageToLoad', options.wait) +'\n';
            } else if ( command.command == 'doubleClickAndWait' ){
                result += tab + tagL('doubleClick', command) +'\n';
                result += tab + tagT('waitForPageToLoad', options.wait) +'\n';
            } else if ( command.command == 'clickAtAndWait' ){
                result += tab + tagL('clickAt', command) + '\n';
                result += tab + tagT('waitForPageToLoad', options.wait) +'\n';
            } else if ( command.command == 'doubleClickAtAndWait' ){
                result += tab + tagL('doubleClickAt', command) +'\n';
                result += tab + tagT('waitForPageToLoad', options.wait) +'\n';
                
            // assert commands
            } else if (command.command == 'assertElementPresent') {
                result += tab + tagA(tagL('isElementPresent', command)) + '\n';
            } else if (command.command == 'assertTextPresent') {
                result += tab + tagA(tagP('isTextPresent', command)) + '\n';
                
            // verify commands
            } else if (command.command == 'verifyTextPresent') {
                result += tab + tagA(tagP('isTextPresent', command)) + '\n';
            } else if (command.command == 'verifyTitle') {
                result += tab;
                result += '<getTitle property="sel.gettitle-' + id + '"/>\n';
                result += tab;
                result += '<assert><eq op1="${sel.gettitle-' + id + '}" op2="' +
                          command.target + '"/></assert>\n';
                id++;
                
            // waitfor commands
            } else if (command.command == 'waitForTextPresent') {
                result += tagWhileNot(tab, tagP('isTextPresent', command));
            } else if (command.command == 'waitForElementPresent') {
                result += tagWhileNot(tab, tagL('isElementPresent', command));
            } else if (command.command == 'waitForTitle') {
                result += tab + '<getTitle property="sel.gettitle-' + id + 
                                '"/>\n';
                result += tab + '<while>\n';
                result += tab + '    <neq op1="${sel.gettitle-' + id + 
                          '}" op2="' + command.target + '"/>\n';
                result += tab + '    <sleep time="1s"/>\n';
                result += tab + '    <getTitle property="sel.gettitle-' + id + 
                                '"/>\n';
                result += tab + '</while>\n';
                id++;
                
            } else {
                alert("Unhandled command [" + command.command + "].");
            }
        }
    }

    if (options.type == 'function') {
        result += '    </selenium>\n';
        result += '</function>\n';
    } else {
        result += '        </selenium>\n';
        result += '    </sequence>\n';
        result += '</script>\n';
    }

    return result;
}

options = {
    // Test Output Options
    auth : 'John Doe',
    mail : 'noman@nowhere',
    desc : 'selenium generated testcase', 
    
    type : 'function',
    name : 'selenium',
   
    // Selenium Server Options
    host : 'localhost',
    port : '44444',
    url  : '${baseURL}',
   
    // Default Values
    wait : '30000'
};

configForm = '<description>Output Type</description>' + 
             '<menulist id="options_type">' + 
             '<menupopup>' + 
             '<menuitem label="Function Output" value="function"/>' + 
             '<menuitem label="Script Output" value="script"/>' + 
             '</menupopup>' + 
             '</menulist>' + 
             '<description>Test Case Author Name</description>' +
             '<textbox id="options_auth"/>' +
             '<description>Test Case Author Email</description>' +
             '<textbox id="options_mail"/>' +
             '<description>Test Case Description</description>' +
             '<textbox id="options_desc"/>' +
             '<description>Type Name (name to use for function or script)</description>' +
             '<textbox id="options_name"/>' +
             '<description>Selenium Server host</description>' +
             '<textbox id="options_host"/>' +
             '<description>Selenium Server Port</description>' +
             '<textbox id="options_port"/>' +
             '<description>Default Wait Time(ms)</description>' +
             '<textbox id="options_wait"/>';
