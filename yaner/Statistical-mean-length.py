#！/usr/bin/env python
#-*- coding:utf-8 -*-
'''ML-fanhua-fenci_xssed_unquote_new.txt 但是还不足以应用到机器学习的代码中
还需要把ML-fanhua-fenci_xssed_unquote_new.txt中的方括号、单引号去掉(直接txt文档替换就行)，
还要截断成等长(截断成等长之前先统计一下平均长度是多少)'''
'''本代码想统计ML-fanhua-fenci_xssed_unquote_new.txt中的所有列表字符串(元素)的个数总和，然后取平均，
然后再应用到机器学习代码前，选择合适的字符串长度'''
# #应用到机器学习内的文件的字符串数目
# X=0
# file = open('data/ML-fanhua-fenci_xssed_unquote_new.txt','r')
# for lines in file:
#     num=len(lines)
#     X+=num
#     #file.close()
# print('恶意脚本字符串个数：')
# print(X)
# #打印结果是  1671144      因为上述恶意文件总共有15279行，所以平均每行有109.4个字符串
# print('\n')
# N=0
# file = open('data/ML-fanhua-fenci_normal_unquote_new.txt','r')
# for lines in file:
#     num=len(lines)
#     N+=num
#     #file.close()
# print('正常脚本字符串个数：')
# print(N)
# #打印结果是  2066487      因为上述良性文件总共有12599行，所以平均每行有164个字符串
#
#
# '''原始的csv文件，泛化分词之后变成字符串'''
# #应用到神经网络内的文件的字符串数目
# X=0
# file = open('data/fanhua-fenci_xssed_unquote_new.csv','r',encoding='utf-8')
# for lines in file:
#     num=len(lines)
#     X+=num
#     #file.close()
# print('恶意脚本字符串个数：')
# print(X)#输出结果为：4551903    因为上述恶意文件总共有40513行，所以平均每行有112.3个字符串
# print('\n')
# N=0
# file = open('data/fanhua-fenci_normal_examples_unquote_new.csv','r',encoding='utf-8')
# for lines in file:
#     num=len(lines)
#     N+=num
#     #file.close()
# print('正常脚本字符串个数：')
# print(N)#输出结果为：33015211    因为上述良性文件总共有202147行，所以平均每行有163.3个字符串