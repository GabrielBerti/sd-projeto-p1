package persistence;

import connection.ConnectionSQLite;
import exception.ResourceNotFoundException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.Curso;

public interface CursoPersistence extends Remote {

    void create(Curso curso) throws RemoteException;

    List<Curso> listAll() throws RemoteException;

    Curso findById(Long id) throws RemoteException, ResourceNotFoundException;
}