import scrapy
import json

class EncyclopediaSpider(scrapy.Spider):
    name = "encyclopedia"
    allowed_domains = ['encyclopedia.com']
    keywords = [
        "Computer", "Glasgow", "United", "Kingdom", "Library", "Fog", "Empires", "Doctor", "Hospital", "Bachelor",
        "Degree", "Internet", "Things", "Information", "Info", "Retrieval", "Retrieve", "Info", "Universe", "University",
    ]
    start_urls = [
        "https://www.encyclopedia.com/economics/news-and-education-magazines/computer-software-engineer",   #961
        "https://www.encyclopedia.com/social-sciences/encyclopedias-almanacs-transcripts-and-maps/computers",   #961
        "https://www.encyclopedia.com/books/politics-and-business-magazines/systems-computer-technology-corp",  #961
    ]
    visited_urls = set()

    def start_requests(self):   # Overrides default start_requests() function and start_urls
        urls = [
            "https://www.encyclopedia.com/social-sciences/encyclopedias-almanacs-transcripts-and-maps/computers",
        ]
        for url in urls:
            yield scrapy.Request(url=url, callback=self.parse)
        return super().start_requests()

    def parse(self, response):
        current_url = response.url
        if current_url in self.visited_urls:
            return  # Skip Processing visited URL
        
        self.visited_urls.add(current_url)  # Mark current URL as visited
        data = []

        # Extract meta information
        meta_data = {
            "url": response.url,
            "title": response.xpath("//title/text()").extract_first(),
            "description": response.xpath('//meta[@name="description"]/@content').get(),
            "abstract": response.xpath('//meta[@name="abstract"]/@content').get(),
            "content": response.xpath("//div[contains(@class, 'doccontentwrapper')]/p").getall(),   #Just to get an estimate of the JSON file size with content of pages
            # Add more meta information as needed
        }

        # Save the meta information to a separate JSON file
        with open(f"meta-{self.keywords[0]}.json", "a+") as f:
            f.write(json.dumps(meta_data) + ",\n")

        # next_page_url = response.xpath('//a[@href]')  #Crawls all url on page
        next_page_url = response.xpath('//a[contains(@href, "computer")]/@href')
        for url in next_page_url:
            if url in self.visited_urls:
                continue    # Skip visited URL
            
            self.visited_urls.add(url)  # Mark the URL as visited

            yield response.follow(url, self.parse)