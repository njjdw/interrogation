import yaml


class YamlParser:

    def __init__(self, yamlfile):
        self.yamlfile = yamlfile
        self.data = {}
        self.parseTodict()

    def parseTodict(self):
        with open(self.yamlfile, 'r', encoding='utf-8') as f:
            self.data = yaml.load(f, Loader=yaml.FullLoader)
            return self.data

    def __getitem__(self, item):
        return self.data[item]
