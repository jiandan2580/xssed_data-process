#！/usr/bin/env python
#-*- coding:utf-8 -*-
'''本代码用于删除文本中每一行的末尾一个字符       可以试试文本文档的替换'''




'''
截取单个列表中前128个元素
lis = ['avi=','bgnqc0rswbqvcmpw0atsnjqwdwcu0rvgewaaeae0acgbasgdyqsgbglscauiggeqacitbhicpobvaw','cid=','caaseuropushlkgejd0alajv0wdo0w','id=','osdtos','ti=','0','r=','u','adk=','0','tt=','0','bs=','0','0','mtos=','0','0','0','0','0','tos=','0','0','0','0','0','p=','0','0','0','0','mcvt=','0','rs=','0','ht=','0','tfs=','0','tls=','0','mc=','0','lte=','0','bas=','0','bac=','0','bos=','0','0','ps=','0','0','ss=','0','0','pt=','0','deb=','0','0','0','0','0','0','tvt=','0','is=','0','0','iframe_loc=','http://u','0','>','>','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0']
count = 0
s=[]
for element in lis:
    if count <12:
        s.append(element)
        count+=1
print(s)'''



'''统计文件中每行的字符数、总行数、及总共的字符数
filename = input('Please input file: -&gt; ')
linesum = open(filename,encoding='utf16').readlines()
Charsum = open(filename, 'rb').read()
for x in range(len(linesum)):
    print("%3d  line:  %4d chars" % (x + 1, len(linesum[x])))
print("Total %d lines, %d chars" % (len(linesum), len(Charsum)))'''



'''
本代码用于截取文本中每一行0-3个字符，即前四个字符
f = open("data-ML/normal-jiequ-finish.txt", "r",encoding='utf16')
line = f.readline()
while line != "":
    s = line[:4]
    print(s)
    line = f.readline()
f.close()'''