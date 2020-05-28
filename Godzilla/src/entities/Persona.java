package entities;

import java.sql.Date;
import java.util.HashMap;

public abstract class Persona 
{
	private String cf, nome, cognome, residenza;
	private Date dataNascita;
	
	public Persona(String cf, String nome, String cognome, String residenza, Date dataNascita) 
	{
		this.cf = cf;
		this.nome = nome;
		this.cognome = cognome;
		this.residenza = residenza;
		this.dataNascita = dataNascita;
	}
	
	public HashMap<String,String> toMap()
	{
		HashMap<String,String> ris = new HashMap<String,String>();
		
		ris.put("cf", cf);
		ris.put("nome", nome);
		ris.put("cognome", cognome);
		ris.put("residenza", residenza);
		ris.put("datanascita", dataNascita + "");		
		
		return ris;		
	}

	public String getCf() {
		return cf;
	}

	public void setCf(String cf) {
		this.cf = cf;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getResidenza() {
		return residenza;
	}

	public void setResidenza(String residenza) {
		this.residenza = residenza;
	}

	public Date getDataNascita() {
		return dataNascita;
	}

	public void setDataNascita(Date dataNascita) {
		this.dataNascita = dataNascita;
	}
	
}
