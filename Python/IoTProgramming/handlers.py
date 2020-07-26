from cryptography.fernet import Fernet
from EmailSender import send_email
from datetime import datetime

import threading
import mysql.connector as MYSQL
import config

class ConnHandler (threading.Thread):
    def __init__(self, connection):
        threading.Thread.__init__(self)
        self.connection = connection
        self.isVerified = False
        self.db = MYSQL.connect(
            host=config.HOST,
            user=config.DB_CREDENTIAL,
            password=config.DB_CREDENTIAL,
            database=config.DB_DATABASE)

    def run(self):
        with self.connection:
            while True:
                data = self.connection.recv(1024)
                if not data:
                    break
                self.runCMD(data.decode())
        print('User left...')

    def runCMD(self, cmd):
        cmdDict = eval(cmd)
        command = cmdDict['CMD']
        if command == 'login':
            result = self.login_user(cmdDict['USERNAME'], cmdDict['PASSWORD'])
            if result['RESULT']:
                outputData = '{},{},{}'.format(result['ID'], result['FIRST'], result['LAST'])
                self.log_event(command, result['ID'])
                send_email(result['EMAIL'], 'Login Notice', 'You have loged in to the system.')
                self.connection.sendall(outputData.encode())
            elif result['REASON'] == 'invalid-password':
                self.log_event(command + '-Failed', result['ID'])
                send_email(result['EMAIL'], 'Login Attempt Notice', 'Your account was trying to login with a wrong password. Is that you?')
                self.connection.sendall(b'Invalid Password')
            else:
                self.connection.sendall(b'Invalid Username')
        elif command == 'create':
            result = self.create_user(cmdDict['USERNAME'], self.encrypt_password(cmdDict['PASSWORD']), cmdDict['FIRST_NAME'], cmdDict['LAST_NAME'], cmdDict['EMAIL'])
            if result == 0:
                send_email(cmdDict['EMAIL'], 'Created New Account on House IoT', 'Thank you for creating account in House IoT.')
                self.connection.sendall(b'User created')
            elif result == 1:
                self.connection.sendall(b'Failed to create user')
            else:
                self.connection.sendall(b'Username is used')
        elif command == 'run':
            cmdRec = command + '-' + cmdDict['OP']
            self.log_event(cmdRec, cmdDict['ID'])
            self.connection.sendall(b'Command Ran')
        else:
            self.connection.sendall(b'Invalid Command Entered...')

    def create_user(self, username, password, firstName, lastName, email):
        sql = 'INSERT INTO user(USERNAME, PASSWORD, FIRST_NAME, LAST_NAME, EMAIL) VALUES (%s, %s, %s, %s, %s)'
        val = (username, password, firstName, lastName, email)
        cursor = self.db.cursor()
        result = 2
        try:
            cursor.execute(sql, val)
            self.db.commit()

            rowC = cursor.rowcount
            if rowC == 1:
                result = 0
            else:
                result = 1
        except MYSQL.IntegrityError as ie:
            print('Error: ', ie)
            self.db.rollback()
            result = 2
        finally:
            cursor.close()
        return result

    def login_user(self, username, password):
        sql = 'SELECT PASSWORD, ID, FIRST_NAME, LAST_NAME, EMAIL FROM user WHERE USERNAME=%s'
        val = (username,)

        cursor = self.db.cursor()
        cursor.execute(sql, val)
        result = cursor.fetchone()
        cursor.close()

        if result is not None:
            (dbPass, id, firstName, lastName, email) = result
            dbPass = self.decrypt_password(dbPass)
            if password == dbPass:
                return {'RESULT':True, 'ID':id, 'FIRST':firstName, 'LAST':lastName, 'EMAIL':email}
            else:
                return {'RESULT':False, 'REASON':'invalid-password', 'ID':id, 'EMAIL':email}
        else:
            return {'RESULT':False, 'REASON':'invalid-username'}

    def log_event(self, command, userID):
        now = datetime.now()
        formatted_date = now.strftime('%Y-%m-%d %H:%M:%S')

        sql = 'INSERT INTO event(COMMAND, USER_ID, INPUT_TIME) VALUEs (%s, %s, %s)'
        val = (command, userID, formatted_date);

        cursor = self.db.cursor()
        try:
            cursor.execute(sql, val)
            self.db.commit()
            if command == 'login':
                updateSQL = 'UPDATE user SET LAST_LOGIN=%s WHERE ID=%s'
                updateVal = (formatted_date, userID)
                cursor.execute(updateSQL, updateVal)
                self.db.commit()
        except MYSQL.IntegrityError as ie:
            print('Error: ', ie)
            self.db.rollback()
        finally:
            cursor.close()

    def encrypt_password(self, password):
        key = config.CRYPTO_KEY
        f = Fernet(key)
        encrypted_password = f.encrypt(password.encode())
        return encrypted_password.decode()

    def decrypt_password(self, password):
        key = config.CRYPTO_KEY
        f = Fernet(key)
        decrypted_password = f.decrypt(password.encode())
        return decrypted_password.decode()
