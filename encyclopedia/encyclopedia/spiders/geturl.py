from scrapy.linkextractors import LinkExtractor
from scrapy.spiders import CrawlSpider, Rule
import base64

class GeturlSpider(CrawlSpider):
    name = "geturl"
    allowed_domains = ["encyclopedia.com"]
    start_urls = ["https://www.encyclopedia.com/"]

    keywords = [
        "Computer", "Glasgow", "United", "Kingdom", "Library", "Fog", "Empires", "Doctor", "Hospital", "Bachelor",
        "Degree", "Internet", "Things", "Information", "Info", "Retrieval", "Retrieve", "Info", "Universe", "University",
    ]

    rules = (
        Rule(LinkExtractor(), callback='parse', follow=True),
    )

    def parse(self, response):
        if any(keyword in response.text for keyword in self.keywords):
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

