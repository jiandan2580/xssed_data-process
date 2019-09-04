package example.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class Test {
	// 特征列表
	public static List<String> featureList = new ArrayList<String>();
	// 特征值列表
	public static List<List<String>> featureValueTableList = new ArrayList<List<String>>();
	// 得到全局数据
	public static Map<Integer, List<String>> tableMap = new HashMap<Integer, List<String>>();


	/**
	 * 输入测试数据得到决策树的预测结果
	 *
	 * @param decisionTree
	 *            决策树
	 * @param featureList
	 *            特征列表
	 * @param testDataList
	 *            测试数据
	 * @return
	 */
	private static String getDTAnswer(Node decisionTree,
			List<String> featureList, List<String> testDataList) {
		if (featureList.size() - 1 != testDataList.size()) {
			System.out.println("输入数据不完整");
			return "ERROR";
		}
		while (decisionTree != null) {
			// 如果孩子节点为空,则返回此节点答案.
			if (decisionTree.childrenNodeList == null
					|| decisionTree.childrenNodeList.size() <= 0) {
				return decisionTree.featureName;
			}
			// 孩子节点不为空,则判断特征值找到子节点
			for (int i = 0; i < featureList.size() - 1; i++) {
				// 找到当前特征下标
				if (featureList.get(i).equals(decisionTree.featureName)) {
					// 得到测试数据特征值
					String featureValue = testDataList.get(i);
					// 在子节点中找到含有此特征值的节点
					Node childNode = null;
					for (Node cn : decisionTree.childrenNodeList) {
						if (cn.lastFeatureValue.equals(featureValue)) {
							childNode = cn;
							break;
						}
					}
					// 如果没有找到此节点,则说明训练集中没有到这个节点的特征值
					if (childNode == null) {
						System.out.println("没有找到此特征值的数据");
						return "ERROR";
					}

					decisionTree = childNode;
					break;
				}
			}
		}
		return "ERROR";
	}


	/**
	 * 创建决策树
	 *
	 * @param dataSetList
	 *            数据集
	 * @param featureIndexList
	 *            可用的特征列表
	 * @param lastFeatureValue
	 *            到达此节点的上一个特征值
	 * @return
	 */
	private static Node createDecisionTree(List<Integer> dataSetList,
			List<Integer> featureIndexList, String lastFeatureValue) {
		// 如果只有一个值的话,则直接返回叶子节点
		int valueIndex = featureIndexList.get(featureIndexList.size() - 1);
		// 选择第一个值
		String firstValue = tableMap.get(dataSetList.get(0)).get(valueIndex);
		int firstValueNum = 0;
		for (Integer id : dataSetList)// 遍历dataSetList
		{
			if (firstValue.equals(tableMap.get(id).get(valueIndex))) {
				firstValueNum++;
			}
		}
		if (firstValueNum == dataSetList.size()) {
			Node node = new Node();
			node.lastFeatureValue = lastFeatureValue;
			node.featureName = firstValue;
			node.childrenNodeList = null;
			return node;
		}
		// 遍历完所有特征时特征值还没有完全相同,返回多数表决的结果
		if (featureIndexList.size() == 1) {
			Node node = new Node();
			node.lastFeatureValue = lastFeatureValue;
			node.featureName = majorityVote(dataSetList);
			node.childrenNodeList = null;
			return node;
		}
		// 获得信息增益最大的特征
		int bestFeatureIndex = chooseBsetFeatureToSplit(dataSetList,
				featureIndexList);
		// 得到此特征在全局的下标
		int realFeatureIndex = featureIndexList.get(bestFeatureIndex);
		String bestFeatureName = featureList.get(realFeatureIndex);
		// 构造决策树
		Node node = new Node();
		node.lastFeatureValue = lastFeatureValue;
		node.featureName = bestFeatureName;
		// 得到所有特征值的集合
		List<String> featureValueList = featureValueTableList
				.get(realFeatureIndex);
		// 删除此特征
		featureIndexList.remove(bestFeatureIndex);

		// 遍历特征所有值,划分数据集,然后递归得到子节点
		for (String fv : featureValueList) {
			// 得到子数据集
			List<Integer> subDataSetList = splitDataSet(dataSetList,
					realFeatureIndex, fv);
			// 如果子数据集为空，则使用多数表决给一个答案。
			if (subDataSetList == null || subDataSetList.size() <= 0) {
				Node childNode = new Node();
				childNode.lastFeatureValue = fv;
				childNode.featureName = majorityVote(dataSetList);
				childNode.childrenNodeList = null;
				node.childrenNodeList.add(childNode);
				break;
			}
			// 添加子节点
			Node childNode = createDecisionTree(subDataSetList,
					featureIndexList, fv);
			node.childrenNodeList.add(childNode);
		}
		return node;
	}




	/**
	 * 对一个数据集进行划分
	 *
	 * @param dataSetList
	 *            待划分的数据集
	 * @param FeatureIndex
	 *            第几个特征(特征下标,从0开始)
	 * @param value
	 *            得到某个特征值的数据集
	 * @return
	 */
	private static List<Integer> splitDataSet(List<Integer> dataSetList,
			int FeatureIndex, String value) {
		List<Integer> resultList = new ArrayList<Integer>();
		for (Integer id : dataSetList) {
			if (tableMap.get(id).get(FeatureIndex).equals(value)) {
				resultList.add(id);
			}
		}
		return resultList;
	}




	/**
	 * 多数表决得到出现次数最多的那个值
	 *
	 * @param dataSetList
	 * @return
	 */
	private static String majorityVote(List<Integer> dataSetList) {
		// 得到结果
		int resultIndex = tableMap.get(dataSetList.get(0)).size() - 1;
		Map<String, Integer> valueMap = new HashMap<String, Integer>();
		for (Integer id : dataSetList) {
			String value = tableMap.get(id).get(resultIndex);
			Integer num = valueMap.get(value);
			if (num == null || num == 0) {
				num = 0;
			}
			valueMap.put(value, num + 1);
		}

		int maxNum = 0;
		String value = " ";

		for (Map.Entry<String, Integer> entry : valueMap.entrySet()) {
			if (entry.getValue() > maxNum) {
				maxNum = entry.getValue();
				value = entry.getKey();
			}
		}

		return value;
	}


	/**
	 * 在指定的几个特征中选择一个最佳特征(信息增益最大)用于划分数据集
	 *
	 * @param dataSetList
	 * @return 返回最佳特征的下标
	 */
	private static int chooseBsetFeatureToSplit(List<Integer> dataSetList,
			List<Integer> featureIndexList) {
		double baseEntropy = calculateEntropy(dataSetList);
		double bestInformationGain = 0;
		int bestFeature = -1;

		// 循环遍历所有特征
		for (int temp = 0; temp < featureIndexList.size() - 1; temp++) {
			int i = featureIndexList.get(temp);
			// 得到特征集合
			List<String> featureValueList = new ArrayList<String>();
			for (Integer id : dataSetList) {
				String value = tableMap.get(id).get(i);
				featureValueList.add(value);
			}
			Set<String> featureValueSet = new HashSet<String>();
			featureValueSet.addAll(featureValueList);

			// 得到此分类下的熵
			double newEntropy = 0;
			for (String featureValue : featureValueSet) {
				List<Integer> subDataSetList = splitDataSet(dataSetList, i,
						featureValue);
				double probability = subDataSetList.size() * 1.0 / dataSetList.size();
				newEntropy += probability * calculateEntropy(subDataSetList);
			}
			// 得到信息增益
			double informationGain = baseEntropy - newEntropy;
			// 得到信息增益最大的特征下标
			if (informationGain > bestInformationGain) {
				bestInformationGain = informationGain;
				bestFeature = temp;
			}
		}
		return bestFeature;
	}



	/**
	 * 计算    在不使用任何特征的情况下计算的熵
	 *
	 * @param dataSetList
	 * @return
	 */
	private static double calculateEntropy(List<Integer> dataSetList)
	{
		if (dataSetList == null || dataSetList.size() <= 0) {
			return 0;
		}
		// 得到良性或恶意的结果
		int resultIndex = tableMap.get(dataSetList.get(0)).size() - 1;
		Map<String, Integer> valueMap = new HashMap<String, Integer>();
		for (Integer id : dataSetList) {
			String value = tableMap.get(id).get(resultIndex);
			Integer num = valueMap.get(value);
			if (num == null || num == 0) {
				num = 0;
			}
			valueMap.put(value, num + 1);
		}
		double entropy = 0;
		for (Map.Entry<String, Integer> entry : valueMap.entrySet()) {
			double prob = entry.getValue() * 1.0 / dataSetList.size();
			entropy -= prob * Math.log10(prob) / Math.log10(2);
		}
		return entropy;
	}



	/**
	 * 初始化数据
	 *
	 * @param file
	 * @throws Exception
	 */
	private static void readOriginalData(File file) {
		int index = 0;
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line;
			while ((line = br.readLine()) != null)// 数据不为空
			{
				// 得到特征名称
				if (line.startsWith("@feature")) {
					line = br.readLine();
					String[] row = line.split(",");
					for (String s : row) {
						featureList.add(s.trim());// trim()去除字符串左右两边的空格，中间的不去掉
						}
				} else if (line.startsWith("@data")) {
					while ((line = br.readLine()) != null) {
						if (line.equals("")) {//去空格
							continue;
						}
						String[] row = line.split(",");
						if (row.length != featureList.size()) {//row里面放的是数据（即属性）；featureList里面放的是特征名称
							throw new Exception("列表数据和特征数目不一致");
						}

						List<String> tempList = new ArrayList<String>();
						for (String s : row) {
							if (s.trim().equals("")) {//去空格！然后和""相比，相同就是true;不相同就抛出异常
								throw new Exception("列表数据不能为空");
							}
							tempList.add(s.trim());
						}
						tableMap.put(index++, tempList);
					}
					// 遍历tableMap得到属性值列表
					Map<Integer, Set<String>> valueSetMap = new HashMap<Integer, Set<String>>();
					for (int i = 0; i < featureList.size(); i++) {
						valueSetMap.put(i, new HashSet<String>());
					}
					for (Map.Entry<Integer, List<String>> entry : tableMap
							.entrySet()) {
						List<String> dataList = entry.getValue();
						for (int i = 0; i < dataList.size(); i++) {
							valueSetMap.get(i).add(dataList.get(i));
						}
					}
					for (Map.Entry<Integer, Set<String>> entry : valueSetMap
							.entrySet()) {
						List<String> valueList = new ArrayList<String>();
						for (String s : entry.getValue()) {
							valueList.add(s);
						}
						featureValueTableList.add(valueList);
					}
				} else {
					continue;
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// 把结果写入xml文件
	public static void writeToXML(Node node, String filename){
		// 生成xml
		try {
			JAXBContext context = JAXBContext.newInstance(Node.class);
			Marshaller marshaller = context.createMarshaller();

			File file = new File(filename);
			if (file.exists() == false) {
				if (file.getParent() == null) {
					file.getParentFile().mkdirs();// 创建一个目录
				}
				file.createNewFile();
			}

			marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8"); // 设置编码字符集
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);// 格式化XML输出，有分行和缩进

			marshaller.marshal(node, System.out);// 打印到控制台

			FileOutputStream fos = new FileOutputStream(file);
			marshaller.marshal(node, fos);// Marshaller 类负责管理将 Java 内容树序列化回 XML
											// 数据的过程
			fos.flush();
			fos.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	public static void main(String[] args){
		// 初始化数据
		readOriginalData(new File("src/data.txt"));
		for (String f : featureList) {
			System.out.print(f + ",");
		}
		System.out.println("\n");// 输出的特征和数据间空一行
		// 获得数据集的列表
		// 即，输出训练集 data.txt 中的数据信息
		List<Integer> tempDataList = new ArrayList<Integer>();

		for (Map.Entry<Integer, List<String>> entry : tableMap.entrySet())// 遍历map中的键值对
		{
			System.out.print(entry.getKey() + ","); // 输出键
			for (String s : entry.getValue()) {
				System.out.print(s + ","); // 输出值
			}
			System.out.println();
			tempDataList.add(entry.getKey());
		}

		// 得到特征的列表
		// 为构造决策树做准备
		List<Integer> featureIndexList = new ArrayList<Integer>();
		for (int i = 0; i < featureList.size(); i++) {
			featureIndexList.add(i);
		}

		// 构造决策树
		Node decisionTree = createDecisionTree(tempDataList, featureIndexList,
				null);

		// 输出到文件中
				String outputFilePath = "E:/id3-Web.xml";
				writeToXML(decisionTree, outputFilePath);

		System.out.println("判断结果:"
				+ getDTAnswer(decisionTree, featureList,
						Arrays.asList("number,parseInt,getTime,null,Object".split(","))));
		
		
		
		
		
	}
	
}
