package persistence;

import exception.ResourceCannotRemovedException;
import exception.ResourceNotFoundException;
import model.Pessoa;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.Aluno;

public interface AlunoPersistence extends Remote {

    void create(Aluno aluno) throws RemoteException;

    List<Aluno> listAll() throws RemoteException;

}