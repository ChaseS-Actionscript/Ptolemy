__author__ = "Chase Saltzman"
__copyright__ = "Copyright (C) 2024 Chase Saltzman"
__license__ = "Public Domain"
__version__ = "1.0"
import requests, mimetypes
from typing import List
from bs4 import BeautifulSoup
"""
TODO
lxml - etree might be the solution to faster parsing current iteration is already fast but it could be faster
lxml is mostly used for xml though
also xml has a vulnerability that has to do with not disabling dtd entity expansion
"""
class Crawler:
    def __init__(self, base_url):
        self.base_url = base_url
        self.visited = set()
        self.edges: List[tuple] = []
        
    def crawl(self):
        self._visit(self.base_url)
        return self.edges  

    def _visit(self, url):
        if url not in self.visited and mimetypes.guess_type(url)[0] == mimetypes.guess_type("https://google.com")[0]:
            print(f"Visiting: {url}")
            self.visited.add(url)
            try:
                response = requests.get(url)
                soup = BeautifulSoup(response.content, 'html.parser') 
                for link in soup.find_all('a', href=True):
                    full_url = self._make_full_url(link['href'])
                    self.edges.append((url, full_url))
            except Exception as e:
                print(f"Failed to visit {url}: {e}")

    def _make_full_url(self, link):
        if link.startswith('http'):
            return link
        return f"{self.base_url}/{link.lstrip('/')}"