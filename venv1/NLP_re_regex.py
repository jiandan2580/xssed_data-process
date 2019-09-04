#！/usr/bin/env python
#-*- coding:utf-8 -*-
'''原文链接：https://blog.csdn.net/meiqi0538/article/details/80788115'''
import re
#测试文档
text_string='文本最重要的来源无疑是网络。我们要把网络中的文本获取形成一个文本数据库。利用一个爬虫爬取到网络中的信息。爬取的策略又广度爬取和深度爬取。根据用户的需求，爬虫可以有主题爬虫和通用爬虫。'
#定义一个简单的正则表达式
regex1='爬虫'
regex2='爬.'
#使用。把测试文档进行分割
p_string = text_string.split("。")
#对分割后的句子进行遍历，找出与正则表达式匹配的句子
for line in p_string:
#search方法是用来查找匹配当前行是否匹配这个regex，返回一个match对象
    if re.search(regex1,line) is not None:
        #如果匹配到了就打印处理啊
        print("符合regex1："+line)
for line in p_string:
    if re.search(regex2,line) is not None:
        print("符合regex2："+line)

'''----------------------------------------------------------'''
'''例如： 
“^a”：匹配所有a开始的字符串 
“a$”：匹配所有a结尾的字符串 
案例：查找以”文本”起始的句子：'''
regex3="^文本"
for line in p_string:
    if re.search(regex3,line) is not None:
        print("符合regex3："+line)
'''------------------------------------------------------'''
'''使用中括号匹配多个字符，如：”[brc]at”代表的是匹配“bat””cat”以及“rat”'''
text_string2=['[重要的]今年第七号台风23日登录广东东部沿海地区','上海发布车库销售监管通知：违规者暂停网签资格','[紧要的]中国对印连发强硬信息，印度急切需要结束对峙']
# 定义一个简单的正则表达式，需要注意的是这里的 \ 是转移字符(在正则表达式中[]是特殊符号)
regex4="^\[[重紧]..\]"
for line in text_string2:
    if(re.search(regex4,line)) is not None:
        print("符合regex4："+line)
'''使用转义字符：加入要匹配 “\” ，那么编程语言表示的正则表达式就需要 “\\” (前两个和后两个分别用于在编程语言里转义为反但斜杠，转成两个反但斜杠后再在正则表达式里转义为一个反斜杠)
然而在python中使用  r"\\" 即可解决'''
if re.search(r"\\","I have one nee\dle") is not None:
    print("match")
print('--------------------------------------------------------------------------')

'''3、抽取文字中的数字'''
'''3.1、通过正则表达式抽取年份'''
import re
strings=['War of 1812','There are 5280 feet to a mile','Happy New Year 2016!']
#定义一个存放结果集的列表
year_strings=[]
for string in strings:
    #这里的正则表达式：年份匹配范围为1000-2999，{3}表示重复[0-9]三次
    if re.search("[1-2][0-9]{3}",string):
        year_strings.append(string)
print(year_strings)
'''3.2、抽取所有的年份'''
'''这是使用re模块的另一个方法findall()来返回匹配带正则表达式的那部分字符串。
re.findall("[a-z]","abc")得到的结果：[‘a’,’b’,’c’]。 '''
import re
year_strings2='2016 was a good year,but 2017 will be better!'
print(re.findall('[2][0-9]{3}',year_strings2))






















