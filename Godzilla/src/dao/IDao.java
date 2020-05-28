package dao;

import java.sql.*;
import java.util.ArrayList;

import entities.*;

public interface IDao 
{
	public Connection getConnection();
	public void caricaCorsi();
	public void caricaPersone();
	
	default Corso cercaCorso(int cod)
	{
		Corso ris = null;
		
		try
		{
			PreparedStatement ps  = getConnection().prepareStatement("select * from corsi where cod = ?");
			ps.setInt(1, cod);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next())
				ris = new Corso (rs.getInt("cod"), rs.getString("nome"), rs.getDate("datainizio"), rs.getDate("datafine"));			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}		
		return ris;		
	}
	
	default Persona cercaPersona(String cf)
	{
		Persona ris = null;
		
		try
		{
			PreparedStatement p1 = getConnection().prepareStatement("select * from perpersonale where cf = ?");
			p1.setString(1, cf);			
			ResultSet rs1 = p1.executeQuery();
			
			while(rs1.next())
			{
				ris =   new Personale (
						rs1.getString("cf"), 
						rs1.getString("nome"),
						rs1.getString("cognome"),
						rs1.getString("residenza"),
						rs1.getDate("datanascita"), 
						rs1.getString("mansione"),
						rs1.getInt("oresett"),
						rs1.getDouble("pagaor")
									);			
			}
			
			if(ris == null)
			{
				PreparedStatement p2 = getConnection().prepareStatement("select * from percorsisti where cf = ?");
				p2.setString(1, cf);
				ResultSet rs2 = p2.executeQuery();
				
				while(rs2.next())
				{
					ris = new Corsista (
							rs2.getString("cf"), 
							rs2.getString("nome"),
							rs2.getString("cognome"),
							rs2.getString("residenza"),
							rs2.getDate("datanascita"), 
							rs2.getString("titolostudio"),
							rs2.getInt("votoper"),
							rs2.getInt("vototec"),
							rs2.getInt("ncolloqui"),
							caricaCorsi(rs2.getString("cf"))
										);			
				}				
			}			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}		
		return ris;
	}
	
	default Corso cercaCorsoOrm(int cod)
	{
		Corso ris = null;
		
		for(Corso i : getCorsi())
			if(i.getCod() == cod)
				ris = i;		
		return ris;		
	}
	
	default Persona cercaPersonaOrm(String cf)
	{
		Persona ris = null;
		
		for(Persona p : getPersone())
			{
			if(p instanceof Personale)
				ris = new Personale(p.getCf(), p.getNome(), p.getCognome(), p.getResidenza(), p.getDataNascita(), ((Personale) p).getMansione(), ((Personale) p).getOreSettimanale(), ((Personale) p).getPagaOre());
			
			if(p instanceof Corsista)
				ris = new Corsista(p.getCf(), p.getNome(), p.getCognome(), p.getResidenza(), p.getDataNascita(), ((Corsista) p).getTitoloStudio(), ((Corsista) p).getVotoPer(), ((Corsista) p).getVotoTec(), ((Corsista) p).getnColoqui(), ((Corsista) p).getCorsi());				
			}		
		
		return ris;
	}
	
	public ArrayList<Corso> getCorsi();
	public ArrayList<Persona> getPersone();
	public ArrayList<Corso> caricaCorsi(String cf);
	
	public ArrayList<Corsista> javisti();	
	public ArrayList<Corsista> javistiOrm();
	
	public int nPartecipanti(int cod); //Dato il codice di un corso dire quanti sono i partecipanti
	public int nPartecipantiOrm(int cod);
	public ArrayList<Corsista> cercaPerCorsoOrm(String nomeCorso);
	public ArrayList<Corsista> cercaPerCorso(String nomeCorso);
	
	default boolean salvaCorso(Corso c)
	{
		boolean ris = false;
		
		try
		{
			PreparedStatement p = getConnection().prepareStatement("insert into corsi (nome, datainizio, datafine) values (?,?,?)");
			p.setString(1, c.getNome());
			p.setDate(2, c.getDataInizio());
			p.setDate(3, c.getDataFine());
					
			if(p.executeUpdate() >= 0)
			{
				ris = true;
				caricaCorsi();
			}			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}		
		
		return ris;		
	}
	
	default boolean salvaPersona(String cf, String nome, String cognome, String residenza, Date dataNascita)
	{
		boolean ris = false;
		
		try
		{
			PreparedStatement p = getConnection().prepareStatement("insert into persone values (?,?,?,?,?)");
			p.setString(1, cf);
			p.setString(2, nome);
			p.setString(3, cognome);			
			p.setString(4, residenza);
			p.setDate(5, dataNascita);
					
			if(p.executeUpdate() >= 0)
			{
				ris = true;				
			}			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}		
		
		return ris;		
	}
	
	default boolean salvaPersonale(String cf, String mansione, int oreSett, double pagaOre)
	{
		boolean ris = false;
		boolean temp = false;
				
		try
		{
			PreparedStatement p = getConnection().prepareStatement("select * from persone where cf = ?");
			p.setString(1, cf);
			ResultSet rs = p.executeQuery();
			
			if(rs.next())
				temp = true;
			
			if(temp)
				{
				PreparedStatement p1 = getConnection().prepareStatement("insert into personale values (?, ?, ?, ?)");
				p1.setString(1, cf);
				p1.setString(2, mansione);
				p1.setInt(3, oreSett);
				p1.setDouble(4, pagaOre);
				if(p1.executeUpdate() >= 0)
					{
					ris = true;
					caricaPersone();
					}
				}					
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}		
		return ris;				
	}
	
	default boolean salvaCorsista(String cf, String titoloStudio, int votoPer, int votoTec , int nColloqui)
	{
		boolean ris = false;
		boolean temp = false;
				
		try
		{
			PreparedStatement p = getConnection().prepareStatement("select * from persone where cf = ?");
			p.setString(1, cf);
			ResultSet rs = p.executeQuery();
			
			if(rs.next())
				temp = true;
			
			if(temp)
				{
				PreparedStatement p1 = getConnection().prepareStatement("insert into corsisti values (?, ?, ?, ?, ?)");
				p1.setString(1, cf);
				p1.setString(2, titoloStudio);
				p1.setInt(3, votoPer);
				p1.setInt(4, votoTec);
				p1.setInt(5, nColloqui);
				if(p1.executeUpdate() >= 0)
					{
					ris = true;
					caricaPersone();
					}
				}					
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}		
		return ris;				
	}
	
	public boolean addPartecipente(String cf, int cod);
	public boolean cancellaCorso(int cod);
	
	public boolean cancellaPersonale(String cf);
	public boolean cancellaCorsista(String cf);
	public ArrayList<Corsista> infoCorsista(int cod);
	public Corso infoCorso(int cod);
	public boolean accesso(String user,String pass);
	
	public void registra(String user, String pass);
	

}
