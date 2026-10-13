from src.utils.formatparser.propertyparser import PropertyParser


class Extraction:
    variables = PropertyParser().readAll()

    @staticmethod
    def extract_value(data, path):
        """按 JSONPath（如 $.data.userInfo.token）提取响应字段"""
        if not path.startswith('$.'):
            raise ValueError(f"不支持的 JSONPath：{path}")
        node = data
        for key in path[2:].split('.'):
            node = node.get(key) or "提取失败"#使用node[key]会报错
        return node

# ---------------- 全局数据提取 ----------------

    @classmethod
    def extract_globals(cls, case, response):
        """将 extract 配置的响应字段写入全局变量（global-variable.properties）"""
        extract = case.get('extract') or {}
        if not extract or not response.content:
            return
        try:
            body = response.json()#返回字典
        except ValueError:
            return
        for key, path in extract.items():
            value = cls.extract_value(body, path)
            cls.variables[key] = str(value)
            PropertyParser().write(key, str(value))
            print(f"[extract] 全局变量 {key} = {value}")