class DiscountService:
    def __init__(self,type:str):
        self.__discount = 0
        if type == 'eid':
            self.__discount = 30
            print(f"Eid Discount {self.__discount}")
        elif type == 'puja':
            self.__discount = 20
            print(f"Puja Discount {self.__discount}")
        elif type == 'crismas':
            self.__discount = 10
            print(f"Crismas Discount {self.__discount}")
        else:
            print("No Discount")

ds = DiscountService('eid')
ds2 = DiscountService('crismas')

# Eid Discount 30
# Crismas Discount 10
