package com.example.projetoteste.projetoteste;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import model.Medicamento;

public class TarefaDownload extends AsyncTask<String, Void, ArrayList<Medicamento>> {

    private Context context;
    private ProgressDialog progress;
    ArrayList<Medicamento> arrayList = new ArrayList<>();

    public TarefaDownload(Context context){

        this.context = context;

    }

    @Override
    protected void onPreExecute(){
      //  progress = new ProgressDialog(context);
      //  progress.setMessage("Carregando dados");
      //  progress.show();



    }

    @Override
    protected ArrayList<Medicamento> doInBackground(String... parametro) {




        return arrayList;
    }

    @Override
    protected void onPostExecute(ArrayList<Medicamento> params){

        if(arrayList.isEmpty()){
            Toast.makeText(context, "Nao existe ", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context, "Existe", Toast.LENGTH_SHORT).show();
        }

       // Toast.makeText(context, "med: "+params.getNomeMedicamento(), Toast.LENGTH_SHORT).show();
     //   progress.setMessage("Dados caregados");
      //  progress.dismiss();

    }

}
