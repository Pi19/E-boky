package com.example.hasimananjara.e_boky;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.hasimananjara.e_boky.adapter.PretAdapter;
import com.example.hasimananjara.e_boky.classy.Lecteur;
import com.example.hasimananjara.e_boky.classy.Livre;
import com.example.hasimananjara.e_boky.classy.PretPk;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
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

public class PretActivity extends AppCompatActivity {

    private String numliv = null;
    private String numlect = null ;




    private TextView numLivre;
    private TextView auteur;
    private TextView designation;
    private TextView date;

    private TextView numLivred;
    private TextView auteurd;
    private TextView designationd;
    private TextView dateEditiond;
    private PretPk pk1 ;

    private Button butModif ;
    private Button butDel ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pret);

        Intent i = getIntent();
        numliv= i.getStringExtra("NUMliv");
        numlect= i.getStringExtra("NUMlect");
        Log.d("Num",""+numlect);
        new LongOperation().execute("http://10.0.2.1:8080/GBibliothecaire/webresources/entity_pret/id;numLivre="+numliv+";numLecteur="+numlect);

        numLivre = (TextView) findViewById(R.id.numerol);
        auteur = (TextView) findViewById(R.id.numerole);
        designation = (TextView) findViewById(R.id.pretDate);



       butModif =(Button) findViewById(R.id.buttonModifPret);
        butModif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(PretActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.modif_pret,null);

                numLivred = (TextView) mView.findViewById(R.id.num_Livpred);

                auteurd = (TextView) mView.findViewById(R.id.lectpred);

                designationd = (TextView) mView.findViewById(R.id.datepred);


                //assignation text
                numLivred.setText(numLivre.getText());
                auteurd.setText(auteur.getText());
                //dateEditiond.setText(date.getText());
                designationd.setText(designation.getText());

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
                                new UpdateLivre().execute("http://10.0.2.1:8080/GBibliothecaire/webresources/entity_pret/id;numLivre="+numliv+";numLecteur="+numlect);
                            }
                        });
                    }
                });
                d.show();

            }
        });

        butDel =(Button) findViewById(R.id.buttonSupplPret);
        butDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(PretActivity.this);
                builder1.setMessage("Voulez vous vraiment supprimez ce livre ?");
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
                                new DeleteLivre().execute("http://10.0.2.1:8080/GBibliothecaire/webresources/entity_pret/id;numLivre="+numliv+";numLecteur="+numlect);
                                dialog.dismiss();
                                PretActivity.this.finish();
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
            Log.d("Contenu",Content);
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

           PretPk pk = null ;
            try {
                Object object = null;
                if(Content != null) {
                        JSONObject pretObject = new JSONObject(Content);
                        //JSONObject pretPkObject = jsonArray.getJSONObject(3);
                        String livre = pretObject.getString("livre");
                        String membre = pretObject.getString("lecteur");
                        JSONObject lvr = new JSONObject(livre);
                        JSONObject mb = new JSONObject(membre);

                        Livre lv = new Livre(lvr.getString("numLivre"));
                        Lecteur lect = new Lecteur(mb.getString("numLecteur"));
                        Log.d("sakay",lv.getNumLivre());
                        pk = new PretPk(pretObject.getString("datepret"),lv,lect);
                        //Log.d("sakay",pk.getLecteur().getNumLecteur());
                    }


                else {
                    Log.d("Test","LEO");
                }


            } catch (JSONException e) {
                Log.d("test",e.getMessage());
            }

            updateUI(pk);

        }

    }

    private void updateUI(PretPk livre){
        //membre.setText(mpret.getLecteur().getNumLecteur());
        //livre.setText(mpret.getLivre().getNumLivre());
        //datePret.setText(mpret.getDatePret());
         ;
        numLivre.setText(livre.getLivre().getNumLivre());
        auteur.setText(livre.getLecteur().getNumLecteur());
        designation.setText(livre.getDatePret());
        ///auteur =
               // designation =
    }



    private class UpdateLivre extends AsyncTask<String, Void, Void> {

        // Required initialization

        private final HttpClient Client = new DefaultHttpClient();
        private String Content;
        private String Error = null;
        private int ok ;
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

                String txt = date.getText().toString();
                Log.d("AMEN",txt);
                jsonObject.put("datepret",txt);

                JSONObject jsonObjectLivre = new JSONObject();
                jsonObjectLivre.put("numLivre",""+numliv);

                JSONObject jsonObjectLecteur = new JSONObject();
                jsonObjectLecteur.put("numLecteur",""+numlect);

                JSONObject jsonObjectPK = new JSONObject();
                jsonObjectPK.put("numLivre",""+numliv);
                jsonObjectPK.put("numLecteur",""+numlect);

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

        }

    }

    private class DeleteLivre  extends AsyncTask<String, Void, Void> {

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

        }

    }

}
