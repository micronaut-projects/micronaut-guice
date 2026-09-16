from jakarta.inject import Singleton

from .CreditCardProcessor import CreditCardProcessor


@Singleton
class PayPalCreditCardProcessor(CreditCardProcessor):
    pass
