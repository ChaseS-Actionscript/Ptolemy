__author__ = "Chase Saltzman"
__copyright__ = "Copyright (C) 2024 Chase Saltzman"
__license__ = "Public Domain"
__version__ = "1.0"
import requests, mimetypes, threading
from bs4 import BeautifulSoup

# Speeds for a test website
# ~12 seconds with default html parser
# ~10 seconds with lxml
# ~6 seconds with multithreading

class Crawler:
    def __init__(self, base_url):
        self.base_url = base_url
        self.visited = set()

    def crawl(self):
        #self.generate_threads(self.base_url, num_of_threads=3)
        self._visit(self.base_url) 
        return self.visited

    def _visit(self, url):
        if url not in self.visited and mimetypes.guess_type(url)[0] == mimetypes.guess_type("https://google.com")[0]:
            print(f"Visiting: {url}")
            self.visited.add(url)
            try:
                response = requests.get(url)
                soup = BeautifulSoup(response.content, 'lxml')
                for link in soup.find_all('a', href=True): # <a> tag defines a hyperlink
                    full_url = self._make_full_url(link['href'])
                    if full_url.startswith(self.base_url):
                        self._visit(full_url)
            except Exception as e:
                print(f"Failed to visit {url}: {e}")

    def _parse_html(self, html): # Identical purpose to the crawl function, but passes in html instead of a url for the first go
        soup = BeautifulSoup(html, 'lxml') # lxml is faster than default html parser
        for link in soup.find_all('a', href=True):
            full_url = self._make_full_url(link['href'])
            if full_url.startswith(self.base_url):
                self._visit(full_url)

    def generate_threads(self, url, num_of_threads):
        threads = []
        try:
            response = requests.get(url)
            html_document = response.text
            chunks = []
            iter = 0
            temp = ""
            end_tag = False
            # iterates through the html until the chunk is a good length, and it reaches an ending tag to seperate into a thread
            for i in html_document:
                iter += 1
                temp += i
                if i == "/":
                    end_tag = True
                elif i == "<":
                    end_tag = False
                elif iter >= len(html_document) / num_of_threads and i == ">" and end_tag:
                    chunks.append(temp)
                    iter = 0
                    end_chunk = False
                    temp = ""
            chunks.append(temp)
            for chunk in chunks:
                thread = threading.Thread(target=self._parse_html, args=(chunk,))
                threads.append(thread)
                thread.start()
        except Exception as e:
            print(f"Failed To Get Html From {url}: {e}")
        for thread in threads:
            thread.join()

    def _make_full_url(self, link):
        if link.startswith('http'):
            return link
        return f"{self.base_url}/{link.lstrip('/')}"
