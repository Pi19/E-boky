package com.example.hasimananjara.e_boky.fragment;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.example.hasimananjara.e_boky.adapter.LecteurAdapter;
import com.example.hasimananjara.e_boky.adapter.PretAdapter;

import com.example.hasimananjara.e_boky.R;
import com.example.hasimananjara.e_boky.classy.Lecteur;
import com.example.hasimananjara.e_boky.classy.Livre;
import com.example.hasimananjara.e_boky.classy.Pret;
import com.example.hasimananjara.e_boky.classy.PretPk;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link liste_pret.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link liste_pret#newInstance} factory method to
 * create an instance of this fragment.
 */
public class liste_pret extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context ct ;
    private ImageButton butAdd;
    private fragmentListeLivre.OnFragmentInteractionListener LListener;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private PretAdapter PretAdapter;
    private String serverURL = "http://10.0.2.1:8080/GBibliothecaire/webresources/entity_pret";

    private OnFragmentInteractionListener mListener;

    public liste_pret() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment liste_pret.
     */
    // TODO: Rename and change types and number of parameters
    public static liste_pret newInstance(String param1, String param2) {
        liste_pret fragment = new liste_pret();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_liste_pret, container, false);
        ct = v.getContext();

        // Inflate the layout for this fragment
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerPret);
        layoutManager = new LinearLayoutManager(this.getActivity());

        recyclerView.setLayoutManager(layoutManager);
        ArrayList<PretPk> pret = new ArrayList<PretPk>();
        new liste_pret.LongOperation().execute(serverURL);
        PretAdapter = new PretAdapter(pret);
        recyclerView.setAdapter(PretAdapter);


        butAdd = (ImageButton) v.findViewById(R.id.Pdf);
        butAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vr) {

                    generatePDF();
            }
        });
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    private class LongOperation  extends AsyncTask<String, Void, Void> {

        // Required initialization

        private final HttpClient Client = new DefaultHttpClient();
        private String Content;
        private String Error = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        // Call after onPreExecute method
        @Override
        protected Void doInBackground(String... urls) {

            Content = requestContent(urls[0]);
            Log.d("Merci", Content);
            return null;
        }
        public String requestContent (String url){
            HttpClient httpclient = new DefaultHttpClient();
            String result = null;
            HttpGet httpget = new HttpGet(url);
            HttpResponse response = null;
            InputStream instream = null;

            try {
                response = httpclient.execute(httpget);
                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    instream = entity.getContent();
                    result = convertStreamToString(instream);
                }

            } catch (Exception e) {
                // manage exceptions
            } finally {
                if (instream != null) {
                    try {
                        instream.close();
                    } catch (Exception exc) {

                    }
                }
            }

            return result;
        }

        public String convertStreamToString(InputStream is) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = null;

            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }

            return sb.toString();
        }

        @Override
        protected void onPostExecute(Void unused) {

            ArrayList<PretPk> Pret = new ArrayList<PretPk>();
            try {
                Object object = null;
                if(Content != null) {
                    JSONArray jsonArray = new JSONArray(Content);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject pretObject = jsonArray.getJSONObject(i);
                        //JSONObject pretPkObject = jsonArray.getJSONObject(3);
                        String livre = pretObject.getString("livre");
                        String membre = pretObject.getString("lecteur");
                        JSONObject lvr = new JSONObject(livre);
                        JSONObject mb = new JSONObject(membre);

                        Livre lv = new Livre(lvr.getString("numLivre"));
                        Lecteur lect = new Lecteur(mb.getString("numLecteur"));

                        PretPk pk = new PretPk(pretObject.getString("datepret"),lv,lect);

                        Pret.add(pk);





                    }

                }
                else {
                    Log.d("Test","LEO");
                }


            } catch (JSONException e) {
                Log.d("test",e.getMessage());
            }

            updateUI(Pret);

        }

    }

    private void updateUI(ArrayList<PretPk> livre){
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        PretAdapter = new PretAdapter(livre);
        recyclerView.setAdapter(PretAdapter);
        Log.d("UpdateUi","Executed");
    }

    public void generatePDF(){

        PdfDocument document = new PdfDocument();
        //create page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(200,300,1).create();
        //start a page
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(5);
        Paint paint1 = new Paint();
        paint1.setColor(Color.GREEN);
        paint1.setTextSize(5);
        Paint paint2 = new Paint();
        paint2.setColor(Color.WHITE);
        paint2.setTextSize(5);
        paint2.setFakeBoldText(true);
        canvas.drawText("Liste Des Pret",50,20,paint);
        canvas.drawLine(20,30,180,30,paint);
        //colonne
        //int siz = employepdf.size();
       /* canvas.drawRect(20,30,180,40,paint1);
        canvas.drawLine(20,30,20,30+10*(siz+1),paint);
        canvas.drawLine(50,30,50,30+10*(siz+1),paint);
        canvas.drawLine(120,30,120,30+10*(siz+1),paint);
        canvas.drawLine(180,30,180,30+10*(siz+1),paint);*/

        canvas.drawText("Numero livre",25,35,paint2);
        canvas.drawText("Lecteur",70,35,paint2);
        canvas.drawText("Date Pret",140,35,paint2);
        canvas.drawLine(20,40,180,40,paint);


        int y = 40;
       /* for(int i=0;i<employepdf.size();i++){
            canvas.drawText(employepdf.get(i).getNum(),25,y+5,paint);
            canvas.drawText(employepdf.get(i).getNom(),55,y+5,paint);
            canvas.drawText(String.valueOf(employepdf.get(i).getSalaire()),125,y+5,paint);
            canvas.drawLine(20,y+10,180,y+10,paint);
            y+=10;
        }*/

        //canvas.drawText("Salaire global des employés : "+totalSalaire,25,y+20,paint);
        // finish the page
        document.finishPage(page);

        String targetPDF = "/sdcard/pret.pdf";
        File filePath = new File(targetPDF);
        try{
            document.writeTo(new FileOutputStream(filePath));
            Toast.makeText(ct,"PDF created successfully",Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(ct,"Something Wrong",Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(ct,"PDF created successfully",Toast.LENGTH_LONG).show();
        }

        //write the document content
        //document.writeTo();

    }

}
