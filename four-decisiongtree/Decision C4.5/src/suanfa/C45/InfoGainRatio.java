package suanfa.C45;

//<span style="font-size:14px;">
import java.util.ArrayList;  
import java.util.Iterator;  
import java.util.List;  
import java.util.Map;  
import java.util.Map.Entry;  
import java.util.Set;  
  
public class InfoGainRatio {  //计算信息增益率
  
    /** 
     * 获取某个属性的熵 
     *      = -∑ p(xi)log(2,p(xi)) 
     * @param list 
     * @return 
     */  
    @SuppressWarnings("rawtypes")  
    public double getEntropy(List<String> list) {  
        //概率统计  
        Map<String, Double> probability = DataSetUtil.getProbability(list);  
          
        //熵计算  
        double entropy = 0;  
        Set set = probability.entrySet();  
        Iterator iterator = set.iterator();  
        while(iterator.hasNext()) {  
            Map.Entry entry = (Entry) iterator.next();  
            double prob = (Double) entry.getValue();  
            entropy -= prob * (Math.log(prob) / Math.log(2));  
        }  
          
        return entropy;  
    }  
      
    /** 
     * 获取某个属性的信息增益 = Entropy(U) − ∑(|Di|/|D|)Entropy(Di) 
     * <br/> 离散属性 
     * @param attrId 
     * @param dataset 
     * @return 
     */  
    @SuppressWarnings("rawtypes")  
    public double getGain(int attrId, List<ArrayList<String>> dataset) {  
        List<String> targetList = DataSetUtil.getTarget(dataset);  
        List<String> attrValueList = DataSetUtil.getAttributeValue(attrId, dataset);  
          
        double totalEntropy = getEntropy(targetList);  
          
        Map<String, Double> probability = DataSetUtil.getProbability(attrValueList);  
          
        double subEntropy = 0;  
          
        Set set = probability.entrySet();  
        Iterator iterator = set.iterator();  
        while(iterator.hasNext()) {  
            Map.Entry entry = (Entry) iterator.next();  
            double prob =  (Double) entry.getValue();  
            List<String> subTargetList = DataSetUtil.getTargetByAttribute((String) entry.getKey(), attrValueList, targetList);  
            double entropy = getEntropy(subTargetList);  
              
            subEntropy += prob * entropy;  
        }  
          
        return totalEntropy - subEntropy;  
    }  
      
    /** 
     * 获取某个属性的信息增益率 = Gain(A) / SplitInfo(A) 
     * <br/> 离散属性 
     * @param attrId 
     * @param dataset 
     * @return 
     */  
    public double getGainRatio(int attrId, List<ArrayList<String>> dataset) {  
          
        List<String> attrValueList = DataSetUtil.getAttributeValue(attrId, dataset);  
        double gain = getGain(attrId, dataset);  
        double splitInfo = getEntropy(attrValueList);  
          
        return splitInfo == 0 ? 0 : gain/splitInfo;  
    }  
      
  
}//</span>  