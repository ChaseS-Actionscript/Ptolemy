__author__ = "Chase Saltzman"
__copyright__ = "Copyright (C) 2024 Chase Saltzman"
__license__ = "Public Domain"
__version__ = "1.0"
import xml.etree.ElementTree as ET

class Sitemap:
    def __init__(self, urls):
        self.urls = urls

    def generate_sitemap(self):
        urlset = ET.Element('urlset', xmlns="http://www.sitemaps.org/schemas/sitemap-image/1.1")
        
        for url in self.urls:
            url_element = ET.SubElement(urlset, 'url')
            ET.SubElement(url_element, 'loc').text = url

        tree = ET.ElementTree(urlset)
        with open("sitemap.xml", "wb") as f:
            tree.write(f)
        print("Sitemap generated: sitemap.xml")
