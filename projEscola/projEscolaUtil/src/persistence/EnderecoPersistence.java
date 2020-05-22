package persistence;

import exception.ResourceCannotRemovedException;
import exception.ResourceNotFoundException;
import model.Aluno;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.Curso;
import model.Endereco;

public interface EnderecoPersistence extends Remote {

    void create(Endereco endereco) throws RemoteException;

    List<Endereco> listAll() throws RemoteException;
    
    Endereco findById(int id) throws RemoteException, ResourceNotFoundException;

}
