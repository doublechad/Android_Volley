package tw.org.iii.testlogin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        queue= Volley.newRequestQueue(this);
    }

   // http://localhost:8080/fsit04/Views_message?total_id=10
    public void login(View view) {
        sighin("test123@gmail.com","test123");
    }

    public void add(View view) {
        addFavorite("1","3");
    }
    public void delect(View view) {
        deleteFavorite("1","3");
    }
    public void addMsg(View view) {
        addMessage("測試員01","10","安安你好");
    }
    public void getMsgByID(View view) {
        getMessageByID("10");
    }

    /**
     * http://36.234.10.186:8080/fsit04/Views_message?total_id=123
     * @param total_id 景點ID
     *
     */
    private void getMessageByID(String total_id) {
        String url ="http://36.234.10.186:8080/fsit04/Views_message?total_id="+total_id;

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
     * http://36.234.10.186 :8080/fsit04/Views_message
     * @param user_name   使用者id
     * @param msg      留言
     * @param total_id 景點id
     */
    private void addMessage(String user_name,String total_id,String msg) {
        String url ="http://36.234.10.186:8080/fsit04/Views_message";
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

    public void select(View view) {
        getFavorite("1");
    }
    /**  http://36.234.10.186:8080/J2EE/sighin.jsp  登入
     *
     * @param mail        信箱 test123@gmail.com
     * @param password    密碼 test123
     */
    private void sighin(String mail,String password){
        final String p1=mail;
        final String p2=password;
        String url ="http://36.234.10.186:8080/fsit04/sighin.jsp";
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
                    return m1;
                }
            };


        queue.add(stringRequest);
    }

    /**  http://36.234.10.186 :8080/J2EE/addFavorite.jsp  加入我的最愛
     *
     * @param user_id     用戶id
     * @param total_id   地點的id
     */
    private void addFavorite(String user_id,String total_id){
        String url ="http://36.234.10.186:8080/fsit04/User_favorite";

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
    /**  http://36.234.10.186 :8080/J2EE/addFavorite.jsp  刪除我的最愛
     *
     * @param user_id     用戶id
     * @param total_id   地點的id
     */
    private void deleteFavorite(String user_id,String total_id){
        String url ="http://36.234.10.186:8080/fsit04/User_favorite";

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
     *  http://36.234.10.186:8080/fsit04/User_favorite
     * @param user_id 用戶id
     */
    private void getFavorite(String user_id){
        final String p1=user_id;
        String url ="http://36.234.10.186:8080/fsit04/User_favorite?user_id="+p1;
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
            for(int i= 0;i<array1.length();i++) {
                JSONObject ob1 =array1.getJSONObject(i);
                String total_id = ob1.getString("total_id");
                Log.v("chad",total_id);
                String name = ob1.getString("name");
                Log.v("chad",name);

                String type= ob1.getString("type");
                Log.v("chad",type);

                String CAT2 = ob1.getString("CAT2");
                Log.v("chad",CAT2);

                String MEMO_TIME = ob1.getString("MEMO_TIME");
                Log.v("chad",MEMO_TIME);

                String address = ob1.getString("address");
                Log.v("chad",address);

                String xbody = ob1.getString("xbody");
                Log.v("chad",xbody);

                String lat = ob1.getString("lat");
                Log.v("chad",lat);

                String lng = ob1.getString("lng");
                Log.v("chad",lng);


                JSONArray imgs =ob1.getJSONArray("Img");
                    for(int y= 0;y<array1.length();y++){
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

    private void sendSpring(){

            final String p1="999";
            String url ="http://36.234.10.186:8080/MySpringMVC2/testRest/"+p1;
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
                    m1.put("_method","put");
                    return m1;
                }
            };

            queue.add(stringRequest);
        }


    public void springTEST(View view) {
        sendSpring();
    }



}
