class Logger:
    _instance = None

    def __new__(cls, *args, **kwargs):
        print("Class instance: ",cls._instance)
        if cls._instance is None:
            cls._instance = object.__new__(cls)
            print("Object.__new__:",cls._instance)
            print("Logger instance created")
        return cls._instance
    
    def log(self, message):
        print(f"Log: {message}")
    
log1 = Logger()
log2 = Logger()

log1.log("First message")
log2.log("Second message")
log2.log("Third message")
log1.log("Fourth message")
print(log1 is log2)  # This will print True, indicating both are the same instance