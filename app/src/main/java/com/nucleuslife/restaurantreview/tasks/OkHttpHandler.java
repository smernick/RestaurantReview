package com.nucleuslife.restaurantreview.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.nucleuslife.restaurantreview.structures.Citation;
import com.nucleuslife.restaurantreview.structures.CustomBusiness;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class OkHttpHandler extends AsyncTask<String, String, String>
{
    private OkHttpClient client = new OkHttpClient();
    private CitationCallback citationCallback;
    private CustomBusiness customBusiness;

    public OkHttpHandler(CitationCallback citationCallback, CustomBusiness customBusiness)
    {
        this.citationCallback = citationCallback;
        this.customBusiness = customBusiness;
    }

    @Override
    protected String doInBackground(String... params)
    {
        Request.Builder builder = new Request.Builder();
        builder.url(params[0]);
        Request request = builder.build();

        try {
            Response response = this.client.newCall(request).execute();


            Log.i("responsesam", "responseHappening");

            return response.body().string();


        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String responseString)
    {
        super.onPostExecute(responseString);
        ArrayList<Citation> citationArrayList = new ArrayList<>();

        try {
            JSONArray citationArray = new JSONArray(responseString) ;

//            if (citationArray == null) {
//                if (this.citationCallback != null)  {
//                    this.citationCallback.onCitationFailure();
//                }
//                return;
//            }
//
            for (int i = 0; i < citationArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) citationArray.get(i);
                String string = jsonObject.toString();
                Log.i("responseSam", string);

                String action = jsonObject.get("action").toString();
                String criticalFlag = jsonObject.get("critical_flag").toString();
                String inspectionDate = jsonObject.get("inspection_date").toString();
                String inspectionDType = jsonObject.get("inspection_type").toString();
                String score = jsonObject.get("score").toString();
                String violationCode = jsonObject.get("violation_code").toString();
                String violationDescription = jsonObject.get("violation_description").toString();

                Citation citation = new Citation(criticalFlag, inspectionDType, inspectionDate, score, violationCode, violationDescription);
                citationArrayList.add(citation);
            }

            this.customBusiness.setCitations(citationArrayList);

            if (this.citationCallback != null)  {
                this.citationCallback.onCitationSuccess(this.customBusiness);
            }

        } catch (JSONException e) {
            e.printStackTrace();

            if (this.citationCallback != null)  {
                this.citationCallback.onCitationFailure();
            }
        }

    }

    public interface CitationCallback
    {
        void onCitationSuccess(CustomBusiness customBusiness);
        void onCitationFailure();

    }
}
