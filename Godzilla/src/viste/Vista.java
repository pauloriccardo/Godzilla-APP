package viste;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import entities.*;

public class Vista 
{
	private String percorsoViste;
	
	public Vista(String percorsoViste)
	{
		this.percorsoViste = percorsoViste;
	}
	
	public String carica(String nomeFile)
	{
		String ris = "";
		
		try
		{
			Scanner file = new Scanner(new File(percorsoViste + "\\" + nomeFile + ".html"));
			
			while(file.hasNextLine())
				ris += file.nextLine();
		}
		catch(Exception e)
		{
			ris = "404 - File " + nomeFile + " non trovato.";
		}		
		return ris;
	}
	
	public String graficaCorso(Corso c)
	{
		String ris = carica("templatecorso");
		
		for(String chiave : c.toMap().keySet() )
			ris = ris.replace("[" + chiave + "]", c.toMap().get(chiave));		
		
		return ris;
	}
	
	public String graficaCorsi(ArrayList<Corso> corsi)
	{
		String ris = "";
		
		for(Corso c : corsi)
			ris += graficaCorso(c);
		
		return ris;
	}
	
	public String graficaPersonale(Persona p)
	{
		String ris = carica("templatepersonale");
		
		for(String chiave : p.toMap().keySet() )
			ris = ris.replace("[" + chiave + "]", p.toMap().get(chiave));		
		
		return ris;
	}
	
	public String graficaPersonale(ArrayList<Persona> persone)
	{
		String ris = "";
		
		for(Persona p : persone)
			if( p instanceof Personale)
				ris += graficaPersonale(p);
		
		return ris;
	}
	
	public String graficaCorsista(Persona c)
	{
		String ris = carica("templatecorsista");
		
		for(String chiave : c.toMap().keySet() )
			ris = ris.replace("[" + chiave + "]", c.toMap().get(chiave));		
		
		return ris;
	}
	
	public String graficaCorsisti(ArrayList<Persona> corsisti)
	{
		String ris = "";
		
		for(Persona p : corsisti)
			if(p instanceof Corsista)
				ris += graficaCorsista(p);
		
		return ris;
	}
		
	public String graficaCorsisti2(ArrayList<Corsista> corsisti)
	{
		String ris = "";
		
		for(Corsista c : corsisti)
			ris += graficaCorsista(c);
		
		return ris;
	}
	
	public String graficaInfoCorso(Persona c)
	{
		String ris = carica("infocorso");
		
		for(String chiave : c.toMap().keySet() )
			ris = ris.replace("[" + chiave + "]", c.toMap().get(chiave));		
		
		return ris;
	}
	
	public String graficaInfoCorsi(ArrayList<Corsista> corsisti)
	{
		String ris = "";
		
		for(Corsista c : corsisti)
			ris += graficaInfoCorso(c);
						
		
		return ris;
	}
	
	
	
	

}
