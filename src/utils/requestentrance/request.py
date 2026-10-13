import pytest
import requests
import json
import re

from src.utils.assertion.assertion import Assertion
from src.utils.formatparser.propertyparser import PropertyParser
from src.utils.formatparser.yamlparser import YamlParser
from src.utils.formatparser.extraction import Extraction

class Requests:
    # 测试数据文件路径
    YAML_FILE = 'D:/automatictesttools/interrogation-test/InterrogationPyTest/src/data/userinfo-case.yaml'

    # 全局变量占位符，形如 ${base_url}
    _PLACEHOLDER = re.compile(r'\$\{(\w+)\}')
    session = requests.session()
    variables = PropertyParser().readAll()
    cases = YamlParser(YAML_FILE).parseTodict()

    # ---------------- 数据准备 ----------------

    @classmethod
    def _skip_condition(cls, case):
        """用例标记 skip 或缺少必需全局变量时跳过，避免用例误失败"""
        if case.get('skip'):
            pytest.skip(f"前端校验项，接口层无法自动化：{case.get('remark', '')}")
        raw = json.dumps(case, ensure_ascii=False)
        placeholders = sorted(set(Requests._PLACEHOLDER.findall(raw)))
        missing = [p for p in placeholders if p not in cls.variables]
        empty = [p for p in placeholders if p in cls.variables and not cls.variables.get(p)]
        if missing:
            pytest.skip(f"缺少全局变量定义，请在 global-variable.properties 中补充：{missing}")
        if empty:
            pytest.skip(f"全局变量未配置，请填写后执行：{empty}")

    @classmethod
    def _resolve(cls, value):
        """递归替换字符串中的 ${变量} 占位符"""
        if isinstance(value, str):
            for key, val in cls.variables.items():
                value = value.replace('${' + key + '}', val or '')
            return value
        if isinstance(value, dict):
            return {k: cls._resolve(v) for k, v in value.items()}
        if isinstance(value, list):
            return [cls._resolve(v) for v in value]
        return value
    # ---------------- 请求执行 ----------------

    @classmethod
    def _send_request(cls, case):
        """根据用例数据发送请求：GET 带查询参数，POST 带 JSON 体或表单"""
        request_conf = case['request']
        #通过类方法第一个参数可以访问类属性
        url = cls._resolve(request_conf['url'])
        headers = cls._resolve(request_conf.get('headers') or {})
        params = cls._resolve(request_conf.get('params') or {})
        form_data = cls._resolve(request_conf.get('form-data') or {})
        # params 中值为 null 的字段不传（等价于请求体缺失该字段）
        if isinstance(params, dict):
            params = {k: v for k, v in params.items() if v is not None}

        req_type = str(request_conf.get('type', 'get')).lower()
        if req_type == 'get':
            return cls.session.get(url=url, headers=headers, params=params or None, timeout=10)
        if req_type == 'post':
            if form_data:
                return cls.session.post(url=url, headers=headers, data=form_data, timeout=10)
            return cls.session.post(url=url, headers=headers, json=params or None, timeout=10)
        if req_type == 'put':
            return cls.session.put(url=url, headers=headers, json=params or None, timeout=10)
        if req_type == 'delete':
            return cls.session.delete(url=url, headers=headers, params=params or None, timeout=10)
        raise ValueError(f"不支持的请求方式：{req_type}")

    # ---------------- 通用执行入口 ----------------

    @classmethod
    def run_case(cls, case):
        cls._skip_condition(case)
        response = cls._send_request(case)
        Assertion.assert_response(response, case['assert'])
        Extraction.extract_globals(case, response)
