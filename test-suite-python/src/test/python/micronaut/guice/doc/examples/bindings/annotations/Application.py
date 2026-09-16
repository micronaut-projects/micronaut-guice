# tag::imports[]
from micronaut.guice.annotation import Guice
from micronaut.runtime import Micronaut

from .CreditCardProcessorModule import CreditCardProcessorModule
# end::imports[]


# tag::class[]
@Guice(modules=[CreditCardProcessorModule])  # <1>
class Application:

    @staticmethod
    def main(args: list[str]) -> None:
        Micronaut.run(Application)
# end::class[]
