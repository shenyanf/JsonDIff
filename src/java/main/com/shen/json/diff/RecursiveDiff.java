package com.shen.json.diff;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shen.json.enums.DiffResultTypeEnum;

/**
 * 递归的比对json格式的字符串，对字符串中的JsonArray支持有问题。<br/>
 * Recursive comparison. If String contains JsonArray, diff result will not be correct.
 * 
 * @author shenyanf
 * @date 2019年1月4日
 */
public class RecursiveDiff {
    private Map<String, String> diffRet;

    // 1st json format string
    private String json1;

    // 2nd json format string
    private String json2;
    // 内部使用的屏蔽keys
    private List<String> insideExcludeKeys;
    // outside,暴露给外使用的
    private List<String> excludeKeys;

    public RecursiveDiff(String json1, String json2) {
        this.json1 = json1;
        this.json2 = json2;
        diffRet = new HashMap<String, String>();
        this.insideExcludeKeys = new ArrayList<String>();
        this.excludeKeys = new ArrayList<String>();
    }

    /**
     * 连接两个string，忽略为空的情况.<br/>
     * contact two strings
     * 
     * @param s1
     * @param s2
     * @return "",nav-aa-bb-cc
     */
    private String concat(String s1, String s2) {
        return s1.length() == 0 ? (s2.length() == 0 ? "" : s2) : (s2.length() == 0 ? s1 : String.join("-", s1, s2));
    }

    /**
     * 比较两个json对象
     * 
     * @param jsonObj2
     * @param jsonObj1
     * @param parentKeys
     * @param key
     * 
     */
    public Boolean compareJSONObject(JSONObject jsonObj1, JSONObject jsonObj2, String parentKeys, String key) {
        // System.out.println("compareJSONObject key:" + key + " \njson1:" + jsonObj1 + " \njson2:" + jsonObj2);
        Boolean finalFlag = Boolean.TRUE;
        String diffKey = concat(parentKeys, key);

        // 判断null
        // if (jsonObj1 == null && jsonObj2 == null) {
        // delKey(key);
        // return Boolean.TRUE;
        // } else if (jsonObj1 == null && jsonObj2 != null) {
        // addKey(key, "key " + key + ", json1 is null");
        // return Boolean.FALSE;
        // } else if (jsonObj1 != null && jsonObj2 == null) {
        // addKey(key, "key " + key + ", json2 is null");
        // return Boolean.FALSE;
        // }
        Set<String> json1keys = jsonObj1.keySet();
        Set<String> json2keys = jsonObj2.keySet();

        // json1 包含key，json2 不包含key
        if (json1keys.contains(key) && !json2keys.contains(key)) {
            addDifference(diffKey, DiffResultTypeEnum.ADDNEWKEY);
            finalFlag = Boolean.FALSE;
            // json1 不包含key，json2 包含key
        } else if (!json1keys.contains(key) && json2keys.contains(key)) {
            addDifference(diffKey, DiffResultTypeEnum.DELKEY);
            finalFlag = Boolean.FALSE;
            // json1 和json2 都包含key
        } else if (json1keys.contains(key) && json2keys.contains(key)) {
            finalFlag = compareJson(jsonObj1.get(key).toString(), jsonObj2.get(key).toString(), parentKeys, key);
        }

        return finalFlag;
    }

    /**
     * 比较两个JSONArray元素个数,内容是否一致---忽略顺序
     * 
     * @param array1
     * @param array2
     * @param parentKeys
     * @param key
     * @return
     */
    public Boolean compareJSONArray(JSONArray array1, JSONArray array2, String parentKeys, String key) {
        // System.out.println("compareJSONArray key:" + key + " \njson1:" + array1 + " \njson2:" + array2);
        // System.out.println(diffRet);

        Boolean finalFlag = Boolean.TRUE;
        String diffKey = concat(parentKeys, key);

        // 判断null和长度
        if (array1 == null && array2 == null) {
            delKey(diffKey);
            return Boolean.TRUE;
        } else if (array1 == null && array2 != null) {
            addDifference(diffKey, DiffResultTypeEnum.DELKEY);
            return Boolean.FALSE;
        } else if (array1 != null && array2 == null) {
            addDifference(diffKey, DiffResultTypeEnum.ADDNEWKEY);
            return Boolean.FALSE;
        } else if (array1.size() != array2.size()) {
            addDifference(diffKey, DiffResultTypeEnum.LENGTHNOTEQUAL);
            return Boolean.FALSE;
        }

        for (int index = 0; index < array1.size(); index++) {
            boolean flag = Boolean.FALSE;
            // array1的第index个元素还是JSONArray,则遍历array2的所有元素,递归比较...
            try {
                Object obj1 = array1.get(index);

                for (int index2 = 0; index2 < array2.size(); index2++) {
                    Object obj2 = array2.get(index2);
                    flag = compareJson(obj1.toString(), obj2.toString(), parentKeys, key);
                    // 如果已经相同，跳出循环
                    if (flag) {
                        break;
                    }
                }
            } catch (Exception e) {
            }
            finalFlag = finalFlag && flag;
        }
        if (!finalFlag) {
            addDifference(diffKey, DiffResultTypeEnum.DIFFERENCE);
        } else {
            delKey(diffKey);
        }
        return finalFlag;
    }

