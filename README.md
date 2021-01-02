# arithmetic-expression-parser

**arithmetic-expression-parser** is a simple arithmetic expression parser written in Kotlin. It supports the following operations:

- unary: -
- binary: +, -, *, /, ^ (power), % (modulo)
- functions: abs, acos, asin, atan, cos, cosh, exp, ln, log, sin, sinh, sqrt, tan, tanh

# Design

The parser is build using combinators. Parsing is a two-step process:

- the tokens package provides various token parsers that match certain tokens, e.g. char literals, identifiers, numbers etc.
- arithmetic package defines expression parsers that use the token parsers to create parsers for arithmetic expressions.

# Usage

The `Expression` object parses arithmetic expressions:

```kotlin
Expression("3 + 4")
```

This will return an abstract syntax tree. The `eval` extension method can be used to evaluate the expression:

```kotlin
Expression("3 + 4").eval()
```

# License

Apache-2.0 License
