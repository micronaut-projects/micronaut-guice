from micronaut.guice.annotation import Guice
from micronaut.runtime import Micronaut

from .CreditCardProcessorModule import CreditCardProcessorModule
from .PayPalCreditCardProcessor import PayPalCreditCardProcessor


# tag::guice[]
@Guice(
    modules=[CreditCardProcessorModule],
    classes=[PayPalCreditCardProcessor]
)
# end::guice[]
class Application:

    @staticmethod
    def main(args: list[str]) -> None:
        Micronaut.run(Application)
