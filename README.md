# encyclopedia-search-engine
CSC3007 Information Retrieval Assignment to create a lucene backed search engine for encyclopedia.com

# Run scraper
- Connect to Zero Tier network: abfd31bd476860fd
- Add "REDIS_URL = 'redis://10.147.19.195:6379" to "encyclopedia-search-engine\encyclopedia\encyclopedia\settings.py"
- Start venv with "venv/Scripts/activate"
- Install libraries with "pip install -r requirements.txt"
- Switch directories with "cd encyclopedia/encyclopedia/spiders"
- Start scraping with "scrapy crawl geturl" -> Scale up by running in multiple terminals
