var md5 = require('md5');

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
// Cheatsheet Publish
//------------------------------------------------------------------------------

function buildCheatsheetSanityCheck() {
  if (! grunt.file.exists('public/js/cheatsheet.min.js')) {
    grunt.fail.warn('Could not find public/js/cheatsheet.min.js! Aborting...');
  }

  // TODO: check to make sure the ctime on cheatsheet.min.js is pretty fresh (< 5 minutes)
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
    }
  }

});

// load tasks from npm
grunt.loadNpmTasks('grunt-contrib-clean');
grunt.loadNpmTasks('grunt-contrib-copy');
grunt.loadNpmTasks('grunt-contrib-less');
grunt.loadNpmTasks('grunt-contrib-watch');

grunt.registerTask('build-cheatsheet-sanity-check', buildCheatsheetSanityCheck);
grunt.registerTask('hash-cheatsheet', hashCheatsheetFiles);
grunt.registerTask('squeeze-classes', squeezeClasses);

grunt.registerTask('build-cheatsheet', [
  'build-cheatsheet-sanity-check',
  'clean:pre',
  'less',
  'copy:cheatsheet',
  'hash-cheatsheet'
]);

grunt.registerTask('snowflake', snowflakeCount);
grunt.registerTask('default', 'less');

// end module.exports
};
