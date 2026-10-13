from src.utils.formatparser.propertyparser import PropertyParser
from src.utils.formatparser.extraction import Extraction

class Assertion:

    variables = PropertyParser().readAll()
    # ---------------- 响应断言 ----------------

    @classmethod
    def assert_response(cls, response, assert_conf):
        """断言 HTTP 状态码及 JSON 字段"""
        expected_status = assert_conf.get('status', 200)
        assert response.status_code == expected_status, \
            f"HTTP 状态码不符：实际 {response.status_code}，期望 {expected_status}"
        if not response.content:
            return
        try:
            body = response.json()
        except ValueError:
            return
        for check in assert_conf.get('checks') or []:
            actual = Extraction.extract_value(body, check['path'])
            expected = check.get('expected')
            op = check.get('type', 'eq')
            if op == 'eq':
                assert actual == expected, f"{check['path']} 断言失败：实际 {actual!r}，期望 {expected!r}"
            elif op == 'ne':
                assert actual != expected, f"{check['path']} 断言失败：实际 {actual!r}，不应等于 {expected!r}"
            elif op == 'contains':
                assert expected in actual, f"{check['path']} 断言失败：实际 {actual!r} 不包含 {expected!r}"
            elif op == 'not_contains':
                assert expected not in actual, f"{check['path']} 断言失败：实际 {actual!r} 不应包含 {expected!r}"
            elif op == 'not_null':
                assert actual is not None, f"{check['path']} 断言失败：字段为空"
            else:
                raise ValueError(f"不支持的断言类型：{op}")