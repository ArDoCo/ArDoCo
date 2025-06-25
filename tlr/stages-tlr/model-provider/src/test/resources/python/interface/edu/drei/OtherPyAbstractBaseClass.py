from abc import ABC, abstractmethod

class OtherPyAbstractBaseClass(ABC):

    @abstractmethod
    def some_method(self):
        pass

    @abstractmethod
    def another_method(self, param):
        pass