package com.example.projetoteste.projetoteste;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private EditText user, password;


    public boolean validaUsuario(String usuario){
        return usuario.contains("@");
    }

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null && firebaseAuth.getCurrentUser().getEmail().equals("christyansoftperes@gmail.com")) {
            Intent intent = new Intent(MainActivity.this, ClassePrincipal.class);
            startActivity(intent);
        }
        else if(firebaseAuth.getCurrentUser() != null){
            Intent intent = new Intent(MainActivity.this, ClassePrincipalComun.class);
            startActivity(intent);
        }

        Button logar = findViewById(R.id.logar);
        user = findViewById(R.id.email);
        password = findViewById(R.id.senha);

        logar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textoEmail = user.getText().toString();
                String textoSenha = password.getText().toString();

                if (textoEmail.equals("") || textoSenha.equals("")) {
                    Toast.makeText(MainActivity.this, "Digite e-mail e senha para logar", Toast.LENGTH_SHORT).show();
                } else {

                    firebaseAuth.signInWithEmailAndPassword(textoEmail, textoSenha)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        if(firebaseAuth.getCurrentUser().isEmailVerified()) {

                                            if(firebaseAuth.getCurrentUser().getEmail().equals("christyansoftperes@gmail.com")) {

                                                Toast.makeText(MainActivity.this, "Logado com sucesso", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(MainActivity.this, ClassePrincipal.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                            else{
                                                Toast.makeText(MainActivity.this, "Logado com sucesso", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(MainActivity.this, ClassePrincipalComun.class);
                                                startActivity(intent);
                                                finish();
                                            }

                                        }else{
                                            Toast.makeText(MainActivity.this, "Verifique seu e-mail antes de logar", Toast.LENGTH_SHORT).show();
                                        }

                                    } else {
                                        Toast.makeText(MainActivity.this, "Usuário não cadastrado, ou erro de login", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });


    }


    public void criarUsuario(View view){

        View view2 = getLayoutInflater().inflate(R.layout.diagcadastro, null);
        final EditText emailC = view2.findViewById(R.id.etEmail);
        final EditText senhaC = view2.findViewById(R.id.etSenha);
        final Button cadastrar = view2.findViewById(R.id.btnCadastro);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setView(view2);

        final AlertDialog dialog = builder.create();
        dialog.show();

        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean result = false;

                if((!validaUsuario(emailC.getText().toString())) || (emailC.getText().toString().equals(""))){
                    emailC.setError("Digite um e-mail valido");
                    result = true;
                }

                if((senhaC.getText().toString().length()<6) || (senhaC.getText().toString().equals(""))){
                    senhaC.setError("Digite no mínimo 6 caracteres");
                    result = true;
                }


                if(!result)

                {

                    firebaseAuth = FirebaseAuth.getInstance();

                    firebaseAuth.createUserWithEmailAndPassword(emailC.getText().toString(), senhaC.getText().toString())
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(MainActivity.this, "Criado com sucesso", Toast.LENGTH_SHORT).show();

                                        firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    dialog.dismiss();
                                                    showDialogo(emailC.getText().toString());
                                                }
                                                else{
                                                    Toast.makeText(MainActivity.this, "nao enviado", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                        firebaseAuth.signOut();


                                    } else {
                                        Toast.makeText(MainActivity.this, "Erro ao criar usuário", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }

            }
        });


    }

    private void showDialogo(String info){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle("Verificação");
        //define a mensagem
        builder.setMessage("Enviamos um e-mail para "+info+", verifique sua caixa de entrada e clique no link enviado" +
                " para que possamos validar sua entrada no sistema.");
        //define um botão como positivo

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });

        //cria o AlertDialog
        android.app.AlertDialog alerta = builder.create();
        //Exibe
        alerta.show();
    }


    public void redefinirSenha(View view){

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View view1 = getLayoutInflater().inflate(R.layout.dialogsenha, null);
        final EditText email = view1.findViewById(R.id.etEmail);
        final Button enviar = view1.findViewById(R.id.btnEnviar);

        builder.setView(view1);

        final AlertDialog dialog = builder.create();
        dialog.show();

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firebaseAuth.sendPasswordResetEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "E-mail enviado com sucesso", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                        else{
                            Toast.makeText(MainActivity.this, "Erro ao enviar, ou e-mail nao encontrado", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });

    }


    @Override
    public void onResume(){
        super.onResume();
        if (firebaseAuth.getCurrentUser() != null){
            finish();
        }
    }


}
