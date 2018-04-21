package tw.org.iii.testlogin;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private RequestQueue queue;
    private File sdroot;
    private Resources resources;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    0);
        }else{
            init();
        }




    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            init();
        }
    }
    private void init() {
        queue= Volley.newRequestQueue(this);
        sdroot = Environment.getExternalStorageDirectory();
        resources =getResources();
    }

    // http://localhost:8080/fsit04/Views_message?total_id=10
    public void login(View view) {
        sighin("test999@gmail.com","測試員2號","","fb");
    }
    public void add(View view) {
        addFavorite("1","16");
    }
    public void select(View view) {
        getFavorite("1");
    }
    public void delect(View view) {
        deleteFavorite("1","5");
    }
    public void addMsg(View view) {
        addMessage("測試員01","10","安安你好");
    }
    public void getMsgByID(View view) {
        getMessageByID("10");
    }
    public void getRestruant(View view) {
        getRest();
    }
    public void search(View view) { doSearch("北投");}
    public void uploadImg(View view) { uploadFile();}





    /**
     * http://36.235.38.228:8080/fsit04/Views_message?total_id=123
     * @param total_id 景點ID
     *
     */
    private void getMessageByID(String total_id) {
        String url ="http://36.235.38.228:8080/fsit04/Views_message?total_id="+total_id;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("chad",response);
                        parseGetViews_message(response);
                    }
                }, null);
        queue.add(stringRequest);
    }

    /**JSON解析地區留言
     *
     * @param response
     */
    private void parseGetViews_message(String response) {
        try {
            JSONArray array = new JSONArray(response);
            //每一筆
            for(int i=0;i<array.length();i++){
                JSONObject obj1 = array.getJSONObject(i);
                //留言的日期
                String date =obj1.getString("date");
                Log.v("chad",date);
                //名稱
                String user_name =obj1.getString("user_name");
                Log.v("chad",user_name);
                //留言的內容
                String msg =obj1.getString("msg");
                Log.v("chad",msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 留言
     * http://36.235.38.228:8080/fsit04/Views_message
     * @param user_name   使用者id
     * @param msg      留言
     * @param total_id 景點id
     */
    private void addMessage(String user_name,String total_id,String msg) {
        String url ="http://36.235.38.228:8080/fsit04/Views_message";
        final String p1 =user_name;
        final String p2=total_id;
        final String p3=msg;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.v("chad",response);
                    }
                }, null){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> m1 =new HashMap<>();
                m1.put("user_name",p1);
                m1.put("total_id", p2);
                m1.put("msg",p3);
                return m1;
            }
        };
        queue.add(stringRequest);
    }


    /**  http://36.235.38.228:8080/J2EE/sighin.jsp  登入
     *   注意類型 首次使用登入fb 登入者 會直接註冊
     *   fb 登入者 密碼 可以隨便打 因為fb不會回傳密碼
     * @param mail        信箱 test123@gmail.com
     * @param password    密碼 test123
     * @param type        類型 1.normal  2.fb
     * @param name        fb登入者輸入名字，一般登入者給空字串 ""
     *
     */
    private void sighin(String mail,String name,String password,String type){
        final String p1=mail;
        final String p2=password;
        final String p3=type;
        final String p4=name;
        String url ="http://36.235.38.228:8080/fsit04/app/sighin";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
               new Response.Listener<String>() {
                   @Override
                   public void onResponse(String response) {
                            Log.v("chad",response);
                        }
                    }, null){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String,String> m1 =new HashMap<>();
                    m1.put("mail",p1);
                    m1.put("password", p2);
                    m1.put("type",p3);
                    m1.put("name",p4);
                    return m1;
                }
            };


        queue.add(stringRequest);
    }

    /**  http://36.235.38.228:8080/fsit04/User_favorite  加入我的最愛
     *
     * @param user_id     用戶id
     * @param total_id   地點的id
     */
    private void addFavorite(String user_id,String total_id){
        String url ="http://36.235.38.228:8080/fsit04/User_favorite";

        final String p1 =user_id;
        final String p2=total_id;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.v("chad",response);
                        }
                    }, null){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String,String> m1 =new HashMap<>();
                    m1.put("user_id",p1);
                    m1.put("total_id", p2);
                    return m1;
                }
            };
        queue.add(stringRequest);

    }
    /**  http://36.235.38.228:8080/J2EE/addFavorite.jsp  刪除我的最愛
     *
     * @param user_id     用戶id
     * @param total_id   地點的id
     */
    private void deleteFavorite(String user_id,String total_id){
        String url ="http://36.235.38.228:8080/fsit04/User_favorite";

        final String p1 =user_id;
        final String p2=total_id;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("chad",response);
                    }
                }, null){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> m1 =new HashMap<>();
                m1.put("_method","DELETE");
                m1.put("user_id",p1);
                m1.put("total_id", p2);

                return m1;
            }
        };
        queue.add(stringRequest);

    }

    /** 取得我的最愛
     *  http://36.235.38.228:8080/fsit04/User_favorite
     * @param user_id 用戶id
     */
    private void getFavorite(String user_id){
        final String p1=user_id;
        String url ="http://36.235.38.228:8080/fsit04/User_favorite?user_id="+p1;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseGetFavorite(response);

                    }
                }, null);

        queue.add(stringRequest);
    }

    /**  用戶我的最愛parseJSON
     *
     * @param response
     */
    private void parseGetFavorite(String response){
        try {
            JSONArray array1 = new JSONArray(response);
            Log.v("chad",array1.length()+"");
            for(int i= 0;i<array1.length();i++) {
                JSONObject ob1 =array1.getJSONObject(i);
                //地點ID
                String total_id = ob1.getString("total_id");
                Log.v("chad",total_id);
                //地點名稱
                String name = ob1.getString("name");
                Log.v("chad",name);
                //地點類型
                String type= ob1.getString("type");
                Log.v("chad",type);
                //分類
                String CAT2 = ob1.getString("CAT2");
                Log.v("chad",CAT2);
                //營業時間
                String MEMO_TIME = ob1.getString("MEMO_TIME");
                Log.v("chad",MEMO_TIME);
                //地址
                String address = ob1.getString("address");
                Log.v("chad",address);
                //簡介
                String xbody = ob1.getString("xbody");
                Log.v("chad",xbody);
                //緯度
                String lat = ob1.getString("lat");
                Log.v("chad",lat);
                //經度
                String lng = ob1.getString("lng");
                Log.v("chad",lng);


                JSONArray imgs =ob1.getJSONArray("Img");
                    for(int y= 0;y<imgs.length();y++){
                        String description =imgs.getJSONObject(y).getString("description");
                        Log.v("chad",description);
                        String imgUrl = imgs.getJSONObject(y).getString("url");
                        Log.v("chad",imgUrl);
                    }


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 取得餐廳資訊
     */

    private void getRest(){

        String url ="http://36.235.38.228:8080/fsit04/restaruant";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseRest(response);

                    }
                }, null);

        queue.add(stringRequest);
    }

    /**
     *  解析餐廳資訊
     * @param response
     */
    private void parseRest(String response){
        try {
            JSONArray array1 = new JSONArray(response);
            Log.v("chad",array1.length()+"");
            for(int i= 0;i<array1.length();i++) {
                JSONObject ob1 =array1.getJSONObject(i);
                //地點ID
                String total_id = ob1.getString("total_id");
                Log.v("chad",total_id);
                //地點名稱
                String stitle = ob1.getString("stitle");
                Log.v("chad",stitle);
                //地點類型
                String type= ob1.getString("type");
                Log.v("chad",type);
                //分類
                String CAT2 = ob1.getString("CAT2");
                Log.v("chad",CAT2);
                //營業時間
                String MEMO_TIME = ob1.getString("MEMO_TIME");
                Log.v("chad",MEMO_TIME);
                //地址
                String address = ob1.getString("address");
                Log.v("chad",address);
                //簡介
                String xbody = ob1.getString("xbody");
                Log.v("chad",xbody);
                //緯度
                String lat = ob1.getString("lat");
                Log.v("chad",lat);
                //經度
                String lng = ob1.getString("lng");
                Log.v("chad",lng);

                JSONArray imgs =ob1.getJSONArray("imgs");
                for(int y= 0;y<imgs.length();y++){
                    String description =imgs.getJSONObject(y).getString("description");
                    Log.v("chad",description);
                    String imgUrl = imgs.getJSONObject(y).getString("url");
                    Log.v("chad",imgUrl);
                }


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * http://36.235.38.228:8080/fsit04/Allviews?param=
     * @param param  搜尋字
     */
    private void doSearch(String param) {

        final String p1 =param;
        String url = String.format("http://36.235.38.228:8080/fsit04/Allviews");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseGetFavorite(response);

                    }
                }, null){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> m1 =new HashMap<>();

                m1.put("param",p1);


                return m1;
            }
        };

        queue.add(stringRequest);
    }

    /**
     * 上傳檔案用
     * @param
     *
     * / http://36.235.38.228:8080/fsit04/photo?user_id=1  傳完到這邊看有沒有成功
     */

    private void uploadFile() {
        String uploadUrl = "http://36.235.38.228:8080/fsit04/saveFile";
        final byte[] data ;
        //路徑上傳
//        File upload =new File(sdroot,"檔案的路徑");
//        data =filePathToByte(upload);


        //BitMap上傳
        Bitmap bmp = BitmapFactory.decodeResource(resources,R.drawable.test);
        data=bitmapToBytes(bmp);

        VolleyMultipartRequest multipartRequest =
                new VolleyMultipartRequest(
                        Request.Method.POST,
                        uploadUrl,
                        new Response.Listener<NetworkResponse>(){
                            @Override
                            public void onResponse(NetworkResponse response) {
                                Log.v("brad", "code: " + response.statusCode);
                            }
                        },
                        null){
                    @Override
                    protected Map<String, DataPart> getByteData()
                            throws AuthFailureError {

                        HashMap<String,DataPart> params = new HashMap<>();
                        //傳檔案
                        params.put("file",new DataPart("iii01.jpg", data));

                        return params;
                    }

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> m1 =new HashMap<>();
                        //使用者ID
                        m1.put("user_id","1");
                        //景點ID
                        m1.put("total_id","1");
                        //lat
                        m1.put("lat","25.00");
                        //lng
                        m1.put("lng","121.00");

                        return m1;
                    }
                };

        queue.add(multipartRequest);
    }

    /**
     *
     * @param file  檔案路徑轉BYTE 陣列
     * @return
     */
    private byte[] filePathToByte(File file){
        byte[] data = new byte[(int) file.length()];
        try {
            FileInputStream fin = new FileInputStream(file);
            fin.read(data);
            fin.close();
        }catch (Exception e){

        }

        return data;
    }
    /**
     *
     * @param bm    Bitmap 轉Byte[];
     * @return
     */
    private byte[] bitmapToBytes(Bitmap bm){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

}
