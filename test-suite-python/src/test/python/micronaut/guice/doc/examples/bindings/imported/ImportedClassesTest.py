from typing import Annotated

from jakarta.inject import Inject
from micronaut.test.extensions.junit5.annotation import MicronautTest
from org.junit.jupiter.api import Test

from .CreditCardProcessor import CreditCardProcessor
from .PayPalCreditCardProcessor import PayPalCreditCardProcessor


@MicronautTest(startApplication=False)
class ImportedClassesTest:

    processor: Annotated[CreditCardProcessor, Inject]

    @Test
    def test_imported_class_is_bound(self) -> None:
        assert isinstance(self.processor, PayPalCreditCardProcessor)
