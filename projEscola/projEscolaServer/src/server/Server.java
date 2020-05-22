package server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import persistence.AlunoPersistenceImpl;
import persistence.CursoPersistenceImpl;
import persistence.CursoProfessorPersistenceImpl;
import persistence.EnderecoPersistenceImpl;
import persistence.ProfessorPersistenceImpl;

public class Server {

    private static final String BASE = "rmi://localhost:8282";

    public Server() {
        try {
            LocateRegistry.createRegistry(8282);
            Naming.rebind(String.format("%s/aluno", BASE), new AlunoPersistenceImpl());
            System.out.println("Servico 'Aluno' iniciado com sucesso.");

            Naming.rebind(String.format("%s/curso", BASE), new CursoPersistenceImpl());
            System.out.println("Servico 'Curso' iniciado com sucesso.");

            Naming.rebind(String.format("%s/curso_professor", BASE), new CursoProfessorPersistenceImpl());
            System.out.println("Servico 'Curso Professor' iniciado com sucesso.");

            Naming.rebind(String.format("%s/endereco", BASE), new EnderecoPersistenceImpl());
            System.out.println("Servico 'Endereco' iniciado com sucesso.");

            Naming.rebind(String.format("%s/professor", BASE), new ProfessorPersistenceImpl());
            System.out.println("Servico 'Professor' iniciado com sucesso.");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Server();
    }

}
