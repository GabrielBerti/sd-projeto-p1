package persistence;

import connection.ConnectionSQLite;
import exception.ResourceCannotRemovedException;
import exception.ResourceNotFoundException;
import model.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CursoProfessorPersistenceImpl extends UnicastRemoteObject implements CursoProfessorPersistence {

    private ConnectionSQLite conn;

    public CursoProfessorPersistenceImpl() throws RemoteException {
        super();
    }

    public void create(CursoProfessor cp) {
        try {
            conn = new ConnectionSQLite();
            conn.exec("INSERT INTO curso_professor (id_professor, id_curso) VALUES (" + cp.getProfessor().getId()+ "," + cp.getCurso().getId() +")");
        } catch (SQLException ex) {
            Logger.getLogger(CursoProfessorPersistenceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CursoProfessorPersistenceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }                
    }

    @Override
    public List<CursoProfessor> listAll() {
        List<CursoProfessor> cursoProfessores = null;
        try (Connection connection = ConnectionSQLite.getConnetion()) {
            String sql = "SELECT "                        
                        + "cp.id_curso     as id_curso, "
                        + "c.nome          as nome_curso, "
                        + "cp.id_professor as id_professor, "
                        + "p.cpf           as cpf_professor, "
                        + "p.nome          as nome_professor, "
                        + "p.telefone      as telefone_professor, "
                        + "p.email         as email_professor, "
                        + "p.salario       as salario_professor, "                
                        + "e.id            as id_endereco, "
                        + "e.logradouro    as logradouro_enedereco, "
                        + "e.numero        as numero_endereco, "
                        + "e.bairro        as bairro_endereco "                   
                        + "FROM curso_professor AS cp "
                        + "INNER JOIN curso AS c ON cp.id_curso = c.id "
                        + "INNER JOIN professor AS p ON cp.id_professor = p.id "
                        + "INNER JOIN endereco AS e ON p.id_endereco = e.id "
                        + "ORDER BY c.nome, p.nome;";

            PreparedStatement ps = connection.prepareStatement(sql);
            cursoProfessores = lerCursoProfessor(ps.executeQuery());
        
        } catch (SQLException ex) {
            Logger.getLogger(CursoProfessorPersistenceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return cursoProfessores;
    }
    
     private List<CursoProfessor> lerCursoProfessor(ResultSet rs) throws SQLException {
        List<CursoProfessor> cursosProfessores = new ArrayList<>();
        while (rs.next()) {
            
            Curso c = new Curso();
            c.setId(rs.getLong(1));
            c.setNome(rs.getString(2));
            
            Professor p = new Professor();
            p.setId(rs.getInt(3));
            p.setCpf(rs.getString(4));
            p.setNome(rs.getString(5));
            p.setTelefone(rs.getString(6));
            p.setEmail(rs.getString(7));
            p.setSalario(rs.getDouble(8));            
            
            Endereco e = new Endereco();
            e.setId(rs.getInt(9));
            e.setLogradouro(rs.getString(10));
            e.setNumero(rs.getInt(11));
            e.setBairro(rs.getString(12));
            
            p.setEndereco(e);
            
            CursoProfessor cp = new CursoProfessor();
            cp.setCurso(c);
            cp.setProfessor(p);
            
            cursosProfessores.add(cp);
        }
        return cursosProfessores;
    }
}
