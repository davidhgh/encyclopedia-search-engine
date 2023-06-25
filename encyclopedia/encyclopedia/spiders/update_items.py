import redis
import pandas as pd
import json

# Connect to Redis
redis_client = redis.Redis(host='10.147.19.195', port=6379)

key = 'geturl_delete:items'



redis_client.hset(key, 0, '{"url": "https://www.encyclopedia.com/", "title": "Encyclopedia.com | Free Online Encyclopedia", "description": "Encyclopedia.com  Online dictionary and encyclopedia with pictures, facts, and videos. Get information and homework help with millions of articles in our FREE, online library.", "abstract": "Encyclopedia.com  Online dictionary and encyclopedia with pictures, facts, and videos. Get information and homework help with millions of articles in our FREE, online library.", "content": "Encyclopedia.com | Free Online Encyclopedia       Skip to main content                    EXPLORE EXPLORE Earth and Environment History Literature and the Arts Medicine | All rights reserved.    ", "html": "PCFET0NUWVBFIGh0bWwk+PC9odG1sPg=="}')


# Retrieve the list from Redis
# json_data_string = redis_client.get(key)

# # Deserialize the JSON string into a list
# json_data_list = json.loads(json_data_string.decode())

# print(json_data_list)

# # Deserialize the JSON string into a list
# json_data_list = json.loads(json_data_string)

# # Find the index of the element you want to update
# index_to_update = 1  # Example: updating the second element

# # Update the element at the specified index
# json_data_list[index_to_update] = {"id": 2, "name": "Updated Name"}

# # Serialize the updated list back to a JSON string
# updated_json_data_string = json.dumps(json_data_list)

# # Set the updated JSON string as the value of the Redis key
# redis_client.set('your_key', updated_json_data_string)






