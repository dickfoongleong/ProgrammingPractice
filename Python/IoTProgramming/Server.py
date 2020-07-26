import socket
import config
from handlers import ConnHandler

def startServer():
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.bind((config.HOST, config.PORT))
        s.listen()

        while True:
            conn, addr = s.accept()
            print('Connected by', addr)
            ConnHandler(conn).start()

isRunning = True
while isRunning:
    try:
        startServer()
    except:
        while True:
            opt = input('Server Failure. Enter (r)Restart or (q)Quit: ')
            if opt == 'q':
                isRunning = False
                break
            elif opt == 'r':
                break
            else:
                print('Invalid Option')

print('Ended...')
