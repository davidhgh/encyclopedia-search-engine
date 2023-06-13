from scrapy.linkextractors import LinkExtractor
from scrapy.spiders import CrawlSpider, Rule
import base64
from bs4 import BeautifulSoup

class GeturlSpider(CrawlSpider):
    name = "geturl"
    allowed_domains = ["encyclopedia.com"]
    start_urls = ["https://www.encyclopedia.com/"]

    rules = (
        Rule(LinkExtractor(), callback='parse', follow=True),
    )

    def parse(self, response):
        b64 = base64.b64encode(response.body)
        sb64 = b64.decode()
        meta_data = {
            "url": response.url,
            "title": response.xpath("//title/text()").extract_first(),
            "description": response.xpath('//meta[@name="description"]/@content').get(),
            "abstract": response.xpath('//meta[@name="abstract"]/@content').get(),
            "content": BeautifulSoup(response.body).get_text(),
            "html": sb64 
            #"content": response.xpath("//div[contains(@class, 'doccontentwrapper')]/p").getall(),   #Just to get an estimate of the JSON file size with content of pages
            # Add more meta information as needed
        }
        yield meta_data

