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
	
	
	
    public static void main(String[] args) throws Exception
	{	time();
		pracenje=1;//0 - ne ispisuje se ništa; 1 - ispisuje se sve; 2 - pritiskaju se enteri da se prati igra;
		/*Popunjavanje niza podela verovatnoća:*/
		for(int i=0;i<10;i++)
		{   Igrac.podelaverovatnoca[i]=i*0.04;
			Igrac.podelaverovatnoca[10+i]=0.4+i*0.03;
			Igrac.podelaverovatnoca[20+i]=0.7+i*0.02;
			Igrac.podelaverovatnoca[30+i]=0.9+i*0.01;
		}
	//	UcitajPodatke();
		PocniUcenje();
		Cashgame cashgame=new Cashgame(10);
		cashgame.igraj(pracenje,Double.POSITIVE_INFINITY);//kol'ko ruku se igra
		time();
		IspisiPodatke();
		//getchar();
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
	
	static void PocniUcenje()
	{	for(int i=0;i<9;i++)
			for(int j=0;j<8;j++)
				for(int k=0;k<10;k++)
				{	Igrac.Q[i][j][k]=0;
					Igrac.Qbrojac[i][j][k]=0;
				}
		for(int i=0;i<8;i++)
			for(int j=0;j<8;j++)
				for(int k=0;k<10;k++)
				{	Igrac.Qboja[i][j][k]=0;
					Igrac.Qbojabrojac[i][j][k]=0;
				}
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
    
	static void UcitajPodatke() throws FileNotFoundException
	{	File file = new File("Matrice.txt");
		File file1 = new File("Podaci.txt");
		Scanner matrice = new Scanner(file); 
		Scanner podaci = new Scanner(file1); 
		int a,i,j,k,ind;
		try{
			//brojac:
			for(i=0;i<3;i++)
				for(j=0;j<=i;j++)
				{	matrice.next();
					matrice.next();
					ind=1;
					for(k=0;k<10;k++)
						Igrac.Qbrojac[i][j][k]=matrice.nextInt();
				}
			for(i=3;i<8;i++)
				for(j=0;j<i;j++)
				{	matrice.next();
					matrice.next();
					ind=2;
					for(k=0;k<10;k++)
						Igrac.Qbrojac[i][j][k]=matrice.nextInt();
				}
			for(j=0;j<8;j++)
			{	matrice.next();
				matrice.next();
					ind=3;
				for(k=0;k<10;k++)
					Igrac.Qbrojac[8][j][k]=matrice.nextInt();
			}

			for(i=0;i<3;i++)
				for(j=0;j<=i;j++)
				{	matrice.next();
					matrice.next();
					ind=4;
					for(k=0;k<10;k++)
						Igrac.Qbojabrojac[i][j][k]=matrice.nextInt();
				}
			for(i=3;i<8;i++)
				for(j=0;j<i;j++)
				{	matrice.next();
					matrice.next();
					ind=5;
					for(k=0;k<10;k++)
						Igrac.Qbojabrojac[i][j][k]=matrice.nextInt();
				}
			//matrica prelaza:
			for(i=0;i<3;i++)
				for(j=0;j<=i;j++)
				{	matrice.next();
					matrice.next();
					ind=6;
					for(k=0;k<10;k++)
						Igrac.Q[i][j][k]=matrice.nextDouble();
				}
			for(i=3;i<8;i++)
				for(j=0;j<i;j++)
				{	matrice.next();
					matrice.next();
					ind=7;
					for(k=0;k<10;k++)
						Igrac.Q[i][j][k]=matrice.nextDouble();
				}
			for(j=0;j<8;j++)
			{	matrice.next();
				matrice.next();
					ind=8;
				for(k=0;k<10;k++)
					Igrac.Q[8][j][k]=matrice.nextDouble();
			}

			for(i=0;i<3;i++)
				for(j=0;j<=i;j++)
				{	matrice.next();
					matrice.next();
					ind=9;
					for(k=0;k<10;k++)
						Igrac.Qboja[i][j][k]=matrice.nextDouble();
				}
			for(i=3;i<8;i++)
				for(j=0;j<i;j++)
				{	matrice.next();
					matrice.next();
					ind=10;
					for(k=0;k<10;k++)
						Igrac.Qboja[i][j][k]=matrice.nextDouble();
				}
			
			int flop,karte,v,b,n,n1,po;
			for(a=0;a<10;a++)
				for(karte=0;karte<20;karte++)
					for(b=0;b<20;b++)
						for(n=0;n<5;n++)
							for(po=0;po<4;po++)
								Igrac.Q[karte][b][n][po][a]=matrice.nextDouble();
			int flop,karte,v,b,n,n1,po;
			for(a=0;a<10;a++)
				for(karte=0;karte<20;karte++)
					for(b=0;b<20;b++)
						for(n=0;n<5;n++)
							for(po=0;po<4;po++)
								Igrac.Qbrojac[karte][b][n][po][a]=matrice.nextInt();
			
			//flop,turn i river:
			for(flop=0;flop<20;flop++)
				for(a=0;a<21;a++)
					for(v=0;v<20;v++)
						for(b=0;b<20;b++)
							for(n=0;n<5;n++)
								for(n1=0;n1<4;n1++)
									for(po=0;po<4;po++)
										Igrac.Qflopturnriver[flop][v][b][n][n1][po][a]=podaci.nextDouble();
		}catch(Exception e){
			ind=0;
		}
		matrice.close();
	}
	
	public static void IspisiPodatke() throws IOException
	{	String niz[]={"25","67","89","10"," J"," Q"," K"," A"};
		String niz1[]={"22","66","88","10"," J"," Q"," K"," A"};
		String niz2[]={"55","77","99","10"," J"," Q"," K"," A"};
		FileWriter fileWriter = new FileWriter("Matrice.txt");
		FileWriter fileWriter1 = new FileWriter("Podaci.txt");
		PrintWriter matrice = new PrintWriter(fileWriter);
		PrintWriter podaci = new PrintWriter(fileWriter1);
		int a,i,j,k;
		try{
			//prve 2 karte:
			//brojac:
			for(i=0;i<3;i++)
				for(j=0;j<=i;j++)
				{	matrice.printf("%s %s",niz[i],niz[j]);
					matrice.printf("\t%4d",Igrac.Qbrojac[0][0][0]);
					for(k=1;k<10;k++)
						matrice.printf("\t%4d",Igrac.Qbrojac[i][j][k]);
					matrice.println();
				}
			for(i=3;i<8;i++)
				for(j=0;j<i;j++)
				{	matrice.printf("%s %s",niz[i],niz[j]);
					matrice.printf("\t%4d",Igrac.Qbrojac[0][0][0]);
					for(k=1;k<10;k++)
						matrice.printf("\t%4d",Igrac.Qbrojac[i][j][k]);
					matrice.println();
				}
			for(j=0;j<8;j++)
			{	matrice.printf("%s %s",niz1[j],niz2[j]);
				matrice.printf("\t%4d",Igrac.Qbrojac[0][0][0]);
				for(k=1;k<10;k++)
					matrice.printf("\t%4d",Igrac.Qbrojac[8][j][k]);
				matrice.println();
			}

			for(i=0;i<3;i++)
				for(j=0;j<=i;j++)
				{	matrice.printf("%s %s",niz[i],niz[j]);
					matrice.printf("\t%4d",Igrac.Qbrojac[0][0][0]);
					for(k=1;k<10;k++)
						matrice.printf("\t%4d",Igrac.Qbojabrojac[i][j][k]);
					matrice.println();
				}
			for(i=3;i<8;i++)
				for(j=0;j<i;j++)
				{	matrice.printf("%s %s",niz[i],niz[j]);
					matrice.printf("\t%4d",Igrac.Qbrojac[0][0][0]);
					for(k=1;k<10;k++)
						matrice.printf("\t%4d",Igrac.Qbojabrojac[i][j][k]);
					matrice.println();
				}
			matrice.println();
			//matrica prelaza:
			for(i=0;i<3;i++)
				for(j=0;j<=i;j++)
				{	matrice.printf("%s %s",niz[i],niz[j]);
					matrice.printf("\t%8.4f",Igrac.Q[0][0][0]);
					for(k=1;k<10;k++)
						matrice.printf("\t%8.4f",Igrac.Q[i][j][k]);
					matrice.println();
				}
			for(i=3;i<8;i++)
				for(j=0;j<i;j++)
				{	matrice.printf("%s %s",niz[i],niz[j]);
					matrice.printf("\t%8.4f",Igrac.Q[0][0][0]);
					for(k=1;k<10;k++)
						matrice.printf("\t%8.4f",Igrac.Q[i][j][k]);
					matrice.println();
				}
			for(j=0;j<8;j++)
			{	matrice.printf("%s %s",niz1[j],niz2[j]);
				matrice.printf("\t%8.4f",Igrac.Q[0][0][0]);
				for(k=1;k<10;k++)
					matrice.printf("\t%8.4f",Igrac.Q[8][j][k]);
				matrice.println();
			}

			for(i=0;i<3;i++)
				for(j=0;j<=i;j++)
				{	matrice.printf("%s %s",niz[i],niz[j]);
					matrice.printf("\t%8.4f",Igrac.Q[0][0][0]);
					for(k=1;k<10;k++)
						matrice.printf("\t%8.4f",Igrac.Qboja[i][j][k]);
					matrice.println();
				}
			for(i=3;i<8;i++)
				for(j=0;j<i;j++)
				{	matrice.printf("%s %s",niz[i],niz[j]);
					matrice.printf("\t%8.4f",Igrac.Q[0][0][0]);
					for(k=1;k<10;k++)
						matrice.printf("\t%8.4f",Igrac.Qboja[i][j][k]);
					matrice.println();
				}
			matrice.println();
			
			//flop,turn i river:
			int flop,v,b,n,n1,po;
			for(flop=0;flop<20;flop++)
			{	for(a=0;a<21;a++)
					for(v=0;v<20;v++)
						for(b=0;b<20;b++)
							for(n=0;n<5;n++)
							{	for(n1=0;n1<4;n1++)
									for(po=0;po<4;po++)
										podaci.printf("%f ",Igrac.Qflopturnriver[flop][v][b][n][n1][po][a]);
								podaci.println();
							}
			}
			for(v=0;v<40;v+=2)
			{	double verovatnoca=Igrac.podelaverovatnoca[i];
				matrice.printf("%.2f",verovatnoca);
				for(a=1;a<10;a++)
					matrice.printf("\t%8.4f",Igrac.Qflopturnriver[flop][v][0][10][4][0][a]);
				matrice.println();
			}
			matrice.println();
		}catch(Exception e){
			i=0;
		}
		matrice.close();
		podaci.close();
	}
}
