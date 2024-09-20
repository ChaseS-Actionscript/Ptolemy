__author__ = "Chase Saltzman"
__copyright__ = "Copyright (C) 2024 Chase Saltzman"
__license__ = "Public Domain"
__version__ = "1.0"
import requests
from bs4 import BeautifulSoup

class Crawler:
    def __init__(self, base_url):
        self.base_url = base_url
        self.visited = set()

    def crawl(self):
        self._visit(self.base_url)
        return self.visited

    def _visit(self, url):
        if url not in self.visited:
            print(f"Visiting: {url}")
            self.visited.add(url)

            # skip over html parsing if url ends with an invalid file extension because it wont contain any hyperlinks
            # theres a bunch of other file types that could be found in the <a> tag that I didnt include
            # this solution still adds the file to the sitemap, so it can be marked as a valid redirect

            validExtension = True
            invalidFileTypes = ['.pdf', '.jpg', '.png', '.gif', '.mp4', '.mp3', '.doc', '.docx', '.xlsx', '.ppt', '.zip', '.rar', '.exe', '.css', '.js', '.xml', '.json']
            for extension in invalidFileTypes:
                if(url.endswith(extension)):
                    print(f'Skipping over {extension} file')
                    validExtension = False
                    break

            # might be overkill, but checks the type through the file header in case something made it through

            if(validExtension):
                fileType = requests.head(url)
                type = fileType.headers.get("Content-Type", "")
                if('text/html' not in type):
                    validExtension = False
                    print(f'Skipping over invalid file')

            # if both checks pass, runs the parser

            if(validExtension):
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
