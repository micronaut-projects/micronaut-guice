from .CreditCardProcessor import CreditCardProcessor


class PayPalCreditCardProcessor(CreditCardProcessor):

    # TODO(python): the Python compiler exposes no primary constructor for a class without __init__ (Java classes
    # have an implicit default constructor), and the @Guice `classes` import needs one
    def __init__(self):
        pass
