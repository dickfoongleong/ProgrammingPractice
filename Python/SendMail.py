import os
import smtplib
from email.message import EmailMessage

EMAIL_ADDRESS = os.environ.get('WORK_GMAIL')
EMAIL_PASSWORD = os.environ.get('WORK_GMAIL_PASSWORD')
RECEIVER = input('Receivers: ')

msg = EmailMessage()
msg['Subject'] = "Wanna Hangout?"
msg['From'] = 'TEST'
msg['To'] = RECEIVER
msg.set_content("Email from Dick Foong program.\nHave a gooooooooood night!!!!!")

with smtplib.SMTP('smtp.gmail.com', 587) as smtp:
	smtp.ehlo()
	smtp.starttls()
	smtp.ehlo()

	smtp.login(EMAIL_ADDRESS, EMAIL_PASSWORD)
	smtp.send_message(msg)
