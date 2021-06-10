package br.unitins.gameloja.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.unitins.gameloja.model.Peso;
import br.unitins.gameloja.model.Genero;
import br.unitins.gameloja.model.Jogo;
import br.unitins.gameloja.model.TipoPeso;

public class JogoDAO implements DAO<Jogo> {

	@Override
	public boolean inserir(Jogo obj) {
		Connection conn = DAO.getConnection();
		boolean deuErro = false;
		
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO jogo ");
		sql.append(" (nome, descricao, estoque, preco, genero) ");
		sql.append("VALUES ");
		sql.append(" (?, ?, ?, ?, ?) ");
		
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
			stat.setString(1, obj.getNome());
			stat.setString(2, obj.getDescricao());
			stat.setInt(3, obj.getEstoque());
			stat.setDouble(4, obj.getPreco());
			
			if (obj.getGenero() != null)
				stat.setInt(5, obj.getGenero().getValue());
			else 
				stat.setObject(5, null);
			
			stat.execute();
				
			ResultSet rs = stat.getGeneratedKeys();
			if (rs.next()) {
				obj.getPeso().setId(rs.getInt("id"));
				PesoDAO dao = new PesoDAO();
				dao.inserir(obj.getPeso());
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			deuErro = true;
		} finally {
			try {
				stat.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (deuErro)
			return false;
		
		return true;
	}

	@Override
	public boolean alterar(Jogo obj) {
		Connection conn = DAO.getConnection();
		boolean deuErro = false;
		
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE jogo SET ");
		sql.append(" nome = ?, ");
		sql.append(" descricao = ?, ");
		sql.append(" estoque = ?, ");
		sql.append(" preco = ?, ");
		sql.append(" genero = ? ");
		sql.append("WHERE ");
		sql.append(" id = ? ");
	
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(sql.toString());
			stat.setString(1, obj.getNome());
			stat.setString(2, obj.getDescricao());
			stat.setDouble(3, obj.getEstoque());
			stat.setDouble(4, obj.getPreco());
			if (obj.getGenero() != null)
				stat.setInt(5, obj.getGenero().getValue());
			else 
				stat.setObject(5, null);
			
			stat.setInt(6, obj.getId());
			
			stat.execute();
		} catch (Exception e) {
			e.printStackTrace();
			deuErro = true;
		} finally {
			try {
				stat.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (deuErro)
			return false;
		return true;
	}

	@Override
	public boolean excluir(Integer id) {
		Connection conn = DAO.getConnection();
		boolean deuErro = false;
		
		StringBuffer sql = new StringBuffer();
		sql.append("DELETE FROM jogo WHERE id = ? ");
		
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(sql.toString());
			stat.setInt(1, id);
			
			stat.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			deuErro = true;
		} finally {
			try {
				stat.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (deuErro)
			return false;
		
		return true;
	}

	@Override
	public List<Jogo> obterTodos() {
		Connection conn = DAO.getConnection();
		
		List<Jogo> listaJogo = new ArrayList<Jogo>();
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append("  j.id, ");
		sql.append("  j.nome, ");
		sql.append("  j.descricao, ");
		sql.append("  j.estoque, ");
		sql.append("  j.preco, ");
		sql.append("  j.genero, ");
		sql.append("  pe.id AS id_peso, ");
		sql.append("  pe.valor, ");
		sql.append("  pe.tipo_peso ");
		sql.append("FROM ");
		sql.append("  jogo j, ");
		sql.append("  peso pe ");
		sql.append("WHERE ");
		sql.append("  j.id = pe.id ");
		sql.append("ORDER BY j.nome ");
		
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(sql.toString());
			ResultSet rs = stat.executeQuery();
			
			while(rs.next()) {
				Jogo jogo = new Jogo();
				jogo.setId(rs.getInt("id"));
				jogo.setNome(rs.getString("nome"));
				jogo.setDescricao(rs.getString("descricao"));
				jogo.setEstoque(rs.getInt("estoque"));
				jogo.setPreco(rs.getDouble("preco"));
				jogo.setGenero(Genero.valueOf(rs.getInt("genero")));
				
				jogo.setPeso(new Peso());
				jogo.getPeso().setId(rs.getInt("id_peso"));
				jogo.getPeso().setValor(rs.getDouble("valor"));
				jogo.getPeso().setTipoPeso(TipoPeso.valueOf(rs.getInt("tipo_peso")));
				
				listaJogo.add(jogo);
			}
		} catch (Exception e) {
			e.printStackTrace();
			listaJogo = null;
		} finally {
			try {
				stat.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (listaJogo == null || listaJogo.isEmpty())
			return null;
		
		return listaJogo;
	}
	

	public List<Jogo> obterPeloNome(String nome) {
		Connection conn = DAO.getConnection();
		
		List<Jogo> listaJogo = new ArrayList<Jogo>();
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append("  j.id, ");
		sql.append("  j.nome, ");
		sql.append("  j.descricao, ");
		sql.append("  j.estoque, ");
		sql.append("  j.preco, ");
		sql.append("  j.genero, ");
		sql.append("  pe.id AS id_peso, ");
		sql.append("  pe.valor, ");
		sql.append("  pe.tipo_peso ");
		sql.append("FROM ");
		sql.append("  jogo j, ");
		sql.append("  peso pe ");
		sql.append("WHERE ");
		sql.append("  j.id = pe.id ");
		sql.append("  AND j.nome ILIKE ? ");
		sql.append("ORDER BY j.nome ");
		
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(sql.toString());
			stat.setString(1, "%"+nome+"%");
			ResultSet rs = stat.executeQuery();
			
			while(rs.next()) {
				Jogo jogo = new Jogo();
				jogo.setId(rs.getInt("id"));
				jogo.setNome(rs.getString("nome"));
				jogo.setDescricao(rs.getString("descricao"));
				jogo.setEstoque(rs.getInt("estoque"));
				jogo.setPreco(rs.getDouble("preco"));
				jogo.setGenero(Genero.valueOf(rs.getInt("genero")));
				
				jogo.setPeso(new Peso());
				jogo.getPeso().setId(rs.getInt("id_peso"));
				jogo.getPeso().setValor(rs.getDouble("valor"));
				jogo.getPeso().setTipoPeso(TipoPeso.valueOf(rs.getInt("tipo_peso")));
				
				listaJogo.add(jogo);
			}
		} catch (Exception e) {
			e.printStackTrace();
			listaJogo = null;
		} finally {
			try {
				stat.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (listaJogo == null || listaJogo.isEmpty())
			return null;
		
		return listaJogo;
	}
	
	public List<Jogo> obterPelaDescricao(String descricao) {
		Connection conn = DAO.getConnection();
		
		List<Jogo> listaJogo = new ArrayList<Jogo>();
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append("  j.id, ");
		sql.append("  j.nome, ");
		sql.append("  j.descricao, ");
		sql.append("  j.estoque, ");
		sql.append("  j.preco, ");
		sql.append("  j.genero, ");
		sql.append("  pe.id AS id_peso, ");
		sql.append("  pe.valor, ");
		sql.append("  pe.tipo_peso ");
		sql.append("FROM ");
		sql.append("  jogo j, ");
		sql.append("  peso pe ");
		sql.append("WHERE ");
		sql.append("  j.id = pe.id ");
		sql.append("  AND j.descricao ILIKE ? ");
		sql.append("ORDER BY j.nome ");
		
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(sql.toString());
			stat.setString(1, "%"+descricao+"%");
			ResultSet rs = stat.executeQuery();
			
			while(rs.next()) {
				Jogo jogo = new Jogo();
				jogo.setId(rs.getInt("id"));
				jogo.setNome(rs.getString("nome"));
				jogo.setDescricao(rs.getString("descricao"));
				jogo.setEstoque(rs.getInt("estoque"));
				jogo.setPreco(rs.getDouble("preco"));
				jogo.setGenero(Genero.valueOf(rs.getInt("genero")));
				
				jogo.setPeso(new Peso());
				jogo.getPeso().setId(rs.getInt("id_peso"));
				jogo.getPeso().setValor(rs.getDouble("valor"));
				jogo.getPeso().setTipoPeso(TipoPeso.valueOf(rs.getInt("tipo_peso")));
				
				listaJogo.add(jogo);
			}
		} catch (Exception e) {
			e.printStackTrace();
			listaJogo = null;
		} finally {
			try {
				stat.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (listaJogo == null || listaJogo.isEmpty())
			return null;
		
		return listaJogo;
	}


	@Override
	public Jogo obterUm(Integer id) {
		Connection conn = DAO.getConnection();
		
		Jogo jogo = null;
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append("  j.id, ");
		sql.append("  j.nome, ");
		sql.append("  j.descricao, ");
		sql.append("  j.estoque, ");
		sql.append("  j.preco, ");
		sql.append("  j.genero ");
		sql.append("FROM ");
		sql.append("  jogo j ");
		sql.append("WHERE ");
		sql.append("  j.id = ? ");
		
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(sql.toString());
			stat.setInt(1, id);
			
			ResultSet rs = stat.executeQuery();
			
			if(rs.next()) {
				jogo = new Jogo();
				jogo.setId(rs.getInt("id"));
				jogo.setNome(rs.getString("nome"));
				jogo.setDescricao(rs.getString("descricao"));
				jogo.setEstoque(rs.getInt("estoque"));
				jogo.setPreco(rs.getDouble("preco"));
				jogo.setGenero(Genero.valueOf(rs.getInt("genero")));
				
				PesoDAO dao = new PesoDAO();
				jogo.setPeso(dao.obterUm(jogo.getId()));
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			jogo = null;
		} finally {
			try {
				stat.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return jogo;
	}

}
