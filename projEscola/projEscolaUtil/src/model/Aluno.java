package model;

import java.io.Serializable;

public class Aluno extends Pessoa implements Serializable  {
    private static final long serialVersionUID = 5388928189437167613L;

    private String ra;
    private Curso curso;

    public String getRa() {
        return ra;
    }

    public void setRa(String ra) {
        this.ra = ra;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    @Override
    public String toString() {
         return "Aluno{" + "id=" + id + ", cpf=" + cpf + ", nome=" + nome + ", telefone=" + telefone + ", email=" + email + ", Ra=" + ra + "," + endereco + "," + curso + "}";
    }      
}
