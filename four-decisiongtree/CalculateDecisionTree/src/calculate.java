import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

public class calculate {

	// 特征列表
	public static List<String> featureList = new ArrayList<String>();
	// 特征值列表
	public static List<List<String>> featureValueTableList = new ArrayList<List<String>>();
	// 得到全局数据
	public static Map<Integer, List<String>> tableMap = new HashMap<Integer, List<String>>();
    //无缺失值的全局数据
	public static Map<Integer, List<String>> tableMap1 = new HashMap<Integer, List<String>>();
	
	
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
			return "输入数据不完整";
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
					// 如果没有找到此节点,则说明训练集中没有找到这个节点的特征值
					if (childNode == null) {
						System.out.println("没有找到此特征值的数据");
						return "没有找到此特征值的数据";
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
		int valueIndex = featureIndexList.get(featureIndexList.size() - 1);// featureIndexList&valueIndex都表示5个特征数目，除去最后的结果
		// 选择第一个值,firstValue的值取的是data.txt中最右边的“结果”那一列的值
		String firstValue = tableMap.get(dataSetList.get(0)).get(valueIndex);// 例如：dataSetList=16个网页数目，共16行
		// System.out.print(firstValue);//firstValue的值取的是data.txt中最右边的“结果”那一列的值
		int firstValueNum = 0;
		for (Integer id : dataSetList) {     //dataSetList 是数组名，由id负责访问该数组
			if (firstValue.equals(tableMap.get(id).get(valueIndex))) {
				firstValueNum++;
			}
			// System.out.print("AA"+tableMap.get(id).get(valueIndex));
		}
		// System.out.print(firstValueNum);
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
		// !!!!!!!!!!!!!!!!!!!开始改动得出信息增益的代码！！！！！！！！！实现增益结果的输出!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		// 获得信息增益最大的特征
		 int bestFeatureIndex =
		 chooseBestFeatureToSplit(dataSetList,featureIndexList);
		// 获得信息增益比最大的特征
		//int bestFeatureIndexRate = chooseBestFeatureToSplitRate(dataSetList,featureIndexList);
		// 得到此特征在全局的下标
		int realFeatureIndex = featureIndexList.get(bestFeatureIndex);
		//int realFeatureIndexRate = featureIndexList.get(bestFeatureIndexRate);
		 String bestFeatureName = featureList.get(realFeatureIndex);
		//String bestFeatureNameRate = featureList.get(realFeatureIndexRate);
		// 构造决策树
		Node node = new Node();
		node.lastFeatureValue = lastFeatureValue;
		node.featureName = bestFeatureName; //以信息增益为依据 构造决策树
		//node.featureName = bestFeatureNameRate; // 以信息增益比为依据 构造决策树
		// 得到所有特征值的集合
		 List<String> featureValueList =
		 featureValueTableList.get(realFeatureIndex);//如果此行出错，则浏览网页https://blog.csdn.net/weixin_39274753/article/details/79709830
		//List<String> featureValueListRate = featureValueTableList
		//		.get(realFeatureIndexRate);
		// 删除此特征
		 featureIndexList.remove(bestFeatureIndex);
		//featureIndexList.remove(realFeatureIndexRate);
		// 遍历特征所有值,划分数据集,然后递归得到子节点

		 for(String fv : featureValueList){//按照信息增益来算的 得到递归子节点 // 得到子数据集
		 List<Integer> subDataSetList =
		 splitDataSet(dataSetList,realFeatureIndex,fv); //如果子数据集为空，则使用多数表决给一个答案。
			 if(subDataSetList ==null || subDataSetList.size()<=0)
			 {
			 	Node childNode = new Node();
		 		childNode.lastFeatureValue = fv;
		 		childNode.featureName = majorityVote(dataSetList);
		 		childNode.childrenNodeList = null;
		 		node.childrenNodeList.add(childNode);
		 		break;
			 } // 添加子节点 Node
		 Node childNode = createDecisionTree(subDataSetList,featureIndexList,fv);
		 node.childrenNodeList.add(childNode);

//		for (String fv : featureValueListRate) {// 按照信息增益比来算的 得到递归子节点
//			List<Integer> subDataSetList = splitDataSet(dataSetList,
//					realFeatureIndexRate, fv);
//			if (subDataSetList == null || subDataSetList.size() <= 0) {
//				Node childNode = new Node();
//				childNode.lastFeatureValue = fv;
//				childNode.featureName = majorityVote(dataSetList);
//				childNode.childrenNodeList = null;
//				node.childrenNodeList.add(childNode);
//				break;
//			}
//			Node childNode = createDecisionTree(subDataSetList,
//					featureIndexList, fv);
//			node.childrenNodeList.add(childNode);
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
		// System.out.print("AA"+dataSetList);
		for (Integer id : dataSetList) {
			if (tableMap.get(id).get(FeatureIndex).equals(value)) {
				resultList.add(id);// resultList=一列中相同属性的编号（编号从零开始）
			}
			// System.out.print("AA"+tableMap.get(id).get(FeatureIndex));
			// System.out.print("BB"+value);
		}
		return resultList;
	}

	/**
	 * 多数表决得到出现次数最多的那个值
	 * @param dataSetList
	 * @return
	 */
	private static String majorityVote(List<Integer> dataSetList) {
		// 得到结果
		int resultIndex = tableMap.get(dataSetList.get(0)).size() - 1;
		// System.out.print(resultIndex);
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
				// System.out.print("AA"+maxNum);
				// System.out.print("BB"+value);
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
	private static int chooseBestFeatureToSplit(List<Integer> dataSetList,
			List<Integer> featureIndexList) {    // 此代码中用到了这个方法，没有用到下面那个最大信息增益比的代码
		double baseEntropy = calculateEntropy(dataSetList);
		double bestInformationGain = 0;
		// double bestGainRate=0;
		// double GainRate=0;//特征的信息增益比设为0
		int bestFeature = -1;
		// 循环遍历所有特征
		System.out.println();
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
				double probability = subDataSetList.size() * 1.0
						/ dataSetList.size();
				newEntropy += probability * calculateEntropy(subDataSetList);
				// System.out.println("000"+subDataSetList);//输出的是每一个属性在该特征下的编号
				// System.out.println(probability);
				// System.out.println(newEntropy);
			}

			// System.out.println();

			// for (String f : featureList) { // 循环输出特征名称
			// System.out.print("特征是：" + f);
			// featureList.remove(f);// 输出一个之后删除，下一次继续读下一个
			// break; // 读一次就要跳出循环
			// }
			for (int f = 0; f < featureList.size(); f++) {
				System.out.print("特征是：" + featureList.get(i));
				break;
			}
			// 得到信息增益
			double informationGain = baseEntropy - newEntropy;
			// GainRate=informationGain/newEntropy;
			// System.out.print("，该特征的熵是：" + newEntropy + ",该特征的信息增益："+
			// informationGain+",该特征的信息增益比："+GainRate);
			System.out.print("，该特征的熵是：" + newEntropy + ",该特征的信息增益："
					+ informationGain);
			System.out.println();

			// 得到信息增益最大的特征下标
			if (informationGain > bestInformationGain) {
				bestInformationGain = informationGain;
				bestFeature = temp;
			}else{break;}
			// 得到信息增益比最大的特征下标
			// if(GainRate>bestGainRate){
			// bestGainRate=GainRate;
			// bestFeature = temp;
			// }
		}
		// System.out.println("最大信息增益的值：" + bestInformationGain);
		// System.out.println("最大信息增益比：" + GainRate);
		return bestFeature;
	}

