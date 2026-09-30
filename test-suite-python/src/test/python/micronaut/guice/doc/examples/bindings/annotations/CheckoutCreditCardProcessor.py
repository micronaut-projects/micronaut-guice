from jakarta.inject import Singleton

from .CreditCardProcessor import CreditCardProcessor


@Singleton
class CheckoutCreditCardProcessor(CreditCardProcessor):
    pass
