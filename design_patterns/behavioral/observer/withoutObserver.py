class PhoneDisplay:
    def __init__(self):
        self.temp = 0
    def update(self,new_temp):
        self.temp = new_temp
        print(f"Phone Display: {self.temp}")

class TVDisplay:
    def __init__(self):
        self.temp = 0
    def update(self,new_temp):
        self.temp = new_temp
        print(f"TV Display: {self.temp}")

class WeatherUpdate:
    def __init__(self):
        self.temp = 0
        self.phone_display = PhoneDisplay()
        self.tv_display = TVDisplay()

    def update(self,new_temp):
        self.temp = new_temp
        self.notify_display()

    def notify_display(self):
        self.phone_display.update(self.temp)
        self.tv_display.update(self.temp)

wu = WeatherUpdate()
wu.update(35)
wu.update(41)


# PS C:\Users\User\On> python -u "c:\Users\User\OneDrive\Desktop\Design-Architecture\Design-Architecture\Design Patterns\Observer\withoutObserver.py"
# Phone Display: 35
# TV Display: 35
# Phone Display: 41
# TV Display: 41
    