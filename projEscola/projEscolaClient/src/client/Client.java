package client;

import connection.ConnectionSQLite;
import exception.ResourceCannotRemovedException;
import exception.ResourceNotFoundException;
import model.*;
import persistence.*;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {

    private static final String ALUNO_PATH = "rmi://localhost:8282/aluno";
    private static final String CURSO_PATH = "rmi://localhost:8282/curso";
    private static final String CURSO_PROFESSOR_PATH = "rmi://localhost:8282/curso_professor";
    private static final String ENDERECO_PATH = "rmi://localhost:8282/endereco";
    private static final String PROFESSOR_PATH = "rmi://localhost:8282/professor";

    private static AlunoPersistence alunoPersistence;
    private static CursoPersistence cursoPersistence;
    private static CursoProfessorPersistence cursoProfessorPersistence;
    private static EnderecoPersistence enderecoPersistence;
    private static ProfessorPersistence professorPersistence;

    private static Scanner scanner;

    public static void main(String[] args) throws Exception {
        instanciaServidor();

        lerEscolha();
    }

    private static void lerEscolha() throws Exception {
        Boolean isContinue = true;
        while (isContinue) {
            try {
                exibirMenu();
                int option = lerInt();
                switch (option) {
                    case 1:
                        insertEndereco();
                        break;
                    case 2:
                        listAllEnderecos();
                        break;
                    case 3:
                        insertCurso();
                        break;
                    case 4:
                        listAllCursos();
                        break;
                    case 5:
                        insertAluno();
                        break;
                    case 6:
                        listAllAluno();
                        break;
                    case 7:
                        insertProfessor();
                        break;
                    case 8:
                        listAllProfessores();
                        break;
                    case 9:
                        insertCursoProfessor();
                        break;
                    case 10:
                        listAllCursoProfessores();
                        break;
                    case 0:
                        isContinue = false;
                        System.out.println("Sistema encerrado.");
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Ocorreu um ao ler a entrada informada.");
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static void insertAluno() throws NotBoundException, MalformedURLException, ResourceNotFoundException {
        try {
            Aluno a = new Aluno();
            System.out.print("Informe o CPF do aluno: ");
            a.setCpf(lerString());

            System.out.print("Informe o nome do aluno: ");
            a.setNome(lerString());

            System.out.print("Informe o e-mail do aluno: ");
            a.setEmail(lerString());

            System.out.print("Informe o RA do aluno: ");
            a.setRa(lerString());

            System.out.print("Informe o telefone do aluno: ");
            a.setTelefone(lerString());           

            System.out.print("Informe o id do endereco do aluno: \n");
            listAllEnderecos();
            
            Endereco e = enderecoPersistence.findById(lerInt());
            a.setEndereco(e);
            
            System.out.print("Informe o id do curso do aluno: \n");
            listAllCursos();
            Curso c = cursoPersistence.findById(lerLong());
            a.setCurso(c);
            
            alunoPersistence.create(a);
        } catch (RemoteException e) {
            System.out.println("Ocorreu um erro com o serviço.");
        }
    }

    private static void listAllAluno() {
        try {
            for (Aluno a : alunoPersistence.listAll()) {
                System.out.println(a.toString());
            }
        } catch (RemoteException e) {
            System.out.println("Ocorreu um erro com o serviço.");
        }
    }

    private static void insertCurso() throws ClassNotFoundException, Exception {
        Curso c = new Curso();
        System.out.print("Informe o nome do curso: ");
        c.setNome(lerString());
        System.out.print("Informe a carga horária do curso: ");
        c.setCargaHoraria(lerString());
        System.out.print("Informe o horário de início do curso: ");
        c.setHoraInicio(lerString());
        System.out.print("Informe o horario do fim do curso: ");
        c.setHoraFim(lerString());
        System.out.print("Informe o número da sala curso: ");
        c.setNumSala(lerInt());
        
        cursoPersistence.create(c);
    }

    private static void listAllCursos() {
        try {
            for (Curso c : cursoPersistence.listAll()) {
                System.out.println(c.toString());
            }
        } catch (RemoteException e) {
            System.out.println("Ocorreu um erro com o serviço.");
        }
    }

    private static void insertEndereco() {
        try {
            Endereco e = new Endereco();

            System.out.print("Informe o logradouro: ");
            e.setLogradouro(lerString());
            System.out.print("Informe o número: ");
            e.setNumero(lerInt());
            System.out.print("Informe o CEP: ");
            e.setCep(lerString());
            System.out.print("Informe o bairro: ");
            e.setBairro(lerString());
            System.out.print("Informe o complemento: ");
            e.setComplemento(lerString());
            System.out.print("Informe a localidade: ");
            e.setLocalidade(lerString());
            System.out.print("Informe o estado(UF): ");
            e.setUf(lerString());

            enderecoPersistence.create(e);
        } catch (RemoteException e) {
            System.out.println("Ocorreu um erro com o serviço.");
        }
    }

    private static void listAllEnderecos() {
        try {
            for (Endereco e : enderecoPersistence.listAll()) {
                System.out.println(e.toString());
            }
        } catch (RemoteException e) {
            System.out.println("Ocorreu um erro com o serviço.");
        }
    }

    private static void insertProfessor() throws ResourceNotFoundException {
        try {
            Professor p = new Professor();
            System.out.print("Informe o CPF: ");
            p.setCpf(lerString());
            System.out.print("Informe o nome: ");
            p.setNome(lerString());
            System.out.print("Informe o telefone: ");
            p.setTelefone(lerString());
            System.out.print("Informe o e-mail: ");
            p.setEmail(lerString());
            System.out.print("Informe o salário: ");
            p.setSalario(lerDouble());

            System.out.print("Informe o id endereco do professor: \n");
            listAllEnderecos();
            Endereco e = enderecoPersistence.findById(lerInt());
            p.setEndereco(e);

            professorPersistence.create(p);
        } catch (RemoteException e) {
            System.out.println("Ocorreu um erro com o serviço.");
        } catch (ResourceNotFoundException e) {
             System.out.println(e);
        }
    }

    private static void listAllProfessores() {
        try {
            for (Professor p : professorPersistence.listAll()) {
                System.out.println(p.toString());
            }
        } catch (RemoteException e) {
            System.out.println("Ocorreu um erro com o serviço.");
        }
    }

    private static void insertCursoProfessor() throws ResourceNotFoundException {
        try {
            CursoProfessor cp = new CursoProfessor();
            System.out.print("Informe o ID do curso: \n");
            listAllCursos();
            Curso c = cursoPersistence.findById(lerLong());
            cp.setCurso(c);

            System.out.print("Informe o ID do professor: \n");
            listAllProfessores();
            Professor p = professorPersistence.findById(lerInt());
            cp.setProfessor(p);

            cursoProfessorPersistence.create(cp);
        } catch (RemoteException e) {
            System.out.println("Ocorreu um erro com o serviço.");
        }
    }

    private static void listAllCursoProfessores() {
        try {
            for (CursoProfessor cp : cursoProfessorPersistence.listAll()) {
                System.out.println(cp.toString());
            }
        } catch (RemoteException e) {
            System.out.println("Ocorreu um erro com o serviço.");
        }
    }

    private static Integer lerInt() {
        scanner = new Scanner(System.in);
        return scanner.nextInt();
    }

    private static String lerString() {
        scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    private static Long lerLong() {
        scanner = new Scanner(System.in);
        return scanner.nextLong();
    }

    private static Double lerDouble() {
        scanner = new Scanner(System.in);
        return scanner.nextDouble();
    }

    private static void exibirMenu() {
        StringBuilder sb = new StringBuilder();
        sb.append("-- Endereço --");
        sb.append("\n1 - Inserir");
        sb.append("\n2 - Listar");
        sb.append("\n-- Curso --");
        sb.append("\n3 - Inserir");
        sb.append("\n4 - Listar");
        sb.append("\n-- Aluno --");
        sb.append("\n5 - Inserir");
        sb.append("\n6 - Listar");
        sb.append("\n-- Professor --");
        sb.append("\n7 - Inserir");
        sb.append("\n8 - Listar");
        sb.append("\n-- Curso / Professor --");
        sb.append("\n9 - Inserir");
        sb.append("\n10 - Listar");

        sb.append("\n\n0 - Sair");
        sb.append("\nEscolha a operação: ");
        System.out.print(sb.toString());
    }

    private static void instanciaServidor() {
        try {
            alunoPersistence = (AlunoPersistence) Naming.lookup(ALUNO_PATH);
            cursoPersistence = (CursoPersistence) Naming.lookup(CURSO_PATH);
            cursoProfessorPersistence = (CursoProfessorPersistence) Naming.lookup(CURSO_PROFESSOR_PATH);
            enderecoPersistence = (EnderecoPersistence) Naming.lookup(ENDERECO_PATH);
            professorPersistence = (ProfessorPersistence) Naming.lookup(PROFESSOR_PATH);
        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            System.out.println("Ocorreu um erro com o serviço.");
        }
    }

    private static void createCC(Curso c, ConnectionSQLite conexao) throws ClassNotFoundException, SQLException {
        //try {
            //conn.exec("INSERT INTO curso (nome, carga_horaria, horario_inicio, horario_fim, numero_sala) VALUES ('" + c.getNome() + "','" + c.getCargaHoraria() + "','" + c.getHoraInicio() + "','" + c.getHoraFim() + "'," + c.getNumSala() + ");");
            //conexao.exec("INSERT INTO curso (nome, carga_horaria, horario_inicio, horario_fim, numero_sala) VALUES ('sdsd' ,'dfdsfdf','fdsdffd','ffsdfsdf'" + "," + 1 + ");");
            //conexao.exec("INSERT INTO curso (nome, carga_horaria, horario_inicio, horario_fim, numero_sala) VALUES ('" + c.getNome() + "','" + c.getCargaHoraria() + "','" + c.getHoraInicio() + "','" + c.getHoraFim() + "'," + c.getNumSala() + ")");
            
        }
    
}
