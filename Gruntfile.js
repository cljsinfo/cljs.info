var fs = require('fs'),
  marked = require('marked'),
  md5 = require('MD5');

module.exports = function(grunt) {
'use strict';

//------------------------------------------------------------------------------
// Snowflake CSS
// TODO: this should become it's own module and published on npm
//------------------------------------------------------------------------------

function keys(o) {
  var a = [];
  for (var i in o) {
    if (o.hasOwnProperty(i) !== true) continue;
    a.push(i);
  }
  return a;
}

function arrToObj(arr) {
  var o = {};
  for (var i = 0; i < arr.length; i++) {
    o[ arr[i] ] = null;
  }
  return o;
}

function difference(arr1, arr2) {
  var o1 = arrToObj(arr1);
  var o2 = arrToObj(arr2);
  var delta = [];

  for (var i in o1) {
    if (o1.hasOwnProperty(i) !== true) continue;

    if (o2.hasOwnProperty(i) !== true) {
      delta.push(i)
    }
  }

  for (var i in o2) {
    if (o2.hasOwnProperty(i) !== true) continue;

    if (o1.hasOwnProperty(i) !== true) {
      delta.push(i)
    }
  }

  return delta.sort();
}

// Snowflake class names must contain at least one letter and one number
function hasNumbersAndLetters(str) {
  if (str.search(/\d/) === -1) {
    return false;
  }

  if (str.search(/[a-z]/) === -1) {
    return false;
  }

  return true;
}

// returns an array of unique Snowflake classes from a file
function extractSnowflakeClasses(filename, pattern) {
  if (! pattern) {
    pattern = /([a-z0-9]+-){1,}([abcdef0-9]){5}/g;
  }

  var fileContents = grunt.file.read(filename);
  var classes = {};

  var matches = fileContents.match(pattern);

  if (matches) {
    for (var i = 0; i < matches.length; i++) {
      var c = matches[i];

      if (hasNumbersAndLetters(c) === true) {
        classes[c] = null;
      }
    }
  }

  return keys(classes);
}

function snowflakeCount() {
  var cssClasses = extractSnowflakeClasses("public/css/main.min.css"),
    jsServer = extractSnowflakeClasses("app.js"),
    jsClient = extractSnowflakeClasses('public/js/client.min.js'),
    jsClasses = jsServer.concat(jsClient);

  console.log(cssClasses.length + " class names found in css/main.min.css");
  console.log(jsClasses.length + " class names found in JS files");

  console.log("Classes found in one file but not the other:");
  console.log( difference(jsClasses, cssClasses) );
}

// http://tinyurl.com/looyyvc
function hexavigesimal(a) {
  a += 1;
  var c = 0;
  var x = 1;
  while (a >= x) {
    c++;
    a -= x;
    x *= 26;
  }

  var s = "";
  for (var i = 0; i < c; i++) {
    s = "abcdefghijklmnopqrstuvwxyz".charAt(a % 26) + s;
    a = Math.floor(a/26);
  }

  return s;
}

// given a unique array of class names, returns an object of them 
// mapped to short versions
// input:  ["foo-91c46", "bar-aedf3", "baz-2a44d", etc]
// output: {"foo-91c46":"a", "bar-aedf3":"b", "baz-2a44d":"c", etc}
function shrinkClassNames(classes, prefix) {
  if (! prefix) {
    prefix = "";
  }

  var o = {};
  for (var i = 0; i < classes.length; i++) {
    o[ classes[i] ] = prefix + hexavigesimal(i);
  }
  return o;
}

function squeezeClasses() {
  var cssFile = '00-publish/css/main.min.css',
    cssClasses = extractSnowflakeClasses(cssFile),
    cssContents = grunt.file.read(cssFile),
    jsFile = '00-publish/js/cheatsheet.min.js',
    jsClasses = extractSnowflakeClasses(jsFile),
    jsContents = grunt.file.read(jsFile),
    allClasses = keys(arrToObj(cssClasses.concat(jsClasses))).sort(),
    squeezedClasses = shrinkClassNames(allClasses);

  for (var i in squeezedClasses) {
    if (squeezedClasses.hasOwnProperty(i) !== true) continue;
    var regex = new RegExp(i, "g");

    if (jsContents.search(regex) === -1) {
      console.log("class \"" + i + "\" not found in cheatsheet.min.js");
    }

    cssContents = cssContents.replace(regex, squeezedClasses[i]);
    jsContents = jsContents.replace(regex, squeezedClasses[i]);
  }

  grunt.file.write(cssFile, cssContents);
  grunt.file.write(jsFile, jsContents);
}

//------------------------------------------------------------------------------
// Doc Files
//------------------------------------------------------------------------------

function extractNamespace(fullName) {
  fullName = fullName + "";
  var slash = fullName.indexOf("/");
  return fullName.substr(0, slash);
}

function extractName(fullName) {
  fullName = fullName + "";
  var slash = (fullName + "").indexOf("/");
  return fullName.substr(slash + 1);
}

function isSectionLine(line) {
  return line.search(/^=====/) !== -1;
}

// converts each non-empty line of a section into an array
function convertSectionIntoArray(s) {
  // NOTE: this condtion should never happen
  if (s === "") return [];

  var arr1 = s.split("\n"),
    arr2 = [];

  // trim each line and remove empty lines
  for (var i = 0; i < arr1.length; i++) {
    var line = arr1[i].trim();
    if (line === "") continue;

    arr2.push(line);
  }

  return arr2;
}

// parse description as Markdown and some minor trimming
function parseDescription(d) {
  return marked(d)
    .trim()
    .replace(/<\/p> <p>/g, '</p><p>');
}

function transformObjToDocs(obj, autoDocObj) {
  if (autoDocObj) {
    obj["name"]      = autoDocObj["name"];
    obj["type"]      = autoDocObj["type"];
    obj["ns"]        = autoDocObj["ns"];
    obj["docstring"] = autoDocObj["docstring"];
    obj["source"]    = autoDocObj["source"];
    obj["github"]    = autoDocObj["github"];
    obj["signature"] = autoDocObj["signature"];
  }

  // rename "name" to "full-name"
  obj["full-name"] = obj["name"];
  delete obj["name"];

  // parse description
  obj["description-html"] = parseDescription(obj["description"]);
  delete obj["description"];

  // type is either "special form", "macro", or "function"
  // "function" is the default if not specified
  if (obj["type"] !== "special form" && obj["type"] !== "macro") {
    delete obj["type"];
  }

  // convert some sections into arrays
  obj.signature = convertSectionIntoArray(obj.signature);

  if (obj.hasOwnProperty("related") === true) {
    obj.related = convertSectionIntoArray(obj.related);
  }

  return obj;
}

function parseDocFileIntoObject(fileContent) {
  var contentArr = fileContent.split("\n"),
    currentSection = false,
    obj = {};

  for (var i = 0; i < contentArr.length; i++) {
    var line = contentArr[i];

    if (isSectionLine(line) === true) {
      currentSection = line.replace(/^=====/, "").trim().toLowerCase();
      obj[currentSection] = "";
      continue;
    }

    if (currentSection === false) continue;

    obj[currentSection] += line + "\n";
  }

  // trim everything and delete empty sections
  var obj2 = {};
  for (var i in obj) {
    if (obj.hasOwnProperty(i) !== true) continue;

    obj[i] = obj[i].trim();
    if (obj[i] !== "") {
      obj2[i] = obj[i];
    }
  }

  return obj2;
}

// quick check that the file has everything we are expecting
function validDocObj(obj) {
  return obj.hasOwnProperty("name") &&
         obj.hasOwnProperty("signature") &&
         obj.hasOwnProperty("description");
}

function parsedStatus(parsed, skipped) {
  var msg = 'Parsed ' + parsed + ' files';

  if (skipped !== 0) {
    msg += ', skipped ' + skipped;
  }

  msg += '.';

  return msg;
}

// http://tinyurl.com/nxszt3y
function bytes (b) {
  var tb = ((1 << 30) * 1024), gb = 1 << 30, mb = 1 << 20, kb = 1 << 10, abs = Math.abs(b);
  if (abs >= tb) return (Math.round(b / tb * 100) / 100) + 'tb';
  if (abs >= gb) return (Math.round(b / gb * 100) / 100) + 'gb';
  if (abs >= mb) return (Math.round(b / mb * 100) / 100) + 'mb';
  if (abs >= kb) return (Math.round(b / kb * 100) / 100) + 'kb';
  return b + 'b';
}

function filesize(filename) {
 var stats = fs.statSync(filename),
   size = stats["size"];

 return bytes(size);
}

function buildDocs() {
  var docs = {},
    parsed = 0,
    skipped = 0;

  grunt.file.recurse("docs", function(docPath) {
    // skip non .cljsdoc files
    if (docPath.search(/\.cljsdoc$/) === -1) return;

    var content = grunt.file.read(docPath),
      docObj = parseDocFileIntoObject(content);

    // quick sanity check that the file is in a good format
    if (validDocObj(docObj) !== true) {
      skipped++;
      grunt.log.error("Skipped file '" + docPath + "'. Invalid format.");
      return;
    }

    parsed++;

    // load auto-generated supplement doc
    var autoDocPath = docPath.replace(/^docs/, "docs-generated"),
      autoDocObj = null;

    if (grunt.file.exists(autoDocPath)) {
      content = grunt.file.read(autoDocPath);
      autoDocObj = parseDocFileIntoObject(content);
    }
    else {
      grunt.log.error("No auto-doc found: " + autoDocPath);
    }

    // clean and transform the data
    var obj = transformObjToDocs(docObj, autoDocObj);

    docs[obj["full-name"]] = obj;
  });

  var docsFile = 'public/cheatsheet/docs.json';
  grunt.file.write(docsFile, JSON.stringify(docs));

  // log status
  grunt.log.writeln(parsedStatus(parsed, skipped));
  grunt.log.writeln('Created ' + docsFile + ' (' + filesize(docsFile) + ')');
}

//------------------------------------------------------------------------------
// Grunt Config
//------------------------------------------------------------------------------

grunt.initConfig({

  clean: {
    options: {
      force: true
    },

    // remove all the files in the 00-publish folder
    pre: ['00-publish']
  },

  copy: {
    cheatsheet: {
      files: [
        { src: 'public/cheatsheet/index.html', dest: '00-publish/cheatsheet/index.html' },
        { src: 'public/cheatsheet/docs.json', dest: '00-publish/cheatsheet/docs.json' },
        { src: 'public/css/main.min.css', dest: '00-publish/css/main.min.css' },
        { src: 'public/fonts/*', dest: '00-publish/fonts/', expand: true, flatten: true },
        { src: 'public/img/*', dest: '00-publish/img/', expand: true, flatten: true },
        { src: 'public/js/cheatsheet.min.js', dest: '00-publish/js/cheatsheet.min.js' },
        { src: 'public/js/libs/jquery-2.1.1.min.js', dest: '00-publish/js/libs/jquery-2.1.1.min.js' }
      ]
    }
  },

  less: {
    options: {
      compress: true
    },

    watch: {
      files: {
        'public/css/main.min.css': 'less/main.less'
      }
    }
  },

  watch: {
    options: {
      atBegin: true
    },

    less: {
      files: "less/*.less",
      tasks: "less:watch"
    },

    docs: {
      files: "docs/*.cljsdoc",
      tasks: "build-docs"
    }
  }

});

function cheatsheetSanityCheck() {
  if (! grunt.file.exists('public/js/cheatsheet.min.js')) {
    grunt.fail.warn('Could not find public/js/cheatsheet.min.js! Aborting...');
  }
}

function hashCheatsheetFiles() {
  var cssFile = grunt.file.read('00-publish/css/main.min.css'),
    cssHash = md5(cssFile).substr(0, 8),
    jsFile = grunt.file.read('00-publish/js/cheatsheet.min.js'),
    jsHash = md5(jsFile).substr(0, 8),
    htmlFile = grunt.file.read('00-publish/cheatsheet/index.html');

  // write the new files
  grunt.file.write('00-publish/css/main.min.' + cssHash + '.css', cssFile);
  grunt.file.write('00-publish/js/cheatsheet.min.' + jsHash + '.js', jsFile);

  // delete the old files
  grunt.file.delete('00-publish/css/main.min.css');
  grunt.file.delete('00-publish/js/cheatsheet.min.js');

  // update the HTML file
  grunt.file.write('00-publish/cheatsheet/index.html',
    htmlFile.replace('main.min.css', 'main.min.' + cssHash + '.css')
    .replace('cheatsheet.min.js', 'cheatsheet.min.' + jsHash + '.js'));

  // show some output
  grunt.log.writeln('00-publish/css/main.min.css → ' +
                    '00-publish/css/main.min.' + cssHash + '.css');
  grunt.log.writeln('00-publish/js/cheatsheet.min.js → ' +
                    '00-publish/js/cheatsheet.min.' + jsHash + '.js');
}

// load tasks from npm
grunt.loadNpmTasks('grunt-contrib-clean');
grunt.loadNpmTasks('grunt-contrib-copy');
grunt.loadNpmTasks('grunt-contrib-less');
grunt.loadNpmTasks('grunt-contrib-watch');

grunt.registerTask('build-docs', buildDocs);
grunt.registerTask('cheatsheet-sanity-check', cheatsheetSanityCheck);
grunt.registerTask('hash-cheatsheet', hashCheatsheetFiles);
grunt.registerTask('squeeze-classes', squeezeClasses);

grunt.registerTask('build-cheatsheet', [
  'cheatsheet-sanity-check',
  'clean:pre',
  'less',
  'build-docs',
  'copy:cheatsheet',
  'hash-cheatsheet'
]);

grunt.registerTask('snowflake', snowflakeCount);
grunt.registerTask('default', 'less');

// end module.exports
};
