package persistence;

import connection.ConnectionSQLite;
import exception.ResourceNotFoundException;
import model.Professor;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Aluno;
import model.Curso;
import model.Endereco;

public class CursoPersistenceImpl extends UnicastRemoteObject implements CursoPersistence {

    private ConnectionSQLite conn;

    public CursoPersistenceImpl() throws RemoteException {
        super();
    }

    @Override
    public void create(Curso curso) throws RemoteException {
        try {
            conn = new ConnectionSQLite();
            conn.exec("INSERT INTO curso (nome, carga_horaria, horario_inicio, horario_fim, numero_sala) VALUES ('" + curso.getNome() + "','" + curso.getCargaHoraria() + "','" + curso.getHoraInicio() + "','" + curso.getHoraFim() + "'," + curso.getNumSala() + ")");
        } catch (SQLException ex) {
            Logger.getLogger(CursoPersistenceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CursoPersistenceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

   public List<Curso> listAll() {
        List<Curso> cursos = null;
        try(Connection connection = ConnectionSQLite.getConnetion())  {
            String sql = "SELECT "
                    + "c.id , "
                    + "c.nome, "
                    + "c.carga_horaria , "
                    + "c.horario_inicio, "
                    + "c.horario_fim , "
                    + "c.numero_sala "
                    + "FROM curso AS c "
                    + "ORDER BY c.nome;";

            PreparedStatement ps = connection.prepareStatement(sql);
            cursos = lerCurso(ps.executeQuery());
        } catch (SQLException ex) {
           ex.printStackTrace();
        }

        return cursos;
    }

    public Curso findById(Long id) throws ResourceNotFoundException {
        Curso c = new Curso();
        
        try(Connection connection = ConnectionSQLite.getConnetion())  {
            String sql ="SELECT "
                        + "c.id , "
                        + "c.nome, "
                        + "c.carga_horaria , "
                        + "c.horario_inicio, "
                        + "c.horario_fim , "
                        + "c.numero_sala "
                        + "FROM curso c " 
                        + "WHERE id = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, id);
            List<Curso> cursos = lerCurso(ps.executeQuery());
            if (cursos.get(0) != null) {
                c = cursos.get(0);
            } else {
                throw new ResourceNotFoundException("O curso informado não foi encontrado.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return c;
    }
    
     private List<Curso> lerCurso(ResultSet rs) throws SQLException {
        List<Curso> cursos = new ArrayList<>();
        while (rs.next()) {
            Curso c = new Curso();
            c.setId(rs.getLong(1));
            c.setNome(rs.getString(2));
            c.setCargaHoraria(rs.getString(3));
            c.setHoraInicio(rs.getString(4));
            c.setHoraFim(rs.getString(5));
            c.setNumSala(rs.getInt(6));
            
            cursos.add(c);
        }
        return cursos;
    }
}
