# The Rock Programming Language

The Rock programming language is a micro-language - meaning it is very minimalistic, much like a micro-kernel.  It is much like LISP, but with maps instead of lists.  It is designed to output source code for any other programming language, with new languages added through additional `.rock` source files.

Rock is also intended to be an IDE-centric language, meaning that code isn't meant to be edited directly.  This brings about some nice bonuses:

- The language can be greatly simplified, eliminating syntactic sugar.
- The language has no formatting, so there are no arguments about it.  Just set your IDE preferences accordingly.
- The programming experience is fully translatable, such that programming can be done across language barriers.
- The IDE can waste less time on parsing and get right to analysis.

It is named Rock after the NES classic *Rockman*, in which the main character could take the abilities of defeated opponents permanently.  Similarly, once a particular language feature is implemented in Rock, it is available to all languages through transpiling.

## Example

Using custom representation:

```
main = rock.string.join(
    values = [
        "This"
        "is"
        rock.control.if(
            condition = rock.boolean.true()
            else = "really"
            then = "not"
        )
        "a"
        "test."
    ]
    separator = " "
)
```

Using YAML:

```yaml
main:
  =: rock.string.join
  values:
  - "This"
  - "is"
  - =: rock.control.if
    condition: true
    else: "really"
    then: "not"
  - "a"
  - "test."
  separator: " "
```

Using JSON:

```json
{
   "main":{  
      "=":"rock.string.join",
      "values":[  
         "This",
         "is",
         {  
            "=":"rock.control.if",
            "condition":true,
            "else":"really",
            "then":"not"
         },
         "a",
         "test."
      ],
      "separator":" "
   }
}
```

## Design Goals

### Simplicity

- There are only two constructs in the whole language: Functions and Function Calls.  All concepts are represented as such.

### Transpiling and Interpretation

- Easily transpiles to other programming languages
- Transpiled output is human-readable, as the highest representation of an action is translated into code rather than a lower-level one
- No boilerplate for translated code, as the transpiled output uses the libraries from the given language
- User can add new languages as transpiling targets
- Code can be interpreted for easy testing

### Extendability

- Constructs (like *for*, *if*, and *class*) are just part of the standard library, meaning that the user can define new language constructs as needed.
- If new constructs are defined in terms of other constructs, they will function correctly in all languages supporting those other constructs.

### Formatting

- Text format is stored in JSON, YAML, or XML for easy parsing and editing by programs
- Post-parsed editing - the system can both parse the language from a text format AND serialize it into a text format.  This enables a code editor and code compiler to share a significant amount of code.
- An IDE will be created in the near future which supports code completion, auto-insertion of implied functions, and rendering/editing in different formats.

### Compile-time work

- Compile-time reflection is encouraged, as it's nearly free
- Metaprogramming is easy because it's all written in the same language

### Safety

- Code/constructs/functions validate their uses to ensure they are being used correctly
- Testing can be directly embedded (if desired) into the constructs themselves, keeping related code together

### Not as important

- Compiling speed - This can be optimized later.  The design so far allows for compiling with multiple threads, so this may end up being a non-issue anyways.
- Easy to edit in text form - This language isn't meant to be edited directly in text form.  You're meant to use an IDE.


## Standard Library Goals

- Represent what the programmer means, not what the system does
- Strictly / strongly typed
- Use one-word identifiers when both possible and specific enough, and `camelCasing` otherwise.
- All implied instructions are written out so the user can see them if needed.  IDEs can hide implied instructions.
- Easily and efficiently converts to the lowest level
- Zero-to-low cost abstractions
- Extendability - Extension functions, variables, and interfaces

## Roadmap 

### Completed

- Strings
- Integers
- Floats
- Pointers
- Control Structures
- Meta
- Debug

### To do - very unfinal

- Iterate through entries
- Validation as Language
- Arrays
- Functions
- Classes
- Interfaces (1)
- Interfaces (2)
- Iterator
- List/Mutable List
- Map/Mutable Map
- Include Library
- Action Stack (for language compiling)
- Language - Kotlin
- Standard Library Auto-implement
- Compiler written in Rock
- Language - Javascript
- IDE - Rendering
- IDE - Function Search
- IDE - Auto-insert fields
- IDE - Interpreter
- IO Interfaces
- File IO
- Network IO 