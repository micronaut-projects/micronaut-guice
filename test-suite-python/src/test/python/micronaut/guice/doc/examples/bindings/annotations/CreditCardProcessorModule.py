# tag::class[]
import java
from com.google.inject import Binder, Module, Provides

from .CheckoutCreditCardProcessor import CheckoutCreditCardProcessor
from .CreditCardProcessor import CreditCardProcessor
from .GoogleCheckout import GoogleCheckout

# The Guice binder EDSL takes the Java classes of the bound types and of the binding annotation
CreditCardProcessorType = java.type("micronaut.guice.doc.examples.bindings.annotations.CreditCardProcessor")
PayPalCreditCardProcessorType = java.type("micronaut.guice.doc.examples.bindings.annotations.PayPalCreditCardProcessor")
PayPalType = java.type("micronaut.guice.doc.examples.bindings.annotations.PayPal")


class CreditCardProcessorModule(Module):

    # end::class[]
    # TODO(python): the Python compiler exposes no primary constructor for a class without __init__ (Java classes
    # have an implicit default constructor), and the @Guice import needs one
    def __init__(self):
        pass
    # tag::class[]

    def configure(self, binder: Binder) -> None:
        # This uses the optional `annotatedWith` clause in the `bind()` statement
        binder.bind(CreditCardProcessorType) \
            .annotatedWith(PayPalType) \
            .to(PayPalCreditCardProcessorType)

    # This uses binding annotation with a @Provides method
    # end::class[]
    # TODO(python): @Provides methods of a Python module are not turned into beans by the Python compiler yet
    # tag::class[]
    @Provides
    @GoogleCheckout()
    def provide_checkout_processor(self, processor: CheckoutCreditCardProcessor) -> CreditCardProcessor:
        return processor
# end::class[]
