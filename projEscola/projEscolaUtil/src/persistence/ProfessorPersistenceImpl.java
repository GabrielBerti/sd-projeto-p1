package persistence;

import connection.ConnectionSQLite;
import exception.ResourceCannotRemovedException;
import exception.ResourceNotFoundException;
import model.Aluno;
import model.Endereco;
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
import model.Professor;

public class ProfessorPersistenceImpl extends UnicastRemoteObject implements ProfessorPersistence {

    private ConnectionSQLite conn;

    public ProfessorPersistenceImpl() throws RemoteException {
        super();
    }

    public void create(Professor p) {
        try {
            conn = new ConnectionSQLite();
            conn.exec("INSERT INTO professor (cpf, nome, telefone, email, salario, id_endereco) VALUES ('" + p.getCpf()+ "','" + p.getNome() + "','" + p.getTelefone()+ "','" + p.getEmail()+ "'," + p.getSalario()+ "," + p.getEndereco().getId() + ")");
        } catch (SQLException ex) {
            Logger.getLogger(ProfessorPersistenceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ProfessorPersistenceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<Professor> listAll() {
        List<Professor> professores = null;
        try(Connection connection = ConnectionSQLite.getConnetion())  {
            String sql = "SELECT "
                         + "p.id         as id_professor, "
                         + "p.cpf        as cpf_professor, "
                         + "p.nome       as nome_professor, "
                         + "p.telefone   as telefone_professor, "
                         + "p.email      as email_professor, "
                         + "p.salario    as salario_professor, "                
                         + "e.id         as id_endereco, "
                         + "e.logradouro as logradouro_enedereco, "
                         + "e.numero     as numero_endereco, "
                         + "e.bairro     as bairro_endereco "
                         + "FROM professor p "
                         + "INNER JOIN endereco AS e ON p.id_endereco = e.id "
                         + "ORDER BY p.nome;";

        PreparedStatement ps = connection.prepareStatement(sql);
        professores = lerProfessor(ps.executeQuery());
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return professores;
    }

    public Professor findById(int id) throws RemoteException, ResourceNotFoundException {
        Professor p = new Professor();
        
        try(Connection connection = ConnectionSQLite.getConnetion())  {
            String sql = "SELECT "
                         + "p.id         as id_professor, "
                         + "p.cpf        as cpf_professor, "
                         + "p.nome       as nome_professor, "
                         + "p.telefone   as telefone_professor, "
                         + "p.email      as email_professor, "
                         + "p.salario    as salario_professor, "                
                         + "e.id         as id_endereco, "
                         + "e.logradouro as logradouro_enedereco, "
                         + "e.numero     as numero_endereco, "
                         + "e.bairro     as bairro_endereco "
                         + "FROM professor p "
                         + "INNER JOIN endereco AS e ON p.id_endereco = e.id "
                         + "WHERE p.id = ? "
                         + "ORDER BY p.nome;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            List<Professor> professores = lerProfessor(ps.executeQuery());           

             //if (ObjectUtils.isNotEmpty(enderecos)) {
             if(professores.get(0) != null){
                p = professores.get(0);
            } else {
                throw new ResourceNotFoundException("O professor informado não foi encontrado.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return p;
    }
    
     private List<Professor> lerProfessor(ResultSet rs) throws SQLException {
        List<Professor> professores = new ArrayList<>();
        while (rs.next()) {
            
            Professor p = new Professor();
            p.setId(rs.getInt(1));
            p.setCpf(rs.getString(2));
            p.setNome(rs.getString(3));
            p.setTelefone(rs.getString(4));
            p.setEmail(rs.getString(5));
            p.setSalario(rs.getDouble(6));            
            
            Endereco e = new Endereco();
            e.setId(rs.getInt(7));
            e.setLogradouro(rs.getString(8));
            e.setNumero(rs.getInt(9));
            e.setBairro(rs.getString(10));
            
            p.setEndereco(e);
            
            professores.add(p);
        }
        return professores;
    }

}
