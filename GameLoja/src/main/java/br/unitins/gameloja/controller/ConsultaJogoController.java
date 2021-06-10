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

@Named
@ViewScoped
public class ConsultaJogoController implements Serializable {
	
	private static final long serialVersionUID = -7607053657604748068L;
	private List<Jogo> listaJogo = null;
	private Integer tipoFiltro;
	private String filtro;
	
	public String novoJogo() {
		return "jogo.xhtml?faces-redirect=true";
	}
	
	public String editar(Jogo usu) {
		JogoDAO dao = new JogoDAO();
		Jogo jogo = dao.obterUm(usu.getId());
		Flash flash =  FacesContext.getCurrentInstance().getExternalContext().getFlash();
		flash.put("jogoFlash", jogo);
		
		return "jogo.xhtml?faces-redirect=true";
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

	public Integer getTipoFiltro() {
		return tipoFiltro;
	}

	public void setTipoFiltro(Integer tipoFiltro) {
		this.tipoFiltro = tipoFiltro;
	}

	public String getFiltro() {
		return filtro;
	}

	public void setFiltro(String filtro) {
		this.filtro = filtro;
	}
	
}
