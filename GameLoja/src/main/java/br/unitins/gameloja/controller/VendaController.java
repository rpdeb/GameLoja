package br.unitins.gameloja.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import br.unitins.gameloja.application.Session;
import br.unitins.gameloja.application.Util;
import br.unitins.gameloja.dao.JogoDAO;
import br.unitins.gameloja.model.ItemVenda;
import br.unitins.gameloja.model.Jogo;

@Named
@ViewScoped
public class VendaController implements Serializable {

	private static final long serialVersionUID = 4860609187938109341L;
	private Integer tipoFiltro;
	private String filtro;
	private List<Jogo> listaJogo;
	
	public void pesquisar() {
		JogoDAO dao = new JogoDAO();
		if (getTipoFiltro() == 1)
			setListaJogo(dao.obterPeloNome(filtro));
		else
			setListaJogo(dao.obterPelaDescricao(filtro));
	}
	
	public void addCarrinho(Jogo jogo) {
		// obtendo o carrinho da sessao
		@SuppressWarnings("unchecked")
		List<ItemVenda> carrinho = (List<ItemVenda>) Session.getInstance().get("carrinho");
		// caso nao exista o carrinho na sessao ... criar uma nova instancia local
		if (carrinho == null) 
			carrinho = new ArrayList<ItemVenda>();
		
		
		ItemVenda iv = new ItemVenda();
		iv.setJogo(jogo);
		iv.setQuantidade(1);
		iv.setValorUnitario(jogo.getPreco());
	
		if (carrinho.contains(iv)) {
			int index = carrinho.indexOf(iv);
			int quantidade = carrinho.get(index).getQuantidade();
			carrinho.get(index).setQuantidade(++ quantidade );
			
		} else {
			carrinho.add(iv);
		}
		
		Session.getInstance().set("carrinho", carrinho);
		
		Util.addInfoMessage("Item adicionado no carrinho.");
		
	}

	public String getFiltro() {
		return filtro;
	}

	public void setFiltro(String filtro) {
		this.filtro = filtro;
	}

	public List<Jogo> getListaJogo() {
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
	
	
}

