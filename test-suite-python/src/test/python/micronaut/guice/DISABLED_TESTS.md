# Python Docs Disabled Test Inventory

This file tracks the Python documentation examples under `test-suite-python` that are present but disabled,
or that deviate from the Java example because the direct port does not compile or does not behave like the
Java example yet. It is the bug-fixing task list for the Python compiler (`micronaut-inject-python` /
`micronaut-context-python`); every row references a `TODO(python)` comment in the sources or a workaround
described below.

The Python examples are compiled by every build and their tests run with
`./gradlew pythonCheck -Ppython-ci` (the "Python CI" GitHub workflow).

## Reconciliation

- Last generated active `@Disabled` count: 0.
- Last generated command: `rg -n "@Disabled\(" test-suite-python/src`.
- Last full-suite command: `./gradlew :test-suite-python:test -Ppython-ci`.
- Last full-suite result: build successful, 3 tests executed, 0 skipped, 0 failures.

## Migration Rules

- Do not define local copies of Micronaut annotation helpers or custom annotation shims in docs snippets.
  Standard Micronaut and Guice annotations are imported from their Java package (`micronaut.guice.annotation`,
  `com.google.inject`, `jakarta.inject`, ...).
- A Python Guice module implements the `com.google.inject.Module` interface (`class CreditCardProcessorModule(Module)`
  with a `configure(self, binder: Binder)` method), see the workaround below; the binder EDSL
  (`binder.bind(...).annotatedWith(...).to(...)`) takes the Python classes of the bound types directly and the
  `java.type(...)` alias of a binding annotation defined in Python (see below). A `[.lang-python]` note in
  `modules.adoc` explains this.
- The `@Guice(modules=[...], classes=[...])` members reference the Python classes directly (class-valued annotation
  members); the module import visitor of `micronaut-guice-processor` runs on the Python class elements and writes the
  associated bean definitions of the modules and imported classes.
- Binding annotations are Python annotation functions meta-annotated like the Java ones (`@Qualifier def PayPal(): ...`,
  `@BindingAnnotation def GoogleCheckout(): ...`) and are applied with parentheses (`@GoogleCheckout()`,
  `Annotated[CreditCardProcessor, Inject, PayPal()]`). The `@Provides` method of the Python module is turned into a
  producer bean (`$CreditCardProcessorModule$Provide_checkout_processor0$Definition`).
- The tests are `@MicronautTest(startApplication=False)` classes injecting the qualified beans as class attributes or
  test-method parameters and asserting the bean type with `isinstance(...)` against the Python classes.

## Active `@Disabled` Tests

None.

## Commented Unsupported Snippet Ports

None.

## Workarounds Kept In Snippets

| Target | Reason |
| --- | --- |
| `annotations.CreditCardProcessorModule`, `imported.CreditCardProcessorModule` (`class ...(Module)` instead of `...(AbstractModule)`) | A Python class extending the Java class `AbstractModule` compiles to a stub extending it, but the module import visitor's `typed(Module)` on the associated bean fails with `Bean defines an exposed type [com.google.inject.Module] that is not implemented by the bean type`: `PythonClassElement.isAssignable(type)` checks Python bases and Java interface bases only, not the supertypes of a Java class base (`AbstractModule implements Module`). The modules implement `Module` and bind through the `Binder` parameter instead of the inherited `bind(...)`. |

## Intentionally Unsupported Snippet Targets

None.

## java.type usages

| File | Alias | Reason |
| --- | --- | --- |
| `annotations/CreditCardProcessorModule.py` | `java.type("micronaut.guice.doc.examples.bindings.annotations.PayPal")` | `Class` argument of `annotatedWith(Class)`: a binding annotation defined in Python (the `PayPal` decorator function) is not converted to `java.lang.Class` at runtime (`Cannot convert '<function PayPal.<locals>.decorator>' (language: Python, type: function) to Java type 'java.lang.Class'`); imported Java annotations and the Python classes of the bound types (`bind(CreditCardProcessor)`, `to(PayPalCreditCardProcessor)`) are. |
