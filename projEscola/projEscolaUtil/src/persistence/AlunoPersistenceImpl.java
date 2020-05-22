package persistence;

import connection.ConnectionSQLite;
import exception.ResourceCannotRemovedException;
import exception.ResourceNotFoundException;
import model.Pessoa;
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
import model.Curso;

public class AlunoPersistenceImpl extends UnicastRemoteObject implements AlunoPersistence {

    private ConnectionSQLite conn;

    public AlunoPersistenceImpl() throws RemoteException {
        super();
    }

    @Override
    public void create(Aluno a) {
        try {
            
            //ConnectionSQLite conexao = new ConnectionSQLite();
            conn = new ConnectionSQLite();       
            conn.exec("INSERT INTO aluno (cpf, ra, nome, telefone, email, id_endereco, id_curso) VALUES ('" + a.getCpf()+ "','" + a.getRa() + "','" + a.getNome()+ "','" + a.getTelefone()+ "','" + a.getEmail()+ "','" + a.getEndereco().getId() + "','" + a.getCurso().getId() + "')");

            //PreparedStatement ps = conexao.prepareStatement(sql);
            //ps.setString(1, a.getCpf());
            
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }                
    }

    @Override
    public List<Aluno> listAll() {
        List<Aluno> alunos = null;
        
        try (Connection connection = ConnectionSQLite.getConnetion()) {

            String sql = "SELECT "
                        + "a.id         as id_aluno, "
                        + "a.cpf        as cpf_aluno, "
                        + "a.nome       as nome_aluno, "
                        + "a.telefone   as telefone_aluno, "
                        + "a.email      as email_aluno, "
                        + "e.id         as id_endereco, "
                        + "e.logradouro as logradouro_endereco, "
                        + "e.numero     as numero_endereco, "
                        + "e.bairro     as bairro_endereco, "
                        + "c.id         as id_curso, "
                        + "c.nome as nome_curso "               
                        + "FROM aluno AS a "
                        + "INNER JOIN curso AS c ON a.id_curso = c.id "
                        + "INNER JOIN endereco AS e ON e.id = a.id_endereco "
                        + "ORDER BY a.nome;";

            PreparedStatement ps = connection.prepareStatement(sql);
            alunos = lerAlunos(ps.executeQuery());

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return alunos;
    }
    
    private List<Aluno> lerAlunos(ResultSet rs) throws SQLException {
        List<Aluno> alunos = new ArrayList<>();
        while (rs.next()) {
            Aluno a = new Aluno();
            a.setId(rs.getInt(1));
            a.setCpf(rs.getString(2));
            a.setNome(rs.getString(3));
            a.setTelefone(rs.getString(4));
            a.setEmail(rs.getString(5));
            
            Endereco e = new Endereco();
            e.setId(rs.getInt(6));
            e.setLogradouro(rs.getString(7));            
            e.setNumero(rs.getInt(8));
            e.setBairro(rs.getString(9));
            
            Curso c = new Curso();
            c.setId(rs.getInt(10));
            c.setNome(rs.getString(11));
            
            a.setEndereco(e);
            a.setCurso(c);
            
            alunos.add(a);
        }
        return alunos;
    }
}
