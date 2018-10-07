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

    public MedicamentoLocalDAO(Context context) {
        super(context, NOME_BANCO, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE " +TABELA+ "(" +
                " "+IDMEDICAMENTO+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                " "+DESCRICAO+" TEXT, "+
                ""+LABORATORIO+" TEXT,"+BARRAS+" TEXT);";

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

        values.put(DESCRICAO, medicamento.getNomeMedicamento());
        values.put(LABORATORIO, medicamento.getNomeLaboratorio());
        values.put(BARRAS, medicamento.getBarras1());

        retornoDB = getWritableDatabase().insert(TABELA, null, values);

        return retornoDB;
    }

    public ArrayList<Medicamento> selectAllMed() {

        String[] coluns = {IDMEDICAMENTO, DESCRICAO, LABORATORIO, BARRAS};
        Cursor cursor = getWritableDatabase().query(TABELA, coluns, null, null, null, null, null, null);

        ArrayList<Medicamento> listaMedicamentoLocal = new ArrayList<Medicamento>();

        while (cursor.moveToNext()) {

            Medicamento med = new Medicamento();

            int idMedicamento = cursor.getInt(0);

            med.setIdMedicamento(String.valueOf(idMedicamento));
            med.setNomeMedicamento(cursor.getString(1));
            med.setNomeLaboratorio(cursor.getString(2));
            med.setBarras1(cursor.getString(3));

            listaMedicamentoLocal.add(med);
        }

        return listaMedicamentoLocal;
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

        String[] identificacao = {String.valueOf(med.getIdMedicamento())};

        retornoDB = getWritableDatabase().update(TABELA, values, "IDMEDICAMENTO = ?", identificacao );

        return retornoDB;
    }

}
