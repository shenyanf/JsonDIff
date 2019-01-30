package com.shen.json.diff;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * 先格式化然后再比对json格式的字符串，对字符串中的JsonArray支持有问题。<br/>
 * Formatted String then diff them line by line. If String contains JsonArray, diff result will not be correct.
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

    public FormattedDiff(String json1, String json2) {
        this.json1 = json1;
        this.json2 = json2;
        diffRet = new HashMap<String, String>();
        setExcludeKeys(new ArrayList<String>());
    }

    public void formatted() {

        JsonParser parser = new JsonParser();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        JsonElement el = parser.parse(json1);
        System.out.println(gson.toJson(el));

    }

    public Map<String, String> getDiffRet() {
        return diffRet;
    }

    public void setDiffRet(Map<String, String> diffRet) {
        this.diffRet = diffRet;
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
        this.excludeKeys = excludeKeys;
    }

    public static void main(String[] args) {
        String json1 = "[{\"key\":\"AdParam\",\"value\":{\"sid\":\"1546595999966\",\"slotIds\":[10001],\"cateParentId\":[101],\"cityId\":0,\"areaId\":0,\"keyword\":\"荣耀v20\",\"userCityId\":637,\"sortPolicy\":0,\"pageNo\":1,\"minPrice\":0,\"maxPrice\":999999,\"serviceIds\":[],\"baoYou\":false,\"uid\":0,\"cookies\":{\"sts\":\"1546595677051\",\"osv\":\"21\",\"t\":\"15\",\"v\":\"5.7.5\",\"tk\":\"EA015534A7CF88207554FEED7831703A\",\"imei\":\"A100004C1A636E\",\"lon\":\"119.581475\",\"model\":\"vivo+X6A\",\"brand\":\"vivo\",\"channelid\":\"market_913\",\"lat\":\"32.608896\",\"seq\":\"124\"},\"userAgent\":\"Zhuan/5.7.5 (5007005) Dalvik/2.1.0 (Linux; U; Android 5.0.2; vivo X6A Build/LRX22G)\",\"ip\":\"49.71.57.64\",\"latitude\":\"32.608896\",\"longitude\":\"119.581475\"}}]";
        String json2 = "[{\"key\":\"String\",\"value\":\"logId\\u003d-658717430 id\\u003d1530979243950 uid\\u003d1841 cmd\\u003dILoginServiceFacade.index sys\\u003dershou_h_youpin ip\\u003d113.88.45.120\"},{\"key\":\"QcResultDTO\",\"value\":{\"qcOrderId\":164245030542770316,\"checkDesc\":\"经检测设备国外版 该机系统资料已更改 型号不详 网络制式无法检测 有拆修痕迹 建议买家根据实际需求谨慎购买。\",\"itemsResult\":[{\"itemId\":1110001,\"result\":[{\"itemValue\":\"1080703\",\"remark\":\"\"}]},{\"itemId\":1110002,\"result\":[{\"itemValue\":\"511433\",\"remark\":\"\"}]},{\"itemId\":1110003,\"result\":[{\"itemValue\":\"1110003004\",\"remark\":\"\"}]},{\"itemId\":1110004,\"result\":[{\"itemValue\":\"1110004004\",\"remark\":\"\"}]},{\"itemId\":1110005,\"result\":[{\"itemValue\":\"1110005004\",\"remark\":\"\"}]},{\"itemId\":1110006,\"result\":[{\"itemValue\":\"358445073486180\",\"remark\":\"\"}]},{\"itemId\":1110007,\"result\":[{\"itemValue\":\"1110007001\",\"remark\":\"\"}]},{\"itemId\":1110008,\"result\":[{\"itemValue\":\"1110008003\",\"remark\":\"\"}]},{\"itemId\":1110010,\"result\":[{\"itemValue\":\"n/a\",\"remark\":\"\"}]},{\"itemId\":1110011,\"result\":[{\"itemValue\":\"1110011002\",\"remark\":\"\"}]},{\"itemId\":1110012,\"result\":[{\"itemValue\":\"1110012002\",\"remark\":\"7.0\"}]},{\"itemId\":1120101,\"result\":[{\"itemValue\":\"1120101001\",\"remark\":\"\"}]},{\"itemId\":1120102,\"result\":[{\"itemValue\":\"1120102004\",\"remark\":\"\"}]},{\"itemId\":1120103,\"result\":[{\"itemValue\":\"1120103003\",\"remark\":\"\"}]},{\"itemId\":1120201,\"result\":[{\"itemValue\":\"1120201002\",\"remark\":\"\"}]},{\"itemId\":1120202,\"result\":[{\"itemValue\":\"1120202004\",\"remark\":\"\"}]},{\"itemId\":1120203,\"result\":[{\"itemValue\":\"1120203001\",\"remark\":\"\"}]},{\"itemId\":1120301,\"result\":[{\"itemValue\":\"1120301003\",\"remark\":\"\"}]},{\"itemId\":1120302,\"result\":[{\"itemValue\":\"1120302001\",\"remark\":\"\"}]},{\"itemId\":1120303,\"result\":[{\"itemValue\":\"1120303001\"}]},{\"itemId\":1120401,\"result\":[{\"itemValue\":\"1120401001\",\"remark\":\"\"}]},{\"itemId\":1130101,\"result\":[{\"itemValue\":\"1130101001\",\"remark\":\"\"}]},{\"itemId\":1130102,\"result\":[{\"itemValue\":\"1130102001\",\"remark\":\"\"}]},{\"itemId\":1130201,\"result\":[{\"itemValue\":\"1130201001\",\"remark\":\"\"}]},{\"itemId\":1130202,\"result\":[{\"itemValue\":\"1130202001\",\"remark\":\"\"}]},{\"itemId\":1130203,\"result\":[{\"itemValue\":\"1130203001\",\"remark\":\"\"}]},{\"itemId\":1130301,\"result\":[{\"itemValue\":\"1130301008\"},{\"itemValue\":\"1130301004\"}]},{\"itemId\":1130401,\"result\":[{\"itemValue\":\"1130401001\",\"remark\":\"\"}]},{\"itemId\":1130402,\"result\":[{\"itemValue\":\"1130402003\",\"remark\":\"\"}]},{\"itemId\":1130403,\"result\":[{\"itemValue\":\"1130403001\",\"remark\":\"\"}]},{\"itemId\":1140101,\"result\":[{\"itemValue\":\"1140101001\",\"remark\":\"\"}]},{\"itemId\":1140102,\"result\":[{\"itemValue\":\"1140102001\",\"remark\":\"\"}]},{\"itemId\":1140103,\"result\":[{\"itemValue\":\"1140103001\",\"remark\":\"\"}]},{\"itemId\":1140104,\"result\":[{\"itemValue\":\"1140104006\",\"remark\":\"\"}]},{\"itemId\":1140105,\"result\":[{\"itemValue\":\"1140105001\",\"remark\":\"\"}]},{\"itemId\":1140106,\"result\":[{\"itemValue\":\"1140106006\",\"remark\":\"\"}]},{\"itemId\":1140201,\"result\":[{\"itemValue\":\"1140201001\",\"remark\":\"\"}]},{\"itemId\":1140202,\"result\":[{\"itemValue\":\"1140202005\",\"remark\":\"\"}]},{\"itemId\":1140203,\"result\":[{\"itemValue\":\"1140203005\",\"remark\":\"\"}]},{\"itemId\":1140301,\"result\":[{\"itemValue\":\"1140301001\",\"remark\":\"\"}]},{\"itemId\":1140302,\"result\":[{\"itemValue\":\"1140302001\",\"remark\":\"\"}]},{\"itemId\":1140303,\"result\":[{\"itemValue\":\"1140303001\",\"remark\":\"\"}]},{\"itemId\":1140304,\"result\":[{\"itemValue\":\"1140304001\",\"remark\":\"\"}]},{\"itemId\":1140401,\"result\":[{\"itemValue\":\"1140401001\"}]},{\"itemId\":1140402,\"result\":[{\"itemValue\":\"1140402001\",\"remark\":\"\"}]},{\"itemId\":1140501,\"result\":[{\"itemValue\":\"1140501001\",\"remark\":\"\"}]},{\"itemId\":1140502,\"result\":[{\"itemValue\":\"1140502001\",\"remark\":\"\"}]},{\"itemId\":1140503,\"result\":[{\"itemValue\":\"1140503001\",\"remark\":\"\"}]},{\"itemId\":1140504,\"result\":[{\"itemValue\":\"1140504003\",\"remark\":\"\"}]},{\"itemId\":1140601,\"result\":[{\"itemValue\":\"1140601001\",\"remark\":\"\"}]},{\"itemId\":1140701,\"result\":[{\"itemValue\":\"1140701001\",\"remark\":\"\"}]},{\"itemId\":1140702,\"result\":[{\"itemValue\":\"1140702001\",\"remark\":\"\"}]},{\"itemId\":1140703,\"result\":[{\"itemValue\":\"1140703001\",\"remark\":\"\"}]},{\"itemId\":1140801,\"result\":[{\"itemValue\":\"1140801001\",\"remark\":\"\"}]},{\"itemId\":1140802,\"result\":[{\"itemValue\":\"1140802004\",\"remark\":\"\"}]},{\"itemId\":1140803,\"result\":[{\"itemValue\":\"1140803005\",\"remark\":\"\"}]},{\"itemId\":1140901,\"result\":[{\"itemValue\":\"1140901001\",\"remark\":\"\"}]},{\"itemId\":1140902,\"result\":[{\"itemValue\":\"1140902004\",\"remark\":\"\"}]},{\"itemId\":1141001,\"result\":[{\"itemValue\":\"1141001001\",\"remark\":\"\"}]},{\"itemId\":1141101,\"result\":[{\"itemValue\":\"1141101001\"}]},{\"itemId\":1141102,\"result\":[{\"itemValue\":\"1141102001\"}]},{\"itemId\":1141103,\"result\":[{\"itemValue\":\"1141103001\",\"remark\":\"\"}]},{\"itemId\":1141201,\"result\":[{\"itemValue\":\"1141201001\",\"remark\":\"\"}]},{\"itemId\":1141301,\"result\":[{\"itemValue\":\"1141301001\",\"remark\":\"\"}]},{\"itemId\":1150101,\"result\":[{\"itemValue\":\"1150101003\"}]},{\"itemId\":1150201,\"result\":[{\"itemValue\":\"1150201001\",\"remark\":\"\"}]},{\"itemId\":1160101,\"result\":[{\"itemValue\":\"1160101001\",\"remark\":\"\"}]}],\"optUser\":\"NJWSZluzilin\",\"status\":1,\"statusDesc\":\"可正常质检\"}}]";
        FormattedDiff diff = new FormattedDiff(json1, json2);
        // diff.formatted();
        String aaa = json2.replace("[", "[\n").replace("{", "{\n").replace("}", "\n}").replace("]", "\n]")
                .replace("\",", "\",\n").replace("},", "},\n").replace("],", "],\n").replace(",\"", ",\n\"");

        System.out.println(aaa);
        System.out.println(Arrays.asList(aaa.split("\n")));
    }
}
