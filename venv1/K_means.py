#！/usr/bin/env python
#-*- coding:utf-8 -*-
'''https://blog.csdn.net/tianse12/article/details/68061271   参考的该链接的聚类算法'''
'''假设论文的数据还没有经过标记，意思就是XSS攻击脚本恶意与否还不知道'''
import numpy as np
# 数据集需要加一列
# 参数有  1、数据集dataSet  2、分为几类(例子中是三类，论文需要的是两类)centriods  3、迭代的次数iterations
def kmeans(X,k,maxIt):
    numPoints,numDim = X.shape    # 返回行列的维度
    dataSet = np.zeros((numPoints,numDim+1))   # 增加一列作为分类标记
    dataSet[:,:-1] = X   #所有行，除了最后一列
    centroids = dataSet[np.random.randint(numPoints,size = k),:]   # 随机选取k行，包含所有列
    centroids[:,-1] = range(1,k+1)     # 给中心点分类进行初始化
    iterations = 0
    oldCentroids = None

    while not shouldStop(oldCentroids,centroids,iterations,maxIt):
        print("iteration:\n",iterations)
        print("dataSet:\n",dataSet)
        print("centroids:\n",centroids)

        oldCentroids = np.copy(centroids)    #不能直接用等号，不然会指向同一个变量
        iterations += 1

        updateLabels(dataSet,centroids)  # 根据数据集以及中心点对数据集的点进行归类
        centroids = getCentroids(dataSet,k)  # 更新中心点
    return dataSet

# 实现函数循环结束的判断
# 当循环次数达到最大值，或者中心点不变化就停止
def shouldStop(oldCentroids,centroids,iterations,maxIt):
    if iterations > maxIt:
        return True
    return np.array_equal(oldCentroids,centroids)
# 根据数据集以及中心点对数据就的点进行归类
def updateLabels(dataSet,centroids):
    numPoints,numDim = dataSet.shape      # 返回行(点数)，列
    for i in range(0,numPoints):
        dataSet[i,-1] = getLabelFromCLoseCentroid(dataSet[i,:-1],centroids)  # 对每一行最后一列进行归类

# 对比一行到每个中心点的距离，返回距离最短的中心点的label
def getLabelFromCLoseCentroid(dataSetRow,centroids):
    label = centroids[0,-1]  # 初始化label为中心点第一点的label
    minDist = np.linalg.norm(dataSetRow - centroids[0,:-1])   # 初始化最小值为当前行到中心点第一点的距离值。np.linalg.norm计算两个向量的距离
    for i in range(1,centroids.shape[0]):     # 对中心点的每个点开始循环
        dist = np.linalg.norm(dataSetRow - centroids[i,:-1])
        if dist < minDist:
            minDist = dist
            label = centroids[i,-1]
    print("minDist",minDist)
    return label

# 更新中心点
# 参数有 1、数据集(包含标签)   2、k个分类

def getCentroids(dataSet,k):
    result = np.zeros((k,dataSet.shape[1]))    #初始化新的中心点矩阵
    for i in range(1,k+1):
        oneCluster = dataSet[dataSet[:,-1] == i,:-1] # 找出最后一列类别为i的行集，即求一个类别里面的所有点
        result[i-1,:-1] = np.mean(oneCluster,axis=0)  # axis=0 对行求均值，并赋值
        result[i-1,-1] = i
    return result
x1 = np.array([1,1])
x2 = np.array([2,1])
x3 = np.array([4,3])
x4 = np.array([5,4])
testX = np.vstack((x1,x2,x3,x4))#将点排列成矩阵
print(testX)
result =  kmeans(testX,2,10)
print("final result:")
print(result)
