#！/usr/bin/env python
#-*- coding:utf-8 -*-
'''前两个同名文件夹读取数据后，丢失太严重，就改了一下原始数据中的格式，本代码直接执行把空格换成逗号即可，希望不出现大量丢失的情况'''
'''结果还是不好  失败了！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！'''
# s=[]
# #path='data-banjiao/banjiao-fanhua-fenci_xssed_unquote_new.txt'
# path='data-banjiao/banjiao-fanhua-fenci_normal_examples_unquote_new_part.txt'
# file = open(path,'r',encoding='utf-16')#.read()
# #file = file.lower()
# for lines in file:
#     ls = lines.replace(',',' ')#这两句话把逗号都换成空格
#     ls=ls.strip().split('[]')
#     for i in ls:
#         s.append(i)
# file.close()
# print("处理完写到txt文件中的"+str(s))
#
# #f1 = open('data-banjiao/banjiao-Mid-xssed.txt','w')
# f1 = open('data-banjiao/banjiao-Mid-normal.txt','w')
# for j in s:
#     f1.write(j+'\n')
# f1.close()

s=[]
#path='data-banjiao/banjiao-Mid-xssed.txt'
path='data-banjiao/banjiao-Mid-normal.txt'
file = open(path,'r')
for lines in file:
    ls = lines.replace(' ',',')  # 这两句话把空格换成逗号，然后就恢复正常了
    ls = ls.strip().split('[]')
    for i in ls:
        s.append(i)
file.close()

#f1 = open('data-banjiao/banjiao-ML-xssed.txt','w')
f1 = open('data-banjiao/banjiao-ML-normal.txt','w')
for j in s:
    f1.write(j+'\n')
f1.close()