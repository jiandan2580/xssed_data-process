#！/usr/bin/env python
#-*- coding:utf-8 -*-
'''
file = open("example.csv")
while 1:
    lines = file.readlines(10)
    print(lines)
    if not lines:
        break
    for line in lines:
        pass
file.close()
'''
'''下述代码用于删除csv文件中重复的字符串'''
'''保证每一行都是独一无二的数据。处理了原始的两个文件，也处理了解码后的两个文件，得到四个新的文件'''
import shutil
readDir = "xssed_unquote_new.csv"   #四个文件需要去重：normal_examples.csv、normal_examples_unquote_new.csv、xssed.csv、xssed_unquote_new.csv
writeDir = "quchong_xssed_unquote_new.csv" #得到四个去重后的文件：quchong_normal_examples.csv、quchong_normal_examples_unquote_new.csv、quchong_xssed.csv、quchong_xssed_unquote_new.csv
lines_seen = set()
outfile = open(writeDir,"w",encoding='UTF-8')
f = open(readDir,"r",encoding='UTF-8')
for line in f:
    if line not in lines_seen:
        outfile.write(line)
        lines_seen.add(line)
outfile.close()
print("finish")





































