from abc import ABC,abstractmethod

class Observer(ABC):
    @abstractmethod
    def update(self,temp):
        pass

class TvDisplay(Observer):
    def __init__(self):
        self.__temp = 0
    def update(self,temp):
        self.__temp = temp
        print(f"TV Display: {self.__temp}")

class TabDisplay(Observer):
    def __init__(self):
        self.__temp = 0
    def update(self,temp):
        self.__temp = temp
        print(f"Tab Display: {self.__temp}")

class PhoneDisplay(Observer):
    def __init__(self):
        self.__temp = 0
    def update(self,temp):
        self.__temp = temp
        print(f"Phone Display: {self.__temp}")

class WeatherUpdate:
    def __init__(self):
        self.__temp = 0
        self.__observers = []
    def update(self,new_temp):
        self.__temp = new_temp
        self.notify()
    def add_observer(self,ob:Observer):
        self.__observers.append(ob)
    def remove_observer(self,ob:Observer):
        self.__observers.remove(ob)
    def notify(self):
        for obs in self.__observers:
            obs.update(self.__temp)

to = TvDisplay()
po = PhoneDisplay()
tao = TabDisplay()
wu = WeatherUpdate()
wu.add_observer(to)
wu.add_observer(po)
wu.update(35)
wu.update(41)
wu.add_observer(tao)
wu.remove_observer(to)
wu.update(55)


# Users\User\OneDrive\Desktop\Design-Architecture\Design-Architecture\Design Patterns\Observer\Observer.py"
# TV Display: 35
# Phone Display: 35
# TV Display: 41
# Phone Display: 41
# Phone Display: 55
# Tab Display: 55
