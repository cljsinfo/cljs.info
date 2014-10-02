module.exports = function(grunt) {
'use strict';

//------------------------------------------------------------------------------
// Grunt Config
//------------------------------------------------------------------------------

grunt.initConfig({

  // LESS conversion
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
    files: "less/*.less",
    tasks: ["less:watch"]
  }

});

// load tasks from npm
grunt.loadNpmTasks('grunt-contrib-less');
grunt.loadNpmTasks('grunt-contrib-watch');

grunt.registerTask('default', ['less']);

// end module.exports
};