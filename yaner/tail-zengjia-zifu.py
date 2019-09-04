#！/usr/bin/env python
#-*- coding:utf-8 -*-
'''本代码用于将文本末尾增加一个字符，字符包括malicious和benign'''
'''程序completion_cut_length.py生成的normal-jiequ-finish1.txt&xssed-jiequ-finish1.txt要用替换操作一下，
去掉  [  ]  '  空格  四个符号，得到normal-jiequ-finish2.txt&xssed-jiequ-finish2.txt'''
'''然后运行本程序，等到normal-jiequ-finish3.txt&xssed-jiequ-finish3.txt，再加上@feature部分就好了'''
s=[]
#file = open('data-ML/normal-jiequ-finish2.txt','r',encoding='utf-16')
file = open('data-ML/xssed-jiequ-finish2.txt','r')
for line in file:
    lines = line.split()
    ls = list(lines)
    for i in ls:
        #s.append(i + ',benign')
        s.append(i+',malicious')
file.close()
#f1 = open('data-ML/normal-jiequ-finish3.txt','w',encoding='utf-16')
f1 = open('data-ML/xssed-jiequ-finish3.txt','w')
for j in s:
    f1.write(j+'\n')
f1.close()

'''生成ormal-jiequ-finish3.txt&xssed-jiequ-finish3.txt这两个文件后，
可以直接在txt文本中添加上ML代码需要的feature和data两个标识，
变成ormal-jiequ-finish4.txt&xssed-jiequ-finish4.txt，即可放入ML代码使用了'''