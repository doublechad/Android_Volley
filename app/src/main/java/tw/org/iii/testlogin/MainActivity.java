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


    public void login(View view) {
        sighin("test123@gmail.com","test123");
    }

    public void add(View view) {
        String[] positions = new String[]{"10","11","99"};
        addFavorite("1",positions);
    }

    public void select(View view) {
        getFavorite("1");
    }
    /**  http://36.234.13.158:8080/J2EE/sighin.jsp  登入
     *
     * @param mail        信箱 test123@gmail.com
     * @param password    密碼 test123
     */
    private void sighin(String mail,String password){
        final String p1=mail;
        final String p2=password;
        String url ="http://36.234.13.158:8080/J2EE/sighin.jsp";
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

    /**  http://36.234.13.158:8080/J2EE/addFavorite.jsp  加入我的最愛
     *
     * @param user_id     用戶id
     * @param total_ids   地點的id
     */
    private void addFavorite(String user_id,String[] total_ids){
        String url ="http://36.234.13.158:8080/J2EE/addFavorite.jsp";

        final String p1 =user_id;
        final String[] p2=total_ids;
        try {
            final JSONArray array = new JSONArray(p2);

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
                    m1.put("total_ids", array.toString());
                    return m1;
                }
            };


            queue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /** http://36.234.13.158:8080/J2EE/getFavorite.jsp 取得用戶我的最愛
     *
     * @param user_id 用戶id
     */
    private void getFavorite(String user_id){
        final String p1=user_id;
        String url ="http://36.234.13.158:8080/J2EE/getFavorite.jsp";
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
                m1.put("mail",p1);
                return m1;
            }
        };

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



}
