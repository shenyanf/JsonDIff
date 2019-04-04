package com.shen.json.diff;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shen.json.enums.DiffResultTypeEnum;

/**
 * 先格式化然后再比对json格式的字符串<br/>
 * Formatted String then diff them line by line.
 * 
 * @author shenyanf
 * @date 2019年1月15日
 */
public class FormattedDiff {
    // diff result
    private Map<String, String> diffRet;

    // 1st json format string
    private String json1;

    // 2nd json format string
    private String json2;

    // exclude key from diffRet
    private List<String> excludeKeys;

    // json1解析后的k，v
    private Map<String, String> json1KV;

    // json2解析后的k，v
    private Map<String, String> json2KV;

    public FormattedDiff(String json1, String json2) {
        this.json1 = json1;
        this.json2 = json2;
        diffRet = new HashMap<String, String>();
        setJson1KV(new HashMap<String, String>());
        setJson2KV(new HashMap<String, String>());
        this.excludeKeys = new ArrayList<String>();
    }

    /**
     * 判断是否是json格式的字符串
     * 
     * @param jsonStr
     * @return true or false
     */
    private Boolean validate(String jsonStr) {
        if (jsonStr == null || jsonStr.isEmpty()) {
            return false;
        }

        try {
            JSON.parse(jsonStr);
            return true;
        } catch (Exception e) {

        }

        try {
            JSON.parseArray(jsonStr);
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 解析JSON格式的String，递归的拿到key和对应的value
     * 
     * @param currentLvKey
     *            上级key
     * @param jsonStr
     * @param map
     */
    private void parse(String currentLvKey, String jsonStr, Map<String, String> map) {
        String prefix = "";
        if (currentLvKey != null && !currentLvKey.isEmpty()) {
            prefix = currentLvKey + "-";
        }
        // 优先使用JSONObject格式对比
        try {
            JSONObject jsonObject = JSON.parseObject(jsonStr);
            Set<String> keys = jsonObject.keySet();
            for (String s : keys) {
                parse(prefix + s, jsonObject.getString(s), map);
            }
            return;
        } catch (Exception e) {
        }

        // 次之使用JSONArray格式对比
        try {
            JSONArray jsonArray1 = JSON.parseArray(jsonStr);
            for (int i = 0; i < jsonArray1.size(); i++) {
                parse(prefix + i, jsonArray1.getString(i), map);
            }
            return;
        } catch (Exception e1) {
        }

        map.put(currentLvKey, jsonStr);
    }

    /**
     * 差异结果
     * 
     * @return
     */
    public Map<String, String> getDiffRet() {
        if (validate(json1) && validate(json2)) {
            // 解析
            parse("", json1, this.getJson1KV());
            parse("", json2, this.getJson2KV());

            // json1Keys - json2Keys 差集
            // 需要新建Set，不能修改原来的Set
            Set<String> json1Keys = new TreeSet<String>(json1KV.keySet());
            Set<String> json2Keys = new TreeSet<String>(json2KV.keySet());
            // json1String 新增的key
            Boolean addKey = json1Keys.removeAll(json2Keys);
            if (addKey) {
                for (String s : json1Keys) {
                    diffRet.put(s, DiffResultTypeEnum.ADDNEWKEY.getDesc());
                }
            }

            // json2Keys - json1Keys 差集
            Set<String> json1Keys1 = new TreeSet<String>(json1KV.keySet());
            Set<String> json2Keys1 = new TreeSet<String>(json2KV.keySet());
            // json1String 删除的key
            Boolean delKey = json2Keys1.removeAll(json1Keys1);
            if (delKey) {
                for (String s : json2Keys1) {
                    diffRet.put(s, DiffResultTypeEnum.DELKEY.getDesc());
                }
            }

            // 交集
            Set<String> json1Keys2 = new TreeSet<String>(json1KV.keySet());
            Set<String> json2Keys2 = new TreeSet<String>(json2KV.keySet());
            json1Keys2.retainAll(json2Keys2);
            for (String s : json1Keys2) {
                if (!json1KV.get(s).equals(json2KV.get(s))) {
                    diffRet.put(s, DiffResultTypeEnum.DIFFERENCE.getDesc());
                }
            }
        } else {
            diffRet.put("warning", "json1 String or json2 String is not JSON formatted String!");
        }

        Iterator<Entry<String, String>> iterator = diffRet.entrySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next().getKey();

            // 去掉在diffRet的keys中出现的excludeKeys
            for (String s : excludeKeys) {
                if (key.contains(s)) {
                    iterator.remove();
                }
            }
        }
        return diffRet;
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

    public List<String> getExcludeKeys() {
        return excludeKeys;
    }

    public void setExcludeKeys(List<String> excludeKeys) {
        this.excludeKeys.addAll(excludeKeys);
    }

    public Map<String, String> getJson1KV() {
        return json1KV;
    }

    public void setJson1KV(Map<String, String> json1kv) {
        json1KV = json1kv;
    }

    public Map<String, String> getJson2KV() {
        return json2KV;
    }

    public void setJson2KV(Map<String, String> json2kv) {
        json2KV = json2kv;
    }

    public static void main(String[] args) {
        String json1 = "[{\"key\":\"AdParam\",\"value\":{\"sid\":\"1546999966\",\"slotIds\":[10001],\"cateParentId\":[111,222],\"cityId\":2,\"areaId\":0,\"keyword\":\"荣耀v20\",\"userCityId\":637,\"sortPolicy\":0,\"pageNo\":1,\"minPrice\":0,\"maxPrice\":999999,\"serviceIds\":[],\"baoYou\":false,\"uid\":0,\"cookies\":{\"sts\":\"1546595677051\",\"osv\":\"21\",\"t\":\"15\",\"v\":\"5.7.5\",\"tk\":\"EA015534A7CF88207554FEED7831703A\",\"imei\":\"A100004C1A636E\",\"lon\":\"119.581475\",\"model\":\"vivo+X6A\",\"brand\":\"vivo\",\"channelid\":\"market_913\",\"lat\":\"32.608896\",\"seq\":\"124\"},\"userAgent\":\"Zhuan/5.7.5 (5007005) Dalvik/2.1.0 (Linux; U; Android 5.0.2; vivo X6A Build/LRX22G)\",\"ip\":\"49.71.57.64\",\"latitude\":\"32.608896\",\"longitude\":\"119.581475\"}}]";
        String json2 = "[{\"key\":\"AdParam\",\"value\":{\"sid\":\"1546595999966\",\"slotIds\":[101],\"cateParentId\":[101],\"cityId\":0,\"areaId\":0,\"keyword\":\"荣耀v20\",\"userCityId\":637,\"sortPolicy\":0,\"pageNo\":1,\"minPrice\":0,\"maxPrice\":999999,\"serviceIds\":[],\"baoYou\":false,\"uid\":0,\"cookies\":{\"sts\":\"1546595677051\",\"osv\":\"21\",\"t\":\"15\",\"v\":\"5.7.5\",\"tk\":\"EA015534A7CF88207554FEED7831703A\",\"imei\":\"A100004C1A636E\",\"lon\":\"119.581475\",\"model\":\"vivo+X6A\",\"brand\":\"vivo\",\"channelid\":\"market_913\",\"lat\":\"32.608896\",\"seq\":\"124\"},\"userAgent\":\"Zhuan/5.7.5 (5007005) Dalvik/2.1.0 (Linux; U; Android 5.0.2; vivo X6A Build/LRX22G)\",\"ip\":\"49.71.57.64\",\"latitude\":\"32.608896\",\"longitude\":\"119.581475\"}},{\"key1\":\"AdParam1\"}]";
        FormattedDiff diff = new FormattedDiff(json1, json2);
        // String aaa = json2.replace("[", "[\n").replace("{", "{\n").replace("}", "\n}").replace("]", "\n]")
        // .replace("\",", "\",\n").replace("},", "},\n").replace("],", "],\n").replace(",\"", ",\n\"");
        //
        // System.out.println(aaa);
        // System.out.println(Arrays.asList(aaa.split("\n")));

        // diff.parse("", json1, diff.getJson2KV());
        System.out.println(diff.getDiffRet());
    }
}
