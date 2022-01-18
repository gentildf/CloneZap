package com.example.clonezap.model;

import com.example.clonezap.config.DatabaseConf;
import com.google.firebase.database.DatabaseReference;

public class Usuario {

    private String nome, email, senha, idUsuario;

    public Usuario(){

    }
    public void salvar(){
        DatabaseReference firebase = DatabaseConf.getFirebaseDatabase();
        firebase.child("usuarios").child(this.idUsuario).setValue(this);
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
