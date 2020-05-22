package model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Professor extends Pessoa implements Serializable {

    private static final long serialVersionUID = 7106801829663849989L;

    private double salario;

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    @Override
    public String toString() {
        return "Professor{" + "id=" + id + ", cpf=" + cpf + ", nome=" + nome + ", telefone=" + telefone + ", email=" + email + ", salario=" + salario + "," + endereco + "}";
    }

}
