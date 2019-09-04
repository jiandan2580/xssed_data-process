#！/usr/bin/env python
#-*- coding:utf-8 -*-
'''本代码把data文件夹中的normal_examples.csv文件的内容解码，并输出到新的normal_examples_unquote.csv文件夹中'''
'''生成了新的文件：normal_examples_unquote.csv和normal_examples_unquote_new.csv。内容一样，肉眼无法识别，后者去除了空字符，更好一点'''
import csv
import codecs
import urllib
import nltk
import re
from urllib.parse import unquote
import tensorflow as tf
import keras.backend.tensorflow_backend as ktf
import numpy as np

oldfile=csv.reader(open('data\\normal_examples.csv','r'))
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
    #payload=unquote(unquote(str(dict_data[i])))#.replace('[\'','').replace('\']',''))) csv文件中有无 [' '] 的关键
    payload = unquote(unquote(str(dict_data[i]).replace('[\'','').replace('\']','')))
    print(str(i) + 'row----' + payload)
    payloads.append(payload.split(','))
    i+=1


file_csv = codecs.open('data\\normal_examples_unquote.csv','wb',encoding='utf-8')
writer = csv.writer(file_csv,delimiter=' ', quotechar=' ', quoting=csv.QUOTE_MINIMAL)
for data in payloads:
    writer.writerow(data)
print("保存文件成功！")

'''上面生成的文件可能包含了空字符，下面的代码用于去除空字符'''
fi = open('data\\normal_examples_unquote.csv', 'r',encoding='UTF-8')
data = fi.read()
fi.close()
fo = open('data\\normal_examples_unquote_new.csv', 'wb')
fo.write(data.replace('\x00', '').encode('utf-8'))
fo.close()
