package br.unitins.gameloja.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import br.unitins.gameloja.application.Util;
import br.unitins.gameloja.dao.DAO;
import br.unitins.gameloja.dao.JogoDAO;
import br.unitins.gameloja.model.Peso;
import br.unitins.gameloja.model.Jogo;
import br.unitins.gameloja.model.TipoPeso;
import br.unitins.gameloja.model.Genero;

@Named
@ViewScoped
public class JogoController implements Serializable {
	
	private static final long serialVersionUID = 4153068272054439450L;
	private Jogo jogo = null;
	private List<Jogo> listaJogo = null;
	
	public JogoController() {
		Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
		flash.keep("jogoFlash");
		setJogo((Jogo)flash.get("jogoFlash"));
	}
	
	public Genero[] getListaGenero() {
		return Genero.values();
	}
	
	public TipoPeso[] getListaTipoPeso() {
		return TipoPeso.values();
	}
	
	public void incluir() {
		DAO<Jogo> dao = new JogoDAO();
		if (dao.inserir(getJogo())) {
			Util.addInfoMessage("Inclusão realizada com sucesso.");
			limpar();
		} else
			Util.addErrorMessage("Problemas ao inserir no banco de dados.");
	}
	
	public void alterar() {
		DAO<Jogo> dao = new JogoDAO();
		if (dao.alterar(getJogo())) {
			Util.addInfoMessage("Alteração realizada com sucesso.");
			limpar();
		} else
			Util.addErrorMessage("Problemas ao alterar no banco de dados.");
	}
	
	public void excluir() {
		excluir(getJogo());
	}
	
	public void excluir(Jogo usu) {
		DAO<Jogo> dao = new JogoDAO();
		if (dao.excluir(usu.getId())) {
			Util.addInfoMessage("Exclusão realizada com sucesso.");
			limpar();
		} else
			Util.addErrorMessage("Problemas ao excluir no banco de dados.");
	}
	
	public void limpar() {
		System.out.println("Limpar");
		setJogo(null);
		setListaJogo(null);
	}
	
	public void editar(Jogo usu) {
		DAO<Jogo> dao = new JogoDAO();
		setJogo(dao.obterUm(usu.getId()));
	}
	
	public List<Jogo> getListaJogo() {
		if (listaJogo == null) {
			DAO<Jogo> dao = new JogoDAO();
			listaJogo = dao.obterTodos();
			if (listaJogo == null)
				listaJogo = new ArrayList<Jogo>();
		}
		return listaJogo;
	}

	public void setListaJogo(List<Jogo> listaJogo) {
		this.listaJogo = listaJogo;
	}

	public Jogo getJogo() {
		if (jogo == null) {
			jogo = new Jogo();
			jogo.setPeso(new Peso());
		}
		return jogo;
	}

	public void setJogo(Jogo jogo) {
		this.jogo = jogo;
	}

}
