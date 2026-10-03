class Logger:
    def __init__(self):
        print("Logger instance created")
    def log(self, message):
        print(f"Log: {message}")
    
log1 = Logger()
log2 = Logger()

log1.log("First message")
log2.log("Second message")
log2.log("Third message")
log1.log("Fourth message")

print(log1 is log2)  # This will print False, indicating two different instances
