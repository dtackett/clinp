var fs = require("fs");
var page = require('webpage').create();
var url = phantom.args[0];

page.onConsoleMessage = function (message) {
    fs.write("/dev/stdout", message, "w");
};

console.log("Loading URL: " + url);

page.open(url, function (status) {
    if (status != "success") {
        console.log('Failed to open ' + url);
        phantom.exit(1);
    }

    console.log("Running test.");

    var result = page.evaluate(function() {

        speclj.run.standard.armed = true;
        return speclj.run.standard.run_specs(
          cljs.core.keyword("color"), true
        );
    });

    // NOTE: PhantomJS 1.4.0 has a bug that prevents the exit codes
    //        below from being returned properly. :(
    //
    // http://code.google.com/p/phantomjs/issues/detail?id=294

    if (result != 0) {
        console.log("*** Test failed! ***");
        phantom.exit(1);
    }

    console.log("Test succeeded.");
    phantom.exit(0);
});
