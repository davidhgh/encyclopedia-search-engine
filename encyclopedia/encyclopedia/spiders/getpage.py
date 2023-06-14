from scrapy_redis.spiders import RedisSpider
import base64

class PageSpider(RedisSpider):
    """Follow categories and extract links."""
    name = 'getpage'
    redis_key = 'geturl:items'

    def parse(self, response):
        b64 = base64.b64encode(response.body)
        sb64 = b64.decode()
        meta_data = {
            "url": response.url,
            "title": response.xpath("//title/text()").extract_first(),
            "description": response.xpath('//meta[@name="description"]/@content').get(),
            "abstract": response.xpath('//meta[@name="abstract"]/@content').get(),
            "html": sb64 
            #"content": response.xpath("//div[contains(@class, 'doccontentwrapper')]/p").getall(),   #Just to get an estimate of the JSON file size with content of pages
            # Add more meta information as needed
        }
        yield meta_data
