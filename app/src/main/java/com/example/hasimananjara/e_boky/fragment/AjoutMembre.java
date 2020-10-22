package com.example.hasimananjara.e_boky.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.hasimananjara.e_boky.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.net.ssl.HttpsURLConnection;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AjoutMembre.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AjoutMembre#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AjoutMembre extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TextView numLecteur;
    private TextView nomLecteur;
    private TextView prenomLecteur;
    private TextView addLecteur;
    private TextView indiceAdd ;
    private Button butAdd;
    private Button butAnn;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AjoutMembre() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AjoutMembre.
     */
    // TODO: Rename and change types and number of parameters
    public static AjoutMembre newInstance(String param1, String param2) {
        AjoutMembre fragment = new AjoutMembre();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       final   View v = inflater.inflate(R.layout.fragment_ajout_membre, container, false);

        butAdd = (Button) v.findViewById(R.id.buttonAjouterMemb);
        butAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vr) {
                indiceAdd = (TextView)  v.findViewById(R.id.indication);
                numLecteur = (TextView) v.findViewById(R.id.num_lecteur);
                nomLecteur = (TextView) v.findViewById(R.id.nom_lecteur);
                prenomLecteur = (TextView) v.findViewById(R.id.prenom_lecteur);
                addLecteur = (TextView) v.findViewById(R.id.adresse_lecteur);
                new AjouterMembre().execute("http://10.0.2.1:8080/GBibliothecaire/webresources/entity_lecteur");

            }
        });
        butAnn = (Button) v.findViewById(R.id.buttonAnnuler);
        butAnn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AjoutMembre ajoutMembre = new AjoutMembre();


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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class AjouterMembre extends AsyncTask<String, Void, Void> {

        // Required initialization

        private final HttpClient Client = new DefaultHttpClient();
        private String Content;
        private String Error = null;
        private int ok  ;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        // Call after onPreExecute method
        @Override
        protected Void doInBackground(String... urls) {

            Content = requestContent(urls[0]);
            return null;
        }
        @Override
        protected void onPostExecute(Void unused) {
            if(ok != 500){
            if(Content.equals("ko")){
                indiceAdd.setError("erreur d'ajout");
            }
            else{
                indiceAdd.setTextColor(Color.GREEN);
                indiceAdd.setText("membre ajouté");
            }

            }
            else{
                indiceAdd.setTextColor(Color.RED);
                indiceAdd.setText("Ce membre existe dejà");

            }
        }

        public String requestContent(String url) {
            InputStream inputStream = null;
            String result = "";

            try {
                // 1. create HttpClient
                HttpClient httpclient = new DefaultHttpClient();
                // 2. make POST request to the given URL

                HttpPost httpPut = new HttpPost(url);
                String json = "";
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("numLecteur", numLecteur.getText());
                jsonObject.put("nom", nomLecteur.getText());
                jsonObject.put("prenom", prenomLecteur.getText());
                jsonObject.put("adresse", addLecteur.getText());
                json = jsonObject.toString();
                StringEntity se = new StringEntity(json);
                // 6. set httpPost Entity
                httpPut.setEntity(se);
                // 7. Set some headers to inform server about the type of the content
                httpPut.addHeader("Accept", "application/json");
                httpPut.addHeader("Content-type", "application/json");
                // 8. Execute POST request to the given URL
                //recuperer les resultats URL
                HttpResponse httpResponse = httpclient.execute(httpPut);
                ok = httpResponse.getStatusLine().getStatusCode();


                //Try to add this
                inputStream = httpResponse.getEntity().getContent();

                if (inputStream != null)
                    result = convertStreamToString(inputStream);
                else
                    result = "ko";

            } catch (Exception e) {
                //Log.d("InputStream", e.getLocalizedMessage());
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

    }
}
