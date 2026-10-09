class EmailSender:
    def send_email(self, sender, receiver, message):
        print(f"Sending email from {sender} to {receiver} with message '{message}'")

class SMSender:
    def send_sms(self, sender, receiver, message):
        print(f"Sending SMS from {sender} to {receiver} with message '{message}'")

class PushSender:
    def send_push(self, sender, receiver, message):
        print(f"Sending Push Notification from {sender} to {receiver} with message '{message}'")

#user input
msg = input("Enter your message: ")
sender = input("Enter sender: ")
receiver = input("Enter receiver: ")
method = input("Enter method (email/sms/push): ").lower()

#without factory pattern
if method == "email":
    email_sender = EmailSender()
    email_sender.send_email(sender, receiver, msg)
elif method == "sms":
    sms_sender = SMSender()
    sms_sender.send_sms(sender, receiver, msg)
elif method == "push":
    push_sender = PushSender()
    push_sender.send_push(sender, receiver, msg)
else:
    print("Invalid method selected.")