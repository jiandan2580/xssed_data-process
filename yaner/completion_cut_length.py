# ！/usr/bin/env python
# -*- coding:utf-8 -*-
'''本代码用于截取固定长度的字符,例如每个列表长为128，但是末尾还没有加上malicious或者begien这两个标记，
还不能放到ML代码里去训练，这里得到的两个文件“normal-jiequ-finish.txt”、“normal-jiequ-finish1.txt”
需要放到tail-zengjia-zifu.py 这里去添加标记'''

'''此代码是测试代码  https://blog.csdn.net/qq_43243022/article/details/83004413  贼优秀
前提是txt文本中没有  [  ]  '   这三个符号，只有逗号
import numpy as np
file = open('data-ML/test.txt','r',encoding='utf16')   test.txt文件已经删除
dataMat=[]
labelMat=[]
for line in file.readlines():
    curLine = line.strip().split(",")
    listLine = list(map(str,curLine))# 这里使用的是map函数直接把数据转化成为list类型
    dataMat.append(listLine[0:128])
#print('dataMat:',np.array(dataMat))
print('dataMat:',dataMat)
print(np.shape(dataMat))'''

import numpy as np
# file = open('data-ML/xssed-jiequ-finish.txt','r')
file = open('data-ML/normal-jiequ-finish.txt','r',encoding='utf16')
dataMat = []
for line in file.readlines():
    curLine = line.strip().split(",")
    listLine = list(map(str,curLine))
    dataMat.append(listLine[0:128])  # 截取文档中第一个到第128个元素
# print('dataMat:',np.array(dataMat))
print(np.shape(dataMat))

# f1 = open('data-ML/xssed-jiequ-finish1.txt','w')
f1 = open('data-ML/normal-jiequ-finish1.txt','w',encoding='utf16')
for j in dataMat:
    f1.write(str(j)+'\n')
f1.close()