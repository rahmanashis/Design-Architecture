#Loolk at the Factory Folder First

from abc import ABC, abstractmethod



# Abstract Products
class MessageSender(ABC):
    @abstractmethod
    def send_message(self, message: str) -> None:
        pass

class MessageFormatter(ABC):
    @abstractmethod
    def format_message(self, message: str) -> str:
        pass



# Concrete Senders
class EmailSender(MessageSender):
    def __init__(self, sender, receiver):
        self.sender = sender
        self.receiver = receiver
    def send_message(self, message: str):
        print(f"Email from {self.sender} to {self.receiver}: {message}")

class SMSender(MessageSender):
    def __init__(self, sender, receiver):
        self.sender = sender
        self.receiver = receiver
    def send_message(self, message: str):
        print(f"SMS from {self.sender} to {self.receiver}: {message}")

class PushSender(MessageSender):
    def __init__(self, sender, receiver):
        self.sender = sender
        self.receiver = receiver
    def send_message(self, message: str):
        print(f"Push from {self.sender} to {self.receiver}: {message}")



# Concrete Formatters
class PlainTextFormatter(MessageFormatter):
    def format_message(self, message: str) -> str:
        return f"[PlainText] {message}"

class HTMLFormatter(MessageFormatter):
    def format_message(self, message: str) -> str:
        return f"<html><body>{message}</body></html>"




# Abstract Factory
class MessageFactory(ABC):
    @abstractmethod
    def create_sender(self, sender, receiver) -> MessageSender:
        pass
    
    @abstractmethod
    def create_formatter(self) -> MessageFormatter:
        pass



# Concrete Factories
class EmailFactory(MessageFactory):
    def create_sender(self, sender, receiver):
        return EmailSender(sender, receiver)
    def create_formatter(self):
        return HTMLFormatter()  # Emails often use HTML

class SMSFactory(MessageFactory):
    def create_sender(self, sender, receiver):
        return SMSender(sender, receiver)
    def create_formatter(self):
        return PlainTextFormatter()  # SMS usually plain text

class PushFactory(MessageFactory):
    def create_sender(self, sender, receiver):
        return PushSender(sender, receiver)
    def create_formatter(self):
        return PlainTextFormatter()


# Client Code
msg = input("Enter your message: ")
sender = input("Enter sender: ")
receiver = input("Enter receiver: ")
method = input("Enter method (email/sms/push): ").lower()

factory_map = {
    "email": EmailFactory(),
    "sms": SMSFactory(),
    "push": PushFactory()
}

try:
    factory = factory_map[method]
    formatter = factory.create_formatter()
    formatted_msg = formatter.format_message(msg)
    message_sender = factory.create_sender(sender, receiver)
    message_sender.send_message(formatted_msg)

except KeyError:
    print("Invalid method selected.")