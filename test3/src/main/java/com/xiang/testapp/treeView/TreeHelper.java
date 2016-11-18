package com.xiang.testapp.treeView;

import com.xiang.testapp.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gordon on 2016/10/19.
 */

public class TreeHelper {

    public static <T> List<Node> getSortData(List<T> data, int defaultLevel, int currentLevel) throws IllegalAccessException {
        List<Node> result = new ArrayList<>();
        List<Node> nodes = getConvertData(data);
        List<Node> rootNodes = getRootNodes(nodes);
        for (int i = 0; i < rootNodes.size(); i++) {
            addNode(result,rootNodes.get(i),defaultLevel,currentLevel);
        }

        return result;
    }

    private static <T> List<Node> getConvertData(List<T> data) throws IllegalAccessException {
        List<Node> nodes = new ArrayList<>();
        Node node = null;
        for (int i = 0; i < data.size(); i++) {
            T t = data.get(i);
            int id = -1;
            int pid = -1;
            String label = "";

            Class clazz = t.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (int j = 0; j < fields.length; j++) {
                Field field = fields[j];
                if (field.getAnnotation(TreeNodeId.class) != null) {
                    field.setAccessible(true);
                    id = field.getInt(t);
                }
                if (field.getAnnotation(TreeNodePid.class) != null) {
                    field.setAccessible(true);
                    pid = field.getInt(t);
                }
                if (field.getAnnotation(TreeNodeLabel.class) != null) {
                    field.setAccessible(true);
                    label = (String) field.get(t);
                }
            }
            node = new Node(id,pid,label);
            nodes.add(node);
        }

        for (int i = 0; i < nodes.size(); i++) {
            Node n = nodes.get(i);
            for (int j = i + 1; j < nodes.size(); j++) {
                Node m = nodes.get(j);
                if (n.getId() == m.getPid()) {
                    n.getChildList().add(m);
                    m.setParent(n);
                }else if (m.getId() == n.getPid()){
                    m.getChildList().add(n);
                    n.setParent(m);
                }
            }
        }

        for (int i = 0; i < nodes.size(); i++) {
            setNodeIcon(nodes.get(i));
        }

        return nodes;
    }

    private static void setNodeIcon(Node node) {
        if (node.getChildList().size() > 0 && node.isExpand()) {
            node.setIcon(R.drawable.tree_ex);
        }else if (node.getChildList().size() > 0 && !node.isExpand()){
            node.setIcon(R.drawable.tree_ec);
        }else {
            node.setIcon(-1);
        }
    }

    private static List<Node> getRootNodes(List<Node> nodes) {
        List<Node> rootNode = new ArrayList<>();
        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            if (node.isParent()) {
                rootNode.add(node);
            }
        }
        return rootNode;
    }

    private static void addNode(List<Node> result, Node node, int defaultLevel, int currentLevel) {
        result.add(node);
        if (defaultLevel >= currentLevel) {
            node.setExpand(true);
        }
        if (node.isLeaf()) {
            return;
        }
        for (int i = 0; i < node.getChildList().size(); i++) {
            addNode(result,node.getChildList().get(i),defaultLevel,currentLevel + 1);
        }
    }

    public static List<Node> filterVisibleNode(List<Node> datas){
        List<Node> result = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            Node node = datas.get(i);
            if (node.isParent() || node.isParentExpand()) {
                setNodeIcon(node);
                result.add(node);
            }
        }

        return result;
    }

}
