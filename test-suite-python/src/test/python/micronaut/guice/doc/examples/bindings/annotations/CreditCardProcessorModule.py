# tag::class[]
import java
from com.google.inject import Binder, Module, Provides

from .CheckoutCreditCardProcessor import CheckoutCreditCardProcessor
from .CreditCardProcessor import CreditCardProcessor
from .GoogleCheckout import GoogleCheckout
from .PayPalCreditCardProcessor import PayPalCreditCardProcessor

# end::class[]
# TODO(python): a Python-defined annotation (decorator function) is not converted to the `Class` parameter of
# `annotatedWith(Class)` ("Cannot convert '<function PayPal...>' to Java type 'java.lang.Class'"); imported Java
# annotations and the Python classes of the bound types are
# tag::class[]
# The binder EDSL takes the Java class of the binding annotation
PayPalType = java.type("micronaut.guice.doc.examples.bindings.annotations.PayPal")


class CreditCardProcessorModule(Module):

    def configure(self, binder: Binder) -> None:
        # This uses the optional `annotatedWith` clause in the `bind()` statement
        binder.bind(CreditCardProcessor) \
            .annotatedWith(PayPalType) \
            .to(PayPalCreditCardProcessor)

    # This uses binding annotation with a @Provides method
    @Provides
    @GoogleCheckout()
    def provide_checkout_processor(self, processor: CheckoutCreditCardProcessor) -> CreditCardProcessor:
        return processor
# end::class[]
