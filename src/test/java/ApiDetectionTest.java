import apiDependent.FileUtil;
import apiDependent.HttpClient4Util;
import apiDependent.MessageDigestUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.Test;
import java.text.MessageFormat;
import java.util.*;

/**
 * @author wufeng
 * @date 2025/5/6 14:15
 */
public class ApiDetectionTest {

    private static String baseUrl="http://monitor.pdmiryun.com";

    @Test
    public void test() {
        String appSecret = "7d7541e767bf46818331e53a5cbcf486";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("appkey", "9b9350a1b25e48aa93f2a1221b8f27d7");
        paramMap.put("timestamp", System.currentTimeMillis());

        List<String> paramList = new ArrayList<>();
        paramMap.keySet().forEach(key -> {
            paramList.add(String.valueOf(paramMap.get(key)));
        });
        paramList.add(appSecret);
        Collections.sort(paramList);
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("sign", MessageDigestUtil.getMd5String(StringUtils.join(paramList, "")));

        String result = HttpClient4Util.doGet(baseUrl + "/monitor/api/token/accessToken", paramMap,
                headerMap);
        System.out.println(result);

        JSONObject json = JSONObject.parseObject(result);
        String token = json.getJSONObject("data").getString("token");

        paramList.add(token);
        Collections.sort(paramList);
        headerMap = new HashMap<>();
        headerMap.put("authorization", token);
        headerMap.put("sn", MessageDigestUtil.getMd5String(StringUtils.join(paramList, "&")));

        //文本检测
        this.auditText(paramMap,headerMap);
        //图片检测
//        this.auditImage(paramMap,headerMap);
        //富文本检测
//        this.auditMultiText(paramMap,headerMap);
        //检测结果-传任务id
//        this.auditResult(paramMap,headerMap,Arrays.asList("559e5460210d4bf4a995e59b319fd81b"));
    }

    //纯文本检测
    public void auditText(Map<String, Object> paramMap,Map<String, String> headerMap){
        String url = baseUrl + "/monitor/api/v2/audit/text?appkey={0}&timestamp={1}";
        String formatUrl = MessageFormat.format(url, paramMap.get("appkey"), String.valueOf(paramMap.get("timestamp")));

        String path="D:\\autotest\\fengkong\\res\\testText.txt";
        paramMap.clear();
        paramMap.put("dataWay","polling");
        paramMap.put("content",FileUtil.readFile(path));
        String s = HttpClient4Util.doPostJson(formatUrl, paramMap, headerMap);
        System.out.println("text:" + s);

    }

    //图片检测
    public void auditImage(Map<String, Object> paramMap,Map<String, String> headerMap){
        String url = baseUrl + "/monitor/api/v2/audit/image?appkey={0}&timestamp={1}";
        String formatUrl = MessageFormat.format(url, paramMap.get("appkey"), String.valueOf(paramMap.get("timestamp")));

        JSONArray array=new JSONArray();

        JSONObject json=new JSONObject();
        json.put("url","http://monitor.pdmiryun.com/monitor/pub/ceshigongsi/res/img/2025/03/25/7872b28042824643afaecc61361e8a97.png");
        json.put("title","接口传图1.png");
        array.add(json);
        JSONObject json2=new JSONObject();
        json2.put("url","http://monitor.pdmiryun.com/monitor/pub/ceshigongsi/res/img/2025/03/25/9ee6ef038fee44609a7c43f0eff4f4e3.png");
        json2.put("title","接口传图2.png");
        array.add(json2);

        JSONObject param=new JSONObject();
        param.put("dataWay","polling");
        param.put("data",array);

        String s = HttpClient4Util.doPostJson(formatUrl, param, headerMap);
        System.out.println("image:" + s);
    }

    //富文本检测
    public void auditMultiText(Map<String, Object> paramMap,Map<String, String> headerMap){
        String path="D:\\autotest\\fengkong\\res\\auditRichText.txt";
        String url = baseUrl + "/monitor/api/v2/audit/multiText?appkey={0}&timestamp={1}";
        String formatUrl = MessageFormat.format(url, paramMap.get("appkey"), String.valueOf(paramMap.get("timestamp")));
        paramMap.clear();
        paramMap.put("content", FileUtil.readFile(path));
        paramMap.put("dataWay","polling");
        String s = HttpClient4Util.doPostJson(formatUrl, paramMap, headerMap);
        System.out.println("richText result:" + s);
    }

    //查询检测结果
    public void auditResult(Map<String, Object> paramMap,Map<String, String> headerMap,List<String> taskIds){
        String url = baseUrl + "/monitor/api/v2/audit/queryResult?appkey={0}&timestamp={1}";
        String formatUrl = MessageFormat.format(url, paramMap.get("appkey"),
                String.valueOf(paramMap.get("timestamp")));
        paramMap.clear();

        paramMap.put("taskIds",taskIds);
        String s = HttpClient4Util.doPostJson(formatUrl, paramMap, headerMap);
        System.out.println("result:" + s);
    }
}
