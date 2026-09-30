from com.google.inject import Binder, Module

from .CreditCardProcessor import CreditCardProcessor
from .PayPalCreditCardProcessor import PayPalCreditCardProcessor


class CreditCardProcessorModule(Module):

    def configure(self, binder: Binder) -> None:
        binder.bind(CreditCardProcessor).to(PayPalCreditCardProcessor)
