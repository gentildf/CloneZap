package com.example.clonezap.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.clonezap.R;
import com.example.clonezap.config.DatabaseConf;
import com.example.clonezap.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText campoEmail, campoSenha;
    private Button botaoLogar;
    private FirebaseAuth autenticacao;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        campoEmail = findViewById(R.id.textEmail);
        campoSenha = findViewById(R.id.textSenha);
        botaoLogar = findViewById(R.id.botaoLogar);


        botaoLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailInserido = campoEmail.getText().toString();
                String senhaInserida = campoSenha.getText().toString();

                if (!emailInserido.isEmpty()){
                    if (!senhaInserida.isEmpty()){
                        usuario = new Usuario();
                        usuario.setEmail(emailInserido);
                        usuario.setSenha(senhaInserida);

                        validarLogin();
                    }else{
                        Toast.makeText(
                                LoginActivity.this,
                                "Preencha o campo de senha!",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }else{
                    Toast.makeText(
                            LoginActivity.this,
                            "Preencha o campo de email!",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });


    }
    public void validarLogin(){
        autenticacao = DatabaseConf.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(usuario.getEmail(),usuario.getSenha())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    System.out.println("Login bem sucedido");
                    abrirTelaPrincipal();
                }else{
                    String excecao = "";
                    try {
                        throw task.getException();
                    }catch ( FirebaseAuthInvalidCredentialsException e){
                        excecao = "O login e senha nao correspondem a um usuario cadastrado";
                    }catch (FirebaseAuthInvalidUserException e){
                        excecao = "Email nao cadastrado";
                    }catch (Exception e){
                        excecao = " Nao foi possivel fazer login: " + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(
                            LoginActivity.this,
                            excecao,
                            Toast.LENGTH_SHORT
                    ).show();
                }

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        autenticacao = DatabaseConf.getFirebaseAutenticacao();
        FirebaseUser usuarioAtual = autenticacao.getCurrentUser();
        if (usuarioAtual != null){
            abrirTelaPrincipal();
        }
    }

    public void abrirTelaPrincipal(){
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }


    public void botaoCadastro(View view){
        startActivity(new Intent(this, CadastroActivity.class));
    }
}