# The MEGA Programming Language

The Mega programming language is a micro-language - meaning it is very minimalistic, much like a micro-kernel - that is designed to handle all cases in a practical manner.  It is like LISP, but with keyword arguments and prototypal inheritance.  It is also designed to output source code for any other programming language and is very configurable.

Mega is also intended to be an IDE-centric language, meaning that code isn't meant to be edited directly.  This design decision removes the need for syntactic sugar, or in other words, the need for simpler ways to represent certain 

It is named Mega after the NES classic *Megaman*, in which the main character could take the abilities of defeated opponents permanently.  Similarly, once a particular language feature is implemented in Mega, it is available to all languages through transpiling.  *Megaman* also featured a character named Protoman, and since this language uses prototypal inheritance, the name seems all the more appropriate.

## Example

Using YAML:

```yaml
isATest: true
main:
  =: =/standard.string.concat
  values:
    - "This "
    - =: =/standard.control.if
      condition:
        _proto: /isATest
      consequent: "is"
      alternative: "isn't"
    - " a "
    - "test."
```

Using JSON:

```json
{
  "isATest": true,
  "main": {
    "_proto": "/standard.string.concat",
    "values": [
      "This ",
      {
        "_proto": "/standard.control.if",
        "condition": {
          "_proto": "/isATest"
        },
        "consequent": "is",
        "alternative": "isn't"
      },
      " a ",
      "test."
    ]
  }
}
```

## Design Goals

### Simplicity

- The core of the language works in a simple, easy-to-understand manner
- Everything in the language is represented in a single interface

### Transpiling and Interpretation

- Easily transpiles to other programming languages
- Transpiled output is human-readable
- User can add new languages as transpiling targets
- Code can be interpreted for easy testing

### Extendability

- Constructs (like *for*, *if*, and *class*) are user-creatable, meaning that the user can define new language constructs as needed.
- If new constructs are defined in terms of other constructs, they will function correctly in all languages supporting those other constructs.

### Formatting

- Text format is stored in JSON, YAML, or XML for easy parsing and editing by programs
- Post-parsed editing - the system can both parse the language from a text format AND serialize it into a text format.  This enables a code editor and code compiler to share a significant amount of code.
- An IDE will be created in the near future which supports code completion, auto-insertion of implied functions, and rendering/editing in different formats.

### Compile-time work

- Compile-time reflection
- Work can be done at compile time

### Safety

- Code/constructs/functions validate their uses to ensure they are being used correctly
- Testing can be directly embedded (if desired) into the constructs themselves

### Not as important

- Compiling speed - This can be optimized later.  The design so far allows for compiling with multiple threads, so this may end up being a non-issue anyways.
- Easy to edit in text form - This language isn't meant to be edited directly in text form.  You're meant to use an IDE.  As such, there is very little syntactic sugar in the language.


## Standard Library Goals

- Represent what the programmer means, not what the system does
- Strictly / strongly typed
- Use one-word identifiers when both possible and specific enough, and `camelCasing` otherwise.
- All implied instructions are written out so the user can see them if needed.
- Easily and efficiently converts to the lowest level
- Zero-to-low cost abstractions
- Extendability - Extension functions, variables, and interfaces

## Roadmap Completed


## Roadmap

- Strings
- Integers
- Floats
- Pointers
- Control Structures
- Meta
- Debug
- Iterate through entries
- Validation as Language
- Arrays
- Functions
- Classes
- Interfaces (1)
- Interfaces (2)
- Iterator
- List/Mutable List
- ArrayList and LinkedList
- Map/Mutable Map
- HashMap
- Include Library
- Action Stack (for language compiling)
- Language - Kotlin (1)
- Language - Kotlin (2)
- Language - Kotlin (3)
- Standard Library Auto-implement (1)
- Standard Library Auto-implement (2)
- Standard Library Auto-implement (3)
- Standard Library Auto-implement (4)
- Compiler in Mega (1)
- Compiler in Mega (2)
- Compiler in Mega (3)
- Compiler in Mega (4)
- Compiler in Mega (5)
- Language - Javascript (1)
- Language - Javascript (2)
- Language - Javascript (3)
- Javascript Compiler
- Visual Editor - Rendering (1)
- Visual Editor - Rendering (2)
- Visual Editor - Rendering (3)
- Visual Editor - Function Search
- Visual Editor - Auto-insert fields
- Visual Editor - Interpreter
- IO Interfaces
- File IO
- Network IO (1)
- Network IO (2)