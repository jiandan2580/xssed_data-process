package suanfa.C45;

//<span style="font-size:14px;">
import java.util.ArrayList;  
import java.util.List;  
  
public class TreeNode {  //决策树的树节点对象实现
    public String attribute;  
    public List<String> attributeValue;  
    public List<TreeNode> child;  
    //for leaf node  
    public boolean isLeaf;  
    public String targetValue;  
      
    TreeNode() {  
        attributeValue = new ArrayList<String>();  
        child = new ArrayList<TreeNode>();  
    }  
      
    public String getAttribute() {  
        return attribute;  
    }  
      
    public void setAttribute(String attribute) {  
        this.attribute = attribute;  
    }  
      
    public List<String> getAttributeValue() {  
        return attributeValue;  
    }  
      
    public void setAttributeValue(List<String> attributeValue) {  
        this.attributeValue = attributeValue;  
    }  
      
    public void addAttributeValue(String attributeValue) {  
        this.attributeValue.add(attributeValue);  
    }  
      
    public List<TreeNode> getChild() {  
        return child;  
    }  
      
    public void setChild(List<TreeNode> child) {  
        this.child = child;  
    }  
      
    public void addChild(TreeNode child) {  
        this.child.add(child);  
    }  
      
    public boolean isLeaf() {  
        return isLeaf;  
    }  
      
    public void setLeaf(boolean isLeaf) {  
        this.isLeaf = isLeaf;  
    }  
      
    public String getTargetValue() {  
        return targetValue;  
    }  
      
    public void setTargetValue(String targetValue) {  
        this.targetValue = targetValue;  
    }  
      
    public void print(String depth) {  
        if(!this.isLeaf){  
            System.out.println(depth + this.attribute);  
            depth += "\t";  
            for(int i = 0; i < this.attributeValue.size(); i++) {  
                System.out.println(depth + "---(" + this.attributeValue.get(i) + ")---" );  
                this.child.get(i).print(depth + "\t");  
            }  
        } else {  
            System.out.println(depth + "[" + this.targetValue + "]");  
        }  
          
          
    }  
      
}//</span>  