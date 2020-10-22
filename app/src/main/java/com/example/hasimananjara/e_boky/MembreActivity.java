package com.example.hasimananjara.e_boky;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.hasimananjara.e_boky.adapter.LecteurAdapter;
import com.example.hasimananjara.e_boky.classy.Lecteur;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
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

public class MembreActivity extends AppCompatActivity {
    private String num = null;
    private String Url = "http://10.0.2.1:8080/GBibliothecaire/webresources/entity_lecteur/";


    private TextView numLecteur;
    private TextView nomLecteur;
    private TextView prenomLecteur;
    private TextView addLecteur;

    private TextView numLecteurd;
    private TextView nomLecteurd;
    private TextView prenomLecteurd;
    private TextView addLecteurd;
    private Button butModif ;
    private Button butDel ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membre);

        Intent i = getIntent();
        num = i.getStringExtra("NUM");
        Log.d("leo",num);
        new LongOperation().execute(Url+num);

        numLecteur = (TextView) findViewById(R.id.numero);
        nomLecteur = (TextView) findViewById(R.id.nom);
        addLecteur = (TextView) findViewById(R.id.adresse);
        prenomLecteur = (TextView) findViewById(R.id.prenom);


        butModif =(Button) findViewById(R.id.buttonModiflect);
        butModif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MembreActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.modif_lecteur, null);

                numLecteurd = (TextView) mView.findViewById(R.id.num_Lecteurd);
                nomLecteurd = (TextView) mView.findViewById(R.id.nomd);
                addLecteurd = (TextView) mView.findViewById(R.id.addLecteur);
                prenomLecteurd = (TextView) mView.findViewById(R.id.prenomd);


                //assignation text
                numLecteurd.setText(numLecteur.getText());
                nomLecteurd.setText(nomLecteur.getText());
                addLecteurd.setText(addLecteur.getText());
                prenomLecteurd.setText(prenomLecteur.getText());

                mBuilder.setPositiveButton("Enregistrer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //dialog.dismiss();
                    }
                });
                mBuilder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                mBuilder.setView(mView);
                final AlertDialog d = mBuilder.create();
                d.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        Button b = d.getButton(AlertDialog.BUTTON_POSITIVE);
                        b.setHeight(30);
                        Button c = d.getButton(AlertDialog.BUTTON_NEGATIVE);
                        c.setHeight(30);
                        b.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new UpdateMemb().execute("http://10.0.2.1:8080/GBibliothecaire/webresources/entity_lecteur/"+numLecteurd.getText());
                            }
                        });
                    }
                });
                d.show();

            }
        });

        butDel =(Button) findViewById(R.id.buttonSupplect);
        butDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(MembreActivity.this);
                builder1.setMessage("Voulez vous vraiment supprimez ce membre ?");
                builder1.setCancelable(true);
                builder1.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder1.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                final AlertDialog alert1 = builder1.create();
                alert1.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(final DialogInterface dialog) {
                        Button b = alert1.getButton(AlertDialog.BUTTON_POSITIVE);
                        b.setHeight(30);
                        Button c = alert1.getButton(AlertDialog.BUTTON_NEGATIVE);
                        c.setHeight(30);
                        b.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new DeleteLect().execute("http://10.0.2.1:8080/GBibliothecaire/webresources/entity_lecteur/"+numLecteur.getText());
                                dialog.dismiss();
                                MembreActivity.this.finish();
                            }
                        });
                    }
                });
                alert1.show();

            }
        });

    }


    private class LongOperation extends AsyncTask<String, Void, Void> {

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

        public String requestContent(String url) {
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
            Lecteur lect = null;
            try {
                if(Content != null) {
                    JSONObject lecteurObject = new JSONObject(Content);
                    lect = new Lecteur(lecteurObject.getString("numLecteur"),
                            lecteurObject.getString("nom"), lecteurObject.getString("prenom"),
                            lecteurObject.getString("adresse"));
                }

            } catch (JSONException e) {
                Log.d("JSEON", e.getMessage());
            }
            if( lect != null){
                updateUI(lect);
            }
        }


    }

    private void updateUI(Lecteur lecteur){
        numLecteur.setText(lecteur.getNumLecteur());
        nomLecteur.setText(lecteur.getNom());
        prenomLecteur.setText(lecteur.getPrenom());
        addLecteur.setText(lecteur.getAdresse());
    }


    private class UpdateMemb extends AsyncTask<String, Void, Void> {

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
            InputStream inputStream = null;
            String result = "";

            try {
                // 1. create HttpClient
                HttpClient httpclient = new DefaultHttpClient();
                // 2. make POST request to the given URL

                HttpPut httpPut = new HttpPut(url);
                String json = "";
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("numLecteur",numLecteurd.getText());
                jsonObject.put("nom",nomLecteurd.getText());
                jsonObject.put("prenom",prenomLecteurd.getText());
                jsonObject.put("adresse",addLecteurd.getText());
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

                if(inputStream != null)
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

        @Override
        protected void onPostExecute(Void unused) {

        }

    }

    private class DeleteLect  extends AsyncTask<String, Void, Void> {

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
            InputStream inputStream = null;
            String result = "";

            try {
                // 1. create HttpClient
                HttpClient httpclient = new DefaultHttpClient();
                // 2. make POST request to the given URL

                HttpDelete httpPut = new HttpDelete(url);
                /*String json = "";
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("numEmploye",num_diag.getText());
                jsonObject.put("nom",nom_diag.getText());
                jsonObject.put("adresse",add_diag.getText());
                jsonObject.put("tauxHoraire",Double.parseDouble(taux_diag.getText().toString()));
                json = jsonObject.toString();
                StringEntity se = new StringEntity(json);
                // 6. set httpPost Entity
                httpPut.setEntity(se);
                // 7. Set some headers to inform server about the type of the content
                httpPut.addHeader("Accept", "application/json");
                httpPut.addHeader("Content-type", "application/json");*/
                // 8. Execute POST request to the given URL
                HttpResponse httpResponse = httpclient.execute(httpPut);

                //Try to add this
                inputStream = httpResponse.getEntity().getContent();

                if(inputStream != null)
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

        @Override
        protected void onPostExecute(Void unused) {
           /* if(Content.equals("ko")){
                indiceAdd.setError("erreur d'ajout");
            }else{
                indiceAdd.setTextColor(Color.GREEN);
                indiceAdd.setText("employé ajouté");
            }*/
        }

    }

}
