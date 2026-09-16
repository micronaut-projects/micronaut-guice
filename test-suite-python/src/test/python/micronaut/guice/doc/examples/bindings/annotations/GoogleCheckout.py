from com.google.inject import BindingAnnotation


@BindingAnnotation
def GoogleCheckout():
    def decorator(target):
        return target
    return decorator
