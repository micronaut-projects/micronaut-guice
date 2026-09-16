import java
from com.google.inject import Binder, Module

# The Guice binder EDSL takes the Java classes of the bound types
CreditCardProcessorType = java.type("micronaut.guice.doc.examples.bindings.imported.CreditCardProcessor")
PayPalCreditCardProcessorType = java.type("micronaut.guice.doc.examples.bindings.imported.PayPalCreditCardProcessor")


class CreditCardProcessorModule(Module):

    # TODO(python): the Python compiler exposes no primary constructor for a class without __init__ (Java classes
    # have an implicit default constructor), and the @Guice import needs one
    def __init__(self):
        pass


    def configure(self, binder: Binder) -> None:
        binder.bind(CreditCardProcessorType).to(PayPalCreditCardProcessorType)
