core
====

This repository contains many reusable functions/classes.

- `com.everyware.math`: Implementations of matrices and vectors as well as helper functions for "long" multiplication/division and addition/subtraction with carry.
- `com.everyware.xml`: Lightweight XML parser that does not "pull" from an input stream. Instead, you have to "push" characters into the parser and it will generate SAX-like events once a complete tag is parsed.
- `com.everyware.posix`: Implementation of a subset of the POSIX API in pure Java. Additionally a simplistic ELF parser is available in `com.everyware.posix.elf`.
- `com.everyware.util`: Various utility functions and classes. The most interesting parts are the logging configuration (`com.everyware.util.log.Trace`) and various I/O related classes (`com.everyware.util.io`).

How to build
------------

1. Install [mx](https://github.com/graalvm/mx)
2. `mx build`
3. Use the compiled jar files from the `build` directory
