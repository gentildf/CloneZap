package com.example.clonezap.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.clonezap.R;
import com.example.clonezap.helper.Base64Custom;
import com.example.clonezap.config.DatabaseConf;
import com.example.clonezap.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastroActivity extends AppCompatActivity {


    private EditText campoNome, campoEmail, campoSenha;
    private Button botaoCadastrar;
    private FirebaseAuth autenticacao;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        campoNome = findViewById(R.id.campoNome);
        campoEmail = findViewById(R.id.campoEmail);
        campoSenha = findViewById(R.id.campoSenha);
        botaoCadastrar = findViewById(R.id.botaoCadastrar);

        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nomeInserido = campoNome.getText().toString();
                String emailInserido = campoEmail.getText().toString();
                String senhaInserida = campoSenha.getText().toString();

                if (!nomeInserido.isEmpty()){
                    if (!emailInserido.isEmpty()){
                        if (!senhaInserida.isEmpty()){
                            Usuario usuario = new Usuario();
                            usuario.setNome(nomeInserido);
                            usuario.setEmail(emailInserido);
                            usuario.setSenha(senhaInserida);

                            concluirCadastro(usuario);

                        }  else {
                            Toast.makeText(
                                    CadastroActivity.this,
                                    "Preencha a senha!",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }else {
                        Toast.makeText(
                                CadastroActivity.this,
                                "Preencha o email!",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }else {
                    Toast.makeText(
                            CadastroActivity.this,
                            "Preencha o nome!",
                            Toast.LENGTH_SHORT
                    ).show();
                }

            }
        });

    }

    public void concluirCadastro(Usuario usuario){
        autenticacao = DatabaseConf.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    String idUsuario = Base64Custom.codificarBase64(usuario.getEmail());
                    usuario.setIdUsuario(idUsuario);
                    usuario.salvar();

                    finish();
                }else {
                    String excecao = "";

                    try {
                     throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        excecao = "Digite uma senha mais forte!";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        excecao = "Digite um email valido!";
                    }catch (FirebaseAuthUserCollisionException e){
                        excecao = "Email ja cadastrado!";
                    }catch (Exception e){
                        excecao = "Erro ao cadastrar usuario: " + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(
                            CadastroActivity.this,
                            excecao,
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });
    }
}