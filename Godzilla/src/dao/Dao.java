package dao;

import java.sql.*;
import java.util.ArrayList;

import entities.*;

public class Dao implements IDao
{
	private Connection c = null;
	private ArrayList<Corso> corsi = new ArrayList<Corso>();
	private ArrayList<Persona> persone = new ArrayList<Persona>();
	
	public Dao()
	{
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			c = DriverManager.getConnection("jdbc:mysql://localhost:3306/godzilla?user=root&password=root&useTimezone=true&serverTimezone=UTC");
			caricaCorsi();
			caricaPersone();
		}
		catch(Exception e)
		{
			System.out.println("No DB. terminating");
			e.printStackTrace();
			System.exit(-1);		
		}		
	}

	public Connection getConnection() 
	{		
		return c;
	}

	public void caricaCorsi() 
	{
		corsi.clear(); // Limpar para carregar o array do zero
		try
		{
			PreparedStatement ps = c.prepareStatement("select * from corsi");
			ResultSet rs = ps.executeQuery();
			
			while(rs.next())
			{
				corsi.add(new Corso (
						rs.getInt("cod"), 
						rs.getString("nome"), 
						rs.getDate("datainizio"), 
						rs.getDate("datafine")
									)
						);			
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}		
	}

	public void caricaPersone() 
	{
		persone.clear(); // Limpar para carregar o array do zero
		try
		{
			PreparedStatement ps = c.prepareStatement("select * from perpersonale");
			ResultSet rs = ps.executeQuery();
			
			while(rs.next())
			{
				persone.add(new Personale (
						rs.getString("cf"), 
						rs.getString("nome"),
						rs.getString("cognome"),
						rs.getString("residenza"),
						rs.getDate("datanascita"), 
						rs.getString("mansione"),
						rs.getInt("oresett"),
						rs.getDouble("pagaor")
									)
						);			
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		try
		{
			PreparedStatement ps = c.prepareStatement("select * from percorsisti");
			ResultSet rs = ps.executeQuery();
			
			while(rs.next())
			{
				persone.add(new Corsista (
						rs.getString("cf"), 
						rs.getString("nome"),
						rs.getString("cognome"),
						rs.getString("residenza"),
						rs.getDate("datanascita"), 
						rs.getString("titolostudio"),
						rs.getInt("votoper"),
						rs.getInt("vototec"),
						rs.getInt("ncolloqui"),
						caricaCorsi(rs.getString("cf"))
									)
						);			
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}		
		
	}

	public ArrayList<Corso> getCorsi() 
	{
		return corsi;
	}

	public ArrayList<Persona> getPersone() 
	{
		return persone;
	}

	public ArrayList<Corso> caricaCorsi(String cf) 
	{
		ArrayList<Corso> ris = new ArrayList<Corso>();
		
		try
		{
			PreparedStatement ps = c.prepareStatement("select * from corsi, frequenta where corsi.cod = frequenta.cod and frequenta.cf = ?");
			ps.setString(1, cf);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next())
			{
				ris.add(new Corso (
						rs.getInt("cod"), 
						rs.getString("nome"), 
						rs.getDate("datainizio"), 
						rs.getDate("datafine")
									)
						);			
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}	
		
		return ris;
	}

	public ArrayList<Corsista> javisti() 
	{
		ArrayList<Corsista> ris = new ArrayList<Corsista>();
		
		try
		{
			PreparedStatement ps = c.prepareStatement("select distinct percorsisti.* from percorsisti, corsi, frequenta where percorsisti.cf = frequenta.cf and corsi.cod = frequenta.cod and corsi.nome like '%java%' ");
			ResultSet rs = ps.executeQuery();
			
			while(rs.next())
			{
				ris.add(new Corsista (
						rs.getString("cf"), 
						rs.getString("nome"),
						rs.getString("cognome"),
						rs.getString("residenza"),
						rs.getDate("datanascita"), 
						rs.getString("titolostudio"),
						rs.getInt("votoper"),
						rs.getInt("vototec"),
						rs.getInt("ncolloqui"),
						caricaCorsi(rs.getString("cf"))
									)
						);
				
			}			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}		
		return ris;
	}

	public ArrayList<Corsista> javistiOrm() 
	{
		ArrayList<Corsista> ris = new ArrayList<Corsista>();
		
		for(Persona p : persone)
			if( p instanceof Corsista)
			{
				ArrayList<Corso> temp = ((Corsista) p).getCorsi();
				for(Corso i : temp)
				{
					String corso = i.getNome().toLowerCase();
					if(corso.indexOf("java") >= 0)
						{
						ris.add((Corsista)p);
						break;
						}
				}					
			}		
		return ris;
	}

	public int nPartecipanti(int cod) 
	{
		int ris = 0;
		try
		{
		PreparedStatement ps = c.prepareStatement("select count(*) as tot from frequenta where frequenta.cod = ?");
		ps.setInt(1, cod);
		ResultSet rs = ps.executeQuery();
		
		if(rs.next())
			ris = rs.getInt("tot");		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		return ris;
	}

	public int nPartecipantiOrm(int cod) 
	{
		int ris = 0;
		
		for(Persona p : persone)
		{
			if(p instanceof Corsista && ((Corsista) p).frequentato(cod))
				ris ++;				
		}		
		return ris;
	}

	public ArrayList<Corsista> cercaPerCorsoOrm(String nomeCorso) 
	{
		ArrayList<Corsista> ris = new ArrayList<Corsista>();
		
		for(Persona p : persone)
			if(p instanceof Corsista)
			{
				ArrayList<Corso> temp = ((Corsista) p).getCorsi();
				for(Corso i : temp)
					if(i.getNome().equalsIgnoreCase(nomeCorso))
						ris.add((Corsista)p);				
			}		
		return ris;
	}

	public ArrayList<Corsista> cercaPerCorso(String nomeCorso) 
	{
		ArrayList<Corsista> ris = new ArrayList<Corsista>();
		
		try
		{
			PreparedStatement ps = c.prepareStatement("select percorsisti.* from percorsisti, frequenta, corsi where percorsisti.cf = frequenta.cf and frequenta.cod = corsi.cod and corsi.nome = ?");
			ps.setString(1, nomeCorso);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next())
			{
				ris.add(new Corsista (
						rs.getString("cf"), 
						rs.getString("nome"),
						rs.getString("cognome"),
						rs.getString("residenza"),
						rs.getDate("datanascita"), 
						rs.getString("titolostudio"),
						rs.getInt("votoper"),
						rs.getInt("vototec"),
						rs.getInt("ncolloqui"),
						caricaCorsi(rs.getString("cf"))
									)
						);				
			}			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}		
		return ris;
	}

	public boolean addPartecipente(String cf, int cod) 
	{
		boolean ris = false;
		
		try
		{
			PreparedStatement ps = c.prepareStatement("insert into frequenta values (?, ?)");
			ps.setString(1, cf);
			ps.setInt(2, cod);
			
			if(ps.executeUpdate() >= 0)
				ris = true;			
		}
		catch(Exception e)
		{
			e.printStackTrace();			
		}		
		return ris;
	}

	public boolean cancellaCorso(int cod) 
	{
		boolean ris = true;
		
		if(nPartecipanti(cod) >= 5)
			ris = false;
		else			
			{
				try
				{
					if(nPartecipanti(cod) > 0)
					{
						PreparedStatement p1 = c.prepareStatement("delete from frequenta where cod = ?");
						p1.setInt(1, cod);
						p1.executeUpdate();						
					}
					else
					{
						PreparedStatement p1 = c.prepareStatement("delete from corsi where corsi.cod = ?");
						p1.setInt(1, cod);
						p1.executeUpdate();						
					}
					caricaCorsi();
										
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				
			}		
		return ris;
	}

	public boolean cancellaPersonale(String cf) 
	{
		boolean ris = false;
		
		try
		{
			PreparedStatement p = c.prepareStatement("delete from personale where cf = ?");
			p.setString(1, cf);
			if(p.executeUpdate() >= 0)
				{
				ris = true;
				caricaPersone();
				}			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}			
		
		return ris;
	}

	public boolean cancellaCorsista(String cf) 
	{
		boolean ris = false;		
		
		try
		{
			PreparedStatement p1 = c.prepareStatement("delete from frequenta where cf = ?");
			p1.setString(1, cf);
			p1.executeUpdate();
				
			PreparedStatement p2 = c.prepareStatement("delete from corsisti where cf = ?");
			p2.setString(1, cf);
			p2.executeUpdate();	
			
			ris = true;
			caricaPersone();
		}
		catch(Exception e)
		{
			e.printStackTrace();			
		}		
		return ris;
	}

	public boolean accesso(String user, String pass) 
	{
		boolean ris = false;
		
		try
		{
			PreparedStatement p = c.prepareStatement("select count(*) as tot from users where user = ? and password = md5(?)");
			p.setString(1, user);
			p.setString(2, pass);
			ResultSet rs = p.executeQuery();
			
			if(rs.next())
				if(rs.getInt("tot") > 0 )
					ris = true;			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return ris;
	}

	public ArrayList<Corsista> infoCorsista(int cod) 
	{
		ArrayList<Corsista> ris = new ArrayList<Corsista>();
		
		try
		{
			PreparedStatement p = c.prepareStatement("select * from percorsisti, frequenta where percorsisti.cf = frequenta.cf and frequenta.cod = ?");
			p.setInt(1, cod);
			ResultSet rs = p.executeQuery();
			
			while(rs.next())
			{
				ris.add(new Corsista (
						rs.getString("cf"), 
						rs.getString("nome"),
						rs.getString("cognome"),
						rs.getString("residenza"),
						rs.getDate("datanascita"), 
						rs.getString("titolostudio"),
						rs.getInt("votoper"),
						rs.getInt("vototec"),
						rs.getInt("ncolloqui"),
						caricaCorsi(rs.getString("cf"))
									)
						);
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return ris;
	}
	
	public Corso infoCorso(int cod) 
	{
		Corso ris = null;
		
		try
		{
			PreparedStatement p = c.prepareStatement("select * from corsi where cod = ?");
			p.setInt(1, cod);
			ResultSet rs = p.executeQuery();
			
			while(rs.next())
			{
				ris = new Corso (
						rs.getInt("cod"), 
						rs.getString("nome"), 
						rs.getDate("datainizio"), 
						rs.getDate("datafine")
									);			
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return ris;
	}

	public void registra(String user, String pass) 
	{
		try
		{
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	
	
	
	
	
	

}
