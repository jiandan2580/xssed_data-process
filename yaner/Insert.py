#！/usr/bin/env python
#-*- coding:utf-8 -*-
'''本代码用于增加txt文本开头字符以及末尾的字符'''

# file_from = open('data-ML/xssed-zengjia-finish.txt','r')
# file = open('data-ML/xssed-zengjia-head.txt','w')
file_from = open('data-ML/normal-zengjia-finish.txt','r')
file = open('data-ML/normal-zengjia-head.txt','w')
for line in file_from:
    file.writelines('['+line)
file.close()
file_from.close()


'''要在txt文档末尾增加  ]  这个符号，需要按照completion_truncation_length.py 13-28行的内容增加'''
s=[]
#file = open('data-ML/xssed-zengjia-head.txt','r')
file = open('data-ML/normal-zengjia-head.txt','r')
for line in file:
    lines = line.split()
    ls = list(lines)
    for i in ls:
        #s.append(i)
        s.append(i+']')
file.close()
#f1 = open('data-ML/xssed-zengjia-tail.txt','w')
f1 = open('data-ML/normal-zengjia-tail.txt','w')
for j in s:
    f1.write(j+'\n')
f1.close()