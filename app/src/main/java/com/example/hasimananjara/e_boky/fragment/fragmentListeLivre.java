package com.example.hasimananjara.e_boky.fragment;

import android.content.Context;
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

import com.example.hasimananjara.e_boky.R;
import com.example.hasimananjara.e_boky.adapter.LecteurAdapter;
import com.example.hasimananjara.e_boky.adapter.LivreAdapter;
import com.example.hasimananjara.e_boky.classy.Lecteur;
import com.example.hasimananjara.e_boky.classy.Livre;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
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
 * {@link fragmentListeLivre.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link fragmentListeLivre#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragmentListeLivre extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private fragmentListeLivre.OnFragmentInteractionListener LListener;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private LivreAdapter livreAdapter;
    private String serverURL = "http://10.0.2.1:8080/GBibliothecaire/webresources/entity_livre";

    private OnFragmentInteractionListener mListener;

    public fragmentListeLivre() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragmentListeLivre.
     */
    // TODO: Rename and change types and number of parameters
    public static fragmentListeLivre newInstance(String param1, String param2) {
        fragmentListeLivre fragment = new fragmentListeLivre();
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
        View v = inflater.inflate(R.layout.fragment_fragment_liste_livre, container, false);
        // Inflate the layout for this fragment
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerLiv);
        layoutManager = new LinearLayoutManager(this.getActivity());

        recyclerView.setLayoutManager(layoutManager);
        ArrayList<Livre> liv = new ArrayList<Livre>();
        new LongOperation().execute(serverURL);
        livreAdapter = new LivreAdapter(liv);
        recyclerView.setAdapter(livreAdapter);
        return v;
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (LListener != null) {
            LListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            LListener = (OnFragmentInteractionListener) context;
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

            ArrayList<Livre> liv = new ArrayList<Livre>();
            try {
                Object object = null;
                if(Content != null) {
                    JSONArray jsonArray = new JSONArray(Content);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject livreObject = jsonArray.getJSONObject(i);
                        Livre livr = new Livre(livreObject.getString("numLivre").toString(),
                                livreObject.getString("disponible").toString(),
                                livreObject.getString("designation").toString());
                        liv.add(livr);

                    }

                }
                else {
                    Log.d("Test","LEO");
                }


            } catch (JSONException e) {
                Log.d("test",e.getMessage());
            }

            updateUI(liv);

        }

    }

    private void updateUI(ArrayList<Livre>livre){
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        livreAdapter = new LivreAdapter(livre);
        recyclerView.setAdapter(livreAdapter);
        Log.d("UpdateUi","Executed");
    }

}
