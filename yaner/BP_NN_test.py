# ！/usr/bin/env python
# -*- coding:utf-8 -*-

from BP_NN import NeuralNetwork
import numpy as np
nn = NeuralNetwork([2, 2, 1], 'tanh')
x = np.array([[0, 0], [0, 1], [1, 0], [1, 1]])
y = np.array([0, 1, 1, 0])
nn.fit(x, y)
for i in [[0, 0], [0, 1], [1, 0], [1, 1]]:
    print(i, nn.predict(i))

'''程序中测试的是异或关系'''
'''结果如下
[0, 0] [-0.00985869]
[0, 1] [0.9981696]
[1, 0] [0.99807252]
[1, 1] [-0.03136386]'''