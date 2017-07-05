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

import static com.nucleuslife.restaurantreview.Constants.KEY_UNAVAILABLE;


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
        Log.i("paramsam", "params: " + params[0]);
        Request.Builder builder = new Request.Builder();
        builder.url(params[0]);
        Request request = builder.build();

        try {
            Response response = this.client.newCall(request).execute();

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

        String response = responseString;

        if (response != null) {
            try {
                JSONArray citationArray = new JSONArray(response);

                for (int i = 0; i < citationArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) citationArray.get(i);
                    String string = jsonObject.toString();
                    Log.i("responseSam", string);

//                    String action = jsonObject.get("action").toString();
                    String criticalFlag = jsonObject.has("critical_flag") ? jsonObject.get("critical_flag").toString() : KEY_UNAVAILABLE;
                    String inspectionDate = jsonObject.has("inspection_date") ? jsonObject.get("inspection_date").toString() : KEY_UNAVAILABLE;
                    String inspectionDType = jsonObject.has("inspection_type") ? jsonObject.get("inspection_type").toString() : KEY_UNAVAILABLE;
                    String grade = jsonObject.has("grade") ? jsonObject.get("grade").toString() : KEY_UNAVAILABLE;
                    String violationCode = jsonObject.has("violation_code") ? jsonObject.get("violation_code").toString() : KEY_UNAVAILABLE;
                    String violationDescription = jsonObject.has("violation_description") ? jsonObject.get("violation_description").toString() : KEY_UNAVAILABLE;


                    Citation citation = new Citation(criticalFlag, inspectionDType, inspectionDate, grade, violationCode, violationDescription);
                    citationArrayList.add(citation);
                }

            } catch (JSONException e) {
                e.printStackTrace();

                if (this.citationCallback != null) {
                    this.citationCallback.onCitationFailure();
                }
            }
        }

        Log.i("testlist", "setCitation");
        this.customBusiness.setCitations(citationArrayList);

        if (this.citationCallback != null)  {
            this.citationCallback.onCitationSuccess(this.customBusiness);
        }

    }

    public interface CitationCallback
    {
        void onCitationSuccess(CustomBusiness customBusiness);
        void onCitationFailure();

    }
}
