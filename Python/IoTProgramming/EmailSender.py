import os
import smtplib
from email.message import EmailMessage

EMAIL_ADDRESS = os.environ.get('WORK_GMAIL')
EMAIL_PASSWORD = os.environ.get('WORK_GMAIL_PASSWORD')

def send_email(email, subject, contentMsg):
    msg = EmailMessage()
    msg['Subject'] = subject
    msg['From'] = 'House IoT Notifier'
    msg['To'] = email

    content = "This is an email regarding your recent activity on House IoT application.\n\n" + contentMsg +"\n\nHave a wonderful day!!!!!"
    msg.set_content(content)

    with smtplib.SMTP('smtp.gmail.com', 587) as smtp:
    	smtp.ehlo()
    	smtp.starttls()
    	smtp.ehlo()

    	smtp.login(EMAIL_ADDRESS, EMAIL_PASSWORD)
    	smtp.send_message(msg)
