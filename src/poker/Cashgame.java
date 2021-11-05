package poker;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import java.util.*;
/**
 *
 * @author fpesic
 */
public class Cashgame
{	
	static public int rednibroj=1;
	public int brojigraca;
	public int buyin;
	public int bigblind;
	
	public Cashgame()
	{	brojigraca=0;
		buyin=0;
		bigblind=0;
	}
	
	public Cashgame(int n,int b,int bb)
	{	brojigraca=n;
		buyin=b;
		bigblind=bb;
	}
	
	public Cashgame(int n)
	{	brojigraca=n;
		bigblind=10;
		buyin=bigblind*100;
	}
	
	public void dodajigraca(List<Igrac> igraci,int i)
	{	String ime="igrac "+rednibroj;
		Igrac novi;
	/*	if(rednibroj%2==0)
			novi=new Igrac(buyin,ime,buyin,Main.tridana);
		else
	*/		novi=new Igrac(buyin,ime,buyin,Main.svevreme);
		igraci.add(novi);
		rednibroj++;
	}
	
	public void igraj(int pracenje,double n) throws IOException, Exception
	{	int smallblind=bigblind/2,dugme=brojigraca-2,I;
		int timsvevreme=0,timtridana=0;
		List<Igrac> igraci=new ArrayList<>();
		System.out.printf("%d igraca, pocetni kapital %d$\n",brojigraca,buyin);
		//wcout<<brojigraca<<" igraca, pocetni kapital "<<ulog<<"$"<<endl;
		for(I=0;I<brojigraca;I++)
			dodajigraca(igraci,I);
		int rruka=1;
		for(int i=0;i<n;i++)
		{	int ii=25000;
			if(i%ii==ii-1)
			{
				//Neuronska_mreza.mreza.train(0);
				//Neuronska_mreza.mreza.train(1);
				//Neuronska_mreza.mreza.train(2);
				//Ruka.ispisiistoriju(0);
				//Ruka.ispisiistoriju(1);
				//Ruka.ispisiistoriju(2);
				Main.svevreme.IspisiPodatke("");
			}
			dugme++;
			int r=1000;
			if(pracenje==1)
				System.out.printf("\n\nRUKA %d\n",rruka);
			else if(i%r==r-1)
				System.out.printf("\n\nRUKA %d\n",rruka);
			rruka++;
			if(pracenje>0)
				for(Igrac igrac:igraci)
					System.out.println(igrac);
			/*if(pracenje==2)
				System.in.read();
				//getchar();
			else
				System.out.println();*/
			while(dugme>=(int)igraci.size())
				dugme-=(int)igraci.size();
			Ruka ruka=new Ruka(igraci,dugme,smallblind,bigblind,pracenje);
		//	if(igraci.size()<5||igraci.get(0).novac==0&&igraci.get(1).novac==0&&igraci.get(2).novac==0&&igraci.get(3).novac==0&&igraci.get(4).novac==0)
		//		dugme=dugme;
		//	else
			igraci=ruka.igraj();
			i+=0;
			while(true)
			{	for(int j=0;j<igraci.size();)
				{	Igrac igrac=igraci.get(j);
					if(igrac.novac<100)
						igraci.remove(j);
					else if(igrac.novac>5*buyin)
					{	igraci.remove(j);
						if(igrac.podaci.id.equals("svevreme"))
							timsvevreme+=buyin*5;
						else
							timtridana+=buyin*5;
					}
					else
						j++;
				}
				for(int j=igraci.size()+1;j<=brojigraca;j++)
					dodajigraca(igraci,j);
				if(igraci.size()<5||igraci.get(0).novac==0&&igraci.get(1).novac==0&&igraci.get(2).novac==0&&igraci.get(3).novac==0&&igraci.get(4).novac==0)
					//dugme=dugme;
					break;
				else
					break;
			}
		}
	}
}