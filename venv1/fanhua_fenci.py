#！/usr/bin/env python
#-*- coding:utf-8 -*-
'''本串代码用于将normai_examole_unquote.csv和xssed——unquote.csv文件进行下一步处理，
即范化和分词。把数字和超链接范化，将数字替换为”0”,超链接替换为http://u。'''
'''最后生成两个文件：fanhua-fenci_normal_examples_unquote_new.csv和fanhua-fenci_xssed_unquote_new.csv'''
import nltk
import re
import csv
import codecs
from urllib.parse import unquote
import tensorflow as tf
import keras.backend.tensorflow_backend as ktf
def GeneSeg(payload):
    #数字泛化为"0"
    payload=payload.lower()
    #payload=unquote(unquote(payload))   这里已经解过码了，所以不用再解码了
    payload,num=re.subn(r'\d+',"0",payload)
    #替换url为”http://u
    payload,num=re.subn(r'(http|https)://[a-zA-Z0-9\.@&/#!#\?]+', "http://u", payload)
    #分词
    r = '''
        (?x)[\w\.]+?\(
        |\)
        |"\w+?"
        |'\w+?'   
        |http://\w
        |</\w+>
        |<\w+>
        |<\w+
        |\w+=
        |>
        |[\w\.]+
    '''
    return nltk.regexp_tokenize(payload, r)
def init_session():
    #gpu_options=tf.GPUOptions(allow_growth=True)
    ktf.set_session(tf.Session())


'''normal_examples_unquote.csv和xssed_unquote.csv两个文件打不开，因为文件里存在空字符，
所以用normal_examples_unquote_new.csv和xssed_unquote_new.csv这两个文件,已经经过“空字符删除”处理'''
#readerr = csv.reader(open('data\\xssed_unquote_new.csv',"r",encoding="utf-8"))
readerr = csv.reader(open('data\\normal_examples_unquote_new.csv',"r",encoding="utf-8"))
dict_data=[]
for lines in readerr:
    if readerr.line_num == 1:
        continue
    else:
        dict_data.append(lines)
row_num = len(dict_data)

del readerr  #为了节约内存，读完文件就删
'''循环读取每一行并解码'''

i=0
payloads=[]
while(i<row_num):
    payload=GeneSeg(str(dict_data[i]))
    print(str(i) + 'row----' + str(payload))
    payloads.append(str(payload).replace(' ','').split(' '))
    i += 1


#file_csv = codecs.open('data\\fanhua-fenci_xssed_unquote_new.csv','w',encoding='utf-8')
file_csv = codecs.open('data\\fanhua-fenci_normal_examples_unquote_new.csv','w',encoding='utf-8')
writer = csv.writer(file_csv,delimiter=' ', quotechar=' ', quoting=csv.QUOTE_MINIMAL)
for data in payloads:
    writer.writerow(data)
print("保存文件成功！")





















