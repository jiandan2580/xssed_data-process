package suanfa.C45;

//<span style="font-size:14px;">
import java.util.ArrayList;  
import java.util.Arrays;  
import java.util.List;  


public class DecisionTree {  //实现决策树的主要逻辑
      
    InfoGainRatio infoGainRatio = new InfoGainRatio();  
      
    public TreeNode createDecisionTree(List<String> attribute, List<ArrayList<String>> dataset) {  
        TreeNode tree = new TreeNode();  
          
        //check if it is pure  
        if(DataSetUtil.isPure(DataSetUtil.getTarget(dataset))) {  
            tree.setLeaf(true);  
            tree.setTargetValue(DataSetUtil.getTarget(dataset).get(0));  
            return tree;  
        }  
        //choose the best attribute  
        int bestAttr = getBestAttribute(attribute, dataset);  
        //create a decision tree  
        tree.setAttribute(attribute.get(bestAttr));  
        tree.setLeaf(false);  
        List<String> attrValueList = DataSetUtil.getAttributeValueOfUnique(bestAttr, dataset);      
        List<String> subAttribute = new ArrayList<String>();  
        subAttribute.addAll(attribute);  
        subAttribute.remove(bestAttr);  
        for(String attrValue : attrValueList) {  
            //更新数据集dataset  
            List<ArrayList<String>> subDataSet = DataSetUtil.getSubDataSetByAttribute(dataset, bestAttr, attrValue);  
            //递归构建子树  
            TreeNode childTree = createDecisionTree(subAttribute, subDataSet);  
            tree.addAttributeValue(attrValue);  
            tree.addChild(childTree);  
        }  
  
        return tree;  
    }  
      
    /** 
     * 选出最优属性 
     * @param attribute 
     * @param dataset 
     * @return 
     */  
    public int getBestAttribute(List<String> attribute, List<ArrayList<String>> dataset) {  
        //calculate the gainRatio of each attribute, choose the max  
        int bestAttr = 0;  
        double maxGainRatio = 0;  
          
        for(int i = 0; i < attribute.size(); i++) {  
            double thisGainRatio = infoGainRatio.getGainRatio(i, dataset);  
            if(thisGainRatio > maxGainRatio) {  
                maxGainRatio = thisGainRatio;  
                bestAttr = i;  
            }  
        }  
          
        System.out.println("The best attribute is \"" + attribute.get(bestAttr) + "\"");  
        return bestAttr;  
    }  
  
      
    public static void main(String args[]) {  
    	 //eg 1  
        String attr = "num fun gett sett acti conclution";  
        String[] set = new String[17];  
        set[0] = "log function null null Error malicious";  
        set[1] = "random function Date null null malicious";  
        set[2] = "null function Date null null malicious";  
        set[3] = "null function null null null malicious";  
        set[4] = "null function null null apply malicious";  
        set[5] = "random function Date null apply malicious";  
        set[6] = "random function null null null malicious";  
        set[7] = "null function Date null RegExp malicious";  
        set[8] = "null function null null hasOwnProperty benign";  
        set[9] = "null function null null error benign";  
        set[10] = "math parseInt null null call benign";  
        set[11] = "random parseInt Date setdate call benign";  
        set[12]="random parse getTime null RegExp benign";
        set[13]="random function getTime setTime RegExp benign";
        set[14]="null function null null null benign";
        set[15]="null parseInt getTime null Object benign";
        set[16]="math parseInt null null RegExp benign";
  
        List<ArrayList<String>> dataset = new ArrayList<ArrayList<String>>();  
        List<String> attribute = Arrays.asList(attr.split(" "));  
        for(int i = 0; i < set.length; i++) {  
            String[] s = set[i].split(" ");  
            ArrayList<String> list = new ArrayList<String>();  
            for(int j = 0; j < s.length; j++) {  
                list.add(s[j]);  
            }  
            dataset.add(list);  
        }  
          
        DecisionTree dt = new DecisionTree();  
        TreeNode tree = dt.createDecisionTree(attribute, dataset);  
        tree.print("");  
    }  
  
}//</span>  
