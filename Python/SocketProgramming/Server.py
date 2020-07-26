import threading
import socket

HOST = '10.0.0.18'
PORT = 8866
isRunning = True

class ConnHandler (threading.Thread):
    def __init__(self, connection):
        threading.Thread.__init__(self)
        self.connection = connection

    def run(self):
        with self.connection:
            while True:
                data = self.connection.recv(1024)
                if not data:
                    break
                self.connection.sendall(data)

def startServer():
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.bind((HOST, PORT))
        s.listen()

        while True:
            conn, addr = s.accept()
            print('Connected by', addr)
            ConnHandler(conn).start()

while isRunning:
    try:
        startServer()
    except:
        while True:
            opt = input('Enter (r)Restart or (q)Quit: ')
            if opt == 'q':
                isRunning = False
                break
            elif opt == 'r':
                break
            else:
                print('Invalid Option')

print('Ended...')
