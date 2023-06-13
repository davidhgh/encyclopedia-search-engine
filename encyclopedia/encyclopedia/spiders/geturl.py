from scrapy.linkextractors import LinkExtractor
from scrapy.spiders import CrawlSpider, Rule
import base64

class GeturlSpider(CrawlSpider):
    name = "geturl"
    allowed_domains = ["encyclopedia.com"]
    start_urls = ["https://www.encyclopedia.com/"]

    keywords = [
        "computer", "glasgow", "united", "kingdom", "library", "fog", "empires", "doctor", "hospital", "bachelor",
        "degree", "internet", "things", "information", "info", "retrieval", "retrieve", "info", "universe", "university",
    ]

    rules = (
        Rule(LinkExtractor(), callback='parse', follow=True),
    )

    def parse(self, response):
        checktext = response.text.lower().split()
        if any(keyword in checktext for keyword in self.keywords):
            b64 = base64.b64encode(response.body)
            sb64 = b64.decode()
            meta_data = {
                "url": response.url,
                "title": response.xpath("//title/text()").extract_first(),
                "description": response.xpath('//meta[@name="description"]/@content').get(),
                "abstract": response.xpath('//meta[@name="abstract"]/@content').get(),
                "content": response.text,
                "html": sb64
                #"content": response.xpath("//div[contains(@class, 'doccontentwrapper')]/p").getall(),   #Just to get an estimate of the JSON file size with content of pages
                # Add more meta information as needed
            }
            yield meta_data

