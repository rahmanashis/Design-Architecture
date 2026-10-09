from abc import ABC,abstractmethod

class DiscountStrategy(ABC):
    @abstractmethod
    def calculate_discount(self):
        pass

class Eid(DiscountStrategy):
    def calculate_discount(self):
        print("Applying Eid Discount 30%")

class Puja(DiscountStrategy):
    def calculate_discount(self):
        print("Applying Puja Discount 20%")

class Crismas(DiscountStrategy):
    def calculate_discount(self):
        print("Applying Crismas Discount 10%")


class DiscountService:
    def __init__(self,d:DiscountStrategy):
        self.__strategy = d
        self.process()
    def set_strategy(self,new_d):
        self.__strategy = new_d
        self.process()
    def process(self):
        self.__strategy.calculate_discount()

cris = Crismas()
eid = Eid()
puja = Puja()

ds = DiscountService(eid)
ds.set_strategy(puja)