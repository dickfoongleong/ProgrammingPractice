import threading
import socket
import config

HOST = '10.0.0.18'
PORT = 8866

with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
    s.connect((config.HOST, config.PORT))
    try:
        while True:
            inputStr = input("Enter: ")
            if (inputStr == 'quit' or inputStr == 'QUIT'):
                break
            s.sendall(inputStr.encode())
            result = s.recv(1024).decode()
            print("Server --> " + result)
    except:
        print('Ended...')
