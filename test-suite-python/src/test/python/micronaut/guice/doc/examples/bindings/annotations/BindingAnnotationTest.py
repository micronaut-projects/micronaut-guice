from typing import Annotated

from jakarta.inject import Inject
from micronaut.test.extensions.junit5.annotation import MicronautTest
from org.junit.jupiter.api import Test

from .CheckoutCreditCardProcessor import CheckoutCreditCardProcessor
from .CreditCardProcessor import CreditCardProcessor
from .GoogleCheckout import GoogleCheckout
from .PayPal import PayPal
from .PayPalCreditCardProcessor import PayPalCreditCardProcessor


@MicronautTest(startApplication=False)
class BindingAnnotationTest:

    paypal_processor: Annotated[CreditCardProcessor, Inject, PayPal()]

    @Test
    def test_inject_with_binding_annotation(self) -> None:
        assert isinstance(self.paypal_processor, PayPalCreditCardProcessor)

    @Test
    def test_inject_with_provides_method(self, checkout_processor: Annotated[CreditCardProcessor, GoogleCheckout()]) -> None:
        assert isinstance(checkout_processor, CheckoutCreditCardProcessor)
