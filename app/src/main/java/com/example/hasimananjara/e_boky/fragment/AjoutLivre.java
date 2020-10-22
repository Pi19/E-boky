package com.example.hasimananjara.e_boky.fragment;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AjoutLivre.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AjoutLivre#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AjoutLivre extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView numLivre;
    private TextView auteur;
    private TextView designation;
    private TextView date;
    private TextView indiceAdd ;
    private TextView dispo ;
    private Button butAdd;
    private Button butAnn;
    private OnFragmentInteractionListener mListener;

    public AjoutLivre() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AjoutLivre.
     */
    // TODO: Rename and change types and number of parameters
    public static AjoutLivre newInstance(String param1, String param2) {
        AjoutLivre fragment = new AjoutLivre();
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

        final   View v = inflater.inflate(R.layout.fragment_ajout_livre, container, false);

        butAdd = (Button) v.findViewById(R.id.buttonAjouterLivre);
        butAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vr) {
                indiceAdd = (TextView)  v.findViewById(R.id.indication);
                numLivre = (TextView) v.findViewById(R.id.num_livre);
                auteur = (TextView) v.findViewById(R.id.auteur);
                designation = (TextView) v.findViewById(R.id.designation);
                date = (TextView) v.findViewById(R.id.Edition);
                new AjouterLivre().execute("http://10.0.2.1:8080/GBibliothecaire/webresources/entity_livre");
            }
        });
        butAnn = (Button) v.findViewById(R.id.buttonAnnulerLivre);
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
    private class AjouterLivre extends AsyncTask<String, Void, Void> {

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
        @Override
        protected void onPostExecute(Void unused) {
            if(Content.equals("ko")){
                indiceAdd.setError("erreur d'ajout");
            }
            else{
                indiceAdd.setTextColor(Color.GREEN);
                indiceAdd.setText("livre ajouté");
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
                jsonObject.put("numLivre", numLivre.getText());
                jsonObject.put("auteur", auteur.getText());
                jsonObject.put("designation", designation.getText());
                jsonObject.put("dateedition ", date.getText());



                json = jsonObject.toString();
                StringEntity se = new StringEntity(json);
                // 6. set httpPost Entity
                httpPut.setEntity(se);
                // 7. Set some headers to inform server about the type of the content
                httpPut.addHeader("Accept", "application/json");
                httpPut.addHeader("Content-type", "application/json");
                // 8. Execute POST request to the given URL
                HttpResponse httpResponse = httpclient.execute(httpPut);

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
