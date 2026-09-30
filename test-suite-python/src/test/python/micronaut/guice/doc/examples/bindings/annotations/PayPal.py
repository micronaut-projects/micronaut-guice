from jakarta.inject import Qualifier


@Qualifier
def PayPal():
    def decorator(target):
        return target
    return decorator
