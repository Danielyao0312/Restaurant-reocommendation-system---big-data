from TwitterAPI import TwitterAPI

CONSUMER_KEY = 'GX8reD9raAKw8B2lXRoOJuP5A'
CONSUMER_SECRET = 'XRlN1NsTDmzzb2sfYGiUNSkEF1JXmMJbNUoUfVpAZ5tFcvMvJw'
ACCESS_TOKEN_KEY = '700866681325793280-nKDuWXlccooGlcujmjgmxkIVseKQHFS'
ACCESS_TOKEN_SECRET = 'LeX3oprLV0fBtAc4o74KCyWe7wBj7zkaNUWESREf1m9DI'


api = TwitterAPI(CONSUMER_KEY,
                 CONSUMER_SECRET,
                 ACCESS_TOKEN_KEY,
                 ACCESS_TOKEN_SECRET)


r = api.request('statuses/filter', {'locations':'-74,40,-73,41'})
f = open("twitter.txt","a+")
for item in r:
	if 'text' in item:
		f.writelines(item['text'].encode('utf-8')+"\n")
	        	
f.close()
