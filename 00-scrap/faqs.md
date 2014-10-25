## Can I use ClojureScript with node.js?

Yes. See examples 4000, 4001 and this tutorial.

## Is ClojureScript fast?

Yes.

TODO: longer answer explaining that you can write slow code in any language and
that often architecture decisions effect speed much more than implementation,
etc

Persistent data structures can be much faster than their mutable equivalents
for certain kinds of operations. (link to David Nolen's talk about this)

Link to examples of the Core team focusing on speed. Speed tests, etc.

## Can I use jQuery with ClojureScript?

Absolutely! You can use any JavaScript library from ClojureScript. Check out
Examples 1000 and 1001 and read about JavaScript Interop for more information.

## Is ClojureScript a good fit for a single-page application (SPA)?

Yes, very much so. Dead code removal, real namespaces, package management, etc.
You get a lot of stuff baked into the language that otherwise has to be
accomplished with libraries or conventions in JS.

## What is the relationship between Clojure and ClojureScript? Are they the same thing?

Clojure is a language that targets the JVM. ClojureScript targets JavaScript.
Both languages share a similar syntax and most core library functionality from
Clojure exists ClojureScript. _Most_ Clojure code is valid ClojureScript code
and vice versa. The place where they differ the most is interop with the host
platform. Obviously `(js/alert "Hello!")` doesn't make any sense on the JVM and
`(slurp "/path/to/myFile.txt")` won't work in a browser.

The following table lists some key differences and similarities between the two:
TODO: need to look up markdown table syntax?

targets: JVM, JavaScript
data structures: same - persistent data structures
numbers: Java numbers, JavaScript as one number type
strings: same

## Why does ClojureScript require the JVM / Java? I thought this was a JavaScript thing.

ClojureScript *is* a JavaScript thing.

## What is Leiningen? Why do I need this for ClojureScript?

Leiningen is a popular build tool for Clojure projects. The ClojureScript
compiler relies on Clojure, so in a sense, a ClojureScript project is also a
Clojure project. Because of this relationship (and for reasons of not wanting to
reinvent the wheel), most ClojureScript projects use the lein-cljsbuild plugin
and Leiningen to compile ClojureScript files.

The relationship between Clojure and ClojureScript may seem odd to persons
coming from singular language ecosystems, but the symbiosis between the two and
their fundamentally similar syntax allow for a lot of useful overlap in tooling.
Leiningen is a good example of this.

## What is `project.clj`?

`project.clj` is a Leiningen project file. If you are familiar with common
node.js conventions, `project.clj` acts like a combination of `package.json` and
`Gruntfile.js`. TODO: need more language comparisons here

## Can I share ClojureScript code between the client and a node.js server?

Yes. TODO: link to tutorial on how to do this

## Where can I find ClojureScript libraries?

TODO: this is a damn good question that I don't know the answer to right now