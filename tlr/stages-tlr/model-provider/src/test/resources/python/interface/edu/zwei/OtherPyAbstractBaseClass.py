from abc import ABC, abstractmethod

class OtherInterface(ABC):
    
    @abstractmethod
    def method_one(self):
        pass

    @abstractmethod
    def method_two(self, param):
        pass