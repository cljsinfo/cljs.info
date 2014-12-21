### %why-clojurescript% Why should I use ClojureScript over JavaScript?

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

Why should I use ClojureScript instead of &lt;other language that compiles to JS&gt;?

The thing that distinguishes ClojureScript from other compile-to-js languages
are the powerful ideas behind Clojure. Specifically, it's excellent defaults:
pure functions, explicit state management, immutable data, persistent data
structures, and powerful data types.

When evaluating programming languages, it is popular to focus on syntax
differences instead of conceptual differences. The abstractions you use to build
your program matter far more than the syntax of the language.

Many other compile-to-js languages are mostly syntax sugar over JS or add a
handful of ideas to JavaScript. Few languages change the defaults of JS,
resulting in programs that have similar structure (and similar bugs and
problems) despite being written in a "different language".

### %why-not-clojurescript% When is JavaScript a better choice than ClojureScript?

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

### %syntax% What is the best way to learn ClojureScript? I'm sold on the ideas, but intimidated by the syntax.

TODO: address intimidation concerns, mention simplicity of syntax
maybe the "best way to learn" and "intimidated" are two different sections

Learning ClojureScript is a combination of understanding the syntax (15 minutes)
and familiarizing yourself with the functions available in the core library. The
ClojureScript core library is extensive and contains a wealth of functions that
will make up the backbone of any program. Many popular JavaScript libraries can
be replaced with functions from ClojureScript core (underscore.js is a good
example). It is worth exploring and familiarizing yourself with what's
available. (link to cheatsheet)

Like any language, the best way to learn ClojureScript is to write something in
it. A mix of theory and practice will get you a long way.

If you get stuck, there are many excellent community resources (TODO: links).
The ClojureScript community is full of smart people and has a reputation for
being friendly.

### %js-libraries% But JavaScript has so many libraries!

You can use *any* JavaScript library from ClojureScript. No exceptions.

### %node.js% Can I use ClojureScript with Node.js?

Yes. See examples 4000, 4001 and this tutorial.

### %license% Is ClojureScript open source?

Yes, ClojureScript is released under the Eclipse Public License 1.0.

TODO: more here

### %speed% Is ClojureScript fast?

Yes.

The longer answer to this question is that it is possible to write slow code in
any programming language and ClojureScript is no exception. The good news is
that modern JavaScript engines are amazingly fast and ClojureScript data types
and core library functions are designed to take full advantage of these engine's
performance.

TODO: longer answer explaining that you can write slow code in any language and
that often architecture decisions effect speed much more than implementation,
etc

Program architecture decisions often effect program speed more than
implementation details, and persistent data structure can be much faster than
their mutable equivalents for certain kinds of operations. (link to David
Nolen's talk about this)

Persistent data structures can be much faster than their mutable equivalents
for certain kinds of operations. (link to David Nolen's talk about this)

Link to examples of the Core team focusing on speed. Speed tests, etc.

### %jquery% Can I use jQuery with ClojureScript?

Absolutely! You can use any JavaScript library from ClojureScript. Check out
Examples 1000 and 1001 and read about JavaScript Interop for more information.

### %single-page-applications% Is ClojureScript a good fit for a single-page application (SPA)?

Yes, very much so. Dead code removal, real namespaces, package management, etc.
You get a lot of stuff baked into the language that otherwise has to be
accomplished with libraries or conventions in JS.

### %relationship-to-clojure% What is the relationship between Clojure and ClojureScript? Are they the same thing?

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

### %jvm% Why does ClojureScript require the JVM / Java? I thought this was a JavaScript thing.

ClojureScript *is* a JavaScript thing.

### %leiningen% What is Leiningen? Why do I need this for ClojureScript?

Leiningen is a popular build tool for Clojure projects. The ClojureScript
compiler relies on Clojure, so in a sense, a ClojureScript project is also a
Clojure project. Because of this relationship (and for reasons of not wanting to
reinvent the wheel), most ClojureScript projects use the lein-cljsbuild plugin
and Leiningen to compile ClojureScript files.

The relationship between Clojure and ClojureScript may seem odd to persons
coming from singular language ecosystems, but the symbiosis between the two and
their fundamentally similar syntax allow for a lot of useful overlap in tooling.
Leiningen is a good example of this.

### %project.clj% What is `project.clj`?

`project.clj` is a [Leiningen] project file. If you are familiar with common
Node.js conventions, `project.clj` acts like a combination of `package.json` and
`Gruntfile.js`.

[Leiningen]:http://leiningen.org/

TODO: provide more language comparisons here

### %sharing-code% Can I share ClojureScript code between the client and a Node.js server?

Yes.

TODO: link to tutorial on how to do this

### %libraries% Where & can I find ClojureScript libraries?

[Clojars] is a popular repository of open source Clojure libraries.

[npm] is a huge repository of JavaScript libraries. You can use any JavaScript
library with ClojureScript.

Finally, the [Google Closure Library] is shipped as part of ClojureScript, and
can be added to a namespace using `:import`.

TODO: link to namespace tutorial here

[Clojars]:https://clojars.org/
[npm]:https://www.npmjs.com/
[Google Closure Library]:https://developers.google.com/closure/library/
