package persistence;

import exception.ResourceCannotRemovedException;
import exception.ResourceNotFoundException;
import model.Curso;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.CursoProfessor;

public interface CursoProfessorPersistence extends Remote {

    void create(CursoProfessor cursoProfessor) throws RemoteException;

    List<CursoProfessor> listAll() throws RemoteException;


}
