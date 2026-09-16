# Python Docs Disabled Test Inventory

This file tracks the Python documentation examples under `test-suite-python` that are present but disabled,
or that deviate from the Java example because the direct port does not compile or does not behave like the
Java example yet. It is the bug-fixing task list for the Python compiler (`micronaut-inject-python` /
`micronaut-context-python`); every row references a `TODO(python)` comment in the sources or a workaround
described below.

The Python examples are compiled by every build and their tests run with
`./gradlew pythonCheck -Ppython-ci` (the "Python CI" GitHub workflow).

## Reconciliation

- Last generated active `@Disabled` count: 1.
- Last generated command: `rg -n "@Disabled\(" test-suite-python/src`.
- Last full-suite command: `./gradlew :test-suite-python:test -Ppython-ci`.
- Last full-suite result: build successful, 3 tests executed, 1 skipped, 0 failures.

## Migration Rules

- Do not define local copies of Micronaut annotation helpers or custom annotation shims in docs snippets.
  Standard Micronaut and Guice annotations are imported from their Java package (`micronaut.guice.annotation`,
  `com.google.inject`, `jakarta.inject`, ...).
- A Python Guice module implements the `com.google.inject.Module` interface (`class CreditCardProcessorModule(Module)`
  with a `configure(self, binder: Binder)` method) because a Python class cannot extend the Java class `AbstractModule`;
  the binder EDSL (`binder.bind(...).annotatedWith(...).to(...)`) takes the Java classes of the bound types and of the
  binding annotation, which are `java.type(...)` aliases of the generated classes (see below). A `[.lang-python]` note
  in `modules.adoc` explains this.
- The `@Guice(modules=[...], classes=[...])` members reference the Python classes directly (class-valued annotation
  members); the module import visitor of `micronaut-guice-processor` runs on the Python class elements and writes the
  associated bean definitions of the modules and imported classes.
- Binding annotations are Python annotation functions meta-annotated like the Java ones (`@Qualifier def PayPal(): ...`,
  `@BindingAnnotation def GoogleCheckout(): ...`) and are applied with parentheses (`@GoogleCheckout()`,
  `Annotated[CreditCardProcessor, Inject, PayPal()]`).
- The `GuiceModuleBinder` of the integration is a `@Context` bean created before the GraalPy runtime exists, so the
  Java helper `io.micronaut.guice.docs.support.PythonRuntimeInitializer` (a no-op `TypeConverter` injecting the
  `@Named("python") Context`; type converters are initialized before the context-scoped beans) forces the creation of
  the runtime first.
- The tests are `@MicronautTest(startApplication=False)` classes injecting the qualified beans as class attributes or
  test-method parameters and asserting the bean type with `isinstance(...)` against the Python classes.

## Active `@Disabled` Tests

| Test | Reason |
| --- | --- |
| `micronaut.guice.doc.examples.bindings.annotations.BindingAnnotationTest.test_inject_with_provides_method` | The `@Provides @GoogleCheckout` method of the Python module is not turned into a bean: `BeanElementBuilder.produceBeans(...)` filters the producer methods with `modifiers(m -> m.contains(ElementModifier.PUBLIC))` and the Python element implementation reports an empty modifier set, so no `$CreditCardProcessorModule$ProvideCheckoutProcessor0$Definition` is written (`No bean of type [CreditCardProcessor] exists for the given qualifier: @GoogleCheckout`). The `annotatedWith` binding of the same module works. |

## Commented Unsupported Snippet Ports

None.

## Workarounds Kept In Snippets

| Target | Reason |
| --- | --- |
| `annotations.CreditCardProcessorModule`, `imported.CreditCardProcessorModule`, `imported.PayPalCreditCardProcessor` | Explicit no-op `__init__` (hidden from the guide by the tags): `PythonClassElement.getPrimaryConstructor()` is empty for a Python class without `__init__` (Java class elements expose the implicit default constructor), and the module import visitor (`Cannot import Guice module [...], since it has multiple constructors or no accessible constructor`) and the `classes` import (`Cannot create associated bean with no accessible primary constructor`) need one. |

## Intentionally Unsupported Snippet Targets

None.

## java.type usages

| File | Alias | Reason |
| --- | --- | --- |
| `annotations/CreditCardProcessorModule.py` | `java.type("micronaut.guice.doc.examples.bindings.annotations.CreditCardProcessor")`, `...PayPalCreditCardProcessor`, `...PayPal` | Runtime `Class` arguments of the Guice binder EDSL (`binder.bind(Class)`, `annotatedWith(Class)`, `to(Class)`); imported shim classes only work as type hints. |
| `imported/CreditCardProcessorModule.py` | `java.type("micronaut.guice.doc.examples.bindings.imported.CreditCardProcessor")`, `...PayPalCreditCardProcessor` | Same. |
