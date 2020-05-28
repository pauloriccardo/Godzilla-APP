package web;

import java.io.IOException;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.Dao;
import dao.IDao;
import entities.*;
import viste.Vista;

@WebServlet("/Index")
public class Index extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	IDao d = new Dao();
	Vista v = new Vista("C:\\Users\\paulo\\OneDrive\\Área de Trabalho\\Material Curso Java\\Java Base\\Corso Java\\Godzilla\\WebContent\\viste");
        
    public Index() 
    {
        super();        
    }
	
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		response.setContentType("text/html");
		String ris = "";		
		String comando = request.getParameter("comando");		
		if(comando == null)
			comando = "home";
		
		// Se sulla session l'atributto loggato è null, ovvero non esiste mettilo false
		if(request.getSession().getAttribute("loggato") == null)
			request.getSession().setAttribute("loggato", "no"); // trocado no
		
		//Se non sei loggato ti mando alla pagina di login
		
		if(request.getSession().getAttribute("loggato").equals("no"))
		{
			switch(comando.toLowerCase())
			{
			case "accesso" :
				String user = request.getParameter("user");
				String pass = request.getParameter("password");
				
				if(d.accesso(user,pass))
					{
					request.getSession().setAttribute("loggato", "si");
					ris = v.carica("home");
					}
				else
					ris = "CREDENDIALI ERRATE. <br>" + v.carica("formlogin");
				
				break;			
			default :
				ris = v.carica("formlogin");
			}
			
		}
		// Se sei loggato ti mando allo switch che decide cosa farti fare 
		if(request.getSession().getAttribute("loggato").equals("si"))
		{
			switch(comando.toLowerCase())
			{
			case "home" :
				ris = v.carica("home");
				break;
			case "listacorsi" :
				ris = v.graficaCorsi(d.getCorsi());
				break;
			case "listapersonale" :
				ris = v.graficaPersonale(d.getPersone());
				break;
			case "listacorsisti" :
				ris = v.graficaCorsisti(d.getPersone());
				break;
			case "formnuovocorso" :
				ris = v.carica("formnuovocorso");			
				break;
			case "formnuovapersona" :
				ris = v.carica("formnuovapersona");			
				break;
			case "formnuovopersonale" :
				ris = v.carica("formnuovopersonale");			
				break;
			case "formnuovocorsista" :
				ris = v.carica("formnuovocorsista");			
				break;
			case "nuovocorso" :
				String nome = request.getParameter("nome");
				Date dataIni = Date.valueOf(request.getParameter("datainizio"));
				Date dataFin = Date.valueOf(request.getParameter("datafine"));
				
				Corso c = new Corso(0,nome,dataIni,dataFin);
				
				boolean risposta = d.salvaCorso(c);
				
				if(risposta)
					ris = "Fatto";
				else
					ris = "Problema";
				break;
			case "nuovapersona" :
				String cf = request.getParameter("cf");
				String nomeP = request.getParameter("nome");
				String cognome = request.getParameter("cognome");			
				String residenza = request.getParameter("residenza");
				Date dataNascita = Date.valueOf(request.getParameter("datanascita"));
				
				boolean persona = d.salvaPersona(cf, nomeP, cognome , residenza, dataNascita);
				
				if(persona)
					ris = "Fatto";
				else
					ris = "Problema";
				break;
			case "nuovopersonale" :
				String cfPersonale = request.getParameter("cf");
				String mansione = request.getParameter("mansione");
				int oreSett = Integer.parseInt(request.getParameter("oresett"));
				double pagaOre = Double.parseDouble(request.getParameter("pagaore"));
				
				boolean personale = d.salvaPersonale(cfPersonale, mansione, oreSett, pagaOre);
				
				if(personale)
					ris = "Fatto";
				else
					ris = "Problema";			
				break;
			case "nuovocorsista" :
				
				boolean corsista = d.salvaCorsista(request.getParameter("cf"), request.getParameter("titolostudio"), 
						Integer.parseInt(request.getParameter("votoper")), Integer.parseInt(request.getParameter("vototec")), 
								Integer.parseInt(request.getParameter("ncolloqui")) );	
				
				if(corsista)
					ris = "Fatto";
				else
					ris = "Problema";
				
				break;
			case "javisti" :
				ris = v.graficaCorsisti2(d.javisti());
				break;
				
			case "formcercacorsisticorso" :
				ris = v.carica("formcercacorsisticorso");			
				break;
			case "cercacorsisticorso" :
				
				ris =v.graficaCorsisti2(d.cercaPerCorso(request.getParameter("corso")));
				
				break;
				
			case "formaddpartecipante" :
				ris = v.carica("formaddpartecipante");
				break;
				
			case "addpartecipante" :
				
				if(d.addPartecipente(request.getParameter("cf"), Integer.parseInt(request.getParameter("codicecorso"))))
					ris = "Partecipente associato ao corso ";
				else
					ris = "Qualcosa non va";		
				break;
				
			case "cancellacorso" :
				
				if(d.cancellaCorso(Integer.parseInt(request.getParameter("cod"))))
					ris = "Fatto";
				else
					ris = "Problema ";
				break;
				
			case "cancellapersonale" :
				
				if(d.cancellaPersonale(request.getParameter("cf")))
					ris = "Fatto";
				else
					ris = "Problema ";
				
				break;
				
			case "cancellacorsista" :
				
				if(d.cancellaCorsista(request.getParameter("cf")))
					ris = "Fatto";
				else
					ris = "Problema ";
				
				break;				
				
			case "infocorso" :				
							
				ris = v.graficaCorso(d.infoCorso(Integer.parseInt(request.getParameter("cod")))) + v.graficaInfoCorsi(d.infoCorsista(Integer.parseInt(request.getParameter("cod"))));
				
				break;
				
			case "logout" :
				request.getSession().setAttribute("loggato", "no");
				ris = v.carica("formlogin");				
				break;
				
			}
			
		}
		
		response.getWriter().append(v.carica("menu") + ris);
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{		
		doGet(request, response);
	}

}
