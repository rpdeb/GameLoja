package br.unitins.gameloja.model;

public enum Genero {
	ACAO (1, "Ação"), 
	RPG (2, "Rpg"), 
	ESPORTE (3, "Esporte");
	
	private int value;
	private String label;
	
	Genero (int value, String label) {
		this.value = value;
		this.label = label;
	}
	
	public int getValue() {
		return value;
	}
	
	public String getLabel() {
		return label;
	}
	
	public static Genero valueOf(Integer value) {
		if (value == null)
			return null;
		
		for (Genero genero : Genero.values()) {
			if (genero.getValue() == value) {
				return genero;
			}
		}
		return null;
	}
}
