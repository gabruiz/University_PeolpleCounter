import paho.mqtt.client as mqtt
import datetime
import pickle
import pyodbc
import json
import urllib
import sqlalchemy
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker
from sqlalchemy import *
	
#db connection
params = urllib.quote_plus("DRIVER={ODBC Driver 17 for SQL Server};SERVER=iotdb-dr.database.windows.net;DATABASE=IoTDB;UID=DR;PWD=IoT2019.")
engine = create_engine("mssql+pyodbc:///?odbc_connect=%s" % params)
Session = sessionmaker(bind=engine)
Session.configure(bind=engine)
session = Session()

Base = declarative_base()

class Element(Base):
    __tablename__ = 'Elements'
    id = Column(Integer, primary_key=True)
    type = Column(String(20))
    name = Column(String(50))
    value = Column(Integer)
    timestamp = Column(Integer)

    def __repr__(self):
    	return "<User(id='%s', type='%s', name='%s',value='%s', timestamp='%s')>" % (self.id, self.type, self.name, self.value, self.timestamp)


#for instance in session.query(University):
#    print(instance)
	
	
# callback mqtt
def on_message(client, userdata, message):
	received = json.loads(message.payload)
	timestamp = received['timestamp']
	total = received['total']
	print(message.topic)
	a, elementType, value = message.topic.split("/")

	my_entry = Element(type=elementType ,name=value, value=total, timestamp=timestamp)
	print ("Write in db: " ,my_entry)

	
	session.add(my_entry)
	session.commit()
		
print("creating new instance")

broker_address="iot.eclipse.org"

client = mqtt.Client() #create new instance
client.on_message=on_message #attach function to callback
print("connecting to broker")
client.connect(broker_address, 1883) #connect to broker 
print("Subscribing to topic","CT")

client.subscribe("CT/#")
#subscribed



client.loop_forever()
