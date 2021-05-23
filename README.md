# arithmetic-expression-parser

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.mthaler/aparser/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.mthaler/aparser)

**arithmetic-expression-parser** is a simple arithmetic expression parser written in Kotlin. It supports the following operations:

- unary: -
- binary: +, -, *, /, ^ (power), % (modulo)
- functions: abs, acos, asin, atan, cos, cosh, exp, ln, log, sin, sinh, sqrt, tan, tanh

For trigonometric functions, both radians and degrees are supported.

# Design

The parser is build using combinators. Parsing is a two-step process:

- the tokens package provides various token parsers that match certain tokens, e.g. char literals, identifiers, numbers etc.
- arithmetic package defines expression parsers that use the token parsers to create parsers for arithmetic expressions.

# Usage

The `Expression` object parses arithmetic expressions:

```kotlin
Expression("3 + 4")
```

This will return an abstract syntax tree. The `tryEval` extension method can be used to evaluate the expression:

```kotlin
Expression("3 + 4").tryEval()
```

The result will be `Success(7.0)`. If an invalid expression like `Expression("foo(0)")` is evaluated, a `Failure` will be returned.

## Trigonometric units

The default trigonometric unit is radians, but degrees are also supported:

```kotlin
val ctx = Context.Empty.copy(trigonometricUnit = TrigonometricUnit.Degree)
Expression("sin(90)").tryEval(ctx)
```

The result will be `Success(1.0)`.

# License

Apache-2.0 License
