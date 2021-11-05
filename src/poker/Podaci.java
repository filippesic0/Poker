/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 *
 * @author filip
 */
public class Podaci
{	
	String id;
	public final int brojakcija[]={13,22,22,22};//za 1 manje
	//Sto se tice biranja akcije na random: Jaci call i bet se mogu birati samo ako su slabiji call i bet dobra ideja.
	//ispod 100 call, a iznad bet.
/*	1. dimenzija su 2 karte,
	2. koliko ljudi je diglo ulog,
	3. broj igrača za stolom,
	4. broj igrača sa kartama i parama u ruci,
	5. pozicija,
	6. akcija.*/
	public double Q[][][][][][]=new double[Karta.dvekarte.length][3][5][4][4][brojakcija[0]];
	public int Qbrojac[][][][][][]=new int[Karta.dvekarte.length][3][5][4][4][brojakcija[0]];
/*	1. dimenzija je flop/turn/river,
	2. dimenzija je verovatnoća,
	3. koliko ljudi je diglo ulog,
	4. broj igrača za stolom,
	5. broj igrača sa kartama i parama u ruci,
	6. pozicija,
	7. odnos novac/pot,
	8. akcija.*/
	public double Qflopturnriver[][][][][][][][]=new double[3][101][3][5][4][5][Istorija.novackrozppot.length][brojakcija[1]];
	
	public Podaci(String s)
	{
		id=s;
	}
	
	void PocniUcenje()
	{	int a,flop,karte,v,b,n=2,n1,po,np;//radimo samo za 5 igrača
	/*	for(karte=0;karte<60;karte++)
			for(b=0;b<3;b++)
				for(n=0;n<5;n++)
					for(n1=0;n1<4;n1++)
						for(po=0;po<4;po++)
							for(a=1;a<brojakcija[0];a++)
								Q[karte][b][n][n1][po][a]=0;


		for(karte=0;karte<60;karte++)
			for(b=0;b<3;b++)
				for(n=0;n<5;n++)
					for(n1=0;n1<4;n1++)
						for(po=0;po<4;po++)
							for(a=1;a<brojakcija[0];a++)
								Qbrojac[karte][b][n][n1][po][a]=0;*/

		//flop,turn i river:
		for(flop=0;flop<3;flop++)
			for(v=0;v<101;v++)
				for(b=0;b<3;b++)
					//for(n=0;n<5;n++)
						for(n1=0;n1<4;n1++)
							for(po=0;po<5;po++)
								for(np=0;np<Istorija.novackrozppot.length;np++)
									for(a=1;a<brojakcija[1];a++)
										Qflopturnriver[flop][v][b][n][n1][po][np][a]=0;
	}
	
	void UcitajPodatke(String s) throws FileNotFoundException
	{	File file = new File("Podacipreflop"+s+".txt");
		File file1 = new File("Podaci"+s+".txt");
		Scanner podacipreflop = new Scanner(file); 
		Scanner podaci = new Scanner(file1);
		int a,flop,karte,v,b,n=4,n1,po,np;//radimo samo za 10 igrača
		try{
			for(karte=0;karte<60;karte++)
				for(b=0;b<3;b++)
					//for(n=0;n<5;n++)//radimo samo za 10 igrača
						for(n1=0;n1<4;n1++)
							for(po=0;po<4;po++)
								for(a=1;a<brojakcija[0];a++)
									Q[karte][b][n][n1][po][a]=podacipreflop.nextDouble();


			for(karte=0;karte<60;karte++)
				for(b=0;b<3;b++)
					//for(n=0;n<5;n++)
						for(n1=0;n1<4;n1++)
							for(po=0;po<4;po++)
								for(a=1;a<brojakcija[0];a++)
									Qbrojac[karte][b][n][n1][po][a]=podacipreflop.nextInt();

			//flop,turn i river:
			for(flop=0;flop<3;flop++)
				for(v=0;v<101;v++)
					for(b=0;b<3;b++)
						//for(n=0;n<5;n++)
							for(n1=0;n1<4;n1++)
								for(po=0;po<5;po++)
									for(np=0;np<Istorija.novackrozppot.length;np++)
										for(a=1;a<brojakcija[1];a++)
											Qflopturnriver[flop][v][b][n][n1][po][np][a]=podaci.nextDouble();
		}catch(Exception e){
			a=0;
		}
		podacipreflop.close();
		podaci.close();
	}
	
