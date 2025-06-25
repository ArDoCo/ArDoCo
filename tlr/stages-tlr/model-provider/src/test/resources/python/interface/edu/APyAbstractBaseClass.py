from abc import ABC, abstractmethod

class APyAbstractBaseClass(ABC):

    @abstractmethod
    def some_method(self):
        pass

    @abstractmethod
    def another_method(self, param):
        pass