package persistence;

import exception.ResourceCannotRemovedException;
import exception.ResourceNotFoundException;
import model.Endereco;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.Curso;
import model.Professor;

public interface ProfessorPersistence extends Remote {

    void create(Professor Professor) throws RemoteException;

    List<Professor> listAll() throws RemoteException;
    
    Professor findById(int id) throws RemoteException, ResourceNotFoundException;

}