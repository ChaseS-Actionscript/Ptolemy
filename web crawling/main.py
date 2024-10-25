from crawler import Crawler
import sys, os
def main():
    url = sys.argv[1]
    crawler = Crawler(url)
    urls = crawler.crawl()
    x = lambda a : a.rstrip(r"\web crawling")
    base_directory = x(os.path.dirname(os.path.realpath(__file__)))
    with open(f"{base_directory}\\tmp.csv", 'w') as file: #TODO: make a temp folder so base_directory has a purpose
        file.writelines(",".join([*urls]))
    print(urls)
if __name__ == "__main__":
    main()