	public void IspisiPodatke(String s) throws IOException
	{	String niz[]={"25","67","89","10"," J"," Q"," K"," A"};
		String niz1[]={"22","66","88","10"," J"," Q"," K"," A"};
		String niz2[]={"55","77","99","10"," J"," Q"," K"," A"};
		FileWriter fileWriter = new FileWriter("Podacipreflop"+s+".txt");
		FileWriter fileWriter1 = new FileWriter("Podaci"+s+".txt");
		FileWriter fileWriter2 = new FileWriter("Matrice.txt");
		PrintWriter podacipreflop = new PrintWriter(fileWriter);
		PrintWriter podaci = new PrintWriter(fileWriter1);
		PrintWriter matrice = new PrintWriter(fileWriter2);
		double broj=0;
		try{
			int a,flop,karte,v,b,n=4,n1,po,np;//radimo samo za 10 igrača
			for(karte=0;karte<60;karte++)
			for(b=0;b<3;b++)
			//for(n=0;n<5;n++)
			for(n1=0;n1<4;n1++)
			for(po=0;po<4;po++)
			{	for(a=1;a<brojakcija[0];a++)
					podacipreflop.printf("%f ",Q[karte][b][n][n1][po][a]);
				podacipreflop.println();
			}
			
			for(karte=0;karte<60;karte++)
			for(b=0;b<3;b++)
			//for(n=0;n<5;n++)
			for(n1=0;n1<4;n1++)
			for(po=0;po<4;po++)
			{	for(a=1;a<brojakcija[0];a++)
					podacipreflop.printf("%d ",Qbrojac[karte][b][n][n1][po][a]);
				podacipreflop.println();
			}
			podacipreflop.println();
			
			for(karte=0;karte<60;karte++)
			{	matrice.printf("%-6s",Karta.dvekarte[karte]);
				for(a=1;a<brojakcija[0];a++)
				{	broj=0;
					for(po=0;po<4;po++)
						broj+=Q[karte][0][n][1][po][a];
					matrice.printf("%5.0f",250*broj);
				}
				matrice.println();
			}
			matrice.println();

			float broj1[]=new float[brojakcija[1]];
			int broj2[]=new int[brojakcija[1]];
			//flop,turn i river:
			for(flop=0;flop<3;flop++)
			{	int i;
				for(i=3,v=0;v<101;v++,i++)
				{	for(b=0;b<3;b++)
					//for(n=0;n<5;n++)
					for(n1=0;n1<4;n1++)
					for(po=0;po<5;po++)
					{	for(np=0;np<Istorija.novackrozppot.length;np++)
						{	for(a=1;a<brojakcija[1];a++)
							{	broj=Qflopturnriver[flop][v][b][n][n1][po][np][a];
								podaci.printf("%f ",broj);
								if(np==9)
									broj1[a]+=Qflopturnriver[flop][v][b][n][n1][po][np][a];
							}
							podaci.println();
						}
					}
					if(i==5||v==100)
					{	double verovatnoca=v/100.0-0.02;
						matrice.printf("%-6.2f",verovatnoca);
						for(a=1;a<brojakcija[1];a++)
							matrice.printf("%5.0f",74*broj1[a]);
						broj1=new float[brojakcija[1]];
						i=0;
					matrice.println();
					}
					//System.out.println(v);
				}
				//System.out.println();
				//System.out.println(flop);
				//System.out.println();
				matrice.println();
			}
			
		}catch(Exception e){
			int i=0;
		}
		podacipreflop.close();
		podaci.close();
		matrice.close();
	}
	
}
