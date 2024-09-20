__author__ = "Chase Saltzman"
__copyright__ = "Copyright (C) 2024 Chase Saltzman"
__license__ = "Public Domain"
__version__ = "1.0"
import requests, mimetypes
from bs4 import BeautifulSoup

class Crawler:
    def __init__(self, base_url):
        self.base_url = base_url
        self.visited = set()

    def crawl(self):
        self._visit(self.base_url)
        return self.visited

    def _visit(self, url):
        if url not in self.visited and mimetypes.guess_type(url)[0] == mimetypes.guess_type("https://google.com")[0]:
            print(f"Visiting: {url}")
            self.visited.add(url)
           
            try:
                response = requests.get(url)
                soup = BeautifulSoup(response.content, 'html.parser')
                for link in soup.find_all('a', href=True): # <a> tag defines a hyperlink
                    full_url = self._make_full_url(link['href'])
                    if full_url.startswith(self.base_url):
                        self._visit(full_url)
            except Exception as e:
                print(f"Failed to visit {url}: {e}")

    def _make_full_url(self, link):
        if link.startswith('http'):
            return link
        return f"{self.base_url}/{link.lstrip('/')}"
