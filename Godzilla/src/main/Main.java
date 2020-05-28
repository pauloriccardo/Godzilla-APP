package main;

import dao.*;
import entities.*;

public class Main 
{

	public static void main(String[] args) 
	{
		IDao d = new Dao();
		
			
		System.out.println(d.cercaPerCorsoOrm("Java Base"));
		System.out.println(d.cercaPerCorso("Java Base"));
		
		System.out.println(d.javisti());
		
		
		
		

	}

}
