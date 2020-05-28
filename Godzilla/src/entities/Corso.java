package entities;

import java.sql.Date;
import java.util.HashMap;

public class Corso 
{
	private int cod;
	private String nome;
	private Date dataInizio, dataFine;
	
	public Corso(int cod, String nome, Date dataInizio, Date dataFine) 
	{
		this.cod = cod;
		this.nome = nome;
		this.dataInizio = dataInizio;
		this.dataFine = dataFine;
	}
	
	public HashMap<String,String> toMap()
	{
		HashMap<String,String> ris = new HashMap<String,String>();
		
		ris.put("cod", cod + "");
		ris.put("nome", nome);
		ris.put("datainizio", dataInizio + "");
		ris.put("datafine", dataFine + "");
		
		return ris;
	}

	public int getCod() {
		return cod;
	}

	public void setCod(int cod) {
		this.cod = cod;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Date getDataInizio() {
		return dataInizio;
	}

	public void setDataInizio(Date dataInizio) {
		this.dataInizio = dataInizio;
	}

	public Date getDataFine() {
		return dataFine;
	}

	public void setDataFine(Date dataFine) {
		this.dataFine = dataFine;
	}
	
}