	/**
	 * 在指定的几个特征中选择一个最佳特征(信息增益比最大)用于划分数据集
	 * 
	 * @param dataSetList
	 * @return 返回最佳特征的下标
	 */
	private static int chooseBestFeatureToSplitRate(List<Integer> dataSetList,
			List<Integer> featureIndexList) {
		double baseEntropy = calculateEntropy(dataSetList);
		// double bestInformationGain = 0;
		double bestGainRate = 0;
		double GainRate = 0;// 特征的信息增益比设为0
		int count = 0;// 记录每一轮里面熵为0的个数，若某一轮里面所有 的熵都为0，则停止划分
		int temp = 0;
		int bestFeature = -1;
		// 循环遍历所有特征
		System.out.println();
		System.out.print("此轮特征总的熵是：" + baseEntropy + "         ");
		int a = featureIndexList.size() - 1;// 每一轮的特征数目
		System.out.println("这一轮特征总数：" + a);
		// for (int temp = 0; temp < featureIndexList.size() - 1; temp++) {
		while (temp < featureIndexList.size() - 1) {
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
				double probability = subDataSetList.size() * 1.0
						/ dataSetList.size();
				newEntropy += probability * calculateEntropy(subDataSetList);
				// System.out.println("000"+subDataSetList);//输出的是每一个属性在该特征下的编号
				// System.out.println(probability);
				// System.out.println(newEntropy);
			}
			// for (String f : featureList) { // 循环输出特征名称
			// System.out.print("特征是：" + f);
			// featureList.remove(f);// 输出一个之后删除，下一次继续读下一个
			// break; // 读一次就要跳出循环
			// }
			for (int f = 0; f < featureList.size(); f++) {
				System.out.print("特征是：" + featureList.get(i));
				break;
			}
			// 得到信息增益

			double informationGain = baseEntropy - newEntropy;
			GainRate = informationGain / newEntropy;
			if (newEntropy == 0) {
				count++;
				GainRate = 0.000000000001;
			}
			System.out.print("，该特征的熵是：" + newEntropy + ",该特征的信息增益："
					+ informationGain + ",该特征的信息增益比：" + GainRate);
			// System.out.print("，该特征的熵是：" + newEntropy + ",该特征的信息增益："+
			// informationGain);
			System.out.println();

			// 得到信息增益最大的特征下标
			// if (informationGain > bestInformationGain) {
			// bestInformationGain = informationGain;
			// bestFeature = temp;
			// }

			// 得到信息增益比最大的特征下标
			if (GainRate > bestGainRate/* &Double.isInfinite(GainRate)==true */) {
				bestGainRate = GainRate;
				bestFeature = temp;
			}

			if (count == a) {
				// System.out.print("@@@@@@@@@@@@@@@@"+count+"  "+a);
				temp = featureIndexList.size() + 1;// ??????这句话没什么用？？？？？？？？？？？？？？？
				// return 0;
			} else {
				temp++;
			}

		}
		// System.out.println("最大信息增益的值：" + bestInformationGain);
		System.out.println("最大信息增益比：" + bestGainRate);
		return bestFeature;
	}

	/**
	 * 计算熵
	 * 
	 * @param dataSetList
	 * @return
	 */
	private static double calculateEntropy(List<Integer> dataSetList) {
		if (dataSetList == null || dataSetList.size() <= 0) {
			return 0;
		}
		// 得到良性或恶意的结果
		int resultIndex = tableMap.get(dataSetList.get(0)).size() - 1;// 特征 数目   按顺序标号
		Map<String, Integer> valueMap = new HashMap<String, Integer>();
		for (Integer id : dataSetList) {
			String value = tableMap.get(id).get(resultIndex); // value代表恶意或者良性
			// System.out.print("A"+value+" ");//value 通过tableMap的帮助，统计了所有17个的良恶个数，计算用得到，
			                               //所以只能再加一个tableMap1，里面放着无“缺失值”的属性数据,用到计算里去！！试试
			Integer num = valueMap.get(value);
			if (num == null || num == 0) {
				num = 0;
			}
			valueMap.put(value, num + 1); // 得到最后的结果，良性个数、恶意个数
		}
//可以看到恶意和良性的个数		 System.out.print("@@"+valueMap); //得到最后的结果，良性个数、恶意个数
		                                 //!BUT，这里有问题，还是把缺失值对应的恶意良性计算在内，需要改改改
		
		double entropy = 0;
		for (Map.Entry<String, Integer> entry : valueMap.entrySet()) {
			//if (!entry.getValue().equals("0")) {// 20180105
														// 突破口!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				// continue;//在if语句的条件下，执行continue，那就是把null的数据不计算进去了，得改公式了
			    // 计算熵
				double prob = entry.getValue() * 1.0 / dataSetList.size();
				entropy -= prob * Math.log10(prob) / Math.log10(2);// Java中没log2的函数，用此种办法换算
		}
		//}
		// System.out.print("?"+dataSetList.size()+"："+valueMap.entrySet());//20180104想办法删去null对应的良恶值，得改
		return entropy;
	}

	/**
	 * 初始化数据
	 * 
	 * @param file
	 * @throws IOException
	 * @throws Exception
	 */
	private static void readOriginalData(File file) throws IOException {
		int index = 0;
		int index1= 0;
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line;
			while ((line = br.readLine()) != null)// 数据不为空
			{
				// 得到特征名称
				if (line.startsWith("@feature")) {
					line = br.readLine();
					// System.out.print("BBBBB"+line);
					String[] row = line.split(",");
					for (String s : row) {
						featureList.add(s.trim());// trim()去除字符串左右两边的空格，中间的不去掉
					}
				}
				// 得到属性值
				else if (line.startsWith("@data")) {
					while ((line = br.readLine()) != null) {
						if (line.equals("")) {// 去空格
							continue;
						}
						String[] row = line.split(",");
						//for (int i = 0; i < row.length; i++) {
							// if(!row[i].equals("null")){//20180104增加的代码，想办法去掉
							// 缺失值,but还没起效
							// continue;
							// }
						//}
						// System.out.println("BBBBB"+line);//此处的line代表的是属性值
						// if (row.length != featureList.size()) {//
						// row里面放的是数据（即属性）；featureList里面放的是特征名称
						// throw new Exception("列表数据和特征数目不一致");
						// }

						List<String> tempList = new ArrayList<String>();
						List<String> tempList1 = new ArrayList<String>();
						// for(String s :row){
						for (int i = 0; i < row.length; i++) {
							// if (s.trim().equals("")) {//
							// 去空格！然后和""相比，相同就是true;不相同就抛出异常
							// throw new Exception("列表数据不能为空");
							// }
							if (row[i].equals("null")) {// 20180105:此处将文件中的null改成了0，但是0依然是个字符串
								row[i] = "0";//根据文件里的具体数据信息来改
							}
							tempList.add(row[i]);
						}
	
						for(int j=0;j<row.length;j++){//该循环为了得到tableMap1做准备
							while(row[j].equals("0")){
								j++;//如果数组里面遇到的那个值是缺失值，那么pass，继续看下一个
							}
							tempList1.add(row[j]);
						}
						tableMap.put(index++, tempList);
						tableMap1.put(index1++, tempList1);
						// System.out.println("?"+tableMap);//tableMap记录了每一行的属性值,包括null或者0zhi缺失值
					}
					

//!!!!!!!!!!!!!!!!!!!!!!!!!!!					// 遍历 tableMap   得到属性值列表
					Map<Integer, Set<String>> valueSetMap = new HashMap<Integer, Set<String>>();
					for (int i = 0; i < featureList.size(); i++) {
						valueSetMap.put(i, new HashSet<String>());
					}
					for (Map.Entry<Integer, List<String>> entry : tableMap
							.entrySet()) {// 使用Map.Entry类，可以在同一时间得到键和值
						List<String> dataList = entry.getValue();// dataList里放着所有的属性
						for (int i = 0; i < dataList.size(); i++) {// 每一行数据(属性)的个数:7个
							if (!dataList.get(i).equals("0")) {// 20180106使valueSetMap中只包含了关键属性
								valueSetMap.get(i).add(dataList.get(i));
							}
						}
					}
					// System.out.println("QQQ "+valueSetMap);//!!!!!valueSetMap中只包含了关键属性，已经把null或者0都去掉了!!!!
					for (Map.Entry<Integer, Set<String>> entry : valueSetMap
							.entrySet()) {
						List<String> valueList = new ArrayList<String>();
						for (String s : entry.getValue()) {
							// if(!s.equals("null"))//这句话不起效
							valueList.add(s);// s是每一列的属性值(不重复的)
							// System.out.println("AAA "+s);//因为第456句话，s中已经不会有null或者0这样的缺失值了！！！！
						}
						featureValueTableList.add(valueList);
						// System.out.println("AAA "+valueList);//每一列存在的属性值都识别并且列出来了
					}
					// System.out.println("BBB "+featureValueTableList);//所有属性值已经清理干净，每一个数组里只含有该列的单个属性
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
	public static void writeToXML(Node node, String filename) {
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

	
//!!!!!!!!!!!!!!!!主函数!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!主函数!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	public static void main(String[] args) throws IOException {
		// 初始化数据
		//readOriginalData(new File("src/data-rk.txt"));          //src文件夹中的test30.txt文件包含31长度的3054良数据&3054恶数据
		readOriginalData(new File("src/data.txt"));    //src文件夹中的test.txt文件包含129长度的3054良数据&3054恶数据
		System.out.print("特征是：");                           //src文件夹中的test1.txt文件包含91长度的3054良数据&3054恶数据
		for (String f : featureList) {
			System.out.print(f + ",");//输出特征
		}
		System.out.println("\n");// 输出的特征和数据间空一行

		// 获得数据集的列表
		// 即，输出训练集 data.txt 中的原始数据信息
		List<Integer> tempDataList = new ArrayList<Integer>();
		List<String> tempValueList = new ArrayList<String>();
		System.out.println("文件中的原始属性值：");
		for (Map.Entry<Integer, List<String>> entry : tableMap.entrySet())// 遍历map中的键值对
		{
			System.out.print(entry.getKey() + ","); // 输出键0~16
			for (String s : entry.getValue()) {//getValue()里有属性值
				System.out.print(s + ","); // 输出值
				tempValueList.add(s);
			}
			System.out.println();
			tempDataList.add(entry.getKey());
			//System.out.println("??"+tempValueList);
			//System.out.println("@@"+tempDataList);
		}
		System.out.println();

		//!!!!!!!!!!输出valueMap1 去掉缺失值之后的表格数据
		List<Integer> tempDataList1 = new ArrayList<Integer>();
		List<String> tempValueList1 = new ArrayList<String>();
		System.out.println("tableMap1中的属性值：");
		for (Map.Entry<Integer, List<String>> entry1 : tableMap1.entrySet())// 遍历map中的键值对
		{
			System.out.print(entry1.getKey() + ",");   // 输出键0~16
			for (String s : entry1.getValue()) {   //getValue()里有属性值
				System.out.print(s + ",");    // 输出值
				tempValueList1.add(s);
			}
			System.out.println();
			tempDataList1.add(entry1.getKey());
		}
		System.out.println();
		
		
		// 得到特征的列表
		// 为构造决策树做准备
		List<Integer> featureIndexList = new ArrayList<Integer>();
		for (int i = 0; i < featureList.size(); i++) {
			featureIndexList.add(i);
		}

		// 输出在不使用任何特征的情况下计算的熵
		double baseEntropy = calculateEntropy(tempDataList);
		System.out.println();
		System.out.print("在不使用任何特征的情况下计算的熵:" + baseEntropy);
		System.out.println();

		// 输出每个特征的熵的值和信息增益
		// int FeatureEntropy = chooseBestFeatureToSplit(tempDataList,
		// featureIndexList);
		// System.out.println(FeatureEntropy);

		// 疑似输出判断结果
		// String ma=majorityVote(tempDataList);
		// System.out.println(ma);

		// 构造决策树
		Node decisionTree = createDecisionTree(tempDataList, featureIndexList,
				null);
		// 输出到文件中
		String outputFilePath = "E:/id3-calculate.xml";
		//String outputFilePath = "E:/test-xssed-normal.xml";
		writeToXML(decisionTree, outputFilePath);
		
		//训练完毕之后，输入一组数据，进行预测
		//math,parseint,indexof,gettime,settime,regexp
		System.out.println("判断结果:"
				+ getDTAnswer(decisionTree, featureList,
					Arrays.asList("random,function,Date,nl,apply".split(","))));
//						Arrays.asList("ct_orig_url=,arena,>,<script>,alert(,0,),</script>,<iframe,src=,http://u,>,</iframe>,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0".split(","))));
//						Arrays.asList("_=,0,list=,sz0,sh0,sz0,sz0,sz0,sz0,sh0,sz0,sz0,sz0,rb0,i0,m0,c0,jd0,sr0,bu0,ru0,ag0,au0,hf_cad,hf_cl,hf_gc,hf_si,hf_s,hf_bo,hf_c,hf_w,hf_ahd,hf_oil,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0".split(","))));
//						Arrays.asList("searchconfiguration=,0bd0a0ab0d0b0cd0b0f0aebf0f0ade0f0b0a0a0b0b0e0f0b0b0b0e0c0fcd0a0e0ccbde0f0fe0e0f0ef0fffd0c0a0fa0c0cd0cb0a0ea0ccb0c0d0bc0d0dc0ead0b0d0a0e0f0a0d0b0f0c0c0bde0fb0c0ea0a0da0dd0d0c0c0a0cbaca0e0ec0d0e0bea0b0bc0c0ace0a0c0f0be0ecf0cf0a0cc0dd0d0d0b0c0afe0df0ff0ffdf0db0b0ceb0fc0a0da0ffb0b0de0fe0aed0beba0ce0d0dcc0b0ffca0cd0c,searchterm=,>,<script>,alert(,0ssi0_tr,),</script>,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0".split(","))));
// 						Arrays.asList("hid=,sgpy,windows,generic,device,id,v=,0.0.0.0,brand=,0,platform=,0,ifbak=,0,ifmobile=,0,ifauto=,0,type=,0,filename=,sgim_privilege.zip,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0".split(","))));
	}
//长度为129的xss语句的测试数据：ct_orig_url=,arena,>,<script>,alert(,0,),</script>,<iframe,src=,http://u,>,</iframe>,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
//长度为129的normal语句的测试数据：_=,0,list=,sz0,sh0,sz0,sz0,sz0,sz0,sh0,sz0,sz0,sz0,rb0,i0,m0,c0,jd0,sr0,bu0,ru0,ag0,au0,hf_cad,hf_cl,hf_gc,hf_si,hf_s,hf_bo,hf_c,hf_w,hf_ahd,hf_oil,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
//长度为90的xss语句的测试数据：searchconfiguration=,0bd0a0ab0d0b0cd0b0f0aebf0f0ade0f0b0a0a0b0b0e0f0b0b0b0e0c0fcd0a0e0ccbde0f0fe0e0f0ef0fffd0c0a0fa0c0cd0cb0a0ea0ccb0c0d0bc0d0dc0ead0b0d0a0e0f0a0d0b0f0c0c0bde0fb0c0ea0a0da0dd0d0c0c0a0cbaca0e0ec0d0e0bea0b0bc0c0ace0a0c0f0be0ecf0cf0a0cc0dd0d0d0b0c0afe0df0ff0ffdf0db0b0ceb0fc0a0da0ffb0b0de0fe0aed0beba0ce0d0dcc0b0ffca0cd0c,searchterm=,>,<script>,alert(,0ssi0_tr,),</script>,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
//长度为90的normal语句的测试数据：hid=,sgpy,windows,generic,device,id,v=,0.0.0.0,brand=,0,platform=,0,ifbak=,0,ifmobile=,0,ifauto=,0,type=,0,filename=,sgim_privilege.zip,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
//长度为30的xss语句的测试数据：option=,com_wdshop,view=,userinfo,ajax_json=,ajax_fill_city_state,format=,raw,zip=,>,</style>,</script>,<script>,alert(,document.cookie,),</script>,0,0,0,0,0,0,0,0,0,0,0,0,0
//长度为30的normal语句的测试数据：cgi=,cgi_farm_xiaotan_index,type=,0,time=,0,uin=,0,domain=,nc.qzone.qq.com,rate=,0,code=,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
}
