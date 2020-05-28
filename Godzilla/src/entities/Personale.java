package entities;

import java.sql.Date;
import java.util.HashMap;

public class Personale extends Persona 
{
	private String mansione;
	private int oreSettimanale;
	private double pagaOre;
		
	public Personale(String cf, String nome, String cognome, String residenza, Date dataNascita, String mansione,
			int oreSettimanale, double pagaOre) 
	{
		super(cf, nome, cognome, residenza, dataNascita);
		this.mansione = mansione;
		this.oreSettimanale = oreSettimanale;
		this.pagaOre = pagaOre;
	}
	
	public double stipendio()
	{
		return pagaOre * oreSettimanale * 4;
	}
	
	public HashMap<String,String> toMap()
	{
		HashMap<String,String> ris = super.toMap();
		
		ris.put("mansione", mansione);
		ris.put("oresettimanale", oreSettimanale + "");
		ris.put("pagaore", pagaOre + "");
		
		return ris;		
	}

	public String getMansione() {
		return mansione;
	}

	public void setMansione(String mansione) {
		this.mansione = mansione;
	}

	public int getOreSettimanale() {
		return oreSettimanale;
	}

	public void setOreSettimanale(int oreSettimanale) {
		this.oreSettimanale = oreSettimanale;
	}

	public double getPagaOre() {
		return pagaOre;
	}

	public void setPagaOre(double pagaOre) {
		this.pagaOre = pagaOre;
	}
	
}
