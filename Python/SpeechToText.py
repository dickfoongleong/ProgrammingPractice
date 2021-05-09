import speech_recognition as sr

lis = sr.Recognizer()
try:
	with sr.Microphone() as src:
		print('Listening...')
		voice = lis.listen(src)
		info = lis.recognize_google(voice)
		print(info)
except:
	pass