    /**
     * 比较两个java 实体object。<br/>
     * compare two Java Object
     * 
     * @param obj1
     * @param obj2
     * @param key
     * @return
     */
    public Boolean compareObject(Object obj1, Object obj2, String parentKeys, String key) {
        // System.out.println("compareObject key:" + key + " \njson1:" + obj1 + " \njson2:" + obj2);
        String diffKey = concat(parentKeys, key);

        if ((obj1 == null && obj2 == null) || (obj1 != null && obj1.equals(obj2))) {
            delKey(diffKey);
            return Boolean.TRUE;
        } else {
            addDifference(diffKey, DiffResultTypeEnum.DIFFERENCE);
            return Boolean.FALSE;
        }
    }

    /**
     * expose for outside call.
     */
    public Boolean compareJson() {
        return compareJson(json1, json2, "", "");
    }

    /**
     * 一旦相同删除对应key
     * 
     * @param key
     */
    private void delKey(String key) {
        // System.out.println("==========del key:" + key + " ==============");
        try {
            diffRet.remove(key);
            insideExcludeKeys.add(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 不存在才添加
     * 
     * @param key
     * @param value
     */
    private void addDifference(String key, DiffResultTypeEnum diffResultTypeEnum) {
        // System.out.println("=============addDifference key:" + key + "===============");

        // 不记录key=""的差异
        if (key == null || key.length() == 0) {
            return;
        }

        if (diffRet.containsKey(key)) {
            Optional<DiffResultTypeEnum> optional = Optional
                    .ofNullable(DiffResultTypeEnum.getDiffResultTypeEnumByDesc(diffRet.get(key)));
            // 如果差异结果是更具体的差异，覆盖之前的结果
            if (optional.isPresent() && optional.get().getValue() < 300 && diffResultTypeEnum.getValue() >= 300) {
                diffRet.put(key, diffResultTypeEnum.getDesc());
            }
        } else {
            diffRet.put(key, diffResultTypeEnum.getDesc());
        }
    }

    /**
     * 获取json string 级联的所有key<br/>
     * get keys, cascade
     * 
     * @param jsonStr
     * @return
     */
    public List<String> cascadeKeys(String jsonStr, String prefix) {
        List<String> keys = new ArrayList<String>();
        List<String> currentlevelKeys = null;
        try {
            JSONObject jsonObject = JSON.parseObject(jsonStr);

            currentlevelKeys = new ArrayList<>(
                    jsonObject.keySet().stream().map((k) -> concat(prefix, k)).collect(Collectors.toSet()));
            keys.addAll(currentlevelKeys);

            for (String k : currentlevelKeys) {
                String newPrefix = concat(prefix, k);
                keys.addAll(cascadeKeys(jsonObject.get(k).toString(), newPrefix));
            }
        } catch (Exception e) {
        }
        return keys;
    }

    /**
     * 比对两个json格式的字符串
     * 
     * @param json1
     * @param json2
     * @param parentKeys
     *            '-'分割
     * @param key
     * @return
     */
    public Boolean compareJson(String jsonStr1, String jsonStr2, String parentKeys, String key) {
        // System.out.println("compareJson key:" + key + " \njson1:" + jsonStr1 + " \njson2:" + jsonStr2);

        String diffKey = concat(parentKeys, key);

        // 判断null
        if (jsonStr1 == null && jsonStr2 == null) {
            delKey(diffKey);
            return Boolean.TRUE;
        } else if (jsonStr1 == null && jsonStr2 != null) {
            addDifference(diffKey, DiffResultTypeEnum.DELKEY);
            return Boolean.FALSE;
        } else if (jsonStr1 != null && jsonStr2 == null) {
            addDifference(diffKey, DiffResultTypeEnum.ADDNEWKEY);
            return Boolean.FALSE;
        }
        // String作为兜底
        if (jsonStr1.equals(jsonStr2)) {
            List<String> keys = cascadeKeys(jsonStr1, diffKey);

            keys.forEach(k -> delKey(k));
            // 当前的diffKey也需要屏蔽掉
            delKey(diffKey);

            return Boolean.TRUE;
        }

        // 优先使用JSONObject格式对比
        try {
            Boolean flag = Boolean.FALSE;
            Boolean finalFlag = Boolean.TRUE;
            JSONObject jsonObject1 = JSON.parseObject(jsonStr1);
            JSONObject jsonObject2 = JSON.parseObject(jsonStr2);
            Set<String> json1keys = jsonObject1.keySet();
            Set<String> json2keys = jsonObject2.keySet();
            // 交集
            Set<String> intersectionKeys = json1keys.stream().collect(Collectors.toSet());
            intersectionKeys.retainAll(json2keys);
            // 并集
            Set<String> unionKeys = json1keys.stream().collect(Collectors.toSet());
            unionKeys.addAll(json2keys);

            // System.out.println("key:" + key + " unionKeys:" + unionKeys + " intersectionKeys:" + intersectionKeys);
            if (unionKeys.size() > 0) {
                for (String k : unionKeys) {
                    // System.out.println(
                    // "Before compareJson key:" + k + " \njson1:" + jsonObject1 + " \njson2:" + jsonObject2);
                    flag = compareJson(jsonObject1.getString(k), jsonObject2.getString(k), diffKey, k);
                    finalFlag = finalFlag && flag;
                }
            }

            return finalFlag;
        } catch (Exception e) {
        }

        // 次之使用JSONArray格式对比
        try {
            JSONArray jsonArray1 = JSON.parseArray(jsonStr1);
            JSONArray jsonArray2 = JSON.parseArray(jsonStr2);

            return compareJSONArray(jsonArray1, jsonArray2, parentKeys, key);
        } catch (Exception e1) {
        }

        // 再次之使用Object格式对比
        try {
            Object obj1 = (Object) jsonStr1;
            Object obj2 = (Object) jsonStr2;

            return compareObject(obj1, obj2, parentKeys, key);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        return Boolean.TRUE;
    }

    public Map<String, String> getDiffRet() {
        // System.out.println("excluedKeys:" + insideExcludeKeys);
        // System.out.println("diffRet:" + diffRet);

        Iterator<Entry<String, String>> iterator1 = diffRet.entrySet().iterator();
        while (iterator1.hasNext()) {
            String key = iterator1.next().getKey();

            // 去掉在insideExcludeKeys中出现的diffRet的keys
            if (insideExcludeKeys.contains(key)) {
                iterator1.remove();
            }
        }

        Iterator<Entry<String, String>> iterator2 = diffRet.entrySet().iterator();
        while (iterator2.hasNext()) {
            String key = iterator2.next().getKey();

            // 去掉在diffRet的keys中出现的excludeKeys
            for (String outStr : excludeKeys) {
                if (key.contains(outStr)) {
                    iterator2.remove();
                }
            }
        }
        // System.out.println("diffRet:" + diffRet);
        return diffRet;
    }

    public void setDiffRet(Map<String, String> diffRet) {
        this.diffRet = diffRet;
    }

    public List<String> getExcludeKeys() {
        return excludeKeys;
    }

    /**
     * 建议填写完整key链路，例如：data-a-b-c-d、data-respData-nav-text
     * 
     * @param excludeKeys
     */
    public void setExcludeKeys(List<String> excludeKeys) {
        this.excludeKeys.addAll(excludeKeys);
    }

    public String getJson1() {
        return json1;
    }

    public void setJson1(String json1) {
        this.json1 = json1;
    }

    public String getJson2() {
        return json2;
    }

    public void setJson2(String json2) {
        this.json2 = json2;
    }

    public static void main(String[] args) {
        // String json1 = "{\"nav\":[{},{\"aa\":[1,2,3]},{\"text\":\"小灰灰\"},{\"bb\":\"haha\"}]}";
        // String json2 = "{\"nav\":[{\"cc\":2},{\"aa\":[1,2,3]},{\"text\":\"大灰灰\"},{}]}";
        String json1 = "{\"data\":{\"respCode\":[1,2,3],\"errMsg\":\"\",\"respData\":{\"nav\":[{\"aa\":[1,2,3]},{},{\"text\":\"大灰机\",\"href\":\"//mbiubiubiu\"}]}}}";
        String json2 = "{\"data\":{\"aaaa\":1,\"respData\":{\"nav\":[{\"aa\":[2,1,3,4]},{},{\"text\":\"大灰机2\",\"href\":\"//mbiubiubiu\"}]},\"errMsg\":\"\",}}";

        RecursiveDiff diffUtil = new RecursiveDiff(json1, json2);
        diffUtil.setExcludeKeys(new ArrayList<String>() {
            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            {
                add("aaaa");
                add("data-respData-nav-text");
            }
        });
        diffUtil.compareJson();

        System.out.println(diffUtil.getDiffRet());
    }
}
