from crawler import Crawler
from sitemap import Sitemap
import sys
def main():
    url = sys.argv[1]
    crawler = Crawler(url)
    urls = crawler.crawl()
    
    sitemap = Sitemap(urls)
    sitemap.generate_sitemap()

if __name__ == "__main__":
    main()
