# leon.computer.basis

Minimum viable Clojure backend with a Datomic database.

Intended to be cloned, experimented with, and modified to become your
next Clojure program.

These things are included:

- REPL namespace with "reset" command
- Working logback configuration
- CIDER-NREPL server
- Support for different configurations
- Project main with graceful shutdown (TODO)
- Uberjar with source obfuscation (TODO)
- A HTTP web server component
- A Datomic database component


# Why?

Fills the gap between example code that doesn't scale and templates which already add many dependencies and make many choices for you.  It is a minimalist example of a scalable scaffolding with a "reloaded" development workflow and road to production.

# Usage

- Start nrepl by scripts/start-nrepl.sh
- Connect your editor
- `(require 'dev)`
- `(reset)`




## License

MIT
