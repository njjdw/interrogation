# Please install OpenAI SDK first: `pip3 install openai`
import os

from openai import OpenAI

client = OpenAI(
    api_key=os.environ.get('DEEPSEEK_API_KEY'),
    base_url="https://api.deepseek.com")

with open('D:/automatictesttools/interrogation-test/InterrogationPyTest/src/data/userinfo-case.yaml','r') as params_file:
    params = params_file.read()

prompt = f"""
你是一名专业的测试开发工程师。请根据以下yaml风格的测试用例，生成一个 pytest 风格的测试文件。
要求：
1、准备请求数据（路径、请求方式、请求头、请求参数(或表单数据)）
1.1、统一保存接口基础上下文：如http://119.91.65.108:88/interrogation/api
1.2、根据请求方式调用不同request方法
2、发送请求
3、接收响应结果
4、对响应状态码、数据进行断言，将有需要的数据储存在全局使用
5、每个测试函数以 test_ 开头
6、只输出 Python 代码，不要任何解释或注释
测试用例如下：
{params}
"""

response = client.chat.completions.create(
    model="deepseek-chat",
    messages=[
        {"role": "user", "content": prompt},
    ],
    stream=False
)



if __name__ == '__main__':
    print(response.choices[0].message.content)