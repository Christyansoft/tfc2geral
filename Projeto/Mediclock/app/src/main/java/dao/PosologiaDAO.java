package dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import model.Alarme;

import static android.content.Context.MODE_PRIVATE;

public class PosologiaDAO extends SQLiteOpenHelper {

    private static final String NOME_BANCO = "posologia.db";
    private static final int VERSION = 1;
    private static final String TABELA = "alarme";
    private static final String IDALARME = "idAlarme";
    private static final String LEGENDA = "legenda";
    private static final String HORA = "hora";
    private static final String MINUTO = "minuto";
    private static final String SEGUNDO = "segundo";
    private static final String IDPENDING = "idPending";
    private static final String REPEATING = "repeating";
    private static final String IDMEDICAMENTO = "idMedicamento";
    private static final String IDTRATAMENTO = "idTratamento";

    public PosologiaDAO(Context context) {
        super(context, NOME_BANCO, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE " +TABELA+ "(" +
                " "+IDALARME+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                " "+LEGENDA+" TEXT, "+
                " "+HORA+" INTEGER, " +MINUTO+" INTEGER, "+
                "" +SEGUNDO+" INTEGER, "+
                "" +IDPENDING+" INTEGER, "+REPEATING+" TEXT, "+
                ""+IDMEDICAMENTO+" TEXT,"+IDTRATAMENTO+" TEXT);";

        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String sql = "DROP TABLE If EXISTS " + TABELA;
        db.execSQL(sql);
        onCreate(db);

    }

    public long salvarAlarme(Alarme alarme){
        ContentValues values = new ContentValues();
        long retornoDB;

        values.put(LEGENDA, alarme.getLegenda());
        values.put(HORA, alarme.getHora());
        values.put(MINUTO, alarme.getMinuto());
        values.put(SEGUNDO, alarme.getSegundo());
        values.put(IDPENDING, alarme.getIdPending());
        values.put(REPEATING, alarme.getRepeating());
        values.put(IDMEDICAMENTO, alarme.getIdMedicamento());
        values.put(IDTRATAMENTO, alarme.getIdTratameto());

        retornoDB = getWritableDatabase().insert(TABELA, null, values);

        return retornoDB;
    }

    public ArrayList<Alarme> selectAllAlarme() {

        String[] coluns = {IDALARME, LEGENDA, HORA, MINUTO, SEGUNDO, IDPENDING, REPEATING, IDMEDICAMENTO, IDTRATAMENTO};
        Cursor cursor = getWritableDatabase().query(TABELA, coluns, null, null, null, null, null, null);
        ArrayList<Alarme> listaAlarme = new ArrayList<Alarme>();

        while (cursor.moveToNext()) {
            Alarme a = new Alarme();
            a.setIdAlarme(cursor.getInt(0));
            a.setLegenda(cursor.getString(1));
            a.setHora(cursor.getInt(2));
            a.setMinuto(cursor.getInt(3));
            a.setSegundo(cursor.getInt(4));
            a.setIdPending(cursor.getInt(5));
            a.setRepeating(cursor.getString(6));
            a.setIdMedicamento(cursor.getString(7));
            a.setIdTratameto(cursor.getString(8));

            listaAlarme.add(a);
        }

        return listaAlarme;
    }

    public Alarme BuscarAlarme(String idMedicamento, String idTratamento){

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM alarme WHERE idMedicamento = '"+idMedicamento+"' AND idTratamento = '"+idTratamento+"' ",null);

        Alarme a = new Alarme();

        while (cursor.moveToNext()){

            a.setIdAlarme(cursor.getInt(0));
            a.setLegenda(cursor.getString(1));
            a.setHora(cursor.getInt(2));
            a.setMinuto(cursor.getInt(3));
            a.setSegundo(cursor.getInt(4));
            a.setIdPending(cursor.getInt(5));
            a.setRepeating(cursor.getString(6));
            a.setIdMedicamento(cursor.getString(7));
            a.setIdTratameto(cursor.getString(8));

        }

        return  a;
    }


    public long deletarAlarme(Alarme alarme){

        long retornoDB;

        String[] identificacao = {String.valueOf(alarme.getIdAlarme())};

        retornoDB = getWritableDatabase().delete(TABELA, "IDALARME= ?", identificacao );

        return retornoDB;
    }

}



