package com.xiang.viewanim.view.comm.TreeView;

import com.xiang.viewanim.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gordon on 2016/6/18.
 */
public class TreeHelper {

    /**
     * 将数据转为节点数据
     * @param datas 传入的数据
     * @param <T>
     * @return
     */
    public static <T> List<Node> convertDatasToNodes(List<T> datas) throws IllegalAccessException {
        List<Node> nodes = new ArrayList<>();
        Node node = null;
        for(T t : datas){
            int id = -1;
            int pid = -1;
            String label = null;
            // 通过反射获取到那些添加属性注解的属性值

            // 得到实体类的class
            Class clazz = t.getClass();
            // 得到该类中所有属性
            Field[] fields = clazz.getDeclaredFields();

            // 遍历所有的属性，当然如果你知道你想要的属性的名称可以使用clazz.getDeclaredField("属性名")来得到，这样提升效率
            // 但这里主要考虑到多个类都需要转换时，而且他们的属性名不一样的情况下该怎么拿到证明他们关系的属性值(在这些类的属性上加注解)
            for(Field field : fields){
                // 判断该属性是否加了TreeNodeId这个注解
                if(field.getAnnotation(TreeNodeId.class) != null){
                    //如果该属性设置了像private这样的权限，我们可以通过此方法让该属性变得无障碍访问，从而获取的它的值
                    field.setAccessible(true);
                    id = field.getInt(t);// 如果有异常，抛出异常
                }

                if(field.getAnnotation(TreeNodePid.class) != null){
                    field.setAccessible(true);
                    pid = field.getInt(t);
                }

                if (field.getAnnotation(TreeNodeLabel.class) != null){
                    field.setAccessible(true);
                    label = (String) field.get(t);
                }

            }
            node = new Node(id,pid,label);
            nodes.add(node);
        }

        // 为节点设置关联关系
        for(int i = 0;i < nodes.size();i++){
            Node n = nodes.get(i);// 遍历所有的node
            for(int j = i + 1;j < nodes.size();j++){// 遍历i+1以后的所有node
                Node m = nodes.get(j);
                if(m.getPid() == n.getId()){// 问n是否是m的父节点
                    n.getChildren().add(m);
                    m.setParent(n);
                }else if(n.getPid() == m.getId()){// 问m是否是n的父节点
                    m.getChildren().add(n);
                    n.setParent(m);
                }
            }
        }

        // 为每一个节点设置图标
        for(Node n : nodes){
            setNodeIcon(n);
        }

        return nodes;
    }

    /**为Node设置图标*/
    private static void setNodeIcon(Node n) {
        if(n.getChildren().size() > 0 && n.isExpand()){// 有孩子且是展开的
            n.setIcon(R.drawable.tree_ex);
        }else if(n.getChildren().size() > 0 && !n.isExpand()){// 有孩子但不是展开的
            n.setIcon(R.drawable.tree_ec);
        }else {
            n.setIcon(-1);
        }
    }

    /**
     * 过滤出真正显示的数据
     * @param nodes
     * @return
     */
    public static List<Node> filterVisibleNodes(List<Node> nodes){
        List<Node> result = new ArrayList<>();
        for(Node node : nodes){
            if(node.isRoot() || node.isParentExpand()){
                // 如果当前节点是根节点或者它的父节点是展开的就必须显示此数据
                setNodeIcon(node);
                result.add(node);
            }
        }

        return result;
    }


    /**
     * 将数据转换和排序 外界只需调用此方法获取已封装好的数据
     * @param datas
     * @param defaultExpandLevel 默认展开几层
     * @param <T>
     * @return
     * @throws IllegalAccessException
     */
    public static <T> List<Node> getSortedNodes(List<T> datas,int defaultExpandLevel) throws IllegalAccessException {
        List<Node> result = new ArrayList<>();
        // 获取转换后的数据
        List<Node> nodes = convertDatasToNodes(datas);
        // 获得所有根节点
        List<Node> rootNodes = getRootNodes(nodes);
        for(Node node : rootNodes){
            addNode(result,node,defaultExpandLevel,1);
        }
        return result;
    }

    /**获取所有根节点*/
    private static List<Node> getRootNodes(List<Node> nodes) {
        List<Node> root = new ArrayList<>();
        for(int i = 0;i < nodes.size();i++){
            Node node = nodes.get(i);
            if (node.isRoot()){
                root.add(node);
            }
        }
        return root;
    }

    /**
     * 递归遍历根节点，封装好数据结构
     * @param result 封装好数据结构
     * @param node 当前根节点
     * @param defaultExpandLevel 默认展开层级
     * @param currentLevel 当前层级(一般指的是父节点的层级)
     */
    private static void addNode(List<Node> result, Node node, int defaultExpandLevel, int currentLevel) {
        result.add(node);
        if(defaultExpandLevel >= currentLevel) {
            node.setExpand(true);// 说明是展开的
        }
        if(node.isLeaf()) {// 如果是叶子节点就不需要继续遍历了
            return ;
        }
        for(int i = 0;i < node.getChildren().size();i++){
            addNode(result,node.getChildren().get(i),defaultExpandLevel,currentLevel+1);
        }
    }

}
