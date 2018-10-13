package dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import model.Alarme;
import model.Medicamento;
import model.MedicamentoLocal;

public class MedicamentoLocalDAO extends SQLiteOpenHelper {

    private static final String NOME_BANCO = "poso.db";
    private static final int VERSION = 1;
    private static final String TABELA = "medicamento";
    private static final String IDMEDICAMENTO = "idMedicamento";
    private static final String DESCRICAO = "descricao";
    private static final String LABORATORIO = "laboratorio";
    private static final String BARRAS = "barras";
    private static final String USUARIOMEDICAMENTO = "usuarioMedicamento";

    public MedicamentoLocalDAO(Context context) {
        super(context, NOME_BANCO, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE " +TABELA+ "(" +
                " "+IDMEDICAMENTO+" TEXT PRIMARY KEY ,"+
                " "+DESCRICAO+" TEXT, "+
                " "+USUARIOMEDICAMENTO+" TEXT, "+
                " "+LABORATORIO+" TEXT,"+BARRAS+" TEXT);";

        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        String sql = "DROP TABLE IS EXISTS" + TABELA;
        db.execSQL(sql);
        onCreate(db);

    }

    public long salvarMedicamento(Medicamento medicamento){
        ContentValues values = new ContentValues();
        long retornoDB;

        values.put(IDMEDICAMENTO, medicamento.getIdMedicamento());
        values.put(DESCRICAO, medicamento.getNomeMedicamento());
        values.put(USUARIOMEDICAMENTO, medicamento.getUsuarioMedicamento());
        values.put(LABORATORIO, medicamento.getNomeLaboratorio());
        values.put(BARRAS, medicamento.getBarras1());


        retornoDB = getWritableDatabase().insert(TABELA, null, values);

        return retornoDB;
    }


    public ArrayList<Medicamento> ListaMedicamentoFiltrado(String usuarioMedicamento){

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM medicamento WHERE usuarioMedicamento = '"+usuarioMedicamento+"' ",null);

        ArrayList<Medicamento> listaMedicamentoLocal = new ArrayList<Medicamento>();

        while (cursor.moveToNext()){

            Medicamento med = new Medicamento();

            med.setIdMedicamento(cursor.getString(0));
            med.setNomeMedicamento(cursor.getString(1));
            med.setUsuarioMedicamento(cursor.getString(2));
            med.setNomeLaboratorio(cursor.getString(3));
            med.setBarras1(cursor.getString(4));

            listaMedicamentoLocal.add(med);

        }

        return  listaMedicamentoLocal;
    }

    public long deletarMedicamento(Medicamento med){

        long retornoDB;

        String[] identificacao = {String.valueOf(med.getIdMedicamento())};

        retornoDB = getWritableDatabase().delete(TABELA, "IDMEDICAMENTO= ?", identificacao );

        return retornoDB;
    }

    public long atualizarMedicamento(Medicamento med){
        ContentValues values = new ContentValues();
        long retornoDB;

        values.put(DESCRICAO, med.getNomeMedicamento());
        values.put(LABORATORIO, med.getNomeLaboratorio());
        values.put(BARRAS, med.getBarras1());
        values.put(USUARIOMEDICAMENTO, med.getUsuarioMedicamento());

        String[] identificacao = {String.valueOf(med.getIdMedicamento())};

        retornoDB = getWritableDatabase().update(TABELA, values, "IDMEDICAMENTO = ?", identificacao );

        return retornoDB;
    }

}
