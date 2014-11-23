var marked = require('marked'),
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
    .replace(/\n/g, ' ')
    .replace(/<\/p> <p>/g, '</p><p>');
}

function transformObjToDocs(fn) {
  // extract namespace and function name from the full name
  fn["full-name"] = fn["function"];
  fn["namespace"] = extractNamespace(fn["function"]);
  fn["name"] = extractName(fn["function"]);
  delete fn["function"];

  // parse description
  fn["description-html"] = parseDescription(fn["description"]);
  delete fn["description"];

  // convert some sections into arrays
  fn.signature = convertSectionIntoArray(fn.signature);

  if (fn.hasOwnProperty("related") === true) {
    fn.related = convertSectionIntoArray(fn.related);
  }

  return fn;
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
  return obj.hasOwnProperty("function") &&
         obj.hasOwnProperty("signature") &&
         obj.hasOwnProperty("description");
}

function buildDocs() {
  var docs = {};

  grunt.file.recurse("docs", function(abspath) {
    // skip non .cljsdoc files
    if (abspath.search(/\.cljsdoc$/) === -1) return;

    var fileContent = grunt.file.read(abspath),
      obj = parseDocFileIntoObject(fileContent);

    // quick sanity check that the file is in a good format
    if (validDocObj(obj) !== true) return;

    // clean and transform the data
    obj = transformObjToDocs(obj);

    docs[obj["full-name"]] = obj;
  });

  grunt.file.write("public/docs.json", JSON.stringify(docs, null, 2));
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
        { src: 'public/css/main.min.css', dest: '00-publish/css/main.min.css' },
        { src: 'public/fonts/*', dest: '00-publish/fonts/', expand: true, flatten: true },
        { src: 'public/img/*', dest: '00-publish/img/', expand: true, flatten: true },
        { src: 'public/js/cheatsheet.min.js', dest: '00-publish/js/cheatsheet.min.js' },
        { src: 'public/js/libs/jquery-2.1.1.min.js', dest: '00-publish/js/libs/jquery-2.1.1.min.js' },
        { src: 'public/favicon.png', dest: '00-publish/favicon.png' }
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
}

// load tasks from npm
grunt.loadNpmTasks('grunt-contrib-clean');
grunt.loadNpmTasks('grunt-contrib-copy');
grunt.loadNpmTasks('grunt-contrib-less');
grunt.loadNpmTasks('grunt-contrib-watch');

grunt.registerTask('build-docs', buildDocs);
grunt.registerTask('hash-cheatsheet', hashCheatsheetFiles);

grunt.registerTask('build-cheatsheet', [
  'clean:pre',
  'less',
  'copy:cheatsheet',
  'hash-cheatsheet'
]);

grunt.registerTask('snowflake', snowflakeCount);
grunt.registerTask('default', 'less');

// end module.exports
};