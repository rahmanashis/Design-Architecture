from abc import ABC, abstractmethod

#strategy interface
class MessageSender(ABC):
    @abstractmethod
    def send_message(self, message: str) -> None:
        pass

#concrete strategies
class EmailSender(MessageSender):
    def __init__(self, sender: str, receiver: str):
        self.sender = sender
        self.receiver = receiver

    def send_message(self, message: str) -> None:
        print(f"Sending email from {self.sender} to {self.receiver} with message '{message}'")

class SMSender(MessageSender):
    def __init__(self, sender: str, receiver: str):
        self.sender = sender
        self.receiver = receiver

    def send_message(self, message: str) -> None:
        print(f"Sending SMS from {self.sender} to {self.receiver} with message '{message}'")

class PushSender(MessageSender):
    def __init__(self, sender: str, receiver: str):
        self.sender = sender
        self.receiver = receiver

    def send_message(self, message: str) -> None:
        print(f"Sending Push Notification from {self.sender} to {self.receiver} with message '{message}'")

#factory class
class MessageSenderFactory:
    sender_map = {
        "email": EmailSender,
        "sms": SMSender,
        "push": PushSender,
        # later we can add without touching code logic
        # "whatsapp": WhatsAppSender  
    }

    @staticmethod
    def get_message_sender(method: str, sender: str, receiver: str) -> MessageSender:
        try:
            return MessageSenderFactory.sender_map[method](sender, receiver)
        except KeyError:
            raise ValueError("Invalid method selected.")

#user input
msg = input("Enter your message: ")
sender = input("Enter sender: ")
receiver = input("Enter receiver: ")
method = input("Enter method (email/sms/push): ").lower()
#with factory pattern
try:
    message_sender = MessageSenderFactory.get_message_sender(method, sender, receiver)
    message_sender.send_message(msg)
except ValueError as e:
    print(e)