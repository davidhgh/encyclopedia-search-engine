import redis
import pandas as pd
import json

# Connect to Redis
redis_client = redis.Redis(host='10.147.19.195', port=6379)


cursor, keys = redis_client.scan(cursor='0', count=10)
# print(type(cursor))
# print(keys)


# key = keys[0] #
key = 'geturl_delete:items'


value = redis_client.lrange(key, 0, -1) #0,-1 #for all data
# print(value)

# print(type(value))
string_list = [b.decode() for b in value]

json_data_list = [json.loads(s) for s in string_list]

df = pd.DataFrame.from_records(json_data_list)

print(df.info())
print(df.shape[0])


