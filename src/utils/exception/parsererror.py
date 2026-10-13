from dataclasses import dataclass


@dataclass
class ParseError(Exception):
    message: str
    code: int
    def __init__(self,message:str,code:int):
        self.message = message
        self.code = code
        super().__init__(self.message)