#！/usr/bin/env python
#-*- coding:utf-8 -*-
'''想要去掉txt文件中每一条记录末尾多余的逗号'''
'''https://blog.csdn.net/u012814856/article/details/78544956 Python 小技巧：去掉字符串首尾附带的标点符号'''
import string
# item = '[\'This\',\'is\',\'a\',\'demo\'],,,,,,,,,'
# item = item.strip(string.punctuation)
# print(item)

'''读取txt中的每一行，并删掉每行末尾的\n符号，并删掉末尾的多余符号，按行打印，输出到新的txt文件中'''
# s=[]
# path='data/normal_test.txt'
# file = open(path,encoding='gb18030',errors='ignore')
# for lines in file:
#     ls = lines.replace(',',' ')#这两句话把逗号都换成空格
#     ls=ls.strip().split('[]')
#     for i in ls:
#         s.append(i)
# file.close()
# print("处理完写到txt文件中的"+str(s))
#
#
# f1 = open('data/normal_test1.txt','w')
# for j in s:
#     f1.write(j+'\n')
# f1.close()

s=[]
file = open('data/normal_test1.txt','r')
for lines in file:
    ls = lines.replace(' ',',')#这两句话把空格换成逗号，然后就恢复正常了
    ls = ls.strip().split('[]')
    for i in ls:
        s.append(i)
file.close()

f1 = open('data/ML-fanhua-fenci_normal_unquote_new.txt','w')
for j in s:
    f1.write(j+'\n')
f1.close()