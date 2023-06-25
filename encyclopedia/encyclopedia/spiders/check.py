import redis

# Connect to Redis
redis_client = redis.Redis(host='10.147.19.195', port=6379)

# Check the data type of the key
key = 'geturl_delete:items'  # Replace 'your_key' with the actual key name
data_type = redis_client.type(key)

print(f"Data type of {key}: {data_type}")
