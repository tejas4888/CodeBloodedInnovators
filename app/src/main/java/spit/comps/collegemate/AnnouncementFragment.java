package spit.comps.collegemate;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import spit.comps.collegemate.HelperClasses.AppConstants;
import spit.comps.collegemate.HelperClasses.HttpHandler;
import spit.comps.collegemate.HelperClasses.RequestHandler;
import spit.comps.collegemate.Items.ProjectType1Item;
import spit.comps.collegemate.RecyclerAdapter.AnnouncementRecyclerAdapter;
import spit.comps.collegemate.RecyclerAdapter.ProjectType1RecyclerAdapter;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class AnnouncementFragment extends Fragment {


    String JSON_NEWS_STRING="";
    public AnnouncementFragment() {
        // Required empty public constructor
    }

    ArrayList<AnnouncementItem> items;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_announcement, container, false);

        items=new ArrayList<>();
        recyclerView=(RecyclerView)view.findViewById(R.id.fragment_announcement_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        new GetProjectList().execute();

        return view;
    }


    private class GetProjectList extends AsyncTask<Void,Void,String>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setMessage("Fetching data");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            HashMap<String,String> args = new HashMap<>();
            args.put("email","mithil@gmail.com");
            RequestHandler sh = new RequestHandler();

            String jsonStr = sh.sendPostRequest(AppConstants.get_announcement_list,args);
            return jsonStr;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (progressDialog.isShowing())
            {
                progressDialog.hide();
            }

            JSON_NEWS_STRING = s;

            //Toast.makeText(getActivity(), "H:"+JSON_NEWS_STRING, Toast.LENGTH_SHORT).show();

            JSONObject jsonObject = null;
            String jsonstring1="";
            try {

                jsonObject = new JSONObject(JSON_NEWS_STRING);
                jsonstring1 = (String) jsonObject.get("data");



            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Toast.makeText(getActivity(), "A:"+jsonstring1, Toast.LENGTH_SHORT).show();

            int status = readNews(jsonstring1);

            if (status==1)
            {

            }
            else
            {

            }

            recyclerView.setAdapter(new AnnouncementRecyclerAdapter(items,getActivity()));
        }
    }

    private int readNews(String JSON_NEWS_STRING){

        try {
            JSONArray result = new JSONArray(JSON_NEWS_STRING);

            //String jsonstring1 = (String) jsonObject.get("data");
            //JSONArray result = new JSONArray(jsonstring1);

            for(int i=0;i<result.length();i++)
            {
                JSONObject js = result.getJSONObject(i);

                JSONObject c = js.getJSONObject("fields");

                String date = c.getString("date");
                String time = c.getString("time");
                String title = c.getString("title");
                String importance = c.getString("importance");
                String description = c.getString("description");
                String link = c.getString("link");

                AnnouncementItem object = new AnnouncementItem(String.valueOf(i+1),date,time,title,importance,description,link);
                items.add(object);
            }
            //Toast.makeText(getActivity(), "No.:"+items.size(), Toast.LENGTH_SHORT).show();
            return 1;

        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }
    }



    /*
    private class getAnnouncementList extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setMessage("Fetching data");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            SharedPreferences prefs = getActivity().getSharedPreferences(AppConstants.LOGIN_PREFS, MODE_PRIVATE);

            HashMap<String,String> args = new HashMap<>();
            args.put("email", prefs.getString("email", ""));

            RequestHandler rh = new RequestHandler();

            String jsonStr = rh.sendPostRequest(AppConstants.get_announcement_list, args);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONArray feeds = jsonObj.getJSONArray("result");

                    for (int i = 0; i < feeds.length(); i++) {
                        JSONObject c = feeds.getJSONObject(i);

                        String alert_id = c.getString("alert_id");
                        String date = c.getString("date");
                        String time = c.getString("time");
                        String title = c.getString("title");
                        String importance = c.getString("importance");
                        String description = c.getString("description");
                        String link = c.getString("link");

                        AnnouncementItem object = new AnnouncementItem(alert_id,date,time,title,importance,description,link);
                        items.add(object);
                    }

                } catch (final JSONException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(),"Couldn't get data from Server. Please try again later", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),"No Internet Connection",Toast.LENGTH_LONG).show();
                    }
                });

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (progressDialog.isShowing())
            {
                progressDialog.hide();
            }
            recyclerView.setAdapter(new AnnouncementRecyclerAdapter(items,getActivity()));
        }
    }
    */

}
