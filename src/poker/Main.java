/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poker;

import java.io.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 *
 * @author fpesic
 */
public class Main
{
	static int pracenje;
	static Podaci svevreme=new Podaci("svevreme"),tridana=new Podaci("tridana");
	
	public static void main(String[] args) throws Exception
	{	time();
		pracenje=2;//0 - ne ispisuje se ništa; 1 - ispisuje se sve; 2 - pritiskaju se enteri da se prati igra;
		svevreme.UcitajPodatke("");
	//	tridana.UcitajPodatke(" posle 3 dana rada");
	//	Podaci.PocniUcenje();
		Cashgame cashgame=new Cashgame(10);
		time();
		cashgame.igraj(pracenje,Double.POSITIVE_INFINITY);//kol'ko ruku se igra
		svevreme.IspisiPodatke("");
		tridana.IspisiPodatke(" posle 3 dana rada");
		time();
	}
	
	static {
	/*Ruka.ucitajistoriju();
	Ruka.ucitajistoriju(0);
	Ruka.ucitajistoriju(1);
	Ruka.ucitajistoriju(2);
	Obradiistoriju();


	Ruka.ispisiistoriju();
	Ruka.ispisiistoriju(0);
	Ruka.ispisiistoriju(1);
	Ruka.ispisiistoriju(2);*/
	}
	
	static void Obradiistoriju()
	{	/*for(vector<Istorija>.iterator istorija1=Ruka.istorija.begin();istorija1!=Ruka.istorija.end();)
			if((*istorija1).nagrada<-1000||(*istorija1).nagrada>1000)
				Ruka.istorija.erase(istorija1);
			else
				istorija1++;*/
		/*for(int i=0;i<Ruka.istorija.size();i++)
			if(Ruka.istorija.get(i).akcija==9)
				Ruka.istorija.get(i).nagrada=Ruka.istorija.get(i).verflop;*/

	}

	public static void time()
	{	LocalTime time = LocalTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
		System.out.println(time.format(formatter));
	}
}
