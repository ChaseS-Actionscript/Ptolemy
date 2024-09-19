from crawler import Crawler
from sitemap import Sitemap
import sys
def main():
    url = input("Enter the URL of the website: ")
    crawler = Crawler(url)
    urls = crawler.crawl()
    
    sitemap = Sitemap(urls)
    sitemap.generate_sitemap()

if __name__ == "__main__":
    main()
