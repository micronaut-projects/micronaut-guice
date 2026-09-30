# tag::class[]
from com.google.inject import Binder, Module, Provides

from .CheckoutCreditCardProcessor import CheckoutCreditCardProcessor
from .CreditCardProcessor import CreditCardProcessor
from .GoogleCheckout import GoogleCheckout
from .PayPal import PayPal
from .PayPalCreditCardProcessor import PayPalCreditCardProcessor


class CreditCardProcessorModule(Module):

    def configure(self, binder: Binder) -> None:
        # This uses the optional `annotatedWith` clause in the `bind()` statement
        binder.bind(CreditCardProcessor) \
            .annotatedWith(PayPal) \
            .to(PayPalCreditCardProcessor)

    # This uses binding annotation with a @Provides method
    @Provides
    @GoogleCheckout()
    def provide_checkout_processor(self, processor: CheckoutCreditCardProcessor) -> CreditCardProcessor:
        return processor
# end::class[]
