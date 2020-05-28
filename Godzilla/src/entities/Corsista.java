package entities;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;

public class Corsista extends Persona 
{
	private String titoloStudio;
	private int votoPer, votoTec, nColoqui;
	private ArrayList<Corso> corsi;
	
	public Corsista(String cf, String nome, String cognome, String residenza, Date dataNascita, String titoloStudio,
			int votoPer, int votoTec, int nColoqui, ArrayList<Corso> corsi) 
	{
		super(cf, nome, cognome, residenza, dataNascita);
		this.titoloStudio = titoloStudio;
		this.votoPer = votoPer;
		this.votoTec = votoTec;
		this.nColoqui = nColoqui;
		this.corsi = corsi;
	}
	
	public HashMap<String,String> toMap()
	{
		HashMap<String,String> ris = super.toMap();
		
		ris.put("titolostudio", titoloStudio);
		ris.put("votoper", votoPer + "");
		ris.put("vototec", votoTec + "");
		ris.put("ncoloqui", nColoqui + "");
		ris.put("corsi", corsi + "");
		
		return ris;		
	}
	
	public boolean frequentato(int cod)
	{
		boolean ris = false;
		
		for(Corso c : corsi)
			if(c.getCod() == cod)
				{
				ris = true;
				break;
				}
		
		return ris;		
	}

	public String getTitoloStudio() {
		return titoloStudio;
	}

	public void setTitoloStudio(String titoloStudio) {
		this.titoloStudio = titoloStudio;
	}

	public int getVotoPer() {
		return votoPer;
	}

	public void setVotoPer(int votoPer) {
		this.votoPer = votoPer;
	}

	public int getVotoTec() {
		return votoTec;
	}

	public void setVotoTec(int votoTec) {
		this.votoTec = votoTec;
	}

	public int getnColoqui() {
		return nColoqui;
	}

	public void setnColoqui(int nColoqui) {
		this.nColoqui = nColoqui;
	}

	public ArrayList<Corso> getCorsi() {
		return corsi;
	}
	
}
