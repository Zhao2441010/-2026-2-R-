import pymysql

conn=pymysql.connect(
    host="localhost",
    user="root",
    password="Zhao123456",
    database="test"
)

cur=conn.cursor()

if __name__ == "__main__":
    
    
    
    
    
    
    conn.commit()
    cur.close()
    conn.close()