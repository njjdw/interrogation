from src.utils.exception.parsererror import ParseError


class PropertyParser:
    delimeter = '\n'
    annotation = '#'
    map_delimeter = "="
    def __init__(self):
        self.file = "D:/automatictesttools/interrogation-test/InterrogationPyTest/src/global-variable.properties"
        self.dicts = {}
        self.parse()

    def parse(self)->dict:
        try:
            with open(self.file,'r',encoding='utf-8') as stream:
                for line in stream:
                    line = line.lstrip()
                    if line == "":
                        break
                    if line.startswith(self.annotation):
                        continue
                    self.dicts[line[:line.rindex("=")]] = line[line.rindex("=")+1:].rstrip(PropertyParser.delimeter)
        except FileNotFoundError:
            raise ParseError
        return self.dicts

    def read(self,variable:str):
        return self.dicts[variable]

    def write(self,key:str,value:str):
        if '=' in key:
            raise ParseError("key must not contain '='",-200)
        if key is None or key == '':
            raise ParseError("key must not be empty",-201)

        if key in self.dicts:
            self.dicts[key] = value
            dictToString = self.convertToString(self.dicts)
            with open(self.file,'w',encoding='utf-8') as stream:
                stream.write(dictToString)
            return

        with open(self.file,'w',encoding='utf-8') as stream:
            stream.write(key + self.map_delimeter + value)
            stream.write('\n')

    def readAll(self):
        return self.dicts

    '''无法保存property文件的注释'''
    def convertToString(self,string:dict):
        toString = ""
        for key in string.keys():
            toString += key+PropertyParser.map_delimeter+string[key]+"\n"
        return toString
