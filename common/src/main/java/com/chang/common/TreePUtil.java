package com.chang.common;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONArray;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class TreePUtil {
   public static List<Tree<Long>> getTreeLong(List<?> list, String idKey, String parentIdKey, String weightKey, Integer deep) {
      TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
      if (weightKey != null) {
         treeNodeConfig.setWeightKey(weightKey);
      }

      treeNodeConfig.setIdKey(idKey);
      treeNodeConfig.setParentIdKey(parentIdKey);
      treeNodeConfig.setDeep(deep);
      List<Tree<Long>> treeNodes = TreeUtil.build(list, 0L, treeNodeConfig, (treeNode, tree) -> {
         tree.setId((Long)BeanUtil.getFieldValue(treeNode, idKey));
         tree.setParentId((Long)BeanUtil.getFieldValue(treeNode, parentIdKey));
         tree.setWeight((Comparable)BeanUtil.getFieldValue(treeNode, weightKey));
         Map<String, Object> stringObjectMap = BeanUtil.beanToMap(treeNode, (String[])null);
         stringObjectMap.remove(idKey);
         stringObjectMap.remove(parentIdKey);
         stringObjectMap.remove(weightKey);
         tree.putAll(stringObjectMap);
      });
      return treeNodes;
   }

   public static List<Tree<Integer>> getTreeInteger(List<?> list, String idKey, String parentIdKey, String weightKey, Integer deep) {
      TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
      if (weightKey != null) {
         treeNodeConfig.setWeightKey(weightKey);
      }

      treeNodeConfig.setIdKey(idKey);
      treeNodeConfig.setParentIdKey(parentIdKey);
      treeNodeConfig.setDeep(deep);
      List<Tree<Integer>> treeNodes = TreeUtil.build(list, 0, treeNodeConfig, (treeNode, tree) -> {
         tree.setId((Integer)BeanUtil.getFieldValue(treeNode, idKey));
         tree.setParentId((Integer)BeanUtil.getFieldValue(treeNode, parentIdKey));
         tree.setWeight((Comparable)BeanUtil.getFieldValue(treeNode, weightKey));
         Map<String, Object> stringObjectMap = BeanUtil.beanToMap(treeNode, (String[])null);
         stringObjectMap.remove(idKey);
         stringObjectMap.remove(parentIdKey);
         stringObjectMap.remove(weightKey);
         tree.putAll(stringObjectMap);
      });
      return treeNodes;
   }

   public static List<Tree<String>> getTreeString(List<?> list, String topParentId, String idKey, String parentIdKey, String weightKey, Integer deep) {
      TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
      if (weightKey != null) {
         treeNodeConfig.setWeightKey(weightKey);
      }

      treeNodeConfig.setIdKey(idKey);
      treeNodeConfig.setParentIdKey(parentIdKey);
      treeNodeConfig.setDeep(deep);
      List<Tree<String>> treeNodes = TreeUtil.build(list, topParentId, treeNodeConfig, (treeNode, tree) -> {
         tree.setId(String.valueOf(BeanUtil.getFieldValue(treeNode, idKey)));
         tree.setParentId(String.valueOf(BeanUtil.getFieldValue(treeNode, parentIdKey)));
         tree.setWeight((Comparable)BeanUtil.getFieldValue(treeNode, weightKey));
         Map<String, Object> stringObjectMap = BeanUtil.beanToMap(treeNode, (String[])null);
         stringObjectMap.remove(idKey);
         stringObjectMap.remove(parentIdKey);
         stringObjectMap.remove(weightKey);
         tree.putAll(stringObjectMap);
      });
      return treeNodes;
   }

   private static <T> void checkTrees(List<Tree<T>> trees) {
      if (trees == null || trees.size() == 0) {
         throw new RuntimeException("Trees is null or size is 0");
      }
   }

   public static <T> Tree<T> getChildrenTree(List<Tree<T>> trees, T id) {
      checkTrees(trees);
      Iterator var2 = trees.iterator();

      Tree node;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         Tree<T> tree = (Tree)var2.next();
         node = tree.getNode(id);
      } while(node == null);

      return node;
   }

   public static <T> boolean checkDeadLoop(List<Tree<T>> trees, T id, T pid) {
      List<T> childrenIds = getChildrenIds(trees, id, true);
      return childrenIds.contains(pid);
   }

   public static <T> List<T> getChildrenSubTreeAllIds(List<Tree<T>> trees, T id) {
      checkTrees(trees);
      List<T> ids = new ArrayList();
      Iterator var3 = trees.iterator();

      while(var3.hasNext()) {
         Tree<T> tree = (Tree)var3.next();
         Tree<T> node = tree.getNode(id);
         if (node != null) {
            findTreeSubIds(node, ids);
         }
      }

      return ids;
   }

   private static <T> void findTreeSubIds(Tree<T> tree, List<T> ids) {
      if (ObjectUtil.isNull(tree.getParent())) {
         treeDoWith(tree, (x) -> {
            ids.add((T) x.getId());
         });
      } else {
         if (ObjectUtil.isNull(tree.getParent().getParent())) {
            treeDoWith(tree, (x) -> {
               ids.add((T) x.getId());
            });
            ids.add(tree.getParent().getId());
         } else {
            tree = tree.getParent();
            findTreeSubIds(tree, ids);
         }

      }
   }

   public static <T> void treeDoWith(Tree tree, Consumer<Tree<T>> consumer) {
      consumer.accept(tree);
      Object children = tree.getChildren();
      List<Tree> treeChildren = null;
      if (children != null) {
         if (children instanceof JSONArray) {
            JSONArray jaChildren = (JSONArray)JSONArray.class.cast(children);
            treeChildren = JSONArray.parseArray(jaChildren.toString(), Tree.class);
            tree.setChildren(treeChildren);
         } else {
            if (!(children instanceof List)) {
               throw new RuntimeException("type is err");
            }

            treeChildren = (List)List.class.cast(children);
         }

         if (treeChildren != null) {
            Iterator var6 = treeChildren.iterator();

            while(var6.hasNext()) {
               Tree<T> node = (Tree)var6.next();
               treeDoWith(node, consumer);
            }
         }
      }

   }

   public static void treeRepair(Tree tree) {
      Object children = tree.getChildren();
      List<Tree> treeChildren = null;
      if (children != null) {
         if (children instanceof JSONArray) {
            JSONArray jaChildren = (JSONArray)JSONArray.class.cast(children);
            tree.setChildren(JSONArray.parseArray(jaChildren.toString(), Tree.class));
         } else {
            if (!(children instanceof List)) {
               throw new RuntimeException("type is err");
            }

            treeChildren = (List)List.class.cast(children);
         }

         if (treeChildren != null) {
            Iterator var5 = treeChildren.iterator();

            while(var5.hasNext()) {
               Tree node = (Tree)var5.next();
               treeRepair(node);
            }
         }
      }

   }

   public static <T> List<T> getParentsIds(List<Tree<T>> trees, T id, boolean includeCurrentNode) {
      checkTrees(trees);
      List<T> parentsIds = new ArrayList();
      Iterator var4 = trees.iterator();

      while(var4.hasNext()) {
         Tree<T> tree = (Tree)var4.next();
         Tree<T> node = tree.getNode(id);
         if (node != null) {
            if (includeCurrentNode) {
               parentsIds.add(node.getId());
            }

            getParentsIds(node, parentsIds);
         }
      }

      return parentsIds;
   }

   public static <T> List<T> getChildrenIds(List<Tree<T>> trees, T id, boolean includeCurrentNode) {
      checkTrees(trees);
      List<T> childrenIds = new ArrayList();
      Iterator var4 = trees.iterator();

      while(var4.hasNext()) {
         Tree<T> tree = (Tree)var4.next();
         Tree<T> node = tree.getNode(id);
         if (node != null) {
            if (includeCurrentNode) {
               childrenIds.add(node.getId());
            }

            getChildrenIds(node, childrenIds);
         }
      }

      return childrenIds;
   }

   public static <T> List<T> getTreeAllIds(List<Tree<T>> trees, T id) {
      checkTrees(trees);
      T topParentId = getTopParentId(trees, id);
      return getChildrenIds(trees, topParentId, true);
   }

   private static <T> void getParentsIds(Tree<T> tree, List<T> list) {
      if (tree.getParent() != null) {
         Tree<T> parent = tree.getParent();
         list.add(parent.getId());
         getParentsIds(parent, list);
      }

   }

   private static <T> void getChildrenIds(Tree<T> tree, List<T> list) {
      List<Tree<T>> childrenS = tree.getChildren();
      if (childrenS != null) {
         Iterator var3 = childrenS.iterator();

         while(var3.hasNext()) {
            Tree<T> children = (Tree)var3.next();
            list.add(children.getId());
            getChildrenIds(children, list);
         }
      }

   }

   public static <T> T getTopParentId(List<Tree<T>> trees, T id) {
      checkTrees(trees);
      Optional<T> parentId = trees.stream().filter((node) -> {
         return node.getNode(id) != null;
      }).map((filterNode) -> {
         return filterNode.getId();
      }).distinct().findFirst();
      return parentId.isPresent() ? parentId.get() : null;
   }
}
