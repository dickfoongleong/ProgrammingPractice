import os
import smtplib
from email.message import EmailMessage

EMAIL_ADDRESS = os.environ.get('WORK_GMAIL')
EMAIL_PASSWORD = os.environ.get('WORK_GMAIL_PASSWORD')
RECEIVER = input('Receivers: ')

msg = EmailMessage()
msg['Subject'] = "Happy New Year"
msg['From'] = 'Dick Foong (no-reply)'
msg['To'] = RECEIVER
msg['Bcc'] = os.environ.get('PERSONAL_GMAIL')
msg.set_content("Happy New Year!!!\nMay your goals of the year come true and stay safe!!!\n\nBest Regards,\nDick Foong Leong >:)")

with smtplib.SMTP('smtp.gmail.com', 587) as smtp:
	smtp.ehlo()
	smtp.starttls()
	smtp.ehlo()

	smtp.login(EMAIL_ADDRESS, EMAIL_PASSWORD)
	smtp.send_message(msg)
