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
import com.example.hasimananjara.e_boky.classy.Lecteur;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link listeLecteur.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link listeLecteur#newInstance} factory method to
 * create an instance of this fragment.
 */
public class listeLecteur extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private LecteurAdapter lecteurAdapter;
    private String serverURL = "http://10.0.2.1:8080/GBibliothecaire/webresources/entity_lecteur";

    public listeLecteur() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment listeLecteur.
     */
    // TODO: Rename and change types and number of parameters
    public static listeLecteur newInstance(String param1, String param2) {
        listeLecteur fragment = new listeLecteur();
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
        View v = inflater.inflate(R.layout.fragment_liste_lecteur, container, false);
        // Inflate the layout for this fragment
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerEmp);
        layoutManager = new LinearLayoutManager(this.getActivity());

        recyclerView.setLayoutManager(layoutManager);
        ArrayList<Lecteur> lect = new ArrayList<Lecteur>();
        new LongOperation().execute(serverURL);
        lecteurAdapter = new LecteurAdapter(lect);
        recyclerView.setAdapter(lecteurAdapter);
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
            ArrayList<Lecteur> lect = new ArrayList<Lecteur>();
            try {
                Object object = null;
                if(Content != null) {
                    JSONArray jsonArray = new JSONArray(Content);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject lecteurObject = jsonArray.getJSONObject(i);
                        Lecteur lecteur = new Lecteur(lecteurObject.getString("numLecteur"),
                                lecteurObject.getString("nom"),
                                lecteurObject.getString("prenom"));
                        lect.add(lecteur);
                    }
                }
                else {
                    Log.d("Test","LEO");
                }


            } catch (JSONException e) {
            }
            updateUI(lect);
        }

    }

    private void updateUI(ArrayList<Lecteur> lecteur){
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        lecteurAdapter = new LecteurAdapter(lecteur);
        recyclerView.setAdapter(lecteurAdapter);
        Log.d("UpdateUi","Executed");
    }
}
