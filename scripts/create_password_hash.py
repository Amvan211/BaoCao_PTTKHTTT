# pip install bcrypt
import bcrypt,getpass
password=getpass.getpass("Mật khẩu tài khoản demo: ")
print(bcrypt.hashpw(password.encode(),bcrypt.gensalt()).decode())
