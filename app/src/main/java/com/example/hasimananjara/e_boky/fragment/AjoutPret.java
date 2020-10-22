package com.example.hasimananjara.e_boky.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.hasimananjara.e_boky.R;
import com.example.hasimananjara.e_boky.classy.Lecteur;
import com.example.hasimananjara.e_boky.classy.Livre;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AjoutPret.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AjoutPret#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AjoutPret extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Spinner spinlivre;
    private Spinner spinLecteur;
    public  Spinner spinli;
    public  Spinner spinLe;
    private  TextView indiceAdd;
    private  TextView date ;
    private Button butAdd ;
    private Button butAnn;
    private static Context ct;

    private OnFragmentInteractionListener mListener;

    public AjoutPret() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AjoutPret.
     */
    // TODO: Rename and change types and number of parameters
    public static AjoutPret newInstance(String param1, String param2) {
        AjoutPret fragment = new AjoutPret();
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

        final View v = inflater.inflate(R.layout.fragment_ajout_pret, container, false);
        ct = v.getContext();
        spinLecteur = (Spinner) v.findViewById(R.id.numLect);
        spinlivre = (Spinner)  v.findViewById(R.id.numliv);
        date = (TextView) v.findViewById(R.id.datePret);
        indiceAdd = (TextView) v.findViewById(R.id.indicationT);

        new ListerEmploye().execute("http://10.0.2.1:8080/GBibliothecaire/webresources/entity_livre");
        new ListerEntreprise().execute("http://10.0.2.1:8080/GBibliothecaire/webresources/entity_lecteur");

        butAdd = (Button) v.findViewById(R.id.buttonAjouterP);
        butAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vr) {
                indiceAdd = (TextView)v.findViewById(R.id.indicationT);

                new AjouterPret().execute("http://10.0.2.1:8080/GBibliothecaire/webresources/entity_pret");

            }
        });
        butAnn = (Button) v.findViewById(R.id.buttonAnnulerP);
        butAnn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AjoutMembre ajoutMembre = new AjoutMembre();


            }
        });

        return  v ;
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

    private class ListerEmploye  extends AsyncTask<String, Void, Void> {

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

        public String convertStreamToString(InputStream is) throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = null;

            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
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
            ArrayList<Livre> liv = new ArrayList<Livre>();
            ArrayList<String> employ = new ArrayList<>();
            try {
                Object object = null;
                if(Content != null) {
                    JSONArray jsonArray = new JSONArray(Content);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject LivreObject = jsonArray.getJSONObject(i);
                        Livre livre = new Livre(LivreObject.getString("numLivre"));

                        liv.add(livre);
                        employ.add(livre.getNumLivre());
                        Log.d("kivy", "" + employ.size());
                    }
                     ArrayAdapter<String> adapter = new ArrayAdapter<String>(ct,android.R.layout.simple_spinner_item,employ);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinlivre.setAdapter(adapter);
                }

            } catch (JSONException e) {
            }

        }

    }

    private class ListerEntreprise  extends AsyncTask<String, Void, Void> {

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
            ArrayList<Lecteur> ent = new ArrayList<Lecteur>();
            ArrayList<String> entrep = new ArrayList<>();
            try {
                Object object = null;
                if (Content != null) {
                    JSONArray jsonArray = new JSONArray(Content);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject EntrepriseObject = jsonArray.getJSONObject(i);
                        Lecteur entre = new Lecteur(EntrepriseObject.getString("numLecteur"));

                        ent.add(entre);
                        entrep.add(entre.getNumLecteur());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(ct,android.R.layout.simple_spinner_item,entrep);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinLecteur.setAdapter(adapter);
                }
            }
            catch (JSONException e) {
            }
        }

    }
    public class AjouterPret  extends AsyncTask<String, Void, Void> {

        // Required initialization

        private final HttpClient Client = new DefaultHttpClient();
        private String Content;
        private String Error = null;
        private int ok;

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

        public String requestContent (String url){
            InputStream inputStream = null;
            String result = "";

            try {
                // 1. create HttpClient
                HttpClient httpclient = new DefaultHttpClient();
                // 2. make POST request to the given URL

                HttpPost httpPut = new HttpPost(url);
                String json = "";

                JSONObject jsonObject = new JSONObject();

                String txt = date.getText().toString();
                Log.d("AMEN",txt);
                jsonObject.put("datepret",txt);

                JSONObject jsonObjectLivre = new JSONObject();
                jsonObjectLivre.put("numLivre",""+spinlivre.getSelectedItem().toString());

                JSONObject jsonObjectLecteur = new JSONObject();
                jsonObjectLecteur.put("numLecteur",""+spinLecteur.getSelectedItem().toString());

                JSONObject jsonObjectPK = new JSONObject();
                jsonObjectPK.put("numLivre",""+spinlivre.getSelectedItem().toString());
                jsonObjectPK.put("numLecteur",""+spinLecteur.getSelectedItem().toString());

                jsonObject.put("lecteur",jsonObjectLecteur);
                jsonObject.put("livre",jsonObjectLivre);
                jsonObject.put("pretPK",jsonObjectPK);


                json = jsonObject.toString();
                StringEntity se = new StringEntity(json);
                // 6. set httpPost Entity
                httpPut.setEntity(se);
                // 7. Set some headers to inform server about the type of the content
                httpPut.addHeader("Accept", "application/json");
                httpPut.addHeader("Content-type", "application/json");
                // 8. Execute POST request to the given URL
                HttpResponse httpResponse = httpclient.execute(httpPut);

                ok = httpResponse.getStatusLine().getStatusCode();

                //Try to add this
                inputStream = httpResponse.getEntity().getContent();

                if(inputStream != null)
                    result = convertStreamToString(inputStream);
                else
                    result = "ko";

            }
            catch (Exception e) {

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
            if(ok != 500){
                if(Content.equals("ko")){
                    indiceAdd.setError("erreur d'ajout");
                }
                else{
                    indiceAdd.setTextColor(Color.GREEN);
                    indiceAdd.setText("Ajout avec succes ");
                }
            }
            else{
                indiceAdd.setTextColor(Color.RED);
                indiceAdd.setText("Ce livre n'est pas disponible");
            }
        }

    }
}
