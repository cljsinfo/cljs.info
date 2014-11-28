## Why should I use ClojureScript over JavaScript?

Like any decision that depends on multiple factors, the answer to this question
will vary based on person and project. Below are some common reasons why you
might choose ClojureScript over JavaScript.

Before discussing why you might choose ClojureScript over JavaScript, it is
beneficial to define related concerns and provide a foundational vocabulary for
comparison.

"A programming system has two parts. The programming "environment" is the part
that's installed on the computer. The programming "language" is the part that's
installed in the programmer's head." -- Bret Victor, [Learnable
Programming](http://worrydream.com/LearnableProgramming/)

TODO: Distinguishing between "JavaScript the language" and "JavaScript the
platform". "JavaScript the platform" is an all-star pick as of this writing (Nov
2014); provide some numbers to back up this claim. "JavaScript the language" is
less desireable. Cite known flaws; most importantly, "JavaScript the language"
was never designed for the complexity of program that it is currently being used
to develop. Some of the more popular conventions adopted by the JavaScript
community in recent years are things that other languages have had for years.
Many more important constructs are missing from the language and will likely
never have a resolution.

TODO: The role of a language in a program. The importance of defaults in 
programming languages and environments. Defaults inform convention and set the 
tone for everything else.

## When is JavaScript a better choice than ClojureScript?

Sometimes the inverse of a question can provide insight to the original (TODO:
link to original here). There are some cases where JavaScript might prove to be
a better choice than ClojureScript.

* file download concern(s) outweight program complexity. TODO: should provide
some hard numbers here on JavaScript library size in order to demonstrate how
rare of a concern this really is

* you are writing code that will ultimately be handed off to a third party to
maintain and the paying client is mandating technology choice for their
maintenance concerns. in this case, you may want to distinguish with the third
party whether or not their primary concern is 1) cost of development 2) speed
of development or 3) choice of technology. Technology choice is often cited as
a concern, but rarely over speed and cost.

The following are concerns which are often cited as reasons to *not* choose
ClojureScript. We believe most of these concerns are inconsequential and often
the result of misunderstanding.

* development environment restrictions preclude development in something other
than JavaScript. TODO: in this case, your problem isn't the language you're
using, it's your development environment
* my team can't learn LISP. TODO: cite case study
* the effort is not worth the result. TODO: cite case study
* I won't be able to hire anyone who knows ClojureScript. Answer: don't hire for
 specific technology skill; hire good developers and teach them.

## But JavaScript has so many libraries!

You can use *any* JavaScript library from ClojureScript. No exceptions.

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

Clojure is a language that targets the JVM. ClojureScript is a language that
targets JavaScript. Both languages share a similar syntax and most core library
functionality from Clojure exists ClojureScript. Most Clojure code is valid
ClojureScript code and vice versa. The place where they differ the most is
interop with the host platform. Obviously `(js/alert "Hello!")` doesn't make any
sense on the JVM and `(slurp "/path/to/myFile.txt")` won't work in a browser.

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