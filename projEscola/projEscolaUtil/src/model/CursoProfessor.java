package model;

import java.io.Serializable;

public class CursoProfessor implements Serializable {

    private static final long serialVersionUID = 1;

    private Curso curso;
    private Professor professor;

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    @Override
    public String toString() {
        return "CursoProfessor{" + "curso=" + curso + ", professor=" + professor + '}';
    }

}
