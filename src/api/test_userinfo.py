"""用户信息接口自动化测试（数据驱动）

测试数据：data/userinfo-case.yaml（用例来源 Product/Interrogation Test Case.xlsx）
覆盖接口：token 校验 / 短信验证码 / 注册 / 登录
全局变量：global-variable.properties（${base_url}、${captcha}、${sms_code} 等占位符）
"""
from src.utils.formatparser.yamlparser import YamlParser
from src.utils.requestentrance.request import Requests

import pytest

class TestUserInfo:

    YAML_FILE = 'D:/automatictesttools/interrogation-test/InterrogationPyTest/src/data/userinfo-case.yaml'
    cases = YamlParser(YAML_FILE).parseTodict()

    # ---------------- 各接口测试用例 ----------------
    '''列表推导式[表达式 for list in item if 判断条件]'''
    @pytest.mark.parametrize("case", [c for c in cases.values() if c['interface'] == 'checkToken'],
                             ids=lambda c: c['title'])
    def test_checktoken(self, case):
        """token 校验接口：请求头为空 / 伪造 / 过期"""
        Requests.run_case(case)

    @pytest.mark.parametrize("case", [c for c in cases.values() if c['interface'] == 'getSmsCode'],
                             ids=lambda c: c['title'])
    def test_get_sms_code(self, case):
        """注册-短信验证码接口"""
        Requests.run_case(case)

    @pytest.mark.parametrize("case", [c for c in cases.values() if c['interface'] == 'regist'],
                             ids=lambda c: c['title'])
    def test_register(self, case):
        """注册接口（Excel Interrogation_001~014）"""
        Requests.run_case(case)

    @pytest.mark.parametrize("case", [c for c in cases.values() if c['interface'] == 'login'],
                             ids=lambda c: c['title'])
    def test_login(self, case):
        """登录接口（Excel Interrogation_015~022）"""
        Requests.run_case(case)

    @pytest.mark.parametrize("case", [c for c in cases.values() if c['interface'] == 'checkTokenValid'],
                             ids=lambda c: c['title'])
    def test_checktoken_with_login_token(self, case):
        """登录成功后使用提取的 token 校验接口（依赖 test_login 写入全局变量）"""
        Requests.run_case(case)
