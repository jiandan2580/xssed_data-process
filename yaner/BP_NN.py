# ！/usr/bin/env python
# -*- coding:utf-8 -*-
'''BP神经网络原理推导及程序实现'''
'''https://www.cnblogs.com/xuhongbin/p/6666826.html'''
import numpy as np
'''神经网络的激活函数(Activation Function)及python代码实现'''
'''https://blog.csdn.net/carmelcarmen/article/details/79344996'''

# tanh 函数
def tanh(x):
    return np.tanh(x)


# tanh 函数的导数
def tanh_derivative(x):      # derivative的意思是导数
    return 1 - np.tanh(x)*np.tanh(x)


# sigmoid 函数
def logistic(x):
    return 1/(1+np.exp(-x))


# sigmoid 函数的导数
def logistic_derivative(x):
    return logistic(x)*(1-logistic(x))


class NeuralNetwork:
    def __init__(self, layers, activation='tanh'):  # “activation”参数决定了激活函数的种类，是tanh函数还是sigmoid函数。
        if activation == 'logistic':
            self.activation = logistic
            self.activation_deriv = logistic_derivative
        elif activation == 'tanh':
            self.activation = tanh
            self.activation_deriv = tanh_derivative
        # 随机产生权重值
        self.weights = []  # 以隐含层前后层计算产生权重参数，参数初始时随机，取值范围是[-0.25, 0.25]
        for i in range(1, len(layers)-1):  # 不算输入层，循环
            self.weights.append((2*np.random.random((layers[i-1]+1, layers[i]+1))-1)*0.25)
            self.weights.append((2*np.random.random((layers[i]+1, layers[i+1]))-1)*0.25)
            #self.weights.append((2*np.random.random((layers[i+1], layers[i+2]))-1)*0.25)
            print("输入层、隐含层和输出层的数量是："+str(layers))
            print("权重是："+str(self.weights))

    def fit(self, x, y, learning_rate=0.2, epochs=10000):
        x = np.atleast_2d(x)  # 创建并初始化要使用的变量。
        temp = np.ones([x.shape[0], x.shape[1]+1])
        temp[:, 0:-1] = x
        x = temp
        y = np.array(y)
#  以下开始至74行，进行BP神经网络的训练的核心部分
        for k in range(epochs):  # 循环epochs次
            i = np.random.randint(x.shape[0])  # 随机产生一个数，对应行号，即数据集编号
            a = [x[i]]  # 抽出这行的数据集

            # 迭代将输出数据更新在a的最后一行
            for l in range(len(self.weights)):
                a.append(self.activation(np.dot(a[l], self.weights[l])))

            # 减去最后更新的数据，得到误差
            error = y[i] - a[-1]
            deltas = [error * self.activation_deriv(a[-1])]

            # 求梯度
            for l in range(len(a) - 2, 0, -1):
                deltas.append(deltas[-1].dot(self.weights[l].T)*self.activation_deriv(a[l]))

            # 反向排序
            deltas.reverse()

            # 梯度下降法更新权值
            for i in range(len(self.weights)):
                layer = np.atleast_2d(a[i])
                delta = np.atleast_2d(deltas[i])
                self.weights[i] += learning_rate * layer.T.dot(delta)

#  这段是预测函数，其实就是将测试集的数据输入，然后正向走一遍训练好的网络最后再返回预测结果。
    def predict(self, x):
        x = np.array(x)
        temp = np.ones(x.shape[0] + 1)
        temp[0:-1] = x
        a = temp
        for l in range(0, len(self.weights)):
            a = self.activation(np.dot(a, self.weights[l]))
        return a

























