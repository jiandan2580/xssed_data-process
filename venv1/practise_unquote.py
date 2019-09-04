#！/usr/bin/env python
#-*- coding:utf-8 -*-
'''本串代码用于尝试将data文件夹中的经过编码的原始数据xssed.csv文件进行一次解码。
即生成xssed_unquote_once.csv和xssed_unquote_once_new.csv，后者文件去除了空字符，更好'''
'''文件删掉了，如果需要，执行一边即可'''

import csv
import codecs
import urllib
import nltk
import re
from urllib.parse import unquote
import tensorflow as tf
import keras.backend.tensorflow_backend as ktf
import numpy as np

oldfile=csv.reader(open('data\\xssed.csv','r'))
dict_data=[]
for lines in oldfile:
    if oldfile.line_num == 1:
        continue
    else:
        dict_data.append(lines)
row_num = len(dict_data)

'''循环读取每一行并解码'''
i=0
payloads=[]
while(i<row_num):
    #payload=unquote(unquote(str(dict_data[i])))#.replace('[\'','').replace('\']','')))  csv文件中有无 [' '] 的关键
    payload = unquote(str(dict_data[i]).replace('[\'','').replace('\']',''))
    print(str(i) + 'row----' + payload)
    payloads.append(payload.split(','))
    i+=1


file_csv = codecs.open('data\\xssed_unquote_once.csv','wb',encoding='utf-8')
writer = csv.writer(file_csv,delimiter=' ', quotechar=' ', quoting=csv.QUOTE_MINIMAL)
for data in payloads:
    writer.writerow(data)
print("保存文件成功！")


'''上面生成的文件可能包含了空字符，下面的代码用于去除空字符'''
fi = open('data\\xssed_unquote_once.csv', 'r',encoding='UTF-8')
data = fi.read()
fi.close()
fo = open('data\\xssed_unquote_once_new.csv', 'wb')
fo.write(data.replace('\x00', '').encode('utf-8'))
fo.close()