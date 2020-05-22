package persistence;

import connection.ConnectionSQLite;
import exception.ResourceNotFoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Curso;
import model.Endereco;
import org.apache.commons.lang3.ObjectUtils;

public class EnderecoPersistenceImpl extends UnicastRemoteObject implements EnderecoPersistence {

    private ConnectionSQLite conn;

    public EnderecoPersistenceImpl() throws RemoteException {
        super();
    }

    public void create(Endereco e) {
        try {
            conn = new ConnectionSQLite();
            conn.exec("INSERT INTO endereco (logradouro, numero, cep, bairro, complemento, localidade, uf) VALUES ('" + e.getLogradouro() + "'," + e.getNumero() + ",'" + e.getCep() + "','" + e.getBairro() + "','" + e.getComplemento() + "','" + e.getLocalidade()+ "','" + e.getUf()+ "')");
        } catch (SQLException ex) {
            Logger.getLogger(EnderecoPersistenceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(EnderecoPersistenceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<Endereco> listAll() {
        List<Endereco> enderecos = null;
       try (Connection connection = ConnectionSQLite.getConnetion()) {
        String sql = "SELECT "
                    + "id , "
                    + "logradouro, "
                    + "numero , "
                    + "cep, "
                    + "bairro , "
                    + "complemento, "
                    + "localidade, "
                    + "uf "
                    + "FROM endereco "
                    + "ORDER BY uf, localidade, bairro, logradouro;";

        PreparedStatement ps = connection.prepareStatement(sql);
        enderecos = lerEndereco(ps.executeQuery());
        } catch (SQLException ex) {
           ex.printStackTrace();
        }

        return enderecos;
    }
    
    public Endereco findById(int id) throws RemoteException, ResourceNotFoundException {
        Endereco e = new Endereco();
        
        try(Connection connection = ConnectionSQLite.getConnetion())  {
            String sql ="SELECT "
                        + "e.id , "
                        + "e.logradouro, "
                        + "e.numero , "
                        + "e.cep, "
                        + "e.bairro , "
                        + "e.localidade , "
                        + "e.uf , "
                        + "e.complemento "
                        + "FROM endereco e " 
                        + "WHERE e.id = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            List<Endereco> enderecos = lerEndereco(ps.executeQuery());           

             //if (ObjectUtils.isNotEmpty(enderecos)) {
             if(enderecos.get(0) != null){
                e = enderecos.get(0);
            } else {
                throw new ResourceNotFoundException("O endereco informado não foi encontrado.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return e;
    }
    
     private List<Endereco> lerEndereco(ResultSet rs) throws SQLException {
        List<Endereco> enderecos = new ArrayList<>();
        while (rs.next()) {
            Endereco e = new Endereco();
            e.setId(rs.getInt(1));
            e.setLogradouro(rs.getString(2));
            e.setNumero(rs.getInt(3));
            e.setCep(rs.getString(4));
            e.setBairro(rs.getString(5));
            e.setLocalidade(rs.getString(6));
            e.setUf(rs.getString(7));
            e.setComplemento(rs.getString(8));
            
            enderecos.add(e);
        }
        return enderecos;
    }

}
